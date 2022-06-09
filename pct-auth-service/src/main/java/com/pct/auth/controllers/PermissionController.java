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

import com.pct.auth.dto.PermissionDto;
import com.pct.auth.dto.Status;
import com.pct.auth.response.BaseResponse;
import com.pct.auth.service.IPermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@Api(tags="Permission controller provider" , description="This is a permission contoller to provide all the permission related APIs")
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class PermissionController {
	
	private IPermissionService permissionService;
	
	@ApiOperation(value="It is to add new permission")
	@PostMapping("/addPermission")
	public ResponseEntity<BaseResponse<Boolean, Integer>> addPermission(@Valid @RequestBody PermissionDto permissionDto) {
		permissionService.addPermission(permissionDto);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Permission is created successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	
	@ApiOperation(value = "It is to update existing permission")
	@PutMapping("/updatePermission/{permissionId}")
	public ResponseEntity<BaseResponse<Boolean, Integer>> updatePermission(@PathVariable Integer permissionId, @Valid @RequestBody PermissionDto permissionDto) {
		permissionService.updatePermission(permissionId, permissionDto);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Permission with id: ["+permissionId+"] updated successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	
	@ApiOperation(value = "It is to delete existing permission")
	@DeleteMapping("/deletePermission/{permissionId}")
	public ResponseEntity<BaseResponse<Boolean, Integer>> deletePermission(@PathVariable Integer permissionId) {
		permissionService.deletePermission(permissionId);
		BaseResponse<Boolean, Integer> response = new BaseResponse<>();
		response.setStatus(Status.SUCCESS);
		response.setMessage("Permission with id: ["+permissionId+"] deleted successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
