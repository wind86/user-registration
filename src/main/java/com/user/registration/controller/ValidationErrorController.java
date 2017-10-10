package com.user.registration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.registration.dto.validation.ValidationErrorDto;

@RestControllerAdvice
public class ValidationErrorController {

	@Autowired
	@Qualifier("validationMessageSource")
	MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ValidationErrorDto processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		return processFieldErrors(result.getFieldErrors());
	}

	private ValidationErrorDto processFieldErrors(List<FieldError> fieldErrors) {
		ValidationErrorDto dto = new ValidationErrorDto();
		for (FieldError fieldError : fieldErrors) {
			dto.addFieldError(fieldError.getField(), resolveLocalizedErrorMessage(fieldError));
		}
		return dto;
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		return messageSource.getMessage(fieldError.getDefaultMessage(), fieldError.getArguments(), LocaleContextHolder.getLocale());
	}
}