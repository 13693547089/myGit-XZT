package com.faujor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class LoginController {

	@ResponseBody
	@RequestMapping("/login")
	public String login(){
		return "登录成功";
	}
}
