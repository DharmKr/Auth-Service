package com.pct.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pct.auth.entity.User;;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
    User findByUsername(String username);
}
