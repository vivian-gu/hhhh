package soosokan.Activity;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import soosokan.Entity.SysApplication;
import android.app.TabActivity;
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
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.soosokanfu.R;
//import com.facebook.AppEventsLogger;

public class UserMainActivity extends TabActivity implements
		OnCheckedChangeListener {

	public final static int REFRESH_LOCATION = 0x0100;
	public static final String TAGFU = "AndroidUCActivity";

	private LocationManager lm;
	private Location location;
	private String provider;
	private SharedPreferences sp;
	private String longtitude;
	private String latitude;

	private static final String TAG = "USERMAIN";
	private Intent aIntent;
	private Intent fIntent;
	private Intent accountIntent;
	private RadioButton radio_home;
	private RadioButton radio_service;
	private RadioButton radio_account;
	private TabHost mTabHost;
	private AutoCompleteTextView search_autocompletetextview;
	private ImageButton IBtn;
	private String searchString;
	private long mExitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_usermain);
		search_autocompletetextview = (AutoCompleteTextView) findViewById(R.id.search_autocompletetextview);
		ImageDownLoader.initImageLoader();
		ImageDownLoader.initContext(getApplicationContext());

		sp = this.getSharedPreferences("userInfo", 0);

		IBtn = (ImageButton) findViewById(R.id.search_button);
		
		
		Timer timer=new Timer();  
        timer.schedule(new TimerTask() {  
            public void run() {  
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
            }  
        }, 2000);  
		
		
		IBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editable s = search_autocompletetextview.getText();
				searchString = s.toString();

				Intent intent = new Intent();
				intent.setClass(UserMainActivity.this, ShopListActivity.class);
				searchString = s.toString();
				intent.putExtra("searchKeyword", searchString);

				lm = (LocationManager) getApplicationContext()
						.getSystemService(Context.LOCATION_SERVICE);
				if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(UserMainActivity.this, "Please Open GPS...",
							Toast.LENGTH_SHORT).show();
					Intent intent1 = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent1, 0);
					return;
				}

				provider = lm.getBestProvider(getCriteria(), true);
				location = lm.getLastKnownLocation(provider);

				if (location == null) {
					if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
						lm.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 1000, 1,
								locationListener);
						location = lm
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}

				longtitude = Double.toString(location.getLongitude());
				latitude = Double.toString(location.getLatitude());
				
				if(longtitude == null ||latitude==null ){
					Toast.makeText(UserMainActivity.this, "Locating...",
							Toast.LENGTH_SHORT).show();
				}

				lm.addGpsStatusListener(listener);
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
						1, locationListener);

				intent.putExtra("longtitude", longtitude);
				intent.putExtra("latitude", latitude);

				Editor editor = sp.edit();
				editor.putString("longtitude", longtitude);
				editor.putString("latitude", latitude);
				editor.commit();

				startActivity(intent);
			}

		});

		this.aIntent = new Intent(this, AdsMainInterface.class);
		this.fIntent = new Intent(this, FavoriteActivity.class);
//		this.accountIntent = new Intent(this, AdsFullListActivity.class);
		this.accountIntent = new Intent(this, StoredAdsListActivity.class);

		radio_home = (RadioButton) findViewById(R.id.radio_home);
		radio_service = (RadioButton) findViewById(R.id.radio_service);
		radio_account = (RadioButton) findViewById(R.id.radio_account);
		radio_home.setChecked(true);

		radio_home
				.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);
		radio_service
				.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);
		radio_account
				.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);
		setupIntent();
	}

	private LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "��ǰGPS״̬Ϊ�ɼ�״̬");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "��ǰGPS״̬Ϊ��������״̬");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "��ǰGPS״̬Ϊ��ͣ����״̬");
				break;
			}

		}

		/**
		 * GPS����ʱ����
		 */
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
				Log.i(TAG, "��һ�ζ�λ");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "����״̬�ı�");
				GpsStatus gpsStatus = lm.getGpsStatus(null);
				int maxSatellites = gpsStatus.getMaxSatellites();
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("��������" + count + "������");
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "��λ����");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "��λ����");
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

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_home:
				this.mTabHost.setCurrentTabByTag("Radio_Home");
				break;
			case R.id.radio_service:
				this.mTabHost.setCurrentTabByTag("Radio_Service");
				break;
			case R.id.radio_account:
				this.mTabHost.setCurrentTabByTag("Radio_Account");
				break;
			}
		}
	}

	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("Radio_Home", R.string.main_ads,
				R.drawable.tab_selector_home, this.aIntent));

		localTabHost.addTab(buildTabSpec("Radio_Service",
				R.string.main_service, R.drawable.tab_selector_favourite,
				this.fIntent));

		localTabHost.addTab(buildTabSpec("Radio_Account",
				R.string.main_account, R.drawable.tab_selector_account,
				this.accountIntent));
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_user_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class WebServiceTask extends AbstractWebServiceTask {

		public WebServiceTask(int taskType, Context mContext,
				String processMessage) {
			super(taskType, mContext, processMessage);
		}

		@Override
		protected void hideKeyboard() {
			InputMethodManager inputManager = (InputMethodManager) UserMainActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(UserMainActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

		}

		@Override
		public void handleResponse(String response) {
			// TODO Auto-generated method stub
			handleResponseLocal(response);
		}
	}

	public void handleResponseLocal(String response) {

	}

	@Override
	protected void onResume() {
		super.onResume();
//		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		AppEventsLogger.deactivateApp(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "Press Again to Exit.", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				SysApplication.closeall();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
