package com.sismics.Exceptions;

public class ExpiredJWTException extends Exception {
    public Exception innerException;

    public ExpiredJWTException(String error, Exception innerException) {
        super(error);
        this.innerException = innerException;
    }
}
