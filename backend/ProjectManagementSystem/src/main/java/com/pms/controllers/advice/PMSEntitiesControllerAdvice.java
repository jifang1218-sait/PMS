package com.pms.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pms.controllers.exceptions.DuplicateObjectsException;
import com.pms.controllers.exceptions.RequestValueMismatchException;
import com.pms.controllers.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class PMSEntitiesControllerAdvice {

	@ExceptionHandler(value= {BindException.class})
	ResponseEntity<String> bindExceptionHandler(BindException ex) {
		return ResponseEntity.badRequest()
				.body(ex.getAllErrors().get(0).getDefaultMessage());
	}
	
	@ExceptionHandler({DuplicateObjectsException.class})
	ResponseEntity<String> duplicateObjectsExceptionHandler(DuplicateObjectsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler({ResourceNotFoundException.class})
	ResponseEntity<String> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
