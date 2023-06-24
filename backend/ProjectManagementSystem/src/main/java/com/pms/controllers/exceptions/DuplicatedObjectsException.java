package com.pms.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedObjectsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicatedObjectsException() {
        this("Duplicated Object!");
    }
    public DuplicatedObjectsException(String message) {
        this(message, null);
    }
    public DuplicatedObjectsException(String message, Throwable cause) {
        super(message, cause);
    }
}
