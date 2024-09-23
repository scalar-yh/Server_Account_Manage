package com.server.account.WebController;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.account.MsSql.ServicePack.MSSQL_Service;
import com.server.account.ServerList.Service.ServerListService_JPA;
import com.server.account.ServerList.Vo.SERVER_LIST;
import com.server.account.ServerList.Vo.UpdateServerPasswordReq;
import com.server.account.Users.UpdatePasswordReq;
import com.server.account.Users.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RestAPIControll {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private final UserService userService;
	
	private final MSSQL_Service MssqlService;
	
	private final ServerListService_JPA ServerListsService;
	
	@GetMapping("/searchshrs")
	public List<HashMap<String, String>> shrslist(@RequestParam("sno") String sno
			, HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod) {
		List<HashMap<String, String>> shrslist = new ArrayList<HashMap<String, String>>();
    	shrslist = userService.searchshrsAPI(sno);
    	log.info(">>>>> SHRS DATA SEARCH & RETURN API <<<<<");
		log.info(">> Response USER SNO : {}", shrslist.get(0).get("SNO"));
		log.info(">> Response USER KNAME : {}", shrslist.get(0).get("KNAME"));
		log.info(">> Response USER DEPT : {}", shrslist.get(0).get("DEPTNAME"));
		log.info(">> Request URL : {}",request.getRequestURL());
		log.info(">> Request Host : {}",request.getRemoteAddr());
		log.info(">> Method Type : {}",httpMethod);
		log.info(">> Status Code : {}", response.getStatus());
    	return shrslist;
    }
	
	// 인증번호 API
	@GetMapping("/numbercheck")
	public Integer CheckNumber(@RequestParam("phone") String phone, HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod) {
		// 4자리 인증번호 랜덤 생성 후 임시저장
		Integer Numb = (int)(Math.random() * 8999) + 1000;
		// MMS 발송을 위한 HashMap 선언 및 초기화
		HashMap<String, String> MmsMap = new HashMap<String, String>();
		MmsMap.put("TEL_NUM", phone);
		MmsMap.put("NOW_DATE", sdfDate.format(new Date()));
		MmsMap.put("SEND_DATE", sdfDate.format(new Date()));
		MmsMap.put("MSG", "[조선호텔 계정관리시스템] 인증번호는 "+Integer.toString(Numb)+" 입니다.");
		// MMS 발송
		MssqlService.MMS_SEND(MmsMap);
		// MMS 발송에 대한 로그화
		log.info(">>>>> Auth Number SMS SEND & CHECK API <<<<<");
		log.info(">> Auth Number : {}", Numb);
		log.info(">> Phone Number : {}", phone);
		log.info(">> Request URL : {}",request.getRequestURL());
		log.info(">> Request Host : {}",request.getRemoteAddr());
		log.info(">> Method Type : {}",httpMethod);
		log.info(">> Status Code : {}", response.getStatus());
		return Numb;
	}
	
	@PatchMapping("/chpasswd")
	public void updatePassword(UpdatePasswordReq updatepw, HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod) throws Exception {
		InetAddress address = InetAddress.getLocalHost();
		//log.info(">> {}", updatepw.getUsername());
		//log.info(">> {}", updatepw.getNewpassword());
		userService.updatePassword(updatepw.getUsername(), updatepw.getNewpassword());
		// PW 변경 로그기록은 사용자이름(아이디) / 변경일자 / 접속IP / 요청 URL / 접속한 PC이름(AD명) / 변경시간 기록된다.
		userService.createpasswordhist(updatepw.getUsername(), new Date(), request.getRemoteAddr()
				, String.valueOf(request.getRequestURL()), address.getHostName(), new Date());
	}
	
	@PatchMapping("/chserverpasswd")
	public void pingtest(UpdateServerPasswordReq serverinfo, HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod) throws Exception {
		InetAddress address = InetAddress.getLocalHost();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
		ServerListsService.UpdatePassword(serverinfo.getSid(), serverinfo.getAsispassword(), serverinfo.getNewpassword());
		// PW 변경 로그기록은 사용자이름(아이디) / 변경일자 / 접속IP / 요청 URL / 접속한 PC이름(AD명) / 변경시간 기록된다.
		userService.createpasswordhist(userDetails.getUsername(), new Date(), request.getRemoteAddr()
		, String.valueOf(request.getRequestURL()), address.getHostName(), new Date());
	}

}
