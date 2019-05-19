package com.faujor.web.mdm;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.faujor.common.annotation.Log;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.document.TemplateService;
import com.faujor.service.mdm.LatentPapersService;
import com.faujor.service.mdm.LatentSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
public class LatentSuppController {

	@Value("${ftp_base_path}")
	private  String ftpBasePath ;
	@Autowired
	private LatentSuppService latentSuppService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private LatentPapersService LatentPapersService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private TemplateService temService;
	@Autowired
	private TaskService taskService;
	/**
	 * 潜在供应商展示列表跳转到潜在供应商登记页面
	 * @return
	 */
	@RequestMapping("/getLatentSuppRegHtml")
	public String getLatentSuppRegHtml(Model model){
		//供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		//付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		//惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		//惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		//附件类型
		List<Dic> acceList = basicService.findDicListByCategoryCode("ACCETYPE");
		//证件名称
		List<Dic> taxenameList = basicService.findDicListByCategoryCode("ACCENAME");
		model.addAttribute("suppCateList",suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList",currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("acceList", acceList);
		model.addAttribute("taxenameList", taxenameList);
		return "mdm/supp/latentSuppReg";
	}
	
	
	/**
	 * 文件上传
	 * @param files
	 * @param direCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/docPaperUpload")
	public RestCode docUpload(@RequestParam(required= false,value="file")MultipartFile[] files,String direCode,String linkNo,String docCate,String linkId){
		InputStream is=null;
		Directory dire = directoryService.getDireByCode(direCode);
		List<Document> docs=new ArrayList<Document>();
		if(dire==null){
			return RestCode.error("请选择正确的文件路径编码！"); 
		}
		String monthStr = DateUtils.format(new Date(), "yyyyMM");
		boolean uploadFlag=true;
		if(files!=null && files.length>0){
			try {
				for (MultipartFile file : files) {
					String fileName = file.getOriginalFilename();
					String newName=UUIDUtil.getUUID()+fileName;
					String realPath=ftpBasePath.concat(dire.getDireFcode().concat(monthStr)+"/");
					is=file.getInputStream();
					uploadFlag = FtpUtil.uploadFile(realPath, newName, file.getInputStream());
					if(uploadFlag){
						Document doc=new Document();
						doc.setId(UUIDUtil.getUUID());
						doc.setDireCode(direCode);
						doc.setFileUrl(realPath);
						doc.setFileName(newName);
						doc.setRealName(fileName);
						doc.setLinkNo(linkNo);
						doc.setLinkId(linkId);
						doc.setDocCate(docCate);
						doc.setCreateUser(UserCommon.getUser().getUsername());
						doc.setCreateTime(new Date());
						doc.setDocSize(file.getSize()/1024+"Kb");
						String[] split = fileName.split(".");
						if(split.length>0){
							doc.setDocType(split[split.length-1]);
						}
						docs.add(doc);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			}finally{
				IoUtil.closeIo(is);
			}
		}
		if(uploadFlag){
			documentService.saveDocs(docs);
			return new RestCode().put("data", docs);
		}else{
			return RestCode.error("上传失败！"); 
		}
	}
	
	/***
	 * 文件下载
	 * @param docId 文件ID
	 * @return
	 */
	@RequestMapping("/downLoadPaperDoc")
	public String downLoadDoc(String docId,String docName,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("docId", docId);
		map.put("docName", docName);
		Document doc = documentService.getDoc(map);
		String filePath=doc.getFileUrl();
		String realName=doc.getFileName();
		OutputStream os=null;
		if(doc!=null){
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				String filename=doc.getRealName();
				filename=URLEncoder.encode(filename, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				os = response.getOutputStream();
				FtpUtil.downloadFtpFile(filePath, realName, os);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				IoUtil.closeIo(os);
			}
		}
		return null;
	}
	
	/**
	 * 删除文件
	 * @param docId
	 * @return
	 */
	@RequestMapping("/deletePaperFile")
	@ResponseBody
	public RestCode deleteFile(String jsonStr){
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = templateService.deleteFile(docIds);
		return restCode;
	}
	
	/**
	 * 潜在供应商登记
	 * @param latentSupp
	 * @return
	 */
	@Log(value ="保存/批准潜在供应商")
	@ResponseBody
	@RequestMapping("/addLatentSupp")
	public boolean addLatentSupp(LatentSupp latentSupp,String paperData,String type){
		SysUserDO user = UserCommon.getUser();
		int userId = user.getUserId().intValue();
		Date date = new Date();
		String srmId = codeService.getCodeByCodeType("suppNo");
		latentSupp.setSrmId(srmId);
		if("1".equals(type)){
			latentSupp.setStatus("未审核");
		}else if("2".equals(type)){
			//latentSupp.setStatus("已初审");
			latentSupp.setStatus("未审核");
			//latentSupp.setFirstTrialId(userId);
			//latentSupp.setBuyerAuditTime(date);
		}
		boolean b = latentSuppService.insertLatentSupp(latentSupp);
		List<LatentPapers> list = JsonUtils.jsonToList(paperData, LatentPapers.class);
		for(LatentPapers lp:list){
			lp.setSuppId(latentSupp.getSuppId());
			latentSuppService.insertLatentPapers(lp);
		}
		return b;
	}
	
	/**
	 * 查询附件类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryPaperType")
	public List<Dic> queryPapersType(){
		//附件类型
		List<Dic> acceList = basicService.findDicListByCategoryCode("ACCETYPE");
		return acceList;
	}
	/**
	 * 查询证件名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryPaperName")
	public List<Dic> queryPapersName(){
		//证件名称
		List<Dic> taxenameList = basicService.findDicListByCategoryCode("ACCENAME");
		return taxenameList;
	}
	
	/**
	 * 潜在供应商列表分页展示
	 * @param latentSupp
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Log(value = "获取潜在供应商列表")
	@ResponseBody
	@RequestMapping("/queryLatentSuppByPage")
	public Map<String,Object> queryLatentSuppByPage(LatentSupp latentSupp,Integer page,Integer limit){
		if(page == null){page=1;}
		if(limit == null){limit=10;}
		int start=(page-1)*limit+1;
		int end = page*limit;
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("latentSupp", latentSupp);
		Map<String, Object> page2 = latentSuppService.queryLatentSuppByPage(map);
		return page2;
	}
	
	/**
	 * 跳转到潜在供应商列表展示页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getLatentSuppRegListHtml")
	public String getLatentSuppRegListHtml(Model model){
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		model.addAttribute("statusList", statusList);
		return "mdm/supp/latentSuppRegList";
	}
	/**
	 * 跳转到潜在供应商列表展示页面(部长审核页面)
	 * @param model
	 * @return
	 */
	@RequestMapping("/getLatentSuppMiniListHtml")
	public String getLatentSuppMiniListHtml(Model model){
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		model.addAttribute("statusList", statusList);
		return "mdm/supp/latentSuppMiniList";
	}
	/**
	 * 删除不合格的潜在供应商
	 * @param suppIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteLatentSuppBySuppId")
	public boolean deleteLatentSuppBySuppId(String[] suppIds){
		boolean b = latentSuppService.deleteLatentSuppBySuppId(suppIds);
		return b;
	}
	
	/**
	 * 跳转到潜在供应商编辑
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateLatentSupp")
	public String updateLatentSupp(Model model,String suppId,String type,String editType){
		//供应商的信息
		LatentSupp supp = latentSuppService.queryOneLatentSuppBySuppId(suppId);
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		//供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		//付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		//惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		//惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		//附件类型
		List<Dic> acceList = basicService.findDicListByCategoryCode("ACCETYPE");
		//证件名称
		List<Dic> taxenameList = basicService.findDicListByCategoryCode("ACCENAME");
		model.addAttribute("suppCateList",suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList",currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("supp", supp);
		model.addAttribute("statusList", statusList);
		model.addAttribute("type", type);
		model.addAttribute("acceList", acceList);
		model.addAttribute("taxenameList", taxenameList);
		model.addAttribute("editType",editType);
		return "mdm/supp/latentSuppEdit";
	}
	
	@ResponseBody
	@RequestMapping("/queryLatentPapersOfLatentSupp")
	public List<LatentPapers> queryLatentPapersOfLatentSupp(String suppId){
		//供应商证件信息
		List<LatentPapers> paperList = LatentPapersService.queryManyLatentPapersBySuppId(suppId);
		return paperList;
	}
	
	/**
	 * 采购员编辑潜在供应商信息
	 * @param latentSupp
	 * @param paperData
	 * @return
	 */
	@Log(value ="编辑潜在供应商")
	@ResponseBody
	@RequestMapping("/updateLatentSuppBySuppId")
	public boolean updateLatentSuppBySuppId(LatentSupp latentSupp,String paperData,String type){
		if("3".equals(type)){
			latentSupp.setStatus("已初审");
		}
		List<LatentPapers> list = JsonUtils.jsonToList(paperData, LatentPapers.class);
		boolean b = latentSuppService.updateLatentSupp(latentSupp, list);
		return b;
	}
	
	/**
	 * 跳转至采购员审核页面
	 * @param model
	 * @param suppId
	 * @return
	 */
	@RequestMapping("/getLatentSuppAuditHtml")
	public String getLatentSuppAuditHtml(Model model,String suppId){
		//任务信息
		TaskDO task = taskService.getTask(suppId);
		model.addAttribute("task", task);
		//供应商的信息
		LatentSupp supp = latentSuppService.queryOneLatentSuppBySuppId(suppId);
		//供应商证件信息
		List<LatentPapers> paperList = LatentPapersService.queryManyLatentPapersBySuppId(suppId);
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		//供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		//付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		//惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		//惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		model.addAttribute("suppCateList",suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList",currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("supp", supp);
		model.addAttribute("paperList", paperList);
		model.addAttribute("paperList2", JsonUtils.beanToJson(paperList));
		model.addAttribute("statusList", statusList);
		return "mdm/supp/latentSuppAudit";
	}
	/**
	 * 采购员审核潜在供应商
	 * @param latentSupp
	 * @param paperData
	 * @return
	 */
	@Log(value ="批准潜在供应商")
	@ResponseBody
	@RequestMapping("/approveLatentSupp")
	public boolean approveLatentSupp(LatentSupp latentSupp){
		SysUserDO user = UserCommon.getUser();
		int userId = user.getUserId().intValue();
		Date date = new Date();
		//latentSupp.setStatus("已初审");
		latentSupp.setStatus("未审核");
		//latentSupp.setFirstTrialId(userId);
		//latentSupp.setBuyerAuditTime(date);
		boolean b = latentSuppService.approveLatentSupp(latentSupp);
		return b;
	}
	
	/**
	 * 采购部长提交OA页面
	 * @return
	 */
	@RequestMapping("/getLatentSuppSubOAHtml")
	public String getLatentSuppSubOAHtml(Model model,String suppId){
		//任务信息
		TaskDO task = taskService.getTask(suppId);
		model.addAttribute("task", task);
		//供应商的信息
		LatentSupp supp = latentSuppService.queryOneLatentSuppBySuppId(suppId);
		//供应商证件信息
		List<LatentPapers> paperList = LatentPapersService.queryManyLatentPapersBySuppId(suppId);
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		//供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		//付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		//惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		//惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		model.addAttribute("suppCateList",suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList",currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("supp", supp);
		model.addAttribute("paperList", paperList);
		model.addAttribute("paperList2", JsonUtils.beanToJson(paperList));
		model.addAttribute("statusList", statusList);
		return "mdm/supp/latentSuppSubOA";
	}
	
	/**潜在供应商
	 * 采购部长审核列表
	 * @return
	 */
	@Log(value ="获取潜在供应商审核列表")
	@ResponseBody
	@RequestMapping("/queryLatentSuppToMini")
	public Map<String,Object> queryLatentSuppToMini(LatentSupp latentSupp,Integer page,Integer limit){
		SysUserDO user = UserCommon.getUser();
		List<Integer> userIds = new ArrayList<Integer>();
		//获取部长下的所有采购员
		List<UserDO> list = orgService.manageOrgByCode(user.getUserId().intValue(), "PURCHAROR",null);
		if(list.size() != 0){
			for(UserDO u:list){
				userIds.add((int)u.getId());
			}
		}else{
			userIds.add(9999999);
		}
		if(page == null){page=1;}
		if(limit == null){limit=10;}
		int start=(page-1)*limit+1;
		int end = page*limit;
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("latentSupp", latentSupp);
		map.put("userIds", userIds);
		Map<String, Object> page2 = latentSuppService.queryLatentSuppToMini(map);
		return page2;
	}
	/**
	 * 部长将潜在供应商提交OA
	 * @param suppId
	 * @return
	 */
	@Log(value ="将潜在供应商提交OA")
	@ResponseBody
	@RequestMapping("/subLatentSuppToOA")
	public boolean subLatentSuppToOA(String suppId){
		SysUserDO user = UserCommon.getUser();
		LatentSupp latentSupp = new LatentSupp();
		latentSupp.setSuppId(suppId);
		latentSupp.setStatus("已提交OA");
		latentSupp.setMiniId(user.getUserId().intValue());
		return latentSuppService.subLatentSuppToOA(latentSupp);
	}
	/**
	 * 部长退回潜在供应商
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendBackLatentSupp")
	public boolean sendBackLatentSupp(String suppId){
		SysUserDO user = UserCommon.getUser();
		LatentSupp latentSupp = new LatentSupp();
		latentSupp.setSuppId(suppId);
		latentSupp.setStatus("不合格");
		latentSupp.setMiniId(user.getUserId().intValue());
		return latentSuppService.subLatentSuppToOA(latentSupp);
	}
	
	/**
	 * 采购员退回潜在供应商
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/buyerSendBackLatentSupp")
	public boolean buyerSendBackLatentSupp(String suppId){
		SysUserDO user = UserCommon.getUser();
		LatentSupp latentSupp = new LatentSupp();
		latentSupp.setSuppId(suppId);
		latentSupp.setStatus("不合格");
		latentSupp.setFirstTrialId(user.getUserId().intValue());
		return latentSuppService.buyerSendBackLatentSupp(latentSupp);
	}
	
	/**
	 * 下载附件弹出框
	 * @return
	 */
	@RequestMapping("/getLatentSuppPaperListHtml")
	public String getLatentSuppPaperListHtml(){
		return "mdm/supp/latentSuppPaperList";
	}
	
	/**
	 * 弹出框数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryPapers")
	public List<Document>  queryPapers(){
		long l =(long) 119;
		List<Document> list = temService.getDocByMenuId(l);
		return list;
	}
	
	/**
	 * 潜在供应商任务流审核页面
	 * @return
	 */
	@RequestMapping("/getLatentSuppTaskAuditHtml")
	public String getLatentSuppTaskAuditHtml(Model model,TaskParamsDO taskPDO){
		//供应商的信息
		LatentSupp supp = latentSuppService.queryOneLatentSuppBySuppId(taskPDO.getSdata1());
		//供应商证件信息
		List<LatentPapers> paperList = LatentPapersService.queryManyLatentPapersBySuppId(taskPDO.getSdata1());
		//供应商状态
		List<Dic> statusList = basicService.findDicListByCategoryCode("SUPPSTATUS");
		//供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		//付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		//惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		//惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		model.addAttribute("suppCateList",suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList",currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("supp", supp);
		model.addAttribute("paperList", paperList);
		model.addAttribute("paperList2", JsonUtils.beanToJson(paperList));
		model.addAttribute("statusList", statusList);
		model.addAttribute("taskPDO", taskPDO);
		return "mdm/supp/latentSuppTaskAudit";
	}
	
	/**
	 * 在任务流中，采购员初审潜在供应商
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/buyerAuditLatentSupp")
	public boolean buyerAuditLatentSupp(String suppId){
		SysUserDO user = UserCommon.getUser();
		LatentSupp latentSupp = new LatentSupp();
		latentSupp.setSuppId(suppId);
		latentSupp.setStatus("已初审");
		latentSupp.setFirstTrialId(user.getUserId().intValue());
		return latentSuppService.buyerSendBackLatentSupp(latentSupp);
	}
	
}
