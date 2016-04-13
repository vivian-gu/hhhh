package soosokan.Activity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import soosokan.Database.DBManager;
import soosokan.Entity.Adsmod;
import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdsDetailActivity extends Activity {
	public static final String TAG = "ADSDETAIL";
	public DBManager dbManager = new DBManager(this);
	private TextView adstime, adstitle, adsdes, sellername, ads_address;
	private String picUrl;
//	private SimpleDateFormat sDateFormat;
	String date;
	ImageView adsPic;
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/byAdsId";

	public AdsDetailActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		dbManager.open();
		setContentView(R.layout.activity_ads_detail);
		Intent intent = getIntent();

		String id = intent.getStringExtra("adsId");

		sellername = (TextView) findViewById(R.id.sellername);
		adstime = (TextView) findViewById(R.id.adstime);
		adstitle = (TextView) findViewById(R.id.adstitle);
		ads_address = (TextView) findViewById(R.id.ads_address);
		adsdes = (TextView) findViewById(R.id.adsdes);
		adsPic =(ImageView) findViewById(R.id.adsPic);
		initData(id);
	}
	
	public boolean newAdsmod(Adsmod adsmod) {
		dbManager.add(adsmod);
		return true;
	}

	public void adsSaveBtn(View v) {
		String text1 = sellername.getText().toString();
		String text2 = adstitle.getText().toString();
		String text3 = ads_address.getText().toString();
		String text4 = adsdes.getText().toString();
		String text5 = adstime.getText().toString();
		String text6 = adsPic.getTag().toString();
//		String adspic = picUrl;
		
//		adsPic.setDrawingCacheEnabled(true);
//		bmp = Bitmap.createBitmap(adsPic.getDrawingCache());

//	    final ByteArrayOutputStream os = new ByteArrayOutputStream(); 
//
//	    bmp.compress(Bitmap.CompressFormat.PNG, 100, os);  
//
//	    byte[] adspic = os.toByteArray();
		
		
		
		newAdsmod(new Adsmod(text1, text2, text3, text4, text5, text6));
	}


	private void initData(String id) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("adsId", id);

		client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg0 == 404)
					Toast.makeText(getApplicationContext(),
							"Requested Resource not found", Toast.LENGTH_LONG)
							.show();
				else if (arg0 == 500)
					Toast.makeText(getApplicationContext(),
							"Something wrong at the server end",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), "Unexpected error",
							Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] Response) {

				String response = byteToString(Response);
				try {
					JSONObject json = new JSONObject(response);
					String title = json.getString("title");
					//add address here
					
					String descr = json.getString("description");
					String description = descr.split(":")[1];
					String sellernamestr = descr.split(":")[0];
					String time = json.getString("time");
					String picUrl = json.getString("picture");
					
					adsPic.setTag(picUrl);
					new CanvasImageTask().execute(adsPic);
					adstime.setText(time);
					adstitle.setText(title);
					//add address here
					
					adsdes.setText(description);
					sellername.setText(sellernamestr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private String byteToString(byte[] response) {
				String a = "";
				for (int i = 0; i < response.length; i++)
					a += (char) response[i];
				return a;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbManager.close();
	}

}
