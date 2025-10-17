package com.example.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.security.dto.RefreshResponse;
import com.example.test.security.jwt.JwtTokenService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class TokenController {

	private final JwtTokenService jwtTokenService;

	@PostMapping("/refresh")
	@Operation(tags = "Refresh Service", description = "To refresh token")
	public ResponseEntity<RefreshResponse> loginRequest(HttpServletRequest request) {

		final RefreshResponse refreshResponse = jwtTokenService.getRefreshResponse(request);

		return ResponseEntity.ok(refreshResponse);
	}
}
