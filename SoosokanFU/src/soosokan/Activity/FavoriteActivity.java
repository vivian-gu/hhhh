package soosokan.Activity;

import java.util.ArrayList;

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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FavoriteActivity extends Activity {
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/seller/favorite";
	private static final String TAG = "FAVORITE";
	private SharedPreferences sp;
	private ListView mListView;
	private JSONArray mData;
	private ShopListAdapter mAdapter;
	String userId = "";
	String longtitude, latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoplist);
		SysApplication.addActivity(this, TAG);

		mListView = (ListView) findViewById(R.id.shoplist_list);
		sp = this.getSharedPreferences("userInfo", 0);
		userId = sp.getString("Facebook_id", "");

		longtitude = sp.getString("longtitude", "");
		latitude = sp.getString("latitude", "");

		if (sp != null) {
			if (userId.equals("")) {
				Toast.makeText(FavoriteActivity.this, "Please Login!",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(FavoriteActivity.this,
						AccountActivity.class);
				startActivity(intent);
			} else
				ShowShopList(userId, longtitude, latitude);
		} else
			Toast.makeText(FavoriteActivity.this, "Something is wrong!",
					Toast.LENGTH_LONG).show();
	}

	private void ShowShopList(String userId, String longtitude, String latitude) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("userId", userId);
		params.add("longtitude", longtitude);
		params.add("latitude", latitude);
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

	private void tranferToList(String response) {
		try {

			final JSONArray json = new JSONArray(response);

			mData = json;
			mListView = new ListView(this);

			mListView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			setContentView(mListView);
			mAdapter = new ShopListAdapter(this, mData);
			mListView.setAdapter(mAdapter);
			mListView
					.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);

			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ArrayList<String> result = new ArrayList<String>();

					JSONObject obj = mAdapter.getItem(position);
					String sellerId = null;

					try {
						sellerId = obj.getString("sellerId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Intent intent = new Intent();
					intent.putExtra("sellerId", sellerId);

					intent.setClass(FavoriteActivity.this,
							ViewShopItemActivity.class);
					startActivity(intent);
					FavoriteActivity.this.finish();
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		intent.setClass(FavoriteActivity.this, UserMainActivity.class);
		SysApplication.close(TAG);
		startActivity(intent);
		return super.onKeyDown(keyCode, event);
	}

}
