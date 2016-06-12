package com.aristotle.member.test.exception;

public class FieldDoNotExistsException extends Exception {

	public FieldDoNotExistsException() {
	}

	public FieldDoNotExistsException(String message) {
		super(message);
	}

	public FieldDoNotExistsException(Throwable cause) {
		super(cause);
	}

	public FieldDoNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldDoNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
