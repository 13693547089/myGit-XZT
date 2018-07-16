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
	
	@PostConstruct
	public void init() {
		User user1 = new User("1","lisi","123456",23);
		User user2 = new User("2","xiao","78900",30);
		map.put(user1.getId(), user1);
		map.put(user2.getId(), user2);
	}
	
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
		System.out.println("111111");
		return "登录成功";
	}
	
	
	
	
	
}
