package soosokan.Activity;

import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import soosokan.Activity.RefreshableView.PullToRefreshListener;
import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import soosokan.Tool.ACache;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdsFullListActivity extends Activity {

	private static final String TAG = "ADSFULLLIST";
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/allAds";

	private TextView adstime, adstitle, adsdes;
	private String adstimeString, adstitleString, adsdesString;
//	private long lastUpdateTime;
	private SharedPreferences preferences;
	RefreshableView refreshableView;
	private int mId = -1;
	private static final String UPDATED_AT = "updated_at";
	private LocationManager lm;
	private Location location;
	private String provider;
	String longtitude;
	String latitude;
	ListView listView;

	private static JSONArray mData;
	private AdsListAdapter mAdapter;
//	static Vector<JSONArray> temp;
//	static JSONArray temp ;
	SharedPreferences sp;
	String sellerId, adsId;

	private ACache mCache;  //a simple cache
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.adslist_full);
		
		mCache = ACache.get(this);
		mData = new JSONArray();
	
		
//		initData((long)0);
		
//		read data from cache first
		JSONArray savedJSONArray = mCache.getAsJSONArray("savedJSONArray");
		
		if (savedJSONArray == null) {
			Toast.makeText(this, "JSONArray cache is null ...",
					Toast.LENGTH_SHORT).show();
			
			initData(0);
		}else{
			Toast.makeText(this, savedJSONArray.toString(),
					Toast.LENGTH_SHORT).show();
			showCacheList(savedJSONArray);
		}
	}
	
	//
	private void showCacheList(JSONArray savedJSONArray){
//		JSONObject soosokan = new JSONObject();
//		try {
//			soosokan.put("title", "Soosokan");
//			soosokan.put("description", "Soosokan");
//			soosokan.put("adsId", "ww@qq.com/2015-07-15 15:30:25");//Soosokan  ���ID
//			soosokan.put("time", " ");
//			
//			
//			mData.put(soosokan);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			for(int i=0;i<savedJSONArray.length();i++){
				mData.put(savedJSONArray.get(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.adslist_full);

		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		listView = (ListView) findViewById(R.id.ads_fulllist);
		
		mAdapter = new AdsListAdapter(this, mData);
		listView.setAdapter(mAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject ads = mAdapter.getItem(position);
				try {
					adsId = ads.getString("adsId");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.putExtra("adsId", adsId);

				System.out.println("Item manage:" + adsId);
				intent.setClass(AdsFullListActivity.this,
						AdsDetailActivity.class);
				startActivity(intent);
			}
		});

		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				preferences = PreferenceManager.getDefaultSharedPreferences(refreshableView.getContext());
//				lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
				long timepass = preferences.getLong("timepassed", -1);
				refreshableView.finishRefreshing();
				Refresh(timepass);
				
			}
		}, 0);	
	}

	
	
	private void tranferToList(String response) {
		
//		if (!response.equals(null)){
		if (!response.equals(null)){
			try {
				JSONArray json = new JSONArray(response);
				
				for(int i=0;i<json.length();i++){
					mData.put(json.get(i));
				}
				//save jsonArray to cache
				mCache.put("savedJSONArray", json);
				Toast.makeText(getApplicationContext(),"save data : "+ mData , Toast.LENGTH_LONG).show();
				
				//			mData = json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}


		setContentView(R.layout.adslist_full);

		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		listView = (ListView) findViewById(R.id.ads_fulllist);
		
		mAdapter = new AdsListAdapter(this, mData);
		listView.setAdapter(mAdapter);
		

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject ads = mAdapter.getItem(position);
				try {
					adsId = ads.getString("adsId");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.putExtra("adsId", adsId);

//				System.out.println("Item manage:" + adsId);
				intent.setClass(AdsFullListActivity.this,
						AdsDetailActivity.class);
				startActivity(intent);
			}
		});

		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				preferences = PreferenceManager.getDefaultSharedPreferences(refreshableView.getContext());
//				lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
				long timepass = preferences.getLong("timepassed", -1);
				refreshableView.finishRefreshing();
				Refresh(timepass);
				
			}
		}, 0);
	}

	public void Refresh( final long timepass) {
		new Thread() {
			public void run() {
				AdsFullListActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(),"lasf Refresh : "+ timepass , Toast.LENGTH_LONG).show();
						initData(timepass);
					}
				});
			}
		}.start();
	}

	private void initData(long timepass) {
//		String response = null;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "Please Open GPS...", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			return;
		}

		provider = lm.getBestProvider(getCriteria(), true);
		location = lm.getLastKnownLocation(provider);

		if (location == null) {
			if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1000, 1, locationListener);
				location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}

		longtitude = Double.toString(location.getLongitude());
		latitude = Double.toString(location.getLatitude());
		lm.addGpsStatusListener(listener);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				locationListener);
		
		
//		sp = this.getSharedPreferences("userInfo", 0);
//		
//		String longstr = sp.getString("longtitude", "");
//		String latstr = sp.getString("latitude", "");
//		
		params.add("longtitude", longtitude);
		params.add("latitude", latitude);
		String lasttime = String.valueOf(timepass);
		params.add("lasttime", lasttime);
		
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
				tranferToList(response);
				
				
			}

			private String byteToString(byte[] response) {
				String a = "";
				for (int i = 0; i < response.length; i++)
					a += (char) response[i];
				return a;
			}
		});
	}
	private LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "当前GPS状�?�为可见状�??");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "当前GPS状�?�为服务区外状�??");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "当前GPS状�?�为暂停服务状�??");
				break;
			}

		}

		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
			Location location = lm.getLastKnownLocation(provider);
		}
	};

	private void updateWithNewLocation(Location location2) {
		while (location == null) {
			lm.requestLocationUpdates(provider, 2000, (float) 0.1,
					locationListener);
		}
	}

	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i(TAG, "第一次定�??");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "卫星状�?�改�??");
				GpsStatus gpsStatus = lm.getGpsStatus(null);
				int maxSatellites = gpsStatus.getMaxSatellites();
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("搜索到：" + count + "颗卫�??");
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "定位启动");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "定位结束");
				break;
			}
		};
	};

	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		criteria.setBearingRequired(false);
		criteria.setAltitudeRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

}
