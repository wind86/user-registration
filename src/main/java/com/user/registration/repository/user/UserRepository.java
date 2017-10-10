package com.user.registration.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user.registration.repository.entity.user.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.name = :username or u.email = :email")
	List<UserEntity> findByNameOrEmail(@Param("username") String name, @Param("email") String email);
}