package com.server.account.Users.Service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.server.account.Users.Mapper.MyBatis_USER_Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBatis_USER_Service {
	
	private final MyBatis_USER_Mapper usersMapper;
	
	public Integer SQ_Maxvalue() {
		return usersMapper.SQ_Maxvalue();
	}
	
	
	public int In_LoginHist(HashMap<String, String> LOGIN) {
		return usersMapper.In_LoginHist(LOGIN);
	}

}
