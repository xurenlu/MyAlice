package com.myalice.ctrl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminUserCtrl {

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
}
