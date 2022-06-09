package com.pct.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class User extends BaseEntity {
  
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
	
    @Column
    private String username;
    
    @Column
    @JsonIgnore
    private String password;
    
    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
    private String mobileNumber;
    
    @Column
    private String emailId;
    
    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleEntity role;
    
    @Column(name="account_non_locked")
    private boolean accountNonLocked;
    
    @Column(name = "failed_attempt")
    private int failedAttempt;
    
    @Column(name = "lock_time")
    private Date lockTime;
}
