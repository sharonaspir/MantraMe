package MantraMeClasses;

import java.text.SimpleDateFormat;
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

public class ServerDataBaseManager {

	static JSONParser jsonParser = new JSONParser();
	private static final String SERVER_IP 			= "10.0.0.10";
	private static final String URL_ADDUSER 		= "http://" + SERVER_IP + "/MyMantra/addUser.php";
	private static final String URL_LOGIN			= "http://" + SERVER_IP + "/MyMantra/getUserByEMailAndPassword.php";
	private static final String URL_GETALLMANTRAS 	= "http://" + SERVER_IP + "/MyMantra/getAllMantras.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	// USERS

	// Gets the user saved on the sql server, with mail and password as inputed
	public static UserProfile getUser(String mail, String password){

		Log.w("UserProfile.getUser()" , "mail " + mail + " pass " + password);

		if ( password.equals("") || password.equals("")){
			return null;
		}
		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("email", mail));

			Log.d("UserProfile.getUser()", "getUser starting , " + URL_LOGIN);
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_LOGIN, "POST", params);

			// check your log for json response
			Log.d("UserProfile.getUser()", json.toString());

			success = json.getInt(TAG_SUCCESS);			

			if (success == 1) {				
				UserProfile user = GetUserFromJson(json);

				return user;				
			}else{
				Log.d("UserProfile.getUser()", json.getString(TAG_MESSAGE));				

				//String n = json.getString("Name");		

				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.w("UserProfile.getUser()", "Exception\n" + e);
		}

		return null;		
	}

	// Gets a UserProfile from user info containing json
	private static UserProfile GetUserFromJson(JSONObject json) {
		Log.w("GetUserFromJson()", "starting GetUserFromJson");

		try {
			String msg = json.getString(TAG_MESSAGE);
			Log.w("GetUserFromJson()", "msg " + msg);
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
			Log.w("GetUserFromJson()", "user " + user.toString());
			return user;
		} catch (JSONException e) {
			Log.w("GetUserFromJson Exception 111111111111!", e);
		}

		return null;
	}

	public static String addUser(UserProfile user){		

		Log.d("addUser", user.toString());
		
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
			params.add(new BasicNameValuePair("newage", ""+user.intrestNewAge));
			params.add(new BasicNameValuePair("sport", ""+user.intrestSport));
			params.add(new BasicNameValuePair("health", ""+user.intrestHealth));

			Log.d("request!", "addUser starting");
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_ADDUSER, "POST", params);

			// check your log for json response
			Log.d("Login attempt", json.toString());

			// json success tag
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				Log.d("Login Successful!", json.toString());
				return json.getString(TAG_MESSAGE);				
			}else{
				Log.d("Login Failure!", json.getString(TAG_MESSAGE));				

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
	public static void addMantra(Mantra mantra){

	}

	public static List<Mantra> getAllMantras(){

		List<Mantra> mantras = new LinkedList<Mantra>();

		Log.w("UserProfile.getAllMantras()" , "Starting");

		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tmp", "a"));

			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(URL_GETALLMANTRAS, "POST", params);

			if (json == null){
				Log.d("UserProfile.getAllMantras()", "null");				
			}else{
				Log.d("UserProfile.getAllMantras()", "NOT null");			
			}

			// check your log for json response
			Log.d("UserProfile.getAllMantras()", json.toString());

			success = json.getInt(TAG_SUCCESS);			

			if (success == 1) {				
				//UserProfile user = GetUserFromJson(json);
				String msg = json.getString(TAG_MESSAGE);
				Log.w("GetUserFromJson()", "msg " + msg);

				String[] mantrasStr = msg.split(" , ");
				for(String man : mantrasStr){

					if (man.contains("Id")){
						JSONObject msgAsJson = new JSONObject(man);
						Mantra mantra = getMantraFromJson(msgAsJson);
						if (mantra != null) mantras.add(mantra);
					}
				}

			}else{
				Log.d("UserProfile.getAllMantras()", json.getString(TAG_MESSAGE));				
				//String n = json.getString("Name");		

				return null;
			}
		} catch (JSONException e) {
			Log.w("UserProfile.getAllMantras()", "JSONException \n" + e);
			e.printStackTrace();
		} catch (Exception e) {
			Log.w("UserProfile.getAllMantras()", "Exception \n" + e);
		}

		return mantras;		
	}

	private static Mantra getMantraFromJson(JSONObject msgAsJson){

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
				SimpleDateFormat formatter; 					      
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				date = (Date)formatter.parse(creationDate);  
			} 
			catch (Exception e)
			{
				date = null; 
			}

			if (date != null){
				mantra.SetCreationDate(date);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			mantra = null;
		}

		return mantra;
	}	
}