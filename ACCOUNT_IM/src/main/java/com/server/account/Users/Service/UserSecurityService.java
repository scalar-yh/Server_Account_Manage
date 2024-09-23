package com.server.account.Users.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.account.Users.UserVo;
import com.server.account.Users.UsersRole;
import com.server.account.Users.Reposit.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private final UsersRepository userRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserVo> _siteUser = this.userRepository.findByusername(username);
        if (_siteUser.isEmpty()) {
//        	log.info(">>>>> 로그인 Failed Log <<<<<");
//        	log.info(">> 로그인 실패 시도 ID : {}", username);
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        UserVo Uservo = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UsersRole.ADMIN.getValue()));
        return new User(Uservo.getUsername(), Uservo.getPassword(), authorities);
    }

}
