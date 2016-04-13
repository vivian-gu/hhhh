package soosokan.Activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ViewShopItemActivity extends Activity {
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/bySeller";
	private static final String SUB_URL = NetworkProperties.nAddress
			+ "/seller/sub";

	public static final String TAG = "ITEMMANAGE";
	private ListView mListView;
	private JSONArray mData;
	private String itemid;
	SharedPreferences sp;
	private ItemListAdapter mAdapter;
	String sellerId;
	ArrayList<HashMap<String, Object>> ItemList_item;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		Intent intent = this.getIntent();
		sellerId = intent.getStringExtra("sellerId");
		ItemList_item = new ArrayList<HashMap<String, Object>>();
		initData();
		setContentView(R.layout.activity_shop_main);
	}

	public void subscribe(View v) {
		sp = this.getSharedPreferences("userInfo", 0);
		String userId = sp.getString("Facebook_id", "");

		if (sp != null) {
			if (userId.equals("")) {
				Toast.makeText(getApplicationContext(), "Please Login!",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getApplicationContext(),
						AccountActivity.class);
				startActivity(intent);
			} else {
				WebServiceTask wst = new WebServiceTask(
						WebServiceTask.POST_TASK, this, "Posting data...");

				wst.addNameValuePair("sellerId", sellerId);
				wst.addNameValuePair("userId", userId);
				wst.execute(new String[] { SUB_URL });
			}
		} else
			Toast.makeText(getApplicationContext(), "SP is null!",
					Toast.LENGTH_LONG).show();
	}

	private void initData() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("sellerId", sellerId);

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

	private class WebServiceTask extends AbstractWebServiceTask {

		public WebServiceTask(int taskType, Context mContext,
				String processMessage) {
			super(taskType, mContext, processMessage);
		}

		@Override
		protected void hideKeyboard() {
			InputMethodManager inputManager = (InputMethodManager) ViewShopItemActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(ViewShopItemActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		public void handleResponse(String response) {
			handleResponseLocal(response);
		}

		private void handleResponseLocal(String response) {
			if (response.equals("true")) {
				Toast.makeText(getApplicationContext(), "Subscribe success",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "You Have Subscribed This Store.",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void tranferToList(String response) {
		try {

			JSONArray json = new JSONArray(response);

			mData = json;
			mListView = (ListView) findViewById(R.id.itemlist);
			mAdapter = new ItemListAdapter(this, mData);
			mListView.setAdapter(mAdapter);
			mListView
					.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					JSONObject item = mAdapter.getItem(position);
					try {
						itemid = item.getString("itemId");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent();
					
					try {
						intent.putExtra("name", item.getString("name"));
						intent.putExtra("sellerId",
								item.getString("sellerId"));
						intent.putExtra("picture",
								item.getString("picture"));
						intent.putExtra("price", item.getString("price"));
						intent.putExtra("itemId", item.getString("itemId"));
						Intent intent1 = getIntent();
						String sellerAdd = intent1.getStringExtra("sellerAdd");
						intent.putExtra("sellerAdd", sellerAdd);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					intent.setClass(ViewShopItemActivity.this,
							ItemDetailActivity.class);
					startActivity(intent);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		intent.setClass(ViewShopItemActivity.this, FavoriteActivity.class);
		this.finish();
		startActivity(intent);
		return super.onKeyDown(keyCode, event);
	}
}
