package com.faujor.web.home;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemMsgController {
	@RequestMapping("/systemmsg")
    public String index(Model model,HttpServletResponse response){
		/*response.setHeader("X-Frame-Options", "SAMEORIGIN");*/
       /* Msg msg =  new Msg("测试标题","测试内容","额外信息，只对管理员显示");
        model.addAttribute("msg", msg);*/
        return "home/systemmsg";
    }
}
