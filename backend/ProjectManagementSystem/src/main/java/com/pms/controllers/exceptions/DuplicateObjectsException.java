package com.pms.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateObjectsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateObjectsException() {
        this("Duplicated Object!");
    }
    public DuplicateObjectsException(String message) {
        this(message, null);
    }
    public DuplicateObjectsException(String message, Throwable cause) {
        super(message, cause);
    }
}
