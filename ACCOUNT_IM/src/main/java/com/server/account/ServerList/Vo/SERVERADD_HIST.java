package com.server.account.ServerList.Vo;

import java.util.Date;

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
public class SERVERADD_HIST {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long mkseq;
	
	@Column
	public String servername;
	
	@Column
	public String serverip;
	
	@Column
	public String adduser;
	
	@Column
	public Date adddate;
	
	@Column
	public String addtime;

}
