package com.example.test.security.jwt;

import java.util.Date;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test.utils.Role;
import com.example.test.utils.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class JwtTokenManager {


	protected final JwtProperties jwtProperties;

	public abstract String generateToken(User user);

	protected abstract DecodedJWT getDecodedJWT(String token);

	public String getUsernameFromToken(String token) {
		final DecodedJWT decodedJWT = getDecodedJWT(token);
		return decodedJWT.getSubject();
	}

	public boolean validateToken(String token, String authenticatedUsername) {
		final String usernameFromToken = getUsernameFromToken(token);
		final boolean equalsUsername = usernameFromToken.equals(authenticatedUsername);
		final boolean tokenExpired = isTokenExpired(token);
		return equalsUsername && !tokenExpired;
	}

	private boolean isTokenExpired(String token) {
		final Date expirationDateFromToken = getExpirationDateFromToken(token);
		return expirationDateFromToken.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		final DecodedJWT decodedJWT = getDecodedJWT(token);
		return decodedJWT.getExpiresAt();
	}
}


@Component("accessTokenManager")
@Primary
class AccessTokenManager extends JwtTokenManager {

	public AccessTokenManager(JwtProperties jwtProperties) {
		super(jwtProperties);
	}

	@Override
	public String generateToken(User user) {
		final String username = user.getUsername();
		final Role userRole = user.getRole();

		return JWT.create()
				.withSubject(username)
				.withIssuer(jwtProperties.getIssuer())
				.withClaim("role", userRole.name())
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMinute() * 60 * 1000))
				.sign(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()));
	}

	@Override
	protected DecodedJWT getDecodedJWT(String token) {
		final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes())).build();
		return jwtVerifier.verify(token);
	}
}


@Component("refreshTokenManager")
class RefreshTokenManager extends JwtTokenManager {

	public RefreshTokenManager(JwtProperties jwtProperties) {
		super(jwtProperties);
	}

	@Override
	public String generateToken(User user) {
		final String username = user.getUsername();

		return JWT.create()
				.withSubject(username)
				.withIssuer(jwtProperties.getIssuer())
				.withClaim("type", "refresh")
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMinute() * 60 * 1000))
				.sign(Algorithm.HMAC256(jwtProperties.getRefreshSecretKey().getBytes()));
	}

	@Override
	protected DecodedJWT getDecodedJWT(String token) {
		final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperties.getRefreshSecretKey().getBytes())).build();
		return jwtVerifier.verify(token);
	}
}