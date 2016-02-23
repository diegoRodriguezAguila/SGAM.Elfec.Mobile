package com.elfec.sgam.model.exceptions;

/**
 * Api Error
 */
public class ApiException extends Exception {
    private String errors;
    private int code;

    public ApiException() {
        code = -1;
    }

    public ApiException(String message, int code) {
        super(message);
        errors = message;
        this.code = code;
    }

    @Override
    public String getMessage(){
        return errors;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
