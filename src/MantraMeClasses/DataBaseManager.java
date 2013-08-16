package MantraMeClasses;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DataBaseManager {

	DataBaseHelper db;

	public DataBaseManager(Context context){		
		db = new DataBaseHelper(context,"MyDB",null,1);
	}

	public List<Mantra> GetAllMantra(){

		List<Mantra> allMantras = new LinkedList<Mantra>();
		SQLiteDatabase tbls = db.getReadableDatabase();	

		String[] projection = {
				"MantraID",
				"MantraName",
				"MantraText",  
				"ReleventSport",    
				"ReleventEducation",    
				"ReleventNewAge", 
				"ReleventHealth",
		};    	


		Cursor c = tbls.query(
				DataBaseHelper.tableName,  // The table to query
				projection,                               // The columns to return
				null,                                // The columns for the WHERE clause
				null,                            // The values for the WHERE clause
				null,                                     // don't group the rows
				null,                                     // don't filter by row groups
				null                                 // The sort order
				);


		for (c.moveToFirst(); !c.isAfterLast() ; c.moveToNext() ){
			Mantra m = GetMantraFromCursor(c);
			allMantras.add(m);
		}		

		return allMantras;
	}

	public void AddMantra(List<Mantra> mantras){

		Log.w("DataBaseManager1", "AddMantra called, " + mantras.size() + " mantras will be added to DB");
		for (Mantra mantra : mantras) {
			AddMantra(mantra);
		}
	}

	public void AddMantra(Mantra mantra){

		String sql = "INSERT INTO " + DataBaseHelper.tableName +
				" VALUES ('" + 				
				mantra.mantra_id + "', '" +
				mantra.mantraName + "', '" +
				mantra.man_str + "', " +
				mantra.ReleventSport + ", " +
				mantra.ReleventEducation + ", " +
				mantra.ReleventNewAge + ", " +	
				mantra.ReleventHealth +
				")";

		Log.w("DataBaseManager2", sql);

		SQLiteDatabase tbls = db.getWritableDatabase();	
		tbls.execSQL(sql);
	}

	private Mantra GetMantraFromCursor(Cursor c) {

		Mantra m = new Mantra(c.getString(2), c.getString(1), c.getString(0));
		m.SetRelevents(c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));		
		Log.w("DataBaseManager3", m.toString());		
		return m;
	}


}
