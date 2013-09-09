package com.Sharon.MantraMeWidget;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.mantrame.R;

import MantraMeClasses.MantraGetter;
import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class NewUserRatings extends Activity {


	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user_ratings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user_ratings, menu);
		return true;
	}


	public void doneClicked(View view) {	

		Log.w("111111" , "doneClicked");
		boolean succses = false;
		
		try{
			// Set UI
			SeekBar seekBarEducation = (SeekBar)findViewById(R.id.seekBarEducation);
			SeekBar seekBarNewAge = (SeekBar)findViewById(R.id.seekBarNewAge);
			SeekBar intrestSport = (SeekBar)findViewById(R.id.seekBarSport);
			SeekBar seekBarHealth = (SeekBar)findViewById(R.id.seekBarHealth);

			// Set the seek bars 
			int education = seekBarEducation.getProgress();
			int newAge = seekBarNewAge.getProgress();
			int sport = intrestSport.getProgress();
			int health = seekBarHealth.getProgress();

			UserProfile user = UserProfile.userProfileUsed ;
			if (user != null){
				user.SetInterst(education, newAge, sport, health);
				succses = addNewUserToServer(user);
			}

		}catch(Exception e){
			Log.w("111111" , "Exception in NewUserRatings.doneClicked(). \n " + e);
		}

		if (succses){

			Log.w("SHARON" , "succses to connect to server");
			updateMantras();
			finish();
		}
		else{

			Log.w("SHARON" , "Failed to connect to server");
			
			// the user is null now
			UserProfile.userProfileUsed = null;
			
			Toast.makeText(NewUserRatings.this, "Failed connecting to server", Toast.LENGTH_LONG).show();
			
			try
			{
				Log.w("SHARON" , "try to start activity");
				Intent k = new Intent(getApplicationContext(), WidgetConfigPage.class);
				startActivity(k);
			}catch(Exception e){
				Log.w("WidgetConfigPage" , "Exception! \n" + e);
			}
			finish();
		}
		
		
	}

	public void updateMantras(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);		
		MantraGetter getter = new MantraGetter();
		getter.connectivityManager = cm;		
		getter.getAllMantrasFromServer();	
	}
	
	private boolean addNewUserToServer(UserProfile user) {		
		addNewUser action = new addNewUser();
		action.usr = user;
		action.execute();	
		action.context = this;

		try {
			action.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return !action.URLNotReachable;		
	}

	// This class will add user via our url server
	public class addNewUser extends AsyncTask<String, String, String> {

		public UserProfile usr = null;
		boolean URLNotReachable = false;
		public Context context;

		protected void onPreExecute() {
			Log.w("77777777777" , "addNewUser onPreExecute");
			super.onPreExecute();
			pDialog = new ProgressDialog(NewUserRatings.this);
			pDialog.setMessage("Attempting connection...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {			
			
			Log.w("77777777777" , "addNewUser doInBackground");
			
			if (!isURLReachable(context, ServerDataBaseManager.URL_ADDUSER)){
				Log.w("77777777777" , "isURLReachable false");
				URLNotReachable = true;
				return null;
			}			
			
			return ServerDataBaseManager.addUser(usr);
		}

		protected void onPostExecute(String file_url) {
			Log.w("77777777777" , "addNewUser onPostExecute");
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(NewUserRatings.this, file_url, Toast.LENGTH_LONG).show();
			}
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
