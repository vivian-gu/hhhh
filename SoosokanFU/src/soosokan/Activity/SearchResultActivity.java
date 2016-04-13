//package soosokan.Activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONObject;
//
//import soosokan.Entity.ItemEntity;
//import soosokan.Entity.NetworkProperties;
//import soosokan.Entity.SysApplication;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Toast;
//
//import com.example.soosokanfu.R;
//
//public class SearchResultActivity extends Activity {
//	private static final String SERVICE_URL = NetworkProperties.nAddress
//			+ "/search";
//	private static final String TAG = "SEARCHRESULT";
//	private List<ItemEntity> list = new ArrayList<ItemEntity>();
//
//	public SearchResultActivity() {
//		ItemEntity item = null;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		SysApplication.addActivity(this, TAG);
//		setContentView(R.layout.activity_shoplist);
//	}
//
//	public void postData(View vw) {
//
//		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this,
//				"Posting data...");
//		wst.execute(new String[] { SERVICE_URL });
//	}
//
//	private class WebServiceTask extends AbstractWebServiceTask {
//
//		public WebServiceTask(int taskType, Context mContext,
//				String processMessage) {
//			super(taskType, mContext, processMessage);
//		}
//
//		@Override
//		protected void hideKeyboard() {
//			// TODO Auto-generated method stub
//			InputMethodManager inputManager = (InputMethodManager) SearchResultActivity.this
//					.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//			inputManager.hideSoftInputFromWindow(SearchResultActivity.this
//					.getCurrentFocus().getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
//		}
//
//		@Override
//		public void handleResponse(String response) {
//			// TODO Auto-generated method stub
//			handleResponseLocal(response);
//		}
//
//	}
//
//	public void handleResponseLocal(String response) {
//		if (!response.equals(null)) {
//			Toast.makeText(this, response, Toast.LENGTH_LONG).show();
//		}
//	}
//}
