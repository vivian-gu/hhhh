//package soosokan.Activity;
//
//import org.apache.http.Header;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import soosokan.Entity.NetworkProperties;
//import soosokan.Entity.SysApplication;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//import com.example.soosokanfu.R;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
//public class ItemListActivity extends Activity {
//	private static final String SERVICE_URL = NetworkProperties.nAddress
//			+ "/item/bySeller";
//	private static final String TAG = "ITEMLIST";
//
//	private ListView mListView;
//	private JSONArray mData;
//	private ItemListAdapter mAdapter;
//	SharedPreferences sp;
//	String sellerId;
//
//	public void ItemAdd(MenuItem item) {
//	}
//
//	public void ItemMod(MenuItem item) {
//	}
//
//	protected void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		SysApplication.addActivity(this, TAG);
//		sp = this.getSharedPreferences("userInfo", 0);
//		sellerId = sp.getString("EMAIL", "");
//		initData();
//		setContentView(R.layout.mslist_main);
//
//	}
//
//	private void initData() {
//		AsyncHttpClient client = new AsyncHttpClient();
//		RequestParams params = new RequestParams();
//		params.add("sellerId", sellerId);
//		client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				if (arg0 == 404)
//					Toast.makeText(getApplicationContext(),
//							"Requested Resource not found", Toast.LENGTH_LONG)
//							.show();
//				else if (arg0 == 500)
//					Toast.makeText(getApplicationContext(),
//							"Something wrong at the server end",
//							Toast.LENGTH_LONG).show();
//				else
//					Toast.makeText(getApplicationContext(), "Unexpected error",
//							Toast.LENGTH_LONG).show();
//			}
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] Response) {
//
//				String reponse = byteToString(Response);
//				tranferToList(reponse);
//			}
//
//			private String byteToString(byte[] response) {
//				String a = "";
//				for (int i = 0; i < response.length; i++)
//					a += (char) response[i];
//				return a;
//			}
//		});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int item_id = item.getItemId();
//		return true;
//	}
//
//	private void tranferToList(String response) {
//		try {
//
//			JSONArray json = new JSONArray(response);
//			mData = json;
//			mListView = new ListView(this);
//			mListView.setLayoutParams(new LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			setContentView(mListView);
//			mAdapter = new ItemListAdapter(this, mData);
//			mListView.setAdapter(mAdapter);
//			mListView
//					.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
//
//			mListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					JSONObject item = mAdapter.getItem(position);
//					Intent intent = new Intent();
//					try {
//						intent.putExtra("itemId", item.getString("itemId"));
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					intent.setClass(ItemListActivity.this,
//							ItemDetailActivity.class);
//					startActivity(intent);
//				}
//
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
