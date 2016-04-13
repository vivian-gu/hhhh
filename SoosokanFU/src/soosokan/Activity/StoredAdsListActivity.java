package soosokan.Activity;

import java.util.ArrayList;
import java.util.List;

import com.example.soosokanfu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import soosokan.Database.DBManager;


import soosokan.Entity.Adsmod;

public class StoredAdsListActivity extends Activity implements
		OnItemClickListener {

	private ListView listView;
	public static List<Adsmod> ads = new ArrayList<Adsmod>();
	private ImageView ads_pic;
	

	public DBManager dbManager = new DBManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saved_adslist);
		dbManager.open();

		listView = (ListView) findViewById(R.id.savedAdsList);
		ads_pic = (ImageView) findViewById(R.id.adsPic);
		ads = dbManager.getAll();
		AdsmodAdapter adapter = new AdsmodAdapter(this, ads);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((OnItemClickListener) this);
		MyApplication.getInstance().addActivity(this);
	}
	
	  
	@Override  
	protected void onResume() {  
	    super.onResume();  
	    onCreate(null);   
	} 
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Adsmod advertisement = ads.get(position);
		Intent Intent1 = new Intent();
		Intent1.setClass(StoredAdsListActivity.this, AdsDetailView.class);
		Bundle Bundle1 = new Bundle();
		Bundle1.putInt("id", advertisement.id);
		Bundle1.putString("sellername", advertisement.sellername);
		Bundle1.putString("title", advertisement.title);
		Bundle1.putString("description", advertisement.description);
		Bundle1.putString("time", advertisement.time);
		Bundle1.putString("pic", advertisement.pic);

		Intent1.putExtras(Bundle1);
		startActivity(Intent1);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbManager.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_delete, menu);
		return true;
	}

	public void delete(MenuItem item) {
		startActivity(new Intent(this, AdsDeleteActivity.class));
	}
	
	public void delete_all(MenuItem item){
		dbManager.reset();
		
		Toast.makeText(this,"All advertisements are deleted successfully!",
				Toast.LENGTH_LONG).show();
		
		
//		refresh();
		startActivity(new Intent(this, UserMainActivity.class));
		
	}
	
	

}

class AdsmodAdapter extends ArrayAdapter<Adsmod> {
	private Context context;
	public List<Adsmod> Adsmods;

	public AdsmodAdapter(Context context, List<Adsmod> Adsmods) {
		super(context, R.layout.row_ads, Adsmods);
		this.context = context;
		this.Adsmods = Adsmods;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.row_ads, parent, false);
		final Adsmod Adsmod = Adsmods.get(position);

		TextView row_AdsTitle = (TextView) view.findViewById(R.id.row_AdsTitle);
		TextView row_SellerName = (TextView) view.findViewById(R.id.row_SellerName);
		ImageView row_AdsPic = (ImageView) view.findViewById(R.id.row_ads_pic);

		row_AdsTitle.setText("" + Adsmod.title);
		row_SellerName.setText("" + Adsmod.sellername);
		String picUrl = Adsmod.pic;

//		Toast toast = Toast.makeText(this, "Store : "+ picUrl,
//				Toast.LENGTH_LONG);
//		
		System.out.println("Store : "+ picUrl);
		row_AdsPic.setTag(picUrl);
		new CanvasImageTask().execute(row_AdsPic);

		view.setId(Adsmod.id);
		

		return view;
	}
}