package com.server.account.Users.Reposit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.Users.ChangePasswordHist;

public interface UserPasswordHistRepository extends JpaRepository<ChangePasswordHist, Long> {
	
	

}
