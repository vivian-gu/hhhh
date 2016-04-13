package soosokan.Activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import soosokan.Entity.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soosokanfu.R;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.LoginButton;

public class AccountActivity extends Activity {
//	private UiLifecycleHelper uihelper;
//	private static final String TAG = "ACCOUNT";
//	private SharedPreferences sp;
//	private TextView username, useremail, usergender;
//	private long mExitTime = 0;
//
//	void showMsg(String string) {
//		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
//				.show();
//	}
//
//	private Session.StatusCallback callback = new Session.StatusCallback() {
//		@Override
//		public void call(Session session, SessionState state,
//				Exception exception) {
//			onSessionStateChange(session, state, exception);
//		}
//	};
//
//	void onSessionStateChange(Session session, SessionState state,
//			Exception exception) {
//		username = (TextView) this.findViewById(R.id.sellername);
//		useremail = (TextView) this.findViewById(R.id.selleremail);
//		usergender = (TextView) this.findViewById(R.id.sellergender);
//		if (state.isOpened()) {
//			Log.i("facebook", "Logged in...");
//			Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//				@Override
//				public void onCompleted(GraphUser user, Response response) {
//
//					if (user != null) {
//
//						username.setText(user.getName());
//						String facebookId = user.getId();
//						useremail.setText(user.getProperty("email") + "");
//						usergender.setText(user.getProperty("gender") + "");
//						Editor editor = sp.edit();
//						editor.putString("Facebook_id", facebookId);
//						editor.commit();
//					} else {
//						showMsg("its null");
//						showMsg(response.getError().getErrorMessage());
//					}
//				}
//			}).executeAsync();
//
//		} else if (state.isClosed()) {
//			Log.i("facebook", "Logged out...");
//			Editor editor = sp.edit();
//			editor.putString("Facebook_id", null);
//			editor.commit();
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		uihelper.onResume();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		uihelper.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		uihelper.onPause();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		uihelper.onDestroy();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		uihelper.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		SysApplication.addActivity(this, TAG);
//		setContentView(R.layout.account);
//		sp = this.getSharedPreferences("userInfo", 0);
//		uihelper = new UiLifecycleHelper(this, callback);
//		uihelper.onCreate(savedInstanceState);
//
//		try {
//			PackageInfo info = getPackageManager().getPackageInfo(
//					"com.example.soosokanfu", PackageManager.GET_SIGNATURES);
//			for (Signature signature : info.signatures) {
//				MessageDigest md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				String keyhash = Base64.encodeToString(md.digest(),
//						Base64.DEFAULT);
//				Log.d("KeyHash:", keyhash);
//			}
//		} catch (NameNotFoundException e) {
//
//		} catch (NoSuchAlgorithmException e) {
//
//		}
//
//		ArrayList<String> permission = new ArrayList<String>();
//		permission.add("email");
//		permission.add("public_profile");
//		permission.add("user_friends");
//
//		LoginButton btn = (LoginButton) findViewById(R.id.authButton);
//		btn.setPublishPermissions(permission);
//	}
//
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Intent intent = new Intent();
//		intent.setClass(AccountActivity.this, UserMainActivity.class);
//		SysApplication.close(TAG);
//		startActivity(intent);
//		return super.onKeyDown(keyCode, event);
//	}
}
