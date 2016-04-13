package soosokan.Activity;

import java.net.HttpURLConnection;
import java.net.URL;

import soosokan.Entity.NetworkProperties;
import soosokan.Entity.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soosokanfu.R;

public class ItemDetailActivity extends Activity {

//	private static final String SERVICE_URL = NetworkProperties.nAddress
//			+ "/item/byItemId";
	private TextView itemName, itemPrice, selleraddress;
	// itemKeyword, itemDiscountno;
	private ImageView ItemPic;
	private String itemId, itemid, sellerId, name, keyword, pictureURL, price, sellerAdd;
	private int discount;
	private Bitmap bm;
	private static final String TAG = "ITEMDETAIL";

	public ItemDetailActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		SysApplication.addActivity(this, TAG);

		Intent intent = this.getIntent();
		itemId = intent.getStringExtra("itemId");
		itemName = (TextView) this.findViewById(R.id.itemName);
		itemPrice = (TextView) this.findViewById(R.id.itemPrice);
		selleraddress = (TextView) this.findViewById(R.id.address);
		// itemKeyword = (TextView) this.findViewById(R.id.itemKeyword);
		// itemDiscountno = (TextView) this.findViewById(R.id.itemDiscountno);
		ItemPic = (ImageView) this.findViewById(R.id.ItemPic);

		name = intent.getStringExtra("name");
		price = intent.getStringExtra("price");
		sellerId = intent.getStringExtra("sellerId");
		pictureURL = intent.getStringExtra("picture");
		sellerAdd = intent.getStringExtra("sellerAdd");

		itemName.setText(name);
		itemPrice.setText(price + "€");
		selleraddress.setText(sellerAdd);

		
		ItemPic.setTag(pictureURL);
		new CanvasImageTask().execute(ItemPic);
		// getOneItem(itemId);
	}
	


	// private void getOneItem(String itemId) {
	// AsyncHttpClient client = new AsyncHttpClient();
	// RequestParams params = new RequestParams();
	// params.add("itemId", itemId);
	// client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {
	// @Override
	// public void onFailure(int arg0, Header[] arg1, byte[] arg2,
	// Throwable arg3) {
	// if (arg0 == 404)
	// Toast.makeText(getApplicationContext(),
	// "Requested Resource not found", Toast.LENGTH_LONG)
	// .show();
	// else if (arg0 == 500)
	// Toast.makeText(getApplicationContext(),
	// "Something wrong at the server end",
	// Toast.LENGTH_LONG).show();
	// else
	// Toast.makeText(getApplicationContext(), "Unexpected error",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	// String response = NetworkProperties.byteToString(arg2);
	// JSONObject myJsonObject;
	// try {
	// myJsonObject = new JSONObject(response);
	// itemid = myJsonObject.getString("itemId");
	// sellerId = myJsonObject.getString("sellerId");
	// name = myJsonObject.getString("name");
	//
	// // keyword = myJsonObject.getString("keyword");
	// String priceString = myJsonObject.getString("price");
	// // price = Float.valueOf(priceString);
	// // String timeString = myJsonObject.getString("time");
	// picture = myJsonObject.getString("picture");
	//
	// bm = SaveBitMap.StringToBitMap(picture);
	// if (bm == null) {
	// }
	//
	// String discountString = myJsonObject.getString("discount");
	// discount = Integer.valueOf(discountString);
	//
	// itemName.setText(name);
	// itemPrice.setText(priceString+"��");
	// // itemKeyword.setText(keyword);
	// // itemDiscountno.setText(String.valueOf(discount));
	// ItemPic.setImageBitmap(bm);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	public void viewShop(View v) {

		System.out.println("View this shop :"+sellerId);
		Intent intent;
		if(sellerId.startsWith("tesco")){
			Uri uri = Uri.parse("http://www.tesco.ie");
			intent = new Intent(Intent.ACTION_VIEW, uri);
		}else if(sellerId.startsWith("boots")){
			Uri uri = Uri.parse("http://www.boots.ie");
			intent = new Intent(Intent.ACTION_VIEW, uri);
		}else if(sellerId.startsWith("argos")){
			Uri uri = Uri.parse("http://www.argos.ie");
			intent = new Intent(Intent.ACTION_VIEW, uri);
		}else if(sellerId.startsWith("arnotts")){
			Uri uri = Uri.parse("http://www.arnotts.ie");
			intent = new Intent(Intent.ACTION_VIEW, uri);
		}else if(sellerId.startsWith("pcworld")){
			Uri uri = Uri.parse("http://www.pcworld.ie");
			intent = new Intent(Intent.ACTION_VIEW, uri);
		}else{
			intent = new Intent();
			intent.putExtra("sellerId", sellerId);
			intent.putExtra("sellerAdd", sellerAdd);
			Toast.makeText(getApplicationContext(), "View shop :" + sellerId,
					Toast.LENGTH_LONG).show();
			intent.setClass(ItemDetailActivity.this, ViewShopItemActivity.class);
		}
		startActivity(intent);
		ItemDetailActivity.this.finish();
	}

}
