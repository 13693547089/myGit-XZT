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

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.CutPlan;
import com.faujor.entity.bam.CutPlanMate;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.CutPlanService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.bam.SaleFcstService;
import com.faujor.service.common.CodeService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

import jcifs.dcerpc.msrpc.netdfs;

@Controller
@RequestMapping("/cutPlan")
public class CutPlanController {
	@Autowired
	private CutPlanService cutPlanService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private SaleFcstService saleFcstService;
	
	/**
	 * 获取打切计划管理页
	 * @return
	 */
	@RequestMapping("/getCutPlanIndex")
	public String getCutPlanIndex(){
		return "bam/cutPlan/cutPlanIndex";
	}
	/**
	 * 获取打切计划编辑查看页面 1新增 2 编辑 3查看
	 * @param type
	 * @param planId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCutPlanEditPage")
	public String getCutPlanEditPage(String type,String planId,Model model){
		CutPlan cutPlan=new CutPlan();
		List<String> cutPlanCodes = cutPlanService.queryCutPlanCodeListByStatus();
		if(!StringUtils.isEmpty(type) && !"1".equals(type)){//编辑，查看
			cutPlan = cutPlanService.getCutPlanByPlanId(planId);
			//获取已作废的打切计划单号
			//cutPlanCodes = cutPlanService.queryCutPlanListByCutMonth(cutPlan.getCutMonth());
		}else{//新建
			Date date = new Date();
			String cutMonth = DateUtils.format(date, "yyyy-MM");
			cutPlan.setCutMonth(cutMonth);
			cutPlan.setCreateDate(date);
			//获取已作废的打切计划单号
			//cutPlanCodes = cutPlanService.queryCutPlanListByCutMonth(cutMonth);
		}
		model.addAttribute("cutPlanCodeList", cutPlanCodes);
		model.addAttribute("cutPlan", cutPlan);
		model.addAttribute("type", type);
		return "bam/cutPlan/cutPlanEdit";
	}
	/**
	 * 分页获取打切计划
	 * @param cutMonth
	 * @param page
	 * @return
	 */
	@Log(value ="获取打切计划列表")
	@ResponseBody
	@RequestMapping("/getCutPlanByPage")
	public LayuiPage<CutPlan> getCutPlanByPage(String cutMonth,String cutPlanCode,String status,LayuiPage<CutPlan> page){
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cutMonth", cutMonth);
		params.put("cutPlanCode", cutPlanCode);
		params.put("status", status);
		page.calculatePage();
		params.put("page", page);
		LayuiPage<CutPlan> returnPage = cutPlanService.getCutPlanByPage(params);
		return returnPage;
	}
	/**
	 * 保存
	 * @param type
	 * @param cutPlan
	 * @param detailJson
	 * @return
	 */
	@Log(value ="新建/编辑打切计划")
	@ResponseBody
	@RequestMapping("/saveCutPlan")
	public RestCode saveCutPlan(String type,CutPlan cutPlan,String detailJson){
		List<CutPlanMate> mates = JsonUtils.jsonToList(detailJson, CutPlanMate.class);
		if("1".equals(type)){
			String cutPlanCode = codeService.getCodeByCodeType("cutPlanNo");
			cutPlan.setCutPlanCode(cutPlanCode);
			cutPlanService.saveCutPlan(cutPlan, mates);
		}else{
			cutPlanService.udateCutPlan(cutPlan, mates);
		}
		return new RestCode().put("data", cutPlan);
	}
	
	/**
	 * 更新打切计划的状态
	 * @param jsonIds
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeCutPlanStatus")
	public RestCode changeCutPlanStatus(String jsonIds,String status){
		List<String> planIds = JsonUtils.jsonToList(jsonIds, String.class);
		cutPlanService.changeCutPlanStatus(planIds, status);
		return new RestCode();
	}
	/**
	 * 删除打切计划
	 * @param jsonIds
	 * @return
	 */
	@Log(value ="删除打切计划")
	@ResponseBody
	@RequestMapping("/delCutPlans")
	public RestCode delCutPlans(String jsonIds){
		List<String> planIds = JsonUtils.jsonToList(jsonIds, String.class);
		cutPlanService.delCutPlan(planIds);
		return new RestCode();
	}
	/**
	 * 根据打切计划id 获取打切计划物料
	 * @param planId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCutPlanMateByPlanId")
	public List<CutPlanMate> getCutPlanMateByPlanId(String planId){
		List<CutPlanMate> mates = cutPlanService.getCutPlanMateByPlanId(planId);
		return mates;
	}
	/**
	 * 根据年月获取物料
	 * @param planMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCutPlanMateByPlanMonth")
	public Map<String, Object> getCutPlanMateByPlanMonth(String cutMonth){
		Map<String, Object> map  = new HashMap<String,Object>();
		//根据月份查询已保存和已提价的打切计划单
		List<String> cutPlanCodes = cutPlanService.queryCutPlansByCutMonth(cutMonth);
		if(cutPlanCodes.size()>0){
			map.put("judge", false);
			map.put("msg", cutMonth+"存在已保存或已提交的打切计划,请作废后再新建");
			return map;
		}
		// 获取激活状态下的销售预测的字段
		Map<String, Object> sMap = new HashMap<String, Object>();
		sMap.put("status", "激活");
		List<SaleForecast> saleList = saleFcstService.getSaleFcstByCondition(sMap);
		
		String[] currYm = cutMonth.split("-");
		int mYear = Integer.parseInt(currYm[0].trim());
		int mMonth = Integer.parseInt(currYm[1].trim());
		
		String saleId = "";
		String columnName = "'0' as q1,'0' as q2,'0' as q3,'0' as q4,'0' as q5";
		if(saleList.size()>0){
			SaleForecast saleFore = saleList.get(0);
			// 预测id
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
			
			columnName = "";
			// 计算与初始的年月相差月份
			int qty = (mYear-sY)*12+mMonth-sM;
			if(qty>=0 && qty<24){
				for(int i=1;i<=5;i++){
					if(qty+i+1>12){
						if(qty-12+i+1<=12){
							columnName += "sale_Fore"+(qty-12+i+1)+" as q"+i+",";
						}else{
							// 超过12
							columnName += "sale_Fore"+(qty-12+i+1-12)+" as q"+i+",";
						}
					}else{
						columnName += "sale_Fore_Qty"+(qty+i+1)+" as q"+i+",";
					}
				}
			}else if(qty>=24){
				columnName = "'0' as q1,'0' as q2,'0' as q3,'0' as q4,'0' as q5,";
			}else{
				for(int i=1;i<=5;i++){
					if(qty+i+1<1){
						columnName+="'0' as q"+i+",";
					}else{
						columnName+="sale_Fore_Qty"+(qty+i+1)+" as q"+i+",";
					}
				}
			}
			// 字段
			columnName = columnName.substring(0,columnName.length()-1);
		}
		
		List<CutPlanMate> mates =cutPlanService.getCutPlanMateFromLiaison(cutMonth,columnName,saleId);
		map.put("data", mates);
		map.put("judge", true);
		return map;
	}
	
	/**
	 * 导出打切计划
	 * @return
	 */
	@RequestMapping("/exportCutPlan")
	public String exportCutPlan(String planId,HttpServletRequest req,HttpServletResponse res){
		ServletOutputStream os=null;
		try{
			//获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = sdf.format(date);
			//文件名从称
			SysUserDO user = UserCommon.getUser();
			String fileName = user.getUsername()+"-打切计划信息-"+time+".xls";
			//根据费用编号的查询费用信息，返回一个List集合
			CutPlan cutPlan = cutPlanService.getCutPlanByPlanId(planId);
			List<CutPlanMate> list = cutPlanService.getCutPlanMateByPlanId(planId);
			String sheetName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			//新建文件
			HSSFWorkbook wb =new HSSFWorkbook();
			//设置列头样式
			HSSFCellStyle columnHeadStyle  = wb.createCellStyle();
			columnHeadStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
			//新建工作表
			HSSFSheet sheet = wb.createSheet(sheetName);
			ExportExcel.createExcelOfCutPlan(cutPlan,list,sheet);
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
	 * 筛选物料
	 * @return
	 */
	
	@RequestMapping("/matRepeat")
	public String matRepeat(){
		return "/bam/cutPlan/matRepeat";
	}
	/**
	 * 根据月份查询已作废的打切计划的单号集合
	 * @param cutMonth
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping("/queryCutPlanCodesByCutMonth")
	public List<String> queryCutPlanCodesByCutMonth(String cutMonth){
		List<String> list = cutPlanService.queryCutPlanListByCutMonth(cutMonth);
		return list;
	}*/
	/**
	 * 根据引用的打切计划单号，引入物料的打切进度
	 * @param cutMonth
	 * @param citeCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCutScheOfCutPlanMate")
	public Map<String, Object> getCutScheOfCutPlanMate(String cutMonth,String citeCode){
		Map<String, Object> map  = new HashMap<String,Object>();
		//根据月份查询已保存和已提价的打切计划单
		List<String> cutPlanCodes = cutPlanService.queryCutPlansByCutMonth(cutMonth);
		if(cutPlanCodes.size()>0){
			map.put("judge", false);
			map.put("msg", cutMonth+"存在已保存或已提交的打切计划,请作废后再新建");
			return map;
		}
		return cutPlanService.getCutScheOfCutPlanMate(cutMonth,citeCode);
	}
	/**
	 * 根据月份查询已保存和已提价的打切计划单
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryCutPlansByCutMonth")
	public Map<String, Object> queryCutPlansByCutMonth(String cutMonth){
		Map<String, Object> map  = new HashMap<String,Object>();
		//根据月份查询已保存和已提价的打切计划单
		List<String> cutPlanCodes = cutPlanService.queryCutPlansByCutMonth(cutMonth);
		if(cutPlanCodes.size()>0){
			map.put("judge", false);
			map.put("msg", cutMonth+"存在已保存或已提交的打切计划,请作废后再提交");
		}else{
			map.put("judge", true);
		}
		return map;
	}
	
	
}
