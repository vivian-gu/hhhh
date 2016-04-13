package soosokan.Activity;

import java.util.ArrayList;
import java.util.List;

import com.example.soosokanfu.R;

import soosokan.Entity.Adsmod;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import soosokan.Database.DBManager;

public class AdsDeleteActivity extends Activity{
	private ListView listView;
	public static List<Adsmod> deleteads = new ArrayList<Adsmod>();
	
	public DBManager dbManager = new DBManager(this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saved_adslist);
		dbManager.open();

		listView = (ListView) findViewById(R.id.savedAdsList);
		deleteads = dbManager.getAll();
		deAdsmodAdapter adapter = new deAdsmodAdapter(this,
				deleteads);
		listView.setAdapter(adapter);

		MyApplication.getInstance().addActivity(this);
	}

}

class deAdsmodAdapter extends ArrayAdapter<Adsmod> {
	private Context context;
	public List<Adsmod> adsmods;

	public deAdsmodAdapter(Context context, List<Adsmod> adsmods) {
		super(context, R.layout.row_delete_ads, adsmods);
		this.context = context;
		this.adsmods = adsmods;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.row_delete_ads, parent, false);
		final Adsmod adsmod = adsmods.get(position);
		TextView row_SellerName = (TextView) view.findViewById(R.id.row_SellerName);
		TextView row_AdsTitle = (TextView) view.findViewById(R.id.row_AdsTitle);
		ImageView row_AdsPic = (ImageView) view.findViewById(R.id.row_ads_pic);

		row_AdsTitle.setText("" + adsmod.title);
		row_SellerName.setText(""+ adsmod.sellername);
		String picUrl = adsmod.pic;

		row_AdsPic.setTag(picUrl);
		new CanvasImageTask().execute(row_AdsPic);
//		Bitmap bmpout = BitmapFactory.decodeByteArray(adsmod.pic,0,adsmod.pic.length); 
//
//		row_AdsPic.setImageBitmap(bmpout);

		 view.setId(adsmod.id);

		Button button = (Button)view.findViewById(R.id.delete_btn);

		//设置按钮监听器 
		button.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {

				DBManager dbManager = new DBManager(context);
				dbManager.open();
				dbManager.delete(adsmod.id);
				dbManager.close();
				Toast.makeText(context,"This advertisement is deleted successfully!",
						Toast.LENGTH_LONG).show();
			}
		});
		return view;

	}
	

}
