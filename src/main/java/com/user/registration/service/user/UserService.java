package com.user.registration.service.user;

import java.util.List;

import com.user.registration.dto.user.UserDto;
import com.user.registration.service.ServiceException;

public interface UserService {
	UserDto createUser(UserDto user) throws ServiceException;
	List<UserDto> listUsers();
}
