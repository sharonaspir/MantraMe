package com.Sharon.MantraMeWidget;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.Sharon.MantraMeWidget.AddNewUser.addNewUser;
import com.example.mantrame.R;

public class WidgetConfigPage extends Activity {

	private static int REQUESTCODE = 1;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private AppWidgetManager awm;
	private Context c;

	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widgetconfigpage);

		// Set context
		c = WidgetConfigPage.this;

		// Get and Set the widget id
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// Set AppWidgetManager
		awm = AppWidgetManager.getInstance(c);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		Log.w("WidgetConfigPage" , "onActivityResult. requestCode " + requestCode + " resultCode" + resultCode);

		if (requestCode == REQUESTCODE ) {
			end();
		}
	}

	public void onNewUserClicked(View view){
		try
		{
			Intent k = new Intent(getApplicationContext(), AddNewUser.class);
			startActivityForResult(k,REQUESTCODE);
		}catch(Exception e){
			Log.w("WidgetConfigPage" , "Exception! \n" + e);
		}	
	}

	public void onLoginRegisteredUserClicked(View view){

		String mail = ((EditText) findViewById(R.id.user_mail)).getText().toString();
		String pass = ((EditText) findViewById(R.id.user_password)).getText().toString();

		Log.w("WidgetConfigPage" , "onLoginRegisteredUserClicked mail " + mail + " pass " + pass);

		UserProfile user = addNewUserToServer(mail, pass);
		
		// Set our user
		UserProfile.userProfileUsed = user;
				
		if (user != null){ 
			Log.w("WidgetConfigPage" , "user " + user.toString());
		}

		end();
	}

	public void end(){
		// Update the widget info
		Intent updateWidget = new Intent(getApplicationContext(), MantraMeWidget.class);
		updateWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] ids = { mAppWidgetId };
		updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		sendBroadcast(updateWidget);

		// Set the activity result as Ok 
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);	
		finish();
	}

	private UserProfile addNewUserToServer(String mail, String password) {		
		LoginUser action = new LoginUser();
		action.mail = mail;
		action.password = password;
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

		return action.user;
	}

	// This class will add user via our url server
	public class LoginUser extends AsyncTask<String, String, String> {

		public UserProfile user = null;
		public String password;
		public String mail;
		boolean failure = false;

		protected void onPreExecute() {
			Log.w("LoginUser" , "onPreExecute");
			super.onPreExecute();
			pDialog = new ProgressDialog(WidgetConfigPage.this);
			pDialog.setMessage("Attempting connection...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Log.w("LoginUser" , "doInBackground");
			user = ServerDataBaseManager.getUser(mail, password);
			if (user == null) return "null";
			return user.toString();
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			Log.w("LoginUser" , "onPostExecute");
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(WidgetConfigPage.this, file_url, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(WidgetConfigPage.this, "Problem", Toast.LENGTH_LONG).show();
			}
		}		
	}
}

