package com.thoughtworks.rslist.exceptions;

public class InvalidRequestParamException extends Exception {
    public InvalidRequestParamException(String errorMessage) {
        super(errorMessage);
    }
}
