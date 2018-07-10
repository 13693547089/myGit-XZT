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
		System.out.println("111111");
		return "登录成功";
	}
}
