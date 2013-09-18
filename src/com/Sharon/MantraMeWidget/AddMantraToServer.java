package com.Sharon.MantraMeWidget;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import MantraMeClasses.Mantra;
import MantraMeClasses.ServerDataBaseManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddMantraToServer extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mantra_to_server);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_mantra_to_server, menu);
		return true;
	}

	public void doneAddingMantraClicked(View view) {

		// Set the views
		EditText mantra_desc = (EditText) findViewById(R.id.addMantraDesc);
		EditText mantra_author = (EditText) findViewById(R.id.addMantraAuthor);
		SeekBar seekBarEducation = (SeekBar)findViewById(R.id.seekBarMantraEducation);
		SeekBar seekBarNewAge = (SeekBar)findViewById(R.id.seekBarMantraNewAge);
		SeekBar intrestSport = (SeekBar)findViewById(R.id.seekBarMantraSport);
		SeekBar seekBarHealth = (SeekBar)findViewById(R.id.seekBarMantraHealth);

		// Set the seek bars 
		int education = seekBarEducation.getProgress();
		int newAge = seekBarNewAge.getProgress();
		int sport = intrestSport.getProgress();
		int health = seekBarHealth.getProgress();		

		String desc = mantra_desc.getText().toString();
		String author = mantra_author.getText().toString();

		if (desc.isEmpty()){
			Toast.makeText(AddMantraToServer.this, "Please enter mantra!", Toast.LENGTH_LONG).show();
			return;
		}

		if (author.isEmpty()){
			Toast.makeText(AddMantraToServer.this, "Please enter author!", Toast.LENGTH_LONG).show();
			return;
		}

		UUID idOne = UUID.randomUUID();
		String id = idOne.toString();

		Mantra mant = new Mantra(desc, author, id);

		Date date = new Date();
		mant.SetCreationDate(date);

		mant.SetRelevents(sport, education, newAge, health);

		Toast.makeText(AddMantraToServer.this, "Adding mantra to server", Toast.LENGTH_LONG).show();

		if (addMantraToServer(mant)){
			Toast.makeText(AddMantraToServer.this, "Mantra added", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(AddMantraToServer.this, "Failed to upload mantra", Toast.LENGTH_LONG).show();
		}

		finish();
	}

	private boolean addMantraToServer(Mantra mant) {
		addMantraToServerAsync action = new addMantraToServerAsync();
		action.mant = mant;
		action.context = this;
		action.execute();	

		try {
			action.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		} catch (TimeoutException e) {
			e.printStackTrace();
			return false;
		}

		return action.succses;		
	}

	public class addMantraToServerAsync extends AsyncTask<String, String, String> {

		public Mantra mant;
		public Context context;
		public boolean succses;

		@Override
		protected String doInBackground(String... arg0) {

			Log.w("addMantraToServerAsync" , "doInBackground");

			if (!isURLReachable(context, ServerDataBaseManager.URL_ADDMANTRA)){
				Log.w("77777777777" , "isURLReachable false");
				succses = false;
				return null;
			}	

			ServerDataBaseManager.addMantra(mant);
			
			succses = true;
			return null;
		}

		public boolean isURLReachable(Context context, String urlString) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {
				try {

					URL url = new URL(urlString);  
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(10 * 1000);          // 10 s.
					urlc.connect();	            

					if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
						Log.wtf("Connection", "Success !");
						return true;
					} else {
						return false;
					}
				} catch (MalformedURLException e1) {
					return false;
				} catch (IOException e) {
					return false;
				}catch (Exception e) {
					Log.wtf("SHARON", "e " + e);
					return false;
				}
			}
			return false;
		}	

	}


}
