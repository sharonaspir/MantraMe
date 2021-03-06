package MantraMeClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerDataBaseManager{

	static JSONParser jsonParser = new JSONParser();
	public static final String SERVER_IP 			= "192.168.1.68";
	public static final String URL_ADDUSER 			= "http://" + SERVER_IP + "/MyMantra/addUser.php";
	public static final String URL_LOGIN			= "http://" + SERVER_IP + "/MyMantra/getUserByEMailAndPassword.php";
	public static final String URL_GETALLMANTRAS 	= "http://" + SERVER_IP + "/MyMantra/getAllMantras.php";
	public static final String URL_ADDMANTRA 		= "http://" + SERVER_IP + "/MyMantra/addMantra.php";

	public static final String TAG_SUCCESS = "success";
	public static final String TAG_MESSAGE = "message";

	// USERS

	// Gets the user saved on the sql server, with mail and password as inputed
	public static UserProfile getUser(String mail, String password){

		Log.w("ServerDataBaseManager" , "mail " + mail + " pass " + password);

		if ( password.equals("") || password.equals("")){
			return null;
		}
		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("email", mail));

			Log.d("ServerDataBaseManager", "getUser starting , " + URL_LOGIN);
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_LOGIN, "POST", params);

			// check your log for json response
			Log.d("ServerDataBaseManager", json.toString());

			success = json.getInt(TAG_SUCCESS);			

			if (success == 1) {				
				UserProfile user = GetUserFromJson(json);

				return user;				
			}else{
				Log.d("ServerDataBaseManager", json.getString(TAG_MESSAGE));				

				//String n = json.getString("Name");		

				return null;
			}
		} catch (JSONException e) {
			Log.wtf("ServerDataBaseManager", "JSONException " + e);
		} catch (Exception e) {
			Log.wtf("ServerDataBaseManager", "Exception\n" + e);
		}

		return null;		
	}

	// Gets a UserProfile from user info containing json
	private static UserProfile GetUserFromJson(JSONObject json) {
		Log.w("ServerDataBaseManager", "starting GetUserFromJson");

		try {
			String msg = json.getString(TAG_MESSAGE);
			//Log.w("GetUserFromJson()", "msg " + msg);
			JSONObject msgAsJson = new JSONObject(msg);

			String name = msgAsJson.getString("Name");			
			String id = msgAsJson.getString("Id");
			String email = msgAsJson.getString("Email");
			int education = msgAsJson.getInt("IntrestEducation");
			int newAge = msgAsJson.getInt("IntrestNewAge");
			int sport = msgAsJson.getInt("IntrestSport");
			int health = msgAsJson.getInt("IntrestHealth");

			UserProfile user = new UserProfile(name, email, id);
			user.SetInterst(education, newAge, sport, health);
			return user;
		} catch (JSONException e) {
			Log.wtf("ServerDataBaseManager", "JSONException, " + e);
		}

		return null;
	}

	public static String addUser(UserProfile user){		

		Log.d("ServerDataBaseManager", user.toString());

		if (user == null){
			return "null";
		}

		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			UUID id = UUID.randomUUID();
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("username", user.name));
			params.add(new BasicNameValuePair("password", user.GetPassWord()));
			params.add(new BasicNameValuePair("email", user.email));
			params.add(new BasicNameValuePair("education", ""+ user.intrestEducation));
			params.add(new BasicNameValuePair("newage", ""+ user.intrestNewAge));
			params.add(new BasicNameValuePair("sport", ""+ user.intrestSport));
			params.add(new BasicNameValuePair("health", ""+ user.intrestHealth));

			Log.d("ServerDataBaseManager", "addUser starting");
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_ADDUSER, "POST", params);

			// check your log for json response
			Log.d("ServerDataBaseManager","Login attempt : " + json.toString());

			// json success tag
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				Log.d("ServerDataBaseManager", json.toString());
				return json.getString(TAG_MESSAGE);				
			}else{
				Log.d("ServerDataBaseManager", json.getString(TAG_MESSAGE));				

				return json.getString(TAG_MESSAGE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void modifyUser(UserProfile user){

	}

	// MANATRAS
	public static String addMantra(Mantra mantra){

		if (mantra == null){
			return null;
		}
		
		int success;
		
		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("id", 			mantra.Id));
			params.add(new BasicNameValuePair("Author", 		mantra.Author));
			params.add(new BasicNameValuePair("Description", 	mantra.Description));
			params.add(new BasicNameValuePair("sport", 			""+ mantra.ReleventSport));
			params.add(new BasicNameValuePair("education", 		""+ mantra.ReleventEducation));
			params.add(new BasicNameValuePair("newage", 		""+ mantra.ReleventNewAge));
			params.add(new BasicNameValuePair("health", 		""+ mantra.ReleventHealth));
			params.add(new BasicNameValuePair("date", 			mantra.getCreationDateInFormat()));
			
			
			Log.d("ServerDataBaseManager", "addMantra starting");
			
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_ADDMANTRA, "POST", params);

			Log.d("ServerDataBaseManager", json.toString());

			// json success tag
			success = json.getInt(TAG_SUCCESS);
			
			if (success == 1) {
				Log.d("ServerDataBaseManager", json.toString());
				return json.getString(TAG_MESSAGE);				
			}else{
				Log.d("ServerDataBaseManager", json.getString(TAG_MESSAGE));				

				return json.getString(TAG_MESSAGE);
			}
			
		}
		catch (JSONException e) {
			Log.w("ServerDataBaseManager" , "JSONException, " + e);
		}
		return null;

	}

	public static List<Mantra> getAllMantras(){

		List<Mantra> mantras = new LinkedList<Mantra>();

		Log.w("ServerDataBaseManager" , "Starting");

		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tmp", "a"));

			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_GETALLMANTRAS, "POST", params);

			if (json == null){
				Log.d("ServerDataBaseManager", "null");				
			}else{
				Log.d("ServerDataBaseManager", "NOT null");			
			}

			// check your log for json response
			//Log.d("UserProfile.getAllMantras()", json.toString());

			success = json.getInt(TAG_SUCCESS);			

			if (success == 1) {				
				String msg = json.getString(TAG_MESSAGE);

				//Log.w("GetUserFromJson()", "msg " + msg);

				String[] mantrasStr = msg.split(" , ");
				for(String man : mantrasStr){

					if (man.contains("Id")){
						JSONObject msgAsJson = new JSONObject(man);
						Mantra mantra = getMantraFromJson(msgAsJson);
						if (mantra != null) mantras.add(mantra);
					}
				}

			}else{
				// Failed reading mantras

				//Log.d("UserProfile.getAllMantras()", json.getString(TAG_MESSAGE));				
				//String n = json.getString("Name");		

				return null;
			}
		} catch (JSONException e) {
			Log.wtf("ServerDataBaseManager", "JSONException \n" + e);
		} catch (Exception e) {
			Log.wtf("ServerDataBaseManager", "Exception \n" + e);
		}

		return mantras;		
	}

	public static Mantra getMantraFromJson(JSONObject msgAsJson){

		Mantra mantra = null;

		try {
			String id = msgAsJson.getString("Id");
			String author = msgAsJson.getString("Author");
			String desc = msgAsJson.getString("Description");
			int releventSport = msgAsJson.getInt("ReleventSport");
			int releventEducation = msgAsJson.getInt("ReleventEducation");
			int releventNewAge = msgAsJson.getInt("ReleventNewAge");
			int releventHealth = msgAsJson.getInt("ReleventHealth");
			String creationDate = msgAsJson.getString("CreationDate");

			mantra = new Mantra(desc, author, id);
			mantra.SetRelevents(releventSport, releventEducation, releventNewAge, releventHealth);

			Date date = null; 
			try 
			{  
				date = Mantra.mantraDateFormat.parse(creationDate);  
			} 
			catch (Exception e)
			{
				Log.wtf("ServerDataBaseManager", "Exception \n" + e);
				date = null; 
			}

			if (date != null){
				mantra.SetCreationDate(date);
			}

		} catch (JSONException e1) {
			Log.wtf("ServerDataBaseManager", "JSONException \n" + e1);
			mantra = null;
		}

		return mantra;
	}	

}