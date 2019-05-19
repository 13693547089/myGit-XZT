package com.faujor.web.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.entity.task.ProcTestDO;
import com.faujor.entity.task.TestDO;
import com.faujor.service.demo.ProcessTestService;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/process")
public class ProcessController {
	@Autowired
	private ProcessTestService ptService;

	@RequestMapping("/test")
	public String index(Model model, String sdata1) {
		ProcTestDO ptd = new ProcTestDO();
		if (!StringUtils.isEmpty(sdata1)) {
			ptd = ptService.findTestById(sdata1);
		}
		model.addAttribute("ptd", ptd);
		return "/task/test/test";
	}

	@RequestMapping("/save")
	@ResponseBody
	public RestCode save(TestDO td, String executors) {
		int i = ptService.saveProcessTest(td, executors);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/saveTestInfo")
	@ResponseBody
	public RestCode saveTestInfo(TestDO td) {
		int k = ptService.saveTestInfo(td);
		if (k > 0)
			return RestCode.ok();
		return RestCode.error();
	}

}
