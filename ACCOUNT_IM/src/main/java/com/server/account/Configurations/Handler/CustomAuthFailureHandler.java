package com.server.account.Configurations.Handler;

import java.io.IOException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.server.account.Users.Service.MyBatis_USER_Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private MyBatis_USER_Service userhistserives;
	
	public CustomAuthFailureHandler(MyBatis_USER_Service userhistserives) {
		this.userhistserives = userhistserives;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String errorMessage;
		String Cookie = request.getCookies().toString();
		String Seison = request.getSession().getId();
		int wrongCount = 0;

		if(exception instanceof BadCredentialsException) {
			wrongCount += 1;
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info(">> 로그인 실패 시도 ID : {}",request.getParameter("username"));
			log.info(">> Session Info : {}", Seison);
			errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			wrongCount += 1;
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info(">> 로그인 실패 시도 ID : {}",request.getParameter("username"));
			errorMessage = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
			log.info(">> Session Info : {}", Seison);
		} else if (exception instanceof UsernameNotFoundException) {
			wrongCount += 1;
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info(">> 로그인 실패 시도 ID : {}",request.getParameter("username"));
			log.info(">> Session Info : {}", Seison);
			errorMessage = "존재하지 않는 계정입니다. 관리자에게 계정 생성을 요청하세요.";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			wrongCount += 1;
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info(">> 로그인 실패 시도 ID : {}",request.getParameter("username"));
			log.info(">> Session Info : {}", Seison);
			errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
		} else {
			wrongCount += 1;
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info("{}", exception.getMessage());
			log.info(">> 로그인 실패 시도 ID : {}",request.getParameter("username"));
			log.info(">> Session Info : {}", Seison);
			errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
		}
		if (wrongCount > 5) {
			log.info(">>>>> 로그인 Failed Log <<<<<");
			log.info(">> 로그인 실패 5회 초과로 세션을 삭제 합니다.");
			log.info(">> Session Info : {}", Seison);
		}
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8"); /* 한글 인코딩 깨진 문제 방지 */
		setDefaultFailureUrl("/login?error=true&exception="+errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}

}
