package com.pct.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pct.auth.config.TokenProvider;
import com.pct.auth.dto.AuthenticationRequest;
import com.pct.auth.dto.Status;
import com.pct.auth.response.GenericResponse;
import com.pct.auth.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Authentication Controller Provider")
@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider jwtTokenUtil;
	
	@Autowired
	private UserServiceImpl userService;

	@ApiOperation(value = "It is to generate new authorization token")
	@PostMapping
	public ResponseEntity<GenericResponse> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUsername(),
							authenticationRequest.getPassword()
							)
					);

			final String token = jwtTokenUtil.generateToken(authentication);
			GenericResponse response = new GenericResponse();
			response.setMessage("New authorization token has been generated.");
			response.setStatus(Status.SUCCESS);
			response.setToken(token);
			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {
			userService.updateUserAccountStatus(authenticationRequest.getUsername());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is invalid");
		} catch (DisabledException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user is blocked");
		}

	}

}
