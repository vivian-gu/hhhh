package soosokan.Activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soosokanfu.R;

public class ShopListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private JSONArray mData = null;

	public ShopListAdapter(Context ctx, JSONArray list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = list;
	}

	public void setData(JSONArray list) {
		mData = list;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	private class ViewHolder {
		TextView mName;
		TextView mDistance;
		ImageView mIcon;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.length();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return mData.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_shop, null);
		}

		ViewHolder holder = new ViewHolder();
		holder.mName = (TextView) convertView.findViewById(R.id.row_ShopName);
		holder.mDistance = (TextView) convertView.findViewById(R.id.distance);
		holder.mIcon = (ImageView) convertView.findViewById(R.id.shop_logo);
		convertView.setTag(holder);

		String distext = "--";
		String name = "--";

		try {
			String distance = getItem(position).getString("distance");

			double disdouble = Double.parseDouble(distance);
			BigDecimal b = new BigDecimal(disdouble);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			distext = f1 + " KM";

			name = getItem(position).getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.mName.setText(name);
		if (name.startsWith("Argos")) {
			holder.mIcon.setBackgroundResource(R.drawable.argos);
		} else if (name.startsWith("Tesco")) {
			holder.mIcon.setBackgroundResource(R.drawable.tesco_logo);
//		} else if (name.startsWith("Boots")) {
//			holder.mIcon.setBackgroundResource(R.drawable.boots_logo);
//		} else if (name.startsWith("Arnotts")) {
//			holder.mIcon.setBackgroundResource(R.drawable.arnotts_logo);
//		} else if (name.startsWith("PC World") || name.startsWith("Currys")) {
//			holder.mIcon.setBackgroundResource(R.drawable.pcworld_logo);
		} 
		holder.mDistance.setText(distext);
		return convertView;
	}

}
