package com.elfec.sgam.helpers.utils;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Helper methods for check of exceptions
 */
public class ExceptionChecker {

    /**
     * Checks if the exception is a {@link HttpException}, if it is, checks
     * the code status if matches the code provided
     * @param t throwable
     * @param httpCode http code status
     * @return true if the throwable is a {@link HttpException}, and its http code status matches
     * the http code status provided
     */
    public static boolean isHttpCodeException(Throwable t, int httpCode) {
        return t instanceof HttpException
                && (((HttpException) t).code() == httpCode);
    }
}
