package com.pct.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pct.auth.dto.PermissionDto;
import com.pct.auth.entity.PermissionEntity;
import com.pct.auth.entity.User;
import com.pct.auth.repository.UserRepository;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService{

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userDao;
	
	public static final int MAX_FAILED_ATTEMPT =3;
	
	public static final long LOCK_TIME_DURATION = 30 * 60 * 1000;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username");
		}
		if(!user.isAccountNonLocked()) {
			if(unlockUser(user) == true) {
				throw new ResponseStatusException(HttpStatus.OK, "Your accout has been unlocked, please try to login again.");
			}
			throw new ResponseStatusException(HttpStatus.LOCKED, "Your accout has been locked due to 3 failed attempts."
					+ " It will be unlocked after 24 hours.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new HashSet<>());
	}

	public List<PermissionDto> getAllPermissionsByUsername(String username) {
		User user = userDao.findByUsername(username);
		List<PermissionDto> permissionDtoList = new ArrayList<PermissionDto>();
		for(PermissionEntity permission : user.getRole().getPermissions()) {
			PermissionDto permissionDto = modelMapper.map(permission, PermissionDto.class);
			permissionDtoList.add(permissionDto);
		}
		return permissionDtoList;
	}
	
	public void updateUserAccountStatus(String username) {
		User user = userDao.findByUsername(username);
		if(user != null) {
			if(user.isAccountNonLocked()) {
				if(user.getFailedAttempt() < MAX_FAILED_ATTEMPT - 1) {
					increaseFailedAttempt(user);
				}else {
					lockUser(user);
				}
			}
		}
	}
	
	private void increaseFailedAttempt(User user) {
		int newFailedAttempt = user.getFailedAttempt() + 1;
		user.setFailedAttempt(newFailedAttempt);
		userDao.save(user);
	}
	
	private void lockUser(User user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userDao.save(user);
	}
	
	private boolean unlockUser(User user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeInMillis = System.currentTimeMillis();
		
		if((lockTimeInMillis + LOCK_TIME_DURATION) < currentTimeInMillis) {
			System.out.println("inside unlock method");
			user.setAccountNonLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userDao.save(user);
			
			return true;
		}
		return false;
	}
}
