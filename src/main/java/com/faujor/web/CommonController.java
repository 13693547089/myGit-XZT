package com.faujor.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CommonController {

	@Value("${prourl}")
	private String prourl;

	@RequestMapping("/commonMethod")
	@ResponseBody
	public Map<String, Object> commonMethod(String str) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prourl", prourl);
		return map;
	}

	@RequestMapping("/currentUserMethod")
	@ResponseBody
	public Map<String, Object> currentUserMethod() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prourl", prourl);
		return map;
	}
}