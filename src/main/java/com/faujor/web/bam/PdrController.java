package com.faujor.web.bam;

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
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.entity.bam.psm.PdrStockReport;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.bam.PdrService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.sapcenter.bam.SapPdrService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

/**
 * 产能上报 控制类
 * 
 * @author Vincent
 *
 */
@Controller
@RequestMapping(value = "/bam/pdr")
public class PdrController {

	@Autowired
	private PdrService pdrService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private SapPdrService sapPdrService;
	@Autowired
	private QualSuppService qualSuppService;

	/**
	 * 产能上报列表
	 * 
	 * @param model
	 * @return
	 */
	@Log(value = "产能上报列表信息")
	@RequestMapping("/pdrPage")
	public String pdrPage(Model model) {

		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);

		return "bam/pdr/pdrList";
	}

	/**
	 * 产能上报列表分页查询
	 * 
	 * @param page
	 * @param search_pdrCode
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @return
	 */
	@Log(value = "获取产能上报列表信息")
	@ResponseBody
	@RequestMapping("/getPdrPageList")
	public LayuiPage<Pdr> getPdrPageList(LayuiPage<Pdr> page, String search_pdrCode, String search_status,
			String prodDateStart,String prodDateEnd,String suppName,
			String search_crtDateStart, String search_crtDateEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("pdrCode", search_pdrCode);
		params.put("suppName", suppName);
		params.put("status", search_status);
		params.put("produceDateStart", prodDateStart);
		params.put("produceDateEnd", prodDateEnd);
		params.put("crtDateStart", search_crtDateStart);
		params.put("crtDateEnd", search_crtDateEnd);
		params.put("crtUser", "");
		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		params.put("suppCode", user.getSuppNo());
		LayuiPage<Pdr> pageList = pdrService.getPdrByPage(params);
		return pageList;
	}

	/**
	 * 删除产能上报
	 * 
	 * @param id
	 * @return
	 */
	@Log(value = "删除单个产能上报列表信息")
	@ResponseBody
	@RequestMapping("/deletePdr")
	public RestCode deletePdr(String id) {
		pdrService.delPdrInfoById(id);
		return new RestCode();
	}

	/**
	 * 批量删除删除产能上报
	 * 
	 * @param ids
	 * @return
	 */
	@Log(value = "删除批量产能上报列表信息")
	@ResponseBody
	@RequestMapping("/deleteBatchPdr")
	public RestCode deleteBatchPdr(String ids) {

		List<String> list = JSON.parseArray(ids, String.class);

		pdrService.delBatchPdrInfoByIds(list);
		return new RestCode();
	}

	/**
	 * 产能上报明细列表
	 * 
	 * @param model
	 * @param mainId
	 * @param type
	 *            类型： 1：编辑或创建 ，2：查看
	 * @return
	 */
	@Log(value = "创建/编辑/查看 产能上报明细")
	@RequestMapping("/pdrDetailPage")
	public String pdrDetailPage(Model model, String mainId, String type) {

		model.addAttribute("type", type);

		Pdr pdr = new Pdr();
		if (mainId == null || mainId.equals("")) {
			// ******* 创建
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			// 设置创建人
			pdr.setCrtUser(user.getName());
			// 设置供应商信息
			pdr.setSuppCode(user.getSuppNo());
			pdr.setSuppName(user.getName());

			// id
			pdr.setId(UUIDUtil.getUUID());
			// pdrCode
			pdr.setPdrCode("");
			// 状态
			pdr.setStatus("");
			// 同步状态
			pdr.setSyncFlag("0");

			pdr.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		} else {
			// ****** 编辑或查看
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", mainId);
			pdr = pdrService.getPdrByMap(map).get(0);
		}

		// 设置主表ID
		model.addAttribute("mainId", pdr.getId());
		// 主表信息
		model.addAttribute("list", pdr);

		return "bam/pdr/pdrDetail";
	}
	
	/**
	 * 产能上报明细列表分页查询
	 * 
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取产能上报明细数据")
	@ResponseBody
	@RequestMapping("/getPdrDetailPageList")
	public LayuiPage<PdrDetail> getPdrDetailPageList(LayuiPage<PdrDetail> page, String mainId) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("mainId", mainId);
		LayuiPage<PdrDetail> pageList = pdrService.getPdrDetailPage(params);
		return pageList;
	}

	/**
	 * 产能上报明细明细数据获取
	 * 
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取产能上报明细数据")
	@ResponseBody
	@RequestMapping("/getPdrDetailList")
	public List<PdrDetail> getPdrDetailList(String mainId) {
		return pdrService.getPdrDetailListByMainId(mainId);
	}

	/**
	 * 保存/提交 产能上报信息
	 * 
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "保存产能上报信息")
	@ResponseBody
	@RequestMapping("/savePdrInfo")
	public RestCode savePdrInfo(Pdr pdr, String pdrDetailData, String pdrItem1Data, String pdrItem2Data,
			String pdrItem3Data, String sType) {
		
		RestCode restCode = new RestCode();
		// 判断供应商的生产日期产能日报是否重复
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("produceDate", DateUtils.format(pdr.getProduceDate(), DateUtils.DATE_PATTERN));
		map.put("suppCode", pdr.getSuppCode());
		map.put("nonId", pdr.getId());
		int rs = pdrService.getPdrCount(map);
		if (rs > 0) {
			return RestCode.error(90, "已存在该生产日期的产能日报，请重新选择！");
		}

		// 明细数据转成list
		List<PdrDetail> detailList = JSON.parseArray(pdrDetailData, PdrDetail.class);
		
		String repStr = "";
		Map<String,String> repMap = new HashMap<String,String>();
		for (int i = 0; i < detailList.size(); i++) {
			PdrDetail item = detailList.get(i);
			
			if (sType.equals("sb")) {
				// 提交时触发
				// 获取重复的物料
				if(!repMap.containsKey(item.getMatCode())){
					repMap.put(item.getMatCode(), "1");
				}else{
					repStr +=item.getMatCode()+","+item.getMatName()+";<br>";
				}
			}
			
			// 设置明细ID
			item.setId(UUIDUtil.getUUID());

			item.setActPdcQty(item.getActPdcQty() == null ? 0 : item.getActPdcQty());
			item.setActDevQty(item.getActDevQty() == null ? 0 : item.getActDevQty());
			item.setStockQty(item.getStockQty() == null ? 0 : item.getStockQty());
			item.setQcStock(item.getQcStock() == null ? 0 : item.getQcStock());
			item.setUnQcStock(item.getUnQcStock() == null ? 0 : item.getUnQcStock());
			item.setBeginStock(item.getBeginStock() == null ? 0 : item.getBeginStock());
			item.setTheoryStock(item.getTheoryStock() == null ? 0 : item.getTheoryStock());
			item.setDiffStock(item.getDiffStock() == null ? 0 : item.getDiffStock());
			item.setPreSumDev(item.getPreSumDev() == null ? 0 : item.getPreSumDev());
		}
		
		// 存在重复物料,返回
		if(!repStr.equals("")){
			if (sType.equals("sb")) {
				// 提交时触发
				restCode.put("code", "80");
				restCode.put("msg", repStr);
				return restCode;
			}
		}
		
		// 设置编码
		if (pdr.getPdrCode() == null || pdr.getPdrCode().equals("")) {
			// pdrCode , 产能日报编码规则代码 ：pdrNo
			String pdrCode = codeService.getCodeByCodeType("pdrNo");
			pdr.setPdrCode(pdrCode);
		}
		List<PdrItem> item1List = JSON.parseArray(pdrItem1Data, PdrItem.class);
		List<PdrItem> item2List = JSON.parseArray(pdrItem2Data, PdrItem.class);
		List<PdrItem> item3List = JSON.parseArray(pdrItem3Data, PdrItem.class);
		for (int i = 0; i < item1List.size(); i++) {
			PdrItem item = item1List.get(i);
			// 设置ID
			item.setId(UUIDUtil.getUUID());
			item.setQty(item.getQty() == null ? 0 : item.getQty());
			item.setQcQty(item.getQcQty() == null ? 0 : item.getQcQty());
			item.setUnQcQty(item.getUnQcQty() == null ? 0 : item.getUnQcQty());
		}
		for (int i = 0; i < item2List.size(); i++) {
			PdrItem item = item2List.get(i);
			// 设置ID
			item.setId(UUIDUtil.getUUID());
			item.setQty(item.getQty() == null ? 0 : item.getQty());
			item.setQcQty(item.getQcQty() == null ? 0 : item.getQcQty());
			item.setUnQcQty(item.getUnQcQty() == null ? 0 : item.getUnQcQty());
		}
		for (int i = 0; i < item3List.size(); i++) {
			PdrItem item = item3List.get(i);
			// 设置ID
			item.setId(UUIDUtil.getUUID());
			item.setQty(item.getQty() == null ? 0 : item.getQty());
			item.setQcQty(item.getQcQty() == null ? 0 : item.getQcQty());
			item.setUnQcQty(item.getUnQcQty() == null ? 0 : item.getUnQcQty());
		}

		List<PdrItem> itemList = new ArrayList<PdrItem>();
		if (item1List.size() > 0) {
			itemList.addAll(item1List);
		}
		if (item2List.size() > 0) {
			itemList.addAll(item2List);
		}
		if (item3List.size() > 0) {
			itemList.addAll(item3List);
		}

		// sType sb：提交 sv：保存
		if (sType.equals("sb")) {
			// 提交
			pdr.setStatus("已提交");
		}

		pdrService.savePdrInfo(pdr, detailList, itemList);

		// 提交情况下，同步数据至 sap的中间库中
		if (sType.equals("sb")) {
			try {
				int res = sapPdrService.saveSapPdrInfo(pdr, detailList, itemList);
				if(res>0){
					// 修改同步标志  1：已同步  0:未同步
					pdrService.updatePdrSyncFlag("1", pdr.getId());
				}
			} catch (Exception e) {
			}
		}
		
		return restCode;
	}

	/**
	 * 提交
	 * 
	 * @param page
	 * @param mainId
	 * @return
	 */
	@Log(value = "提交产能上报列表信息")
	@ResponseBody
	@RequestMapping("/submitPdr")
	public RestCode submitPdr(String mainId) {

		String status = "已提交";
		pdrService.updatePdrStatus(status, mainId);

		return new RestCode();
	}

	/**
	 * 从供应商排产中获取 产能上报 明细数据
	 * 
	 * @param suppCode
	 *            供应商编码
	 * @param productDate
	 *            生产日期
	 * @param firstDate
	 *            本月第一天
	 * @param preDate
	 *            生产日期前一天
	 * @param mainId
	 *            主表ID
	 * @return
	 */
	@Log(value = "从供应商排产中获取产能上报明细数据")
	@ResponseBody
	@RequestMapping("/getPdrDetailListFromSuppProd")
	public List<PdrDetail> getPdrDetailListFromSuppProd(String suppCode, String productDate, String mainId) {

		String[] tempDate = productDate.split("-");
		// 本月第一天
		String firstDate = tempDate[0] + "-" + tempDate[1] + "-" + "01";

		Date currDate = DateUtils.parse(productDate, DateUtils.DATE_PATTERN);
		// 当前日期前一天
		String preDate = DateUtils.format(DateUtils.addDate(currDate, -1), DateUtils.DATE_PATTERN);
		// 上个月最后一天及以前最后一次已提交的日期

		return pdrService.getPdrDetailListFromSuppProd(suppCode, productDate, firstDate, preDate, mainId);
	}
	
	@Log(value = "获取产能上报上个日期的实际库存明细数据")
	@ResponseBody
	@RequestMapping("/getPdrItemStockList")
	public List<PdrItem> getPdrItemStockList(String suppCode, String productDate, String itemTb2Json) {

		// 转成实体类
		List<PdrItem> itemList = JSON.parseArray(itemTb2Json, PdrItem.class);

		Date currDate = DateUtils.parse(productDate, DateUtils.DATE_PATTERN);
		// 当前日期前一天
		String preDate = DateUtils.format(DateUtils.addDate(currDate, -1), DateUtils.DATE_PATTERN);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemType", "po");
		map.put("produceDate", preDate);
		map.put("suppCode", suppCode);
		map.put("list", itemList);
		
		return pdrService.getPdrItemListFromPreItem(map);
	}
	

	/**
	 * 产能上报品类数据获取
	 * 
	 * @param mainId
	 * @param type
	 * @return
	 */
	@Log(value = "获取产能上报品类数据")
	@ResponseBody
	@RequestMapping("/getPdrItemList")
	public List<PdrItem> getPdrItemList(String mainId, String type) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mainId", mainId);
		map.put("itemType", type);

		return pdrService.getPdrItemListByMainId(map);
	}

	/**
	 * 产能上报查看界面
	 * 
	 * @param model
	 * @return
	 */
	@Log(value = "产能上报查看列表信息")
	@RequestMapping("/pdrView")
	public String pdrView(Model model) {

		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);

		return "bam/pdr/pdrView";
	}

	/**
	 * 产能上报列表分页查询
	 * 
	 * @param page
	 * @param search_pdrCode
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @return
	 */
	@Log(value = "获取产能上报查看列表信息")
	@ResponseBody
	@RequestMapping("/getPdrViewList")
	public LayuiPage<Pdr> getPdrViewList(LayuiPage<Pdr> page, String search_pdrCode, String search_status,
			String prodDateStart,String prodDateEnd,String suppName,
			String search_crtDateStart, String search_crtDateEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("pdrCode", search_pdrCode);
		params.put("suppName", suppName);
		params.put("status", search_status);
		params.put("produceDateStart", prodDateStart);
		params.put("produceDateEnd", prodDateEnd);
		params.put("crtDateStart", search_crtDateStart);
		params.put("crtDateEnd", search_crtDateEnd);
		params.put("crtUser", "");
		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		LayuiPage<Pdr> list = new LayuiPage<Pdr>();
		if(user.getUserType().equals("supplier")){
			// 用户为供应商
			params.put("suppCode", user.getSuppNo());
			list = pdrService.getPdrByPage(params);
		}else{
			// 用户不为供应商
			
			// 判断是否为采购员
			OrgDo org = new OrgDo();
			org.setSpersonId(user.getUserId());
			org.setSfcode("%PURCHAROR%");
			int count = orgService.getOrgUserByCondition(org).size();
			if(count>0){
				// 为采购员，获取采购员及其下级用户
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("ownId", user.getUserId());
				paramMap.put("orgCode", "PURCHAROR");
				paramMap.put("isContainOwn", true);
				// 获取这个用户的下级采购员
				List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
				List<Long> userIdList = new ArrayList<Long>();
				for(UserDO item : userList){
					userIdList.add(item.getId());
				}
				// 用户的id
				params.put("list", userIdList);
				
				list = pdrService.getPdrViewByPage(params);
			}else{
				// 不是采购员
				// 获取全部
				list = pdrService.getPdrByPage(params);
			}
		}
		
		return list;
	}
	
	/**
	 * 产能上报查看明细界面
	 * @param model
	 * @param mainId
	 * @param type
	 * @return
	 */
	@Log(value = "产能上报查看明细")
	@RequestMapping("/pdrViewDetailPage")
	public String pdrViewDetailPage(Model model, String mainId, String type) {

		model.addAttribute("type", type);

		Pdr pdr = new Pdr();
		if (mainId == null || mainId.equals("")) {
			// ******* 创建
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			// 设置创建人
			pdr.setCrtUser(user.getName());
			// 设置供应商信息
			pdr.setSuppCode(user.getSuppNo());
			pdr.setSuppName(user.getName());

			// id
			pdr.setId(UUIDUtil.getUUID());
			// pdrCode
			pdr.setPdrCode("");
			// 状态
			pdr.setStatus("");

			pdr.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		} else {
			// ****** 编辑或查看
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", mainId);
			pdr = pdrService.getPdrByMap(map).get(0);
		}

		// 设置主表ID
		model.addAttribute("mainId", pdr.getId());
		// 主表信息
		model.addAttribute("list", pdr);

		return "bam/pdr/pdrViewDetail";
	}
	
	/**
	 * 物料筛选
	 * @return
	 */
	@RequestMapping("/matRepeat")
	public String matRepeat(){
		return "bam/pdr/matRepeat";
	}
	
	/**
	 * 修改产能上报的状态
	 * @param id
	 * @param status
	 * @return
	 */
	@Log(value = "产能上报退回处理")
	@ResponseBody
	@RequestMapping("/pdrReturn")
	public RestCode pdrReturn(String id,String status,String suppCode,String productDate){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("id", id);
		map.put("suppCode", suppCode);
		map.put("productDate", productDate);
		// 修改计算数据
		int rs = pdrService.updatePdrCalcData(map);
		if(rs>0){
			// 删除中间库存中的数据
			try {
				sapPdrService.delSapPdrInfo(id);
				
				// 修改同步标志  1：已同步  0:未同步
				pdrService.updatePdrSyncFlag("0", id);
			} catch (Exception e) {
			}
		}
		return new RestCode();
	}
	
	/*******************产能上报：供应商库存报表**********************/
	
	/**
	 * 产能上报库存报表
	 * 
	 * @param model
	 * @return
	 */
	@Log(value = "产能上报库存报表")
	@RequestMapping("/pdrStockReportPage")
	public String pdrStockReportPage(Model model) {

		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);
		
		return "bam/pdr/pdrStockReport";
	}
	
	@Log(value = "获取产能上报供应商库存信息")
	@ResponseBody
	@RequestMapping("/getPdrStockReportInfo")
	public List<PdrStockReport> getPdrStockReportInfo(String suppName,String status,String prodDateStart,String prodDateEnd,
			String series,String matName,String matType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("suppName", suppName);
		params.put("status", status);
		params.put("prodDateStart", prodDateStart);
		params.put("prodDateEnd", prodDateEnd);
		params.put("series", series);
		params.put("matName", matName);
		params.put("matType", matType);
		
		return pdrService.getPdrStockReportInfo(params);
	} 
	
	/**
	 * 导出供应商库存信息
	 * @param request
	 * @param response
	 */
	@Log(value = "导出供应商库存信息")
	@ResponseBody
	@RequestMapping("/exportPdrStock")
	public void exportPdrStock(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		
		String suppName = request.getParameter("suppName");
		String prodDateStart = request.getParameter("prodDateStart");
		String prodDateEnd = request.getParameter("prodDateEnd");
		String series = request.getParameter("series");
		String matName = request.getParameter("matName");
		String matType = request.getParameter("matType");
		String status = request.getParameter("status");
		
		Workbook wb;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("suppName", suppName);
			params.put("status", status);
			params.put("prodDateStart", prodDateStart);
			params.put("prodDateEnd", prodDateEnd);
			params.put("series", series);
			params.put("matName", matName);
			params.put("matType", matType);
			
			List<PdrStockReport> list = pdrService.getPdrStockReportInfo(params);
			// 总数
			int size = list.size();
			// 当前日期
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
			
			
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath+"templates\\excelTemp\\suppStockReportTemp.xlsx";
			
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			
			String fileName = "供应商库存报表"+currDate;
			// 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xlsx");
			Sheet sheet = wb.getSheetAt(0);
			// sheet 名称改变
			wb.setSheetName(0, currDate);
			
			if(size-1>0){
				// 插入行数 
				ExcelUtil.insertRow(sheet, 1, size-1);
			}
			
			int startRow = 1;
			for(int i=0;i<size;i++){
				PdrStockReport item = list.get(i);
				ExcelUtil.setValue(sheet, i+startRow, 0, item.getSuppName(), null);
				ExcelUtil.setValue(sheet, i+startRow, 1, item.getProduceDate(), null);
				ExcelUtil.setValue(sheet, i+startRow, 2, item.getSeries(), null);
				ExcelUtil.setValue(sheet, i+startRow, 3, item.getMatName(), null);
				ExcelUtil.setValue(sheet, i+startRow, 4, item.getSeries(), null);
				ExcelUtil.setValue(sheet, i+startRow, 5, item.getBatchNo(), null);
				ExcelUtil.setValue(sheet, i+startRow, 6, item.getQcQty(), null);
				ExcelUtil.setValue(sheet, i+startRow, 7, item.getUnQcQty(), null);
				ExcelUtil.setValue(sheet, i+startRow, 8, item.getQty(), null);
				ExcelUtil.setValue(sheet, i+startRow, 9, item.getMatCode(), null);
				ExcelUtil.setValue(sheet, i+startRow, 10, item.getSuppCode(), null);
			}
			
			// 导出excel
			OutputStream out = response.getOutputStream();
			ExcelUtil.exportExcel(wb, out);
			
		} catch (Exception e) {
		}
		
	}
	
	/**
	 * 产能上报列表(采购员)
	 * 
	 * @param model
	 * @return
	 */
	@Log(value = "产能上报列表信息（采购员）")
	@RequestMapping("/pdrSpecialPage")
	public String pdrSpecialPage(Model model) {

		List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);

		return "bam/pdr/pdrSpecialList";
	}
	
	/**
	 * 产能上报列表分页查询（采购员）
	 * 
	 * @param page
	 * @param search_pdrCode
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @return
	 */
	@Log(value = "获取产能上报列表信息（采购员）")
	@ResponseBody
	@RequestMapping("/getPdrSpecialPageList")
	public LayuiPage<Pdr> getPdrSpecialPageList(LayuiPage<Pdr> page, String search_pdrCode, String search_status,
			String prodDateStart,String prodDateEnd,String suppName,
			String search_crtDateStart, String search_crtDateEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("pdrCode", search_pdrCode);
		params.put("suppName", suppName);
		params.put("status", search_status);
		params.put("produceDateStart", prodDateStart);
		params.put("produceDateEnd", prodDateEnd);
		params.put("crtDateStart", search_crtDateStart);
		params.put("crtDateEnd", search_crtDateEnd);
		params.put("crtUser", "");
		// 获取当前用户
		SysUserDO user = UserCommon.getUser();
		
		// 获取采购员
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.manageSubordinateUsers(paramMap);
		params.put("list", list);
		
		LayuiPage<Pdr> pageList = pdrService.getPdrSpecialByPage(params);
		return pageList;
	}
	
	/**
	 * 产能上报明细列表（采购员）
	 * 
	 * @param model
	 * @param mainId
	 * @param type
	 *            类型： 1：编辑或创建 ，2：查看
	 * @return
	 */
	@Log(value = "创建/编辑/查看 产能上报明细")
	@RequestMapping("/pdrSpecialDetailPage")
	public String pdrSpecialDetailPage(Model model, String mainId, String type) {

		model.addAttribute("type", type);

		Pdr pdr = new Pdr();
		if (mainId == null || mainId.equals("")) {
			// ******* 创建
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			// 设置创建人
			pdr.setCrtUser(user.getName());
			// 设置供应商信息
			pdr.setSuppCode("");
			pdr.setSuppName("");

			// id
			pdr.setId(UUIDUtil.getUUID());
			// pdrCode
			pdr.setPdrCode("");
			// 状态
			pdr.setStatus("");
			// 同步状态
			pdr.setSyncFlag("0");

			pdr.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		} else {
			// ****** 编辑或查看
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", mainId);
			pdr = pdrService.getPdrByMap(map).get(0);
		}

		// 设置主表ID
		model.addAttribute("mainId", pdr.getId());
		// 主表信息
		model.addAttribute("list", pdr);

		return "bam/pdr/pdrSpecialDetail";
	}
	
	/**
	 * 供应商选择弹框
	 * @return
	 */
	@RequestMapping("/suppSelDialog")
	public String suppSelDialog(){
		return "bam/pdr/suppSelectDialog";
	}
	
	/**
	 * 采购员的所有供应商列表
	 * 
	 * @param qualSupp
	 * @return
	 */
	@Log(value ="获取供应商信息列表")
	@ResponseBody
	@RequestMapping("/queryAllQualSuppOfUser")
	public Map<String, Object> queryAllQualSuppOfUser(String suppInfo, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		SysUserDO user = UserCommon.getUser();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);

		Map<String, Object> map = new HashMap<String, Object>();
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.manageSubordinateUsers(paramMap);
		map.put("userDO", list);
		
		QualSuppDO qualSuppDO= new QualSuppDO();
		qualSuppDO.setSuppInfo(suppInfo);
		
		map.put("start", start);
		map.put("end", end);
		map.put("qualSuppDO", qualSuppDO);
		Map<String, Object> page2 = qualSuppService.queryQualSuppByUserIds(map);
		return page2;
	}
	
	/**
	 * 同步产能上报信息
	 * @param pdrIds
	 * @return
	 */
	@Log(value = "同步产能上报信息")
	@ResponseBody
	@RequestMapping("/syncPdrInfo")
	public RestCode syncPdrInfo(String pdrIds) {
		
		RestCode restCode = new RestCode();

		try {
			List<String> ids = JSON.parseArray(pdrIds, String.class);
			int res = 0;
			for(int i=0;i<ids.size();i++){
				String mainId = ids.get(i);
				// 明细数据
				List<PdrDetail> detailList = pdrService.getPdrDetailListByMainId(mainId);
				// item数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mainId", mainId);
				map.put("itemType", "");
				List<PdrItem> itemList = pdrService.getPdrItemListByMainId(map);
				// 主表数据
				map.put("id", mainId);
				List<Pdr> list = pdrService.getPdrByMap(map);
				Pdr pdr = new Pdr();
				if(list.size()>0){
					pdr = list.get(0);
				}
				
				// 同步数据
				res = sapPdrService.saveSapPdrInfo(pdr, detailList, itemList);
				if(res>0){
					// 修改同步标志  1：已同步  0:未同步
					pdrService.updatePdrSyncFlag("1", pdr.getId());
				}
			}
		} catch (Exception e) {
			return RestCode.error(-1, e.getMessage());
		}
		
		return restCode;
	}
}
