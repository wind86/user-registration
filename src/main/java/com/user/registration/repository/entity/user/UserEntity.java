package com.user.registration.repository.entity.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="users", uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name"), 
		@UniqueConstraint(columnNames = "email")})
public class UserEntity implements Serializable {

	static final long serialVersionUID = -1658467872636291795L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@NotNull
	@Column(name = "name", unique = true, nullable = false)
	String name;
	
	@NotNull
	@Column(name = "email", unique = true, nullable = false)
	String email;
	
	@NotNull
	@Column(name = "password", nullable = false)
	String password;
}