package MantraMeClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;


public class ServerDataBaseManager {

	static JSONParser jsonParser = new JSONParser();
	private static final String URL_ADDUSER = "http://192.168.1.75/MyMantra/addUser.php";
	private static final String URL_LOGIN = "http://192.168.1.75/MyMantra/getUserByEMailAndPassword.php";

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

			Log.d("UserProfile.getUser()", "getUser starting");
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

			UserProfile user = new UserProfile(name,email);
			user.SetInterst(education, newAge, sport, health);
			Log.w("GetUserFromJson()", "user " + user.toString());
			return user;
		} catch (JSONException e) {
			Log.w("GetUserFromJson Exception 111111111111!", e);
		}
		
		return null;
	}

	public static String addUser(UserProfile user, String pass){		

		if (user == null || pass == null || pass.equals("")){
			return "Enter User Details and password";
		}

		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			UUID id = UUID.randomUUID();
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("username", user.name));
			params.add(new BasicNameValuePair("password", pass));
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

	public static void getAllMantras(){

	}	

}