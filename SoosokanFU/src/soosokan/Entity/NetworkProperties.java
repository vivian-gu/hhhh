package soosokan.Entity;

public class NetworkProperties {
    public static String nAddress = "http://10.6.43.67:8080/Jersey1/rest"; // default. static because for now all nodes are on the same machine
//    public static String nAddress = "http://soosokanserver.cloudapp.net/Jersey1/rest"; // default. static because for now all nodes are on the same machine


    public static String byteToString(byte[] response) {
		String a="";
		for(int i=0;i<response.length;i++)
			a+=(char)response[i];
		return a;
	}
}
