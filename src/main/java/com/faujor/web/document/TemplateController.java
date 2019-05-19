package com.faujor.web.document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.entity.document.TempMenu;
import com.faujor.entity.document.Template;
import com.faujor.service.common.CodeService;
import com.faujor.service.document.TemplateService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/template")
public class TemplateController {
	@Autowired
	private TemplateService templateService;
	@Autowired
	private CodeService codeService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	/**
	 * 获取文件管理页面
	 * @return
	 */
	@Log("获取模板列表信息")
	@RequestMapping("/getTempListPage")
	public String getTempListPage(){
		return "document/tempList";
	}
	/**
	 * 获取添加角色的页面
	 * @return
	 */
	@RequestMapping("/getAddMenuPage")
	public String getAddMenuPage(String tempNo,Model model){
		model.addAttribute("tempNo", tempNo);
		return "document/editMenu";
	}
	/**
	 * 获取修改或编辑模板页面
	 * @param type
	 * 1新增 2编辑 3查看
	 * @return
	 */
	@Log("创建/编辑/查看模板")
	@RequestMapping("/getAddTempPage")
	public String getAddTempPage(Model model,String type,String tempNo){
		Map<String, Object> params=new HashMap<String,Object>();
		Template temp=null;
		if("1".equals(type)){
			 temp=new Template();
		}else {
			params.put("tempNo", tempNo);
			temp = templateService.getTemp(params);
		}
		model.addAttribute("temp", temp);
		model.addAttribute("type", type);
		return "document/addTemp";
	}
	/**
	 * 模板管理分页查询
	 * @param page
	 * @param tempName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTempByPage")
	public LayuiPage<Template> getDocByPage(LayuiPage<Template> page,String tempName){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		if(!StringUtils.isEmpty(tempName)){
			params.put("tempName", "%"+tempName+"%");
		}
		LayuiPage<Template> tempPage = templateService.getTempByPage(params);
		System.out.println(JsonUtils.beanToJson(tempPage));
		return tempPage;
	}
	/**
	 * 保存模板信息
	 * @param temp
	 * @param jsonStr
	 * @return
	 */
	@Log("保存模板")
	@ResponseBody
	@RequestMapping("/saveTemp")
	public RestCode saveTemp(Template temp,String jsonFile,String jsonMenu,String type){
		List<Document> docs = JsonUtils.jsonToList(jsonFile, Document.class);
		List<TempMenu> tempMenus = JsonUtils.jsonToList(jsonMenu, TempMenu.class);
		if("1".equals(type)){
			temp.setTempNo(codeService.getCodeByCodeType("tempNo"));
			templateService.saveTemp(temp, docs, tempMenus);
		}else if("2".equals(type)){
			templateService.updateTemp(temp, docs, tempMenus);
		}
		
		return new RestCode().put("temp", temp);
	}
	/**
	 * 获取模板的树形菜单
	 * @param tempNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMenuTree")
	public Tree<MenuDO> getMenuTree( String tempNo) {
		Tree<MenuDO> tree = new Tree<MenuDO>();
		tree = templateService.getTree(tempNo);
		return tree;
	}
	/**
	 * 获取菜单列表
	 * @param tempNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMenuList")
	public List<MenuDO> getMenuList( String tempNo) {
		List<MenuDO> menuList = templateService.getMenuList(tempNo);
		return menuList;
	}
	/**
	 * 获取选中的菜单
	 * @param jsonStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCheckedMenus")
	public List<MenuDO> getCheckedMenus(String jsonStr){
		List<Long> menuIds=JsonUtils.jsonToList(jsonStr, Long.class);
		List<MenuDO> menus = templateService.getCkeckedMenus(menuIds);
		return menus;
	}
	/**
	 * 获取模板的日志信息
	 * @param tempNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getTempLogByTempNo")
	public List<TempLog> getTempLogByTempNo(String tempNo) {
		List<TempLog> logs = templateService.getTempLogByTempNo(tempNo);
		return logs;
	}
	/**
	 * 删除文件
	 * @param docId
	 * @return
	 */
	@Log("删除模板附件")
	@RequestMapping("/deleteFile")
	@ResponseBody
	public RestCode deleteFile(String jsonStr){
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = templateService.deleteFile(docIds);
		return restCode;
	}
	/**
	 * 删除模板
	 * @param tempNo
	 * @return
	 */
	@Log("删除模板")
	@ResponseBody
	@RequestMapping("/deleteTemp")
	public RestCode deleteTemp(String tempNos){
		List<String> list = JsonUtils.jsonToList(tempNos, String.class);
		templateService.deleteTemp(list);
		return new RestCode();
		
	}
}
