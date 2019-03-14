package monads;

import java.net.URL;

public class TestLegacyAuthenticator {
	
	public static void main(String[] args) throws Exception{
		final URL dashboard = new URL("http://dashboard");
		final URL loginPage = new URL("http://login");
		
		
		final String userid = "admin";
		final String pwd = "1234";
		User user = null;
		
		Authenticator authenticator =  new Authenticator();
		
		try {
			user = authenticator.login(userid, pwd);
		} catch (Exception es) {
			System.out.println("system login failed = " + es.getMessage());
			try {
				user = authenticator.gmailLogin(userid, pwd);
			} catch (Exception eg) {
				System.out.println("gmail loging failed = " + eg.getMessage());
				Dispatcher.redirect(loginPage);
				return;
			}
		}
		
		URL target; 
		try {
			long twoFactorPwd  = 1234L;
			authenticator.twoFactor(user, twoFactorPwd);
			target = dashboard;
		} catch (RuntimeException e) {
			target = loginPage;
		}
		Dispatcher.redirect(target);
	}

}


