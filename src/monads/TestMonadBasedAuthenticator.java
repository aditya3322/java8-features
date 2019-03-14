package monads;

import java.net.URL;
import java.util.function.Function;


public class TestMonadBasedAuthenticator {
	
	public static void main(String[] args) throws Exception{
		final URL dashboard = new URL("http://dashboard");
		final String userid = "admin";
		final String pwd = "1234";
		Authenticator authenticator =  new Authenticator();
		
		Try<URL> url = Try.with(() -> authenticator.login(userid, pwd))
					   .recoverWith(user -> Try.with(() -> authenticator.gmailLogin(userid, pwd)))
					   .chain(user -> Try.with(() -> {
						   long twoFactorPwd  = 1234L;
						   authenticator.twoFactor(user, twoFactorPwd);
						   return dashboard;
					   }))
					   .orElse(Try.with(() ->  new URL("http://login")));
		Dispatcher.redirect(url.get());
	
	}

}
interface SupplierThrowsException<T, E extends Exception> {
	T get() throws E;
}

abstract class Try<T> {
	public static <T,E extends Exception> Try<T> with(SupplierThrowsException<T, E> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Exception e) {
			return new Failure<>(e);
		}
	}
	abstract public <U> Try<U> chain(Function<T, Try<U>> f);
	abstract T get();
	abstract <U> Try<U> recoverWith(Function<Exception, Try<U>> f);
	abstract <U> Try<U> orElse(Try<U> other);
}

class Success<T> extends Try<T> {
	private final T value;
	Success(T value) { this.value = value;}
	
	public <U> Try<U> chain(Function<T, Try<U>> f) {
		try {
			return f.apply(value);
		} catch (Exception e) {
			return new Failure<>(e);
		}
	}
	
	public <U> Try<U> recoverWith(Function<Exception, Try<U>> f) { return (Try<U>) this; }
	<U> Try<U> orElse(Try<U> other) { return (Try<U>) this; }
	
	T get() { return value; }
	
}

class Failure<T> extends Try<T> {
	private final Exception e;
	Failure(Exception e) {this.e = e;} 
	
	public <U> Try<U> chain(Function<T, Try<U>> f) {
		return (Try<U>) this;
	}
	
	public <U> Try<U> recoverWith(Function<Exception, Try<U>> f) {
		try { return f.apply(e); }
		catch (Exception e) {
			return new Failure<>(e);
		}
	}
	<U> Try<U> orElse(Try<U> other) {
		return other;
	}
	
	T get() { throw new RuntimeException(e); }
}

