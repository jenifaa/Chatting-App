import java.util.HashMap;

public class IDandPasswords {
HashMap<String,String> logininfo = new HashMap<String,String>();
//HashMap<String,String> logininfoClient = new HashMap<String,String>();
 IDandPasswords(){
	 logininfo.put("Bro","pizza");
	 logininfo.put("Bro2","pizza2");
	 logininfo.put("Bro3","pizza3");
	
	 
	 
	 
 }
 
 protected HashMap getLoginInfo(){
	 return logininfo;
	 
 }

}
