package com.pct.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.pct.auth.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long>{
	
	RoleEntity findByRoleId(Integer roleId);

	RoleEntity findByName(String name);
}
