package MantraMeClasses;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.util.Log;

public class MantraGetter {

	private static Mantra currentMantra = null;	
	private static List<Mantra> allMantras = new LinkedList<Mantra>();

	public MantraGetter(){				
	}

	public void getAllMantrasFromServer() {
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
