package com.Sharon.MantraMeWidget;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import MantraMeClasses.MantraGetter;
import MantraMeClasses.ServerDataBaseManager;
import MantraMeClasses.UserProfile;
import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mantrame.R;

public class WidgetConfigPage extends Activity {

	private static int REQUESTCODE = 1;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private AppWidgetManager awm;
	private Context context;

	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widgetconfigpage);
		
		// Set context
		context = WidgetConfigPage.this;
		
		MantraGetter.context = context;

		// Get and Set the widget id
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// Set AppWidgetManager
		awm = AppWidgetManager.getInstance(context);
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


	public void onAnonymousClicked(View view){
		
		//ProgressDialog dialog = ProgressDialog.show(WidgetConfigPage.this, "", "Loading...", true);
		
		UserProfile.userProfileUsed = null;	

		updateMantras();
		
		end();
	}
	
	public void onLoginRegisteredUserClicked(View view){

		String mail = ((EditText) findViewById(R.id.user_mail)).getText().toString();
		String pass = ((EditText) findViewById(R.id.user_password)).getText().toString();

		if (mail.isEmpty() || pass.isEmpty()){
			Toast.makeText(WidgetConfigPage.this, "Enter email and password", Toast.LENGTH_LONG).show();
			return;
		}		
		
		Log.w("WidgetConfigPage" , "onLoginRegisteredUserClicked mail " + mail + " pass " + pass);

		UserProfile user = getUserByMailPass(mail, pass);
		
		if (user == null){
			Toast.makeText(WidgetConfigPage.this, "Failed to login", Toast.LENGTH_LONG).show();
			((EditText) findViewById(R.id.user_mail)).setText("");
			((EditText) findViewById(R.id.user_password)).setText("");
		}else{	

			Log.w("WidgetConfigPage" , "user " + user.toString());
			
			// Set our user
			UserProfile.userProfileUsed = user;	
			updateMantras();
			end();
		}
	}

	public void updateMantras(){
		SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
		if (isFirstRun)
		{
			Log.w("updateMantras" , "first Run");
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);		
			MantraGetter getter = new MantraGetter();
			getter.connectivityManager = cm;		
			getter.getAllMantrasFromServer();
		}else{
			Log.w("updateMantras" , "Not first Run");
		}
			
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

	private UserProfile getUserByMailPass(String mail, String password) {	

		Log.w("SHARON" , "checkConnection true");

		LoginUser action = new LoginUser();
		action.context = this;
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
		public boolean failure = false;
		public Context context;

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
			
			if (!isURLReachable(context, ServerDataBaseManager.URL_LOGIN)){
				return null;
			}			
			
			user = ServerDataBaseManager.getUser(mail, password);
			if (user == null) return "null";
			return user.toString();
		}

		protected void onPostExecute(String file_url) {
			Log.w("LoginUser" , "onPostExecute");
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(WidgetConfigPage.this, file_url, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(WidgetConfigPage.this, "Problem login user", Toast.LENGTH_LONG).show();
				//Toast.makeText(WidgetConfigPage.this, "Loged in as anonymous", Toast.LENGTH_LONG).show();
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

