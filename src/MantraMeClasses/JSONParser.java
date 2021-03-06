package MantraMeClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	// constructor
	public JSONParser() {

	}

	// function get json from url, by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

		InputStream inputStream = null;
		JSONObject jObj = null;
		String json = "";

		// Making HTTP request
		try {

			// check for request method
			if(method == "POST"){
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();

			}else if(method == "GET"){
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
			}           

		} catch (UnsupportedEncodingException e) {
			Log.wtf("JSONParser", "UnsupportedEncodingException " + e.toString());
		} catch (ClientProtocolException e) {
			Log.wtf("JSONParser", "ClientProtocolException " + e.toString());
		} catch (IOException e) {
			Log.wtf("JSONParser", "IOException " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.wtf("JSONParser", "Exception Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.wtf("JSONParser", "JSONException Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;
	}
}
