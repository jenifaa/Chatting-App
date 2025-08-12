
public class Main {

	public static void main(String[] args) {
		IDandPasswords  idandpasswords = new IDandPasswords();
	
		Login loginPage = new Login(idandpasswords.getLoginInfo());

	}

}
