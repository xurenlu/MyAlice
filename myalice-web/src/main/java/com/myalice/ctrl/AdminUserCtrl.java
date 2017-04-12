package com.myalice.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminUserCtrl {

	@RequestMapping("/admin/list")
	public String list() {
		return "redirect:/admin/index.html";
	}

}
