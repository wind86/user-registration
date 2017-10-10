package com.user.registration.controller.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.registration.dto.user.UserDto;
import com.user.registration.service.ServiceException;
import com.user.registration.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/")
	public UserDto saveUser(@Valid @RequestBody UserDto user) throws ServiceException {
		return userService.createUser(user);
	}
	
	@GetMapping("/")
	public List<UserDto> getUsers() {
		return userService.listUsers();
	}
}
