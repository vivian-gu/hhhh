package soosokan.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soosokanfu.R;

public class AdsDetailView extends Activity{
	private TextView sellername_view, adstitle_view, adsdes_view, adstime_view, ads_address_view;
	ImageView adsPic_view;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ads_view);
		

		MyApplication.getInstance().addActivity(this);
		
		Bundle Bundle1 = this.getIntent().getExtras();
		sellername_view = (TextView)findViewById(R.id.sellername_view);
		adstitle_view = (TextView)findViewById(R.id.adstitle_view);
		ads_address_view = (TextView)findViewById(R.id.ads_address_view);
		adsdes_view = (TextView)findViewById(R.id.adsdes_view);
		adstime_view = (TextView)findViewById(R.id.adstime_view);
		adsPic_view = (ImageView)findViewById(R.id.adsPic_view);
		
		sellername_view.setText(Bundle1.getString("sellername"));
		adstitle_view.setText(Bundle1.getString("title"));
		ads_address_view.setText(Bundle1.getString("address"));
		adsdes_view.setText(Bundle1.getString("description"));
		adstime_view.setText(Bundle1.getString("time"));
		
		String picUrl = Bundle1.getString("pic");
		
		Toast toast = Toast.makeText(this, "Ads Detail View: "+ picUrl,
				Toast.LENGTH_LONG);
		toast.show();
	
		
		adsPic_view.setTag(picUrl);
		new CanvasImageTask().execute(adsPic_view);
		
//		byte[] pic = Bundle1.getByteArray("pic");
//		Bitmap bmpout = BitmapFactory.decodeByteArray(pic,0,pic.length);
//		adsPic_view.setImageBitmap(bmpout);
		
		
		
	}
	

}
