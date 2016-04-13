package soosokan.Activity;

import soosokan.Entity.SysApplication;
import soosokan.Tool.CameraActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.soosokanfu.R;

public class AdsListActivity extends Activity {

	private static final String TAG = "ADSLIST";
	private TextView adstime, adstitle, adsdes;
	private String adstimeString, adstitleString, adsdesString;

	public AdsListActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_ads_detail);

		Intent intent = getIntent();

		String id = intent.getStringExtra("adsId");
		String title = intent.getStringExtra("title");
		String description = intent.getStringExtra("description");
		String distance = intent.getStringExtra("distance");
		String attribute = intent.getStringExtra("attribute");

		adstime = (TextView) findViewById(R.id.adstime);
		adstitle = (TextView) findViewById(R.id.adstitle);
		adsdes = (TextView) findViewById(R.id.adsdes);

		adstitle.setText(title);
		adsdes.setText(description);
	}

}
