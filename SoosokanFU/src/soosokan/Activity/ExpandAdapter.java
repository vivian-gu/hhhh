package soosokan.Activity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soosokanfu.R;


public class ExpandAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[]   mGroupStrings = {"Discount","Voucher","New Product","Other"};
    private JSONArray  mData = null;

    public ExpandAdapter(Context ctx, JSONArray list) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mGroupStrings = mContext.getResources().getStringArray(R.array.groups);
        mData = list;
    }

    public void setData(JSONArray list) {
        mData = list;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mData.length();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition).length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
        
    }

    @Override
    public JSONArray  getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public JSONObject getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition).getJSONObject(childPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        holder.mGroupCount = (TextView) convertView
                .findViewById(R.id.group_count);
        try {
			holder.mGroupCount.setText("[" + mData.getJSONArray(groupPosition).length() + "]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			holder.mGroupCount.setText("["+0+"]");
		}
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
        String account = "soosokan";
        
        try {
			account = getChild(groupPosition, childPosition).getString("sellerID");
			System.out.println(account);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
       
		if (account.startsWith("Argos")) {
			holder.mIcon.setBackgroundResource(R.drawable.argos);
		} else if (account.startsWith("Tesco")) {
			holder.mIcon.setBackgroundResource(R.drawable.tesco_logo);
//		} else if (account.startsWith("Boots")) {
//			holder.mIcon.setBackgroundResource(R.drawable.boots_logo);
//		} else if (account.startsWith("Arnotts")) {
//			holder.mIcon.setBackgroundResource(R.drawable.arnotts_logo);
//		} else if (account.startsWith("PC World") || account.startsWith("Currys")) {
//			holder.mIcon.setBackgroundResource(R.drawable.pcworld_logo);
		} else{
			try {
				String picUrl = getChild(groupPosition, childPosition).getString("picture");
				holder.mIcon.setTag(picUrl);
				new CanvasImageTask().execute(holder.mIcon);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			holder.mIcon.setBackgroundResource(R.drawable.head);
			}
        
        holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
        try {
			holder.mChildName.setText(getChild(groupPosition, childPosition).getString("title"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
        holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
        try {
			holder.mDetail.setText(getChild(groupPosition, childPosition).getString("description"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        
        return true;
    }

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;
    }

    private class ChildViewHolder {
        ImageView mIcon;
        TextView mChildName;
        TextView mDetail;
    }

}
