package soosokan.Tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class StreamUtil {
		// TODO Auto-generated constructor stub
		 /**
		  * 返回字节数组
		  * 
		  * @param in输入的流
		  * @return
		  * @throws Exception
		  */

		 public static byte[] read(InputStream in) throws Exception {
		  ByteArrayOutputStream out = new ByteArrayOutputStream();
		  if (in != null) {
		   byte[] buffer = new byte[1024];
		   int length = 0;
		   while ((length = in.read(buffer)) != -1) {
		    out.write(buffer, 0, length);
		   }
		   out.close();
		   in.close();
		   return out.toByteArray();
		  }
		  return null;
		 }
		 
		 /**
		  * 采用HttpClient发�?�POST请求
		  * @param path 请求路径
		  * @param params 请求参数
		  * @throws Exception
		  */
		 public static boolean sendHttpClientPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception{
		  List<NameValuePair> param = new ArrayList<NameValuePair>();
		  if(params!=null && !params.isEmpty()){
		   for(Map.Entry<String, String> entry : params.entrySet()){
		    param.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		   }
		  }
		  UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param, encoding);
		  HttpPost post = new HttpPost(path);
		//  HttpGet get = new HttpGet();
		  post.setEntity(entity);
		  DefaultHttpClient client = new DefaultHttpClient();
		  HttpResponse response = client.execute(post);
		  if(response.getStatusLine().getStatusCode() == 200){
		//   response.getEntity().getContent();//获取服务器返回的数据
		   return true;
		  }
		  return false;
		 }

	

}
