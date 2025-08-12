import java.util.HashMap;

public class IDandPassword {
//HashMap<String,String> logininfo = new HashMap<String,String>();
HashMap<String,String> logininfoClient = new HashMap<String,String>();
 IDandPassword(){
	 
	 logininfoClient.put("Bro4","pizza");
	 logininfoClient.put("Bro5","pizza2");
	 logininfoClient.put("Bro6","pizza3");
	 
	 
	 
 }

protected HashMap<String, String> getLoginInfoCleint() {
	// TODO Auto-generated method stub
	return logininfoClient;
}
}
