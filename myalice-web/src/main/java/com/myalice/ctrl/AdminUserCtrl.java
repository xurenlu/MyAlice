package com.myalice.ctrl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.myalice.domain.Users;
import com.myalice.services.UsersService;
import com.myalice.utils.ResponseMessageBody;

@Controller
@RequestMapping("/admin")
public class AdminUserCtrl {
	
	protected static Logger logger = org.slf4j.LoggerFactory.getLogger("ctrl") ; 
	
	@Autowired
	UsersService userService;
	
	@RequestMapping("/list")
	public String list() {
		return "redirect:/admin/index.html";
	}
	
	@RequestMapping("/loadUserinfo")
	@ResponseBody
	public Map<String,Object> loadUserinfo(Principal principal){
		 Map<String,Object> principalMap = new HashMap<>();
		 principalMap.put("username", principal.getName()); 
		 return principalMap;
	}
	
	@RequestMapping("/listdata")
	@ResponseBody
	public PageInfo<Users> listData(Integer pageNum,Users user){
		Page<Users> searchUsers = userService.searchUsers(pageNum , user) ; 
		return new PageInfo<Users>(searchUsers) ;
	}
	
	@PostMapping("/user/enable")
	@ResponseBody
	public ResponseMessageBody enable( String[]username,Integer enable){
		try {
			int enableUser = userService.enableUser(username, enable ) ;
			return new ResponseMessageBody("账号启用成功" , enableUser>0) ; 
		} catch (Exception e) {
			return new ResponseMessageBody("账号启用失败,原因：" 
					+ e.getMessage() , false) ;
		}
	}
}