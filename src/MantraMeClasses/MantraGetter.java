package MantraMeClasses;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.Sharon.MantraMeWidget.AddNewUser;
import com.Sharon.MantraMeWidget.WidgetConfigPage.LoginUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MantraGetter {

	private UserProfile userProfile;	
	private List<Mantra> allMantras;

	public MantraGetter(UserProfile user, Context context){		
		userProfile = user;		
		allMantras = new LinkedList<Mantra>();

		getAllMantrasFromServer();						
	}

	private void getAllMantrasFromServer() {
		GetAllMantrasAction action = new GetAllMantrasAction();
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
		allMantras = action.mantras;
	}

	public Mantra GetNewMantra(){
		if (allMantras == null || allMantras.size() == 0){
			return null;
		}
		Random r = new Random();
		int indexRandom = r.nextInt(allMantras.size());
		return allMantras.get(indexRandom);		
	}

	public class GetAllMantrasAction extends AsyncTask<String, String, String> {

		List<Mantra> mantras;

		protected void onPreExecute() {
			Log.w("getAllMantrasAction" , "onPreExecute");
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			Log.w("getAllMantrasAction" , "doInBackground");

			mantras = ServerDataBaseManager.getAllMantras();	

			return null;
		}

		protected void onPostExecute(String file_url) {
			Log.w("getAllMantrasAction" , "onPostExecute");
		}		
	}
}
