package MantraMeClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class MantraGetter{

	private static Mantra currentMantra = null;	
	private static List<Mantra> allMantras = new LinkedList<Mantra>();

	public final static String ALLMANTRASFILENAME = "AllMantrasFile.txt";
	public final static String CURRENTMANTRASFILENAME = "CURRENTMANTRAS.txt";

	public static Context context;
	public static ConnectivityManager connectivityManager;

	public MantraGetter(){				
	}

	public void getAllMantrasFromServer() {

		Log.w("SHARON" , "getAllMantrasFromServer");

		GetAllMantrasAction action = new GetAllMantrasAction();
		action.connectivityManager = connectivityManager;
		action.execute();	

		try {
			action.get(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		if (action.failureServer){
			Log.w("SHARON" , "failureServer mantra getter");

		}else{
			allMantras = action.mantras;

			// Save the mantras to file
			writeAllMantrasToFile(allMantras);
		}
	}

	private static void writeCurrentMantraToFile(Mantra man) {		

		Log.w("SHARON" , "write Current Mantra To File");

		String json = "";
		Gson gson = new Gson();
		json = gson.toJson(man);

		writeToFile(json, CURRENTMANTRASFILENAME); 
		Log.w("SHARON" , "write Current Mantra To File DONE");		
	}

	private void writeAllMantrasToFile(List<Mantra> mantraList) {

		Log.w("SHARON" , "writeAllMantrasToFile");

		String allData = "";
		for (Mantra m : mantraList) {		
			String json = "";

			Gson gson = new Gson();
			json = gson.toJson(m);

			allData += json + "\n";
		}

		writeToFile(allData, ALLMANTRASFILENAME); 
		Log.w("SHARON" , "writeAllMantrasToFile DONE");
	}

	private static void writeToFile(String allData, String fileName) {

		if (context == null){
			return;
		}

		FileOutputStream fos;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(allData.getBytes());
			fos.close();	
		} catch (FileNotFoundException e) {
			Log.w("SHARON" , "writeToFile Failed : "  + e);  
		} catch (IOException e) {
			Log.w("SHARON" , "writeToFile Failed : "  + e);  
		}	
	}

	private static Mantra readCurrentMantraFromFile() {

		if (context == null){
			return null;
		}

		FileInputStream fis = null;
		Mantra currentMantra = null;

		try {
			fis = context.openFileInput(CURRENTMANTRASFILENAME);
			String collected = null;

			byte[] data = new byte[fis.available()];

			while (fis.read(data) != -1){
				collected = new String(data);
				Log.w("SHARON" , "readCurrentMantraFromFile fis.read(data) , collected : "  + collected);  				
			}

			JSONObject msgAsJson = new JSONObject(collected);
			currentMantra = ServerDataBaseManager.getMantraFromJson(msgAsJson);				

		} catch (IOException e) {
			Log.w("SHARON" , "readCurrentMantraFromFile Failed : "  + e);  
		} catch (JSONException e) {
			Log.w("SHARON" , "readCurrentMantraFromFile Failed : "  + e);  
		} catch (Exception e) {
			Log.w("SHARON" , "readCurrentMantraFromFile Failed : "  + e);  
		}
		finally{
			try {
				fis.close();
			} catch (IOException e) {
				Log.w("SHARON" , "readCurrentMantraFromFile fis.close() Failed : "  + e);  
			}
		}
		return currentMantra;
	}

	public static ArrayList<Mantra> readMantrasFromFile() {

		if (context == null){
			return null;
		}

		ArrayList<Mantra> allMan = new ArrayList<Mantra>();		

		FileInputStream fis = null;
		try {
			fis = context.openFileInput(ALLMANTRASFILENAME);
			String collected = null;

			byte[] data = new byte[fis.available()];

			while (fis.read(data) != -1){
				collected = new String(data);
				Log.w("SHARON" , "fis.read(data) , collected : "  + collected);  				
			}

			String[] mantrasStr = collected.split("\\n");

			for (String line: mantrasStr) {

				JSONObject msgAsJson;
				try {
					msgAsJson = new JSONObject(line);
					Mantra m = ServerDataBaseManager.getMantraFromJson(msgAsJson);					
					allMan.add(m);
					Log.w("SHARON" , "readMantrasFromFile - mantra added = " + m.Description);  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			Log.w("SHARON" , "writeToFile Failed : "  + e);  
		} catch (IOException e) {
			Log.w("SHARON" , "writeToFile Failed : "  + e);  
		}	
		finally{
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return allMan;
	}

	public static Boolean next(){
		
		Log.w("SHARON" , "MangtraGetter.next()");
		
		if (allMantras == null || allMantras.size() == 0){

			Log.w("SHARON" , "allMantras == null");

			// Try re-reading the mantras from file
			allMantras = readMantrasFromFile();

			if (allMantras == null || allMantras.size() == 0){
				Log.w("SHARON" , "allMantras == null , after file read");
				return false;
			}
		}

		Mantra man = null;
		int count = 0;
		Random r = new Random();

		if (currentMantra == null){
			int indexRandom = r.nextInt(allMantras.size());
			man = allMantras.get(indexRandom);	
		}
		else{
			while ((man == null || man.Id.equals(currentMantra.Id)) && count++ < 100){
				
				Log.w("SHARON" , "MangtraGetter.next() getting new mantra");
				
				int indexRandom = r.nextInt(allMantras.size());
				man = allMantras.get(indexRandom);
			}
		}

		currentMantra = man;

		// write mantra to file
		writeCurrentMantraToFile(man);

		return true;
	}


	public static Mantra getCurrentMantra(){

		Log.w("SHARON" , "getCurrentMantra");

		if (currentMantra == null){

			currentMantra = readCurrentMantraFromFile();
			if (currentMantra == null){

				Log.w("SHARON" , "currentMantra == null");
				Mantra defaultMantra = new Mantra("This is the default Mantra", "MantraMe", "1");
				return defaultMantra ;
			}
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

		public boolean isURLReachable(String urlString) {

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
