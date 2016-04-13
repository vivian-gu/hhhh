package soosokan.Activity;

import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdsMainListActivity extends Activity implements
		OnChildClickListener {
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/todayAds";
	private static final String TAG = "ADSMAIN";
	private ExpandableListView mListView = null;
	private ExpandAdapter mAdapter = null;
	private JSONArray mData;
	private LocationManager lm;
	private Location location;
	private String provider;
	private SharedPreferences sp;
	String sellerId;
	String longtitude;
	String latitude;

	/** Called when the activity is first created. */
	public void AdsAdd(MenuItem item) {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		initData();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		JSONObject item = mAdapter.getChild(groupPosition, childPosition);
		Intent intent = new Intent();

		try {
			intent.putExtra("adsId", item.getString("adsId"));
			intent.setClass(AdsMainListActivity.this, AdsDetailActivity.class);

			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private void initData() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("sellerId", sellerId);

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

		sp = this.getSharedPreferences("userInfo", 0);
		Editor editor = sp.edit();
		editor.putString("longtitude", longtitude);
		editor.putString("latitude", latitude);
		editor.commit();

		params.add("latitude", latitude);
		params.add("longtitude", longtitude);
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

				String reponse = byteToString(Response);
				tranferToList(reponse);
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

	private void tranferToList(String response) {
		try {
			JSONArray json = new JSONArray(response);
			JSONArray jsonArray = (JSONArray) json;

			mData = jsonArray;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mListView = new ExpandableListView(this);
		mListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		setContentView(mListView);

		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mAdapter = new ExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		mListView.setOnChildClickListener(this);
	}

}