package com.faujor.web.bam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.faujor.common.annotation.Log;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.core.plugins.redis.RedisClient;
import com.faujor.entity.bam.psm.PadMateMess;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PadPlanDetailForm;
import com.faujor.entity.bam.psm.PadPlanRecord;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.bam.psm.UserSeries;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.service.bam.ConfigService;
import com.faujor.service.bam.PadPlanService;
import com.faujor.service.bam.SaleFcstService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.sapcenter.bam.MatInfoService;
import com.faujor.service.sapcenter.bam.SapPadPlanService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

/**
 * 生产/交货计划 控制类
 * @author Vincent
 *
 */
@Controller
@RequestMapping(value = "/bam/ps")
public class PadPlanController {
	
	@Autowired
	private PadPlanService padPlanService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MatInfoService matInfoService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private SaleFcstService saleFcstService;
	
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private SapPadPlanService sapPadPlanService;
	@Autowired
	private ConfigService configService;
	
	@Value("${ftp_base_path}")
	private  String ftpBasePath ;
	/**
	 * 生产/交货计划列表
	 * @param model
	 * @return
	 */
	@Log(value = "生产/交货计划列表信息")
	@RequestMapping("/padPlanPage")
	public String padPlanPage(Model model){
		
		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);
		// 获取当前用户登录编码
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("userCode", user.getUsername());
		
		// 判断是否为产销部人员 0：不是，1：是
		String isPad = "0";
		OrgDo org = orgService.findOrgByUserId(user.getUserId());
		if(org != null){
			String sfname = org.getSfname();
			if(sfname!=null && sfname.contains("产销部")){
				isPad = "1";
			}
		}
		model.addAttribute("isPad", isPad);
		
		return "bam/padPlan/padPlanList";
	}
	
	/**
	 * 生产/交货计划查看
	 * @param model
	 * @return
	 */
	@Log(value = "生产/交货计划查看列表信息")
	@RequestMapping("/padPlanViewPage")
	public String padPlanViewPage(Model model){
		
		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);
		// 获取当前用户登录编码
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("userCode", user.getUsername());
		
		// 判断是否为产销部人员 0：不是，1：是
		String isPad = "0";
		/*OrgDo org = orgService.findOrgByUserId(user.getUserId());
		if(org != null){
			if(org.getSfname().contains("产销部")){
				isPad = "1";
			}
		}*/
		model.addAttribute("isPad", isPad);
		
		return "bam/padPlan/padPlanView";
	}
	
	/**
	 * 生产/交货计划列表分页查询
	 * @param page
	 * @param search_planName
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @param flag 1:加上用户过滤  2:不加上用户过滤
	 * @return
	 */
	@Log(value = "获取生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/getPadPlanPageList")
	public LayuiPage<PadPlan> getPadPlanPageList(LayuiPage<PadPlan> page,String search_planName,String search_status,String search_crtDateStart,String search_crtDateEnd,String flag){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("planName", search_planName);
		params.put("planCode","");
		params.put("status",search_status);
		params.put("crtDateStart",search_crtDateStart);
		params.put("crtDateEnd",search_crtDateEnd);
		params.put("crtUser","");
		
		// 1：加上用户过滤
		if(flag.equals("1")){
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			params.put("userCode",user.getUsername());
		}
		
		return padPlanService.getPadPlanByPage(params);
	}
	
	/**
	 * 删除生产/交货计划
	 * @param id
	 * @return
	 */
	@Log(value = "删除单个生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/deletePadPlan")
	public RestCode deletePadPlan(String id){
		padPlanService.delPadPlanInfoById(id);
		return new RestCode();
	}
	
	/**
	 * 批量删除删除生产/交货计划
	 * @param ids
	 * @return
	 */
	@Log(value = "删除批量生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/deleteBatchPadPlan")
	public RestCode deleteBatchPadPlan(String ids){
		
		List<String> list = JSON.parseArray(ids, String.class);
		
		padPlanService.delBatchPadPlanInfoByIds(list);
		return new RestCode();
	}
	
	/**
	 * 生产/交货计划明细列表
	 * @param model
	 * @param mainId
	 * @param type 类型： 1：编辑或创建 ，2：查看
	 * @return
	 */
	@Log(value = "创建/编辑/查看 生产/交货计划明细")
	@RequestMapping("/padPlanDetailPage")
	public String padPlanDetailPage(Model model,String mainId,String type){
		
		model.addAttribute("type", type);

		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		PadPlan padPlan = new PadPlan();
		if(mainId == null || mainId.equals("")){
			//******* 创建
			// 设置创建人
			padPlan.setCrtUser(user.getName());
			padPlan.setCrtUserCode(user.getUsername());
			// id
			padPlan.setId(UUIDUtil.getUUID());
			// planCode
			padPlan.setPlanCode("");
			// 状态
			padPlan.setStatus(""); //已保存
			// 同步标记, 0:未同步
			padPlan.setSyncFlag("0");
			
			padPlan.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		}else{
			//****** 编辑或查看
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("id", mainId);
			padPlan = padPlanService.getPadPlanByMap(map);

			// 设置编辑中标记
			String key = "PAD_"+padPlan.getPlanCode();
			String users = redisClient.get(key);
			String keyValue = "";
			String userCode =  user.getUsername();
			boolean isAdd = true;
			if(users == null){
				keyValue = userCode;
			}else{
				if(users.equals(userCode)){
					isAdd = false;
				}
				if(isAdd && !(","+users+",").contains(","+userCode+",")){
					keyValue=users +","+userCode;
				}
			}
			if(isAdd){
				redisClient.set(key,keyValue,10*60);
			}
		}
		
		// 设置主表ID
		model.addAttribute("mainId", padPlan.getId());
		// 主表信息
		model.addAttribute("list", padPlan);
		// 当前月份
		String currDate = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
		model.addAttribute("pCurrDate", currDate);
		
		return "bam/padPlan/padPlanDetail";
	}
	
	@Log(value = "已提交状态下编辑交货计划明细")
	@RequestMapping("/padPlanDetailSubmitPage")
	public String padPlanDetailSubmitPage(Model model,String mainId,String type){
		
		model.addAttribute("type", type);

		//****** 编辑或查看
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("id", mainId);
		PadPlan padPlan = padPlanService.getPadPlanByMap(map);
		
		// 获取当前用户
		SysUserDO user = UserCommon.getUser();

		// 设置编辑中标记
		String key = "PAD_"+padPlan.getPlanCode();
		String users = redisClient.get(key);
		String keyValue = "";
		String userCode =  user.getUsername();
		boolean isAdd = true;
		if(users == null){
			keyValue = userCode;
		}else{
			if(users.equals(userCode)){
				isAdd = false;
			}
			if(isAdd && !(","+users+",").contains(","+userCode+",")){
				keyValue=users +","+userCode;
			}
		}
		if(isAdd){
			redisClient.set(key,keyValue,10*60);
		}
		
		// 设置主表ID
		model.addAttribute("mainId", padPlan.getId());
		// 主表信息
		model.addAttribute("list", padPlan);
		// 当前月份
		String currDate = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
		model.addAttribute("pCurrDate", currDate);
		
		return "bam/padPlan/padPlanDetailSubmit";
	}
	
	/**
	 * 生产/交货计划明细列表分页查询
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取生产/交货计划明细信息")
	@ResponseBody
	@RequestMapping("/getPadPlanDetailPageList")
	public LayuiPage<PadPlanDetail> getPadPlanDetailPageList(LayuiPage<PadPlanDetail> page,String mainId){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("mainId", mainId);
		return padPlanService.getPadPlanDetailPage(params);
	}
	
	/**
	 * 生产/交货计划明细明细数据获取
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取生产/交货计划明细数据")
	@ResponseBody
	@RequestMapping("/getPadPlanDetailList")
	public List<PadPlanDetail> getPadPlanDetailList(String mainId){
		return padPlanService.getPadPlanDetailListByMainId(mainId);
	}
	
	/**
	 * 保存生产/交货计划信息
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "保存生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/savePadPlanInfo")
	public RestCode savePadPlanInfo(PadPlan padPlan,String padPlanDetailData){
		// 判断月份是否已存在
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("planMonth", padPlan.getPlanMonth());
		map.put("nonId", padPlan.getId());
		int rs = padPlanService.getPadPlanCount(map);
		if(rs>0){
			return RestCode.error(90, "已存在该月份的计划，请重新选择！");
		}
		
		// 设置编码
		if(padPlan.getPlanCode()==null || padPlan.getPlanCode().equals("")){
			// planCode , 生产交货编码规则代码 ：padPlanNo
			String planCode =codeService.getCodeByCodeType("padPlanNo");
			padPlan.setPlanCode(planCode);
		}

		// 明细数据
		List<PadPlanDetail> detailList = dealDetailData(padPlanDetailData);

		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		padPlanService.savePadPlanInfo(padPlan, detailList,user.getUsername());
		
		return RestCode.ok(0,padPlan.getPlanCode());
	}
	
	/**
	 * 提交
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "提交生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/submitPadPlan")
	public RestCode submitPadPlan(PadPlan padPlan,String padPlanDetailData){
		
		// 判断月份是否已存在
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("planMonth", padPlan.getPlanMonth());
		map.put("nonId", padPlan.getId());
		int rs = padPlanService.getPadPlanCount(map);
		if(rs>0){
			return RestCode.error(90, "已存在该月份的计划，请重新选择！");
		}
		
		// 设置编码
		if(padPlan.getPlanCode()==null || padPlan.getPlanCode().equals("")){
			// planCode , 生产交货编码规则代码 ：padPlanNo
			String planCode =codeService.getCodeByCodeType("padPlanNo");
			padPlan.setPlanCode(planCode);
		}
		
		// 设置同步标记为 0 ,允许重新同步
		padPlan.setSyncFlag("0");
		
		// 明细数据
		List<PadPlanDetail> detailList = dealDetailData(padPlanDetailData);
		
		// 设置状态为 已提交
		padPlan.setStatus("已提交");
		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		padPlan.setSubmitUserCode(user.getUsername());
		padPlan.setSubmitUserName(user.getName());
		padPlan.setSubmitDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
		
		// 保存
		padPlanService.savePadPlanInfo(padPlan, detailList,user.getUsername());
		
		// 编码缓存处理
		dealPlanCodeCache(user.getUsername(),"PAD_"+padPlan.getPlanCode());
		
		// 同步至sap的数据库
		try {
			int res = sapPadPlanService.saveSapPadPlanInfo(padPlan, detailList);
			if(res>0){
				// 修改同步状态 1: 已同步
				padPlanService.updatePadPlanSyncFlag("1",padPlan.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//*******【已弃用】 更新下个月计划中的明细数据 
		/*String ym = padPlan.getPlanMonth();
		String[] ymArr = ym.split("-");
		int year = Integer.parseInt(ymArr[0]);
		int month = Integer.parseInt(ymArr[1]);
		String nextYm = "";
		if(month == 12){
			nextYm = (year+1)+"-01";
		}else{
			int nextMonth = month+1;
			nextYm = year+"-"+(nextMonth<10?("0"+nextMonth):nextMonth);
		}
		
		// 获取下个月计划的ID
		Map<String, Object> pMap=new HashMap<String,Object>();
		pMap.put("planMonth", nextYm);
		PadPlan nextPadPlan = padPlanService.getPadPlanByMap(pMap);
		String nextID = "";
		if(nextPadPlan != null){
			nextID = nextPadPlan.getId();
		}
		// 用本计划中的下个月销售预测更新下个计划中的本月销售测试
		if(!nextID.equals("")){
			String currID = padPlan.getId();
			
			Map<String, Object> dMap=new HashMap<String,Object>();
			dMap.put("currID", currID);
			dMap.put("nextID", nextID);
			padPlanService.updateNextPadPlanDetail(dMap);
		}*/
		
		return RestCode.ok(0,padPlan.getPlanCode());
	}
	
	/**
	 * 处理明细数据
	 * @param padPlanDetailData
	 * @return
	 */
	private List<PadPlanDetail> dealDetailData(String padPlanDetailData){
		// 明细数据转成list
		List<PadPlanDetail> detailList = JSON.parseArray(padPlanDetailData, PadPlanDetail.class);
		for(int i=0;i<detailList.size();i++){
			PadPlanDetail item = detailList.get(i);
			
			// 设置明细ID
			item.setId(UUIDUtil.getUUID());
			// 默认值
			item.setNationStock3(0.0f);

			item.setNationStock1(item.getNationStock1()==null?0:Math.round(item.getNationStock1())+0.0f);
			item.setNationStock2(item.getNationStock2()==null?0:Math.round(item.getNationStock2())+0.0f);
			item.setPadPlanQty(item.getPadPlanQty()==null?0:Math.round(item.getPadPlanQty())+0.0f);
			item.setSaleForeQty(item.getSaleForeQty()==null?0:Math.round(item.getSaleForeQty())+0.0f);
			item.setTurnOverDays(item.getTurnOverDays()==null?0:Math.round(item.getTurnOverDays())+0.0f);
			item.setNextSaleForeQty(item.getNextSaleForeQty()==null?0:Math.round(item.getNextSaleForeQty())+0.0f);
			item.setEstDeliQty(item.getEstDeliQty()==null?0:Math.round(item.getEstDeliQty())+0.0f);
			item.setEstSaleQty(item.getEstSaleQty()==null?0:Math.round(item.getEstSaleQty())+0.0f);
			item.setActSaleQty(item.getActSaleQty()==null?0:Math.round(item.getActSaleQty())+0.0f);
			item.setActDeliQty(item.getActDeliQty()==null?0:Math.round(item.getActDeliQty())+0.0f);
			item.setActTurnOverDays(item.getActTurnOverDays()==null?0:Math.round(item.getActTurnOverDays())+0.0f);
		}
		
		return detailList;
	}
	
	/**
	 * 物料选择弹出框
	 * @return
	 */
	@RequestMapping("/matSelectDialog")
	public String matSelectDialog(){
		return "bam/padPlan/matSelectDialog";
	}
	
	
	/**
	 * 获取物料的数据
	 * @param matInfoData
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatExtraInfo")
	public String getMatExtraInfo(String matInfoData,String year,String month,String mainId){
		
		// 获取选择的物料数据
		List<PadPlanDetail> checkMatInfo = JSON.parseArray(matInfoData, PadPlanDetail.class);

		int mYear = Integer.parseInt(year);
		int mMonth = Integer.parseInt(month);
		
		String preYm = "";
		// 上个年月
		if(mMonth == 1){
			preYm = (mYear-1)+"-12";
		}else{
			preYm = year+"-"+(mMonth<11?"0"+(mMonth-1):(mMonth-1));
		}
		
		// 获取激活状态下的销售预测的字段
		Map<String, Object> sMap = new HashMap<String, Object>();
		sMap.put("status", "激活");
		List<SaleForecast> saleList = saleFcstService.getSaleFcstByCondition(sMap);
		
		String saleId = "";
		String columnName = "'0'";
		String nextColumnName = "'0'";
		if(saleList.size()>0){
			SaleForecast saleFore = saleList.get(0);
			saleId = saleFore.getId();
			// 预测期间
			String saleYm = saleFore.getFsctYear();
			String[] yearArr = saleYm.split("~");
			String sYm = yearArr[0].trim();
			
			String[] sYmArr = sYm.split("-");
			String sYear = sYmArr[0];
			String sMonth = sYmArr[1];
			int sY = Integer.parseInt(sYear);
			int sM = Integer.parseInt(sMonth);
			
			// 计算与初始的年月相差月份
			int qty = (mYear-sY)*12+mMonth-sM;
			if(qty>=0 && qty<24){
				if(qty>=12){
					columnName = "sale_Fore"+(qty-11);
					if(qty-10>12){
						// 当前月份为最后一月
						nextColumnName = "sale_Fore1";
					}else{
						nextColumnName = "sale_Fore"+(qty-10);
					}
				}else{
					columnName = "sale_Fore_Qty"+(qty+1);
					if(qty+2>12){
						nextColumnName = "sale_Fore1";
					}else{
						nextColumnName = "sale_Fore_Qty"+(qty+2);
					}
				}
			}else{
				columnName = "'0'";
				nextColumnName = "'0'";
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", checkMatInfo);
		map.put("preYm", preYm);
		map.put("mainId", mainId);
		
		map.put("saleId", saleId);
		map.put("columnName", columnName);
		map.put("nextColumnName", nextColumnName);
		
		// 加入 销售预测中获取 当月销售预测与下个月销售预测
		List<PadPlanDetail> tempList = padPlanService.getPadPlanTempDetailByMap(map);
		
		// =-------分为当前月与未来月
		// 当前年月
		String currDate = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
		String[] currYmArr = currDate.split("-");
		String cYear = currYmArr[0];
		String cMonth = currYmArr[1];
		int cY = Integer.parseInt(cYear);
		int cM = Integer.parseInt(cMonth);
		
		boolean isFuture = false;
		if(cY<mYear){
			// 未来月处理
			isFuture = true;
		}else if(cY==mYear){
			if(cM>=mMonth){
				// 取中间表中的数据
				isFuture = false;
			}else{
				// 未来月
				isFuture = true;
			}
		}else{
			// 取中间表中的数据
			isFuture = false;
		}
		
		if(isFuture){
			// 未来月
			return JSON.toJSONString(tempList);
		}else{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mainId", mainId);
			paramMap.put("year", year);
			paramMap.put("month", month);
			paramMap.put("preYear", mYear-1);
			if(mMonth == 12){
				paramMap.put("preYearT", year);
				paramMap.put("nextMonth", "01");
			}else{
				paramMap.put("preYearT", String.valueOf(mYear-1));
				String nextMonth = String.valueOf(mMonth+1);
				paramMap.put("nextMonth", nextMonth.length()==1?"0"+nextMonth:nextMonth);
			}
			paramMap.put("list", tempList);
			// 当前月及以前
			return JSON.toJSONString(matInfoService.getPadPlanMatInfo(paramMap));
		}
	}
	
	/**新建预约中
	 * 弹出框的内容
	 * 查询供应商对应的所有物料
	 * @return
	 */
	@Log(value = "新建预约中，获取供应商对应的所有物料")
	@ResponseBody
	@RequestMapping("/queryAllMaterialOfSupp")
	public List<MateDO> queryAllMaterialOfSupp(String matType,String suppCodeName,String seriesExpl){
		/*SysUserDO user = UserCommon.getUser();//注意后期修改，供应商用户对应的供应商编号suppId
		if("supplier".equals(user.getUserType())){
			List<MateDO> list = materialService.queryAllMaterialOfSuppBySapId(user.getUsername());
			return list;
		}else{
		}*/
		
		MateDO mateDo = new MateDO();
		mateDo.setMateName(suppCodeName);
		mateDo.setMateType(matType);
		mateDo.setSeriesExpl(seriesExpl);
		return materialService.findMateDOList(mateDo);
	}
	
	private HashMap<Integer,Object> summaryCalc(HashMap<Integer,Object> sumMap,int key,float val){
		
		if(sumMap.containsKey(key)){
			float sum = Float.parseFloat(sumMap.get(key).toString());
			sum+=val;
			sumMap.put(key, sum);
		}else{
			sumMap.put(key, val);
		}
		return sumMap;
	}
	
	/**
	 * 导出生产计划excel
	 * @param response
	 */
	@Log(value = "导出进销存报表")
	@ResponseBody
	@RequestMapping("/exportPadPlanInfo")
	public void exportPadPlanInfo(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		
		Workbook wb;
		try {
			
			// 获取报表数据
			String currYm = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
			
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN_TWO);

			SysUserDO user = UserCommon.getUser();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userCode", user.getUsername());
			paramMap.put("planMonth", currYm);
			
			// 处理后续月份条件
			// 当前年份
			String currYy = currYm.split("-")[0];
			String currMstr = currYm.split("-")[1];
			// 当前月份
			int currM = Integer.parseInt(currMstr);
			// 去年
			String preY = String.valueOf(Integer.valueOf(currYy)-1);
			String preYy = preY.substring(preY.length()-2,preY.length());
			// 明年
			String nextYy = String.valueOf(Integer.valueOf(currYy)+1);
			String nextY = nextYy.substring(nextYy.length()-2,nextYy.length());
			
			// 参数
			paramMap.put("preYear", preY);
			paramMap.put("currYear", currYy);
			paramMap.put("currMonth", currMstr);
			
			String months = "";
			String pMonths = "";
			int j = 0;
			for(int i=currM+1;i<=12;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				months += "'"+currYy+"-"+m+"' as p"+j+",";
			}
			for(int i=1;i<=currM;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				months += "'"+nextYy+"-"+m+"' as p"+j+",";
			}
			
			j=0;
			for(int i=currM;i<=12;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				pMonths += "'"+currYy+"-"+m+"' as p"+j+",";
			}
			for(int i=1;i<currM;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				pMonths += "'"+nextYy+"-"+m+"' as p"+j+",";
			}
			
			months = months.substring(0, months.length()-1);
			pMonths = pMonths.substring(0, pMonths.length()-1);
			paramMap.put("months", months);
			paramMap.put("pMonths", pMonths);
			
			//String basepath = request.getSession().getServletContext().getRealPath("/").toString();
			
			//InputStream stream = getClass().getClassLoader().getResourceAsStream("templates\\excelTemp\\psiTemp.xls");
			////File targetFile = new File("xxx.pdf");
			////FileUtils.copyInputStreamToFile(stream, targetFile);
			
			//wb = ExcelUtil.getWorkBook(stream,".xls");
			
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath+"templates\\excelTemp\\psiTemp3.xls";
			File file = new File(xlsTemplatePath);
			if(!file.exists()){
				xlsTemplatePath = "C:\\TOP_SRM\\excelTemp\\psiTemp3.xls";
			}
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			
			String fileName = "进销存报表"+currDate;
			// 设置response参数，可以打开下载页面
	        response.reset();
			//response.setCharacterEncoding("UTF-8");
	       	//response.setContentType("application/octet-stream");
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls"); // +
																// URLEncoder.encode(new
																// RequestContext(request).getMessage("qmg.air.export.searchresult"),
																// "UTF-8")
			Sheet sheet = wb.getSheetAt(0);
			
			// sheet 名称改变
			wb.setSheetName(0, currDate);
			
			// 设置标题
			ExcelUtil.setValue(sheet, 0, 0, currYy+"年家用生产进销存计划表-"+currDate, null);
			
			//***** 设置列头 *****
			// 设置去年列头
			for(int i=1;i<=12;i++){
				ExcelUtil.setValue(sheet, 1, 5+i, preYy+"年"+i+"月实际销售数量", null);
			}
			
			// 获取当前年份1月至当前月份之间的标题的样式
			//获取第2行第19列的样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 1, 18);
			//获取第3行第19列的样式
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 2, 18);
			//获取第4行第19列的样式
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 3, 18);
			
			// 上月库存样式样式
			//获取第2行第20列的样式
			CellStyle currStyle1 = ExcelUtil.getCellStyle(sheet, 1, 19);
			//获取第3行第20列的样式
			CellStyle currStyle2 = ExcelUtil.getCellStyle(sheet, 2, 19);
			//获取第4行第20列的样式
			CellStyle currStyle3 = ExcelUtil.getCellStyle(sheet, 3, 19);
			
			// 本月生产交货计划
			//获取第2行第21列的样式
			CellStyle currStyle11 = ExcelUtil.getCellStyle(sheet, 1, 20);
			//获取第3行第21列的样式
			CellStyle currStyle21 = ExcelUtil.getCellStyle(sheet, 2, 20);
			//获取第4行第21列的样式
			CellStyle currStyle31 = ExcelUtil.getCellStyle(sheet, 3, 20);
			
			// 第25列样式
			CellStyle currActStyle1 = ExcelUtil.getCellStyle(sheet, 1, 24);
			CellStyle currActStyle2 = ExcelUtil.getCellStyle(sheet, 2, 24);
			CellStyle currActStyle3 = ExcelUtil.getCellStyle(sheet, 3, 24);
			
			// 获取后续月份的样式
			CellStyle nextPdcStyle1 = ExcelUtil.getCellStyle(sheet, 1, 30);
			CellStyle nextPdcStyle2 = ExcelUtil.getCellStyle(sheet, 2, 30);
			CellStyle nextPdcStyle3 = ExcelUtil.getCellStyle(sheet, 3, 30);

			CellStyle nextSaleStyle1 = ExcelUtil.getCellStyle(sheet, 1, 31);
			CellStyle nextSaleStyle2 = ExcelUtil.getCellStyle(sheet, 2, 31);
			CellStyle nextSaleStyle3 = ExcelUtil.getCellStyle(sheet, 3, 31);

			CellStyle preStockStyle1 = ExcelUtil.getCellStyle(sheet, 1, 32);
			CellStyle preStockStyle2 = ExcelUtil.getCellStyle(sheet, 2, 32);
			CellStyle preStockStyle3 = ExcelUtil.getCellStyle(sheet, 3, 32);

			CellStyle zzStyle1 = ExcelUtil.getCellStyle(sheet, 1, 33);
			CellStyle zzStyle2 = ExcelUtil.getCellStyle(sheet, 2, 33);
			CellStyle zzStyle3 = ExcelUtil.getCellStyle(sheet, 3, 33);
			
			// 获取列宽
			int columnWidth1 = sheet.getColumnWidth(18); // 11.7
			int columnWidth2 = sheet.getColumnWidth(19); // 17.5
			int columnWidth3 = sheet.getColumnWidth(32); // 9.2
			int columnWidth4 = sheet.getColumnWidth(33); // 7.5
			
			
			String currY = currYy.substring(currYy.length()-2,currYy.length());
			
			// 設置今年本月以前的銷售
			int startCol1 = 17;
			for(int i=1;i<=currM-1;i++){
				ExcelUtil.setValue(sheet, 1, startCol1+i, currY+"年"+i+"月实际销售数量", cellStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+i, null, cellStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+i, null, cellStyle3);
				// 设置列宽
				sheet.setColumnWidth(startCol1+i, columnWidth1);
			}

			startCol1 = startCol1+currM-1;
			
			int xx=1;
			// 上个月月底库存等
			if(currM == 1){
				ExcelUtil.setValue(sheet, 1, startCol1+xx, preYy+"年12月底全国库存", currStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			}else{
				ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+(currM-1)+"月底全国库存", currStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			}
			// 设置列宽
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			// 设置当前月份数据
			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月生产/交货计划", currStyle11);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle21);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle31);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, "实际生产", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "预计生产", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, "达成率", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"销售预测", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月实际销售数量", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth1);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "达成率", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "本月预计销售", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月底全国库存", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "周转天数", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth4);
			
			// 后续列起始
			int nextMCol = startCol1+xx;
			int startCol2 = startCol1+xx;
			
			for(int i=1;i<=12-currM;i++){
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+1, currY+"年"+(currM+i)+"月生产/交货计划", nextPdcStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+1, null, nextPdcStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+1, null, nextPdcStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+1, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+2, currY+"年"+(currM+i)+"月销售预测", nextSaleStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+2, null, nextSaleStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+2, null, nextSaleStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+2, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+3, currY+"年"+(currM+i)+"月底全国库存", preStockStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+3, null, preStockStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+3, null, preStockStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+3, columnWidth3);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+4, "周转天数", zzStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+4, null, zzStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+4, null, zzStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+4, columnWidth4);
			}
			
			startCol2 = startCol2+(12-currM)*4;
			
			for(int i=1;i<=currM;i++){
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+1, nextY+"年"+i+"月生产/交货计划", nextPdcStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+1, null, nextPdcStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+1, null, nextPdcStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+1, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+2, nextY+"年"+i+"月销售预测", nextSaleStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+2, null, nextSaleStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+2, null, nextSaleStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+2, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+3, nextY+"年"+i+"月底全国库存", preStockStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+3, null, preStockStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+3, null, preStockStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+3, columnWidth3);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+4, "周转天数", zzStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+4, null, zzStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+4, null, zzStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+4, columnWidth4);
			}
			//------------- 暂时添加物料编码------------------
			/*ExcelUtil.setValue(sheet, 1, startCol2+(currM-1)*4+5, "物料编码", zzStyle1);
			ExcelUtil.setValue(sheet, 2, startCol2+(currM-1)*4+5, null, zzStyle2);
			ExcelUtil.setValue(sheet, 3, startCol2+(currM-1)*4+5, null, zzStyle3);
			sheet.setColumnWidth(startCol2+(currM-1)*4+5, columnWidth4);*/
			
			// 统计行字体处理
			Font sumFont = wb.createFont();
			sumFont.setFontName("微软雅黑");    
			sumFont.setFontHeightInPoints((short) 10);//设置字体大小
			sumFont.setBold(true);
			
			// 获取导出数据
			List<Psi> psiList = padPlanService.getPsiInfoByMap(paramMap);
			int listSize = psiList.size();
			if(listSize==0) {
				return ;
			}
			// 获取导出大品项统计
			List<Psi> sumList = padPlanService.getPsiSumByMap(paramMap);
			int sumListSize = sumList.size();
			if(sumListSize==0) {
				return ;
			}
			//******* 数据填充  ******
			
			// 插入行数,listSize:数据行数（模板中自带一行），sumListSize：小计行数
			ExcelUtil.insertRow(sheet, 2, listSize-1+sumListSize);
			int startRow = 2;
			
			Class<? extends Psi> psiClass = Psi.class;
			// 调用get方法
			Method method;
			
			// 汇总数据存储
			HashMap<Integer,Object> sumMap = new HashMap<Integer,Object>();
			// 周转列保存
			List<Integer> turnColList = new ArrayList<Integer>();
			DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance();
			
			// 大品项统计起始
			int sumIndex = 0;
			// 汇总行所在行下标，初始
			int sumC = sumList.get(0).getSumnum();
			
			// 设置大品项统计行的样式
			// 系列
			CellStyle sumCellStyle0 = wb.createCellStyle();
			CellStyle sumStyle0 = ExcelUtil.getCellStyle(sheet, 2, 2);
			sumCellStyle0.cloneStyleFrom(sumStyle0);
			sumCellStyle0.setFont(sumFont);
			// 简称
			CellStyle sumCellStyle1 = wb.createCellStyle();
			CellStyle sumStyle1 = ExcelUtil.getCellStyle(sheet, 2, 4);
			sumCellStyle1.cloneStyleFrom(sumStyle1);
			sumCellStyle1.setFont(sumFont);
			// 三个月平均
			CellStyle sumCellStyle2 = wb.createCellStyle();
			CellStyle sumStyle2 = ExcelUtil.getCellStyle(sheet, 2, 5);
			sumCellStyle2.cloneStyleFrom(sumStyle2);
			sumCellStyle2.setFont(sumFont);
			
			// 设置统计行样式
			Map<Integer,CellStyle> sumStyleMap = new HashMap<Integer,CellStyle>();
			for(int xy=6;xy<=nextMCol+11*4+4;xy++){
				CellStyle sumCellStyle = wb.createCellStyle();
				CellStyle sumStyle = ExcelUtil.getCellStyle(sheet, 2, xy);
				sumCellStyle.cloneStyleFrom(sumStyle);
				sumCellStyle.setFont(sumFont);
				
				sumStyleMap.put(xy, sumCellStyle);
			}
			
			for(int i=0;i<listSize+sumListSize;i++){
				
				//*** 大品项汇总小计行添加  ***
				if(sumC == i){
					// 需要添加大品项汇总行
					Psi sumItem = sumList.get(sumIndex);
					ExcelUtil.setValue(sheet, startRow+i, 2, sumItem.getProdSeriesCode(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 3, sumItem.getBigItemExpl(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 4, "小计", sumCellStyle1);

					ExcelUtil.setValue(sheet, startRow+i, 5, sumItem.getThreeAvgSales(), sumCellStyle2);
					
					// 大品项汇总行处理
					SumItemDeal(sheet,startRow,sumItem,i,startCol1,nextMCol,currM);
					
					// 设置统计行样式
					for(int xy=5;xy<=nextMCol+11*4+4;xy++){
						ExcelUtil.setCellStyle(sheet, startRow+i, xy, sumStyleMap.get(xy));
					}
					
					// 处理下次统计的行信息
					if(sumIndex<sumListSize-1){
						// 下一个汇总行
						sumIndex = sumIndex+1;
						// 最后行下标计算完成，不需要再计算下个汇总行下标
						// 下一个大品项统计行下标
						int itemSumCount = sumList.get(sumIndex).getSumnum();
						sumC += itemSumCount;
						sumC += 1;
					}
					
					continue;
				}
				
				Psi item = psiList.get(i-sumIndex);// 如有汇总行，则 sumIndex 已加一
				ExcelUtil.setValue(sheet, startRow+i, 0, item.getRank(), null);
				ExcelUtil.setValue(sheet, startRow+i, 1, item.getProduExpl(), null);//产能划分
				ExcelUtil.setValue(sheet, startRow+i, 2, item.getProdSeriesCode(), null);
				ExcelUtil.setValue(sheet, startRow+i, 3, item.getBigItemExpl(), null);
				ExcelUtil.setValue(sheet, startRow+i, 4, item.getMatShort(), null);
				ExcelUtil.setValue(sheet, startRow+i, 5, item.getThreeAvgSales(), null);
				
				int ssIndex = 5;
				
				// 计算列总计
				int key = 5;
				float val = item.getThreeAvgSales();
				sumMap = summaryCalc(sumMap,key,val);
				
				try {
					// 去年一整年的实际销售
					for(int z=1;z<=12;z++){
							method = psiClass.getMethod("getPreY" + z +"mSales");
							// 得到值
							Object invoke = method.invoke(item);
							// 计算统计值
							key = ssIndex+z;
							val = Float.parseFloat(invoke.toString());
							sumMap = summaryCalc(sumMap,key,val);
							
							ExcelUtil.setValue(sheet, startRow+i, ssIndex+z, invoke, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 今年本月之前月份的实际销售
				int sIndex = ssIndex+12;
				try {
					for(int z=1;z<=currM-1;z++){
							method = psiClass.getMethod("getCurrY" + z +"mSales");
							// 得到值
							Object invoke = method.invoke(item);
							
							// 计算统计值
							key = (sIndex+z);
							val = Float.parseFloat(invoke.toString());
							sumMap = summaryCalc(sumMap,key,val);
							
							ExcelUtil.setValue(sheet, startRow+i, sIndex+z, invoke, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 上个月的全国库存
				ExcelUtil.setValue(sheet, startRow+i, startCol1+1, item.getNationStock2(), null);
				// 计算统计值
				key = startCol1+1;
				val = item.getNationStock2();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 生产交货计划数量
				ExcelUtil.setValue(sheet, startRow+i, startCol1+2, item.getPadPlanQty(), null);
				// 计算统计值
				key = startCol1+2;
				val = item.getPadPlanQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 实际生产
				ExcelUtil.setValue(sheet, startRow+i, startCol1+3, item.getPadActQty(), null);
				// 计算统计值
				key = startCol1+3;
				val = item.getPadActQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 预计生产
				ExcelUtil.setValue(sheet, startRow+i, startCol1+4, item.getEstDeliQty(), null);
				// 计算统计值
				key = startCol1+4;
				val = item.getEstDeliQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 达成率
				if(item.getPadPlanQty() != 0){
					float calcNum = (item.getPadActQty()/item.getPadPlanQty());
					String per = df.format(calcNum);
					
					ExcelUtil.setValue(sheet, startRow+i, startCol1+5, per, null);
				}else{
					ExcelUtil.setValue(sheet, startRow+i, startCol1+5, "-", null);
				}
				
				// 本月销售预测
				ExcelUtil.setValue(sheet, startRow+i, startCol1+6, item.getSaleForeQty(), null);
				// 计算统计值
				key = startCol1+6;
				val = item.getSaleForeQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 实际销售
				ExcelUtil.setValue(sheet, startRow+i, startCol1+7, item.getSaleForeActQty(), null);
				// 计算统计值
				key = startCol1+7;
				val = item.getSaleForeActQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 达成率
				if(item.getSaleForeQty() != 0){
					float calcNum = (item.getSaleForeActQty()/item.getSaleForeQty());
					String per = df.format(calcNum);
					
					ExcelUtil.setValue(sheet, startRow+i, startCol1+8, per, null);
				}else{
					ExcelUtil.setValue(sheet, startRow+i, startCol1+8, "-", null);
				}
				// 本月预计销售
				ExcelUtil.setValue(sheet, startRow+i, startCol1+9, item.getEstSaleQty(), null);
				// 计算统计值
				key = startCol1+9;
				val = item.getEstSaleQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 全国库存
				ExcelUtil.setValue(sheet, startRow+i, startCol1+10, item.getNationStock1(), null);
				// 计算统计值
				key = startCol1+10;
				val = item.getNationStock1();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 周转天数
				ExcelUtil.setValue(sheet, startRow+i, startCol1+11, item.getTurnOverDays(), null);
				// 计算统计值
				key = startCol1+11;
				// 记录周转天数列
				turnColList.add(key);
				
				//----------------------- 后续月份数据填充 ------------------------------
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1, item.getNext1Plan(), null);
				// 计算统计值
				key = nextMCol+1;
				val = item.getNext1Plan();
				sumMap = summaryCalc(sumMap,key,val);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2, item.getNext1SalesF(), null);
				// 计算统计值
				key = nextMCol+2;
				val = item.getNext1SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				float stock1 = item.getNationStock1()+(item.getNext1Plan()==null?0:item.getNext1Plan())
						-(item.getNext1SalesF()==null?0:item.getNext1SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3, stock1, null);
				// 计算统计值
				key = nextMCol+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				
				// 周转天数
				float tod1 = 0;
				float nextSalesF = item.getNext1SalesF()==null?0:item.getNext1SalesF();
				if(nextSalesF==0){
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+4;
				// 记录周转天数列
				turnColList.add(key);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+1, item.getNext2Plan(), null);
				// 计算统计值
				key = nextMCol+1*4+1;
				val = item.getNext2Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+2, item.getNext2SalesF(), null);
				// 计算统计值
				key = nextMCol+1*4+2;
				val = item.getNext2SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext2Plan()==null?0:item.getNext2Plan())
						-(item.getNext2SalesF()==null?0:item.getNext2SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+1*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext2SalesF()==null?0:item.getNext2SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+1*4+4;
				// 记录周转天数列
				turnColList.add(key);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+1, item.getNext3Plan(), null);
				// 计算统计值
				key = nextMCol+2*4+1;
				val = item.getNext3Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+2, item.getNext3SalesF(), null);
				// 计算统计值
				key = nextMCol+2*4+2;
				val = item.getNext3SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext3Plan()==null?0:item.getNext3Plan())
						-(item.getNext3SalesF()==null?0:item.getNext3SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+2*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext3SalesF()==null?0:item.getNext3SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+2*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+1, item.getNext4Plan(), null);
				// 计算统计值
				key = nextMCol+3*4+1;
				val = item.getNext4Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+2, item.getNext4SalesF(), null);
				// 计算统计值
				key = nextMCol+3*4+2;
				val = item.getNext4SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext4Plan()==null?0:item.getNext4Plan())
						-(item.getNext4SalesF()==null?0:item.getNext4SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+3*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext4SalesF()==null?0:item.getNext4SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+3*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+1, item.getNext5Plan(), null);
				// 计算统计值
				key = nextMCol+4*4+1;
				val = item.getNext5Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+2, item.getNext5SalesF(), null);
				// 计算统计值
				key = nextMCol+4*4+2;
				val = item.getNext5SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext5Plan()==null?0:item.getNext5Plan())
						-(item.getNext5SalesF()==null?0:item.getNext5SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+4*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext5SalesF()==null?0:item.getNext5SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+4*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+1, item.getNext6Plan(), null);
				// 计算统计值
				key = nextMCol+5*4+1;
				val = item.getNext6Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+2, item.getNext6SalesF(), null);
				// 计算统计值
				key = nextMCol+5*4+2;
				val = item.getNext6SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext6Plan()==null?0:item.getNext6Plan())
						-(item.getNext6SalesF()==null?0:item.getNext6SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+5*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext6SalesF()==null?0:item.getNext6SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+5*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+1, item.getNext7Plan(), null);
				// 计算统计值
				key = nextMCol+6*4+1;
				val = item.getNext7Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+2, item.getNext7SalesF(), null);
				// 计算统计值
				key = nextMCol+6*4+2;
				val = item.getNext7SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext7Plan()==null?0:item.getNext7Plan())
						-(item.getNext7SalesF()==null?0:item.getNext7SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+6*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext7SalesF()==null?0:item.getNext7SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+6*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+1, item.getNext8Plan(), null);
				// 计算统计值
				key = nextMCol+7*4+1;
				val = item.getNext8Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+2, item.getNext8SalesF(), null);
				// 计算统计值
				key = nextMCol+7*4+2;
				val = item.getNext8SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext8Plan()==null?0:item.getNext8Plan())
						-(item.getNext8SalesF()==null?0:item.getNext8SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+7*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext8SalesF()==null?0:item.getNext8SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+7*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+1, item.getNext9Plan(), null);
				// 计算统计值
				key = nextMCol+8*4+1;
				val = item.getNext9Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+2, item.getNext9SalesF(), null);
				// 计算统计值
				key = nextMCol+8*4+2;
				val = item.getNext9SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext9Plan()==null?0:item.getNext9Plan())
						-(item.getNext9SalesF()==null?0:item.getNext9SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+8*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext9SalesF()==null?0:item.getNext9SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+8*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+1, item.getNext10Plan(), null);
				// 计算统计值
				key = nextMCol+9*4+1;
				val = item.getNext10Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+2, item.getNext10SalesF(), null);
				// 计算统计值
				key = nextMCol+9*4+2;
				val = item.getNext10SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext10Plan()==null?0:item.getNext10Plan())
						-(item.getNext10SalesF()==null?0:item.getNext10SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+9*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext10SalesF()==null?0:item.getNext10SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+9*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+1, item.getNext11Plan(), null);
				// 计算统计值
				key = nextMCol+10*4+1;
				val = item.getNext11Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+2, item.getNext11SalesF(), null);
				// 计算统计值
				key = nextMCol+10*4+2;
				val = item.getNext11SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext11Plan()==null?0:item.getNext11Plan())
						-(item.getNext11SalesF()==null?0:item.getNext11SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+10*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext11SalesF()==null?0:item.getNext11SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+10*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+1, item.getNext12Plan(), null);
				// 计算统计值
				key = nextMCol+11*4+1;
				val = item.getNext12Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+2, item.getNext12SalesF(), null);
				// 计算统计值
				key = nextMCol+11*4+2;
				val = item.getNext12SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext12Plan()==null?0:item.getNext12Plan())
						-(item.getNext12SalesF()==null?0:item.getNext12SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+11*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext12SalesF()==null?0:item.getNext12SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+11*4+4;
				// 记录周转天数列
				turnColList.add(key);

				// ---------------------物料编码赋值-----------------------
				//ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+5, item.getMatCode(), null);
			}
			
			// 周转天数处理
			for(int i=0;i<turnColList.size();i++){
				int colIx = turnColList.get(i);
				float stock = Float.parseFloat(sumMap.get(colIx-1).toString());
				float sale = Float.parseFloat(sumMap.get(colIx-2).toString());
				if(sale != 0){
					sumMap.put(colIx, Math.round((stock/sale)*30));
				}else{
					sumMap.put(colIx, 0);
				}
			}
			
			// 统计行赋值
			Iterator iter = sumMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int key = Integer.parseInt(entry.getKey().toString());
				Object val = entry.getValue();
				
				ExcelUtil.setValue(sheet, startRow+listSize+sumListSize, key, val, null);
			}
			
			/*// 添加合计的公式
			int cellNumSum = nextMCol+11*4+4;
			// 行
			int rowSum = listSize+2;
			// 设置计算的第一列公式
			ExcelUtil.setFormula(sheet, rowSum, 2, "sum(C3:C"+rowSum+")", null);
			for(int i=3;i<=cellNumSum;i++){
				if(i == startCol1+4 ||i == startCol1+7){
					continue;
				}
				ExcelUtil.setFormula(sheet, rowSum, i, ExcelUtil.getCellFormula(sheet, rowSum, 2), null);
			}*/
			
			// 导出excel
			OutputStream out = response.getOutputStream();
			ExcelUtil.exportExcel(wb, out);
			
			// 上传至ftp
			//uploadExcelFile(wb,fileName+".xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 大品项汇总行处理
	 * @param sheet
	 * @param startRow
	 * @param item
	 * @param i
	 * @param startCol1
	 * @param nextMCol
	 * @param currM
	 */
	private void SumItemDeal(Sheet sheet,int startRow,Psi item,int i,int startCol1,int nextMCol,int currM){
		int ssIndex = 5;

		Class<? extends Psi> psiClass = Psi.class;
		// 调用get方法
		Method method;
		DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance();
		try {
			// 去年一整年的实际销售
			for(int z=1;z<=12;z++){
					method = psiClass.getMethod("getPreY" + z +"mSales");
					// 得到值
					Object invoke = method.invoke(item);
					
					ExcelUtil.setValue(sheet, startRow+i, ssIndex+z, invoke, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 今年本月之前月份的实际销售
		int sIndex = ssIndex+12;
		try {
			for(int z=1;z<=currM-1;z++){
					method = psiClass.getMethod("getCurrY" + z +"mSales");
					// 得到值
					Object invoke = method.invoke(item);
					
					ExcelUtil.setValue(sheet, startRow+i, sIndex+z, invoke, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 上个月的全国库存
		ExcelUtil.setValue(sheet, startRow+i, startCol1+1, item.getNationStock2(), null);
		
		// 生产交货计划数量
		ExcelUtil.setValue(sheet, startRow+i, startCol1+2, item.getPadPlanQty(), null);
		
		// 实际生产
		ExcelUtil.setValue(sheet, startRow+i, startCol1+3, item.getPadActQty(), null);
		
		// 预计生产
		ExcelUtil.setValue(sheet, startRow+i, startCol1+4, item.getEstDeliQty(), null);
		
		// 达成率
		if(item.getPadPlanQty() != 0){
			float calcNum = (item.getPadActQty()/item.getPadPlanQty());
			String per = df.format(calcNum);
			
			ExcelUtil.setValue(sheet, startRow+i, startCol1+5, per, null);
		}else{
			ExcelUtil.setValue(sheet, startRow+i, startCol1+5, "-", null);
		}
		
		// 本月销售预测
		ExcelUtil.setValue(sheet, startRow+i, startCol1+6, item.getSaleForeQty(), null);
		
		// 实际销售
		ExcelUtil.setValue(sheet, startRow+i, startCol1+7, item.getSaleForeActQty(), null);
		
		// 达成率
		if(item.getSaleForeQty() != 0){
			float calcNum = (item.getSaleForeActQty()/item.getSaleForeQty());
			String per = df.format(calcNum);
			
			ExcelUtil.setValue(sheet, startRow+i, startCol1+8, per, null);
		}else{
			ExcelUtil.setValue(sheet, startRow+i, startCol1+8, "-", null);
		}
		// 本月预计销售
		ExcelUtil.setValue(sheet, startRow+i, startCol1+9, item.getEstSaleQty(), null);
		
		// 全国库存
		ExcelUtil.setValue(sheet, startRow+i, startCol1+10, item.getNationStock1(), null);
		
		// 周转天数
		ExcelUtil.setValue(sheet, startRow+i, startCol1+11, item.getTurnOverDays(), null);
		
		//----------------------- 后续月份数据填充 ------------------------------
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1, item.getNext1Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2, item.getNext1SalesF(), null);
		// 全国库存
		float stock1 = item.getNationStock1()+(item.getNext1Plan()==null?0:item.getNext1Plan())
				-(item.getNext1SalesF()==null?0:item.getNext1SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3, stock1, null);
		// 周转天数
		float tod1 = 0;
		float nextSalesF = item.getNext1SalesF()==null?0:item.getNext1SalesF();
		if(nextSalesF==0){
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, tod1, null);
		}
		
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+1, item.getNext2Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+2, item.getNext2SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext2Plan()==null?0:item.getNext2Plan())
				-(item.getNext2SalesF()==null?0:item.getNext2SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext2SalesF()==null?0:item.getNext2SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, tod1, null);
		}
		
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+1, item.getNext3Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+2, item.getNext3SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext3Plan()==null?0:item.getNext3Plan())
				-(item.getNext3SalesF()==null?0:item.getNext3SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext3SalesF()==null?0:item.getNext3SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+1, item.getNext4Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+2, item.getNext4SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext4Plan()==null?0:item.getNext4Plan())
				-(item.getNext4SalesF()==null?0:item.getNext4SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext4SalesF()==null?0:item.getNext4SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+1, item.getNext5Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+2, item.getNext5SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext5Plan()==null?0:item.getNext5Plan())
				-(item.getNext5SalesF()==null?0:item.getNext5SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext5SalesF()==null?0:item.getNext5SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+1, item.getNext6Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+2, item.getNext6SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext6Plan()==null?0:item.getNext6Plan())
				-(item.getNext6SalesF()==null?0:item.getNext6SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext6SalesF()==null?0:item.getNext6SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+1, item.getNext7Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+2, item.getNext7SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext7Plan()==null?0:item.getNext7Plan())
				-(item.getNext7SalesF()==null?0:item.getNext7SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext7SalesF()==null?0:item.getNext7SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+1, item.getNext8Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+2, item.getNext8SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext8Plan()==null?0:item.getNext8Plan())
				-(item.getNext8SalesF()==null?0:item.getNext8SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext8SalesF()==null?0:item.getNext8SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+1, item.getNext9Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+2, item.getNext9SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext9Plan()==null?0:item.getNext9Plan())
				-(item.getNext9SalesF()==null?0:item.getNext9SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext9SalesF()==null?0:item.getNext9SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+1, item.getNext10Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+2, item.getNext10SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext10Plan()==null?0:item.getNext10Plan())
				-(item.getNext10SalesF()==null?0:item.getNext10SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext10SalesF()==null?0:item.getNext10SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+1, item.getNext11Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+2, item.getNext11SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext11Plan()==null?0:item.getNext11Plan())
				-(item.getNext11SalesF()==null?0:item.getNext11SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext11SalesF()==null?0:item.getNext11SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+1, item.getNext12Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+2, item.getNext12SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext12Plan()==null?0:item.getNext12Plan())
				-(item.getNext12SalesF()==null?0:item.getNext12SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext12SalesF()==null?0:item.getNext12SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, tod1, null);
		}
	}
	
	
	/**
	 * 上传导出的excel
	 * @param wb
	 * @param fileName
	 * @return
	 */
	private String uploadExcelFile(Workbook wb,String fileName){
        // 保存至进销存报表目录中
		String direCode = "JXCBB";
        Directory dire = directoryService.getDireByCode(direCode);
		if(dire==null){
			return "请选择正确的文件路径编码！"; 
		}

		boolean uploadFlag=true;
		try {
			// 获取workbook的inputstream
	        InputStream is = ExcelUtil.getInputStreamData(wb);
			String newName=UUIDUtil.getUUID()+fileName;
			String realPath=ftpBasePath.concat(dire.getDireFcode());
			
			uploadFlag = FtpUtil.uploadFile(realPath, newName, is);

			List<Document> docs=new ArrayList<Document>();
			if(uploadFlag){
				Document doc = new Document();
				doc.setId(UUIDUtil.getUUID());
				doc.setDireCode(direCode);
				doc.setFileUrl(realPath);
				doc.setFileName(newName);
				doc.setRealName(fileName);
				
				String currYm = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
				doc.setLinkNo(currYm);
				//doc.setLinkId(linkId);
				//doc.setDocCate(docCate);
				doc.setCreateUser(UserCommon.getUser().getUsername());
				doc.setCreateTime(new Date());
				
				//doc.setDocSize(fc.size()/1024+"Kb");
				String[] split = fileName.split(".");
				if(split.length>0){
					doc.setDocType(split[split.length-1]);
				}
				docs.add(doc);
				
				documentService.saveDocs(docs);
			}
		}catch (Exception e) {
			return e.getMessage();
		}
		return "";
	}
	
	/**
	 * 获取上个年月的物料数据
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPreYmMat")
	public String getPreYmMat(String year,String month,String mainId){
		
		int mYear = Integer.parseInt(year);
		int mMonth = Integer.parseInt(month);
		
		String preYm = "";
		// 上个年月
		if(mMonth == 1){
			preYm = (mYear-1)+"-12";
		}else{
			preYm = year+"-"+(mMonth<11?"0"+(mMonth-1):(mMonth-1));
		}
		
		// 获取上个年月的数据
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("planMonth", preYm);
		List<PadPlanDetail> detailList = padPlanService.getPadPlanDetailByMap(pMap);
		
		if(detailList != null && detailList.size()>0){
			
			// 获取激活状态下的销售预测的字段
			Map<String, Object> sMap = new HashMap<String, Object>();
			sMap.put("status", "激活");
			List<SaleForecast> saleList = saleFcstService.getSaleFcstByCondition(sMap);
			
			String saleId = "";
			String columnName = "'0'";
			String nextColumnName = "'0'";
			if(saleList.size()>0){
				SaleForecast saleFore = saleList.get(0);
				saleId = saleFore.getId();
				// 预测期间
				String saleYm = saleFore.getFsctYear();
				String[] yearArr = saleYm.split("~");
				String sYm = yearArr[0].trim();
				
				String[] sYmArr = sYm.split("-");
				String sYear = sYmArr[0];
				String sMonth = sYmArr[1];
				int sY = Integer.parseInt(sYear);
				int sM = Integer.parseInt(sMonth);
				
				// 计算与初始的年月相差月份
				int qty = (mYear-sY)*12+mMonth-sM;
				if(qty>=0 && qty<24){
					if(qty>=12){
						columnName = "sale_Fore"+(qty-11);
						if(qty-10>12){
							// 当前月份为最后一月
							nextColumnName = "sale_Fore1";
						}else{
							nextColumnName = "sale_Fore"+(qty-10);
						}
					}else{
						columnName = "sale_Fore_Qty"+(qty+1);
						if(qty+2>12){
							nextColumnName = "sale_Fore1";
						}else{
							nextColumnName = "sale_Fore_Qty"+(qty+2);
						}
					}
				}else{
					columnName = "'0'";
					nextColumnName = "'0'";
				}
			}

			// 获取物料的临时数据，取上个月计划的下月预测
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", detailList);
			map.put("preYm", preYm);
			map.put("mainId", mainId);
			
			map.put("saleId", saleId);
			map.put("columnName", columnName);
			map.put("nextColumnName", nextColumnName);
			List<PadPlanDetail> tempList = padPlanService.getPadPlanTempDetailByMap(map);
			
			// =-------分为当前月与未来月
			// 当前年月
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
			String[] currYmArr = currDate.split("-");
			String cYear = currYmArr[0];
			String cMonth = currYmArr[1];
			int cY = Integer.parseInt(cYear);
			int cM = Integer.parseInt(cMonth);
			
			boolean isFuture = false;
			if(cY<mYear){
				// 未来月处理
				isFuture = true;
			}else if(cY==mYear){
				if(cM>=mMonth){
					// 取中间表中的数据
					isFuture = false;
				}else{
					// 未来月
					isFuture = true;
				}
			}else{
				// 取中间表中的数据
				isFuture = false;
			}
			
			if(isFuture){
				// 未来月
				return JSON.toJSONString(tempList);
			}else{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("mainId", mainId);
				paramMap.put("year", year);
				paramMap.put("month", month);
				paramMap.put("preYear", mYear-1);
				if(mMonth == 12){
					paramMap.put("preYearT", year);
					paramMap.put("nextMonth", "01");
				}else{
					paramMap.put("preYearT", String.valueOf(mYear-1));
					String nextMonth = String.valueOf(mMonth+1);
					paramMap.put("nextMonth", nextMonth.length()==1?"0"+nextMonth:nextMonth);
				}
				paramMap.put("list", tempList);
				// 当前月及以前
				return JSON.toJSONString(matInfoService.getPadPlanMatInfo(paramMap));
			}
		}else{
			return "[]";
		}
	}
	
	/**
	 * 物料筛选
	 * @return
	 */
	@RequestMapping("/matRepeat")
	public String matRepeat(){
		return "bam/padPlan/matRepeat";
	}
	
	/**
	 * 获取订单中存在编辑的系列
	 * @param orderCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getEditingSeries")
	public RestCode getEditingSeries(String orderCode){
		RestCode rc = new RestCode();
		boolean rs = redisClient.exists(orderCode);
		if(rs){

			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			String userCode = user.getUsername();
			// 如果是当前编辑人员
			String users = redisClient.get(orderCode);
			String uus = redisClient.get(orderCode);
			// 编辑中人员对应的系列
			String seriesData = "";
			
			if(users.equals(userCode)){
				// 只有当前人员
				rc.put("code", 10);
			}else if(users.equals("")){
				// 不存在人员
				rc.put("code", 50);
			}else{
				// 获取编辑人员对应的系列，除去自己的部分
				if((","+users+",").contains(","+userCode+",")){
					users = (","+users+",").replace(","+userCode+",", ",");
				}
				users = "'"+users.replace(",", "','")+"'";

				// 获取编辑中用户对应的系列
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("users", users);
				List<UserSeries> userSeriesList = configService.getUserSeriesData(map);
				if(userSeriesList != null && userSeriesList.size()>0){
					for(int i=0;i<userSeriesList.size();i++){
						UserSeries item = userSeriesList.get(i);
						seriesData += "已存在用户"+item.getUserName()+"编辑 	，编辑系列为："+item.getSeriesExpl()+";<br>";
					}
				}else{
					String[] useArr = uus.split(",");
					String names = "";
					if(useArr.length>0){
						for(int i=0;i<useArr.length;i++){
							SysUserDO ser = UserCommon.getUserByUsername(useArr[i]);
							if(ser != null){
								names += ser.getName()+",";
							}
						}
					}
					if(!names.equals("")){
						names = names.substring(0, names.length()-1);
						seriesData = "已存在用户"+ names +"正在编辑！";
					}else{
						rc.put("code", 50);
						return rc;
					}
				}
				
				// 存在系列
				rc.put("code", 20);
				rc.put("msg", seriesData==null?"":seriesData);
			}
		}else{
			// 不存在
			rc.put("code", 30);
		}
		return rc;
	}
	
	/**
	 * 删除计划编码
	 * @param orderCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delPlanCode")
	public RestCode delPlanCode(String orderCode){

		RestCode rc = new RestCode();
		try {

			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			String userCode = user.getUsername();
			
			dealPlanCodeCache(userCode,orderCode);
			
			rc.put("code", 0);
		} catch (Exception e) {
			rc.put("code", -1);
		}
		return rc;
	}
	
	/**
	 * 处理点点编码缓存
	 * @param userCode
	 * @param orderCode
	 */
	public void dealPlanCodeCache(String userCode,String orderCode){
		String users = redisClient.get(orderCode);
		if(users.equals(userCode)){
			// 删除key
			redisClient.delKey(orderCode);
		}else{
			users = (","+users+",").replace(","+userCode+",", ",");
			users = users.substring(1, users.length()-1);
			redisClient.set(orderCode, users);
		}
	}
	//-----------------------物料交互计划整年调整--------------------------
	/**
	 * 获取物料整年调整页面
	 * @return
	 */
	@RequestMapping("/page/edit/year")
	public String getYearEditPage(){		
		return "bam/padPlan/yearEdit";
	}
	/**
	 * 获取物料未来一年的产交货计划详情
	 * @param mateCode
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/detail/year")
	public List<PadPlanDetail> getYearPadDetailByMap(String mateCode,String planMonth){
		return padPlanService.getYearPadDetailByMap(mateCode, planMonth);
	}
	/**
	 * 生产交货计划的年度调整
	 * @param jsonStr
	 * @return
	 */
	@RequestMapping("/detail/year/update")
	public RestCode updateYearPadPlanDetail(String jsonStr,List<PadPlanDetail> list){
		List<PadPlanDetail> details=new ArrayList<>();
		System.out.println(jsonStr);
		return new RestCode();
	}
	//-----------------------物料交互计划整年调整--------------------------
	
	
	
	
	//-----------------------批量修改物料--------------------------
	
	/**
	 * 校验生产/交货计划中的被选中物料是否保存
	 * @param planCode
	 * @param matCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkMateIsSave")
	public boolean checkMateIsSave(String planCode,String matCode) {
		
		return padPlanService.checkMateIsSave(planCode,matCode);
	}
	
	/**
	 * 物料批量修改弹出框
	 * @return
	 */
	@RequestMapping("/getBatchUpdateHtml")
	public String getBatchUpdateHtml(Model model){
		return "bam/padPlan/batchUpdate";
	}
	/**
	 * 获取弹窗列表数据
	 * @param matCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPlanMessageListOfMateByMatCode")
	public List<PadMateMess> getPlanMessageListOfMateByMatCode(String matCode) {
		//获取弹窗列表数据
		List<PadMateMess> list = padPlanService.getPlanMessageListOfMateByMatCode(matCode);
		return list;
	}
	/**
	 * 批量修改交货计划值，计算周转天数
	 * @param dataJson
	 * @param matCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/confirmUpdatePadPlanMess")
	public Map<String, Object> confirmUpdatePadPlanMess(String dataJson,String matCode) {
		
		return padPlanService.confirmUpdatePadPlanMess(dataJson,matCode);
	}
	
	//-----------------------批量修改物料--------------------------
	
	/**
	 * 获取生产交货计划记录列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getPadPlanRecordHtml")
	public String getPadPlanRecordHtml(Model model){
		return "bam/padPlan/padPlanRecord";
	}
	
	/**
	 * 获取生产交货计划记录列表数据
	 * @param paForm
	 * @param limit
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPadPlanRecordListByPage")
	public Map<String, Object> getPadPlanRecordListByPage(PadPlanDetailForm paForm,Integer limit,Integer page) {
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("paForm", paForm);
		return padPlanService.getPadPlanRecordListByPage(map);
	}
	/**
	 * 获取生产交货计划的记录列表动态列头信息
	 * @param paForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPadPlanRecordFields")
	public Map<String, Object> getPadPlanRecordFields(PadPlanDetailForm paForm) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("paForm", paForm);
		return padPlanService.getPadPlanRecordFields(map);
	}
	
	
	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportPadPlanMateList")
	public String exportCutMatePackList(String objjson,
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
			//导出excel的文件名称
			String fileName = username + "-生产交货计划记录表-" + time + ".xlsx";
			//查询数据
			Map<String, Object> map = new HashMap<String, Object>();
			PadPlanDetailForm paForm = JsonUtils.jsonToPojo(objjson, PadPlanDetailForm.class);
			map.put("paForm", paForm);
			map = padPlanService.dealWithVersionScope(map);
			List<PadPlanRecord> list = padPlanService.getPadPlanRecordList(map);
			//导出数据
			Workbook wb = ExportExcel.exportPadPlanMateList(list,map, req, res);
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
