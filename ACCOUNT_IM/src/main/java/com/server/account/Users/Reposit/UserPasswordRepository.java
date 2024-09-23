package com.server.account.Users.Reposit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.Users.UserVo;

public interface UserPasswordRepository extends JpaRepository<UserVo, Long>{
	
	UserVo findByusername(String username);

}
