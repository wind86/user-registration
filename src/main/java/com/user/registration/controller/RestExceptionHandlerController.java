package com.user.registration.controller;

import static java.util.Collections.*;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.registration.service.ServiceException;

@RestControllerAdvice
public class RestExceptionHandlerController {

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> processValidationError(ServiceException ex) {
		return new ResponseEntity<>(singletonMap("message", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
