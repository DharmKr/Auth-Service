package com.pct.auth.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.pct.auth.dto.MethodType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "permission")
public class PermissionEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="permission_id", unique=true, nullable=false, length=10)
    private Integer permissionId;
	private String name;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private MethodType methodType;
	
	private String path;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "permissions")
	@EqualsAndHashCode.Exclude @ToString.Exclude private List<RoleEntity> roles;
}
