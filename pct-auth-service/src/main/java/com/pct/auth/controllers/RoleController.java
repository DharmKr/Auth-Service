package com.pct.auth.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pct.auth.dto.RoleDto;
import com.pct.auth.dto.Status;
import com.pct.auth.response.BaseResponse;
import com.pct.auth.service.IRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@Api(tags="Role controller provider" , description="This is a role contoller to provide all the role related APIs")
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RoleController {

	private IRoleService roleService;
	
	@ApiOperation(value="It is to add new role")
	@PostMapping("/addRole")
	public ResponseEntity<BaseResponse<Boolean, Integer>> addRole(@Valid @RequestBody RoleDto roleDto) {
		roleService.addRole(roleDto);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Role is created successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	
	@ApiOperation(value = "It is to update existing role")
	@PutMapping("/updateRole/{roleId}")
	public ResponseEntity<BaseResponse<Boolean, Integer>> updateRole(@PathVariable(name = "roleId") Integer roleId,@Valid @RequestBody RoleDto roleDto) {
		roleService.updateRole(roleId, roleDto);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Role with id: ["+roleId+"] updated successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	
	@ApiOperation(value = "It is to delete existing role")
	@DeleteMapping("/deleteRole/{roleId}")
	public ResponseEntity<BaseResponse<Boolean, Integer>> deleteRole(@PathVariable(name = "roleId") Integer roleId) {
		roleService.deleteRole(roleId);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Role with id: ["+roleId+"] deleted successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
