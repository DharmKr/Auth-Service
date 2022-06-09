package com.pct.auth.service;

import com.pct.auth.dto.PermissionDto;

public interface IPermissionService {

	public void addPermission(PermissionDto permissionDto);

	public void updatePermission(Integer permissionId, PermissionDto permissionDto);

	public void deletePermission(Integer permissionId);
}
