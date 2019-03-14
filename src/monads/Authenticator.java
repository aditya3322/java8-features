package monads;

public class Authenticator {

	public User login(String userid, String pwd) throws Exception {
		System.out.println("Inside login system");
		 throw new Exception("password incorrect");
		//return new User(userid, "Aditya");
	}
	
	public User gmailLogin(String userid, String pwd) throws Exception {
		System.out.println("Inside gmail login");
		// throw new Exception("gmail password incorrect");
		return new User(userid, "Aditya");
	}
	
	public void twoFactor(User user, long pwd) {
		System.out.println("Inside twoFactor: " + user.getUserid());
		throw new RuntimeException("twoFactor Incorrect Key");
	}
}