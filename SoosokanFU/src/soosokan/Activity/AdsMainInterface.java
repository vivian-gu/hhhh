package soosokan.Activity;

import com.example.soosokanfu.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class AdsMainInterface extends TabActivity implements
	OnCheckedChangeListener {

	private RadioButton radio_adsfull, radio_adstypes;

	private TabHost nTabHost;
	private Intent aIntent, fIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ads_main);
		
		this.aIntent = new Intent(this, AdsFullListActivity.class);
		this.fIntent = new Intent(this, AdsMainListActivity.class);

		radio_adsfull = (RadioButton) findViewById(R.id.radio_adsfull);
		radio_adstypes = (RadioButton) findViewById(R.id.radio_adstypes);

		radio_adsfull.setChecked(true);

		radio_adsfull.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);
		radio_adstypes.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);
		
		setupIntent();

	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_adsfull:
				this.nTabHost.setCurrentTabByTag("Radio_Adsfull");
				break;
			case R.id.radio_adstypes:
				this.nTabHost.setCurrentTabByTag("Radio_Adstypes");
				break;
			
			}
		}
	}

	private void setupIntent() {
		this.nTabHost = getTabHost();
		TabHost localTabHost = this.nTabHost;

		localTabHost.addTab(buildTabSpec("Radio_Adsfull", 
				R.drawable.tab_selector_home, this.aIntent));

		localTabHost.addTab(buildTabSpec("Radio_Adstypes",
			 R.drawable.tab_selector_favourite,
				this.fIntent));
	}

	private TabSpec buildTabSpec(String tag, int resIcon,
			final Intent content) {
		// TODO Auto-generated method stub
		return this.nTabHost
				.newTabSpec(tag)
				.setIndicator("tab", getResources().getDrawable(resIcon))
				.setContent(content);
	}


	
	
	
	

}
