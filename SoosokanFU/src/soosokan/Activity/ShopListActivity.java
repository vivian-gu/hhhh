package soosokan.Activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShopListActivity extends Activity {
	private static final String TAG = "SHOPLIST";
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/search";

	private ListView mListView;
	private JSONArray mData;
	private ShopListAdapter mAdapter;
	long t1;
	String keyword;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.shoplist_main);
		mListView = (ListView) findViewById(R.id.shoplist_list);

		t1 =System.currentTimeMillis();
		initData();
	}

	private void initData() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		Intent intent = getIntent(); // ���ڼ���������ͼ����
		keyword = intent.getStringExtra("searchKeyword");

		String longtitude = intent.getStringExtra("longtitude");
		String latitude = intent.getStringExtra("latitude");

		params.add("keyword", keyword);
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

				long t2 = System.currentTimeMillis();
//				Toast.makeText(getApplicationContext(), "耗时："+(t2-t1),
//						Toast.LENGTH_LONG).show();
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
					JSONArray array;
					String sellerId = "";
					String sellerAdd = "";
					
					Intent intent = new Intent();
					try {
						array = obj.getJSONArray("result");
						sellerId = obj.getString("sellerId");
						sellerAdd = obj.getString("address");
						for (int i = 0; i < array.length(); i++) {
							result.add(array.getString(i));
						}
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					intent.putExtra("keyword", keyword);
					intent.putExtra("sellerId", sellerId);
					intent.putExtra("sellerAdd", sellerAdd);
					intent.putStringArrayListExtra("result", result);
					intent.setClass(ShopListActivity.this,
							ItemofOneShopListActivity.class);
					startActivity(intent);
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
		return true;
	}
}
