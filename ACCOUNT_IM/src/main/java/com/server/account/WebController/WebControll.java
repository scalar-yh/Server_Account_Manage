package com.server.account.WebController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.server.account.ServerList.Service.ServerListService_JPA;
import com.server.account.ServerList.Vo.SERVERADD_HIST;
import com.server.account.ServerList.Vo.SERVER_LIST;
import com.server.account.ServerList.Vo.ServerCreateForm;
import com.server.account.Users.SHRSVo;
import com.server.account.Users.UserVo;
import com.server.account.Users.UsersCreateForm;
import com.server.account.Users.UsersLists;
import com.server.account.Users.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class WebControll {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	
	private final UserService userService;
	
	private final ServerListService_JPA ServerListsService;
	
	@GetMapping("/viewpassword")
	public String viewpw(Model model){
		/* Auth User ID Get */
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	List<SHRSVo> shrs = userService.searchshrs(UUID);
    	String Kname = shrs.get(0).getKname();
    	model.addAttribute("UUID", Kname);
		return "viewpassword";
	}
	
	
	@RequestMapping("/viewpassword/{serverip}")
	public String viewpassword(Model model, @PathVariable("serverip") String serverip) {
		/* Auth User ID Get */
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	List<SHRSVo> shrs = userService.searchshrs(UUID);
    	String Kname = shrs.get(0).getKname();
		List<SERVER_LIST> findbyserverip = ServerListsService.findByserverip(serverip);
		model.addAttribute("findbyserverip", findbyserverip);
		model.addAttribute("UUID", Kname);
		return "viewpassword";
	}
	
	// 로그인 페이지
	@GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
    		@RequestParam(value = "exception", required = false) String exception, Model model) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
        return "login";
    }
	
	// 관리자 추가 페이지
	@GetMapping("/signup")
    public String signup(@PageableDefault(page = 1) Pageable pageable, Model model, UsersCreateForm userCreateForm) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	List<SHRSVo> shrs = userService.searchshrs(UUID);
    	String Kname = shrs.get(0).getKname();
		Page<UsersLists> adminpages = userService.paging(pageable);
		int blockLimit = 3;
    	int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
    	int endPage = Math.min((startPage + blockLimit - 1), adminpages.getTotalPages());
    	model.addAttribute("adminpages", adminpages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        /* 현재 로그인한 사용자 정보 전달 */
        model.addAttribute("UUID", Kname);
        return "signup_form";
    }
	
	// 관리자 페이지 에서 실제 관리자 추가하는 부분
    @PostMapping("/signup")
    public String signup(@Valid UsersCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/signup";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword())) {
            bindingResult.rejectValue("password1", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "redirect:/signup";
        }
        userService.create(userCreateForm.getKname(), userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        return "redirect:/main";
    }
    
    // 메인 페이지
    @GetMapping("/main")
    public String main(@PageableDefault(page = 1) Pageable pageable, Model model, HttpServletRequest request) {
    	/* Auth User ID Get */
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	List<SHRSVo> shrs = userService.searchshrs(UUID);
    	String Kname = shrs.get(0).getKname();
    	Page<SERVER_LIST> postsPages = ServerListsService.paging(pageable);
    	int blockLimit = 10;
    	int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
    	int endPage = Math.min((startPage + blockLimit - 1), postsPages.getTotalPages());
    	// Model 에 각 정보 담아서 타임리프 사용
    	model.addAttribute("postsPages", postsPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        /* 현재 로그인한 사용자 정보 전달 */
        model.addAttribute("UUID", Kname);
        return "main";
    }
    
    // 시스템 목록 추가
    @GetMapping("/addsystem")
    public String addsystem(@PageableDefault(page = 1) Pageable pageable, Model model) {
    	/* Auth User ID Get */
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	List<SHRSVo> shrs = userService.searchshrs(UUID);
    	String Kname = shrs.get(0).getKname();
    	
    	Page<SERVERADD_HIST> postsPages = ServerListsService.pagingAddHist(pageable);
    	int blockLimit = 5;
    	int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
    	int endPage = Math.min((startPage + blockLimit - 1), postsPages.getTotalPages());
    	model.addAttribute("postsPages", postsPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        /* 현재 로그인한 사용자 정보 전달 */
    	model.addAttribute("UUID", Kname);
		return "addsystem";
    }
    
    @PostMapping("/addsystem")
    public String addsystem(@Valid ServerCreateForm serverList, BindingResult bindingResult, HttpServletRequest request) {
    	/* Auth User ID Get */
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String UUID = userDetails.getUsername();
    	
    	log.info(">>> Binding : {}", bindingResult.getModel().get("servername"));
    	if (bindingResult.hasErrors()) {
    		log.info(">>> Binding Error");
            return "redirect:/addsystem";
        } 
    	/* 서버 목록 추가 */
    	ServerListsService.create(serverList.getServername(), serverList.getServerip(), serverList.getServermac(), serverList.getServerst(), serverList.getServeros()
    			, serverList.getServermodel(), serverList.getServersn(), serverList.getServeradmin(), serverList.getServerid(), serverList.getServerpw());
		/* 서버 목록 추가에 대한 로그 기록 */
    	ServerListsService.createHistory(serverList.getServername(), serverList.getServerip(), UUID, new Date(), sdfTime.format(new Date()));
    	return "redirect:/addsystem";
    }
	

}
