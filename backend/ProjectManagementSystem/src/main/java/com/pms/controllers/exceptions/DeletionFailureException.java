package com.pms.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class DeletionFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeletionFailureException() {
        this("Deletion failed due to dependencies!");
    }
    public DeletionFailureException(String message) {
        this(message, null);
    }
    public DeletionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
