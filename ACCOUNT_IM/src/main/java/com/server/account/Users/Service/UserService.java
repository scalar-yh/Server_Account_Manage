package com.server.account.Users.Service;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.account.Users.ChangePasswordHist;
import com.server.account.Users.SHRSVo;
import com.server.account.Users.UserVo;
import com.server.account.Users.UsersLists;
import com.server.account.Users.Mapper.MyBatis_USER_Mapper;
import com.server.account.Users.Reposit.UserPasswordHistRepository;
import com.server.account.Users.Reposit.UserPasswordRepository;
import com.server.account.Users.Reposit.UsersListRepository;
import com.server.account.Users.Reposit.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private final UsersRepository users_repository;
	private final UserPasswordRepository passwordrepository;
	private final UserPasswordHistRepository passwordHistrepository;
	private final UsersListRepository UsersLists;
	private final PasswordEncoder passwordEncoder;
	private final MyBatis_USER_Mapper MyBatis_USER;
	
	public UserVo create(String kname, String username, String email, String password) {
		UserVo users = new UserVo();
		users.setKname(kname);
		users.setUsername(username);
		users.setEmail(email);
        users.setPassword(passwordEncoder.encode(password));
        this.users_repository.save(users);
        
        /* LOG FILED */
        log.info(">>>>> User(ADMIN) New Create <<<<<");
        log.info(">> User Name: {}", kname);
        log.info(">> User ID: {}", username);
        return users;
	}
	
	public ChangePasswordHist createpasswordhist(String username, Date changedate, String requestip, String requesturl, String requestuser, Date changetime) {
		ChangePasswordHist passwordhist = new ChangePasswordHist();
		passwordhist.setUsername(username);
		passwordhist.setChangedate(changedate);
		passwordhist.setRequestip(requestip);
		passwordhist.setRequesturl(requesturl);
		passwordhist.setRequestuser(requestuser);
		passwordhist.setChangetime(changetime);
		this.passwordHistrepository.save(passwordhist);
		
		/* LOG FILED */
        log.info(">>>>> User(ADMIN) ChangePassword History Create <<<<<");
        log.info(">> User ID: {}", username);
        log.info(">> User ChangeDate: {}", changedate);
		return passwordhist;
	}
	
	public Page<UsersLists> paging(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageLimit = 3;
		Page<UsersLists> adminpages = UsersLists.findAll(PageRequest.of(page, pageLimit));
		//log.info(">>>>> ADMIN LIST SELECT <<<<<");
		//log.info(">>> ADMIN LISTS: {}", adminpages.getContent().toString());
		return adminpages;
	}
	
	public void updatePassword(String username, String newpassword) throws Exception{
		try {
			UserVo users = vaildUserandPassword(username);
			users.updatePassword(passwordEncoder.encode(newpassword));
			users_repository.save(users);
		} catch (Exception e) {
			log.info(">> UPDATE PASSWORD FAILED : {}", e);
		}
		
	}
	
	public UserVo vaildUserandPassword(String username) {
		UserVo users = passwordrepository.findByusername(username);
		//log.info(">> Request User : {}", username);
		//log.info(">> Request UserKname : {}", users.getKname());
		if (users.equals(null)) {
			log.info(">>>>> Vaild USER Check API <<<<<");
			log.info(">> Change Result : {}", "FAILED");
			throw new InvalidParameterException();
		}
		return users;
	}
	
	public List<SHRSVo> searchshrs(String sno) {
		return MyBatis_USER.searchshrs(sno);
	}
	
	public List<HashMap<String, String>> searchshrsAPI(String sno) {
		return MyBatis_USER.searchshrsAPI(sno);
	}

}
