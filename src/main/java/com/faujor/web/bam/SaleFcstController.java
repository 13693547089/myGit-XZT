package com.faujor.web.bam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ctc.wstx.util.StringUtil;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.bam.psm.SaleFcstDetail;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.SaleFcstService;
import com.faujor.service.common.CodeService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

/**
 * 销售预测 控制类
 * @author Vincent
 *
 */
@Controller
@RequestMapping(value = "/bam/sf")
public class SaleFcstController {
	
	@Autowired
	private SaleFcstService saleFcstService;
	
	@Autowired
	private CodeService codeService;
	
	/**
	 * 销售预测列表
	 * @param model
	 * @return
	 */
	@Log(value = "销售预测列表信息")
	@RequestMapping("/saleFcstPage")
	public String saleFcstPage(Model model){
		
		/*List<Dic> statusList = basicService.findDicListByCategoryCode("PS_STATUS");
		model.addAttribute("statusList", statusList);*/
		
		return "bam/saleFcst/saleFcstList";
	}
	
	/**
	 * 销售预测列表分页查询
	 * @param page
	 * @param search_planName
	 * @param search_status
	 * @param search_crtDateStart
	 * @param search_crtDateEnd
	 * @return
	 */
	@Log(value = "获取生产/交货计划列表信息")
	@ResponseBody
	@RequestMapping("/getSaleFcstPageList")
	public LayuiPage<SaleForecast> getSaleFcstPageList(LayuiPage<SaleForecast> page,String search_fsctName,String search_crtDateStart,String search_crtDateEnd){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("fsctName", search_fsctName);
		params.put("crtDateStart",search_crtDateStart);
		params.put("crtDateEnd",search_crtDateEnd);
		
		return saleFcstService.getSaleFcstByPage(params);
	}
	
	/**
	 * 删除销售预测
	 * @param id
	 * @return
	 */
	@Log(value = "删除单个销售预测列表信息")
	@ResponseBody
	@RequestMapping("/deleteSaleFcst")
	public RestCode deleteSaleFcst(String id){
		RestCode restCode = new RestCode(); 
		try {
			saleFcstService.delSaleFcstById(id);
		} catch (Exception e) {
			restCode = RestCode.error();
		}
		return restCode;
	}
	
	/**
	 * 批量删除删除销售预测
	 * @param ids
	 * @return
	 */
	@Log(value = "删除批量销售预测列表信息")
	@ResponseBody
	@RequestMapping("/deleteBatchSaleFcst")
	public RestCode deleteBatchSaleFcst(String ids){
		
		List<String> list = JSON.parseArray(ids, String.class);

		RestCode restCode = new RestCode(); 
		try {
			saleFcstService.delBatchSaleFcstInfoByIds(list);
		} catch (Exception e) {
			restCode = RestCode.error();
		}
		return restCode;
	}
	
	/**
	 * 销售预测明细列表
	 * @param model
	 * @param mainId
	 * @param type 类型： 1：编辑或创建 ，2：查看
	 * @return
	 */
	@Log(value = "创建/编辑/查看 销售预测明细")
	@RequestMapping("/saleFcstDetailPage")
	public String saleFcstDetailPage(Model model,String mainId,String type){

		model.addAttribute("type", type);

		// 是否为导入的情况 1：是，0：否
		model.addAttribute("isImport", 0);
		
		SaleForecast saleFcst = new SaleForecast();
		if(mainId == null || mainId.equals("")){
			//******* 创建
			// 获取当前用户
			SysUserDO user = UserCommon.getUser();
			// 设置创建人
			saleFcst.setCrtUser(user.getName());
			saleFcst.setCrtUserCode(user.getUsername());
			// id
			saleFcst.setId(UUIDUtil.getUUID());
			// FsctCode
			saleFcst.setFsctCode("");
			// 状态
			saleFcst.setStatus("");
			
			saleFcst.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
		}else{
			//****** 编辑或查看
			saleFcst = saleFcstService.getSaleFcstById(mainId);
			// 判断为导入的情况下
			if(saleFcst.getFsctName().toLowerCase().contains(".xls") 
					|| saleFcst.getFsctName().toLowerCase().contains(".xlsx")){
				model.addAttribute("isImport",1);
			}
		}
		
		// 设置主表ID
		model.addAttribute("mainId", saleFcst.getId());
		// 主表信息
		model.addAttribute("list", saleFcst);
		
		return "bam/saleFcst/saleFcstDetail";
	}
	
	/**
	 * 销售预测明细数据获取
	 * @param mainId
	 * @return
	 */
	@Log(value = "获取销售预测明细数据")
	@ResponseBody
	@RequestMapping("/getSaleFcstDetailList")
	public List<SaleFcstDetail> getSaleFcstDetailList(String mainId){
		return saleFcstService.getSaleFcstDetailListByMainId(mainId);
	}
	
	/**
	 * 保存销售预测信息
	 * @param page
	 * @param mainId
	 * @param isImport 是否为导入数据 1：导入数据，0：不是导入数据
	 * @return
	 */
	@Log(value = "保存销售预测列表信息")
	@ResponseBody
	@RequestMapping("/saveSaleFcstInfo")
	public RestCode saveSaleFcstInfo(SaleForecast saleFcst,String saleFcstDetailData,int isImport){
		// 判断年度是否已存在
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fsctYear", saleFcst.getFsctYear());
		map.put("nonId", saleFcst.getId());
		int rs = saleFcstService.getSaleFcstCount(map);
		if(rs>0){
			return RestCode.error(90, "已存在该年度区间的销售预测！");
		}
		
		// 设置编码
		if(saleFcst.getFsctCode()==null || saleFcst.getFsctCode().equals("")){
			// fsctCode , 销售预测编码规则代码 ：fsctCode
			String fsctCode =codeService.getCodeByCodeType("saleFcstNo");
			saleFcst.setFsctCode(fsctCode);
		}

		// 明细数据
		List<SaleFcstDetail> detailList = dealDetailData(saleFcstDetailData);
		
		try {
			saleFcstService.saveSaleFcstInfo(saleFcst, detailList,isImport);
		} catch (Exception e) {
			return RestCode.error(80,e.getMessage());
		}
		
		return RestCode.ok(0,saleFcst.getFsctCode());
	}
	
	/**
	 * 处理明细数据
	 * @param 
	 * @return
	 */
	private List<SaleFcstDetail> dealDetailData(String saleFcstDetailData){
		// 明细数据转成list
		List<SaleFcstDetail> detailList = JSON.parseArray(saleFcstDetailData, SaleFcstDetail.class);
		for(int i=0;i<detailList.size();i++){
			SaleFcstDetail item = detailList.get(i);
			
			// 设置明细ID
			item.setId(UUIDUtil.getUUID());
			
			item.setSaleForeQty1(item.getSaleForeQty1()==null?new BigDecimal("0"):item.getSaleForeQty1());
			item.setSaleForeQty2(item.getSaleForeQty2()==null?new BigDecimal("0"):item.getSaleForeQty2());
			item.setSaleForeQty3(item.getSaleForeQty3()==null?new BigDecimal("0"):item.getSaleForeQty3());
			item.setSaleForeQty4(item.getSaleForeQty4()==null?new BigDecimal("0"):item.getSaleForeQty4());
			item.setSaleForeQty5(item.getSaleForeQty5()==null?new BigDecimal("0"):item.getSaleForeQty5());
			item.setSaleForeQty6(item.getSaleForeQty6()==null?new BigDecimal("0"):item.getSaleForeQty6());
			item.setSaleForeQty7(item.getSaleForeQty7()==null?new BigDecimal("0"):item.getSaleForeQty7());
			item.setSaleForeQty8(item.getSaleForeQty8()==null?new BigDecimal("0"):item.getSaleForeQty8());
			item.setSaleForeQty9(item.getSaleForeQty9()==null?new BigDecimal("0"):item.getSaleForeQty9());
			item.setSaleForeQty10(item.getSaleForeQty10()==null?new BigDecimal("0"):item.getSaleForeQty10());
			item.setSaleForeQty11(item.getSaleForeQty11()==null?new BigDecimal("0"):item.getSaleForeQty11());
			item.setSaleForeQty12(item.getSaleForeQty12()==null?new BigDecimal("0"):item.getSaleForeQty12());
			
			item.setSaleFore1(item.getSaleFore1()==null?new BigDecimal("0"):item.getSaleFore1());
			item.setSaleFore2(item.getSaleFore2()==null?new BigDecimal("0"):item.getSaleFore2());
			item.setSaleFore3(item.getSaleFore3()==null?new BigDecimal("0"):item.getSaleFore3());
			item.setSaleFore4(item.getSaleFore4()==null?new BigDecimal("0"):item.getSaleFore4());
			item.setSaleFore5(item.getSaleFore5()==null?new BigDecimal("0"):item.getSaleFore5());
			item.setSaleFore6(item.getSaleFore6()==null?new BigDecimal("0"):item.getSaleFore6());
			item.setSaleFore7(item.getSaleFore7()==null?new BigDecimal("0"):item.getSaleFore7());
			item.setSaleFore8(item.getSaleFore8()==null?new BigDecimal("0"):item.getSaleFore8());
			item.setSaleFore9(item.getSaleFore9()==null?new BigDecimal("0"):item.getSaleFore9());
			item.setSaleFore10(item.getSaleFore10()==null?new BigDecimal("0"):item.getSaleFore10());
			item.setSaleFore11(item.getSaleFore11()==null?new BigDecimal("0"):item.getSaleFore11());
			item.setSaleFore12(item.getSaleFore12()==null?new BigDecimal("0"):item.getSaleFore12());

			item.setSumSaleFore1(item.getSumSaleFore1()==null?new BigDecimal("0"):item.getSumSaleFore1());
			item.setSumSaleFore2(item.getSumSaleFore2()==null?new BigDecimal("0"):item.getSumSaleFore2());
			item.setSumSaleFore3(item.getSumSaleFore3()==null?new BigDecimal("0"):item.getSumSaleFore3());
		}
		
		return detailList;
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
	 * 导出销售预测
	 * @param request
	 * @param response
	 */
	@Log(value = "导出销售预测信息")
	@ResponseBody
	@RequestMapping("/exportSaleFcstInfo")
	public void exportSaleFcstInfo(HttpServletRequest request, HttpServletResponse response,
			String id,String year){
		response.setContentType("text/html");
		
		Workbook wb;
		try {
			
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN_TWO);
			
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath+"templates\\excelTemp\\saleFcstTemp.xls";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			
			String fileName = year+"月份销售预测报表"+currDate;
			// 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
			
			Sheet sheet = wb.getSheetAt(0);
			// sheet 名称改变
			wb.setSheetName(0, currDate);
			
			// 设置标题
			ExcelUtil.setValue(sheet, 0, 0, year+"月份销售预测", null);
			
			// 列头获取
			String[] yearArr = year.split("~");
			String sYm = yearArr[0].trim();
			String eYm = yearArr[1].trim();
			
			String[] sYmArr = sYm.split("-");
			String sYear = sYmArr[0];
			String sMonth = sYmArr[1];
			int sY = Integer.parseInt(sYear);
			int sM = Integer.parseInt(sMonth);
			
			String[] eYmArr = eYm.split("-");
			String eYear = eYmArr[0];
			String eMonth = eYmArr[1];
			int eM = Integer.parseInt(eMonth);
			
			String mYear = String.valueOf(sY+1);
			String syy = sYear.substring(sYear.length()-2,sYear.length());
			String myy = mYear.substring(mYear.length()-2,mYear.length());
			String eyy = eYear.substring(eYear.length()-2,eYear.length());
			
			String[] columnName = new String[27];
			String[] getMethods = new String[27];
			if(sMonth.equals("01")){
				for(int j=0;j<12;j++){
					columnName[j] = syy+"年"+(j+1)+"月销售预测";
					getMethods[j] = "getSaleForeQty"+(j+1);
				}
				columnName[12] = syy+"年销售预测总计";
				getMethods[12] = "getSumSaleFore1";
				for(int j=1;j<=12;j++){
					columnName[12+j] = eyy+"年"+j+"月销售预测";
					getMethods[12+j] = "getSaleFore"+j;
				}
				columnName[25] = eyy+"年销售预测总计";
				getMethods[25] = "getSumSaleFore2";
				columnName[26] = "";
				getMethods[26] = "getSumSaleFore3";
			}else{
				int ssi = 12-sM;
				// 初始年份
				for(int j=0;j<=ssi;j++){
					columnName[j] = syy+"年"+(sM+j)+"月销售预测";
					getMethods[j] = "getSaleForeQty"+(j+1);
				}
				columnName[ssi+1] = syy+"年销售预测总计";
				getMethods[ssi+1] = "getSumSaleFore1";
				// 中间年份
				for(int j=1;j<=12;j++){
					columnName[ssi+j+1] = myy+"年"+j+"月销售预测";
				}
				
				// 取值方法
				for(int j=1;j<=eM;j++){
					getMethods[ssi+j+1] = "getSaleForeQty"+(ssi+j+1);
				}
				for(int j=1;j<=12-eM;j++){
					getMethods[12+j] = "getSaleFore"+j;
				}
				getMethods[12-eM+12+1] = "getSumSaleFore2";
				
				columnName[ssi+12+2] = myy+"年销售预测总计";
				// 结束年份
				for(int i=1;i<=eM;i++){
					columnName[ssi+i+14] = eyy+"年"+i+"月销售预测";
					
					getMethods[ssi+i+14] = "getSaleFore"+(12-eM+i);
				}
				
				columnName[26] = eyy+"年销售预测总计";
				getMethods[26] = "getSumSaleFore3";
			}
			
			//***** 设置列头 *****
			
			int sColIndex = 4;
			// 设置列头
			for(int i=1;i<=27;i++){
				ExcelUtil.setValue(sheet, 1, sColIndex+i, columnName[i-1], null);
			}
			
			// 获取明细数据
			List<SaleFcstDetail> list = saleFcstService.getSaleFcstDetailListByMainId(id);
			
			// 获取汇总明细数据
			List<SaleFcstDetail> sumList = saleFcstService.getSaleFcstSumByMainId(id);
			int sumSize = sumList.size();
			
			int size= list.size();
			if(size-1>0){
				// 插入行数 + 汇总行
				ExcelUtil.insertRow(sheet, 2, size-1+sumSize);
			}
			
			int startRow = 2;
			// 调用get方法
			Method method;
			Class<? extends SaleFcstDetail> detailClass = SaleFcstDetail.class;
			
			// 统计行字体处理
			Font sumFont = wb.createFont();
			sumFont.setFontName("微软雅黑");    
			sumFont.setFontHeightInPoints((short) 10);//设置字体大小
			sumFont.setBold(true);
			// 统计行样式
			CellStyle sumCellStyle0 = wb.createCellStyle();
			CellStyle sumStyle0 = ExcelUtil.getCellStyle(sheet, 2, 1);
			sumCellStyle0.cloneStyleFrom(sumStyle0);
			sumCellStyle0.setFont(sumFont);
			
			CellStyle sumCellStyle1 = wb.createCellStyle();
			CellStyle sumStyle1 = ExcelUtil.getCellStyle(sheet, 2, 5);
			sumCellStyle1.cloneStyleFrom(sumStyle1);
			sumCellStyle1.setFont(sumFont);
			// 大品项统计起始
			int sumIndex = 0;
			// 汇总行所在行下标，初始
			int sumC = sumList.get(0).getSumnum();
			
			// 总计
			Map<Integer,Long> sumTotal = new HashMap<Integer,Long>();
			
			for(int i=0;i<size+sumSize;i++){
				
				//*** 大品项汇总小计行添加  ***
				if(sumC == i){
					// 需要添加大品项汇总行
					SaleFcstDetail sumItem = sumList.get(sumIndex);
					
					ExcelUtil.setValue(sheet, startRow+i, 2, sumItem.getProdSeries(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 3, sumItem.getBigItemExpl(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 4, "小计", sumCellStyle0);
					
					// 大品项汇总行处理
					try {
						for(int z=0;z<getMethods.length;z++){
							method = detailClass.getMethod(getMethods[z]);
							// 得到值
							String vv = method.invoke(sumItem).toString();
							Object invoke = Math.round(Double.parseDouble(StringUtils.isEmpty(vv)?"0":vv));
							if(!sMonth.equals("01")){
								ExcelUtil.setValue(sheet, startRow+i, sColIndex+z+1, invoke, sumCellStyle1);
								// 设置总计
								if(!sumTotal.containsKey(sColIndex+z+1)){
									sumTotal.put(sColIndex+z+1, Long.parseLong(invoke.toString()));
								}else{
									long sum = sumTotal.get(sColIndex+z+1);
									sumTotal.put(sColIndex+z+1, Long.parseLong(invoke.toString())+sum);
								}
								
							}else{
								if(z==getMethods.length-1){
									continue;
								}
								ExcelUtil.setValue(sheet, startRow+i, sColIndex+z+1, invoke, sumCellStyle1);
								// 设置总计
								if(!sumTotal.containsKey(sColIndex+z+1)){
									sumTotal.put(sColIndex+z+1, Long.parseLong(invoke.toString()));
								}else{
									long sum = sumTotal.get(sColIndex+z+1);
									sumTotal.put(sColIndex+z+1, Long.parseLong(invoke.toString())+sum);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					/*// 设置统计行样式
					for(int xy=5;xy<=nextMCol+11*4+4;xy++){
						ExcelUtil.setCellStyle(sheet, startRow+i, xy, sumStyleMap.get(xy));
					}*/
					
					// 处理下次统计的行信息
					if(sumIndex<sumSize-1){
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
				
				SaleFcstDetail item = list.get(i-sumIndex);
				ExcelUtil.setValue(sheet, startRow+i, 0, item.getRank(), null);
				ExcelUtil.setValue(sheet, startRow+i, 1, item.getMatCode(), null);
				ExcelUtil.setValue(sheet, startRow+i, 2, item.getProdSeries(), null);
				ExcelUtil.setValue(sheet, startRow+i, 3, item.getBigItemExpl(), null);
				ExcelUtil.setValue(sheet, startRow+i, 4, item.getMatName(), null);
				
				try {
					for(int z=0;z<getMethods.length;z++){
						method = detailClass.getMethod(getMethods[z]);
						// 得到值
						String vv = method.invoke(item).toString();
						Object invoke = Math.round(Double.parseDouble(StringUtils.isEmpty(vv)?"0":vv));
						if(!sMonth.equals("01")){
							ExcelUtil.setValue(sheet, startRow+i, sColIndex+z+1, invoke, null);
						}else{
							if(z==getMethods.length-1){
								continue;
							}
							ExcelUtil.setValue(sheet, startRow+i, sColIndex+z+1, invoke, null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 总计行
			for(Integer indexSum:sumTotal.keySet()){
				ExcelUtil.setValue(sheet, startRow+size+sumSize, indexSum, sumTotal.get(indexSum), null);
			}
			
			// 导出excel
			OutputStream out = response.getOutputStream();
			ExcelUtil.exportExcel(wb, out);
			
		} catch (IOException e) {
		}
	}
	
	/**
	 * 导出销售预测
	 * @param request
	 * @param response
	 */
	@Log(value = "导入销售预测信息")
	@ResponseBody
	@RequestMapping("/importSaleFcstInfo")
	public RestCode importSaleFcstInfo(@RequestParam("file") MultipartFile file){
		
		
		try{
			if(!file.isEmpty()){
				String fileName = file.getOriginalFilename();
				String templateFileType = "";
				if(fileName.indexOf(".xlsx")>=0){
					templateFileType = ".xlsx";
				}else if(fileName.indexOf(".xls")>=0){
					templateFileType = ".xls";
				}else{
					RestCode.error(99, "请选择excel导入！");
				}
				
				InputStream impStream = file.getInputStream();
				
				Workbook wb = ExcelUtil.getWorkBook(impStream,templateFileType);
				
				Sheet sheet = wb.getSheetAt(0);
				// 获取起始行
				int startRow = 2;
				// 获取最后行
				int endRow = sheet.getLastRowNum();
				
				// 主表数据
				SaleForecast saleFcst = new SaleForecast();
				
				String id = UUIDUtil.getUUID();
				// 获取当前用户
				SysUserDO user = UserCommon.getUser();
				// 设置创建人
				saleFcst.setCrtUser(user.getName());
				saleFcst.setCrtUserCode(user.getUsername());
				// id
				saleFcst.setId(id);
				// 临时名称
				saleFcst.setFsctName(fileName);
				// FsctCode
				saleFcst.setFsctCode("");
				// 状态
				saleFcst.setStatus("未激活");
				saleFcst.setCrtDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
				
				// 明细数据
				List<SaleFcstDetail> list = new ArrayList<SaleFcstDetail>();
				for(int i=2;i<=endRow;i++){
					
					Row row = sheet.getRow(i);
					
					SaleFcstDetail detail = new SaleFcstDetail();
					
					detail.setId(UUIDUtil.getUUID());
					detail.setMainId(id);
					detail.setRank(row.getCell(0).getStringCellValue());
					detail.setMatCode(row.getCell(1).getStringCellValue());
					detail.setMatName(row.getCell(2).getStringCellValue());
					detail.setSaleForeQty1((row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(3).getNumericCellValue()));
					detail.setSaleForeQty2((row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(4).getNumericCellValue()));
					detail.setSaleForeQty3((row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(5).getNumericCellValue()));
					detail.setSaleForeQty4((row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(6).getNumericCellValue()));
					detail.setSaleForeQty5((row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(7).getNumericCellValue()));
					detail.setSaleForeQty6((row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(8).getNumericCellValue()));
					detail.setSaleForeQty7((row.getCell(9).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(9).getNumericCellValue()));
					detail.setSaleForeQty8((row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(10).getNumericCellValue()));
					detail.setSaleForeQty9((row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(11).getNumericCellValue()));
					detail.setSaleForeQty10((row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(12).getNumericCellValue()));
					detail.setSaleForeQty11((row.getCell(13).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(13).getNumericCellValue()));
					detail.setSaleForeQty12((row.getCell(14).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(14).getNumericCellValue()));
					
					detail.setSaleFore1((row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(15).getNumericCellValue()));
					detail.setSaleFore2((row.getCell(16).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(16).getNumericCellValue()));
					detail.setSaleFore3((row.getCell(17).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(17).getNumericCellValue()));
					detail.setSaleFore4((row.getCell(18).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(18).getNumericCellValue()));
					detail.setSaleFore5((row.getCell(19).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(19).getNumericCellValue()));
					detail.setSaleFore6((row.getCell(20).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(20).getNumericCellValue()));
					detail.setSaleFore7((row.getCell(21).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(21).getNumericCellValue()));
					detail.setSaleFore8((row.getCell(22).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(22).getNumericCellValue()));
					detail.setSaleFore9((row.getCell(23).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(23).getNumericCellValue()));
					detail.setSaleFore10((row.getCell(24).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(24).getNumericCellValue()));
					detail.setSaleFore11((row.getCell(25).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(25).getNumericCellValue()));
					detail.setSaleFore12((row.getCell(26).getCellType() == Cell.CELL_TYPE_BLANK)?new BigDecimal("-999.99"):new BigDecimal(row.getCell(26).getNumericCellValue()));
					
					list.add(detail);
				}
				// 保存导入数据
				saleFcstService.saveImpSaleFcstInfo(saleFcst, list);
			}
			
		}catch (Exception e) {
			return RestCode.error(99, "请检查导入文件的数据是否正确！");
		}
		
		return RestCode.ok();
	}
	
	/**
	 * 获取物料的月份预测数据
	 * @param matInfoData
	 * @param sYm
	 * @param currYear
	 */
	@ResponseBody
	@RequestMapping("/getSaleFcstDetailByCxjh")
	public List<SaleFcstDetail> getSaleFcstDetailByCxjh(String matInfoData,String sYm,String eYm){
		// 获取选择的物料数据
		List<SaleFcstDetail> checkMatInfo = JSON.parseArray(matInfoData, SaleFcstDetail.class);
		sYm = sYm.trim();
		eYm = eYm.trim();
		
		String[] sYmArr = sYm.split("-");
		String sYear = sYmArr[0];
		String sMonth = sYmArr[1];
		
		String[] eYmArr = eYm.split("-");
		String eYear = eYmArr[0];
		String eMonth = eYmArr[1];
		
		// 获取当前年月区间条件
		String currYmBlock = "'"+sYm+"' as q1,";
		for(int i=2;i<=24;i++){
			currYmBlock += "'"+DateUtils.dateMonthCalc(sYm, i-1)+"' as q"+i+",";
		}
		currYmBlock = currYmBlock.substring(0, currYmBlock.length()-1);
		
		// 获取当前年月上一年区间条件
		int sYy = Integer.parseInt(sYear);
		String yy1 = sYy+"-";
		String yy2 = (sYy+1)+"-";
		String yy3 = (sYy+2)+"-";
		String preY1 = (sYy-1)+"-";
		String preY2 = sYy+"-";
		String preY3 = (sYy+1)+"-";
		
		String preYmBlock = "";
		String preSYm="";
		String preEYm="";
		if(sMonth.equals("01")){
			preSYm = sYm.replace(yy1, preY1);
			preEYm = eYm.replace(yy2, preY2);
			preYmBlock = currYmBlock.replace(yy1, preY1).replace(yy2, preY2);
		}else{
			preSYm = sYm.replace(yy1, preY1);
			preEYm = eYm.replace(yy3, preY3);
			preYmBlock = currYmBlock.replace(yy1, preY1).replace(yy2, preY2).replace(yy3, preY3);
		}
		
		// 获取汇总的字段
		String sumC = "";
		
		if(sMonth.equals("01")){
			sumC = "(a.SALE_FORE_QTY1+a.SALE_FORE_QTY2+a.SALE_FORE_QTY3+a.SALE_FORE_QTY4+"+
	"a.SALE_FORE_QTY5+a.SALE_FORE_QTY6+a.SALE_FORE_QTY7+a.SALE_FORE_QTY8+"+
	"a.SALE_FORE_QTY9+a.SALE_FORE_QTY10+a.SALE_FORE_QTY11+a.SALE_FORE_QTY12) as SUM_SALE_FORE1,"+
	" (a.SALE_FORE1+a.SALE_FORE2+a.SALE_FORE3+a.SALE_FORE4+"+
	"a.SALE_FORE5+a.SALE_FORE6+a.SALE_FORE7+a.SALE_FORE8+"+
	"a.SALE_FORE9+a.SALE_FORE10+a.SALE_FORE11+a.SALE_FORE12) as SUM_SALE_FORE2,0 as SUM_SALE_FORE3";
		}else{
			int sM = Integer.parseInt(sMonth);
			String sumC1 = "";
			for(int i=0;i<=12-sM;i++){
				sumC1 += "a.SALE_FORE_QTY"+(i+1)+"+";
			}
			sumC1 = "("+sumC1.substring(0, sumC1.length()-1)+") as SUM_SALE_FORE1,";
			
			String sumC2 = "";
			int eM = Integer.parseInt(eMonth);
			for(int i=12;i>12-eM;i--){
				sumC2 += "a.SALE_FORE_QTY"+i+"+";
			}
			for(int i=1;i<=12-eM;i++){
				sumC2 += "a.SALE_FORE"+i+"+";
			}
			sumC2 = "("+sumC2.substring(0, sumC2.length()-1)+") as SUM_SALE_FORE2,";
			
			String sumC3="";
			for(int i=12;i>12-eM;i--){
				sumC3 += "a.SALE_FORE"+i+"+";
			}
			sumC3 = "("+sumC3.substring(0, sumC3.length()-1)+") as SUM_SALE_FORE3";
			
			sumC = sumC1+sumC2+sumC3;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("list", checkMatInfo);
		paramMap.put("sYm", sYm);
		paramMap.put("eYm", eYm);
		paramMap.put("currYmBlock", currYmBlock);
		paramMap.put("preYmBlock", preYmBlock);
		paramMap.put("preSYm", preSYm);
		paramMap.put("preEYm", preEYm);
		paramMap.put("sumC", sumC);
		
		return saleFcstService.getSaleFcstDataByCxjh(paramMap);
	}
	
	/**
	 * 导入模板下载
	 * @param request
	 * @param response
	 */
	@Log(value = "导入模板下载")
	@ResponseBody
	@RequestMapping("/impTempDownload")
	public void impTempDownload(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		Workbook wb;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath+"templates\\importTemp\\saleFcstImpTemp.xls";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			
			String fileName = "销售预测导入模板";
			// 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
			
			// 导出excel
			OutputStream out = response.getOutputStream();
			ExcelUtil.exportExcel(wb, out);
			
		} catch (Exception e) {
		}
	}
}
