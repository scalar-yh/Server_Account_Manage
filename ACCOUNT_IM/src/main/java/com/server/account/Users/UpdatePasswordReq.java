package com.server.account.Users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordReq {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String newpassword;

}
