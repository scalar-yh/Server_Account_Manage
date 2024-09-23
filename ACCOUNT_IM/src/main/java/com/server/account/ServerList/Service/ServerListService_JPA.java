package com.server.account.ServerList.Service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.server.account.Configurations.AESCrypto;
import com.server.account.ServerList.Reposit.ServerListAddRepository;
import com.server.account.ServerList.Reposit.ServerListRepository;
import com.server.account.ServerList.Vo.SERVERADD_HIST;
import com.server.account.ServerList.Vo.SERVER_LIST;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerListService_JPA {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private final ServerListRepository serverRepository;
	private final ServerListAddRepository serverAddHistRepository;
	
	public SERVER_LIST create(String servername, String serverip, String servermac, String serverst
			, String serveros, String servermodel, String serversn, String serveradmin, String serverid, String serverpw) {
		SERVER_LIST serverlists = new SERVER_LIST();
		AESCrypto Crypt = new AESCrypto();
		serverlists.setServername(servername);
		serverlists.setServerip(serverip);
		serverlists.setServermac(servermac);
		serverlists.setServerst(serverst);
		serverlists.setServeros(serveros);
		serverlists.setServermodel(servermodel);
		serverlists.setServersn(serversn);
		serverlists.setServeradmin(serveradmin);
		serverlists.setServerid(serverid);
		serverlists.setServerpw(Crypt.aesEncode(serverpw));
		this.serverRepository.save(serverlists);
		log.info(">>>>> 시스템 목록 추가 성공 <<<<<");
		log.info(">> 서버 이름: {}", serverlists.getServername());
		log.info(">> 서버 IP: {}", serverlists.getServerip());
		return serverlists;
	}
	
	public SERVERADD_HIST createHistory(String servername, String serverip, String adduser, Date adddate, String addtime) {
		SERVERADD_HIST serveraddhis = new SERVERADD_HIST();
		serveraddhis.setServername(servername);
		serveraddhis.setServerip(serverip);
		serveraddhis.setAdduser(adduser);
		serveraddhis.setAdddate(adddate);
		serveraddhis.setAddtime(addtime);
		this.serverAddHistRepository.save(serveraddhis);
		log.info(">>>>> 시스템 추가 로그화 성공 <<<<<");
		return serveraddhis;
	}
	
	public Page<SERVER_LIST> paging(Pageable pageable) {
		AESCrypto Crypt = new AESCrypto();
		int page = pageable.getPageNumber() - 1;
		int pageLimit = 10;
		Page<SERVER_LIST> postsPages = serverRepository.findAll(PageRequest.of(page, pageLimit));
		for (int i=0; i < postsPages.getContent().size(); i++) {
			String EnPw = postsPages.getContent().get(i).getServerpw();
			postsPages.getContent().get(i).setServerpw(Crypt.aesDecode(EnPw));
		}
		return postsPages;
	}
	
	public List<SERVER_LIST> findByserverip(String serverip) {
		AESCrypto Crypt = new AESCrypto();
		List<SERVER_LIST> findresults = serverRepository.findByserverip(serverip);
		for (int i=0; i < findresults.size(); i++) {
			findresults.get(i).setServerpw(Crypt.aesDecode(findresults.get(i).getServerpw()));
		}
		return findresults;
	}
	
	public Page<SERVERADD_HIST> pagingAddHist(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageLimit = 5;
		Page<SERVERADD_HIST> postsPages = serverAddHistRepository.findAll(PageRequest.of(page, pageLimit));
		return postsPages;
	}
	
	public void UpdatePassword(Long sid, String asispassword, String newpassword) throws Exception{
		AESCrypto Crypt = new AESCrypto();
		try {
			SERVER_LIST serverlist = validatePassword(sid, asispassword);
			serverlist.updatePassword(Crypt.aesEncode(newpassword));
			serverRepository.save(serverlist);
		} catch(Exception e) {
			log.info(">> UPDATE PASSWORD FAILED : {}", e);
		}
		
	}
	
	public SERVER_LIST validatePassword(Long sid, String asispassword) throws Exception {
		AESCrypto Crypt = new AESCrypto();
		SERVER_LIST serverList = serverRepository.findBysid(sid);
		if (!Crypt.aesDecode(serverList.getServerpw()).equals(asispassword)) {
			throw new Exception();
		}
		return serverList;
	}
	

}
