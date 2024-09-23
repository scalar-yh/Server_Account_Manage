package com.server.account.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserVo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String kname;
	
	@Column(unique = true)
    private String username;
	
	private String password;
	
	@Column(unique = true)
    private String email;
	
	public void updatePassword(String newpassword) {
		this.password = newpassword;
	}

}
