package com.user.registration.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.*;

public class UserNameValidator implements ConstraintValidator<UserName, String> {

	private static final int MINIMAL_LENGTH = 5;
	
	@Override
	public void initialize(UserName userName) { }

	@Override
	public boolean isValid(String userName, ConstraintValidatorContext context) {
		if (isBlank(userName) || length(userName) < MINIMAL_LENGTH) {
			return false;
		}
		
		return isAlphanumeric(userName);
	}
}
