package com.server.account.ServerList.Vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateServerPasswordReq {
	
	@NotBlank
	private Long sid;
	
	@NotBlank
	private String asispassword;
	
	@NotBlank
	private String newpassword;

}
