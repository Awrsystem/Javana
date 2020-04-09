package br.com.javana.business.BusinessException;

public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;

	private String message;

	public BusinessException(Throwable e, String message) {
		super(e);
		this.message = message;
	}

	public BusinessException(Throwable e) {
		super(e);
	}

	public BusinessException(String msg) {
		this.message = msg;
	}

	public BusinessException() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
