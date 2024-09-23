package com.server.account.ServerList.Vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerCreateForm {
	
	@NotEmpty(message = "서버명은 필수항목입니다.")
	public String servername;
	@NotEmpty(message = "서버 IP는 필수항목입니다.")
	public String serverip;
	
	public String servermac;
	@NotEmpty(message = "서버 종류는 필수항목입니다.")
	public String serverst;
	@NotEmpty(message = "서버 OS는 필수항목입니다.")
	public String serveros;
	
	public String servermodel;
	
	public String serversn;
	
	@NotEmpty(message = "서버 메인 관리자는 필수항목입니다.")
	public String serveradmin;
	@NotEmpty(message = "서버 계정ID는 필수항목입니다.")
	public String serverid;
	@NotEmpty(message = "서버 계정PW는 필수항목입니다.")
	public String serverpw;

}
