package soosokan.Database;

import java.sql.Blob;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {
	public static final String 	TABLE_ADS = "table_ads";
	public static final String 	COLUMN_ID = "id";
	public static final String 	COLUMN_TITLE = "title";
	public static final String 	COLUMN_DESCRIPTION = "description";
	public static final String 	COLUMN_TIME = "time";
	public static final String  COLUMN_SELLERNAME = "sellername";
	public static final String  COLUMN_ADDRESS = "address";
	public static final  String COLUMN_ADSPIC = "pic";

	private static final String DATABASE_NAME = "ads.db";
	private static final int 	DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE_TABLE_ADS = "create table "
			+ TABLE_ADS + "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_SELLERNAME + " text not null," 
			+ COLUMN_TITLE + " text not null,"
			+ COLUMN_ADDRESS + " text not null,"
			+ COLUMN_DESCRIPTION + " text not null,"
			+ COLUMN_TIME + " text not null,"
					+ COLUMN_ADSPIC + " text not null);";
//					" BLOB"
//							+ ");";
		
	public DBDesigner(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override           
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_ADS); 
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBDesigner.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);
		onCreate(db);
	}

}
