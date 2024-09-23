package com.server.account.Users.Reposit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.Users.UserVo;

public interface UsersRepository extends JpaRepository<UserVo, Long>{
	
	Optional<UserVo> findByusername(String username);
	
}
