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

import com.faujor.common.ftp.FtpUtil;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.common.CodeService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.document.TemplateService;
import com.faujor.service.mdm.LatentSuppService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;


@Controller
@RequestMapping("/suppReg")
public class LatentSuppRegController
{

	@Value("${ftp_base_path}")
	private String ftpBasePath;
	@Autowired
	private LatentSuppService latentSuppService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TemplateService temService;

	/**
	 * 登录界面跳转到潜在供应商登记页面
	 *
	 * @return
	 */
	@RequestMapping("/getLatentSuppLogRegHtml")
	public String getLatentSuppLogRegHtml(Model model)
	{
		//正式系统
//		long l =(long) 36;
		//测试系统
		long l =(long) 118;
		List<Document> list = temService.getDocByMenuId(l);
		String fileId1="down01";
		String fileId2="down02";
		for (Document d : list) {
			String fileName = d.getFileName().substring(32);
			int i = fileName.indexOf(".");
			String str = fileName.substring(0, i);
			if("营业执照扫描件示例".equals(str)){
				fileId1 = (d.getId() == null || "".equals(d.getId()))? "down01":d.getId();
			}else if("供应商资料评估表".equals(str)){
				fileId2 = (d.getId() == null || "".equals(d.getId()))? "down02":d.getId();
			}
		}
		model.addAttribute("fileId1", fileId1);
		model.addAttribute("fileId2", fileId2);
		return "mdm/supp/potenSuppRegistration";
	}

	/**
	 * 文件上传
	 *
	 * @param files
	 * @param direCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/docUploadPaperAndSaveSupp")
	public RestCode docUpload(@RequestParam(required = false, value = "file") MultipartFile[] files, String direCode,
			String linkNo, String docCate, String linkId, LatentSupp latentSupp, LatentPapers latentPapers)
	{
		String srmId = codeService.getCodeByCodeType("suppNo");
		InputStream is = null;
		Directory dire = directoryService.getDireByCode(direCode);
		List<Document> docs = new ArrayList<Document>();
		if (dire == null)
		{
			return RestCode.error("请选择正确的文件路径编码！");
		}
		String monthStr = DateUtils.format(new Date(), "yyyyMM");
		boolean uploadFlag = true;
		if (files != null && files.length > 0)
		{
			try
			{
				for (MultipartFile file : files)
				{
					String fileName = file.getOriginalFilename();
					String newName = UUIDUtil.getUUID() + fileName;
					String realPath = ftpBasePath.concat(dire.getDireFcode().concat(monthStr+"/"));
					is = file.getInputStream();
					uploadFlag = FtpUtil.uploadFile(realPath, newName, file.getInputStream());
					if (uploadFlag)
					{
						Document doc = new Document();
						doc.setId(UUIDUtil.getUUID());
						doc.setDireCode(direCode);
						doc.setFileUrl(realPath);
						doc.setFileName(newName);
						doc.setRealName(fileName);
						doc.setLinkNo(linkNo);
						doc.setLinkId(linkId);
						doc.setDocCate(docCate);
						doc.setCreateUser(srmId);
						doc.setCreateTime(new Date());
						doc.setDocSize(file.getSize() / 1024 + "Kb");
						String[] split = fileName.split(".");
						if (split.length > 0)
						{
							doc.setDocType(split[split.length - 1]);
						}
						docs.add(doc);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			}
			finally
			{
				IoUtil.closeIo(is);
			}
		}
		if (!uploadFlag)
		{
			return RestCode.error("上传失败！");
		}
		documentService.saveDocs(docs);
		// latentSupp.setSuppId(suppId);
		latentSupp.setSrmId(srmId);
		latentSupp.setStatus("未审核");
		boolean b = latentSuppService.insertLatentSupp(latentSupp);
		LatentPapers lp1 = new LatentPapers();
		LatentPapers lp2 = new LatentPapers();
		lp1.setPapersId("YYZZ");
		lp1.setPapersName("营业执照附件");
		lp1.setPapersType("证照");
		lp1.setStartDate(latentPapers.getStartDate());
		lp1.setEndDate(latentPapers.getEndDate());
		if (docs.get(0) != null)
		{
			lp1.setAcceUrl(docs.get(0).getFileUrl());
			lp1.setAcceOldName(docs.get(0).getRealName());
			lp1.setAcceNewName(docs.get(0).getFileName());
			lp1.setFileId(docs.get(0).getId());
		}
		lp1.setSuppId(latentSupp.getSuppId());
		boolean c = latentSuppService.insertLatentPapers(lp1);
		lp2.setPapersId("GYSZLPGB");
		lp2.setPapersName("供应商资料评估表");
		lp2.setPapersType("证照");
		if (docs.get(1) != null)
		{
			lp2.setAcceUrl(docs.get(1).getFileUrl());
			lp2.setAcceOldName(docs.get(1).getRealName());
			lp2.setAcceNewName(docs.get(1).getFileName());
			lp2.setFileId(docs.get(1).getId());
		}
		lp2.setSuppId(latentSupp.getSuppId());
		boolean d = latentSuppService.insertLatentPapers(lp2);
		if (b && c && d)
		{
			// 生成任务
			/*Date date = new Date();
			String format = DateUtils.format(date);*/
			TaskParamsDO params = new TaskParamsDO();
			params.setNode(1);
			params.setProcessCode("supplierAudit");
			params.setSdata1(latentSupp.getSuppId());
			params.setTaskName("潜在供应商审核: "+latentSupp.getSuppName());
			params.setSubmit(latentSupp.getSrmId());
			params.setSubmitName(latentSupp.getSuppName());
			int i = taskService.createTasksForReg(params);
			return new RestCode().put("data", docs);
		}
		else
		{
			return null;
		}

	}

	/***
	 * 文件下载
	 *
	 * @param docId
	 *           文件ID
	 * @return
	 */
	@RequestMapping("/downLoadDoc")
	public String downLoadDoc(String docId, String docName, HttpServletResponse response)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("docId", docId);
		map.put("docName", docName);
		Document doc = documentService.getDoc(map);
		String filePath = doc.getFileUrl();
		String realName = doc.getFileName();
		OutputStream os = null;
		if (doc != null)
		{
			try
			{
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				String filename = doc.getRealName();
				filename = URLEncoder.encode(filename, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				os = response.getOutputStream();
				FtpUtil.downloadFtpFile(filePath, realName, os);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				IoUtil.closeIo(os);
			}
		}
		return null;
	}

	/**
	 * 删除文件
	 *
	 * @param docId
	 * @return
	 */
	@RequestMapping("/deleteFile")
	@ResponseBody
	public RestCode deleteFile(String jsonStr)
	{
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = templateService.deleteFile(docIds);
		return restCode;
	}

}
