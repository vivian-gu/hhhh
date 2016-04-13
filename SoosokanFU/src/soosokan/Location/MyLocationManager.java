package soosokan.Location;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyLocationManager {

	private final static int CHECK_POSITION_INTERVAL = 60 * 1000;
	
	private LocationManager locationManager;
	
	private Context mContext;
	
	private Handler mHandler;
	
	private myListen myListenGPS;
	
	private myListen myListenNetwork;
	
	
	
	public MyLocationManager(Context context, Handler handler)
	{
		mContext = context;
		
		mHandler = handler;
		
		locationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	
	 public boolean getGPSState()
	 {
    	ContentResolver resolver = mContext.getContentResolver();
        boolean open = Settings.Secure.isLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER);
        
        return open;
	 }
	   

	 public void  toggleGPS() {

		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		
		try {
			PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
	
	 
	public Location getLocationByGps()
	{
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		return location;
	}
	
	public Location getLocationByNetwork()
	{
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		return location;
	}
	 
	
	public void registerListen()
	{
		if (myListenGPS == null)
		{
			myListenGPS = new myListen();
			
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					CHECK_POSITION_INTERVAL, 0, myListenGPS);
			
			
		}
		
		if (myListenNetwork == null)
		{
			myListenNetwork = new myListen();
			
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					CHECK_POSITION_INTERVAL, 0, myListenNetwork);
		}
	}
	
	public void unRegisterListen()
	{
		if(myListenGPS != null)
		{
			locationManager.removeUpdates(myListenGPS);
			
			myListenGPS = null;
			
		}
		
		if(myListenNetwork != null)
		{
			locationManager.removeUpdates(myListenNetwork);
			
			myListenNetwork = null;
			
		}
	}
	

	class myListen implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			MyUtil.display("onLocationChanged.......");

			
			if (mHandler != null)
			{
				Message msg = mHandler.obtainMessage(0x0100, location);
				msg.sendToTarget();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
	
	
	
	
								// %s: latitude, %s: longitude
	private static final String GOOGLE_QUERY_ADDRESS_URL = "http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s";
	
	private static final int TIME_OUT_INTERVAL = 3 * 1000;
	
	private TelephonyManager mTelephonyManager;

	private GsmCellLocation mGsmCellLocation;

	private int cid;								
		
	private int lac;								

	private String mcc = "";
	
	private String mnc = "";
	
	
	
	public CellIDInfo getCellInfo()
	{
		mGsmCellLocation = ((GsmCellLocation) mTelephonyManager.getCellLocation());

		if (mGsmCellLocation == null)
			return null;
		
		cid = mGsmCellLocation.getCid();
		lac = mGsmCellLocation.getLac();
	
		
		String netWorkOperator = mTelephonyManager.getNetworkOperator();
		
		mcc = netWorkOperator.substring(0, 3);	
		mnc = netWorkOperator.substring(3, 5);
		
		CellIDInfo cellIDInfo = new CellIDInfo();
		cellIDInfo.cellId = cid;
		cellIDInfo.mobileCountryCode = mcc;
		cellIDInfo.mobileNetworkCode = mnc;
		cellIDInfo.locationAreaCode = lac;
		
		return cellIDInfo;
	}
	
	
	
	/**
	 
	 * 
	 * @throws Exception
	 */
	public String queryAddressByGoogle(Double lat, Double lon){
		String resultString = "";

	
		String urlString = String.format(GOOGLE_QUERY_ADDRESS_URL, lat, lon);

	
		HttpClient client = new DefaultHttpClient();
		
	
	    client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_INTERVAL);//请求超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT_INTERVAL);//读取超时
		
		
	
		HttpGet get = new HttpGet(urlString);
		try {
			
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString("address");
				}
			}
		} catch (Exception e) {
			return null;
		} finally {
			get.abort();
			client = null;
		}

		return resultString;
	}
	
}
