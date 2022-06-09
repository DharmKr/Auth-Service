package com.pct.auth.service;

import com.pct.auth.dto.RoleDto;

public interface IRoleService {

	public void addRole(RoleDto roleDto);

	public void updateRole(Integer roleId, RoleDto roleDto);

	public void deleteRole(Integer roleId);
}
