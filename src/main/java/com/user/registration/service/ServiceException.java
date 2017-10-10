package com.user.registration.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(Exception exception) {
		super(exception);
	}
}
