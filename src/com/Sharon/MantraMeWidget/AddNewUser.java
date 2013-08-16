package com.Sharon.MantraMeWidget;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.mantrame.R;

public class AddNewUser extends Activity{

	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewuser);
	}

	public void loginClicked(View view) {		

		EditText editTextName = (EditText) findViewById(R.id.editTextPersonName);
		EditText editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
		SeekBar seekBarEducation = (SeekBar)findViewById(R.id.seekBarEducation);
		SeekBar seekBarNewAge = (SeekBar)findViewById(R.id.seekBarNewAge);
		SeekBar intrestSport = (SeekBar)findViewById(R.id.seekBarSport);
		SeekBar seekBarHealth = (SeekBar)findViewById(R.id.seekBarHealth);

		// Update widget info
		String name = editTextName.getText().toString();
		String email = editTextEmail.getText().toString();

		// Set the seek bars 
		int education = seekBarEducation.getProgress();
		int newAge = seekBarNewAge.getProgress();
		int sport = intrestSport.getProgress();
		int health = seekBarHealth.getProgress();

		Log.w("111111" , "name = " + name + ". email = " + email);

		UserProfile user = new UserProfile(name, email); 
		user.SetInterst(education, newAge, sport, health);

		addNewUserToServer(user);

		UserProfile.userProfileUsed = user;

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
			pDialog = new ProgressDialog(AddNewUser.this);
			pDialog.setMessage("Attempting connection...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Log.w("77777777777" , "addNewUser doInBackground");
			return ServerDataBaseManager.addUser(usr, "pass");
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			Log.w("77777777777" , "addNewUser onPostExecute");
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(AddNewUser.this, file_url, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(AddNewUser.this, "Problem", Toast.LENGTH_LONG).show();
			}
		}		
	}
}
