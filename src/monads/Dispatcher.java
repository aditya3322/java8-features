package monads;

import java.net.URL;

public class Dispatcher {
	
	public static void redirect(URL url) {
		System.out.println("redirect to " + url.toString());
	}

}
