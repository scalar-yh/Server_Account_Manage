package com.server.account.ServerList.Vo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
public class SERVER_LIST {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long sid;
	@Column
	public String servername;
	@Column
	public String serverip;
	@Column
	public String servermac;
	@Column
	public String serverst;
	@Column
	public String serveros;
	@Column
	public String servermodel;
	@Column
	public String serversn;
	@Column
	public String serveradmin;
	@Column
	public String serverid;
	@Column
	public String serverpw;
	
	public void updatePassword(String password) {
		this.serverpw = password;
	}

}
