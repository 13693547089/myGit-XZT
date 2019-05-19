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
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.rm.AppoDeli;
import com.faujor.entity.rm.UserSuppMate;
import com.faujor.service.rm.UserSuppMateService;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class UserSuppMateController {

	@Autowired
	private UserSuppMateService userSuppMateService;
	
	/**
	 * 跳转到货源一览列表页面
	 * @return
	 */
	@RequestMapping("/getUserSuppMateHtml")
	private String getUserSuppMateHtml() {
		return "rm/source/userSuppMate";
	}
	
	/**
	 * 获取货源一览列表数据
	 * @param limit
	 * @param page
	 * @param userSuppMate
	 * @return
	 */
	@Log(value ="获取货源一览列表数据")
	@ResponseBody
	@RequestMapping("/getUserSuppMateListByPage")
	public Map<String, Object> getUserSuppMateListByPage(Integer limit,Integer page,UserSuppMate userSuppMate) {
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("userSuppMate", userSuppMate);
		Map<String, Object> result = userSuppMateService.getUserSuppMateListByPage(map);
		return result;
	}
	
	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportUserSuppMateList")
	public String exportUserSuppMateList(String objjson,
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
			String fileName = username + "-采购货源一览表-" + time + ".xlsx";
			//查询采购货源数据集合
			UserSuppMate userSuppMate = JsonUtils.jsonToPojo(objjson, UserSuppMate.class);
			List<UserSuppMate> list = userSuppMateService.getUserSuppMateList(userSuppMate);
			//导出采购货源一览表数据
			Workbook wb = ExportExcel.exportUserSuppMateList(list, req, res);
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
