package com.myalice.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoCtrl {


	@RequestMapping("/admin/list")
	public String list() {
		return "list";
	}

	@GetMapping("/list")
	public String list1() {
		return "list";
	}
}
