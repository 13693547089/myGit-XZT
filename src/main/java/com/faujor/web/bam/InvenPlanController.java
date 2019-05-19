package com.faujor.web.bam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InvenPadCompare;
import com.faujor.entity.bam.psm.InvenPlan;
import com.faujor.entity.bam.psm.InvenPlanDetail;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdVo;
import com.faujor.entity.bam.psm.SuppVo;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.InvenPlanService;
import com.faujor.service.bam.OrderMonthService;
import com.faujor.service.bam.OrderService;
import com.faujor.service.bam.PadPlanService;
import com.faujor.service.bam.PdrService;
import com.faujor.service.bam.SuppProdService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.BigDecimalUtil;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/invenPlan")
public class InvenPlanController {
	@Autowired
	private InvenPlanService invenPlanService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PadPlanService padPlanService;
	@Autowired
	private PdrService pdrService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private SuppProdService suppProdService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private OrderMonthService orderMonthService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 获取备货计划列表页
	 * 
	 * @return
	 */
	@Log("获取备货计划列表信息")
	@RequestMapping("/getInvenPlanIndex")
	public String getInvenPlanIndex() {
		return "bam/invenPlan/invenPlanIndex";
	}

	/**
	 * 获取备货计划登录页
	 * 
	 * @param id
	 * @param type
	 *            1新建 2修改 3查看
	 * @param mode
	 * @return
	 */
	@Log("创建/修改/查看备货计划")
	@RequestMapping("/getEditPage")
	public String getEditPage(String id, String type, Model model,String planMonth) {
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		// 获取供应商排产计划主表信息
		boolean flag=false;
		boolean importflag=false;
		InvenPlan invenPlan = new InvenPlan();
		if (!StringUtils.isEmpty(type) && !"1".equals(type) && id != null) {
			invenPlan = invenPlanService.getInvenPlanById(id);
			String creater = invenPlan.getCreater();
			if(StringUtil.equals(creater, userId+"")){
				importflag=true;
			}
			Date planMonth2 = invenPlan.getPlanMonth();
			Date currMonth = DateUtils.parse(DateUtils.format(new Date(),"yyyy-MM"),"yyyy-MM");
			if(!planMonth2.before(currMonth)){
				flag=true;
			}
		} else {
			invenPlan.setId(UUIDUtil.getUUID());
			invenPlan.setCreateTime(new Date());
		}
		model.addAttribute("importflag", importflag);
		model.addAttribute("invenPlan", invenPlan);
		// 方式
		model.addAttribute("type", type);
		// 获取套袋信息下拉框数据源
		List<Dic> ItemList = basicService.findDicListByCategoryCode("ItemInfo");
		model.addAttribute("ItemList", ItemList);
		// 获取管理的采购员列表
		Map<String, Object> map = new HashMap<>();
		map.put("ownId", userId);
		// map.put("orgCode", "PURCHAROR");
		map.put("isContainOwn", true);
		List<UserDO> users = orgService.manageSubordinateUsers(map);
		List<Long> userIds = new ArrayList<Long>();
		for (UserDO userDo : users) {
			userIds.add(userDo.getId());
		}
		map.put("userIds", userIds);
		if(StringUtils.isEmpty(planMonth)){
			planMonth=DateUtils.format(new Date(), "yyyy-MM");
		}
		map.put("planMonth", planMonth);
		// 获取物料信息
		List<MateDO> mateList = materialService.queryPrdPlanMateListOfUsers(map);
//		获取
		model.addAttribute("mateList", mateList);
		model.addAttribute("flag", flag);
		// 获取供应商信息
		List<QualSupp> suppList = qualSuppService.queryAllQualSuppListOfUsers(map);
		model.addAttribute("suppList", suppList);
		if ("4".equals(type)) {
			return "bam/invenPlan/invenPlanAudit";
		} else {
			return "bam/invenPlan/invenPlanEdit";
		}
	}
	/**
	 * 获取物料下拉列表框数据源
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMateSelectList")
	public List<MateDO> getMateSelectList(String planMonth,String mateDesc,String  planCode){
		// 获取管理的采购员列表
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
/*		Map<String, Object> map = new HashMap<>();
		map.put("ownId", userId);
		map.put("isContainOwn", true);
		List<UserDO> users = orgService.manageSubordinateUsers(map);
		List<Long> userIds = new ArrayList<Long>();
		for (UserDO userDo : users) {
			userIds.add(userDo.getId());
		}*/
/*		Map<String, Object> map = new HashMap<>();
		List<Long> userIds = new ArrayList<Long>();
		if(StringUtils.isEmpty(planCode)){
			userIds.add(userId);
		}else{
			InvenPlan invenPlan = invenPlanService.getInvenPlanByCode(planCode);
			userIds.add(Long.parseLong(invenPlan.getCreater()));
		}
		map.put("userIds", userIds);
		map.put("planMonth", planMonth);
		if(!StringUtils.isEmpty(mateDesc)){
			map.put("mateDesc", "%"+mateDesc+"%");
		}
		// 获取物料信息
		List<MateDO> mateList = materialService.queryPrdPlanMateListOfUsers(map);
		System.out.println(JsonUtils.beanToJson(mateList));*/
		Map<String, Object> map = new HashMap<>();
		map.put("planCode", planCode);
		if(!StringUtils.isEmpty(mateDesc)){
			map.put("mateDesc", "%"+mateDesc+"%");
		}
		List<MateDO> mateList = invenPlanService.getMateSelectList(map);
		return mateList;
	}
	
	/**
	 * 根据备货计划获取他的排产计划列表
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSuppProdPage")
	public String getSuppProdPage(String id, Model model) {

		InvenPlan invenPlan = invenPlanService.getInvenPlanById(id);
		model.addAttribute("invenPlan", invenPlan);
		return "bam/suppProd/invenPlanProd";
	}

	/**
	 * 获取备货计划详情添加的对话框
	 * 
	 * @param prodSeriersCode
	 * @param planMonth
	 * @return
	 */
	@RequestMapping("/getAddInvenPlanDPage")
	public String getAddInvenPlanDPage(String prodSeriesCode, String planMonth, Model model) {
		model.addAttribute("prodSeriesCode", prodSeriesCode);
		model.addAttribute("planMonth", planMonth);
		return "bam/invenPlan/addPlanDg";
	}

	/**
	 * 获取备货计划分解登录
	 * 
	 * @param mateCode
	 * @param mateDesc
	 * @param deliveryPlan
	 * @return
	 */
	@RequestMapping("/getDecomposePage")
	public String getDecomposePage(String mateCode,String status, String mateDesc, String deliveryPlan, Model model, String mainId,
			String planMonth, String nextMonthDeliveryNum,String planDetailId) {
		
		Material mate = materialService.queryMaterialByMateCode(mateCode);
		model.addAttribute("mateCode", mateCode);
		model.addAttribute("mateDesc", mate.getSkuEnglish());
		model.addAttribute("deliveryPlan", deliveryPlan);
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("mainId", mainId);
		model.addAttribute("status", status);
		model.addAttribute("nextMonthDeliveryNum", nextMonthDeliveryNum);
		model.addAttribute("planDetailId", planDetailId);
		return "bam/invenPlan/decomposeDg";
	}

	@RequestMapping("/getDecomposeViewPage")
	public String getDecomposeViewPage(String mateCode, String mateDesc, String deliveryPlan, Model model,
			String mainId, String planMonth, String nextMonthDeliveryNum,String planDetailId) {
		Material mate = materialService.queryMaterialByMateCode(mateCode);
		model.addAttribute("mateCode", mateCode);
		model.addAttribute("mateDesc", mate.getSkuEnglish());
		model.addAttribute("deliveryPlan", deliveryPlan);
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("mainId", mainId);
		model.addAttribute("nextMonthDeliveryNum", nextMonthDeliveryNum);
		model.addAttribute("planDetailId", planDetailId);

		return "bam/invenPlan/viewDecomposeDg";
	}
	/**
	 * 获取供应物料排产计划
	 * @param model
	 * @param mainId
	 * @param suppNo
	 * @param suppName
	 * @return
	 */
	@RequestMapping("/getSuppMateViewPage")
	public String getSuppMateViewPage(Model model, String mainId,String suppNo,String suppName) {
		model.addAttribute("mainId", mainId);
		model.addAttribute("suppNo", suppNo);
		model.addAttribute("suppName", suppName);
		return "bam/invenPlan/viewSuppMateDg";
	}

	/**
	 * 分页获取备货计划
	 * 
	 * @param planDesc
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getInvenPlanByPage")
	public LayuiPage<InvenPlan> getInvenPlanByPage(String planDesc, String status, String startDate, String endDate,
			LayuiPage<InvenPlan> page,Date planMonth,String createUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		page.calculatePage();
		map.put("page", page);
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownId", userId);
		params.put("orgCode", "");
		params.put("isContainOwn", true);
		List<UserDO> list = orgService.manageSubordinateUsers(params);
		map.put("userId", list);
		if (!StringUtils.isEmpty(planDesc)) {
			map.put("planDesc", "%" + planDesc + "%");
		}
		if (!StringUtils.isEmpty(createUser)) {
			map.put("createUser", "%" + createUser + "%");
		}
		map.put("status", status);
		Date start = DateUtils.parse(startDate, "yyyy-MM-dd");
		Date end = DateUtils.parse(endDate, "yyyy-MM-dd");
		if (start != null) {
			map.put("startDate", start);
		}
		if (end != null) {
			end = DateUtils.addDate(end, 1);
			map.put("endDate", end);
		}
		if (planMonth != null) {
			map.put("planMonth", planMonth);
		}
		LayuiPage<InvenPlan> returnPage = invenPlanService.getInvenPlanByPage(map);
		return returnPage;
	}

	/**
	 * 根据月份获取产品系列
	 * 
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProdSeriers")
	public List<BaseEntity> getProdSeriers(String planMonth,String seriesDesc,String planCode) {
/*		// 获取采购员的系列
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		List<Long> userIds = new ArrayList<Long>();
		if(StringUtils.isEmpty(planCode)){
			userIds.add(userId);
		}else{
			InvenPlan invenPlan = invenPlanService.getInvenPlanByCode(planCode);
			userIds.add(Long.parseLong(invenPlan.getCreater()));
		}
		List<BaseEntity> buyerSeries = invenPlanService.getSeriesByUserId(userIds);
		List<String> seriesCodes=new ArrayList<>();
		for (BaseEntity baseEntity : buyerSeries) {
			if(baseEntity!=null){
				seriesCodes.add(baseEntity.getCode());
			}
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ym", planMonth);
		if(seriesCodes!=null && seriesCodes.size()>0){
			params.put("seriesCodes", seriesCodes);
		}
		if(!StringUtils.isEmpty(seriesDesc)){
			params.put("seriesDesc","%"+ seriesDesc+"%");
		}
		// 获取生产备货计划的系列
		List<BaseEntity> seriers = padPlanService.getMatProdSeriesByYearMonth(params);*/
		Map<String, Object> params=new HashMap<>();
		params.put("planCode", planCode);
		if(!StringUtils.isEmpty(seriesDesc)){
			params.put("seriesDesc","%"+ seriesDesc+"%");
		}
		List<BaseEntity> seriers = invenPlanService.getProdSeriers(params);
		return seriers;
	}

	/**
	 * 根据月份 系列 物料 获取物料信息
	 * 
	 * @param planMonth
	 * @param prodSeriersCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDeliveryPlans")
	public List<InvenPlanDetail> getDeliveryPlans(String planMonth, String prodSeriesCode, String mateDesc) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date month = DateUtils.parse(planMonth, "yyy-MM");
		map.put("prodSeries", prodSeriesCode);
		map.put("planMonth", planMonth);
//		map.put("status", "已提交");
		if (!StringUtils.isEmpty(mateDesc)) {
			map.put("mateName", "%" + mateDesc + "%");
		}
		List<PadPlanDetail> padPlanDetails = padPlanService.getPadPlanDetailByMap(map);
		List<InvenPlanDetail> list = new ArrayList<>();
		for (PadPlanDetail padPlanDetail : padPlanDetails) {
			InvenPlanDetail detail = new InvenPlanDetail();
			// 来自生产交货计划
			map.put("planMonth", month);
			detail.setStatus("未分解");
			detail.setId(UUIDUtil.getUUID());
			detail.setMateCode(padPlanDetail.getMatCode());
			detail.setMateDesc(padPlanDetail.getMatName());
			detail.setRanking(padPlanDetail.getRank());
			detail.setProdSeriesCode(padPlanDetail.getProdSeries());
			detail.setProdSeriesDesc(padPlanDetail.getProdSeries());
			detail.setDeliveryPlan(new BigDecimal(padPlanDetail.getPadPlanQty()));
			detail.setPlanMonth(month);
			// 获取期初订单数据 期初库存(有疑问)
			map.put("mateCode", detail.getMateCode());
			Double unpaidNum = orderService.getUnpaidNum(map);
			BigDecimal beginOrder = new BigDecimal(unpaidNum);
			Float nationStock1 = padPlanDetail.getNationStock1();
			BigDecimal beginStock = new BigDecimal(nationStock1 == null ? 0 : nationStock1);
			detail.setBeginOrder(beginOrder);
			detail.setBeginStock(beginStock);
			detail.setBeginEnableOrder(beginOrder.subtract(beginStock));
			// 获取下一个月的交货计划（有疑问）
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(month);
			calendar.add(Calendar.MONTH, 1);
			map.put("planMonth", DateUtils.format(new Date(calendar.getTimeInMillis()), "yyyy-MM"));
			map.put("status", "已提交");
			List<PadPlanDetail> padDetails = padPlanService.getPadPlanDetailByMap(map);
			if (padDetails != null && padDetails.size() > 0) {
				PadPlanDetail padDetail = padDetails.get(0);
				if (padDetail != null) {
					Float padPlanQty = padDetail.getPadPlanQty();
					detail.setNextMonthDeliveryNum(new BigDecimal(padPlanQty == null ? 0 : padPlanQty));
				}
			}
			list.add(detail);
		}
		return list;
	}

	/**
	 * 保存供应商备货计划
	 * 
	 * @param type
	 * @param invenPlan
	 * @param detailJson
	 * @return
	 */
	@Log("保存备货计划")
	@ResponseBody
	@RequestMapping("/saveInvenPlan")
	public RestCode saveInvenPlan(String type, InvenPlan invenPlan, String detailJson) {
		List<InvenPlanDetail> details = JsonUtils.jsonToList(detailJson, InvenPlanDetail.class);
		if (!StringUtils.isEmpty(type) && type.equals("1")) {
			String code = codeService.getCodeByCodeType("InvenPlanNo");
			invenPlan.setPlanCode(code);
			invenPlanService.saveInvenPlan(invenPlan, details);
		} else {
			invenPlanService.updateInvenPlan(invenPlan, details);
		}
		return new RestCode().put("data", invenPlan);
	}

	/**
	 * 分解排产
	 * 
	 * @param mateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppByMateCode")
	public List<SuppProd> getSuppByMateCode(String mateCode, String mateDesc, String mainId, String planMonth,
			String deliveryPlanStr,String planDetailId,String status) {
		// 获取当前物料的供应商
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		//获取单据的创建人ID
		InvenPlanDetail planDetail = invenPlanService.getPlanDetailById(planDetailId);
		if(planDetail!=null){
			String planCode = planDetail.getPlanCode();
			InvenPlan invenPlan = invenPlanService.getInvenPlanByCode(planCode);
			if(invenPlan!=null){
				try {
					userId=Long.parseLong(invenPlan.getCreater());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		Date month = DateUtils.parse(planMonth, "yyyy-MM");
		String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
		Material mate = materialService.queryMaterialByMateCode(mateCode);

		if(status!=null && "已分解".equals(status)){
			Map<String, Object> params=new HashMap<>();
			params.put("buyerId", userId);
			params.put("mateCode", mateCode);
			List<QualSupp> supps = qualSuppService.queryQualSuppOfMateByMateCode(params);
			invenPlanService.deleteSuppProdByQualSupp(planDetailId, supps);
			List<SuppProd> suppProds = suppProdService.getSuppProdByPlanDetailId(planDetailId);
			for (QualSupp qualSupp : supps) {
				SuppProd oldSuppProd=null;
				String sapId = qualSupp.getSapId();
				String suppAbbre = qualSupp.getSuppAbbre();
				//默认不包含改供应商
				boolean flag=false;
				for (SuppProd suppProd : suppProds) {
					String suppNo = suppProd.getSuppNo();
					if(suppNo.equals(sapId)){
						flag=true;
						oldSuppProd=suppProd;
					}
				}
				//不包含的情况下 
				if(!flag){
					Map<String, Object> map = new HashMap<String, Object>();
					SuppProd suppProd = new SuppProd();
					String suppNo = qualSupp.getSapId();
					suppProd.setSuppNo(suppNo);
					suppProd.setSuppName(suppAbbre);
					suppProd.setPlanMonth(month);
					suppProd.setMainId(mainId);
					suppProd.setPlanDetailId(planDetailId);
					suppProd.setMateCode(mateCode);
					suppProd.setMateDesc(mate.getSkuEnglish());
					suppProd.setMainId(mainId);
					suppProd.setDeliveryPlan(new BigDecimal(0));

					// 获取期初订单（当前月的话取上个月的未完成订单否则有疑问）
					map.put("suppNo", suppNo);
					map.put("mateCode", mateCode);
					map.put("planMonth", month);
					Double undeliveredNum = orderMonthService.selectUndeliveredNumByMap(map);
//					Double unpaidNum = orderService.getUnpaidNum(map);
					suppProd.setBeginOrder(new BigDecimal(undeliveredNum));
					// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
					BigDecimal beginStock = new BigDecimal(0);
					if (currMonthStr.equals(planMonth) || month.before(new Date())) {
						PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
						if (pdrDetail != null && pdrDetail.getStockQty() != null) {
							beginStock = new BigDecimal(pdrDetail.getStockQty());
						}
					} else {
						Calendar cal = Calendar.getInstance();
						cal.setTime(month);
						cal.add(Calendar.MONTH, -1);
						map.put("planMonth", cal.getTime());
						List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
						if (prodList != null && prodList.size() > 0) {
							SuppProd suppProd2 = prodList.get(0);
							if (suppProd2 != null && suppProd2.getEndStock() != null) {
								beginStock = suppProd2.getEndStock();
							}
						}
					}
					suppProd.setBeginStock(beginStock);
					suppProd.setStatus("未排产");
					suppProds.add(suppProd);
				}else{
					String suppNo = oldSuppProd.getSuppNo();
					//更新原来的期初订单  期初库存 期初可生产订单 期末预计库存  安全库存率
					Map<String, Object> map = new HashMap<String, Object>();
					// 获取期初订单（当前月的话取上个月的未完成订单否则有疑问）
					map.put("suppNo", suppNo);
					map.put("mateCode", mateCode);
					map.put("planMonth", month);
					Double undeliveredNum = orderMonthService.selectUndeliveredNumByMap(map);
					oldSuppProd.setBeginOrder(new BigDecimal(undeliveredNum));
					// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
					BigDecimal beginStock = new BigDecimal(0);
					if (currMonthStr.equals(planMonth) || month.before(new Date())) {
						PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
						if (pdrDetail != null && pdrDetail.getStockQty() != null) {
							beginStock = new BigDecimal(pdrDetail.getStockQty());
						}
					} else {
						Calendar cal = Calendar.getInstance();
						cal.setTime(month);
						cal.add(Calendar.MONTH, -1);
						map.put("planMonth", cal.getTime());
						List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
						if (prodList != null && prodList.size() > 0) {
							SuppProd suppProd2 = prodList.get(0);
							if (suppProd2 != null && suppProd2.getEndStock() != null) {
								beginStock = suppProd2.getEndStock();
							}
						}
					}
					oldSuppProd.setBeginStock(beginStock);
					oldSuppProd.setSuppName(suppAbbre);
					BigDecimal prodPlan = oldSuppProd.getProdPlan();
					BigDecimal deliveryPlan = oldSuppProd.getDeliveryPlan();
					BigDecimal nextDeliveryNum = oldSuppProd.getNextDeliveryNum();
					BigDecimal endStock = BigDecimalUtil.subtract(BigDecimalUtil.add(beginStock, prodPlan), deliveryPlan);
					oldSuppProd.setEndStock(endStock);
					oldSuppProd.setSafeScale(BigDecimalUtil.getPercentage(endStock, nextDeliveryNum));
				}
			}
			return suppProds;
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			List<SuppProd> list = new ArrayList<>();
			BigDecimal deliveryPlan = new BigDecimal(0);
			if (!StringUtils.isEmpty(deliveryPlanStr)) {
				deliveryPlan = new BigDecimal(deliveryPlanStr);
			}
			// 获取当前物料的供应商
			Map<String, Object> params=new HashMap<>();
			params.put("buyerId", userId);
			params.put("mateCode", mateCode);
			List<QualSupp> supps = qualSuppService.queryQualSuppOfMateByMateCode(params);

			int size = supps.size();
			if (size == 0) {
				return list;
			}
			// 平均分配排产计划
			BigDecimal[] divideAndRemainder = deliveryPlan.divideAndRemainder(new BigDecimal(size));
			BigDecimal divideNum = divideAndRemainder[0];
			BigDecimal remianNum = divideAndRemainder[1];

			for (int i = 0; i < size; i++) {
				QualSupp qualSupp = supps.get(i);
				SuppProd suppProd = new SuppProd();
				String suppNo = qualSupp.getSapId();
				suppProd.setSuppNo(suppNo);
				suppProd.setSuppName(qualSupp.getSuppAbbre());
				suppProd.setPlanMonth(month);
				suppProd.setMainId(mainId);
				suppProd.setPlanDetailId(planDetailId);
				suppProd.setMateCode(mateCode);
				suppProd.setMateDesc(mate.getSkuEnglish());
				// 分配交货计划
				if (i == size - 1) {
					suppProd.setDeliveryPlan(divideNum.add(remianNum));
				} else {
					suppProd.setDeliveryPlan(divideNum);
				}
				suppProd.setMainId(mainId);
				// 获取期初订单（当前月的话取上个月的未完成订单否则有疑问）
				map.put("suppNo", suppNo);
				map.put("mateCode", mateCode);
				map.put("planMonth", month);
				Double undeliveredNum = orderMonthService.selectUndeliveredNumByMap(map);
//				Double unpaidNum = orderService.getUnpaidNum(map);
				suppProd.setBeginOrder(new BigDecimal(undeliveredNum));
				// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
				BigDecimal beginStock = new BigDecimal(0);
				if (currMonthStr.equals(planMonth) || month.before(new Date())) {
					PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
					if (pdrDetail != null && pdrDetail.getStockQty() != null) {
						beginStock = new BigDecimal(pdrDetail.getStockQty());
					}
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(month);
					cal.add(Calendar.MONTH, -1);
					map.put("planMonth", cal.getTime());
					List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
					if (prodList != null && prodList.size() > 0) {
						SuppProd suppProd2 = prodList.get(0);
						if (suppProd2 != null && suppProd2.getEndStock() != null) {
							beginStock = suppProd2.getEndStock();
						}
					}
				}
				suppProd.setBeginStock(beginStock);
				suppProd.setStatus("未排产");
				list.add(suppProd);
			}
			return list;
		}
	}

	/**
	 * 查询备货计划在详情中某些状态的数量
	 * 
	 * @param jsonIds
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getStatusNumsByMainIds")
	public Integer getStatusNumsByMainIds(String jsonIds, String status) {
		List<String> ids = JsonUtils.jsonToList(jsonIds, String.class);
		Integer count = invenPlanService.getStatusNumsByMainIds(ids, status);
		return count;
	}

	/**
	 * 根据Id更改备货计划状态
	 * 
	 * @param jsonIds
	 * @param stautus
	 * @return
	 */
	@Log("变更备货计划状态")
	@ResponseBody
	@RequestMapping("/changeInvenPlanStatus")
	public RestCode changeInvenPlanStatus(String jsonIds, String status) {
		List<String> ids = JsonUtils.jsonToList(jsonIds, String.class);
		invenPlanService.changeInvenPlanStatus(ids, status);
		return new RestCode();
	}

	/**
	 * 取消分解
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Log("撤销备货计划分解")
	@ResponseBody
	@RequestMapping("/cancleDecompose")
	public RestCode cancleDecompose(String id, String status) {
		invenPlanService.cancleDecompose(id, status);
		return new RestCode();
	}

	/**
	 * 删除备货计划
	 * 
	 * @param jsonIds
	 * @return
	 */
	@Log("删除备货计划")
	@ResponseBody
	@RequestMapping("/delInvenPlan")
	public RestCode delInvenPlan(String jsonIds) {
		List<String> ids = JsonUtils.jsonToList(jsonIds, String.class);
		invenPlanService.delInvenPlan(ids);
		return new RestCode();
	}

	/**
	 * 查看某个物料是否存在备货计划
	 * 
	 * @param planCode
	 * @param planMonth
	 * @param mateCodeJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/isExist")
	public RestCode isExist(String planCode, Date planMonth, String mateCodeJson) {
		Map<String, Object> map = new HashMap<>();
		List<String> mateCodes = JsonUtils.jsonToList(mateCodeJson, String.class);
		map.put("planMonth", planMonth);
		map.put("planCode", planCode);
		if (mateCodes == null || mateCodes.size() == 0) {
			return new RestCode();
		}
		map.put("mateCodes", mateCodes);
		RestCode restCode = invenPlanService.isExistMonthMates(map);
		return restCode;
	}

	/**
	 * 根据月份初始化备货计划详情数据
	 * 
	 * @param planMonth
	 * @return
	 */
	@RequestMapping("/initalPlanDetailsData")
	@ResponseBody
	public List<SuppProd> initalPlanDetailsData(String planMonth, String mainId) {
		List<SuppProd> list = invenPlanService.initalPlanDetailsData(planMonth, mainId);
		return list;
	}

	/**
	 * 保存供应商备货计划
	 * 
	 * @param type
	 * @param invenPlan
	 * @param detailJson
	 * @return
	 */
	@Log("保存备货计划")
	@ResponseBody
	@RequestMapping("/saveInvenPlanInfo")
	public RestCode saveInvenPlanInfo(String type, InvenPlan invenPlan, String detailJson) {
		List<SuppProd> details = JsonUtils.jsonToList(detailJson, SuppProd.class);
		RestCode restCode = invenPlanService.saveInvenPlanInfo(invenPlan, details);
		return restCode.put("data", invenPlan);
	}

	/**
	 * 根据计划主表的Id获取备货计划详情
	 * 
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPlanDetailByMainId")
	public List<SuppProd> getPlanDetailByMainId(String mainId, String itemInfo, String suppNosJson,
			String mateCodeJson,String seriesCodeJson ) {
		Map<String, Object> map = new HashMap<>();
		map.put("mainId", mainId);
		if (!StringUtils.isEmpty(itemInfo)) {
			List<String> itemInfos = JsonUtils.jsonToList(itemInfo, String.class);
			if (itemInfos != null && itemInfos.size() > 0) {
				map.put("itemInfos", itemInfos);
			}
		}
		if (!StringUtils.isEmpty(suppNosJson)) {
			List<String> suppNos = JsonUtils.jsonToList(suppNosJson, String.class);
			if (suppNos != null && suppNos.size() > 0) {
				map.put("suppNos", suppNos);
			}
		}
		if (!StringUtils.isEmpty(seriesCodeJson)) {
			List<String> seriesCodes = JsonUtils.jsonToList(seriesCodeJson, String.class);
			if (seriesCodes != null && seriesCodes.size() > 0) {
				map.put("seriesCodes", seriesCodes);
			}
		}
		if (!StringUtils.isEmpty(mateCodeJson)) {
			List<String> mateCodes = JsonUtils.jsonToList(mateCodeJson, String.class);
			if (mateCodes != null && mateCodes.size() > 0) {
				map.put("mateCodes", mateCodes);
			}
		}
		List<SuppProd> list = invenPlanService.getPlanDetailByMainId(map);
		return list;
	}

	/**
	 * 按物料获取报表
	 * 
	 * @param planMonth
	 * @return
	 */
	@RequestMapping("/getReportByMate")
	@ResponseBody
	public List<SuppProd> getReportByMate(SuppProd suppProd) {
		SysUserDO user = UserCommon.getUser();
		List<SuppProd> list = invenPlanService.getReportByMate(suppProd, user.getUserId());
		return list;
	}

	/**
	 * 按照供应商取报表
	 * 
	 * @param suppProd
	 * @return
	 */
	@RequestMapping("/getReportBySupp")
	@ResponseBody
	public List<SuppProd> getReportBySupp(String mainId,String selectSuppCodes) {
		Map<String, Object> params=new HashMap<>();
		params.put("mainId", mainId);
		List<String> suppNos = JsonUtils.jsonToList(selectSuppCodes, String.class);
		if(suppNos!=null && suppNos.size()>0){
			params.put("suppNos", suppNos);
		}
		return invenPlanService.getReportBySupp(params);
	}

	/**
	 * 校验是否重复
	 * 
	 * @param planMonth
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/checkRepeat")
	@ResponseBody
	public RestCode checkRepeat(InvenPlan plan) {
		int i = invenPlanService.countInvenPlanByIdAndPlanMonth(plan);
		if (i == 0)
			return RestCode.ok();
		return RestCode.error();
	}

	// 领导审批开始
	@Log("获取备货计划审核列表")
	@RequestMapping("/leaderIndex")
	public String leaderIndex() {
		return "bam/invenPlan/invenPlanLeader";
	}

	/**
	 * 领导审核列表
	 * 
	 * @param planDesc
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	@RequestMapping("/getInvenPlanAuditByPage")
	@ResponseBody
	public LayuiPage<InvenPlan> getInvenPlanAuditByPage(String planDesc, String status, String startDate, String endDate,
			LayuiPage<InvenPlan> page) {
		Map<String, Object> map = new HashMap<String, Object>();
		page.calculatePage();
		map.put("page", page);
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownId", userId);
		params.put("orgCode", "");
		params.put("isContainOwn", true);
		List<UserDO> list = orgService.manageSubordinateUsers(params);
		map.put("userId", list);
		if (!StringUtils.isEmpty(planDesc)) {
			map.put("planDesc", "%" + planDesc + "%");
		}
		map.put("status", status);
		Date start = DateUtils.parse(startDate, "yyyy-MM-dd");
		Date end = DateUtils.parse(endDate, "yyyy-MM-dd");
		if (startDate != null) {
			map.put("startDate", start);
		}
		if (end != null) {
			end = DateUtils.addDate(end, 1);
			map.put("endDate", end);
		}
		map.put("auditStatus", "已保存");
		LayuiPage<InvenPlan> returnPage = invenPlanService.getInvenPlanByPage(map);
		return returnPage;
	}

	/**
	 * 初始化采购组长审核的备货计划详情数据
	 * 
	 * @param planMonth
	 * @return
	 */
	@RequestMapping("getPlanDetailForLeader")
	@ResponseBody
	public List<SuppProd> getPlanDetailForLeader(String planMonth) {
		List<SuppProd> list = invenPlanService.getPlanDetailForLeader(planMonth);
		return list;
	}

	/**
	 * 采购组长审批保存
	 * 
	 * @param type
	 * @param invenPlan
	 * @param detailJson
	 * @return
	 */
	@Log("采购组长审批保存")
	@RequestMapping("/saveInvenPlanAuditInfo")
	@ResponseBody
	public RestCode saveInvenPlanAuditInfo(String type, InvenPlan invenPlan, String detailJson) {
		List<SuppProd> details = JsonUtils.jsonToList(detailJson, SuppProd.class);
		RestCode restCode = invenPlanService.saveInvenPlanAuditInfo(invenPlan, details);
		return restCode.put("data", invenPlan);
	}
	
	//---------------生产交货计划检索界面开始-------------
	/**
	 * 获取物料检索的对话框
	 * @param selectMateCodes
	 * @param model
	 * @param planMonth
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/getMateSearchDg")
	public String getMateSearchDg(String selectMateCodes,Model model,String planMonth,String planCode){
		model.addAttribute("selectMateCodes", selectMateCodes);
		model.addAttribute("planCode", planCode);
		model.addAttribute("planMonth", planMonth);
		return "bam/invenPlan/mateSearchDg";
	}
	/**
	 * 获取系列检索的对话框
	 * @param selectSeriesCodes
	 * @param model
	 * @param planMonth
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/getSeriesSearchDg")
	public String getSeriesSearchDg(String selectSeriesCodes,Model model,String planMonth,String planCode){
		model.addAttribute("selectSeriesCodes", selectSeriesCodes);
		model.addAttribute("planCode", planCode);
		model.addAttribute("planMonth", planMonth);
		return "bam/invenPlan/seriesSearchDg";
	}
	/**
	 * 获取供应商检索的对话框
	 * @param selectSuppCodes
	 * @param model
	 * @param planMonth
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/getSuppSearchDg")
	public String getSuppSearchDg(String selectSuppCodes,Model model,String planMonth,String planCode){
		model.addAttribute("selectSuppCodes", selectSuppCodes);
		model.addAttribute("planCode", planCode);
		model.addAttribute("planMonth", planMonth);
		return "bam/invenPlan/suppSearchDg";
	}
	/**
	 * 获取管理的供应商列表
	 * @param suppName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppList")
	public  List<QualSupp> getSuppList(String suppName,String planCode,String planMonth){
/*		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		Map<String, Object> map = new HashMap<>();
		List<Long> userIds = new ArrayList<Long>();
		if(StringUtils.isEmpty(planCode)){
			userIds.add(userId);
		}else{
			InvenPlan invenPlan = invenPlanService.getInvenPlanByCode(planCode);
			userIds.add(Long.parseLong(invenPlan.getCreater()));
		}
		map.put("userIds", userIds);
		if(!StringUtils.isEmpty(suppName)){
			map.put("suppName", "%"+suppName+"%");
		}
		
//		map.put("userIds", userIds);
//		map.put("planMonth", planMonth);
		// 获取物料信息
		//List<MateDO> mateList = materialService.queryPrdPlanMateListOfUsers(map);
		
		
		List<QualSupp> suppList = qualSuppService.queryAllQualSuppListOfUsers(map);*/
		
		Map<String, Object> map = new HashMap<>();
		map.put("planCode", planCode);
		if(!StringUtils.isEmpty(suppName)){
			map.put("suppName", "%"+suppName+"%");
		}
		List<QualSupp> suppList = invenPlanService.getSuppList(map);
		return suppList;
	}
	@RequestMapping("/geItemSearchDg")
	public String geItemSearchDg(String selectItemCodes,Model model,String planMonth,String planCode){
		model.addAttribute("selectItemCodes", selectItemCodes);
		model.addAttribute("planCode", planCode);
		model.addAttribute("planMonth", planMonth);
		return "bam/invenPlan/itemSearchDg";
	}
	
	/**
	 * 获取品项列表
	 * @param itemDesc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getItemList")
	public  List<Dic> getItemList(String itemDesc,String planCode){
/*		Map<String, Object> map=new HashMap<>();
		map.put("cateCode", "ItemInfo");
		if(!StringUtils.isEmpty(itemDesc)){
			map.put("dicName", "%"+itemDesc+"%");
		}
		List<Dic> ItemList = basicService.findDicListByCategoryCode(map);*/
		Map<String, Object> map=new HashMap<>();
		map.put("planCode", planCode);
		if(!StringUtils.isEmpty(itemDesc)){
			map.put("itemDesc", "%"+itemDesc+"%");
		}
		List<Dic> itemList = invenPlanService.getItemList(map);
		return itemList;
	}
	
	//---------------生产交货计划检索界面结束-------------
	
	//------------------------根据汇总生成备货计划详情开始3-----------------------------
	/**
	 * 初始化备货计划的物料汇总生成
	 * @param planMonth
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/initPlanDetailsData")
	@ResponseBody
	public List<InvenPlanDetail> initPlanDetailsData(String planMonth) {
		List<InvenPlanDetail> list = invenPlanService.initPlanDetailsData(planMonth);
		return list;
	}	
	/**
	 * 根据计划主表的Id获取备货计划详情
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatePlanDetailByMainId")
	public List<InvenPlanDetail> getMatePlanDetailByMainId(String mainId, String itemInfo, String suppNosJson,
			String mateCodeJson,String seriesCodeJson ) {
		Map<String, Object> map = new HashMap<>();
		map.put("mainId", mainId);
		if (!StringUtils.isEmpty(itemInfo)) {
			List<String> itemInfos = JsonUtils.jsonToList(itemInfo, String.class);
			if (itemInfos != null && itemInfos.size() > 0) {
				map.put("itemInfos", itemInfos);
			}
		}
		if (!StringUtils.isEmpty(seriesCodeJson)) {
			List<String> seriesCodes = JsonUtils.jsonToList(seriesCodeJson, String.class);
			if (seriesCodes != null && seriesCodes.size() > 0) {
				map.put("seriesCodes", seriesCodes);
			}
		}
		if (!StringUtils.isEmpty(mateCodeJson)) {
			List<String> mateCodes = JsonUtils.jsonToList(mateCodeJson, String.class);
			if (mateCodes != null && mateCodes.size() > 0) {
				map.put("mateCodes", mateCodes);
			}
		}
		List<InvenPlanDetail> list = invenPlanService.getMatePlanDetailByMainId(map);
		return list;
	}
	/**
	 * 获取物料下拉列表框数据源
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMateSelectInfo")
	public List<MateDO> getMateSelectInfo(String planMonth,String mateDesc,String  planCode){
		// 获取管理的采购员列表
		Map<String, Object> map = new HashMap<>();
		map.put("planCode", planCode);
		if(!StringUtils.isEmpty(mateDesc)){
			map.put("mateDesc", "%"+mateDesc+"%");
		}
		List<MateDO> mateList = invenPlanService.getMateSelectInfo(map);
		return mateList;
	}

	/**
	 * 根据月份获取产品系列
	 * 
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSelectProdSeriers")
	public List<BaseEntity> getSelectProdSeriers(String planMonth,String seriesDesc,String planCode) {
		Map<String, Object> params=new HashMap<>();
		params.put("planCode", planCode);
		if(!StringUtils.isEmpty(seriesDesc)){
			params.put("seriesDesc","%"+ seriesDesc+"%");
		}
		List<BaseEntity> seriers = invenPlanService.getSelectProdSeriers(params);
		return seriers;
	}
	
	/**
	 * 获取品项列表
	 * @param itemDesc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getItemInfo")
	public  List<Dic> getItemInfo(String itemDesc,String planCode){
		Map<String, Object> map=new HashMap<>();
		map.put("planCode", planCode);
		if(!StringUtils.isEmpty(itemDesc)){
			map.put("itemDesc", "%"+itemDesc+"%");
		}
		List<Dic> itemList = invenPlanService.getItemInfo(map);
		return itemList;
	}
	
	/**
	 * 根据供应商编码获取供应商的备货计划
	 * @param suppNo
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdBySuppNo")
	public List<SuppProd> getSuppProdBySuppNo(String suppNo,String mainId) {
		Map<String, Object> params =new HashMap<>();
		params.put("suppNo", suppNo);
		params.put("mainId", mainId);
		return invenPlanService.getSuppProdBySuppNo(params);
	}
	
	
	//------------------------根据汇总生成备货计划详情结束3-----------------------------
	@RequestMapping("/getInvenPlanTaskAuditHtml")
	public String getInvenPlanTaskAuditHtml(Model model,TaskParamsDO taskPDO){
        InvenPlan invenPlan = invenPlanService.getInvenPlanById(taskPDO.getSdata1());
        // 方式
 		model.addAttribute("type", "2");
 		// 获取套袋信息下拉框数据源
 		List<Dic> ItemList = basicService.findDicListByCategoryCode("ItemInfo");
 		model.addAttribute("ItemList", ItemList);
 		// 获取管理的采购员列表
 		SysUserDO user = UserCommon.getUser();
 		Long userId = user.getUserId();
 		Map<String, Object> map = new HashMap<>();
 		map.put("ownId", userId);
 		// map.put("orgCode", "PURCHAROR");
 		map.put("isContainOwn", true);
 		List<UserDO> users = orgService.manageSubordinateUsers(map);
 		List<Long> userIds = new ArrayList<Long>();
 		for (UserDO userDo : users) {
 			userIds.add(userDo.getId());
 		}
 		map.put("userIds", userIds);
 		String planMonth = DateUtils.format(invenPlan.getPlanMonth(), "yyyy-MM");
 		map.put("planMonth",planMonth);
 		// 获取物料信息
 		List<MateDO> mateList = materialService.queryPrdPlanMateListOfUsers(map);
        // 获取
 		model.addAttribute("mateList", mateList);
 		// 获取供应商信息
 		List<QualSupp> suppList = qualSuppService.queryAllQualSuppListOfUsers(map);
 		model.addAttribute("suppList", suppList);
	    model.addAttribute("invenPlan", invenPlan);
		model.addAttribute("taskPDO", taskPDO);
		return "bam/invenPlan/invenPlanTaskAudit";
	}
	//------------------------------获取已确认的备货计划--------------
	@Log("获取已审核备货计划列表信息")
	@RequestMapping("/getConfirmedIndex")
	public String getConfirmedIndex() {
		return "bam/invenPlan/confirmedIndex";
	}	
	/**
	 * 已审核
	 * 
	 * @param planDesc
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getConfirmedByPage")
	public LayuiPage<InvenPlan> getConfirmedByPage(String planDesc, String status, String startDate, String endDate,
			LayuiPage<InvenPlan> page,Date planMonth,String createUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		page.calculatePage();
		map.put("page", page);		
		if (!StringUtils.isEmpty(planDesc)) {
			map.put("planDesc", "%" + planDesc + "%");
		}
		if (!StringUtils.isEmpty(createUser)) {
			map.put("createUser", "%" + createUser + "%");
		}
		map.put("status", status);
		Date start = DateUtils.parse(startDate, "yyyy-MM-dd");
		Date end = DateUtils.parse(endDate, "yyyy-MM-dd");
		if (start != null) {
			map.put("startDate", start);
		}
		if (end != null) {
			end = DateUtils.addDate(end, 1);
			map.put("endDate", end);
		}
		if (planMonth != null) {
			map.put("planMonth", planMonth);
		}
		LayuiPage<InvenPlan> returnPage = invenPlanService.getInvenPlanByPage(map);
		return returnPage;
	}
	
	//------------------------------获取已确认的备货计划--------------
	
	//------------------------------备货计划数据的更新开始----------------------
	@ResponseBody
	@RequestMapping("/updateInvenMate")
	public RestCode updateInvenMate(String planCode){
		invenPlanService.updateInvenMate(planCode);
		return new RestCode();
	}
	//------------------------------备货计划数据的更新结束----------------------
	
	//------------------------------备货计划分解数据的导入开始----------------------
	/**
	 * 导出备货计划模板
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/exportTemp")
	public  String exportTemp(String planCode, Date planMonth, HttpServletResponse response) {
		String planMonthStr = DateUtils.format(planMonth, "yyyyMM");
		// 获取需要导出的分解的物料供应商数据
		List<SuppProdVo> suppProdVos = invenPlanService.getExportTempData(planCode);
		// 创建HSSFWorkbook对象(excel的文档对象)
		Workbook wb = null;
		OutputStream os = null;
		FileInputStream fis = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(planMonth);
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath + "templates\\excelTemp\\备货计划导入模板.xlsx";
			fis = new FileInputStream(realPath);
			wb = new XSSFWorkbook(fis);
			// 建立新的sheet对象（excel的表单）
			Sheet sheet = wb.getSheetAt(0);
			
	  		Font font = wb.createFont();
			font.setBold(true);
			font.setFontName("微软雅黑");
			font.setFontHeightInPoints((short)9);
			// 获取样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 2, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 2, 1);
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 2, 2);
			CellStyle cellStyle4 = ExcelUtil.getCellStyle(sheet, 2, 3);
			CellStyle cellStyle5 = ExcelUtil.getCellStyle(sheet, 2, 4);
			CellStyle cellStyle6 = ExcelUtil.getCellStyle(sheet, 2, 5);
			CellStyle cellStyle7 = ExcelUtil.getCellStyle(sheet, 2, 6);
			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 2, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 2, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 2, 9);
			CellStyle cellStyle11 = ExcelUtil.getCellStyle(sheet, 2, 10);
			CellStyle cellStyle12 = ExcelUtil.getCellStyle(sheet, 2, 11);
			CellStyle cellStyle13 = ExcelUtil.getCellStyle(sheet, 2, 12);
			CellStyle cellStyle14 = ExcelUtil.getCellStyle(sheet, 2, 13);
			CellStyle cellStyle15 = ExcelUtil.getCellStyle(sheet, 2, 14);
			CellStyle cellStyle16 = ExcelUtil.getCellStyle(sheet, 2, 15);
			CellStyle cellStyle17 = ExcelUtil.getCellStyle(sheet, 2, 16);
			CellStyle cellStyle18 = ExcelUtil.getCellStyle(sheet, 2, 17);
			CellStyle cellStyle19 = ExcelUtil.getCellStyle(sheet, 2, 18);
			CellStyle cellStyle20 = ExcelUtil.getCellStyle(sheet, 2, 19);

	
			CellStyle cellStyle66 = ExcelUtil.getCellStyle(sheet, 2, 65);
			CellStyle cellStyle67 = ExcelUtil.getCellStyle(sheet, 2, 66);
			CellStyle cellStyle68 = ExcelUtil.getCellStyle(sheet, 2, 67);

			CellStyle cellStyle70 = ExcelUtil.getCellStyle(sheet, 2, 69);
			CellStyle cellStyle71 = ExcelUtil.getCellStyle(sheet, 2, 70);
			CellStyle cellStyle72 = ExcelUtil.getCellStyle(sheet, 2, 71);

			Row row1 = sheet.getRow(0);
			// 创建单元格并设置单元格内容
			// 当月数据
			row1.getCell(9).setCellValue(planMonthStr);
			// 下1个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(16).setCellValue(planMonthStr);

			// 下2个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(20).setCellValue(planMonthStr);
			// 下3个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(24).setCellValue(planMonthStr);

			// 下4个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(28).setCellValue(planMonthStr);

			// 下5个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(32).setCellValue(planMonthStr);

			// 下6个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(36).setCellValue(planMonthStr);

			// 下7个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(40).setCellValue(planMonthStr);
			// 下8个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(44).setCellValue(planMonthStr);

			// 下9个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(48).setCellValue(planMonthStr);

			// 下10个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(52).setCellValue(planMonthStr);

			// 下11个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(56).setCellValue(planMonthStr);

			// 下12个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(60).setCellValue(planMonthStr);
			// 下12个月生产交货预测
	
			Calendar cal1=Calendar.getInstance();
			cal1.setTime(planMonth);
			cal1.add(Calendar.MONTH, -1);
			row1.getCell(70).setCellValue( DateUtils.format(cal1.getTime(), "MM")+"月底全国库存");

			int suppProdSize = suppProdVos.size();
			int mateIndex = suppProdVos.size()+3;
			int mateAndSumCount = 0;
			// 在sheet里创建第三行
			//获取临时变量
			SuppProdVo suppProdVo = suppProdVos.get(0);
			String mateCode = suppProdVo.getMateCode();
			String itemCode = suppProdVo.getItemCode();

			//供应商物料合计的开始于结束   用于计算物料的合计
			int mateBegin=3;
			int mateEnd=3;
			//供应商物料合计的开始于结束   用于计算品项的合计
			int subBegin=mateIndex+1;
			int subEnd=mateIndex+1;
			Map<String, List<SuppVo>> suppSubMap=new HashMap<>();
			for (int i = 0; i < suppProdSize; i++) {
				SuppProdVo suppProdVo2 = suppProdVos.get(i);
				int rowNum = 2 + i;
				String mateCode2 = suppProdVo2.getMateCode();
				String itemCode2 = suppProdVo2.getItemCode();
				//------供应商合计相关数据(存放供应商对应的系列)开始
				String itemName2 = suppProdVo2.getItemName();
				String suppNo = suppProdVo2.getSuppNo();
				String suppName = suppProdVo2.getSuppName();
				List<SuppVo> list = suppSubMap.get(suppNo);
				if(list==null){
					list=new ArrayList<>();
				}
				
				SuppVo supp=new SuppVo();
				supp.setSuppNo(suppNo);
				supp.setSuppName(suppName);
				supp.setItemCode(itemCode2);
				supp.setItemName(itemName2);
				if(!list.contains(supp)){
					list.add(supp);
				}
				suppSubMap.put(suppNo, list);
				//------供应商合计相关数据(存放供应商对应的系列)结束

				if (!mateCode2.equals(mateCode)) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					mateEnd=i+2;
					if(i == suppProdVos.size() - 1){
						mateEnd=i+2;
					}
					// 公共和当月数据
					String itemName=suppProdVo.getItemName();
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName, cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo.getRank(), cellStyle6);
					ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo.getMateDesc(), cellStyle7);
					ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo.getBoxNumber(), cellStyle8);
					ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo.getPackNumber(), cellStyle9);
					
					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+mateBegin+":J"+mateEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+mateBegin+":K"+mateEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+mateBegin+":L"+mateEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+mateBegin+":M"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+mateBegin+":N"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+mateBegin+":O"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+mateBegin+":Q"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+mateBegin+":R"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+mateBegin+":S"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);

					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+mateBegin+":U"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+mateBegin+":V"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+mateBegin+":W"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+mateBegin+":Y"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+mateBegin+":Z"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+mateBegin+":AA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+mateBegin+":AC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+mateBegin+":AD"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+mateBegin+":AE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+mateBegin+":AG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+mateBegin+":AH"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+mateBegin+":AI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+mateBegin+":AK"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+mateBegin+":AL"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+mateBegin+":AM"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+mateBegin+":AO"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+mateBegin+":AP"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+mateBegin+":AQ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+mateBegin+":AS"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+mateBegin+":AT"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+mateBegin+":AU"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+mateBegin+":AW"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+mateBegin+":AX"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+mateBegin+":AY"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+mateBegin+":BA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+mateBegin+":BB"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+mateBegin+":BC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+mateBegin+":BE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+mateBegin+":BF"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+mateBegin+":BG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+mateBegin+":BI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+mateBegin+":BJ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+mateBegin+":BK"+mateEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo.getSumPlanDlvNum()), cellStyle71);
					
					
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, suppProdVo.getNationStock(), cellStyle71);

					mateCode=mateCode2;
					mateBegin=i+3;
					if((itemCode==null && itemCode==null )||(itemCode!=null && itemCode2!=null && (itemCode2.equals(itemCode)))){
						suppProdVo=suppProdVo2;
					}
				}
				if (i == suppProdVos.size() - 1) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					mateEnd=i+2;
					if(i == suppProdVos.size() - 1){
						mateEnd=i+3;
					}
					// 公共和当月数据
					String itemName=suppProdVo2.getItemName();
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo2.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName, cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo2.getRank(), cellStyle6);
					ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo2.getMateDesc(), cellStyle7);
					ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo2.getBoxNumber(), cellStyle8);
					ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo2.getPackNumber(), cellStyle9);
					
					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+mateBegin+":J"+mateEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+mateBegin+":K"+mateEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+mateBegin+":L"+mateEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+mateBegin+":M"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+mateBegin+":N"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+mateBegin+":O"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);
					
					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+mateBegin+":Q"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+mateBegin+":R"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+mateBegin+":S"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					
					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+mateBegin+":U"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+mateBegin+":V"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+mateBegin+":W"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+mateBegin+":Y"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+mateBegin+":Z"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+mateBegin+":AA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+mateBegin+":AC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+mateBegin+":AD"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+mateBegin+":AE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+mateBegin+":AG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+mateBegin+":AH"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+mateBegin+":AI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+mateBegin+":AK"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+mateBegin+":AL"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+mateBegin+":AM"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+mateBegin+":AO"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+mateBegin+":AP"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+mateBegin+":AQ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+mateBegin+":AS"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+mateBegin+":AT"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+mateBegin+":AU"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+mateBegin+":AW"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+mateBegin+":AX"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+mateBegin+":AY"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+mateBegin+":BA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+mateBegin+":BB"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+mateBegin+":BC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+mateBegin+":BE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+mateBegin+":BF"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+mateBegin+":BG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+mateBegin+":BI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+mateBegin+":BJ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+mateBegin+":BK"+mateEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo2.getSumPlanDlvNum()), cellStyle71);
					
					
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, suppProdVo2.getNationStock(), cellStyle71);
					
					mateCode=mateCode2;
					mateBegin=i+3;
/*					if((itemCode==null && itemCode==null )||(itemCode!=null && itemCode2!=null && (itemCode2.equals(itemCode)))){
						suppProdVo=suppProdVo2;
					}*/
				}
				if ((itemCode2==null && itemCode!=null ) ||(itemCode!=null && itemCode2!=null && (!itemCode2.equals(itemCode)))||i == suppProdVos.size() - 1) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					subEnd=rowNum;
					// 公共和当月数据
					String itemName=suppProdVo.getItemName();
					if(itemName==null || itemName.equals("")){
						itemName="未知";
					}
					
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName+"合计", cellStyle3);
					

					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+subBegin+":J"+subEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+subBegin+":K"+subEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+subBegin+":L"+subEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+subBegin+":M"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+subBegin+":N"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+subBegin+":O"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+subBegin+":Q"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+subBegin+":R"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+subBegin+":S"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);

					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+subBegin+":U"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+subBegin+":V"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+subBegin+":W"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+subBegin+":Y"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+subBegin+":Z"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+subBegin+":AA"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+subBegin+":AC"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+subBegin+":AD"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+subBegin+":AE"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+subBegin+":AG"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+subBegin+":AH"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+subBegin+":AI"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+subBegin+":AK"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+subBegin+":AL"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+subBegin+":AM"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+subBegin+":AO"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+subBegin+":AP"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+subBegin+":AQ"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+subBegin+":AS"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+subBegin+":AT"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+subBegin+":AU"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+subBegin+":AW"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+subBegin+":AX"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+subBegin+":AY"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+subBegin+":BA"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+subBegin+":BB"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+subBegin+":BC"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+subBegin+":BE"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+subBegin+":BF"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+subBegin+":BG"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+subBegin+":BI"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+subBegin+":BJ"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+subBegin+":BK"+subEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);

					
					//设置字体加粗 黄色背景色
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
					
					itemCode=itemCode2;
					subBegin=rowNum+2;
					suppProdVo=suppProdVo2;
					
				
				}
				// 公共和当月数据
				rowNum = 2 + i;
				ExcelUtil.setValue(sheet, rowNum, 0, i + 1, cellStyle1);
				ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo2.getProdSeriesDesc(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 2, suppProdVo2.getItemName(), cellStyle3);
				ExcelUtil.setValue(sheet, rowNum, 3, suppProdVo2.getSuppNo(), cellStyle4);
				ExcelUtil.setValue(sheet, rowNum, 4, suppProdVo2.getSuppName(), cellStyle5);
				ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo2.getRank(), cellStyle6);
				ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo2.getMateDesc(), cellStyle7);
				ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo2.getBoxNumber(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo2.getPackNumber(), cellStyle9);
				
				ExcelUtil.setValue(sheet, rowNum, 9, BigDecimalUtil.getDoubleVal2(suppProdVo2.getBeginOrder()), cellStyle10);
				ExcelUtil.setValue(sheet, rowNum, 10, BigDecimalUtil.getDoubleVal2(suppProdVo2.getBeginStock()), cellStyle11);
				ExcelUtil.setFormula(sheet, rowNum, 11, "J"+(rowNum+1)+"-K"+(rowNum+1), cellStyle12);
				ExcelUtil.setValue(sheet, rowNum, 12, BigDecimalUtil.getDoubleVal2(suppProdVo2.getPlanPrdNum()), cellStyle13);
				ExcelUtil.setValue(sheet, rowNum, 13, BigDecimalUtil.getDoubleVal2(suppProdVo2.getPlanDlvNum()), cellStyle14);
				ExcelUtil.setFormula(sheet, rowNum, 14, "K"+(rowNum+1)+"+M"+(rowNum+1)+"-N"+(rowNum+1) 	, cellStyle15);
				ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

				// 推迟1个月预测7
				ExcelUtil.setValue(sheet, rowNum, 16, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddOnePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 17, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddOnePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 18,  "O"+(rowNum+1)+"+Q"+(rowNum+1)+"-R"+(rowNum+1)	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
				// 推迟2个月预测8
				ExcelUtil.setValue(sheet, rowNum, 20, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwoPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 21, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwoPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 22, "S"+(rowNum+1)+"+U"+(rowNum+1)+"-V"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
				// 推迟3个月预测9
				ExcelUtil.setValue(sheet, rowNum, 24, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddThreePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 25, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddThreePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 26, "W"+(rowNum+1)+"+Y"+(rowNum+1)+"-Z"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
				// 推迟4个月预测10
				ExcelUtil.setValue(sheet, rowNum, 28, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFourPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 29, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFourPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 30, "AA"+(rowNum+1)+"+AC"+(rowNum+1)+"-AD"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
				// 推迟5个月预测11
				ExcelUtil.setValue(sheet, rowNum, 32, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFivePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 33, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFivePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 34, "AE"+(rowNum+1)+"+AG"+(rowNum+1)+"-AH"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
				// 推迟6个月预测12
				ExcelUtil.setValue(sheet, rowNum, 36, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSixPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 37, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSixPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 38, "AI"+(rowNum+1)+"+AK"+(rowNum+1)+"-AL"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
				// 推迟7个月预测1
				ExcelUtil.setValue(sheet, rowNum, 40, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSevenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 41, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSevenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 42, "AM"+(rowNum+1)+"+AO"+(rowNum+1)+"-AP"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
				// 推迟8个月预测2
				ExcelUtil.setValue(sheet, rowNum, 44, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddEightPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 45, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddEightPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 46, "AQ"+(rowNum+1)+"+AS"+(rowNum+1)+"-AT"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
				// 推迟9个月预测3
				ExcelUtil.setValue(sheet, rowNum, 48, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddNinePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 49, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddNinePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 50, "AU"+(rowNum+1)+"+AW"+(rowNum+1)+"-AX"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
				// 推迟10个月预测4
				ExcelUtil.setValue(sheet, rowNum, 52, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 53, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 54, "AY"+(rowNum+1)+"+BA"+(rowNum+1)+"-BB"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
				// 推迟11个月预测------------5
				ExcelUtil.setValue(sheet, rowNum, 56, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddElevenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 57, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddElevenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 58, "BC"+(rowNum+1)+"+BE"+(rowNum+1)+"-BF"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
				// 推迟12个月预测 6
				ExcelUtil.setValue(sheet, rowNum, 60, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwelvePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 61, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwelvePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 62, "BG"+(rowNum+1)+"+BI"+(rowNum+1)+"-BJ"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
				// 生产交货计划求和 期末库存最大值
				ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle66);
				ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle67);
				ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
				// 获取物料编码  上个月的全国库存
				ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
				ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo2.getNationStock()), cellStyle71);
			}
		     for (int i = 1; i < 71; i++) {
		    	  ExcelUtil.setValue(sheet,  2+suppProdSize, i, "-  ", cellStyle72);
		    	  ExcelUtil.setValue(sheet,  mateIndex, i, "-  ", cellStyle72);

		     }
		     //供应商的相关合计开始
		     int rowNum=mateIndex++;
		     int begin=3;
		     int end=suppProdSize+2;
		     Set<String> keySet = suppSubMap.keySet();
				for (String suppNo : keySet) {
					List<SuppVo> list = suppSubMap.get(suppNo);
					String suppName="";
					for (SuppVo suppVo : list) {
						suppName=suppVo.getSuppName();
						rowNum++;
						ExcelUtil.setValue(sheet, rowNum, 2, suppVo.getItemName(), cellStyle3);
						ExcelUtil.setValue(sheet, rowNum, 3, suppVo.getSuppNo(), cellStyle4);
						ExcelUtil.setValue(sheet, rowNum, 4, suppVo.getSuppName(), cellStyle5);
						String fam9="SUMIFS(J"+begin+":J"+end+",C"+begin+":C"+end+",C"+(rowNum+1)+"&\"\",D"+begin+":D"+end+",D"+(rowNum+1)+")";
						String fam10="SUMIFS(K$"+begin+":K$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")";
						ExcelUtil.setFormula(sheet, rowNum, 9,fam9 , cellStyle10);
						ExcelUtil.setFormula(sheet, rowNum, 10, fam10, cellStyle11);
						ExcelUtil.setFormula(sheet, rowNum, 11, "SUMIFS(L$"+begin+":L$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle12);
						ExcelUtil.setFormula(sheet, rowNum, 12,"SUMIFS(M$"+begin+":M$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 13, "SUMIFS(N$"+begin+":N$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 14, "SUMIFS(O$"+begin+":O$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

						// 推迟1个月预测7
						ExcelUtil.setFormula(sheet, rowNum, 16, "SUMIFS(Q$"+begin+":Q$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 17, "SUMIFS(R$"+begin+":R$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 18,  "SUMIFS(S$"+begin+":S$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
						// 推迟2个月预测8
						ExcelUtil.setFormula(sheet, rowNum, 20, "SUMIFS(U$"+begin+":U$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 21, "SUMIFS(V$"+begin+":V$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 22, "SUMIFS(W$"+begin+":W$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
						// 推迟3个月预测9
						ExcelUtil.setFormula(sheet, rowNum, 24, "SUMIFS(Y$"+begin+":Y$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 25, "SUMIFS(Z$"+begin+":Z$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 26, "SUMIFS(AA$"+begin+":AA$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
						// 推迟4个月预测10
						ExcelUtil.setFormula(sheet, rowNum, 28, "SUMIFS(AC$"+begin+":AC$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 29, "SUMIFS(AD$"+begin+":AD$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 30, "SUMIFS(AE$"+begin+":AE$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
						// 推迟5个月预测11
						ExcelUtil.setFormula(sheet, rowNum, 32, "SUMIFS(AG$"+begin+":AG$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 33, "SUMIFS(AH$"+begin+":AH$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 34, "SUMIFS(AI$"+begin+":AI$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
						// 推迟6个月预测12
						ExcelUtil.setFormula(sheet, rowNum, 36, "SUMIFS(AK$"+begin+":AK$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 37, "SUMIFS(AL$"+begin+":AL$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 38, "SUMIFS(AM$"+begin+":AM$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
						// 推迟7个月预测1
						ExcelUtil.setFormula(sheet, rowNum, 40, "SUMIFS(AO$"+begin+":AO$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 41, "SUMIFS(AP$"+begin+":AP$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 42, "SUMIFS(AQ$"+begin+":AQ$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
						// 推迟8个月预测2
						ExcelUtil.setFormula(sheet, rowNum, 44,"SUMIFS(AS$"+begin+":AS$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 45, "SUMIFS(AT$"+begin+":AT$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 46, "SUMIFS(AU$"+begin+":AU$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
						// 推迟9个月预测3
						ExcelUtil.setFormula(sheet, rowNum, 48, "SUMIFS(AW$"+begin+":AW$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 49, "SUMIFS(AX$"+begin+":AX$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 50, "SUMIFS(AY$"+begin+":AY$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
						// 推迟10个月预测4
						ExcelUtil.setFormula(sheet, rowNum, 52, "SUMIFS(BA$"+begin+":BA$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 53, "SUMIFS(BB$"+begin+":BB$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 54, "SUMIFS(BC$"+begin+":BC$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
						// 推迟11个月预测------------5
						ExcelUtil.setFormula(sheet, rowNum, 56, "SUMIFS(BE$"+begin+":BE$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 57, "SUMIFS(BF$"+begin+":BF$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 58, "SUMIFS(BG$"+begin+":BG$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
						// 推迟12个月预测 6
						ExcelUtil.setFormula(sheet, rowNum, 60, "SUMIFS(BI$"+begin+":BI$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 61, "SUMIFS(BJ$"+begin+":BJ$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 62, "SUMIFS(BK$"+begin+":BK$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
						// 生产交货计划求和 期末库存最大值
						ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
						
						// 获取物料编码  上个月的全国库存
						ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
						ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
					}
					//添加小计数据----------------
					rowNum++;
					ExcelUtil.setValue(sheet, rowNum, 2, "箱小计", cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 3, suppNo, cellStyle4);
					ExcelUtil.setValue(sheet, rowNum, 4, suppName, cellStyle5);
					String fam9="SUMIFS(J"+begin+":J"+end+",D"+begin+":D"+end+",D"+(rowNum+1)+")";
					String fam10="SUMIFS(K$"+begin+":K$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")";
					ExcelUtil.setFormula(sheet, rowNum, 9,fam9 , cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, fam10, cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUMIFS(L$"+begin+":L$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle14);
					ExcelUtil.setFormula(sheet, rowNum, 12,"SUMIFS(M$"+begin+":M$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle14);
					ExcelUtil.setFormula(sheet, rowNum, 13, "SUMIFS(N$"+begin+":N$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle14);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUMIFS(O$"+begin+":O$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测7
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUMIFS(Q$"+begin+":Q$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUMIFS(R$"+begin+":R$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18,  "SUMIFS(S$"+begin+":S$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					// 推迟2个月预测8
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUMIFS(U$"+begin+":U$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUMIFS(V$"+begin+":V$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUMIFS(W$"+begin+":W$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测9
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUMIFS(Y$"+begin+":Y$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUMIFS(Z$"+begin+":Z$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUMIFS(AA$"+begin+":AA$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测10
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUMIFS(AC$"+begin+":AC$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUMIFS(AD$"+begin+":AD$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUMIFS(AE$"+begin+":AE$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测11
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUMIFS(AG$"+begin+":AG$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUMIFS(AH$"+begin+":AH$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUMIFS(AI$"+begin+":AI$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测12
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUMIFS(AK$"+begin+":AK$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUMIFS(AL$"+begin+":AL$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUMIFS(AM$"+begin+":AM$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测1
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUMIFS(AO$"+begin+":AO$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUMIFS(AP$"+begin+":AP$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUMIFS(AQ$"+begin+":AQ$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测2
					ExcelUtil.setFormula(sheet, rowNum, 44,"SUMIFS(AS$"+begin+":AS$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUMIFS(AT$"+begin+":AT$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUMIFS(AU$"+begin+":AU$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测3
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUMIFS(AW$"+begin+":AW$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUMIFS(AX$"+begin+":AX$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUMIFS(AY$"+begin+":AY$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测4
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUMIFS(BA$"+begin+":BA$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUMIFS(BB$"+begin+":BB$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUMIFS(BC$"+begin+":BC$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------5
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUMIFS(BE$"+begin+":BE$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUMIFS(BF$"+begin+":BF$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUMIFS(BG$"+begin+":BG$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
					// 推迟12个月预测 6
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUMIFS(BI$"+begin+":BI$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUMIFS(BJ$"+begin+":BJ$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUMIFS(BK$"+begin+":BK$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);

				}
			String realName = "备货计划" + DateUtils.format(planMonth, "yyyyMM") + ".xlsx";
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				realName = URLEncoder.encode(realName, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + realName);
				os = response.getOutputStream();
				wb.write(os);
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			IoUtil.closeIo(os, fis, wb);
		}
		return null;
	}
	
	
	/**
	 * 模板解析
	 * @param file
	 * @return
	 */
	@RequestMapping("/analysisTemp")
	@ResponseBody
	public  RestCode  analysisTemp(MultipartFile file,HttpServletRequest request){
		Workbook workBook;
		List<SuppProdVo> list=new ArrayList<>();
		List<SuppProdVo> totalList=new ArrayList<>();
		String planMonth="";
		try {
			int nullCount=0;
			workBook=new XSSFWorkbook(file.getInputStream());
			FormulaEvaluator evaluator=workBook.getCreationHelper().createFormulaEvaluator();
			Sheet sheet = workBook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			planMonth=sheet.getRow(0).getCell(9).getStringCellValue();
			for (int i = 2; i < rows; i++) {
				Row row = sheet.getRow(i);
				SuppProdVo vo=new SuppProdVo();
				//基础信息
				Double rowNum=null;
				if(row!=null && row.getCell(0)!=null){
					rowNum=row.getCell(0).getNumericCellValue();
				}
				if(rowNum==null || rowNum==0){
					if(nullCount==0){
						nullCount++;
						continue;
					}else{
						break;
					}
				}
				vo.setRowNum(rowNum);
				//系列
				String prodSeriesDesc=ExcelUtil.getStringCellValue(row, 1);
				vo.setProdSeriesDesc(prodSeriesDesc);
				//品项
				String itemName=ExcelUtil.getStringCellValue(row, 2);
				vo.setItemName(itemName);;
				//供应商编码
				String suppNo=ExcelUtil.getStringCellValue(row, 3);
				vo.setSuppNo(suppNo);
				String suppName=ExcelUtil.getStringCellValue(row, 4);
				vo.setSuppName(suppName);
				//排名
				String rank=ExcelUtil.getStringCellValue(row, 5);
				vo.setRank(rank);
				//品名
				String mateDesc=ExcelUtil.getStringCellValue(row, 6);
				vo.setMateDesc(mateDesc);
				//当月数据
				
				//期初订单
				BigDecimal beginOrder=ExcelUtil.getDecimalCellValue(row, 9,evaluator);
				vo.setBeginOrder(beginOrder);
				//期初库存
				BigDecimal beginStock=ExcelUtil.getDecimalCellValue(row, 10,evaluator);
				vo.setBeginStock(beginStock);;
				//期初可生产订单
				BigDecimal beginEnableOrder=ExcelUtil.getDecimalCellValue(row, 11,evaluator);
				vo.setBeginEnableOrder(beginEnableOrder);;
				
				//生产计划
				BigDecimal planPrdNum=ExcelUtil.getDecimalCellValue(row, 12,evaluator);
				vo.setPlanPrdNum(planPrdNum);
				//交货计划
				BigDecimal planDlvNum=ExcelUtil.getDecimalCellValue(row, 13,evaluator);
				vo.setPlanDlvNum(planDlvNum);
				//期末预计库存
				BigDecimal endStock=ExcelUtil.getDecimalCellValue(row, 14,evaluator);
				vo.setEndStock(endStock);
				//安全库存率
				String safeScale=ExcelUtil.getStringCellValue(row, 15);
				vo.setSafeScale(safeScale);
				
				
				//预测的下1个月数据
				//生产计划
				BigDecimal AddOnePlanPrdNum=ExcelUtil.getDecimalCellValue(row, 16,evaluator);
				vo.setAddOnePlanPrdNum(AddOnePlanPrdNum);
				//交货计划
				BigDecimal AddOnePlanDlvNum=ExcelUtil.getDecimalCellValue(row, 17,evaluator);
				vo.setAddOnePlanDlvNum(AddOnePlanDlvNum);
				//期末预计库存
				BigDecimal AddOnePalnEndStock=ExcelUtil.getDecimalCellValue(row, 18,evaluator);
				vo.setAddOnePlanEndStock(AddOnePalnEndStock);
				//安全库存率
				String addOneSafeScale=ExcelUtil.getStringCellValue(row, 19);
				vo.setAddOneSafeScale(addOneSafeScale);
				
				//预测的下2个月数据
				//生产计划
				BigDecimal AddTwoPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 20,evaluator);
				vo.setAddTwoPlanPrdNum(AddTwoPlanPrdNum);
				//交货计划
				BigDecimal AddTwoPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 21,evaluator);
				vo.setAddTwoPlanDlvNum(AddTwoPlanDlvNum);
				//期末预计库存
				BigDecimal AddTwoPalnEndStock=ExcelUtil.getDecimalCellValue(row, 22,evaluator);
				vo.setAddTwoPlanEndStock(AddTwoPalnEndStock);
				//安全库存率
				String addTwoSafeScale=ExcelUtil.getStringCellValue(row, 23);
				vo.setAddTwoSafeScale(addTwoSafeScale);
				
				//预测的下3个月数据
				//生产计划
				BigDecimal AddThreePlanPrdNum=ExcelUtil.getDecimalCellValue(row, 24,evaluator);
				vo.setAddThreePlanPrdNum(AddThreePlanPrdNum);
				//交货计划
				BigDecimal AddThreePlanDlvNum=ExcelUtil.getDecimalCellValue(row, 25,evaluator);
				vo.setAddThreePlanDlvNum(AddThreePlanDlvNum);
				//期末预计库存
				BigDecimal AddThreePalnEndStock=ExcelUtil.getDecimalCellValue(row, 26,evaluator);
				vo.setAddThreePlanEndStock(AddThreePalnEndStock);
				//安全库存率
				String addThreeSafeScale=ExcelUtil.getStringCellValue(row, 27);
				vo.setAddThreeSafeScale(addThreeSafeScale);
				
				//预测的下4个月数据
				//生产计划
				BigDecimal AddFourPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 28,evaluator);
				vo.setAddFourPlanPrdNum(AddFourPlanPrdNum);
				//交货计划
				BigDecimal AddFourPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 29,evaluator);
				vo.setAddFourPlanDlvNum(AddFourPlanDlvNum);
				//期末预计库存
				BigDecimal AddFourPalnEndStock=ExcelUtil.getDecimalCellValue(row, 30,evaluator);
				vo.setAddFourPlanEndStock(AddFourPalnEndStock);
				//安全库存率
				String addFourSafeScale=ExcelUtil.getStringCellValue(row, 31);
				vo.setAddFourSafeScale(addFourSafeScale);
				
				//预测的下5个月数据
				//生产计划
				BigDecimal AddFivePlanPrdNum=ExcelUtil.getDecimalCellValue(row, 32,evaluator);
				vo.setAddFivePlanPrdNum(AddFivePlanPrdNum);
				//交货计划
				BigDecimal AddFivePlanDlvNum=ExcelUtil.getDecimalCellValue(row, 33,evaluator);
				vo.setAddFivePlanDlvNum(AddFivePlanDlvNum);
				//期末预计库存
				BigDecimal AddFivePalnEndStock=ExcelUtil.getDecimalCellValue(row, 34,evaluator);
				vo.setAddFivePlanEndStock(AddFivePalnEndStock);
				//安全库存率
				String addFiveSafeScale=ExcelUtil.getStringCellValue(row, 35);
				vo.setAddFiveSafeScale(addFiveSafeScale);
				
				//预测的下6个月数据
				//生产计划
				BigDecimal AddSixPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 36,evaluator);
				vo.setAddSixPlanPrdNum(AddSixPlanPrdNum);
				//交货计划
				BigDecimal AddSixPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 37,evaluator);
				vo.setAddSixPlanDlvNum(AddSixPlanDlvNum);
				//期末预计库存
				BigDecimal AddSixPalnEndStock=ExcelUtil.getDecimalCellValue(row, 38,evaluator);
				vo.setAddSixPlanEndStock(AddSixPalnEndStock);
				//安全库存率
				String addSixSafeScale=ExcelUtil.getStringCellValue(row, 39);
				vo.setAddSixSafeScale(addSixSafeScale);
				
				//预测的下7个月数据
				//生产计划
				BigDecimal AddSevenPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 40,evaluator);
				vo.setAddSevenPlanPrdNum(AddSevenPlanPrdNum);
				//交货计划
				BigDecimal AddSevenPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 41,evaluator);
				vo.setAddSevenPlanDlvNum(AddSevenPlanDlvNum);
				//期末预计库存
				BigDecimal AddSevenPalnEndStock=ExcelUtil.getDecimalCellValue(row, 42,evaluator);
				vo.setAddSevenPlanEndStock(AddSevenPalnEndStock);
				//安全库存率
				String addSevenSafeScale=ExcelUtil.getStringCellValue(row, 43);
				vo.setAddSevenSafeScale(addSevenSafeScale);
				
				//预测的下8个月数据
				//生产计划
				BigDecimal AddEightPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 44,evaluator);
				vo.setAddEightPlanPrdNum(AddEightPlanPrdNum);
				//交货计划
				BigDecimal AddEightPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 45,evaluator);
				vo.setAddEightPlanDlvNum(AddEightPlanDlvNum);
				//期末预计库存
				BigDecimal AddEightPalnEndStock=ExcelUtil.getDecimalCellValue(row, 46,evaluator);
				vo.setAddEightPlanEndStock(AddEightPalnEndStock);
				//安全库存率
				String addEightSafeScale=ExcelUtil.getStringCellValue(row, 47);
				vo.setAddEightSafeScale(addEightSafeScale);
				
				//预测的下9个月数据
				//生产计划
				BigDecimal AddNinePlanPrdNum=ExcelUtil.getDecimalCellValue(row, 48,evaluator);
				vo.setAddNinePlanPrdNum(AddNinePlanPrdNum);
				//交货计划
				BigDecimal AddNinePlanDlvNum=ExcelUtil.getDecimalCellValue(row, 49,evaluator);
				vo.setAddNinePlanDlvNum(AddNinePlanDlvNum);
				//期末预计库存
				BigDecimal AddNinePalnEndStock=ExcelUtil.getDecimalCellValue(row, 50,evaluator);
				vo.setAddNinePlanEndStock(AddNinePalnEndStock);
				//安全库存率
				String addNineSafeScale=ExcelUtil.getStringCellValue(row, 51);
				vo.setAddNineSafeScale(addNineSafeScale);
				
				//预测的下10个月数据
				//生产计划
				BigDecimal AddTenPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 52,evaluator);
				vo.setAddTenPlanPrdNum(AddTenPlanPrdNum);
				//交货计划
				BigDecimal AddTenPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 53,evaluator);
				vo.setAddTenPlanDlvNum(AddTenPlanDlvNum);
				//期末预计库存
				BigDecimal AddTenPalnEndStock=ExcelUtil.getDecimalCellValue(row, 54,evaluator);
				vo.setAddTenPlanEndStock(AddTenPalnEndStock);
				//安全库存率
				String addTenSafeScale=ExcelUtil.getStringCellValue(row, 55);
				vo.setAddTenSafeScale(addTenSafeScale);
				
				//预测的下11个月数据
				//生产计划
				BigDecimal AddElevenPlanPrdNum=ExcelUtil.getDecimalCellValue(row, 56,evaluator);
				vo.setAddElevenPlanPrdNum(AddElevenPlanPrdNum);
				//交货计划
				BigDecimal AddElevenPlanDlvNum=ExcelUtil.getDecimalCellValue(row, 57,evaluator);
				vo.setAddElevenPlanDlvNum(AddElevenPlanDlvNum);
				//期末预计库存
				BigDecimal AddElevenPalnEndStock=ExcelUtil.getDecimalCellValue(row, 58,evaluator);
				vo.setAddElevenPlanEndStock(AddElevenPalnEndStock);
				//安全库存率
				String addElevenSafeScale=ExcelUtil.getStringCellValue(row, 59);
				vo.setAddElevenSafeScale(addElevenSafeScale);
				
				//预测的下12个月数据
				//生产计划
				BigDecimal AddTwelvePlanPrdNum=ExcelUtil.getDecimalCellValue(row, 60,evaluator);
				vo.setAddTwelvePlanPrdNum(AddTwelvePlanPrdNum);
				//交货计划
				BigDecimal AddTwelvePlanDlvNum=ExcelUtil.getDecimalCellValue(row, 61,evaluator);
				vo.setAddTwelvePlanDlvNum(AddTwelvePlanDlvNum);
				//期末预计库存
				BigDecimal AddTwelvePalnEndStock=ExcelUtil.getDecimalCellValue(row, 62,evaluator);
				vo.setAddTwelvePlanEndStock(AddTwelvePalnEndStock);
				//安全库存率
				String addTwelveSafeScale=ExcelUtil.getStringCellValue(row, 63);
				vo.setAddTwelveSafeScale(addTwelveSafeScale);
	
				//物料编码
				String mateCode=ExcelUtil.getStringCellValue(row, 69);
				vo.setMateCode(mateCode);
				
				if(nullCount<1){
					list.add(vo);
				}else{
					totalList.add(vo);
				}
			}
									
		} catch (Exception e) {
			e.printStackTrace();
			return RestCode.error();
		}
		HttpSession session=request.getSession();
		session.setAttribute("importPlanMonth", DateUtils.parse(planMonth, "yyyyMM"));
		session.setAttribute("totalList", totalList);
		session.setAttribute("importList", list);
		return new RestCode().put("data", list);
	}
	
	/**
	 * 获取数据校验页面
	 * @param jsonStr
	 * @return
	 */
	@RequestMapping("/getCheckDataPage")
	public String  getCheckDataPage(String planCode,Model model){
		model.addAttribute("planCode", planCode);
		return "bam/invenPlan/checkImportData";
	}
	
	/**
	 * 数据校验
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkTemp")
	@ResponseBody
	public List<SuppProdVo>  checkTemp(String jsonStr,HttpServletRequest request){
		HttpSession session=request.getSession();
		List<SuppProdVo> list = (List<SuppProdVo>)session.getAttribute("importList");
		List<SuppProdVo> totalList = (List<SuppProdVo>)session.getAttribute("totalList");
		Date planMonth = (Date)session.getAttribute("importPlanMonth");
		List<SuppProdVo> checkTempData = invenPlanService.checkTempData(list, totalList, planMonth);
		return checkTempData;
	}
	/**
	 * 保存模板数据
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked" )
	@RequestMapping("/saveTempData")
	@ResponseBody
	public RestCode  saveTempData(String jsonStr,HttpServletRequest request){
		try {
			HttpSession session=request.getSession();
			List<SuppProdVo> list = (List<SuppProdVo>)session.getAttribute("importList");
			List<SuppProdVo> totalList = (List<SuppProdVo>)session.getAttribute("totalList");
			Date planMonth = (Date)session.getAttribute("importPlanMonth");
			invenPlanService.saveTempData(list, totalList, planMonth);		
			session.removeAttribute("importList");
			session.removeAttribute("totalList");
			session.removeAttribute("importPlanMonth");
		} catch (Exception e) {
			e.printStackTrace();
			return RestCode.error();
		}
		return new RestCode();
	}
	//------------------------------备货计划分解数据的导入结束----------------------
	

	
	//----------------------------万片报表导出----------------------
	
	/**
	 * 导出备货计划模板
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/exportInvenTemp")
	public  String exportInvenTemp(String planCode, Date planMonth, HttpServletResponse response) {
		String planMonthStr = DateUtils.format(planMonth, "yyyyMM");
		// 获取需要导出的分解的物料供应商数据
		List<SuppProdVo> suppProdVos = invenPlanService.getExportTempData(planCode);
		// 创建HSSFWorkbook对象(excel的文档对象)
		Workbook wb = null;
		OutputStream os = null;
		FileInputStream fis = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(planMonth);
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath + "templates\\excelTemp\\备货计划导入模板.xlsx";
			fis = new FileInputStream(realPath);
			wb = new XSSFWorkbook(fis);
			// 建立新的sheet对象（excel的表单）
			Sheet sheet = wb.getSheetAt(0);
			
	  		Font font = wb.createFont();
			font.setBold(true);
			font.setFontName("微软雅黑");
			font.setFontHeightInPoints((short)9);
			// 获取样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 2, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 2, 1);
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 2, 2);
			CellStyle cellStyle4 = ExcelUtil.getCellStyle(sheet, 2, 3);
			CellStyle cellStyle5 = ExcelUtil.getCellStyle(sheet, 2, 4);
			CellStyle cellStyle6 = ExcelUtil.getCellStyle(sheet, 2, 5);
			CellStyle cellStyle7 = ExcelUtil.getCellStyle(sheet, 2, 6);
			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 2, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 2, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 2, 9);
			CellStyle cellStyle11 = ExcelUtil.getCellStyle(sheet, 2, 10);
			CellStyle cellStyle12 = ExcelUtil.getCellStyle(sheet, 2, 11);
			CellStyle cellStyle13 = ExcelUtil.getCellStyle(sheet, 2, 12);
			CellStyle cellStyle14 = ExcelUtil.getCellStyle(sheet, 2, 13);
			CellStyle cellStyle15 = ExcelUtil.getCellStyle(sheet, 2, 14);
			CellStyle cellStyle16 = ExcelUtil.getCellStyle(sheet, 2, 15);
			CellStyle cellStyle17 = ExcelUtil.getCellStyle(sheet, 2, 16);
			CellStyle cellStyle18 = ExcelUtil.getCellStyle(sheet, 2, 17);
			CellStyle cellStyle19 = ExcelUtil.getCellStyle(sheet, 2, 18);
			CellStyle cellStyle20 = ExcelUtil.getCellStyle(sheet, 2, 19);

	
			CellStyle cellStyle66 = ExcelUtil.getCellStyle(sheet, 2, 65);
			CellStyle cellStyle67 = ExcelUtil.getCellStyle(sheet, 2, 66);
			CellStyle cellStyle68 = ExcelUtil.getCellStyle(sheet, 2, 67);

			CellStyle cellStyle70 = ExcelUtil.getCellStyle(sheet, 2, 69);
			CellStyle cellStyle71 = ExcelUtil.getCellStyle(sheet, 2, 70);
			CellStyle cellStyle72 = ExcelUtil.getCellStyle(sheet, 2, 71);

			Row row1 = sheet.getRow(0);
			// 创建单元格并设置单元格内容
			// 当月数据
			row1.getCell(9).setCellValue(planMonthStr);
			// 下1个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(16).setCellValue(planMonthStr);

			// 下2个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(20).setCellValue(planMonthStr);
			// 下3个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(24).setCellValue(planMonthStr);

			// 下4个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(28).setCellValue(planMonthStr);

			// 下5个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(32).setCellValue(planMonthStr);

			// 下6个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(36).setCellValue(planMonthStr);

			// 下7个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(40).setCellValue(planMonthStr);
			// 下8个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(44).setCellValue(planMonthStr);

			// 下9个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(48).setCellValue(planMonthStr);

			// 下10个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(52).setCellValue(planMonthStr);

			// 下11个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(56).setCellValue(planMonthStr);

			// 下12个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(60).setCellValue(planMonthStr);

			Calendar cal1=Calendar.getInstance();
			cal1.setTime(planMonth);
			cal1.add(Calendar.MONTH, -1);
			row1.getCell(70).setCellValue( DateUtils.format(cal1.getTime(), "MM")+"月底全国库存");
			
			int suppProdSize = suppProdVos.size();
			int mateIndex = suppProdVos.size()+3;
			int mateAndSumCount = 0;
			// 在sheet里创建第三行
			//获取临时变量
			SuppProdVo suppProdVo = suppProdVos.get(0);
			String mateCode = suppProdVo.getMateCode();
			String itemCode = suppProdVo.getItemCode();

			//供应商物料合计的开始于结束   用于计算物料的合计
			int mateBegin=3;
			int mateEnd=3;
			//供应商物料合计的开始于结束   用于计算品项的合计
			int subBegin=mateIndex+1;
			int subEnd=mateIndex+1;
			Map<String, List<SuppVo>> suppSubMap=new HashMap<>();
			for (int i = 0; i < suppProdSize; i++) {
				SuppProdVo suppProdVo2 = suppProdVos.get(i);
				int rowNum = 2 + i;
				String mateCode2 = suppProdVo2.getMateCode();
				String itemCode2 = suppProdVo2.getItemCode();
				//------供应商合计相关数据(存放供应商对应的系列)开始
				String itemName2 = suppProdVo2.getItemName();
				String suppNo = suppProdVo2.getSuppNo();
				String suppName = suppProdVo2.getSuppName();
				List<SuppVo> list = suppSubMap.get(suppNo);
				if(list==null){
					list=new ArrayList<>();
				}
				
				SuppVo supp=new SuppVo();
				supp.setSuppNo(suppNo);
				supp.setSuppName(suppName);
				supp.setItemCode(itemCode2);
				supp.setItemName(itemName2);
				if(!list.contains(supp)){
					list.add(supp);
				}
				suppSubMap.put(suppNo, list);
				//------供应商合计相关数据(存放供应商对应的系列)结束

				if (!mateCode2.equals(mateCode)) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					mateEnd=i+2;
					if(i == suppProdVos.size() - 1){
						mateEnd=i+2;
					}
					// 公共和当月数据
					String itemName=suppProdVo.getItemName();
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName, cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo.getRank(), cellStyle6);
					ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo.getMateDesc(), cellStyle7);
					ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo.getBoxNumber(), cellStyle8);
					ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo.getPackNumber(), cellStyle9);
					
					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+mateBegin+":J"+mateEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+mateBegin+":K"+mateEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+mateBegin+":L"+mateEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+mateBegin+":M"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+mateBegin+":N"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+mateBegin+":O"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+mateBegin+":Q"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+mateBegin+":R"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+mateBegin+":S"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);

					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+mateBegin+":U"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+mateBegin+":V"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+mateBegin+":W"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+mateBegin+":Y"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+mateBegin+":Z"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+mateBegin+":AA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+mateBegin+":AC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+mateBegin+":AD"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+mateBegin+":AE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+mateBegin+":AG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+mateBegin+":AH"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+mateBegin+":AI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+mateBegin+":AK"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+mateBegin+":AL"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+mateBegin+":AM"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+mateBegin+":AO"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+mateBegin+":AP"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+mateBegin+":AQ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+mateBegin+":AS"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+mateBegin+":AT"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+mateBegin+":AU"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+mateBegin+":AW"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+mateBegin+":AX"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+mateBegin+":AY"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+mateBegin+":BA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+mateBegin+":BB"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+mateBegin+":BC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+mateBegin+":BE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+mateBegin+":BF"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+mateBegin+":BG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+mateBegin+":BI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+mateBegin+":BJ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+mateBegin+":BK"+mateEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo.getSumPlanDlvNum()), cellStyle71);
					
					
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, suppProdVo.getNationStock(), cellStyle71);

					mateCode=mateCode2;
					mateBegin=i+3;
					if((itemCode==null && itemCode==null )||(itemCode!=null && itemCode2!=null && (itemCode2.equals(itemCode)))){
						suppProdVo=suppProdVo2;
					}
				}
				if (i == suppProdVos.size() - 1) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					mateEnd=i+2;
					if(i == suppProdVos.size() - 1){
						mateEnd=i+3;
					}
					// 公共和当月数据
					String itemName=suppProdVo2.getItemName();
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo2.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName, cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo2.getRank(), cellStyle6);
					ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo2.getMateDesc(), cellStyle7);
					ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo2.getBoxNumber(), cellStyle8);
					ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo2.getPackNumber(), cellStyle9);
					
					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+mateBegin+":J"+mateEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+mateBegin+":K"+mateEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+mateBegin+":L"+mateEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+mateBegin+":M"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+mateBegin+":N"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+mateBegin+":O"+mateEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);
					
					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+mateBegin+":Q"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+mateBegin+":R"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+mateBegin+":S"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					
					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+mateBegin+":U"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+mateBegin+":V"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+mateBegin+":W"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+mateBegin+":Y"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+mateBegin+":Z"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+mateBegin+":AA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+mateBegin+":AC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+mateBegin+":AD"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+mateBegin+":AE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+mateBegin+":AG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+mateBegin+":AH"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+mateBegin+":AI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+mateBegin+":AK"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+mateBegin+":AL"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+mateBegin+":AM"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+mateBegin+":AO"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+mateBegin+":AP"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+mateBegin+":AQ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+mateBegin+":AS"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+mateBegin+":AT"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+mateBegin+":AU"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+mateBegin+":AW"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+mateBegin+":AX"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+mateBegin+":AY"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+mateBegin+":BA"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+mateBegin+":BB"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+mateBegin+":BC"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+mateBegin+":BE"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+mateBegin+":BF"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+mateBegin+":BG"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+mateBegin+":BI"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+mateBegin+":BJ"+mateEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+mateBegin+":BK"+mateEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo2.getSumPlanDlvNum()), cellStyle71);
					
					
					ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, suppProdVo2.getNationStock(), cellStyle71);
					
					mateCode=mateCode2;
					mateBegin=i+3;
/*					if((itemCode==null && itemCode==null )||(itemCode!=null && itemCode2!=null && (itemCode2.equals(itemCode)))){
						suppProdVo=suppProdVo2;
					}*/
				}
				if ((itemCode2==null && itemCode!=null ) ||(itemCode!=null && itemCode2!=null && (!itemCode2.equals(itemCode)))||i == suppProdVos.size() - 1) {
					rowNum=mateIndex++;
					mateAndSumCount++;
					subEnd=rowNum;
					// 公共和当月数据
					String itemName=suppProdVo.getItemName();
					if(itemName==null || itemName.equals("")){
						itemName="未知";
					}
					
					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, itemName+"合计", cellStyle3);
					

					ExcelUtil.setFormula(sheet, rowNum, 9, "SUM(J"+subBegin+":J"+subEnd+")", cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUM(K"+subBegin+":K"+subEnd+")", cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUM(L"+subBegin+":L"+subEnd+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUM(M"+subBegin+":M"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13,"SUM(N"+subBegin+":N"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUM(O"+subBegin+":O"+subEnd+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUM(Q"+subBegin+":Q"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUM(R"+subBegin+":R"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUM(S"+subBegin+":S"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);

					// 推迟2个月预测
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUM(U"+subBegin+":U"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUM(V"+subBegin+":V"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUM(W"+subBegin+":W"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUM(Y"+subBegin+":Y"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUM(Z"+subBegin+":Z"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUM(AA"+subBegin+":AA"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUM(AC"+subBegin+":AC"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUM(AD"+subBegin+":AD"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUM(AE"+subBegin+":AE"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUM(AG"+subBegin+":AG"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUM(AH"+subBegin+":AH"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUM(AI"+subBegin+":AI"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUM(AK"+subBegin+":AK"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUM(AL"+subBegin+":AL"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUM(AM"+subBegin+":AM"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUM(AO"+subBegin+":AO"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUM(AP"+subBegin+":AP"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUM(AQ"+subBegin+":AQ"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUM(AS"+subBegin+":AS"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUM(AT"+subBegin+":AT"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUM(AU"+subBegin+":AU"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUM(AW"+subBegin+":AW"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUM(AX"+subBegin+":AX"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUM(AY"+subBegin+":AY"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUM(BA"+subBegin+":BA"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUM(BB"+subBegin+":BB"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUM(BC"+subBegin+":BC"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUM(BE"+subBegin+":BE"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUM(BF"+subBegin+":BF"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUM(BG"+subBegin+":BG"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BI"+(rowNum+1), cellStyle20);
					// 推迟12个月预测
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUM(BI"+subBegin+":BI"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUM(BJ"+subBegin+":BJ"+subEnd+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUM(BK"+subBegin+":BK"+subEnd+")", cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "",cellStyle20);
					
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);

					
					//设置字体加粗 黄色背景色
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
					//添加万片小计
					rowNum=mateIndex++;
					mateAndSumCount++;

					ExcelUtil.setValue(sheet, rowNum, 0, mateAndSumCount, cellStyle1);
					ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo.getProdSeriesDesc(), cellStyle2);
					ExcelUtil.setValue(sheet, rowNum, 2, "万片小计", cellStyle3);
					
					String fam9="SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(J$"+subBegin+":J$"+subEnd+"))/10000";
					String fam10="SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(K$"+subBegin+":K$"+subEnd+"))/10000";
					ExcelUtil.setFormula(sheet, rowNum, 9,fam9 , cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, fam10, cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(L$"+subBegin+":L$"+subEnd+"))/10000", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(M$"+subBegin+":M$"+subEnd+"))/10000", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(N$"+subBegin+":N$"+subEnd+"))/10000", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(O$"+subBegin+":O$"+subEnd+"))/10000"	, cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测7
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(Q$"+subBegin+":Q$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(R$"+subBegin+":R$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(S$"+subBegin+":S$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					// 推迟2个月预测8
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(U$"+subBegin+":U$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(V$"+subBegin+":V$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(W$"+subBegin+":W$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测9
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(Y$"+subBegin+":Y$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(Z$"+subBegin+":Z$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AA$"+subBegin+":AA$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测10
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AC$"+subBegin+":AC$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AD$"+subBegin+":AD$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AE$"+subBegin+":AE$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测11
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AG$"+subBegin+":AG$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AH$"+subBegin+":AH$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AI$"+subBegin+":AI$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测12
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AK$"+subBegin+":AK$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AL$"+subBegin+":AL$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AM$"+subBegin+":AM$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测1
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AO$"+subBegin+":AO$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AP$"+subBegin+":AP$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AQ$"+subBegin+":AQ$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测2
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AS$"+subBegin+":AS$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AT$"+subBegin+":AT$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AU$"+subBegin+":AU$"+subEnd+"))/10000"	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测3
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AW$"+subBegin+":AW$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AX$"+subBegin+":AX$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(AY$"+subBegin+":AY$"+subEnd+"))/10000"	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测4
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BA$"+subBegin+":BA$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BB$"+subBegin+":BB$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BC$"+subBegin+":BC$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------5
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BE$"+subBegin+":BE$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BF$"+subBegin+":BF$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BG$"+subBegin+":BG$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
					// 推迟12个月预测 6
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BI$"+subBegin+":BI$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BJ$"+subBegin+":BJ$"+subEnd+"))/10000", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUMPRODUCT(($H$"+subBegin+":$H$"+subEnd+")*($I$"+subBegin+":$I$"+subEnd+")*(BK$"+subBegin+":BK$"+subEnd+"))/10000" 	, cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.TAN.getIndex());
					itemCode=itemCode2;
					subBegin=rowNum+2;
					suppProdVo=suppProdVo2;
					
				
				}
				// 公共和当月数据
				rowNum = 2 + i;
				ExcelUtil.setValue(sheet, rowNum, 0, i + 1, cellStyle1);
				ExcelUtil.setValue(sheet, rowNum, 1, suppProdVo2.getProdSeriesDesc(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 2, suppProdVo2.getItemName(), cellStyle3);
				ExcelUtil.setValue(sheet, rowNum, 3, suppProdVo2.getSuppNo(), cellStyle4);
				ExcelUtil.setValue(sheet, rowNum, 4, suppProdVo2.getSuppName(), cellStyle5);
				ExcelUtil.setValue(sheet, rowNum, 5, suppProdVo2.getRank(), cellStyle6);
				ExcelUtil.setValue(sheet, rowNum, 6, suppProdVo2.getMateDesc(), cellStyle7);
				ExcelUtil.setValue(sheet, rowNum, 7, suppProdVo2.getBoxNumber(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 8, suppProdVo2.getPackNumber(), cellStyle9);
				
				ExcelUtil.setValue(sheet, rowNum, 9, BigDecimalUtil.getDoubleVal2(suppProdVo2.getBeginOrder()), cellStyle10);
				ExcelUtil.setValue(sheet, rowNum, 10, BigDecimalUtil.getDoubleVal2(suppProdVo2.getBeginStock()), cellStyle11);
				ExcelUtil.setFormula(sheet, rowNum, 11, "J"+(rowNum+1)+"-K"+(rowNum+1), cellStyle12);
				ExcelUtil.setValue(sheet, rowNum, 12, BigDecimalUtil.getDoubleVal2(suppProdVo2.getPlanPrdNum()), cellStyle13);
				ExcelUtil.setValue(sheet, rowNum, 13, BigDecimalUtil.getDoubleVal2(suppProdVo2.getPlanDlvNum()), cellStyle14);
				ExcelUtil.setFormula(sheet, rowNum, 14, "K"+(rowNum+1)+"+M"+(rowNum+1)+"-N"+(rowNum+1) 	, cellStyle15);
				ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

				// 推迟1个月预测7
				ExcelUtil.setValue(sheet, rowNum, 16, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddOnePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 17, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddOnePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 18,  "O"+(rowNum+1)+"+Q"+(rowNum+1)+"-R"+(rowNum+1)	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
				// 推迟2个月预测8
				ExcelUtil.setValue(sheet, rowNum, 20, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwoPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 21, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwoPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 22, "S"+(rowNum+1)+"+U"+(rowNum+1)+"-V"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
				// 推迟3个月预测9
				ExcelUtil.setValue(sheet, rowNum, 24, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddThreePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 25, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddThreePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 26, "W"+(rowNum+1)+"+Y"+(rowNum+1)+"-Z"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
				// 推迟4个月预测10
				ExcelUtil.setValue(sheet, rowNum, 28, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFourPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 29, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFourPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 30, "AA"+(rowNum+1)+"+AC"+(rowNum+1)+"-AD"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
				// 推迟5个月预测11
				ExcelUtil.setValue(sheet, rowNum, 32, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFivePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 33, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddFivePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 34, "AE"+(rowNum+1)+"+AG"+(rowNum+1)+"-AH"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
				// 推迟6个月预测12
				ExcelUtil.setValue(sheet, rowNum, 36, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSixPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 37, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSixPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 38, "AI"+(rowNum+1)+"+AK"+(rowNum+1)+"-AL"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
				// 推迟7个月预测1
				ExcelUtil.setValue(sheet, rowNum, 40, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSevenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 41, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddSevenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 42, "AM"+(rowNum+1)+"+AO"+(rowNum+1)+"-AP"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
				// 推迟8个月预测2
				ExcelUtil.setValue(sheet, rowNum, 44, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddEightPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 45, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddEightPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 46, "AQ"+(rowNum+1)+"+AS"+(rowNum+1)+"-AT"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
				// 推迟9个月预测3
				ExcelUtil.setValue(sheet, rowNum, 48, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddNinePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 49, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddNinePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 50, "AU"+(rowNum+1)+"+AW"+(rowNum+1)+"-AX"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
				// 推迟10个月预测4
				ExcelUtil.setValue(sheet, rowNum, 52, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 53, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 54, "AY"+(rowNum+1)+"+BA"+(rowNum+1)+"-BB"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
				// 推迟11个月预测------------5
				ExcelUtil.setValue(sheet, rowNum, 56, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddElevenPlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 57, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddElevenPlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 58, "BC"+(rowNum+1)+"+BE"+(rowNum+1)+"-BF"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
				// 推迟12个月预测 6
				ExcelUtil.setValue(sheet, rowNum, 60, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwelvePlanPrdNum()), cellStyle17);
				ExcelUtil.setValue(sheet, rowNum, 61, BigDecimalUtil.getDoubleVal2(suppProdVo2.getAddTwelvePlanDlvNum()), cellStyle18);
				ExcelUtil.setFormula(sheet, rowNum, 62, "BG"+(rowNum+1)+"+BI"+(rowNum+1)+"-BJ"+(rowNum+1) 	, cellStyle19);
				ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
				// 生产交货计划求和 期末库存最大值
				ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle66);
				ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle67);
				ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
				// 获取物料编码  上个月的全国库存
				ExcelUtil.setValue(sheet, rowNum, 69, suppProdVo2.getMateCode(), cellStyle70);
				ExcelUtil.setValue(sheet, rowNum, 70, BigDecimalUtil.getDoubleVal(suppProdVo2.getNationStock()), cellStyle71);
			}
		     for (int i = 1; i < 71; i++) {
		    	  ExcelUtil.setValue(sheet,  2+suppProdSize, i, "-  ", cellStyle72);
		    	  ExcelUtil.setValue(sheet,  mateIndex, i, "-  ", cellStyle72);
		     }
		     //供应商的相关合计开始
		     int rowNum=mateIndex++;
		     int begin=3;
		     int end=suppProdSize+2;
		     Set<String> keySet = suppSubMap.keySet();
				for (String suppNo : keySet) {
					List<SuppVo> list = suppSubMap.get(suppNo);
					String suppName="";
					for (SuppVo suppVo : list) {
						suppName=suppVo.getSuppName();
						rowNum++;
						ExcelUtil.setValue(sheet, rowNum, 2, suppVo.getItemName(), cellStyle3);
						ExcelUtil.setValue(sheet, rowNum, 3, suppVo.getSuppNo(), cellStyle4);
						ExcelUtil.setValue(sheet, rowNum, 4, suppVo.getSuppName(), cellStyle5);
						String fam9="SUMIFS(J"+begin+":J"+end+",C"+begin+":C"+end+",C"+(rowNum+1)+"&\"\",D"+begin+":D"+end+",D"+(rowNum+1)+")";
						String fam10="SUMIFS(K$"+begin+":K$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")";
						ExcelUtil.setFormula(sheet, rowNum, 9,fam9 , cellStyle10);
						ExcelUtil.setFormula(sheet, rowNum, 10, fam10, cellStyle11);
						ExcelUtil.setFormula(sheet, rowNum, 11, "SUMIFS(L$"+begin+":L$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle12);
						ExcelUtil.setFormula(sheet, rowNum, 12,"SUMIFS(M$"+begin+":M$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 13, "SUMIFS(N$"+begin+":N$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 14, "SUMIFS(O$"+begin+":O$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

						// 推迟1个月预测7
						ExcelUtil.setFormula(sheet, rowNum, 16, "SUMIFS(Q$"+begin+":Q$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 17, "SUMIFS(R$"+begin+":R$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 18,  "SUMIFS(S$"+begin+":S$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
						// 推迟2个月预测8
						ExcelUtil.setFormula(sheet, rowNum, 20, "SUMIFS(U$"+begin+":U$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 21, "SUMIFS(V$"+begin+":V$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 22, "SUMIFS(W$"+begin+":W$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
						// 推迟3个月预测9
						ExcelUtil.setFormula(sheet, rowNum, 24, "SUMIFS(Y$"+begin+":Y$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 25, "SUMIFS(Z$"+begin+":Z$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 26, "SUMIFS(AA$"+begin+":AA$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
						// 推迟4个月预测10
						ExcelUtil.setFormula(sheet, rowNum, 28, "SUMIFS(AC$"+begin+":AC$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 29, "SUMIFS(AD$"+begin+":AD$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 30, "SUMIFS(AE$"+begin+":AE$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
						// 推迟5个月预测11
						ExcelUtil.setFormula(sheet, rowNum, 32, "SUMIFS(AG$"+begin+":AG$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 33, "SUMIFS(AH$"+begin+":AH$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 34, "SUMIFS(AI$"+begin+":AI$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
						// 推迟6个月预测12
						ExcelUtil.setFormula(sheet, rowNum, 36, "SUMIFS(AK$"+begin+":AK$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 37, "SUMIFS(AL$"+begin+":AL$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 38, "SUMIFS(AM$"+begin+":AM$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
						// 推迟7个月预测1
						ExcelUtil.setFormula(sheet, rowNum, 40, "SUMIFS(AO$"+begin+":AO$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 41, "SUMIFS(AP$"+begin+":AP$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 42, "SUMIFS(AQ$"+begin+":AQ$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
						// 推迟8个月预测2
						ExcelUtil.setFormula(sheet, rowNum, 44,"SUMIFS(AS$"+begin+":AS$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 45, "SUMIFS(AT$"+begin+":AT$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 46, "SUMIFS(AU$"+begin+":AU$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
						// 推迟9个月预测3
						ExcelUtil.setFormula(sheet, rowNum, 48, "SUMIFS(AW$"+begin+":AW$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 49, "SUMIFS(AX$"+begin+":AX$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 50, "SUMIFS(AY$"+begin+":AY$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
						// 推迟10个月预测4
						ExcelUtil.setFormula(sheet, rowNum, 52, "SUMIFS(BA$"+begin+":BA$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 53, "SUMIFS(BB$"+begin+":BB$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 54, "SUMIFS(BC$"+begin+":BC$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
						// 推迟11个月预测------------5
						ExcelUtil.setFormula(sheet, rowNum, 56, "SUMIFS(BE$"+begin+":BE$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 57, "SUMIFS(BF$"+begin+":BF$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 58, "SUMIFS(BG$"+begin+":BG$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
						// 推迟12个月预测 6
						ExcelUtil.setFormula(sheet, rowNum, 60, "SUMIFS(BI$"+begin+":BI$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 61, "SUMIFS(BJ$"+begin+":BJ$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 62, "SUMIFS(BK$"+begin+":BK$"+end+",$C$"+begin+":$C$"+end+",$C"+(rowNum+1)+"&\"\",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
						ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
						// 生产交货计划求和 期末库存最大值
						ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
						
						// 获取物料编码  上个月的全国库存
						ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
						ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
						setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
						rowNum++;
						ExcelUtil.setValue(sheet, rowNum, 2, suppVo.getItemName(), cellStyle3);
						ExcelUtil.setValue(sheet, rowNum, 3, suppVo.getSuppNo(), cellStyle4);
						ExcelUtil.setValue(sheet, rowNum, 4, suppVo.getSuppName(), cellStyle5);
						ExcelUtil.setFormula(sheet, rowNum, 9,  "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(J$"+begin+":J$"+end+"))/10000" , cellStyle10);
						ExcelUtil.setFormula(sheet, rowNum, 10, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(K$"+begin+":K$"+end+"))/10000" , cellStyle11);
						ExcelUtil.setFormula(sheet, rowNum, 11, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(L$"+begin+":L$"+end+"))/10000" , cellStyle12);
						ExcelUtil.setFormula(sheet, rowNum, 12, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(M$"+begin+":M$"+end+"))/10000" , cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 13, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(N$"+begin+":N$"+end+"))/10000" , cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 14, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(O$"+begin+":O$"+end+"))/10000" , cellStyle15);
						ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

						// 推迟1个月预测7
						ExcelUtil.setFormula(sheet, rowNum, 16, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Q$"+begin+":Q$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 17, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(R$"+begin+":R$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 18, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(S$"+begin+":S$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
						// 推迟2个月预测8
						ExcelUtil.setFormula(sheet, rowNum, 20, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(U$"+begin+":U$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 21, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(V$"+begin+":V$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 22, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(W$"+begin+":W$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
						// 推迟3个月预测9
						ExcelUtil.setFormula(sheet, rowNum, 24, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Y$"+begin+":Y$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 25, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Z$"+begin+":Z$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 26, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AA$"+begin+":AA$"+end+"))/10000"  	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
						// 推迟4个月预测10
						ExcelUtil.setFormula(sheet, rowNum, 28, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AC$"+begin+":AC$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 29, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AD$"+begin+":AD$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 30, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AE$"+begin+":AE$"+end+"))/10000" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
						// 推迟5个月预测11
						ExcelUtil.setFormula(sheet, rowNum, 32, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AG$"+begin+":AG$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 33, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AH$"+begin+":AH$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 34, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AI$"+begin+":AI$"+end+"))/10000" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
						// 推迟6个月预测12
						ExcelUtil.setFormula(sheet, rowNum, 36, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AK$"+begin+":AK$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 37, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AL$"+begin+":AL$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 38, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AM$"+begin+":AM$"+end+"))/10000" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
						// 推迟7个月预测1
						ExcelUtil.setFormula(sheet, rowNum, 40, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AO$"+begin+":AO$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 41, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AP$"+begin+":AP$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 42, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AQ$"+begin+":AQ$"+end+"))/10000" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
						// 推迟8个月预测2
						ExcelUtil.setFormula(sheet, rowNum, 44, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AS$"+begin+":AS$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 45, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AT$"+begin+":AT$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 46, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AU$"+begin+":AU$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
						// 推迟9个月预测3
						ExcelUtil.setFormula(sheet, rowNum, 48, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AW$"+begin+":AW$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 49, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AX$"+begin+":AX$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 50, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AY$"+begin+":AY$"+end+"))/10000"  	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
						// 推迟10个月预测4
						ExcelUtil.setFormula(sheet, rowNum, 52, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BA$"+begin+":BA$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 53, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BB$"+begin+":BB$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 54, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BC$"+begin+":BC$"+end+"))/10000"  	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
						// 推迟11个月预测------------5
						ExcelUtil.setFormula(sheet, rowNum, 56, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BE$"+begin+":BE$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 57, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BF$"+begin+":BF$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 58, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BG$"+begin+":BG$"+end+"))/10000" 	, cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
						// 推迟12个月预测 6
						ExcelUtil.setFormula(sheet, rowNum, 60, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BI$"+begin+":BI$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 61, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BJ$"+begin+":BJ$"+end+"))/10000" , cellStyle19);
						ExcelUtil.setFormula(sheet, rowNum, 62, "SUMPRODUCT(($C$"+begin+":$C$"+end+"=$C"+(rowNum+1)+")*($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BK$"+begin+":BK$"+end+"))/10000"  	, cellStyle19);
						ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
						// 生产交货计划求和 期末库存最大值
						ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
						ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
						// 获取物料编码  上个月的全国库存
						ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
						ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
						setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.TAN.index);
						
					}
					//添加小计数据----------------
					rowNum++;
					ExcelUtil.setValue(sheet, rowNum, 2, "箱小计", cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 3, suppNo, cellStyle4);
					ExcelUtil.setValue(sheet, rowNum, 4, suppName, cellStyle5);
					String fam9="SUMIFS(J"+begin+":J"+end+",D"+begin+":D"+end+",D"+(rowNum+1)+")";
					String fam10="SUMIFS(K$"+begin+":K$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")";
					ExcelUtil.setFormula(sheet, rowNum, 9,fam9 , cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, fam10, cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUMIFS(L$"+begin+":L$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12,"SUMIFS(M$"+begin+":M$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13, "SUMIFS(N$"+begin+":N$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUMIFS(O$"+begin+":O$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测7
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUMIFS(Q$"+begin+":Q$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUMIFS(R$"+begin+":R$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18,  "SUMIFS(S$"+begin+":S$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					// 推迟2个月预测8
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUMIFS(U$"+begin+":U$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUMIFS(V$"+begin+":V$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUMIFS(W$"+begin+":W$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测9
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUMIFS(Y$"+begin+":Y$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUMIFS(Z$"+begin+":Z$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUMIFS(AA$"+begin+":AA$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测10
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUMIFS(AC$"+begin+":AC$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUMIFS(AD$"+begin+":AD$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUMIFS(AE$"+begin+":AE$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测11
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUMIFS(AG$"+begin+":AG$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUMIFS(AH$"+begin+":AH$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUMIFS(AI$"+begin+":AI$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测12
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUMIFS(AK$"+begin+":AK$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUMIFS(AL$"+begin+":AL$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUMIFS(AM$"+begin+":AM$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测1
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUMIFS(AO$"+begin+":AO$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUMIFS(AP$"+begin+":AP$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUMIFS(AQ$"+begin+":AQ$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测2
					ExcelUtil.setFormula(sheet, rowNum, 44,"SUMIFS(AS$"+begin+":AS$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUMIFS(AT$"+begin+":AT$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUMIFS(AU$"+begin+":AU$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")"	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测3
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUMIFS(AW$"+begin+":AW$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUMIFS(AX$"+begin+":AX$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUMIFS(AY$"+begin+":AY$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测4
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUMIFS(BA$"+begin+":BA$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUMIFS(BB$"+begin+":BB$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUMIFS(BC$"+begin+":BC$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------5
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUMIFS(BE$"+begin+":BE$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUMIFS(BF$"+begin+":BF$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUMIFS(BG$"+begin+":BG$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
					// 推迟12个月预测 6
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUMIFS(BI$"+begin+":BI$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUMIFS(BJ$"+begin+":BJ$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")", cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUMIFS(BK$"+begin+":BK$"+end+",$D$"+begin+":$D$"+end+",$D"+(rowNum+1)+")" 	, cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());

					//
					rowNum++;
					ExcelUtil.setValue(sheet, rowNum, 2, "万片小计", cellStyle3);
					ExcelUtil.setValue(sheet, rowNum, 3, suppNo, cellStyle4);
					ExcelUtil.setValue(sheet, rowNum, 4, suppName, cellStyle5);
					
					ExcelUtil.setFormula(sheet, rowNum, 9,  "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(J$"+begin+":J$"+end+"))/10000" , cellStyle10);
					ExcelUtil.setFormula(sheet, rowNum, 10, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(K$"+begin+":K$"+end+"))/10000" , cellStyle11);
					ExcelUtil.setFormula(sheet, rowNum, 11, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(L$"+begin+":L$"+end+"))/10000" , cellStyle12);
					ExcelUtil.setFormula(sheet, rowNum, 12, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(M$"+begin+":M$"+end+"))/10000" , cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 13, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(N$"+begin+":N$"+end+"))/10000" , cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 14, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(O$"+begin+":O$"+end+"))/10000" , cellStyle15);
					ExcelUtil.setFormula(sheet, rowNum, 15, "O"+(rowNum+1)+"/R"+(rowNum+1), cellStyle16);

					// 推迟1个月预测7
					ExcelUtil.setFormula(sheet, rowNum, 16, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Q$"+begin+":Q$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 17, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(R$"+begin+":R$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 18, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(S$"+begin+":S$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 19, "S"+(rowNum+1)+"/V"+(rowNum+1), cellStyle20);
					// 推迟2个月预测8
					ExcelUtil.setFormula(sheet, rowNum, 20, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(U$"+begin+":U$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 21, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(V$"+begin+":V$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 22, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(W$"+begin+":W$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 23, "W"+(rowNum+1)+"/Z"+(rowNum+1), cellStyle20);
					// 推迟3个月预测9
					ExcelUtil.setFormula(sheet, rowNum, 24, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Y$"+begin+":Y$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 25, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(Z$"+begin+":Z$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 26, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AA$"+begin+":AA$"+end+"))/10000"  	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 27, "AA"+(rowNum+1)+"/AD"+(rowNum+1), cellStyle20);
					// 推迟4个月预测10
					ExcelUtil.setFormula(sheet, rowNum, 28, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AC$"+begin+":AC$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 29, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AD$"+begin+":AD$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 30, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AE$"+begin+":AE$"+end+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 31, "AE"+(rowNum+1)+"/AH"+(rowNum+1), cellStyle20);
					// 推迟5个月预测11
					ExcelUtil.setFormula(sheet, rowNum, 32, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AG$"+begin+":AG$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 33, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AH$"+begin+":AH$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 34, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AI$"+begin+":AI$"+end+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 35, "AI"+(rowNum+1)+"/AL"+(rowNum+1), cellStyle20);
					// 推迟6个月预测12
					ExcelUtil.setFormula(sheet, rowNum, 36, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AK$"+begin+":AK$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 37, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AL$"+begin+":AL$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 38, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AM$"+begin+":AM$"+end+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 39, "AM"+(rowNum+1)+"/AP"+(rowNum+1), cellStyle20);
					// 推迟7个月预测1
					ExcelUtil.setFormula(sheet, rowNum, 40, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AO$"+begin+":AO$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 41, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AP$"+begin+":AP$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 42, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AQ$"+begin+":AQ$"+end+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 43, "AQ"+(rowNum+1)+"/AT"+(rowNum+1), cellStyle20);
					// 推迟8个月预测2
					ExcelUtil.setFormula(sheet, rowNum, 44, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AS$"+begin+":AS$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 45, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AT$"+begin+":AT$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 46, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AU$"+begin+":AU$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 47, "AU"+(rowNum+1)+"/AX"+(rowNum+1), cellStyle20);
					// 推迟9个月预测3
					ExcelUtil.setFormula(sheet, rowNum, 48, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AW$"+begin+":AW$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 49, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AX$"+begin+":AX$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 50, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(AY$"+begin+":AY$"+end+"))/10000"  	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 51, "AY"+(rowNum+1)+"/BB"+(rowNum+1), cellStyle20);
					// 推迟10个月预测4
					ExcelUtil.setFormula(sheet, rowNum, 52, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BA$"+begin+":BA$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 53, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BB$"+begin+":BB$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 54, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BC$"+begin+":BC$"+end+"))/10000"  	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 55, "BC"+(rowNum+1)+"/BF"+(rowNum+1), cellStyle20);
					// 推迟11个月预测------------5
					ExcelUtil.setFormula(sheet, rowNum, 56, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BE$"+begin+":BE$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 57, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BF$"+begin+":BF$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 58, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BG$"+begin+":BG$"+end+"))/10000" 	, cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 59, "BG"+(rowNum+1)+"/BJ"+(rowNum+1), cellStyle20);
					// 推迟12个月预测 6
					ExcelUtil.setFormula(sheet, rowNum, 60, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BI$"+begin+":BI$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 61, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BJ$"+begin+":BJ$"+end+"))/10000" , cellStyle19);
					ExcelUtil.setFormula(sheet, rowNum, 62, "SUMPRODUCT(($D$"+begin+":$D$"+end+"=$D"+(rowNum+1)+")*($H$"+begin+":$H$"+end+")*($I$"+begin+":$I$"+end+")*(BK$"+begin+":BK$"+end+"))/10000"  	, cellStyle19);
					ExcelUtil.setValue(sheet, rowNum, 63, "", cellStyle20);
					// 生产交货计划求和 期末库存最大值
					ExcelUtil.setFormula(sheet, rowNum, 65, "M"+(rowNum+1)+"+Q"+(rowNum+1)+"+U"+(rowNum+1)+"+Y"+(rowNum+1)+"+AC"+(rowNum+1)+"+AG"+(rowNum+1)+"+AK"+(rowNum+1)+"+AO"+(rowNum+1)+"+AS"+(rowNum+1)+"+AW"+(rowNum+1)+"+BA"+(rowNum+1)+"+BE"+(rowNum+1)+"+BI"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 66, "N"+(rowNum+1)+"+R"+(rowNum+1)+"+V"+(rowNum+1)+"+Z"+(rowNum+1)+"+AD"+(rowNum+1)+"+AH"+(rowNum+1)+"+AL"+(rowNum+1)+"+AP"+(rowNum+1)+"+AT"+(rowNum+1)+"+AX"+(rowNum+1)+"+BB"+(rowNum+1)+"+BF"+(rowNum+1)+"+BJ"+(rowNum+1), cellStyle68);
					ExcelUtil.setFormula(sheet, rowNum, 67, "MAX(K"+(rowNum+1)+",O"+(rowNum+1)+",S"+(rowNum+1)+",W"+(rowNum+1)+",AA"+(rowNum+1)+",AE"+(rowNum+1)+",AI"+(rowNum+1)+",AM"+(rowNum+1)+",AQ"+(rowNum+1)+",AU"+(rowNum+1)+",AY"+(rowNum+1)+",BC"+(rowNum+1)+",BG"+(rowNum+1)+",BK"+(rowNum+1)+")", cellStyle68);
					// 获取物料编码  上个月的全国库存
					ExcelUtil.setValue(sheet, rowNum, 69, null, cellStyle70);
					ExcelUtil.setValue(sheet, rowNum, 70, null, cellStyle71);
					setFontAndBgColor(0, 63, rowNum, wb, font, sheet, IndexedColors.TAN.index);


				}
//			wb.setForceFormulaRecalculation(true);
//			sheet.autoSizeColumn((short) 1); // 调整第二列宽度
//			sheet.autoSizeColumn((short) 3); // 调整第四列宽度
			String realName = "备货计划" + DateUtils.format(planMonth, "yyyyMM") + ".xlsx";
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				realName = URLEncoder.encode(realName, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + realName);
				os = response.getOutputStream();
				wb.write(os);
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			IoUtil.closeIo(os, fis, wb);
		}
		return null;
	}
	//----------------------------万片报表导出----------------------
	
	//----------------------------设置背景色------------------------
	public void setFontAndBgColor(int begin,int end ,int rowNum,Workbook wb,Font font,Sheet sheet,short color){
		  for (int j = begin;j<=end; j++) {
  			  CellStyle cellStyle = wb.createCellStyle();
			  CellStyle cellStyle0 = ExcelUtil.getCellStyle(sheet, rowNum, j);
			  cellStyle.cloneStyleFrom(cellStyle0);
			  cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			  cellStyle.setFillForegroundColor(color);
			  cellStyle.setFont(font);
			  ExcelUtil.setCellStyle(sheet, rowNum,j, cellStyle);
		  }
	}
	//----------------------------设置背景色------------------------
	/**
	 * 导出备货计划模板
	 * @param planCode
	 * @return
	 */
	@RequestMapping("/export/compare")
	public  String exportCompare(String planCode,HttpServletResponse response) {
		InvenPlan invenPlan = invenPlanService.getInvenPlanByCode(planCode);
		Date planMonth = invenPlan.getPlanMonth();
		String planMonthStr = DateUtils.format(planMonth,"yyyyMM");
		// 获取需要导出差别数据
		List<InvenPadCompare> list = invenPlanService.getGetComPareByPlanCode(planCode, planMonth);
		// 创建HSSFWorkbook对象(excel的文档对象)
		Workbook wb = null;
		OutputStream os = null;
		FileInputStream fis = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(planMonth);
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath + "templates\\excelTemp\\交货计划比较差异.xlsx";
			fis = new FileInputStream(realPath);
			wb = new XSSFWorkbook(fis);
			// 建立新的sheet对象（excel的表单）
			Sheet sheet = wb.getSheetAt(0);
			
	  		Font font = wb.createFont();
			font.setBold(true);
			font.setFontName("微软雅黑");
			font.setFontHeightInPoints((short)9);
			// 获取样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 2, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 2, 1);

			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 2, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 2, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 2, 9);
		
			Row row1 = sheet.getRow(0);
			// 创建单元格并设置单元格内容
			// 当月数据
			row1.getCell(7).setCellValue(planMonthStr);
			// 下1个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(10).setCellValue(planMonthStr);

			// 下2个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(13).setCellValue(planMonthStr);
			// 下3个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(16).setCellValue(planMonthStr);

			// 下4个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(19).setCellValue(planMonthStr);

			// 下5个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(22).setCellValue(planMonthStr);

			// 下6个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(25).setCellValue(planMonthStr);

			// 下7个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(28).setCellValue(planMonthStr);
			// 下8个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(31).setCellValue(planMonthStr);

			// 下9个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(34).setCellValue(planMonthStr);

			// 下10个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(37).setCellValue(planMonthStr);

			// 下11个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(40).setCellValue(planMonthStr);

			// 下12个月生产交货预测
			cal.add(Calendar.MONTH, 1);
			planMonthStr = DateUtils.format(cal.getTime(), "yyyyMM");
			row1.getCell(43).setCellValue(planMonthStr);
			for (int i = 0; i < list.size(); i++) {
				InvenPadCompare compareData=list.get(i);
				int rowNum=i+2;
				ExcelUtil.setValue(sheet, rowNum, 0, i+1, cellStyle1);
				ExcelUtil.setValue(sheet, rowNum, 1, compareData.getRanking(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 2, compareData.getMateDesc(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 3, compareData.getSeriesDesc(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 4, compareData.getItemName(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 5, compareData.getBoxNumber(), cellStyle2);
				ExcelUtil.setValue(sheet, rowNum, 6, compareData.getPackNumber(), cellStyle2);
				//当前月
				ExcelUtil.setValue(sheet, rowNum, 7, compareData.getInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 8, compareData.getPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum, 9, "H"+(rowNum+1)+"-I"+(rowNum+1), cellStyle10);
				//推迟1月
				ExcelUtil.setValue(sheet, rowNum, 10, compareData.getAddOneInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 11, compareData.getAddOnePadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,12, "K"+(rowNum+1)+"-L"+(rowNum+1), cellStyle10);
				//推迟2月
				ExcelUtil.setValue(sheet, rowNum, 13, compareData.getAddTwoInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 14, compareData.getAddTwoPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,15, "N"+(rowNum+1)+"-O"+(rowNum+1), cellStyle10);
				//推迟3月
				ExcelUtil.setValue(sheet, rowNum, 16, compareData.getAddThreeInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 17, compareData.getAddThreePadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,18, "Q"+(rowNum+1)+"-R"+(rowNum+1), cellStyle10);
				//推迟4月
				ExcelUtil.setValue(sheet, rowNum, 19, compareData.getAddFourInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 20, compareData.getAddFourPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,21, "T"+(rowNum+1)+"-U"+(rowNum+1), cellStyle10);
				//推迟5月
				ExcelUtil.setValue(sheet, rowNum, 22, compareData.getAddFiveInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 23, compareData.getAddFivePadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,24, "W"+(rowNum+1)+"-X"+(rowNum+1), cellStyle10);
				//推迟6月
				ExcelUtil.setValue(sheet, rowNum, 25, compareData.getAddSixInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 26, compareData.getAddSixPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,27, "Z"+(rowNum+1)+"-AA"+(rowNum+1), cellStyle10);
				//推迟7月
				ExcelUtil.setValue(sheet, rowNum, 28, compareData.getAddSevenInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 29, compareData.getAddSevenPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,30, "AC"+(rowNum+1)+"-AD"+(rowNum+1), cellStyle10);
				//推迟8月
				ExcelUtil.setValue(sheet, rowNum, 31, compareData.getAddEightInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 32, compareData.getAddEightPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,33, "AF"+(rowNum+1)+"-AG"+(rowNum+1), cellStyle10);
				//推迟9月
				ExcelUtil.setValue(sheet, rowNum, 34, compareData.getAddNineInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 35, compareData.getAddNinePadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,36, "AI"+(rowNum+1)+"-AJ"+(rowNum+1), cellStyle10);
				//推迟10月
				ExcelUtil.setValue(sheet, rowNum, 37, compareData.getAddTenInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 37, compareData.getAddTenPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,39, "AL"+(rowNum+1)+"-AM"+(rowNum+1), cellStyle10);
				//推迟11月
				ExcelUtil.setValue(sheet, rowNum, 40, compareData.getAddElevenInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 41, compareData.getAddElevenPadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,42, "AO"+(rowNum+1)+"-AP"+(rowNum+1), cellStyle10);
				//推迟12月
				ExcelUtil.setValue(sheet, rowNum, 43, compareData.getAddTwelveInvenDlvNum(), cellStyle8);
				ExcelUtil.setValue(sheet, rowNum, 44, compareData.getAddTwelvePadDlvNum(), cellStyle9);
				ExcelUtil.setFormula(sheet, rowNum,45, "AR"+(rowNum+1)+"-AS"+(rowNum+1), cellStyle10);
			}
			wb.setForceFormulaRecalculation(true);
			String realName = "备货计划差异" + DateUtils.format(planMonth, "yyyyMM") + ".xlsx";
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				realName = URLEncoder.encode(realName, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + realName);
				os = response.getOutputStream();
				wb.write(os);
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			IoUtil.closeIo(os, fis, wb);
		}
		return null;
	}	
}
