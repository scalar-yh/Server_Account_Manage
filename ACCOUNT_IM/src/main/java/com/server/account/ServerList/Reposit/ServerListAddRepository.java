package com.server.account.ServerList.Reposit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.account.ServerList.Vo.SERVERADD_HIST;

public interface ServerListAddRepository extends JpaRepository<SERVERADD_HIST, Long>{

}
