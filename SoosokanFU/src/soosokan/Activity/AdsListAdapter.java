package soosokan.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.soosokanfu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private JSONArray mData = null;
	String title, time, url;

	public AdsListAdapter(Context ctx, JSONArray list) {
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
		TextView mTitle;
		TextView mSellerName;
		ImageView mIcon;

	}

	@Override
	public int getCount() {
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
			convertView = mInflater.inflate(R.layout.row_ads, null);

			ViewHolder holder = new ViewHolder();
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.row_AdsTitle);
//			holder.mTime = (TextView) convertView
//					.findViewById(R.id.row_AdsTime);
			holder.mSellerName = (TextView) convertView
					.findViewById(R.id.row_SellerName);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.shop_logo);
			convertView.setTag(holder);

			try {
//				System.out.println("Positeion:"+position);
				
				title = getItem(position).getString("title");
				time = getItem(position).getString("time");
				url = getItem(position).getString("picture");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final String subUrl = url.replaceAll("[^\\w]", "");
//			Bitmap bitmap = ImageDownLoader.showCacheBitmap(subUrl);
//			if (bitmap != null) {
//				holder.mIcon.setImageBitmap(bitmap);
//			} else {
//				holder.mIcon.setImageDrawable(mContext.getResources().getDrawable(
//						R.drawable.ic_empty));
//			}

			System.out.println("ads list url:"+url);
			
			holder.mIcon.setTag(url);
			new CanvasImageTask().execute(holder.mIcon);
			holder.mSellerName.setText(time);
			holder.mTitle.setText(title);
		}
		return convertView;
	}
}
