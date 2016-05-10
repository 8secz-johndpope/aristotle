package com.aristotle.web.exception;

import com.aristotle.core.exception.AppException;

public class NotLoggedInException extends AppException {

    private static final long serialVersionUID = 1L;

    public NotLoggedInException() {
        super();
    }

    public NotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(Throwable cause) {
        super(cause);
    }

}
