package com.server.account.MsSql.MapperPack;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MSSQL_Mapper {
	
	/**
	 * MMS 발송용
	 * @param MmsMap
	 * @return integer
	 */
	public int MMS_SEND(HashMap<String, String> MmsMap);

}
