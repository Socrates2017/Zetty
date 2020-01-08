package com.zrzhen.zetty.http.http.http;

/**
 * @author chenanlian
 */
public class HttpException extends Exception{
	private static final long serialVersionUID = 1L;
	private final HttpResponseStatus status;
	private final String message;
	public HttpException(HttpResponseStatus status, String message){
		this.status = status;
		this.message = message;
	}
	public HttpException(HttpResponseStatus status){
		this(status,null);
	}
	
	public HttpException(){
		this(HttpResponseStatus.INTERNAL_SERVER_ERROR,null);
	}
	public HttpResponseStatus getStatus() {
		return status;
	}
	@Override
	public String getMessage() {
		return message;
	}
	
}
