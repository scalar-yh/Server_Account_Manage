package com.server.account.ServerList.Reposit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.ServerList.Vo.SERVER_LIST;

public interface ServerListRepository extends JpaRepository<SERVER_LIST, Long>{

	List<SERVER_LIST> findByserverip(String serverip);
	
	SERVER_LIST findBysid(Long sid);

}
