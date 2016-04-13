package soosokan.Tool;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

/** 
 * @author BigWanng 
 * @version 2013-6-6 下午1:55:33 
 * TODO 处理图片压缩的工具类
 */
public class PictureUtils {
	private final static int compressVal=75;
	/**
	  * 把bitmap转换成String
	  * 将图片保存到本地
	  * @param filePath
	  * @return
	  */
	 public static String bitmapToString(Bitmap bitmap) {
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
		   bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);
		   
		   byte[] b = baos.toByteArray();
		   return Base64.encodeToString(b, Base64.DEFAULT);
		 }
	
	 /**
	  * 计算图片的缩放�??
	  * 如果图片的原始高度或者宽度大与我们期望的宽度和高度，我们�?要计算出缩放比例的数值�?�否则就不缩放�??
	  * heightRatio是图片原始高度与压缩后高度的倍数�?
	  * widthRatio是图片原始宽度与压缩后宽度的倍数�?
	  * inSampleSize就是缩放�? ，取heightRatio与widthRatio中最小的值�??
	  * inSampleSize�?1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来�?1/2(图片为原1/4)�?
	  * @param options
	  * @param reqWidth
	  * @param reqHeight
	  * @return
	  */
	 public static int calculateInSampleSize(BitmapFactory.Options options,
	     int reqWidth, int reqHeight) {
	   // Raw height and width of image
	   final int height = options.outHeight;
	   final int width = options.outWidth;
	   int inSampleSize = 1;
	
	   if (height > reqHeight || width > reqWidth) {
	
	     // Calculate ratios of height and width to requested height and width 
	     final int heightRatio = Math.round((float) height / (float) reqHeight);
	     final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	     // Choose the smallest ratio as inSampleSize value, this will guarantee
	     // a final image with both dimensions(尺寸) larger than or equal to the requested height and width.
	     inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	   }
	
	   return inSampleSize;
	 }
	
	 /**
	  * 根据路径获得图片并压缩返回bitmap用于显示
	  * 
	  * @param imagesrc
	  * @return
	  */
	 public static Bitmap getSmallBitmap(String filePath,int w,int h) {
		   final BitmapFactory.Options options = new BitmapFactory.Options();
		   
		   //该�?�设为true那么将不返回实际的bitmap不给其分配内存空间�?�里面只包括�?些解码边界信息即图片大小信息
		   options.inJustDecodeBounds = true;//inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大�?
		   BitmapFactory.decodeFile(filePath, options);
		
		   // Calculate inSampleSize
		   options.inSampleSize = calculateInSampleSize(options, w, h);
		
		   // Decode bitmap with inSampleSize set
		   options.inJustDecodeBounds = false;//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		   Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeFile()按指定大小取得图片缩略图
		   
		   return bitmap;
		 }
	
	 /**
	  * 保存到本�?
	  * @param bitmap
	  */
	 public static void saveBitmap(Bitmap bitmap,String savePath){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File file = new File(savePath);
			try {
				file.createNewFile();
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));

				bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);
				os.write(baos.toByteArray());

				os.flush();
				os.close();
				
			} catch (IOException e) {
				Log.d("BITMAP", e.getMessage());
			} finally {
				if (bitmap != null) {
					bitmap.recycle();
				}
			} 
		 }
	 
	 /***
	 * 根据路径删除图片
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean deleteTempFile(String path)throws IOException{
        boolean isOk=true;
        File file = new File(path);
        if(file!=null){
            if(file.exists()){
                if(!file.delete()){
                    isOk=false;
                }
            }
        }
        return isOk;
    }
    
    /***
     * 获取文件扩展�?
     * @param filename
     * @return 返回文件扩展�?
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    

}
