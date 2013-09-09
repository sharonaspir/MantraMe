package MantraMeClasses;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class MantraGetter{

	private static Mantra currentMantra = null;	
	private static List<Mantra> allMantras = new LinkedList<Mantra>();
	
	public static ConnectivityManager connectivityManager;

	public MantraGetter(){				
	}

	public void getAllMantrasFromServer() {
		GetAllMantrasAction action = new GetAllMantrasAction();
		action.connectivityManager = connectivityManager;
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
		
		if (action.failureServer){
			Log.w("SHARON" , "failureServer mantra getter");
			
		}else{
			allMantras = action.mantras;
		}
	}

	public static void next(){
		if (allMantras == null || allMantras.size() == 0){
			return;
		}

		Mantra man = null;
		int count = 0;
		Random r = new Random();

		if (currentMantra == null){
			int indexRandom = r.nextInt(allMantras.size());
			man = allMantras.get(indexRandom);	
		}
		else{
			while ((man == null || man.mantra_id.equals(currentMantra.mantra_id)) && count++ < 100){
				int indexRandom = r.nextInt(allMantras.size());
				man = allMantras.get(indexRandom);
			}
		}

		currentMantra = man;
	}

	public static Mantra getCurrentMantra(){
		
		Log.w("SHARON" , "getCurrentMantra");
		
		if (currentMantra == null){
			Log.w("SHARON" , "currentMantra == null");
			Mantra defaultMantra = new Mantra("This is the default Mantra", "me", "1");
			return defaultMantra ;
		}
		
		String mant = currentMantra.man_str;
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
			currentMantra.man_str = mant;			
		}
		
		return currentMantra;
	}

	public class GetAllMantrasAction extends AsyncTask<String, String, String> {

		public ConnectivityManager connectivityManager;
		List<Mantra> mantras;
		public boolean failureServer = false;

		protected void onPreExecute() {
			Log.w("getAllMantrasAction" , "onPreExecute");
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
			Log.w("getAllMantrasAction" , "onPostExecute");
		}		

		public boolean isURLReachable(  String urlString) {

			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

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
