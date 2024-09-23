package com.server.account.Users;

import lombok.Getter;

@Getter
public enum UsersRole {
	
	ADMIN("ROLE_ADMIN");

	UsersRole(String value) {
        this.value = value;
    }

    private String value;

}
