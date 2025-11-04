package com.example.test.security.jwt;

import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.test.security.dto.AuthenticatedUserDto;
import com.example.test.security.dto.LoginRequest;
import com.example.test.security.dto.LoginResponse;
import com.example.test.security.dto.RefreshResponse;
import com.example.test.security.mapper.UserMapper;
import com.example.test.security.service.UserService;
import com.example.test.utils.SecurityConstants;
import com.example.test.utils.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final UserService userService;

	@Autowired
	@Qualifier("accessTokenManager")
	private  JwtTokenManager jwtTokenManager;

	@Autowired
	@Qualifier("refreshTokenManager")
	private  JwtTokenManager jwtRefreshTokenManager;

	private final AuthenticationManager authenticationManager;


	public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		final String username = loginRequest.getUsername();
		final String password = loginRequest.getPassword();

		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

		authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);

		final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
		final String token = jwtTokenManager.generateToken(user);
		final String refreshToken = jwtRefreshTokenManager.generateToken(user);

		log.info("{} has successfully logged in!", user.getUsername());

		return new LoginResponse(token,refreshToken, user.getUsername(),user.getEmail());
	}

	public RefreshResponse getRefreshResponse (HttpServletRequest request) {
		final String header = request.getHeader(SecurityConstants.HEADER_STRING);

		String username = null;
		String authToken = null;

		if (Objects.nonNull(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {

			authToken = header.replace(SecurityConstants.TOKEN_PREFIX, Strings.EMPTY);

			try {
				username = jwtRefreshTokenManager.getUsernameFromToken(authToken);
			}
			catch (Exception e) {
				log.error("Authentication Exception : {}", e.getMessage());
				return null;
			}
		}

		final boolean canBeStartTokenValidation = Objects.nonNull(username);

		if (!canBeStartTokenValidation) {
			return null;
		}

		final boolean validToken = jwtRefreshTokenManager.validateToken(authToken, username);

		if (!validToken) {
			return null;
		}
		final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);

		final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
		final String token= jwtTokenManager.generateToken(user);
		final String refreshToken = jwtRefreshTokenManager.generateToken(user);

		return new RefreshResponse(token, refreshToken);
	}
}