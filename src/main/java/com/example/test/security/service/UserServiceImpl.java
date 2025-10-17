package com.example.test.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.test.security.dto.AuthenticatedUserDto;
import com.example.test.security.mapper.UserMapper;
import com.example.test.utils.Role;
import com.example.test.utils.User;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

	// private static final String REGISTRATION_SUCCESSFUL = "registration_successful";


	// private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private List<User> userList;

    public UserServiceImpl() {
        this.userList = new ArrayList<>();

        userList.add(new User(1, "admin","admin","admin@email.com",Role.ADMIN));
        userList.add(new User(2,"user","user", "user@email.com",Role.USER));
    }

	@Override
	public User findByUsername(String username) {
		return userList.stream().filter(user->user.getUsername().equalsIgnoreCase(username))
		.findFirst().orElse(null);
	}

	// @Override
	// public RegistrationResponse registration(RegistrationRequest registrationRequest) {

	// 	userValidationService.validateUser(registrationRequest);

	// 	final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
	// 	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	// 	user.setUserRole(UserRole.USER);

	// 	userRepository.save(user);

	// 	final String username = registrationRequest.getUsername();
	// 	final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

	// 	log.info("{} registered successfully!", username);

	// 	return new RegistrationResponse(registrationSuccessMessage);
	// }

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

		final User user = findByUsername(username);

		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}
}