package com.user.registration.dto.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationErrorDto {

	private List<FieldErrorDto> fieldErrors = new ArrayList<>();
	
	public void addFieldError(String field, String errorMessage) {
		fieldErrors.add(new FieldErrorDto(field, errorMessage));
	}
	
	public List<FieldErrorDto> getFieldErrors() {
		return Collections.unmodifiableList(fieldErrors);
	}
}
