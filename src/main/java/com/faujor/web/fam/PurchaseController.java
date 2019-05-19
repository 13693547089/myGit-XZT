package com.faujor.web.fam;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.fam.PurchRecon;
import com.faujor.entity.fam.PurchReconDebit;
import com.faujor.entity.fam.PurchReconInvoce;
import com.faujor.entity.fam.PurchReconMate;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.common.CodeService;
import com.faujor.service.fam.PurchReconService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.PrivilegesCommon;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/purchRecon")
public class PurchaseController {

	@Autowired
	private PurchReconService purchReconService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private AsyncLogService asyncLogService;
	@Autowired
	private PrivilegesCommon privilegesCommon;
	@Autowired
	private OrgService orgService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 获取采购对账首页
	 * 
	 * @return
	 */
	@Log("获取采购对账列表信息")
	@RequestMapping("/reconIndex")
	public String auditIndex() {
		return "fam/purchase/purchase";
	}

	/**
	 * 编辑采购订单
	 * 
	 * @param reconNo
	 * @param model
	 * @param type
	 *            1新增 2编辑 3查看
	 * @return
	 */
	@Log("创建/编辑/查看采购对账")
	@SuppressWarnings("unchecked")
	@RequestMapping("/editPurchaseRecn")
	public String editPurchaseRecn(String reconCode, Model model, String type) {
		PurchRecon purchRecon = new PurchRecon();
		if (!"1".equals(type)) {
			purchRecon = purchReconService.getReconByCode(reconCode);
		}
		model.addAttribute("purchRecon", purchRecon);
		// 供应商列表
//		Map<String, Object> map = qualSuppService.queryAllQualSupp();
//		List<QualSupp> suppList = (List<QualSupp>) map.get("data");
		List<String> suppNos = privilegesCommon.getAllSupplierCode();
		List<QualSupp> suppList = qualSuppService.queryQualSuppBySapCodes(suppNos);
		model.addAttribute("suppList", suppList);
		// 工厂列表
		List<Dic> plantList = basicService.findDicListByCategoryCode("PLANT");
		model.addAttribute("plantList", plantList);
		model.addAttribute("type", type);
		return "fam/purchase/purchaseEdit";
	}

	/**
	 * 财务人员分页获取采购订单数据
	 * 
	 * @param page
	 * @param suppName
	 * @param reconCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFinancePurchReconByPage")
	public LayuiPage<PurchRecon> getFinancePurchReconByPage(LayuiPage<PurchRecon> page, String suppName,
			String reconCode, Date startDate, Date endDate,String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		if (!StringUtils.isEmpty(suppName)) {
			params.put("suppName", "%" + suppName + "%");
		}
		if (!StringUtils.isEmpty(reconCode)) {
			params.put("reconCode", "%" + reconCode + "%");
		}
		if (!StringUtils.isEmpty(status)) {
			params.put("status", status);
		}
		if (startDate != null) {
			params.put("startDate", startDate);
		}
		if (endDate != null) {
			params.put("endDate", endDate);
		}
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<>();
		map.put("ownId", user.getUserId());
		map.put("isContainOwn", true);
		List<UserDO> users = orgService.manageSubordinateUsers(map);
		List<String> userNames = new ArrayList<String>();
		for (UserDO userDo : users) {
			userNames.add(userDo.getUserName());
		}
		params.put("userNames", userNames);
		LayuiPage<PurchRecon> resultPage = purchReconService.getFinancePurchReconByPage(params);
		return resultPage;
	}

	/**
	 * 同步供应商物料信息
	 * 
	 * @param suppNo
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Log("采购对账同步供应商物料信息")
	@ResponseBody
	@RequestMapping("/synchReconMate")
	public List<PurchReconMate> synchReconMate(String suppNo, String plantCode, Date startDate, Date endDate) {
		AsyncLog al = new AsyncLog();
		String alId = UUIDUtil.getUUID();
		al.setId(alId);
		al.setAsyncName("采购对账供应商物料信息同步");
		asyncLogService.saveAsyncLog(al);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suppNo", suppNo);
		map.put("plantCode", plantCode);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<PurchReconMate> mates = purchReconService.getSapPurchReconMatesByMap(map);
		for (PurchReconMate mate : mates) {
			String loan = mate.getLoan();
			BigDecimal incomingTotalPrice = mate.getIncomingTotalPrice();
			BigDecimal incomingNum = mate.getIncomingNum();
			//对入库总价进行取反操作
			if (loan != null && "H".equals(loan) && incomingTotalPrice != null) {
				mate.setIncomingTotalPrice(incomingTotalPrice.negate());
				mate.setIncomingNum(incomingTotalPrice.negate());
			}
//			对入库数量取反
			if (loan != null && "H".equals(loan) && incomingNum != null) {
				mate.setIncomingNum(incomingNum.negate());
			}
			String suppRange = mate.getSuppRange();//获取供应商子范围编码
			//通过供应商子范围编码和供应商的SAP编码，查询供应商主数据中的供应商子范围描述
			String suppRangeDesc = qualSuppService.getSuppRangeDescBySapIdAndSuppRange(suppNo,suppRange);
			mate.setSuppRangeDesc(suppRangeDesc);
		}
		List<PurchReconMate> mates2 = purchReconService.getPurchMateByMap(map);
		if(mates!=null && mates.size()>0 && mates2!=null && mates2.size()>0){
			mates.removeAll(mates2);
		}
		if(mates!=null){
			al.setAsyncNum(mates.size());
		}
		al.setAsyncStatus("同步成功");
		asyncLogService.updateAsyncLog(al);
		return mates;
	}

	/**
	 * 保存对账单信息
	 * 
	 * @param recon
	 * @param matesJson
	 * @param debitsJson
	 * @param type
	 * @return
	 */
	@Log("保存采购对账")
	@ResponseBody
	@RequestMapping("/saveRecon")
	public RestCode saveRecon(PurchRecon recon, String matesJson, String debitsJson, String type) {
		List<PurchReconMate> mates = JsonUtils.jsonToList(matesJson, PurchReconMate.class);
		List<PurchReconDebit> debits = JsonUtils.jsonToList(debitsJson, PurchReconDebit.class);
		if ("1".equals(type)) {
			String reconCode = codeService.getCodeByCodeType("purchRecon");
			recon.setReconCode(reconCode);
			purchReconService.saveRecon(recon, mates, debits);
		} else {
			purchReconService.updateRecon(recon, mates, debits);
		}
		return new RestCode().put("data", recon);
	}

	/**
	 * 删除多账单信息
	 * 
	 * @param codeJson
	 * @return
	 */
	@Log("删除采购对账")
	@ResponseBody
	@RequestMapping("/delRecon")
	public RestCode delRecon(String codeJson) {
		List<String> reconCodes = JsonUtils.jsonToList(codeJson, String.class);
		purchReconService.delRecon(reconCodes);
		return new RestCode();
	}

	/**
	 * 修改对账单状态
	 * 
	 * @param reconCodeJson
	 * @param status
	 * @return
	 */
	@Log("变更采购对账状态")
	@ResponseBody
	@RequestMapping("/changeReconStatus")
	public RestCode changeReconStatus(String reconCodeJson, String status) {
		List<String> reconCodes = JsonUtils.jsonToList(reconCodeJson, String.class);
		purchReconService.changeReconState(reconCodes, status);
		return new RestCode();
	}

	/**
	 * 根据对账单编码获取对账单物料信息
	 * 
	 * @param reconCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatesByCode")
	public List<PurchReconMate> getMatesByCode(String reconCode) {
		List<PurchReconMate> mates = purchReconService.getMatesByReconCode(reconCode);
		return mates;
	}

	/**
	 * 根据对账单编码获取扣款信息
	 * 
	 * @param reconCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDebitsByCode")
	public List<PurchReconDebit> getDebitsByCode(String reconCode) {
		List<PurchReconDebit> debits = purchReconService.getDebitsByReconCode(reconCode);
		return debits;
	}

	/**
	 * 获取采购对账首页
	 * 
	 * @return
	 */
	@Log("获取采购对账确认列表信息")
	@RequestMapping("/confirmIndex")
	public String confirmIndex() {
		return "fam/purchase/purchaseConfirm";
	}

	/**
	 * 获取供应商对账单确认详情页面
	 * 
	 * @param reconCode
	 * @param model
	 * @return
	 */
	@Log("获取供应商对账单确认详情")
	@RequestMapping("/confirmPurchaseRecn")
	public String confirmPurchaseRecn(String reconCode, String type, Model model) {
		PurchRecon purchRecon = purchReconService.getReconByCode(reconCode);
		model.addAttribute("purchRecon", purchRecon);
		model.addAttribute("type", type);
		return "fam/purchase/purchaseConfirmDetail";
	}

	/**
	 * 供应商分页获取需要确认的数据
	 * 
	 * @param page
	 * @param suppName
	 * @param reconCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppPurchReconByPage")
	public LayuiPage<PurchRecon> getSuppPurchReconByPage(LayuiPage<PurchRecon> page, String suppName, String reconCode,
			Date startDate, Date endDate,String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		page.calculatePage();
		params.put("page", page);
		if (!StringUtils.isEmpty(suppName)) {
			params.put("suppName", "%" + suppName + "%");
		}
		if (!StringUtils.isEmpty(reconCode)) {
			params.put("reconCode", "%" + reconCode + "%");
		}
		if (startDate != null) {
			params.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(status)) {
			params.put("status", status);
		}
		if (endDate != null) {
			params.put("endDate", endDate);
		}
		SysUserDO user = UserCommon.getUser();
		String suppNo = user.getSuppNo();
		 params.put("suppNo", suppNo);
		LayuiPage<PurchRecon> resultPage = purchReconService.getSuppPurchReconByPage(params);
		return resultPage;
	}

	/**
	 * 获取发票信息
	 * 
	 * @param reconCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getInvocesByReconCode")
	public List<PurchReconInvoce> getInvocesByReconCode(String reconCode) {
		List<PurchReconInvoce> list = purchReconService.getInvocesByReconCode(reconCode);
		return list;
	}

	/**
	 * 确认对账单信息
	 * 
	 * @param reconCode
	 * @param invoiceStr
	 * @return
	 */
	@Log("确认采购对账单信息")
	@ResponseBody
	@RequestMapping("/confirmReconInfo")
	public RestCode confirmReconInfo(String reconCode, String invoice) {
		List<PurchReconInvoce> list = JsonUtils.jsonToList(invoice, PurchReconInvoce.class);
		int i = purchReconService.saveConfirmReconInfo(reconCode, list);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
	
	
	@Log("导出对账单物料信息")
	@RequestMapping("/exportReconMate")
	public String exportReconMate(HttpServletResponse response,String reconCode){
		String realName="对账单"+reconCode+"物料信息.xls";
		//获取物料信息
		List<PurchReconMate> mates = purchReconService.getMatesByReconCode(reconCode);
		//创建HSSFWorkbook对象(excel的文档对象)
	     HSSFWorkbook wb = new HSSFWorkbook();
		//建立新的sheet对象（excel的表单）
		HSSFSheet sheet=wb.createSheet("物资信息");
		//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1=sheet.createRow(0);
		//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell=row1.createCell(0);
		      //设置单元格内容
		cell.setCellValue("采购对账单"+reconCode+"物资信息");
		//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,17));
		//在sheet里创建第二行
		HSSFRow row2=sheet.createRow(1);    
		 //创建单元格并设置单元格内容
	      row2.createCell(0).setCellValue("序号");
	      row2.createCell(1).setCellValue("入库日期");    
	      row2.createCell(2).setCellValue("采购订单");
	      row2.createCell(3).setCellValue("物料描述");    
	      row2.createCell(4).setCellValue("单位");
	      row2.createCell(5).setCellValue("仓库");    
	      row2.createCell(6).setCellValue("入库数量");
	      row2.createCell(7).setCellValue("含税单价");    
	      row2.createCell(8).setCellValue("入库单价");
	      row2.createCell(9).setCellValue("入库总价");
	      row2.createCell(10).setCellValue("入库单号");    
	      row2.createCell(11).setCellValue("内向交货单");
	      row2.createCell(12).setCellValue("订单原因");    
	      row2.createCell(13).setCellValue("物料编码");
	      row2.createCell(14).setCellValue("已折金额");    
	      row2.createCell(15).setCellValue("折前含税价");
/*	      row2.createCell(15).setCellValue("进场支持单价");    
	      row2.createCell(16).setCellValue("进场支持费用(交货)");*/
	      row2.createCell(16).setCellValue("借款项");    
	      //在sheet里创建第三行
	      for (int i=0;i<mates.size();i++) {
	    	  PurchReconMate mate=mates.get(i);
	    	  HSSFRow row = sheet.createRow(2+i);
		      row.createCell(0).setCellValue(i+1);
		      row.createCell(1).setCellValue(DateUtils.format(mate.getIncomingDate(), "yyyy-MM-dd"));    
		      row.createCell(2).setCellValue(mate.getPurchCode());
		      row.createCell(3).setCellValue(mate.getMateDesc());    
		      row.createCell(4).setCellValue(mate.getUnitDesc());
		      row.createCell(5).setCellValue(mate.getWareHouse());
		      BigDecimal incomingNum = mate.getIncomingNum();
		      row.createCell(6).setCellValue(incomingNum==null?0:incomingNum.doubleValue());    
		      BigDecimal taxPrice = mate.getTaxPrice();
		      row.createCell(7).setCellValue(taxPrice==null?0:taxPrice.doubleValue());
		      BigDecimal incomingPrice = mate.getIncomingPrice();
		      row.createCell(8).setCellValue(incomingPrice==null?0:incomingPrice.doubleValue());  
		      BigDecimal incomingTotalPrice = mate.getIncomingTotalPrice();
		      row.createCell(9).setCellValue(incomingTotalPrice==null?0:incomingTotalPrice.doubleValue());
		      row.createCell(10).setCellValue(mate.getIncomingCode());    
		      row.createCell(11).setCellValue(mate.getInnerDeliveryCode());
		      row.createCell(12).setCellValue(mate.getOrderReason());    
		      row.createCell(13).setCellValue(mate.getMateCode());
		      BigDecimal discountAmount = mate.getDiscountAmount();
		      row.createCell(14).setCellValue(discountAmount==null?0:discountAmount.doubleValue());    
		      BigDecimal taxCost = mate.getTaxCost();
		      row.createCell(15).setCellValue(taxCost==null?0:taxCost.doubleValue());
//		      BigDecimal supportPrice = mate.getSupportPrice();
//		      row.createCell(15).setCellValue(supportPrice==null?0:supportPrice.doubleValue());  
//		      BigDecimal supportCost = mate.getSupportCost();
//		      row.createCell(16).setCellValue(supportCost==null?0:supportCost.doubleValue());
		      row.createCell(16).setCellValue(mate.getLoan());  
		}
		OutputStream os=null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/x-msdownlocad");
			realName=URLEncoder.encode(realName, "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+realName);
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IoUtil.closeIo(os,wb);
		}
		return null;
	}
	
	/**
	 * 跳转到采购对账任务流程审核页面
	 * @param model
	 * @param taskPDO
	 * @return
	 */
	@RequestMapping("/getPurchaseAuditHtml")
	public String getPurchaseAuditHtml(Model model ,TaskParamsDO taskPDO){
		PurchRecon purchRecon = purchReconService.getReconById(taskPDO.getSdata1());
		// 供应商列表
//		Map<String, Object> map = qualSuppService.queryAllQualSupp();
//		List<QualSupp> suppList = (List<QualSupp>) map.get("data");
		List<String> suppNos = privilegesCommon.getAllSupplierCode();
		List<QualSupp> suppList = qualSuppService.queryQualSuppBySapCodes(suppNos);
		// 工厂列表
		List<Dic> plantList = basicService.findDicListByCategoryCode("PLANT");
		model.addAttribute("purchRecon", purchRecon);
		model.addAttribute("suppList", suppList); 
		model.addAttribute("plantList", plantList);
		model.addAttribute("type", "2");
		model.addAttribute("taskPDO", taskPDO);
		return "fam/purchase/purchaseAudit";
	}
	
}
