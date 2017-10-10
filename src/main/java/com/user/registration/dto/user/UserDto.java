package com.user.registration.dto.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.user.registration.validation.Password;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

	Long id;
	
	@NotNull(message = "null.user.name")
	@Length(min = 5, message="user.name.length")
	String name;
	
	@NotNull(message = "null.user.email")
	@Email(message="user.email")
	String email;
	
	@NotNull(message = "null.user.password")
	@Password(message = "user.password")
	String password;
}