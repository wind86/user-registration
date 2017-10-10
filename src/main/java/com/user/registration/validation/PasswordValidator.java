package com.user.registration.validation;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.length;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	private static final Pattern ONE_DIGIT_PATTERN = Pattern.compile(".*(\\d){1,}.*");
	private static final Pattern ONE_UPPERCASE_PATTERN = Pattern.compile(".*([A-Z]){1,}.*");
	private static final Pattern ONE_LOWERCASE_PATTERN = Pattern.compile(".*([a-z]){1,}.*");
	
	private static final int MINIMAL_LENGTH = 8;
	
	private static final List<Pattern> VALIDATION_PATTERNS = asList(
			ONE_DIGIT_PATTERN, ONE_UPPERCASE_PATTERN, ONE_LOWERCASE_PATTERN); 
	
	@Override
	public void initialize(Password password) {}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (isBlank(password) || length(password) < MINIMAL_LENGTH) {
			return false;
		}
		
		for (Pattern pattern : VALIDATION_PATTERNS) {
			Matcher matcher = pattern.matcher(password);
			if (!matcher.matches()) {
				return false;
			}
		}
		
		return true;
	}
}