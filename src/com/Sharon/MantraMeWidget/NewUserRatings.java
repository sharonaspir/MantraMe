package com.Sharon.MantraMeWidget;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.mantrame.R;

import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
			user.SetInterst(education, newAge, sport, health);

			addNewUserToServer(user);

		}catch(Exception e){
			Log.w("111111" , "Exception in NewUserRatings.doneClicked(). \n " + e);
		}

		finish();
	}

	private void addNewUserToServer(UserProfile user) {		
		addNewUser action = new addNewUser();
		action.usr = user;
		action.execute();		

		try {
			action.get(20, TimeUnit.SECONDS);
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
	}

	// This class will add user via our url server
	public class addNewUser extends AsyncTask<String, String, String> {

		public UserProfile usr = null;
		boolean failure = false;

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
			return ServerDataBaseManager.addUser(usr);
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			Log.w("77777777777" , "addNewUser onPostExecute");
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(NewUserRatings.this, file_url, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(NewUserRatings.this, "Problem", Toast.LENGTH_LONG).show();
			}
		}		
	}
}
