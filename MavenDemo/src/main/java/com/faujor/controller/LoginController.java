package com.faujor.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.User;

@Controller
@RequestMapping("/user")
public class LoginController {

	Map<String, User> map = new HashMap<String, User>();
	
	
	
	@ResponseBody
	@GetMapping("/get")
	public User getMethod(String id){
		return map.get(id);
	}
	
	@ResponseBody
	@PostMapping("/post")
	public User postMethod(User user){
		Random random = new Random();
		user.setId(random.toString());
		map.put(user.getId(), user);
		Set<String> keySet = map.keySet();
		for (String ket : keySet) {
			User user2 = map.get(ket);
		}
		return user;
	}
	
	
	
	
	@ResponseBody
	@RequestMapping("/login")
	public String login(){
		System.out.println("111111222");
		return "登录成功";
	}
	@RequestMapping("/getHello")
	public String getHelloHtml(){
		return "hello";
	}
	
	
	
	
}
