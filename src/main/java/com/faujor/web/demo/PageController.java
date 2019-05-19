package com.faujor.web.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.Material;
import com.faujor.service.demo.PaginationService;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/page")
public class PageController {
	@Autowired
	private PaginationService pageService;

	/**
	 * 跳转到物料主数据列表展示页面
	 * 
	 * @return
	 */
	@RequestMapping("/getMaterialListHtml")
	public String getMaterialListHtml() {
		return "demo/materialList";
	}

	/**
	 * 物料主数据列表分页
	 * 
	 * @param mate
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryMaterialByPage")
	public Map<String, Object> queryMaterialByPage(Material mate, Integer page, Integer limit) {
		SysUserDO user = UserCommon.getUser();
		int offset = 0;
		if (page != null) {
			offset = (page - 1) * limit;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> page2 = pageService.queryMaterialByPage(offset, limit);
		return page2;
	}
}
