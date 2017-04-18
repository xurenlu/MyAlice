package com.myalice.ctrl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.myalice.domain.Users;
import com.myalice.services.UsersService;

@Controller
@RequestMapping("/admin")
public class AdminUserCtrl {
	
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
	public PageInfo<Users> listData(Integer pageNum) {
		Users user = new Users();  
		Page<Users> searchUsers = userService.searchUsers(pageNum , user) ; 
		return new PageInfo<Users>(searchUsers) ;
	}
}
