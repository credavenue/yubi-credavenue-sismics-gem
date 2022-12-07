package com.sismics.Exceptions;

public class InvalidJWTException extends Exception {
    public Exception innerException;

    public InvalidJWTException(String error, Exception innerException) {
        super(error);
        this.innerException = innerException;
    }
}
