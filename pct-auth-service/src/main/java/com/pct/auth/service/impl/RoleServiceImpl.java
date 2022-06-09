package com.pct.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pct.auth.dto.RoleDto;
import com.pct.auth.entity.PermissionEntity;
import com.pct.auth.entity.RoleEntity;
import com.pct.auth.repository.PermissionRepository;
import com.pct.auth.repository.RoleRepository;
import com.pct.auth.service.IRoleService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements IRoleService {

	private RoleRepository roleRepository;

	private PermissionRepository permissionRepository;

	@Override
	public void addRole(RoleDto roleDto) {
		RoleEntity entity = roleRepository.findByName(roleDto.getName());
		if(entity != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is already exist");

		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setName(roleDto.getName());
		roleEntity.setDescription(roleDto.getDescription());

		List<PermissionEntity> permissionEntityList = new ArrayList<PermissionEntity>();
		for(Integer permissionId : roleDto.getPermissionIdList()) {
			PermissionEntity permissionEntity = permissionRepository.findByPermissionId(permissionId);
			if(permissionEntity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid list of Permission Ids");
			permissionEntityList.add(permissionEntity);
		}

		roleEntity.setPermissions(permissionEntityList);
		roleRepository.save(roleEntity);
	}

	@Override
	public void updateRole(Integer roleId, RoleDto roleDto) {
		RoleEntity roleEntity = roleRepository.findByRoleId(roleId);
		if(roleEntity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Role Id");

		if (!roleDto.getName().equalsIgnoreCase(roleEntity.getName())) {
			RoleEntity entity = roleRepository.findByName(roleDto.getName());
			if(entity != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is already exist");
		}
		List<PermissionEntity> permissionEntityList = new ArrayList<PermissionEntity>();
		for(Integer permissionId : roleDto.getPermissionIdList()) {
			PermissionEntity permissionEntity = permissionRepository.findByPermissionId(permissionId);
			if(permissionEntity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid list of Permission Ids");
			permissionEntityList.add(permissionEntity);
		}
		roleEntity.setName(roleDto.getName());
		roleEntity.setDescription(roleDto.getDescription());
		roleEntity.setPermissions(permissionEntityList);
		roleRepository.save(roleEntity);
	}

	@Override
	public void deleteRole(Integer roleId) {
		RoleEntity roleEntity = roleRepository.findByRoleId(roleId);
		if(roleEntity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Role Id");

		roleRepository.delete(roleEntity);
	}

}
