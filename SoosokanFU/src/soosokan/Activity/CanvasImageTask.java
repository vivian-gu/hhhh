package soosokan.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

public class CanvasImageTask extends AsyncTask<ImageView, Void, Bitmap> {
	private ImageView gView;
	
	public Bitmap getCacheImage(String itemId) {
		Bitmap bitmap = null;
		bitmap = ImageDownLoader.showCacheBitmap(itemId);
		if (bitmap != null) {
			return bitmap;
		} else {
		}
		return bitmap;
	}

	protected Bitmap doInBackground(ImageView... views) {
		Bitmap bmp = null;
		ImageView view = views[0];
		this.gView = view;
		String subUrl = null;
		if (view.getTag() != null) {
			String surl = view.getTag().toString();
			if(surl.startsWith("http")){
				;
			}else{
				surl = "https://soosokanstorage.blob.core.windows.net/images/"+surl;
			}

					URL url;
					try {
						url = new URL(surl);
						String temp =url.toString();
						 subUrl = temp.replaceAll("[^\\w]", "");
						bmp = getCacheImage(subUrl);
						
						if (bmp != null) {
							System.out.println("get image from cache");
							return bmp;
						} 
						
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoInput(true);
						conn.connect();
						InputStream stream = conn.getInputStream();
						bmp = BitmapFactory.decodeStream(stream);
						
						
						
						stream.close();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else {
					bmp = BitmapFactory.decodeFile(view.getTag().toString());
				}
		
		
		
		ImageDownLoader.addBitmapToMemoryCache(subUrl, bmp);
		
		return bmp;
	}

	protected void onPostExecute(Bitmap bm) {
		if (bm != null) {
			this.gView.setImageBitmap(bm);
			this.gView = null;
		}
	}

}