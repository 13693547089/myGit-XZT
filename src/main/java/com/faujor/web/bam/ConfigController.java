package com.faujor.web.bam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.UserSeries;
import com.faujor.entity.bam.psm.UserSeriesOrder;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.bam.ConfigService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.RestCode;

/**
 * 配置控制类
 * 
 * @ClassName:   ConfigController
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月31日 上午10:42:22
 *
 */
@Controller("bamConfigController")
@RequestMapping(value = "/bam/cf")
public class ConfigController {
	
	@Autowired
	private ConfigService configService;
	@Autowired
	private OrgService orgService;
	
	/**
	 * 产销业务员系列配置界面
	 * @param model
	 * @return
	 */
	@Log(value = "产销业务员系列配置界面")
	@RequestMapping("/userSeriesConfigPage")
	public String userSeriesConfigPage(Model model){
		
		return "bam/config/userSeriesConfig";
	}
	
	/**
	 * 产销业务员数据获取
	 * 
	 * @return
	 */
	@Log(value ="产销业务员数据获取")
	@ResponseBody
	@RequestMapping("/getPsUserData")
	public Map<String, Object> getPsUserData(String params) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrgDo org = new OrgDo();
		
		org.setSfname("产销部");
		org.setStype("psn");
		org.setSname(params);
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.getOrgUserByCondition(org);
		
		map.put("code", "0");
		map.put("data", list);
		map.put("count", list.size());
		map.put("msg", "");
		return map;
	}
	
	/**
	 * 根据用户编码获取对应的用户系列数据"
	 * 
	 * @param qualSupp
	 * @param userId
	 * @return
	 */
	@Log(value="根据用户编码获取对应的用户系列数据")
	@ResponseBody
	@RequestMapping("/getUserSeriesData")
	public List<UserSeries> getUserSeriesData(String userCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		List<UserSeries> list = configService.getUserSeriesByMap(map);
		return list;
	}
	
	/**
	 * 用户系列配置，删除用户系列数据"
	 * 
	 * @param qualSupp
	 * @param userId
	 * @return
	 */
	@Log(value="用户系列配置，删除用户系列数据")
	@ResponseBody
	@RequestMapping("/delUserSeriesData")
	public int delUserSeriesData(String ids) {
		String [] idsArr = ids.split(",");
		if(idsArr.length>0){
			try {
				configService.delBatchUserSeriesData(idsArr);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;
	}
	
	/**
	 * 物料系列选择弹框"
	 * 
	 * @param qualSupp
	 * @param userId
	 * @return
	 */
	@Log(value="物料系列选择弹框")
	@RequestMapping("/materialSeriesSelDialog")
	public String materialSeriesSelDialog(){
		return "bam/config/materialSeriesSelDialog";
	}	
	
	/**
	 * 获取物料系列数据
	 * @param seriesExpl 系列编码 或 名称
	 * @return
	 */
	@Log(value="获取物料系列数据")
	@ResponseBody
	@RequestMapping("/getMaterialSeriesData")
	public List<UserSeries> getMaterialSeriesData(String seriesExpl) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seriesExpl", seriesExpl.toLowerCase());
		List<UserSeries> list = configService.getMaterialSeriesData(map);
		return list;
	}
	
	/**
	 * 保存选择的物料系列数据
	 * @param userCode
	 * @param userName
	 * @param userId
	 * @param selSeriesData
	 * @return
	 */
	@Log(value="保存选择的物料系列数据")
	@ResponseBody
	@RequestMapping("/saveSelMaterialSeries")
	public int saveSelMaterialSeries(String userCode,String userName,String userId,String selSeriesData){
		
		// 转成list
		List<UserSeries> seriesList = new ArrayList<UserSeries>();
		if(seriesList !=null && !"".equals(selSeriesData)){
			seriesList =  JSON.parseArray(selSeriesData,UserSeries.class);
		}
		for(int i=0;i<seriesList.size();i++){
			UserSeries item = seriesList.get(i);
			item.setId(UUID.randomUUID().toString());
			item.setUserCode(userCode);
			item.setUserName(userName);
			item.setUserId(userId);
		}
		
		try {
			configService.saveUserSeriesInfo(seriesList);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
	
	
	/**********************用户系列排序**********************/
	
	@Log(value = "用户系列排序的用户列表界面")
	@RequestMapping("/seriesOrderUserPage")
	public String seriesOrderUserPage(Model model){
		return "bam/config/userSeriesOrderConfig";
	}
	
	/**
	 * 获取用户系列排序的用户列表数据
	 * @param page
	 * @param userCodeName
	 * @return
	 */
	@Log(value = "获取用户系列排序的用户列表数据")
	@ResponseBody
	@RequestMapping("/getSeriesOrderUserData")
	public LayuiPage<UserSeriesOrder> getSeriesOrderUserData(LayuiPage<UserSeriesOrder> page,String userCodeName){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("userCodeName", userCodeName);
		
		return configService.getSeriesOrderUserByPage(params);
	}
	
	/**
	 * 保存用户系列排序选择的用户数据
	 * @param detailData
	 * @return
	 */
	@Log(value = "保存用户系列排序选择的用户数据")
	@ResponseBody
	@RequestMapping("/saveSeriesOrderUserData")
	public RestCode saveSeriesOrderUserData(String userData){
		 
		List<UserSeriesOrder> list = new ArrayList<UserSeriesOrder>();
		if(userData !=null && !"".equals(userData)){
			list =  JSON.parseArray(userData,UserSeriesOrder.class);
		}
		
		if(list.size()>0){
			try {
				configService.saveSeriesOrderUserData(list);
			} catch (Exception e) {
				e.printStackTrace();
				return RestCode.error(-1,e.getMessage());
			}
		}
		return RestCode.ok();
	}
	
	/**
	 * 删除用户系列排序用户及对应的系列排序数据
	 * @param ids
	 * @return
	 */
	@Log(value = "删除用户系列排序用户及对应的系列排序数据")
	@ResponseBody
	@RequestMapping("/delSeriesOrderUserData")
	public int delSeriesOrderUserData(String ids){
		List<String> idsList = Arrays.asList(ids.split(","));
		if(idsList.size()>0){
			try {
				configService.delSeriesOrderUserInfo(idsList);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;
	}
	
	/**
	 * 获取用户系列排序数据
	 * @param parentId
	 * @return
	 */
	@Log(value = "获取用户系列排序数据")
	@ResponseBody
	@RequestMapping("/getUserSeriesOrderDetail")
	public List<UserSeriesOrder> getUserSeriesOrderDetail(String parentId){
		return configService.getUserSeriesOrderDetail(parentId);
	}

	/**
	 * 保存
	 * @param detailData
	 * @return
	 */
	@Log(value = "保存用户系列排序数据")
	@ResponseBody
	@RequestMapping("/saveUserSeriesOrderDetail")
	public RestCode saveUserSeriesOrderDetail(String detailData){
		 
		List<UserSeriesOrder> list = new ArrayList<UserSeriesOrder>();
		if(detailData !=null && !"".equals(detailData)){
			list =  JSON.parseArray(detailData,UserSeriesOrder.class);
		}
		
		if(list.size()>0){
			try {
				configService.saveUserSeriesOrderDetail(list);
			} catch (Exception e) {
				e.printStackTrace();
				return RestCode.error(-1,e.getMessage());
			}
		}
		return RestCode.ok();
	}
	
	/**
	 * 删除多个用户系列排序数据
	 * @param ids
	 * @return
	 */
	@Log(value = "删除多个用户系列排序数据")
	@ResponseBody
	@RequestMapping("/delUserSeriesOrderDetailInfo")
	public int delUserSeriesOrderDetailInfo(String ids){
		String[] idsArr = ids.split(",");
		if(idsArr.length>0){
			try {
				configService.delUserSeriesOrderInfo(idsArr);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;
	}
	
	/**
	 * 用户选择弹框
	 * @param qualSupp
	 * @param userId
	 * @return
	 */
	@Log(value="用户选择弹框")
	@RequestMapping("/userSelDialog")
	public String userSelDialog(){
		return "bam/config/userSelDialog";
	}
}
