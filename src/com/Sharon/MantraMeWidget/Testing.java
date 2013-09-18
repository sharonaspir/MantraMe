package com.Sharon.MantraMeWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import MantraMeClasses.DataBaseManager;
import MantraMeClasses.Mantra;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class Testing extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.testing, menu);
		return true;
	}

	public void addMantra(View view){

		Log.w("Testing", "addMantra");

		EditText et1 = (EditText) findViewById(R.id.editTextMantraTEST);

		String man = et1.getText().toString();

		// UUID id = UUID.randomUUID();
		Mantra mant = new Mantra(man, "me", man);

		Log.w("Testing", "mant - " + mant.toString());

		addMantraToLocalTable(mant);

		et1.setText("");
	}


	public void showMantraWithId(View view){

		Log.w("Testing", "showMantraWithId");

		EditText et1 = (EditText) findViewById(R.id.editTextMantraTEST);
		EditText et2 = (EditText) findViewById(R.id.editTextALLMantraTEST);		

		String id = et1.getText().toString();
		Mantra mant = getMantraFromId(id);		

		et1.setText("");

		if (mant != null){
			et2.setText(mant.toString());
		}
		else{
			et2.setText("");
		}
	}



	public void showAllClicked(View view){

		Log.w("Testing", "showAllClicked");

		List<Mantra> allMant = getAllMantrasFromLocalTable();

		EditText et2 = (EditText) findViewById(R.id.editTextALLMantraTEST);

		Log.w("Testing", "et2");

		String all = "ALL : ";

		for (Mantra man: allMant){
			all = all + man.Description + ", ";
		}

		Log.w("Testing", "all - "+ all);

		et2.setText(all);
	}

	private List<Mantra> getAllMantrasFromLocalTable() {
		Log.w("Testing", "getAllMantrasFromLocalTable");

		DataBaseManager dbm = new DataBaseManager(this);	
		List<Mantra> all = dbm.GetAllMantra();

		return all;
	}

	private void addMantraToLocalTable(Mantra mant) {
		Log.w("Testing", "addMantraToLocalTable");		

		DataBaseManager dbm = new DataBaseManager(this);		
		dbm.AddMantra(mant);
	}	

	private Mantra getMantraFromId(String id) {
		Log.w("Testing", "getMantraFromId");		

		DataBaseManager dbm = new DataBaseManager(this);		
		return dbm.getMantraById(id);
	}
}
