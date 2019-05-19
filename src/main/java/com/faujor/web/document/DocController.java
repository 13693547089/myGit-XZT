package com.faujor.web.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;


@Controller
@RequestMapping("/doc")
public class DocController {
	@Value("${ftp_base_path}")
	private  String ftpBasePath ;
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 文件上传
	 * @param files
	 * @param direCode
	 * @return
	 */
	@Log("文件上传")
	@ResponseBody
	@RequestMapping("/docUpload")
	public RestCode docUpload(@RequestParam(required= false,value="file")MultipartFile[] files,String direCode,String linkNo,String docCate,String linkId){
		InputStream is=null;
		Directory dire = directoryService.getDireByCode(direCode);
		List<Document> docs=new ArrayList<Document>();
		if(dire==null){
			return RestCode.error("请选择正确的文件路径编码！"); 
		}
		String monthStr=DateUtils.format(new Date(), "yyyyMM");
		boolean uploadFlag=true;
		if(files!=null && files.length>0){
			try {
				for (MultipartFile file : files) {
					String fileName = file.getOriginalFilename();
					String newName=UUIDUtil.getUUID()+fileName;
					String realPath=ftpBasePath.concat(dire.getDireFcode()).concat(monthStr+"/");
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
			} catch (Exception e) {
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
	@Log("文件下载")
	@RequestMapping("/downLoadDoc")
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
	 * 删除服务器上附件  并删除附件表中的数据
	 * @param fileId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFile")
	public RestCode delFile(String  docIds){
		List<String> list = JsonUtils.jsonToList(docIds, String.class);
		RestCode restCode = documentService.deleteFile(list);
		return restCode;
	}
	/**
	 * 获取文件上传的测试页面
	 * @return
	 */
	@RequestMapping("/getTestUploadPage")
	public String getTestUploadPage(){
		return "document/docList";
	}
	/**
	 * 获取文件管理页面
	 * @return
	 */
	@Log("获取文档管理列表信息")
	@RequestMapping("/getDocListPage")
	public String getDocListPage(){
		return "document/docList";
	}
	/**
	 * 获取目录的树形结构数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDireTree")
	public List<LayuiTree<Directory>> getDireTree(){
		List<LayuiTree<Directory>> direTree = directoryService.getDireTree();
		return direTree;
	}
	/**
	 * 分页获取文档
	 * @param page
	 * @param docName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDocByPage")
	public LayuiPage<Document> getDocByPage(LayuiPage<Document> page,
			String docName,String direId,String createUser,Date startDate,Date endDate,String linkNo){
		if(direId==null){
			direId="";
		}
		Directory dire = directoryService.getDireById(direId);
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		if(dire!=null){
			params.put("direCode", dire.getDireCode());
		}else{
			params.put("direCode", "");
		}
		if(!StringUtils.isEmpty(docName)){
			params.put("docName", "%"+docName+"%");
		}
		if(!StringUtils.isEmpty(linkNo)){
			params.put("linkNo", "%"+linkNo+"%");
		}
		if(startDate!=null){
			params.put("startDate", startDate);
		}
		if(endDate!=null){
			params.put("endDate", endDate);
		}
		if(!StringUtils.isEmpty(createUser)){
			params.put("createUser", "%"+createUser+"%");
		}
		LayuiPage<Document> docPage = documentService.getDocByPage(params);
		System.out.println(JsonUtils.beanToJson(docPage));
		return docPage;
	}
	
	@ResponseBody
	@RequestMapping("/getDocsByLinkNo")
	public List<Document> getDocsByLinkNo(String linkNo,String docCate,String excludeDocId){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("linkNo", linkNo);
		map.put("excludeDocId", excludeDocId);
		map.put("docCate", docCate);
		List<Document> docs = documentService.getDocByLinkNo(map);
		return docs;
	}
}
