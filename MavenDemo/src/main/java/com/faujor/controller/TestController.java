package com.faujor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.faujor.entity.User;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/getHelloWord")
	public String getHelloWord(){
		return "HelloWord";
	}
	
	@RequestMapping("/getUser")
	public User getUser(String username,String password){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}
	
	@RequestMapping("/getLoginHtml")
	public String getLoginHtml(){
		return "login";
	}
}
