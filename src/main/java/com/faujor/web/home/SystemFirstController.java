package com.faujor.web.home;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SystemFirstController {
	@RequestMapping("/systemfirst")
    public String index(Model model,HttpServletResponse response){
		/*response.setHeader("X-Frame-Options", "SAMEORIGIN");*/
       
        return "home/systemfirst";
    }
}
