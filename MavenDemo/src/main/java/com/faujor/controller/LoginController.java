package com.faujor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class LoginController {

	@RequestMapping("/login")
	public String login(){
		return "登录成功";
	}
}
