package com.faujor.web.bam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.faujor.entity.bam.ContTemp;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.service.bam.ContTempService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/contTemp")
public class ContTempController {
	@Autowired
	private ContTempService contTempService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private BasicService basicService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	/**
	 * 获取合同模板管理页面
	 * @return
	 */
	@Log("获取合同模板列表信息")
	@RequestMapping("/getContTempListPage")
	public String getContTempListPage(Model model){
		List<Dic> statusList = basicService.findDicListByCategoryCode("CONTTEMPSTATUS");
		List<Dic> contTypeList = basicService.findDicListByCategoryCode("CONTTYPE");
		model.addAttribute("statusList", statusList);
		model.addAttribute("contTypeList", contTypeList);
		return "bam/contTemp/contTempList";
	}
	/**
	 * 获取修改或编辑模板页面
	 * @param type
	 * 1新增 2编辑 3查看
	 * @return
	 */
	@Log("创建/编辑/查看合同模板")
	@RequestMapping("/getAddTempPage")
	public String getAddTempPage(Model model,String type,String tempNo){
		Map<String, Object> params=new HashMap<String,Object>();
		ContTemp temp=null;
		if("1".equals(type)){
			 temp=new ContTemp();
		}else {
			params.put("tempNo", tempNo);
			temp = contTempService.getTemp(params);
		}
		//模板状态 合同类型下拉
		String tempStatus = temp.getTempStatus();
		List<Dic> statusList = basicService.findDicListByCategoryCode("CONTTEMPSTATUS");
		for (Dic dic : statusList) {
			if(tempStatus!=null && tempStatus.equals(dic.getDicCode())){
				temp.setStatusName(dic.getDicName());
			}
		}
		List<Dic> contTypeList = basicService.findDicListByCategoryCode("CONTTYPE");
		model.addAttribute("statusList", statusList);
		model.addAttribute("contTypeList", contTypeList);
		model.addAttribute("temp", temp);
		model.addAttribute("type", type);
		return "bam/contTemp/addContTemp";
	}
	/**
	 * 模板管理分页查询
	 * @param page
	 * @param tempName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getContTempByPage")
	public LayuiPage<ContTemp> getContTempByPage(LayuiPage<ContTemp> page,String tempName,String contType,String tempStatus){
		SysUserDO user = UserCommon.getUser();
		String username= user.getUsername();
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("contType", contType);
		params.put("tempStatus",tempStatus);
		if(!StringUtils.isEmpty(tempName)){
			params.put("tempName", "%"+tempName+"%");
		}
		params.put("userName", username);
		LayuiPage<ContTemp> contTempPage = contTempService.getTempByPage(params);
		return contTempPage;
	}
	/**
	 * 保存模板信息
	 * @param temp
	 * @param jsonStr
	 * @return
	 */
	@Log("保存合同模板")
	@ResponseBody
	@RequestMapping("/saveContTemp")
	public RestCode saveContTemp(ContTemp temp,String jsonFile,String type){
		List<Document> docs = JsonUtils.jsonToList(jsonFile, Document.class);
		if("1".equals(type)){
			temp.setTempNo(codeService.getCodeByCodeType("contTempNo"));
			contTempService.saveTemp(temp, docs);
		}else if("2".equals(type)){
			contTempService.updateTemp(temp, docs);
		}
		return new RestCode().put("temp", temp);
	}
	/**
	 * 获取模板的日志信息
	 * @param tempNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getTempLogByTempNo")
	public List<TempLog> getTempLogByTempNo(String tempNo) {
		List<TempLog> logs = contTempService.getTempLogByTempNo(tempNo);
		return logs;
	}
	/**
	 * 删除文件
	 * @param docId
	 * @return
	 */
	@Log("删除合同模板附件")
	@RequestMapping("/deleteFile")
	@ResponseBody
	public RestCode deleteFile(String jsonStr){
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = contTempService.deleteFile(docIds);
		return restCode;
	}
	/**
	 * 删除合同模板
	 * @param tempNos
	 * @return
	 */
	@Log("删除合同模板")
	@ResponseBody
	@RequestMapping("/deleteContTemp")
	public RestCode deleteContTemp(String tempNos){
		List<String> list = JsonUtils.jsonToList(tempNos, String.class);
		contTempService.deleteContTemp(list);
		return new RestCode();
	}
	/**
	 * 变更合同模板的状态
	 * 已发布 与未发布的切换
	 * @param tempNos
	 * @param status
	 * @return
	 */
	@Log("变更合同模板状态")
	@ResponseBody
	@RequestMapping("/changeTempStatus")
	public RestCode changeTempStatus(String tempNos,String status){
		List<String> list = JsonUtils.jsonToList(tempNos, String.class);
		contTempService.changeTempStatus(list, status);
		return new RestCode();
	}
}
