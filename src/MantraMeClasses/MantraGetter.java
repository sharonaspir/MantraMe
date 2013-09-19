package MantraMeClasses;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class MantraGetter{
	
	private final String user = "currentUser";
	private final String keyTime = "readFroServerTimeStamp";
	
	// public static Context context;
	public static ConnectivityManager connectivityManager;

	public MantraGetter(){				
	}

	public void getAllMantrasFromServer(Context context) {

		Log.w("MantraGetter" , "getAllMantrasFromServer");

		if (!checkShouldDownloadAgain(context)){
			return;
		}

		GetAllMantrasAction action = new GetAllMantrasAction();
		action.connectivityManager = connectivityManager;
		action.execute();	

		try {
			action.get(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Log.wtf("MantraGetter", "InterruptedException " + e);;
		} catch (ExecutionException e) {
			Log.wtf("MantraGetter", "ExecutionException " + e);
		} catch (TimeoutException e) {
			Log.wtf("MantraGetter", "TimeoutException " + e);
		}

		if (action.failureServer){
			Log.w("MantraGetter" , "failureServer mantra getter");

		}else{
			List<Mantra> allMantras = action.mantras;

			deleteAllMantras(context);
			addMantraToLocalTable(context, allMantras);
		}
	}

	private boolean checkShouldDownloadAgain(Context context) {

		Log.w("MantraGetter" , "checkShouldDownloadAgain");

		Date  now = new Date();

		String dateStamp = readLatestUpdateTimeFromSharedPreferences(context);

		Log.w("MantraGetter" , "dateStamp = " + dateStamp);

		if (dateStamp.isEmpty()){

			Log.w("MantraGetter" , "dateStamp empty");
			
			writeTimeToSharedPreferences(context, now);			

			// we need to download
			return true;

		}else{

			
			// check time diff
			try {

				Log.w("MantraGetter" , "lastUpdate parsing : " + dateStamp);
				
				Date dateSaved = Mantra.mantraDateFormat.parse(dateStamp);

				Log.w("MantraGetter" , "lastUpdate parsed");
				
				long lastUpdate = dateSaved.getTime();				
				
				long difference = now.getTime() - lastUpdate;

				Log.w("MantraGetter" , "difference milliseconds: " + difference);
				
				double allowGapInMinutes = 0.5;
				
				// Check last allowGapInMinutes
				if (difference < (1000 * 60 * allowGapInMinutes)){
					// no need to update 
					Log.w("MantraGetter" , "no need to update . dif = " + difference);
					return false;
				}
				else{
					// update date stamp and return true
					writeTimeToSharedPreferences(context, now);			

					// we need to download
					return true;
				}
			} catch (ParseException e) {
				Log.wtf("MantraGetter" , "ParseException : " + e);
				return false;
			}
			
			
		}
	}

	private String readLatestUpdateTimeFromSharedPreferences(Context context) {
		// READ Latest server read		
		SharedPreferences sharedPref = context.getSharedPreferences(user, Context.MODE_PRIVATE);
		String dateStamp = sharedPref.getString(keyTime, "");
		return dateStamp;
	}

	private void writeTimeToSharedPreferences(Context context, Date now) {
		
		SharedPreferences sharedPref = context.getSharedPreferences(user, Context.MODE_PRIVATE);		
		SharedPreferences.Editor editor = sharedPref.edit();
		
		editor.putString(keyTime, Mantra.mantraDateFormat.format(now));
		editor.commit();
	}

	private static void writeCurrentMantraIDToFile(String id,Context context) {		

		Log.w("MantraGetter" , "writeCurrentMantraIDToFile id " + id);

		String user = "currentUser";
		SharedPreferences sharedPref = context.getSharedPreferences(user, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("currentMantraId", id);

		editor.commit();
		Log.w("MantraGetter" , "SharedPreferences writeCurrentMantraIDToFile.commit();");
	}

	private static Mantra readCurrentMantraFromFile(Context context) {

		if (context == null){
			return null;
		}

		String user = "currentUser";
		SharedPreferences sharedPref = context.getSharedPreferences(user, Context.MODE_PRIVATE);
		String id = sharedPref.getString("currentMantraId", "-1");

		Mantra current =  getMantraFromId(context, id);

		//Log.w("SHARON" , "SharedPreferences read , current : "  + current.toString());  	

		return current;
	}

	public static Boolean next(Context context){

		Log.w("MantraGetter" , "MangtraGetter.next()");

		List<Mantra> allMantras = getAllMantrasFromLocalTable(context);

		if (allMantras == null || allMantras.size() == 0){
			return false;
		}

		Mantra man = null;
		int count = 0;
		Random r = new Random();

		Mantra currentMantra = readCurrentMantraFromFile(context);

		if (currentMantra == null){
			int indexRandom = r.nextInt(allMantras.size());
			man = allMantras.get(indexRandom);	
		}
		else{
			while ((man == null || man.Id.equals(currentMantra.Id)) && count++ < 100){

				Log.w("MantraGetter" , "MangtraGetter.next() getting new mantra");

				int indexRandom = r.nextInt(allMantras.size());
				man = allMantras.get(indexRandom);
			}
		}

		currentMantra = man;

		// write mantra id to file
		writeCurrentMantraIDToFile(man.Id,context);

		return true;
	}

	public static Mantra getCurrentMantra(Context context){

		Log.w("MantraGetter" , "getCurrentMantra");

		Mantra currentMantra = readCurrentMantraFromFile(context);

		if (currentMantra == null){
			Log.w("MantraGetter" , "currentMantra == null");
			Mantra defaultMantra = new Mantra("This is the default Mantra", "MantraMe", "1");
			return defaultMantra ;

		}

		String mant = currentMantra.Description;
		Boolean changed = false;

		if (!Character.isUpperCase(mant.charAt(0))){
			String upper = mant.substring(0, 1).toUpperCase() + mant.substring(1);
			mant = upper;
			changed = true;
		}

		if (!((mant.charAt((mant.length()) - 1)) == '.')){
			mant += ".";
			changed = true;
		}

		if (changed){
			currentMantra.Description = mant;			
		}

		return currentMantra;
	}

	public class GetAllMantrasAction extends AsyncTask<String, String, String> {

		public ConnectivityManager connectivityManager;
		List<Mantra> mantras;
		public boolean failureServer = false;

		protected void onPreExecute() {
			Log.w("MantraGetter" , "onPreExecute");
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {

			if (connectivityManager == null || !isURLReachable(ServerDataBaseManager.URL_GETALLMANTRAS)){
				failureServer = true;
				return null;
			}	

			mantras = ServerDataBaseManager.getAllMantras();	

			return null;
		}

		protected void onPostExecute(String file_url) {
			Log.w("MantraGetter" , "onPostExecute");
		}		

		public boolean isURLReachable(String urlString) {

			Log.wtf("MantraGetter", "isURLReachable");
			
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL(urlString);  
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(10 * 1000);          // 10 s.
					urlc.connect();	            

					if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
						Log.wtf("MantraGetter", "Success !");
						return true;
					} else {
						Log.wtf("MantraGetter", "urlc.getResponseCode NOT OK");
						return false;
					}
				} catch (MalformedURLException e1) {
					Log.wtf("MantraGetter" , "MalformedURLException : " + e1);
					return false;
				} catch (IOException e) {
					Log.wtf("MantraGetter" , "IOException : " + e);
					return false;
				}catch (Exception e) {
					Log.wtf("MantraGetter", "Exception " + e);
					return false;
				}
			}
			return false;
		}	
	}

	public static void addMantraToLocalTable(Context context, List<Mantra> mant) {
		Log.w("MantraGetter", "addMantraToLocalTable");		

		DataBaseManager dbm = new DataBaseManager(context);		
		dbm.AddMantra(mant);
	}	

	public static void addMantraToLocalTable(Context context, Mantra mant) {
		Log.w("MantraGetter", "addMantraToLocalTable");		

		DataBaseManager dbm = new DataBaseManager(context);		
		dbm.AddMantra(mant);
	}	

	public static Mantra getMantraFromId(Context context, String id) {
		Log.w("MantraGetter", "getMantraFromId");		

		DataBaseManager dbm = new DataBaseManager(context);		
		return dbm.getMantraById(id);
	}

	public static List<Mantra> getAllMantrasFromLocalTable(Context context) {
		Log.w("MantraGetter", "getAllMantrasFromLocalTable");

		DataBaseManager dbm = new DataBaseManager(context);	
		List<Mantra> all = dbm.GetAllMantra();

		return all;
	}

	private void deleteAllMantras(Context context) {

		Log.wtf("MantraGetter", "deleteAllMantras");

		DataBaseManager dbm = new DataBaseManager(context);	
		dbm.deleteAllMantras();		
	}
}
