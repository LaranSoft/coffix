package it.halfone.coffix.exception;

public class CoffixException extends Exception{

	private static final long serialVersionUID = -8653296725663999284L;

	private String errorCode;
	
	public CoffixException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
