package com.Sharon.MantraMeWidget;

import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mantrame.R;

public class Testing extends Activity{


	// Progress Dialog
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.testing);
	}


	public void buttonAddUserClicked(View view){
		addNewUser action = new addNewUser();
		action.usr = new UserProfile(null, null);
		action.execute();
	}


	// This class will add user via our url server
	public class addNewUser extends AsyncTask<String, String, String> {

		public UserProfile usr = null;
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Testing.this);
			pDialog.setMessage("Attempting connection...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			return ServerDataBaseManager.addUser(null, "pass");
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(Testing.this, file_url, Toast.LENGTH_LONG).show();
			}
		}		
	}
}


