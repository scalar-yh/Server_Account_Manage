package com.server.account.MsSql.ServicePack;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.server.account.MsSql.MapperPack.MSSQL_Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MSSQL_Service {
	
	private final MSSQL_Mapper MSSQLmapper;
	
	/**
	 * MMS 발송용
	 * @param MmsMap
	 * @return integer
	 */
	public int MMS_SEND(HashMap<String, String> MmsMap) {
		return MSSQLmapper.MMS_SEND(MmsMap);
	}

}
