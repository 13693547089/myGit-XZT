package com.faujor.web.rm;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.rm.AppoDeli;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.rm.AppoDeliService;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class AppoDeliController {

	@Autowired
	private AppoDeliService appoDeliService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private ReceiveMessageService receiveMessageService;
	/**
	 * 跳转到预约送货一览列表页面
	 * @return
	 */
	@RequestMapping("/getAppoDeliListHtml")
	public String getReceiveMessageListHtml(Model model){
		//直发单、预约单
		List<Dic> appoTypeList = basicService.findDicListByCategoryCode("APPOTYPE");
		model.addAttribute("appoTypeList", appoTypeList);
		//是否占用
		List<Dic> isOccupyList = basicService.findDicListByCategoryCode("ISOCCUPY");
		model.addAttribute("isOccupyList", isOccupyList);
		//送货单状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("DJZT");
		model.addAttribute("statusList", statusList);
		//送货地址
		List<String> receUnitList= receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("receUnitList", receUnitList);
		//收货单状态
		List<Dic> receStatusList = basicService.findDicListByCategoryCode("RECEDJZT");
		model.addAttribute("receStatusList", receStatusList);
		//预约状态
		List<Dic> appoStatusList = basicService.findDicListByCategoryCode("YYZT");
		model.addAttribute("appoStatusList", appoStatusList);
		return "rm/appoDeli/appoDeliList";
	}
	
	/**
	 * 预约送货列表数据
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取预约送货列表")
	@ResponseBody
	@RequestMapping("/queryAppoDeliByPage")
	public Map<String,Object> queryReceiveMessByPage(String statusJson,String checkedStatusJson,
			String checkedAppoStatusJson,AppoDeli appoDeli2,Integer limit,Integer page){
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		//处理时间作为查询条件
		AppoDeli appoDeli  = appoDeliService.proceTime(appoDeli2);
		if(statusJson != null){
			List<String> statusList = JsonUtils.jsonToList(statusJson, String.class);
			if(statusList.size()>0){
				//送货单状态
				map.put("statusList", statusList);
			}
		}
		if(checkedStatusJson != null){
			List<String> receStatusList = JsonUtils.jsonToList(checkedStatusJson, String.class);
			if(receStatusList.size()>0){
				//收货单状态
				map.put("receStatusList", receStatusList);
			}
		}
		if(checkedAppoStatusJson != null){
			List<String> appoStatusList = JsonUtils.jsonToList(checkedAppoStatusJson, String.class);
			if(appoStatusList.size()>0){
				 //预约单状态
				map.put("appoStatusList", appoStatusList);
			}
		}
		map.put("start", start);
		map.put("end", end);
		map.put("appoDeli", appoDeli);
		Map<String, Object> result = appoDeliService.queryAppoDeliByPage(map);
		return result;
	}
	
	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportAppoDeliList")
	public String exportAppoDeliList(String objjson,String statusJsonCodes,String checkedStatusJson, String checkedAppoStatusJson,
			HttpServletRequest req, HttpServletResponse res){
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier")) {
				username = user.getSuppNo();
			}
			String fileName = username + "-预约送货一览表-" + time + ".xlsx";
			//查询预约送货数据集合
			Map<String, Object> map = new HashMap<String, Object>();
			AppoDeli appoDeli2 = JsonUtils.jsonToPojo(objjson, AppoDeli.class);
			AppoDeli appoDeli  = appoDeliService.proceTime(appoDeli2);
			if(statusJsonCodes != null){
				List<String> statusList = JsonUtils.jsonToList(statusJsonCodes, String.class);
				if(statusList.size()>0){
					map.put("statusList", statusList);
				}
			}
			if(checkedStatusJson != null){
				List<String> receStatusList = JsonUtils.jsonToList(checkedStatusJson, String.class);
				if(receStatusList.size()>0){
					map.put("receStatusList", receStatusList);
				}
			}
			if(checkedAppoStatusJson != null){
				List<String> appoStatusList = JsonUtils.jsonToList(checkedAppoStatusJson, String.class);
				if(appoStatusList.size()>0){
					map.put("appoStatusList", appoStatusList);
				}
			}
			map.put("appoDeli", appoDeli);
			List<AppoDeli> appoDeliList = appoDeliService.queryAppoDeliListByMap(map);

			//导出数据
			Workbook wb = ExportExcel.exportAppoDeliList(appoDeliList, req, res);
			ExportExcel.setAttachmentFileName(req, res, fileName);
			os = res.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	
	
}
