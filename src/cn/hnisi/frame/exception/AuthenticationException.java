package cn.hnisi.frame.exception;

public class AuthenticationException extends AppException{
	public AuthenticationException(){
		super();
	}
	
	public AuthenticationException(String message){
		super(message);
	}
	
	public AuthenticationException(Throwable cause){
		super(cause);
	}
	
	public AuthenticationException(String message, Throwable cauese){
		super(message, cauese);
	}
}
