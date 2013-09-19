

package MantraMeClasses;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseManager {

	DataBaseHelper db;

	private String[] projection = {
			"MantraID",
			"MantraDescription",
			"MantraAuthor",  
			"ReleventSport",    
			"ReleventEducation",    
			"ReleventNewAge", 
			"ReleventHealth",
			"LastUse",
			"CreationDate",
	};  

	public DataBaseManager(Context context){

		db = new DataBaseHelper(context,"MyDB",null,1);
	}

	public Mantra getMantraById(String id){

		Log.w("DataBaseManager", "GetAllMantra");

		SQLiteDatabase tbls = db.getReadableDatabase();	

		Cursor c = tbls.query(DataBaseHelper.tableName, projection, 
				"MantraID == " + "'" + id + "'", null, null, null, null);


		Log.w("DataBaseManager", "Cursor c");

		for (c.moveToFirst(); !c.isAfterLast() ;  ){
			Mantra m = GetMantraFromCursor(c);
			Log.w("DataBaseManager", "GetMantraFromCursor man = " + m.toString());
			tbls.close();
			return m;
		}

		tbls.close();
		return null;	
	}

	public void deleteAllMantras(){

		SQLiteDatabase tbls = db.getReadableDatabase();	
		tbls.execSQL("delete from "+ DataBaseHelper.tableName);

		tbls.close();

	}

	public List<Mantra> GetAllMantra(){

		Log.w("DataBaseManager", "GetAllMantra");

		List<Mantra> allMantras = new LinkedList<Mantra>();
		SQLiteDatabase tbls = db.getReadableDatabase();	

		Cursor c = tbls.query(
				DataBaseHelper.tableName, // The table to query
				projection,               // The columns to return
				null,           		  // The columns for the WHERE clause
				null,                     // The values for the WHERE clause
				null,         	          // don't group the rows
				null,                     // don't filter by row groups
				null                      // The sort order
				);


		for (c.moveToFirst(); !c.isAfterLast() ; c.moveToNext() ){
			Mantra m = GetMantraFromCursor(c);
			allMantras.add(m);
		}		


		tbls.close();

		return allMantras;
	}

	public void AddMantra(List<Mantra> mantras){

		Log.w("DataBaseManager", "AddMantra");
		for (Mantra mantra : mantras) {
			AddMantra(mantra);
		}
	}

	public boolean AddMantra(Mantra mantra){

		SQLiteDatabase tbls = db.getWritableDatabase();	

		try {
			String sql = 
					"INSERT INTO " + DataBaseHelper.tableName +
					" VALUES " 									+"(" + 				
					"'" +  mantra.Id 					+ "'" 	+ ", " +
					"'" + mantra.Description 			+ "'" 	+ ", " +
					"'" + mantra.Author 				+ "'" 	+ ", " +
					mantra.ReleventSport 						+ ", " +
					mantra.ReleventEducation 					+ ", " +
					mantra.ReleventNewAge 						+ ", " +	
					mantra.ReleventHealth 						+ ", " +
					"'" + mantra.getCreationDateInFormat() 		+ "'" 	+ ", " +
					"datetime()" 								+ ")";

			Log.w("DataBaseManager", "AddMantra sql : " + sql);

			tbls.execSQL(sql);
		}
		catch(Exception e){
			Log.wtf("DataBaseManager", "Exception in AddMantra sql : " + e);

			tbls.close();
			return false;
		}
		tbls.close();
		return true;
	}

	private Mantra GetMantraFromCursor(Cursor c) {

		Mantra m = new Mantra();

		if (c == null){
			Log.w("DataBaseManager", "c == null");
			return null;
		}

		try{
			String id = c.getString(0);
			String desc = c.getString(1);
			String author = c.getString(2);

			Log.w("DataBaseManager", "GetMantraFromCursor id: " + id + ", desc: " + desc + ", author: " + author);

			m = new Mantra(desc, author, id);

			m.SetRelevents(c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));

			String date = c.getString(7); 

			Date startDate;
			try {
				startDate = Mantra.mantraDateFormat.parse(date);
				m.SetCreationDate(startDate);
			} catch (ParseException e) {
				Log.wtf("DataBaseManager", "ParseException + " + e);
			}

			Log.w("DataBaseManager", m.toString());
		}
		catch (Exception e){
			Log.wtf("DataBaseManager", "Exception + " + e);
			return null;
		}

		return m;
	}
}
