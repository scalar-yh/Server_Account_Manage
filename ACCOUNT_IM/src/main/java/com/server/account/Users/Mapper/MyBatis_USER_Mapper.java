package com.server.account.Users.Mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.server.account.Users.SHRSVo;

@Mapper
public interface MyBatis_USER_Mapper {
	
	public Integer SQ_Maxvalue();

	public int In_LoginHist(HashMap<String, String> LOGIN);
	
	public List<SHRSVo> searchshrs(String sno);
	public List<HashMap<String, String>> searchshrsAPI(String sno);
	
}
