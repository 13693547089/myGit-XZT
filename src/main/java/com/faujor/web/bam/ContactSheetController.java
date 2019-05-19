package com.faujor.web.bam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr.Function;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutLiaisonService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class ContactSheetController {
	@Autowired
	private CutLiaisonService cutLiaisonService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private CutProductService cutProductService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CutLiaisonForBaoCaiService cutLiaisonForBaoCaiService;
	/**
	 *
	 * 跳转打切联络单列表
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getContactSheetListHtml")
	public String getContactSheetListHtml(final Model model) {
		//CUTLIAISTATUS
		List<Dic> cutLiaiStatusList = basicService.findDicListByCategoryCode("CUTLIAISTATUS");
		model.addAttribute("cutLiaiStatusList", cutLiaiStatusList);
		return "bam/placut/ContactSheet/ContactSheetList";
	}

	/**
	 * 打切联络单列表数据
	 * 
	 * @param limit
	 * @param page
	 * @param cutLiai
	 * @return
	 */
	@Log(value = "获取打切联络单列表")
	@ResponseBody
	@RequestMapping("/queryCutLiaisonByPage")
	public Map<String, Object> queryCutLiaisonByPage(Integer limit, Integer page, CutLiaison cutLiai) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		map.put("start", start);
		map.put("end", end);
		map.put("cutLiai", cutLiai);
		if("supplier".equals(user.getUserType())){
			map.put("createId", user.getUserId().toString());
		}else{
			map.put("userId", user.getUserId().toString());
		}
		Map<String, Object> page2 = cutLiaisonService.queryCurLiaisonByPage(map);
		return page2;
	}

	/**
	 *
	 * 跳转新建打切联络单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getContactSheetDetailHtml")
	public String getContactSheetDetailHtml(final Model model) {
		SysUserDO user = UserCommon.getUser();
		Date date = new Date();
		CutLiaison cutLiai = new CutLiaison();
		cutLiai.setCreateDate(date);
		cutLiai.setSuppName(user.getName());
		cutLiai.setSuppId("s0000900009");
		if("supplier".equals(user.getUserType())){
			QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			cutLiai.setSuppId(supp.getSuppId());
		}
		//查询已提交的包材打切联络单号
		List<String> liaiCodeList = cutLiaisonForBaoCaiService.queryBaoCaiCutLiaiCodeList();
		model.addAttribute("liaiCodeList", liaiCodeList);
		model.addAttribute("cutLiai", cutLiai);
		return "bam/placut/ContactSheet/ContactSheetDetail";
	}

	/**
	 * 获取字段
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryFields")
	public String queryFields() {
		JSONArray ja = new JSONArray();
		JSONObject jo3 = new JSONObject();
		jo3.put("title", "序号");
		jo3.put("type", "numbers");
		jo3.put("fixed", "left");
		ja.add(jo3);
		JSONObject jo16 = new JSONObject();
		jo16.put("title", "系列名称");
		jo16.put("field", "seriesExpl");
		jo16.put("width", "117");
		jo16.put("fixed", "left");
		ja.add(jo16);
		JSONObject jo5 = new JSONObject();
		jo5.put("title", "物料名称");
		jo5.put("field", "mateName");
		jo5.put("width", "117");
		jo5.put("fixed", "left");
		ja.add(jo5);
		JSONObject jo6 = new JSONObject();
		jo6.put("title", "版本");
		jo6.put("field", "version");
		jo6.put("width", "100");
		jo6.put("fixed", "left");
		ja.add(jo6);
		/*JSONObject jo7 = new JSONObject();
		jo7.put("title", "打切目的");
		jo7.put("field", "cutAim");
		jo7.put("width", "104");
		jo7.put("fixed", "left");
		ja.add(jo7);
		JSONObject jo8 = new JSONObject();
		jo8.put("title", "主包材");
		jo8.put("field", "mainStru");
		jo8.put("width", "86");
		jo8.put("fixed", "left");
		ja.add(jo8);
		JSONObject jo9 = new JSONObject();
		jo9.put("title", "箱入数");
		jo9.put("field", "boxNumber");
		jo9.put("width", "86");
		jo9.put("fixed", "left");
		ja.add(jo9);*/
		JSONObject jo13 = new JSONObject();
		jo13.put("title", "总订单在外量");
		jo13.put("field", "sumOutNum");
		jo13.put("width", "140");
		jo13.put("event", "setSign");
		ja.add(jo13);
		JSONObject jo10 = new JSONObject();
		jo10.put("title", "打切订单在外量");
		jo10.put("field", "outNum");
		jo10.put("edit", "number");
		jo10.put("width", "140");
		jo10.put("event", "setSign");
		ja.add(jo10);
		JSONObject jo14 = new JSONObject();
		jo14.put("title", "总成品库存");
		jo14.put("field", "sumInveNum");
		jo14.put("width", "140");
		jo14.put("event", "setSign");
		ja.add(jo14);
		JSONObject jo11 = new JSONObject();
		jo11.put("title", "打切成品库存");
		jo11.put("field", "inveNum");
		jo11.put("edit", "number");
		jo11.put("width", "140");
		jo11.put("event", "setSign");
		ja.add(jo11);
		JSONObject jo15 = new JSONObject();
		jo15.put("title", "总可生产订单");
		jo15.put("field", "sumProdNum");
		jo15.put("width", "140");
		jo15.put("event", "setSign");
		ja.add(jo15);
		JSONObject jo12 = new JSONObject();
		jo12.put("title", "本月打切可生产订单");
		jo12.put("field", "prodNum");
		jo12.put("width", "147");
		jo12.put("event", "setSign");
		//jo12.put("templet", "function(d){ return getColor(d.prodNum,d.lastProdNum)}");
		ja.add(jo12);
		JSONObject jo17 = new JSONObject();
		jo17.put("title", "上月打切可生产订单");
		jo17.put("field", "lastProdNum");
		jo17.put("width", "147");
		jo17.put("event", "setSign");
		ja.add(jo17);
		List<CutStructure> list = cutProductService.queryAllCutStru();
		for (CutStructure c : list) {
			// 个---xx个的字段名称以A开头
			//获取折算规则 A：表示按箱入数折算，B：表示1:1折算
	        String converRule = c.getConverRule();
	        //B：表示1:1折算----字段名称后面加“MASTER”
			String a = "A".equals(converRule) ? "A" + c.getContentCode() : "A" + c.getContentCode() + "MASTER";
			JSONObject jo = new JSONObject();
			jo.put("title", c.getClassName() + c.getContentName() + "个");
			jo.put("field", a);
			jo.put("edit", "text");
			jo.put("width", "130");
			jo.put("event", "setSign");
			ja.add(jo);
			// 箱----xx箱的字段名称以B开头
			String b = "A".equals(converRule) ? "B" + c.getContentCode() : "B" + c.getContentCode() + "MASTER";
			JSONObject jo1 = new JSONObject();
			jo1.put("title", c.getClassName() + c.getContentName() + "箱");
			jo1.put("field", b);
			//jo1.put("edit", "text");
			jo1.put("width", "130");
			jo1.put("event", "setSign");
			ja.add(jo1);
			// 判断是否需要合计
			String maxContentCode = cutProductService.queryMaxcontentCodeOfClassCode(c.getClassCode());
			int i = Integer.parseInt(maxContentCode)/100;
			int minContentCode = i*100+1;
			if (c.getContentCode().equals(maxContentCode)) {
				// 合计----xx合计的字段名称以C开头
				String d = "A".equals(converRule) ? "C" + minContentCode : "C" + minContentCode + "MASTER";
				JSONObject jo2 = new JSONObject();
				jo2.put("title", c.getClassName() + "合计");
				jo2.put("field", d);
				jo2.put("width", "130");
				jo2.put("event", "setSign");
				ja.add(jo2);
				// 差异----xx差异的字段名称以D开头
				String k = "A".equals(converRule) ? "D" + minContentCode : "D" + minContentCode + "MASTER";
				JSONObject jo19 = new JSONObject();
				jo19.put("title", c.getClassName() + "差异");
				jo19.put("field", k);
				jo19.put("width", "130");
				jo19.put("event", "setSign");
				ja.add(jo19);
			}
		}
		JSONObject jo4 = new JSONObject();
		jo4.put("title", "物料编号");
		jo4.put("field", "mateCode");
		jo4.put("width", "145");
		ja.add(jo4);
		JSONObject jo18 = new JSONObject();
		jo18.put("title", "备注");
		jo18.put("field", "remark");
		jo18.put("width", "145");
		jo18.put("edit", "text");
		ja.add(jo18);

		return ja.toJSONString();
	}

	/**
	 * 根据月份查询
	 * 获取属于这个供应商的所有打切品
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllCutLiaiMate")
	public Map<String,Object>  queryAllCutLiaiMate(String suppId,String cutMonth,String headerFiled) {
		Map<String,Object> result = new HashMap<String,Object>();
		//只有新建打切联络单的时候需要校验
		boolean b = cutLiaisonService.checkoutCutLiaiByMonthAndSuppId(cutMonth,suppId);
		if(!b){
			result.put("judge", false);
			result.put("msg", cutMonth+"已存在打切联络单，相同年月无法创建多个打切联络单。");
			return result;
		}
		Map<String,Object>  map = cutProductService.queryMateOfCutLiai(suppId,cutMonth,headerFiled);
		return map;
	}
	/**
	 * 新增联络单
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value ="保存/提交打切联络单")
	@ResponseBody
	@RequestMapping("/addCutLiaison")
	public boolean addCutLiaison(CutLiaison cutLiai,String cutLiaiMateData,String type){
		SysUserDO user = UserCommon.getUser();
		List<CutLiaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutLiaiMate.class);
		String liaiCode = codeService.getCodeByCodeType("cutLiaiNo");
		cutLiai.setLiaiCode(liaiCode);
		cutLiai.setCreateId(user.getUserId().toString());
		cutLiai.setCreator(user.getName());
		cutLiai.setIsSpecial("NO");
		if("1".equals(type)){
			cutLiai.setStatus("已保存");
		}else{
			//cutLiai.setStatus("已提交");
			cutLiai.setStatus("已保存");
		}
		return cutLiaisonService.addCutLiaison(cutLiai,list);
	}
	/**
	 * 删除打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value ="删除打切联络单")
	@ResponseBody
	@RequestMapping("/deleteCutLiaisonByliaiIds")
	public boolean deleteCutLiaisonByliaiIds(String[] liaiIds){
		return cutLiaisonService.deleteCutLiaisonByliaiIds(liaiIds);
	}
	/**
	 * 提交打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value ="提交打切联络单")
	@ResponseBody
	@RequestMapping("/updateCutLiaiStatusByliaiIds")
	public boolean updateCutLiaiStatusByliaiIds(String liaiIds){
		List<String> liaiId = JsonUtils.jsonToList(liaiIds, String.class);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("liaiIds", liaiId);
		map.put("size", liaiId.size());
		map.put("status", "已提交");
		return cutLiaisonService.updateCutLiaiStatusByliaiIds(map);
	}
	
	/**
	 * 跳转到打切联络单编辑页面
	 * @param liaiId
	 * @return
	 */
	@RequestMapping("/getContactSheetEditHtml")
	public String getContactSheetEditHtml(Model model,String liaiId){
		CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
		model.addAttribute("cutLiai", cutLiai);
		model.addAttribute("cite", "no");
		return "bam/placut/ContactSheet/ContactSheetEdit";
	}
	/**
	 * 跳转到打切联络单编辑页面
	 * @param liaiId
	 * @return
	 */
	@RequestMapping("/getContactSheetCheckHtml")
	public String getContactSheetCheckHtml(Model model,String liaiId){
		CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
		model.addAttribute("cutLiai", cutLiai);
		return "bam/placut/ContactSheet/ContactSheetCheck";
	}
	
	/**
	 * 获取打切联络单的物料信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getData")
	@ResponseBody
	public Map<String, Object> getData(String id){
		Map<String, Object> map = cutLiaisonService.getContactSheet(id);
		return map;
	}
	/**
	 * 解析获取字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryLiaiMateFields")
	public String queryLiaiMateFields(String liaiId){
		JSONArray ja = cutLiaisonService.queryLiaiMateFields(liaiId);
		return ja.toJSONString();
	}
	
	/**
	 *
	 * 跳转打切联络单确认页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getContactConfirmListHtml")
	public String getContactConfirmListHtml(Model model) {
		List<Dic> cutLiaiStatusList = basicService.findDicListByCategoryCode("CUTLIAISTATUS");
		model.addAttribute("cutLiaiStatusList", cutLiaiStatusList);
		return "bam/placut/ContactConfirm/ContactConfirmList";
	} 
	
	/**
	 * 打切联络单确认列表数据
	 * @return
	 */
	@Log(value ="获取打切联络单确认列表")
	@ResponseBody
	@RequestMapping("/queryCutLiaisonForManageByPage")
	public Map<String,Object> queryCutLiaisonForManageByPage(Integer limit, Integer page, CutLiaison cutLiai){
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("cutLiai", cutLiai);
		Map<String, Object> page2 = cutLiaisonService.queryCutLiaisonForManageByPage(map);
		return page2;
	} 
	
	/**
	 * type=1：表示跳转到确认页面
	 * type=2:表示跳转到查看页面
	 * @param liaiId
	 * @return
	 */
	@RequestMapping("/getContactConfirmDetailHtml")
	public String getContactConfirmDetailHtml(String liaiId,String type,Model model){
		CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
		model.addAttribute("cutLiai", cutLiai);
		model.addAttribute("type", type);
		return "bam/placut/ContactConfirm/ContactConfirmDetail";
	}
	
	/**
	 * 确认打切联络单
	 * @param liaiIdJson
	 * @return
	 */
	@Log(value ="确认打切联络单")
	@ResponseBody
	@RequestMapping("/updateStatusOfCutLiaisonByLiaiId")
	public boolean updateStatusOfCutLiaisonByLiaiId(String liaiIdJson){
		List<String> liaiIds = JsonUtils.jsonToList(liaiIdJson, String.class);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("status","已确认");
		map.put("liaiIds", liaiIds);
		map.put("size", liaiIds.size());
		return cutLiaisonService.updateStatusOfCutLiaisonByLiaiId(map);
	}
	/**
	 * 退回打切联络单
	 * @param liaiIdJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateStatusOfCutLiaisonByLiaiId2")
	public boolean updateStatusOfCutLiaisonByLiaiId2(String liaiIdJson){
		List<String> liaiIds = JsonUtils.jsonToList(liaiIdJson, String.class);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("status","已退回");
		map.put("liaiIds", liaiIds);
		map.put("size", liaiIds.size());
		return cutLiaisonService.updateStatusOfCutLiaisonByLiaiId(map);
	}
	
	/**
	 * 编辑保存和提交
	 * type=1:表示保存
	 * type=2:表示提交
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value ="编辑/提交打切联络单")
	@ResponseBody
	@RequestMapping("/udpateCutLiaiMate")
	public Map<String,Object> udpateCutLiaiMate(CutLiaison cutLiai,String cutLiaiMateData,String type){
		if("已退回".equals(cutLiai.getStatus())){
			Map<String,Object> result = new HashMap<String,Object>();
			CutLiaison cutLiaison = cutLiaisonService.queryCutLiaisonByLiaiId(cutLiai.getLiaiId());
			String cutMonth = cutLiaison.getCutMonth();
			String suppId = cutLiaison.getSuppId();
			//只有新建打切联络单的时候需要校验
			boolean b = cutLiaisonService.checkoutCutLiaiByMonthAndSuppId(cutMonth,suppId);
			if(!b){
				result.put("judge", false);
				result.put("msg", cutMonth+"已存在打切联络单，相同年月无法创建多个打切联络单。");
				return result;
			}
		}
		List<CutLiaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutLiaiMate.class);
		return cutLiaisonService.udpateCutLiaiMate(cutLiai,list,type);
	}
	/**
	 * 获取一个类中最大的内容号
	 * @param contentCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryMaxContentCode")
	public String queryMaxContentCode(String contentCode){
		return cutProductService.queryMaxContentCode(contentCode);
	}
	
	/**
	 * 导出打切联络单
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportCutLiaison")
	public String exportCutLiaison(String liaiId,HttpServletRequest req,HttpServletResponse res){
		ServletOutputStream os=null;
		try{
			//获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			//文件名从称
			SysUserDO user = UserCommon.getUser();
			String fileName = user.getName()+"-打切联络单-"+time+".xls";
			//根据费用编号的查询费用信息，返回一个List集合
			CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
			Map<String, Object> map = cutLiaisonService.getContactSheet(liaiId);
			//获取物料列表的表头信息
			JSONArray jay = cutLiaisonService.queryLiaiMateFields(liaiId);
			JSONArray ja = (JSONArray) map.get("data");
			String sheetName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			//新建文件
			HSSFWorkbook wb =new HSSFWorkbook();
			//设置列头样式
			HSSFCellStyle columnHeadStyle  = wb.createCellStyle();
			columnHeadStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
			//新建工作表
			//HSSFSheet sheet = wb.createSheet(sheetName);
			ExportExcel.createExcelOfCutLiai(jay,cutLiai,ja,wb);
			ExportExcel.setAttachmentFileName(req, res, fileName);
			os= res.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 跳转到打切联络单任务流程审核页面
	 * @param model
	 * @param sdata1
	 * @param taskName
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/getCutLiaiAudtiHtml")
	public String getCutLiaiAudtiHtml(Model model ,TaskParamsDO taskPDO){
		CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(taskPDO.getSdata1());
		model.addAttribute("cutLiai", cutLiai);
		model.addAttribute("taskPDO",taskPDO);
		return "bam/placut/ContactSheet/CutLiaiAudit";
	}
	
	
	/**
	 * 弹出窗口 ，选择成品物料
	 * @return
	 */
	@RequestMapping("/getFinMateListHtml")
	public String getFinMateListHtml(){
		//SPECUTLIAISTATUS
		return "bam/placut/ContactSheet/suppMateList";
	}
	
	@ResponseBody
	@RequestMapping("/queryFinMaterialByPage")
	public Map<String,Object> queryFinMaterialByPage(Integer limit,Integer page,Material mate){
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		map.put("start", start);
		map.put("end", end);
		map.put("mate", mate);
		Map<String, Object> page2 = cutLiaisonService.queryFinMaterialByPage(map);
		return page2;
	}
	/**
	 * 根据成品物料查询半成品物料信息
	 * @param mateCodeJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querySemiFinMateByMateCode")
	public Map<String,Object> querySemiFinMateByMateCode(String mateCodeJson){
		List<String> mateCodes = JsonUtils.jsonToList(mateCodeJson, String.class);
		return cutLiaisonService.querySemiFinMateByMateCode(mateCodes);
	}
	/**
	 * 已保存，已退回的打切联络单，更新物料信息
	 * @param suppId
	 * @param cutMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateCutLiaiMateBySuppAndMonth")
	public Map<String, Object> updateCutLiaiMateBySuppAndMonth(String suppId,String cutMonth,String liaiId,String headerFiled){
		return cutLiaisonService.updateCutLiaiMateBySuppAndMonth(suppId,cutMonth,liaiId,headerFiled);
	}

	/**
	 * 作废已确认的打切联络单
	 * @param liaiIdJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancellCutLiaisonByLiaiIds")
	public boolean cancellCutLiaisonByLiaiIds(String liaiIdJson) {
		List<String> list = JsonUtils.jsonToList(liaiIdJson, String.class);
		return cutLiaisonService.cancellCutLiaisonByLiaiIds(list);
	}
	
	/**
	 * 跳转到引用创建打切联络单页面
	 * @param liaiId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCiteCutLiaiHtml")
	public String name(String liaiId,Model model) {
		CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
		cutLiai.setStatus("");
		cutLiai.setLiaiCode("");
		cutLiai.setCreateDate(new Date());
		model.addAttribute("cutLiai", cutLiai);
		model.addAttribute("cite", "citeCutLiai");
		return "bam/placut/ContactSheet/ContactSheetEdit";
	}
	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param suppId
	 * @param cutMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkoutCutLiaiByMonthAndSuppId")
	public boolean checkoutCutLiaiByMonthAndSuppId(String suppId,String cutMonth) {
		return cutLiaisonService.checkoutCutLiaiByMonthAndSuppId(cutMonth, suppId);
	}
	
	
}
