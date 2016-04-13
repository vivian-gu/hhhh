package soosokan.Database;

import java.util.ArrayList;
import java.util.List;

import soosokan.Entity.Adsmod;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;



public class DBManager {
	private SQLiteDatabase database;
	private DBDesigner dbHelper;
	Bitmap bmp;

	public DBManager(Context context) {
		dbHelper = new DBDesigner(context); 
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();  
	}
	
	public void close() {
		database.close();
	}
	
	public void add(Adsmod d) {
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_SELLERNAME, d.sellername);
		values.put(DBDesigner.COLUMN_TITLE, d.title);
		values.put(DBDesigner.COLUMN_DESCRIPTION, d.address);
		values.put(DBDesigner.COLUMN_DESCRIPTION, d.description);
		values.put(DBDesigner.COLUMN_TIME, d.time);
		
		values.put(DBDesigner.COLUMN_ADSPIC, d.pic);
		

		database.insert(DBDesigner.TABLE_ADS, null, values);
	}
	
	public List<Adsmod> getAll() {
		List<Adsmod> ADSmods = new ArrayList<Adsmod>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_ADS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Adsmod d = toAdsmod(cursor);

			ADSmods.add(d);
			cursor.moveToNext();
		}
		cursor.close();
		return ADSmods;
	}
	
	public Adsmod get(int id) {
		Adsmod d = null;

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_ADS + " WHERE "
				+ DBDesigner.COLUMN_ID + " = " + id, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Adsmod temp = toAdsmod(cursor);
			d = temp;
			cursor.moveToNext();   
		}
		cursor.close();
		return d;
	}
	
	public void delete(int myid) {
		database.delete(DBDesigner.TABLE_ADS, "ID=" + myid +"",null);
	}
	
	
	private Adsmod toAdsmod(Cursor cursor) {
		Adsmod pojo = new Adsmod();
		pojo.id = cursor.getInt(0);
		pojo.sellername = cursor.getString(1);
		pojo.title = cursor.getString(2);
		pojo.address = cursor.getString(3);
		pojo.description = cursor.getString(4);
		pojo.time = cursor.getString(5);
		pojo.pic = cursor.getString(6);  //update
		return pojo;
	}
	
	public void reset() {
		database.delete(DBDesigner.TABLE_ADS, null, null);
	}

}
