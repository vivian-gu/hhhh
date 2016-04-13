package soosokan.Entity;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import android.graphics.Bitmap;

public class Adsmod {
	

		public int 	id;
		public String title;
		public String description;
		public String sellername;
		public String address;
		public String time;
//		public byte[] pic;
		public String pic;
		
		public Adsmod (String sellername, String title, String address, String description, String time, String pic)
		  {
		    this.sellername = sellername;
		    this.title = title;
		    this.address = address;
		    this.description = description;
		    this.time = time;
//		    final ByteArrayOutputStream os = new ByteArrayOutputStream(); 
//		    bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
//		    pic= os.toByteArray();
		    this.pic = pic;
		  }
		  
		  public Adsmod ()
		  {
			  this.sellername = "";
			  this.title = "";
			  this.address= "";
			  this.description = "";
			  this.time = "";
			  this.pic = "";
		   
		  }
		  
		  public String toString()
		  {
		    return  "[" + id + "] " + sellername + "," + title + ", " + address + ","
		  + description + "," + time + "," + pic;
		  }

	

}
