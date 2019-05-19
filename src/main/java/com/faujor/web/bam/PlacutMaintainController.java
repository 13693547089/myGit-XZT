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

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.CutProductDO;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.ProdMateDO;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class PlacutMaintainController
{
	@Autowired
	private CutProductService cutProductService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private BasicService basicService;
	/**
	 * 
	 * 获取打切维护列表
	 * 
	 */
	@RequestMapping("/getMaintainListHtml")
	public String getMaintainListHtml(final Model model)
	{
	//CUTPRODTYPE2
		List<Dic> cutProdTypeList = basicService.findDicListByCategoryCode("CUTPRODTYPE2");
		model.addAttribute("cutProdTypeList", cutProdTypeList);
		List<CutStructure> cutStruList = cutProductService.queryCutStruForCutProd();
		model.addAttribute("cutStruList", cutStruList);
		return "bam/placut/Maintain/MaintainList";
	}
	
	@Log(value="获取打切品维护列表")
	@ResponseBody
	@RequestMapping("/queryCutProductByPage")
	public Map<String,Object> queryCutProductByPage(Integer limit,Integer page,ProdMateDO mate){
		if(limit==null){limit=10;}
		if(page == null){page=1;}
		int start =(page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("mate", mate);
		map.put("start", start);
		map.put("end", end);
		Map<String, Object> cutProductPage = cutProductService.queryCutProductByPage(map);
		return cutProductPage;
	}
	/**
	 * 移除打切品
	 * @param mateIds
	 * @return
	 */
	@Log(value ="移除打切品")
	@ResponseBody
	@RequestMapping("/deleteCutProductByprodId")
	public boolean deleteCutProductByprodId(String[] prodIds){
		boolean b= cutProductService.deleteCutProductByprodId(prodIds);
		return b;
	}
	/**
	 * 弹出框页面
	 * @return
	 */
	@RequestMapping("/getAllMaterialsHtml")
	public String getAllMaterialsHtml(Model model){
		List<Dic> cutProdTypeList = basicService.findDicListByCategoryCode("CUTPRODTYPE");
		model.addAttribute("cutProdTypeList", cutProdTypeList);
		return "bam/placut/Maintain/allMateList";
	}
	
	/**
	 * 查询所有打切品
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllCutProduct")
	public List<ProdMateDO> queryAllCutProduct(){
		List<ProdMateDO> list = cutProductService.queryAllCutProduct();
		return list;
	}
	/**
	 * 添加打切品
	 * @param mateIds
	 * @return
	 */
	@Log(value = "添加打切品")
	@ResponseBody
	@RequestMapping("/addCutProduct")
	public boolean addCutProduct(String[] mateIds){
		boolean b = cutProductService.addCutProduct(mateIds);
		return b;
	}
	
	/**
	 *
	 * 获取打切结构页面
	 *
	 */
	@RequestMapping("/getMaintainDetailHtml")
	public String getMaintainDetailHtml(Model model)
	{
		return "bam/placut/Maintain/MaintainDetail";
	}
	/**
	 * 查询所有打切结构
	 * @return
	 */
	@Log(value ="获取打切结构信息")
	@ResponseBody
	@RequestMapping("/queryAllCutStru")
	public List<CutStructure> queryAllCutStru(){
		return cutProductService.queryAllCutStru();
	}
	/**
	 * 新建打切结构
	 * @param cutStruData
	 * @return
	 */
	@Log(value ="保存打切结构")
	@ResponseBody
	@RequestMapping("/addCutStru")
	public boolean addCutStru(String cutStruData){
		List<CutStructure> list = JsonUtils.jsonToList(cutStruData, CutStructure.class);
		return cutProductService.addCutStru(list);
	}
	
	/**
	 * 查询货源清单中所有物料
	 * @param mate
	 * @param page
	 * @param limit
	 * @return
	 */
	@Log(value ="获取弹窗中的物料信息列表")
	@ResponseBody
	@RequestMapping("/queryMateOfSuppMateConfig")
	public Map<String,Object> queryMateOfSuppMateConfig(String cutProdType,Material mate,Integer page,Integer limit){
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
		map.put("mate", mate);
		map.put("cutProdType", cutProdType);
		Map<String, Object> page2 = materialService.queryMateOfSuppMateConfig(map);
		return page2;
	}
	/**
	 * 弹窗，添加物料的版本
	 * @param mateId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAddProdMateVerHtml")
	public String getAddProdMateVerHtml(String prodId,Model model,String type){
		ProdMateDO prod = cutProductService.queryOneProdMateDOByProdId(prodId);
		List<CutStructure> cutStruList = cutProductService.queryCutStruForCutProd();
		if("add".equals(type)){
			prod.setVersion("");
			prod.setCutAim("");
		}
		model.addAttribute("type", type);
		model.addAttribute("prod", prod);
		model.addAttribute("cutStruList", cutStruList);
		return "bam/placut/Maintain/addProdMateVer";
	}
	/**
	 * 添加不同版本的物料打切品
	 * @param mateIds
	 * @return
	 */
	@Log(value ="添加不同版本的打切品")
	@ResponseBody
	@RequestMapping("/addCutProduct2")
	public Map<String,Object> addCutProduct2(ProdMateDO prod){
		return cutProductService.addCutProduct2(prod);
	}
	/**
	 * 修改打切品品物料版本
	 * @param prodId
	 * @param version
	 * @return
	 */
	@Log(value ="编辑打切品")
	@ResponseBody
	@RequestMapping("/updateCutProductVer")
	public Map<String,Object>  updateCutProductVer(ProdMateDO prod){
		return cutProductService.updateCutProductVer(prod);
	}
	
	@ResponseBody
	@RequestMapping("/exportCutProduct")
	public String exportCutProduct(String objjson, HttpServletRequest req, HttpServletResponse res){
		ServletOutputStream os=null;
		try{
			//获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			ProdMateDO pm = JsonUtils.jsonToPojo(objjson,ProdMateDO.class);
			//文件名从称
			SysUserDO user = UserCommon.getUser();
			String fileName = user.getName()+"-打切维护品信息-"+time+".xls";
			//根据费用编号的查询费用信息，返回一个List集合
			List<CutProductDO> cpdoList = cutProductService.queryCutProduct(pm);
			String sheetName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			//新建文件
			HSSFWorkbook wb =new HSSFWorkbook();
			//设置列头样式
			HSSFCellStyle columnHeadStyle  = wb.createCellStyle();
			columnHeadStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
			//新建工作表
			HSSFSheet sheet = wb.createSheet(sheetName);
			ExportExcel.createExcelOfCutProduct(cpdoList,sheet);
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
	 * 处理选中的物料数据过滤，并添加到打切品维护列表中
	 * @param dataJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dealMates")
	public Map<String,Object> dealMates(String dataJson){
		List<MateDO> list = JsonUtils.jsonToList(dataJson, MateDO.class);
		return cutProductService.dealMates(list);
	}
	
}
