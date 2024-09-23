package com.server.account.Users.Reposit;


import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.Users.UsersLists;

public interface UsersListRepository extends JpaRepository<UsersLists, Long>{
	
}
