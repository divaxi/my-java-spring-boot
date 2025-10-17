package com.example.test.security.service;

import com.example.test.security.dto.AuthenticatedUserDto;

import com.example.test.utils.User;


public interface UserService {

	User findByUsername(String username);
	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

}