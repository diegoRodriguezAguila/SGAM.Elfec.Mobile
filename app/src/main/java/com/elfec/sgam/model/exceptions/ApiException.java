package com.elfec.sgam.model.exceptions;

/**
 * Api Error
 */
public class ApiException extends Exception {
    private int code;

    public ApiException() {
        code = -1;
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
