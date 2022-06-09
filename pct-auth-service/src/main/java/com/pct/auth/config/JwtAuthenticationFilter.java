package com.pct.auth.config;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pct.auth.dto.PermissionDto;
import com.pct.auth.dto.Status;
import com.pct.auth.response.GenericResponse;
import com.pct.auth.service.impl.UserServiceImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider jwtTokenUtil;

	@Resource(name = "userService")
	private UserServiceImpl userService;

	@Autowired
	private ObjectMapper objectMapper;
	private List<String> excludedUrls;
	
	private String tokenHeader="Authorization";

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String username = null;

		excludedUrls = Arrays.asList("springfox-swagger-ui", "swagger-resources", "swagger-ui.html", "/v2/api-docs", "/api/auth", "/api/ping");
		
		if (excludedUrls.stream().noneMatch(url-> req.getRequestURL().toString().toLowerCase().contains(url.toLowerCase())) && !req.getMethod().equals("OPTIONS")) {
			String authToken = req.getHeader(this.tokenHeader);
			
			if(null == authToken) {
				logger.error("Token can not be left blank.");
				prepareErrorResponse(res, "Token can not be left blank.");
				return;
			}
			
			if(authToken.startsWith("Bearer ")) {
				authToken = authToken.substring(7);
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			}

			if(null == username) {
				logger.info("Token is not valid.");
				prepareErrorResponse(res, "Token is not valid.");
				return;
			}
			
			try {
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = userService.loadUserByUsername(username);

					if (jwtTokenUtil.validateToken(authToken, userDetails)) {
						//    UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
						logger.info("authenticated user " + username + ", setting security context");
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
				if(username != null) {
					List<PermissionDto> permissionList = userService.getAllPermissionsByUsername(username);			
					if (permissionList.stream().filter(permission -> req.getRequestURI().contains(permission.getPath()) 
							&& permission.getMethodType().name().equalsIgnoreCase(req.getMethod())).findAny().isEmpty()) {
						prepareErrorResponse(res, "you don't have permission to access the requested resource");
						return;
					}
				}
			} catch (Exception e) {
				prepareErrorResponse(res, "Invalid user");
				return;
			}
		}

		//TODO validate user-permission to requested API

		chain.doFilter(req, res);
	}

	private void prepareErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		GenericResponse body = new GenericResponse();
		body.setStatus(Status.FAILURE);
		body.setError(errorMessage);
		response.getOutputStream().println(objectMapper.writeValueAsString(body));
	}
}