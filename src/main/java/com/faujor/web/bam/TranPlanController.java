package com.faujor.web.bam;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.TranPlan;
import com.faujor.entity.bam.psm.TranPlanDetail;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.sapcenter.bam.TranPlanMatDetail;
import com.faujor.service.bam.TranPlanService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.sapcenter.bam.MatInfoService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

/**
 * 调拨计划 控制类
 * @author Vincent
 *
 */
@Controller
@RequestMapping(value = "/bam/tran")
public class TranPlanController {
	
	@Autowired
	private TranPlanService tranPlanService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MatInfoService matInfoService;
	@Autowired
	private CodeService codeService;
	
	/**
	 * 调拨计划 列表
	 * @param model
	 * @return
	 */
	@Log(value = "调拨计划列表信息")
	@RequestMapping("/tranPlanPage")
	public String tranPlanPage(Model model){
		
		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);
		
		return "bam/tranPlan/tranPlanList";
	}
	
	/**
	 * 调拨计划 列表分页查询
	 * @param page
	 * @param search_pdrCode
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @return
	 */
	@Log(value = "获取调拨计划列表信息")
	@ResponseBody
	@RequestMapping("/getTranPlanPageList")
	public LayuiPage<TranPlan> getTranPlanPageList(LayuiPage<TranPlan> page,String search_name,String search_status,
			//String search_tranDateStart,String search_tranDateEnd,
			String search_crtDateStart,String search_crtDateEnd){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("tranName", search_name);
		params.put("status",search_status);
		params.put("tranDateStart","");
		params.put("tranDateEnd","");
		params.put("crtDateStart",search_crtDateStart);
		params.put("crtDateEnd",search_crtDateEnd);
		LayuiPage<TranPlan> pageList = tranPlanService.getTranPlanByPage(params);
		return pageList;
	}
	
	/**
	 * 删除调拨计划
	 * @param id
	 * @return
	 */
	@Log(value = "删除单个调拨计划信息")
	@ResponseBody
	@RequestMapping("/deleteTranPlan")
	public RestCode deleteTranPlan(String id){
		tranPlanService.delTranPlanInfoById(id);
		return new RestCode();
	}
	
	/**
	 * 批量删除调拨计划
	 * @param ids
	 * @return
	 */
	@Log(value = "删除批量调拨计划信息")
	@ResponseBody
	@RequestMapping("/deleteBatchTranPlan")
	public RestCode deleteBatchTranPlan(String ids){
		
		List<String> list = JSON.parseArray(ids, String.class);
		
		tranPlanService.delBatchTranPlanInfoByIds(list);
		return new RestCode();
	}
	
	/**
	 * 调拨计划 明细列表
	 * @param model
	 * @param mainId
	 * @param type 类型： 1：编辑或创建 ，2：查看，3：引用创建
	 * @return
	 */
	@Log(value = "创建/编辑/查看调拨计划明细信息")
	@RequestMapping("/tranPlanDetailPage")
	public String tranPlanDetailPage(Model model,String mainId,String type,String refId){
		
		model.addAttribute("type", type);
		// 引用id
		model.addAttribute("refId", refId);
		
		TranPlan tranPlan = new TranPlan();
		if(mainId == null || mainId.equals("")){
			//******* 创建
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			// 设置创建人
			tranPlan.setCrtUser(user.getName());
			
			// id
			tranPlan.setId(UUIDUtil.getUUID());
			// code
			tranPlan.setTranCode("");
			
			// 状态
			tranPlan.setStatus("");
			
			tranPlan.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		}else{
			//****** 编辑或查看
			tranPlan = tranPlanService.getTranPlanById(mainId);
		}
		
		// 设置主表ID
		model.addAttribute("mainId", tranPlan.getId());
		// 主表信息
		model.addAttribute("list", tranPlan);
		
		return "bam/tranPlan/tranPlanDetail";
	}
	
	/**
	 * 调拨计划 明细列表分页查询
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取调拨计划明细数据")
	@ResponseBody
	@RequestMapping("/getTranPlanDetailPageList")
	public LayuiPage<TranPlanDetail> getTranPlanDetailPageList(LayuiPage<TranPlanDetail> page,String mainId){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("mainId", mainId);
		LayuiPage<TranPlanDetail> pageList = tranPlanService.getTranPlanDetailPage(params);
		return pageList;
	}
	
	/**
	 * 调拨计划 明细数据获取
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取调拨计划明细数据")
	@ResponseBody
	@RequestMapping("/getTranPlanDetailList")
	public List<TranPlanDetail> getTranPlanDetailList(String mainId){
		return tranPlanService.getTranPlanDetailListByMainId(mainId);
	}
	
	/**
	 * 保存 调拨计划  信息
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "保存调拨计划信息")
	@ResponseBody
	@RequestMapping("/saveTranPlanInfo")
	public RestCode saveTranPlanInfo(TranPlan tranPlan,String tranPlanDetailData,String sType){
		
		// 设置编码
		if(tranPlan.getTranCode() == null || tranPlan.getTranCode().equals("")){
			// code , 调拨计划编码规则代码 ：tranPlanNo
			String code = codeService.getCodeByCodeType("tranPlanNo");
			tranPlan.setTranCode(code);
		}
		// 明细数据转成list
		List<TranPlanDetail> detailList = JSON.parseArray(tranPlanDetailData, TranPlanDetail.class);
		for(int i=0;i<detailList.size();i++){
			TranPlanDetail item = detailList.get(i);
			
			// 设置明细ID
			item.setId(UUIDUtil.getUUID());
			
			item.setMonQty(item.getMonQty()==null?0:item.getMonQty());
			item.setTueQty(item.getTueQty()==null?0:item.getTueQty());
			item.setWedQty(item.getWedQty()==null?0:item.getWedQty());
			item.setThuQty(item.getThuQty()==null?0:item.getThuQty());
			item.setFriQty(item.getFriQty()==null?0:item.getFriQty());
			item.setSatQty(item.getSatQty()==null?0:item.getSatQty());
			item.setSunQty(item.getSunQty()==null?0:item.getSunQty());
			item.setEstDevQty(item.getEstDevQty()==null?0:item.getEstDevQty());
		}
		
		//sType  sb：提交  sv：保存
		if(sType.equals("sb")){
			// 提交
			tranPlan.setStatus("已提交");
		}
		
		tranPlanService.saveTranPlanInfo(tranPlan, detailList);
		
		RestCode restCode = new RestCode();
		
		return restCode;
	}
	
	/**
	 * 提交
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "提交调拨计划信息")
	@ResponseBody
	@RequestMapping("/submitTranPlan")
	public RestCode submitTranPlan(String mainId){
		
		String status = "已提交";
		tranPlanService.updateTranPlanStatus(status, mainId);
		
		return new RestCode();
	}
	
	/**
	 * 物料选择弹出框
	 * @return
	 */
	@RequestMapping("/matSelectDialog")
	public String matSelectDialog(){
		return "bam/tranPlan/matSelectDialog";
	}
	
	/**
	 * 根据条件获取 调拨任务 物料信息
	 * @param map
	 * @return
	 */
	@Log(value = "获取调拨计划物料数据")
	@ResponseBody
	@RequestMapping("/getMatExtraInfo")
	public List<TranPlanMatDetail> getMatExtraInfo(String matInfoData,String year,String month,String mainId,String ym){
		// 获取选择的物料数据
		List<MateDO> checkMatInfo = JSON.parseArray(matInfoData, MateDO.class);
		
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("list", checkMatInfo);
		pMap.put("ym", ym);
		List<TranPlanDetail> tranDetialList = tranPlanService.getMatInfoFromPadPlan(pMap);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("list", tranDetialList);
		paramMap.put("mainId", mainId);
		paramMap.put("year", year);
		paramMap.put("month", month);
		
		List<TranPlanMatDetail> list = matInfoService.getTranPlanMatInfo(paramMap);
		
		return list;
	}
	
	/**
	 * 根据年月获取 生产/交货计划 中的明细数据
	 * @param planYm
	 * @return
	 */
	@Log(value = "从生产/交货计划明细中获取调拨计划物料数据")
	@ResponseBody
	@RequestMapping("/getTranPlanDetailFromPadPlan")
	public List<TranPlanMatDetail> getTranPlanDetailFromPadPlan(String planYm,String matInfo,String seriesExpl,String mainId){
		
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("ym", planYm);
		pMap.put("matInfo", matInfo);
		pMap.put("seriesExpl", seriesExpl);
		List<TranPlanDetail> tranDetialList = tranPlanService.getTranPlanDetailFromPadPlan(pMap);
		
		if(tranDetialList != null && tranDetialList.size()>0){
			String[] arrYm = planYm.split("-");
			String year = arrYm[0];
			String month = arrYm[1];
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("list", tranDetialList);
			paramMap.put("mainId", mainId);
			paramMap.put("year", year);
			paramMap.put("month", month);
			
			List<TranPlanMatDetail> list = matInfoService.getTranPlanMatInfo(paramMap);
			
			return list;
		}else{
			return new ArrayList<TranPlanMatDetail>();
		}
	}
	
	/**
	 * 从生产交货计划中更新最新的物料数据
	 * @param planYm
	 * @param mainId
	 * @return
	 */
	@Log(value = "从生产交货计划中更新最新的物料数据")
	@ResponseBody
	@RequestMapping("/updateTranPlanDetailFromPadPlan")
	public RestCode updateTranPlanDetailFromPadPlan(String planYm,String mainId){
		
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("planYm", planYm);
		pMap.put("mainId", mainId);
		
		try {
			tranPlanService.updateTranPlanDetailFromPadPlan(pMap);
		} catch (Exception e) {
			e.printStackTrace();
			return RestCode.error(-1, e.getMessage());
		}
		
		return new RestCode();
	}
	
	/**
	 * 引用调拨计划 明细数据获取
	 * @param refId
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取引用调拨计划明细数据")
	@ResponseBody
	@RequestMapping("/getTranPlanDetailListRef")
	public List<TranPlanDetail> getTranPlanDetailListRef(String refId,String mainId){
		
		List<TranPlanDetail> detailList = tranPlanService.getTranPlanDetailListByMainId(refId);
		if(detailList.size()>0){
			for(TranPlanDetail item : detailList){
				item.setId(mainId);
			}
		}
		return detailList;
	}
	
	/**
	 * 导出调拨计划excel
	 * @param response
	 */
	@Log(value = "导出调拨计划报表")
	@ResponseBody
	@RequestMapping("/exportTranPlanInfo")
	public void exportTranPlanInfo(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		
		Workbook wb;
		try {
			String mainId = request.getParameter("mainId");
			
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN_TWO);
			
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath+"templates\\excelTemp\\tranPlanTemp.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);

			// 获取主表数据
			TranPlan tranPlan = tranPlanService.getTranPlanById(mainId);
			if(tranPlan == null){
				return;
			}
			String tranDate = tranPlan.getTranDate();
			
			String fileName = tranDate+"调拨计划报表"+currDate;
			// 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xlsx");
			
			
			// 获取明细数据
			List<TranPlanDetail> detailList = tranPlanService.getTranPlanDetailListByMainId(mainId);
			
			Sheet sheet = wb.getSheetAt(0);
			// sheet 名称改变
			wb.setSheetName(0, currDate);
			
			// 设置标题
			ExcelUtil.setValue(sheet, 0, 0, tranDate+"调拨计划", null);
			
			//***** 设置列头 *****
			
			String date1 = tranDate.split("~")[0].trim();
			Date dateT = DateUtils.parse(date1, DateUtils.DATE_PATTERN);
			ExcelUtil.setValue(sheet, 1, 12, date1, null);
			ExcelUtil.setValue(sheet, 1, 13, DateUtils.format(DateUtils.addDate(dateT, 1),DateUtils.DATE_PATTERN), null);
			ExcelUtil.setValue(sheet, 1, 14, DateUtils.format(DateUtils.addDate(dateT, 2),DateUtils.DATE_PATTERN), null);
			ExcelUtil.setValue(sheet, 1, 15, DateUtils.format(DateUtils.addDate(dateT, 3),DateUtils.DATE_PATTERN), null);
			ExcelUtil.setValue(sheet, 1, 16, DateUtils.format(DateUtils.addDate(dateT, 4),DateUtils.DATE_PATTERN), null);
			ExcelUtil.setValue(sheet, 1, 17, DateUtils.format(DateUtils.addDate(dateT, 5),DateUtils.DATE_PATTERN), null);
			ExcelUtil.setValue(sheet, 1, 18, DateUtils.format(DateUtils.addDate(dateT, 6),DateUtils.DATE_PATTERN), null);
			
			//***** 填充数据 *****
			// 插入行
			int listSize = detailList.size();
			if(listSize > 1){
				ExcelUtil.insertRow(sheet, 2, listSize-1);
			}
			
			// 初始行
			int beginRow = 2;
			for(int i=0;i<listSize;i++){
				TranPlanDetail item = detailList.get(i);
				ExcelUtil.setValue(sheet, beginRow+i, 0, item.getMatCode(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 1, item.getMatName(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 2, item.getProdSeries(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 3, item.getRank(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 4, item.getSaleForeQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 5, item.getSaleQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 6, item.getSaleScale(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 7, item.getPlanDevQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 8, item.getActDevQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 9, item.getEstDevQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 10, item.getDevScale(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 11, item.getUnDevQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 12, item.getMonQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 13, item.getTueQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 14, item.getWedQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 15, item.getThuQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 16, item.getFriQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 17, item.getSatQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 18, item.getSunQty(), null);
				ExcelUtil.setValue(sheet, beginRow+i, 19, item.getSumQty(), null);
			}

			// 导出excel
			OutputStream out = response.getOutputStream();
			ExcelUtil.exportExcel(wb, out);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退回调拨计划
	 * @param mainId
	 * @return
	 */
	@Log(value = "退回调拨计划")
	@ResponseBody
	@RequestMapping("/returnTranPlan")
	public RestCode returnTranPlan(String mainId){
		String status = "已保存";
		tranPlanService.updateTranPlanStatus(status, mainId);
		
		return new RestCode();
	}
}
