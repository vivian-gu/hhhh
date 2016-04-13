package soosokan.Activity;

import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.soosokanfu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ResourceAsColor")
public class ItemofOneShopListActivity extends Activity {
	private static String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/shopItem";
	private static final String TAG = "ITEMLIST";

	private ListView mListView;
	private JSONArray mData;
	private ItemListAdapter mAdapter;
	SharedPreferences sp;
	String sellerId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);

		initData();
		setContentView(R.layout.mslist_main);
	}

	private void initData() {
		Intent intent = getIntent();
		List<String> result = intent.getStringArrayListExtra("result");
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		String keyword = intent.getStringExtra("keyword");
		String sellerId = intent.getStringExtra("sellerId");
	

		params = new RequestParams("result", result);
		params.add("sellerId", sellerId); // 此处应有 sellerId
		params.add("keyword", keyword);
		// }
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;

	}

	private void tranferToList(String response) {
		try {
			
			if (response.equals("[]")) {
				
				setContentView(R.layout.mslist_main);
				
				TextView noresults = (TextView) this
						.findViewById(R.id.noresults);
				noresults.setText("No Results.");
				noresults.setTextColor(R.color.search_back);
			} else {
				JSONArray json = new JSONArray(response);

				mData = json;
				mListView = new ListView(this);
				mListView.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				setContentView(mListView);
				mAdapter = new ItemListAdapter(this, mData);
				mListView.setAdapter(mAdapter);
				mListView
						.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);

				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						JSONObject item = mAdapter.getItem(position);

						Toast.makeText(getApplicationContext(),
								item.toString(), Toast.LENGTH_LONG).show();

						Intent intent1 = getIntent();
						String sellerAdd = intent1.getStringExtra("sellerAdd");
						
//						Toast.makeText(getApplicationContext(), sellerAdd,
//								Toast.LENGTH_LONG).show();
						
						Intent intent = new Intent();
						try {
							intent.putExtra("name", item.getString("name"));
							intent.putExtra("sellerId",
									item.getString("sellerId"));
							intent.putExtra("picture",
									item.getString("picture"));
							intent.putExtra("price", item.getString("price"));
							intent.putExtra("itemId", item.getString("itemId"));
							intent.putExtra("sellerAdd", sellerAdd);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						intent.setClass(ItemofOneShopListActivity.this,
								ItemDetailActivity.class);
						startActivity(intent);
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
