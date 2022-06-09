package com.pct.auth.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pct.auth.dto.PermissionDto;
import com.pct.auth.entity.PermissionEntity;
import com.pct.auth.repository.PermissionRepository;
import com.pct.auth.service.IPermissionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PermissionServiceImpl implements IPermissionService {

	private PermissionRepository permissionRepository;

	@Override
	public void addPermission(PermissionDto dto) {
		PermissionEntity permissionEntity = permissionRepository.findByName(dto.getName());
		if(permissionEntity != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permission is already exist");

		PermissionEntity entity = new PermissionEntity();
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setMethodType(dto.getMethodType());
		entity.setPath(dto.getPath());
		permissionRepository.save(entity);
	}

	@Override
	public void updatePermission(Integer permissionId, PermissionDto dto) {
		PermissionEntity entity = permissionRepository.findByPermissionId(permissionId);
		if(entity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Permission Id");

		if(!dto.getName().equalsIgnoreCase(entity.getName())) {
			PermissionEntity permissionEntity = permissionRepository.findByName(dto.getName());
			if(permissionEntity != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permission is already exist");	
		}
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setMethodType(dto.getMethodType());
		entity.setPath(dto.getPath());
		permissionRepository.save(entity);
	}

	@Override
	public void deletePermission(Integer permissionId) {
		PermissionEntity entity = permissionRepository.findByPermissionId(permissionId);
		if(entity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Permission Id");

		permissionRepository.delete(entity);
	}
}
