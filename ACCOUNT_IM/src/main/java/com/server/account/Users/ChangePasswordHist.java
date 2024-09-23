package com.server.account.Users;

import java.util.Date;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="CHANGE_PASSWORD_HIST")
public class ChangePasswordHist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private Date changedate;
	
	private String requestip;
	
	private String requesturl;
	
	private String requestuser;
	
	private Date changetime;

}
