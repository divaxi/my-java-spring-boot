package com.example.test.security.dto;

import com.example.test.utils.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private long id;

	private String username;

	private String password;

    private String email;

	private Role role;



}