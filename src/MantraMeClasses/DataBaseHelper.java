package MantraMeClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	
	public static String tableName = "TableMantras";

	public static String createTable = 	
			"CREATE TABLE "		+ tableName 			+ "(" + 
			"MantraID" 			+ " TEXT PRIMARY KEY" 	+ "," +
			"MantraDescription" + " TEXT" 				+ "," +
			"MantraAuthor" 		+ " TEXT" 				+ "," +
			"ReleventSport" 	+ " INTEGER" 			+ "," +
			"ReleventEducation" + " INTEGER" 			+ "," +
			"ReleventNewAge" 	+ " INTEGER" 			+ "," +
			"ReleventHealth" 	+ " INTEGER" 			+ "," +
			"LastUse" 			+ " DATE"				+ "," +
			"CreationDate" 		+ " DATE" 				+ ")" ;
 
 	public static String deleteTable =	"DROP TABLE IF EXISTS " + tableName;
 
 	public  DataBaseHelper(Context context, String name, CursorFactory factory,int version) {
 		super(context, name, factory, version);
 		Log.w("DataBaseHelper", "DataBaseHelper(...)");
 	}
 
 	@Override
 	public void onCreate(SQLiteDatabase db) {
 		Log.w("DataBaseHelper", "onCreate");
		db.execSQL(createTable);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DataBaseHelper", "onUpgrade");
		db.execSQL(deleteTable);
		onCreate(db);
	}
}
