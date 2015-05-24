package com.aristotle.core.exception;

import java.util.HashMap;
import java.util.Map;

public class FieldsAppException extends AppException {

    private static final long serialVersionUID = 1L;
    private final Map<String, String> fieldErrors = new HashMap<String, String>();

    public FieldsAppException() {
        super();
    }

    public FieldsAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FieldsAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldsAppException(String message) {
        super(message);
    }

    public FieldsAppException(Throwable cause) {
        super(cause);
    }

    public void addFieldError(String fieldName, String error){
        fieldErrors.put(fieldName, error);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

}
