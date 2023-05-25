package com.pms.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestValueMismatchException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public RequestValueMismatchException() {
        this("Request Value mismatch!");
    }
    public RequestValueMismatchException(String message) {
        this(message, null);
    }
    public RequestValueMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}