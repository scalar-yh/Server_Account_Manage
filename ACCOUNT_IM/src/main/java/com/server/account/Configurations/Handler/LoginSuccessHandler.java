package com.server.account.Configurations.Handler;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.server.account.Users.Service.MyBatis_USER_Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    private DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Autowired
    private MyBatis_USER_Service userhistserives;
    
    public LoginSuccessHandler(MyBatis_USER_Service userhistserives) {
    	this.userhistserives = userhistserives;
    }
    
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession(); 
		setDefaultTargetUrl("/main");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        Integer sq = userhistserives.SQ_Maxvalue() + 1;
        if(savedRequest != null) {
        	response.addHeader("User", request.getParameter("username"));
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);     
            // 로그인 기록 남기기
            HashMap<String, String> LOGIN = new HashMap<String, String>();
            LOGIN.put("SQ", String.valueOf(sq));
            LOGIN.put("USERNAME", request.getParameter("username"));
            LOGIN.put("LOGDATE", sdfDate.format(new Date()));
            LOGIN.put("LOGINTYPE", authentication.getAuthorities().toString());
            LOGIN.put("LOGINIP", request.getRemoteAddr());
            userhistserives.In_LoginHist(LOGIN);
            log.info(">>>>> 로그인 성공 Log <<<<<");
            log.info("로그인 ID : {}", request.getParameter("username"));
        	log.info("로그인 부여 권한 : {}", authentication.getAuthorities().toString());
        }else{
        	response.addHeader("User", request.getParameter("username"));
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
            // 로그인 기록 남기기
            HashMap<String, String> LOGIN = new HashMap<String, String>();
            LOGIN.put("SQ", String.valueOf(sq));
            LOGIN.put("USERNAME", request.getParameter("username"));
            LOGIN.put("LOGDATE", sdfDate.format(new Date()));
            LOGIN.put("LOGINTYPE", authentication.getAuthorities().toString());
            LOGIN.put("LOGINIP", request.getRemoteAddr());
            userhistserives.In_LoginHist(LOGIN);
            log.info(">>>>> 로그인 성공 Log <<<<<");
            log.info("로그인 ID : {}", request.getParameter("username"));
        	log.info("로그인 부여 권한 : {}", authentication.getAuthorities().toString());
        }
	}

}
