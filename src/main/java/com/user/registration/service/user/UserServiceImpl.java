package com.user.registration.service.user;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.registration.dto.user.UserDto;
import com.user.registration.repository.entity.user.UserEntity;
import com.user.registration.repository.user.UserRepository;
import com.user.registration.service.ServiceException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger log =  LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDto createUser(UserDto user) throws ServiceException {
		log.debug("creating user: " + user);
		
		List<UserEntity> existingUsers = userRepository.findByNameOrEmail(user.getName(), user.getEmail());
		if (isNotEmpty(existingUsers)) {
			throw new ServiceException("User with following name or email already exists");
		}
		
		UserEntity savedUser = userRepository.save(toEntity(user));
		log.debug("user created: " + savedUser);
		
		return toDto(savedUser);
	}

	@Override
	public List<UserDto> listUsers() {
		List<UserDto> users = new ArrayList<>();
		
		log.debug("getting all users");
		
		for (UserEntity entity : userRepository.findAll()) {
			users.add(toDto(entity));
		}
		
		log.debug("users selected: " + users.size());
		
		return users;
	}

	private static UserEntity toEntity(UserDto user) {
		UserEntity entity = new UserEntity();
		copyProperties(user, entity);
		return entity;
	}

	private static UserDto toDto(UserEntity user) {
		UserDto dto = new UserDto();
		copyProperties(user, dto);
		return dto;
	}
}