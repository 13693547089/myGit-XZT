package com.faujor.web.fam;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.common.RFC.SAPInvoke;
import com.faujor.common.annotation.Log;
import com.faujor.dao.master.fam.ContactsMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.fam.ContactsAtta;
import com.faujor.entity.fam.ContactsMain;
import com.faujor.entity.fam.ContactsMone;
import com.faujor.entity.fam.ContactsMones;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.common.CodeService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.fam.intercourseService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExportExcelForCostInfo;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.SAPInterfaceUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;


@Controller
@RequestMapping("/intercourse")
public class ReconciliationController
{
	@Value("${file_upload_url}")
	private String url;
	@Value("${file_upload_username}")
	private String username;
	@Value("${file_upload_password}")
	private String password;
	@Autowired
	private intercourseService intercourseService;
	@Autowired
	private QualSuppService QualSuppService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private ContactsMapper ContactsMapper;
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;

	/**
	 * 获取采购对账首页
	 *
	 * @return
	 */
	@Log(value = "查看财务往来页面")
	@RequestMapping("/Reconciliation")
	public String auditIndex()
	{
		return "fam/intercourse/Reconciliation";
	}

	/**
	 * 获取往来对账
	 *
	 * @return
	 */
	@Log(value = "查看财务往来确认页面")
	@RequestMapping("/IntercourseConfirm")
	public String auditIntercourse()
	{
		return "fam/intercourseConfirm/confirmList";
	}

	/**
	 * 跳转到新建页面
	 *
	 * @return getReconciliationDetail
	 */
	@Log(value = "新建财务往来对账")
	@RequestMapping("/getReconciliationDetail")
	public String jumpDetail(Model model, String type)
	{
		ContactsMain contactsMain = new ContactsMain();
		Date date = new Date();
		String format = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		// contactsMain.setRecoNumb(format);
		contactsMain.setCreatTime(date);
		// contactsMain.setStatus("已保存");
		model.addAttribute("contactsMain", contactsMain);
		model.addAttribute("type", type);
		Map<String, Object> map = QualSuppService.queryAllQualSupp();
		List<QualSupp> list = (List<QualSupp>) map.get("data");
		model.addAttribute("SuppList", list);
		return "fam/intercourse/ReconciliationDetail";
	}

	/*
	 * 列表获取数据
	 *
	 */
	@Log(value = "获取财务对账信息")
	@ResponseBody
	@RequestMapping("/queryReconciliationList")
	public Map<String, Object> queryReconciliationList(final String supply, final String Buyer, String dateSta, String dateEnd,
			Material mate, Integer page, Integer limit, String confirm)
	{
		int offset = 0;
		if (page != null)
		{
			offset = (page - 1) * limit;
		}

		final Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(supply))
		{
			map.put("supply", "%" + supply + "%");
		}
		if (!StringUtils.isEmpty(Buyer))
		{
			map.put("Buyer", "%" + Buyer + "%");
		}

		if (!StringUtils.isEmpty(dateSta))
		{
			map.put("dateSta", DateUtils.parse(dateSta, "yyyy-MM-dd"));
		}
		if (!StringUtils.isEmpty(dateEnd))
		{
			map.put("dateEnd", DateUtils.parse(dateEnd, "yyyy-MM-dd"));
		}
		if ("confirm".equals(confirm))
		{

			Map<String, Object> data = intercourseService.queryReconciliationconfirmList(map, offset, limit);
			// page.setCount(data.size());
			// page.setData(data);
			return data;
		}
		else
		{
			Map<String, Object> data = intercourseService.queryReconciliationList(map, offset, limit);
			// page.setCount(data.size());
			// page.setData(data);
			return data;
		}

	}

	/***
	 * type:1 新建，2 查看 ， 3编辑 保存方法
	 */
	/*
	 * @ResponseBody
	 *
	 * @RequestMapping("/savedateList") public boolean savedatelist(ContactsMain conMain, String receMateData, String
	 * receMateData2, String papData, String typ, String Fid, String SuppId) { System.out.println(conMain);
	 * System.out.println(Fid); System.out.println("haha" + papData); if (conMain.getSuppNumb() == null ||
	 * "".equals(conMain.getSuppNumb())) { return false;
	 *
	 * } //保存 if ("recon".equals(typ)) { if (conMain.getRecoNumb() == "" || conMain.getRecoNumb() == null) { String code
	 * = codeService.getCodeByCodeType("reconNo"); conMain.setRecoNumb(code); } if (conMain.getFounder() == "" ||
	 * conMain.getFounder() == null) { SysUserDO user = UserCommon.getUser(); String suppCode = user.getUsername();
	 * conMain.setFounder(suppCode);
	 *
	 * } intercourseService.deleteContactsMain(Fid); intercourseService.deleteContactsMone(Fid);
	 * intercourseService.deleteContactsMones(Fid); List<ContactsMone> list = JsonUtils.jsonToList(receMateData,
	 * ContactsMone.class); List<ContactsMones> list2 = JsonUtils.jsonToList(receMateData2, ContactsMones.class); if
	 * (papData == null) { List<ContactsAtta> list3 = null; boolean b = intercourseService.insertContactsMain(conMain,
	 * list, list2, list3, SuppId); return b; } else { List<ContactsAtta> list3 = JsonUtils.jsonToList(papData,
	 * ContactsAtta.class); boolean b = intercourseService.insertContactsMain(conMain, list, list2, list3, SuppId);
	 * return b; } } else if ("submit".equals(typ))//提交 { if (conMain.getRecoNumb() == "" || conMain.getRecoNumb() ==
	 * null) { String code = codeService.getCodeByCodeType("reconNo"); conMain.setRecoNumb(code); } if
	 * (conMain.getFounder() == "" || conMain.getFounder() == null) { SysUserDO user = UserCommon.getUser(); String
	 * suppCode = user.getUsername(); conMain.setFounder(suppCode);
	 *
	 * } intercourseService.deleteContactsMain(Fid); intercourseService.deleteContactsMone(Fid);
	 * intercourseService.deleteContactsMones(Fid); List<ContactsMone> list = JsonUtils.jsonToList(receMateData,
	 * ContactsMone.class); List<ContactsMones> list2 = JsonUtils.jsonToList(receMateData2, ContactsMones.class); if
	 * (papData == null) { List<ContactsAtta> list3 = null; boolean b = intercourseService.insertContactsMain(conMain,
	 * list, list2, list3, SuppId); return b; } else { List<ContactsAtta> list3 = JsonUtils.jsonToList(papData,
	 * ContactsAtta.class); boolean b = intercourseService.insertContactsMain(conMain, list, list2, list3, SuppId);
	 * return b; } } else//确认 {
	 *
	 * intercourseService.deleteContactsMain(Fid); intercourseService.deleteContactsMone(Fid);
	 * intercourseService.deleteContactsMones(Fid); List<ContactsMone> list = JsonUtils.jsonToList(receMateData,
	 * ContactsMone.class); List<ContactsMones> list2 = JsonUtils.jsonToList(receMateData2, ContactsMones.class); if
	 * (papData == null) { List<ContactsAtta> list3 = null; boolean b = intercourseService.insertContactsMain(conMain,
	 * list, list2, list3, SuppId); return b; } else { List<ContactsAtta> list3 = JsonUtils.jsonToList(papData,
	 * ContactsAtta.class); boolean b = intercourseService.insertContactsMain(conMain, list, list2, list3, SuppId);
	 * return b; } }
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 * }
	 */

	/**
	 * type:1 新建，2 查看 ， 3编辑
	 *
	 * @param conMain
	 * @param receMateData
	 * @param receMateData2
	 * @param papData
	 * @param typ
	 * @param Fid
	 * @param SuppId
	 * @param type
	 * @return
	 */
	@Log(value = "创建/编辑/查看采购对账")
	@ResponseBody
	@RequestMapping("/savedateList")
	public boolean savedatelist(ContactsMain conMain, String receMateData, String receMateData2, String papData, String typ,
			String Fid, String sapId, String type)
	{
		/*
		 * if (conMain.getSuppNumb() == null || "".equals(conMain.getSuppNumb())) { return false;
		 *
		 * }
		 */
		// 保存
		if ("recon".equals(typ))
		{
			List<ContactsMone> list = JsonUtils.jsonToList(receMateData, ContactsMone.class);
			List<ContactsMones> list2 = JsonUtils.jsonToList(receMateData2, ContactsMones.class);
			boolean b = false;
			if ("1".equals(type))
			{// 新建保存
				String code = codeService.getCodeByCodeType("reconNo");
				conMain.setRecoNumb(code);
				SysUserDO user = UserCommon.getUser();
				conMain.setFounder(user.getName());
				// List<ContactsAtta> list3 = JsonUtils.jsonToList(papData,
				// ContactsAtta.class);
				b = intercourseService.insertContactsMain(conMain, list, list2);
			}
			else if ("3".equals(type))
			{// 编辑保存
				b = intercourseService.updateContactsMain(conMain, list, list2);
			}
			return b;
		}
		else if ("submit".equals(typ))// 提交
		{
			List<ContactsMone> list = JsonUtils.jsonToList(receMateData, ContactsMone.class);
			List<ContactsMones> list2 = JsonUtils.jsonToList(receMateData2, ContactsMones.class);
			boolean b = false;
			if ("1".equals(type))
			{// 新建提交
				String code = codeService.getCodeByCodeType("reconNo");
				conMain.setRecoNumb(code);
				SysUserDO user = UserCommon.getUser();
				conMain.setFounder(user.getName());
				// List<ContactsAtta> list3 = JsonUtils.jsonToList(papData,
				// ContactsAtta.class);
				b = intercourseService.insertContactsMain(conMain, list, list2);
			}
			else if ("3".equals(type))
			{// 编辑提交
				b = intercourseService.updateContactsMain(conMain, list, list2);
			}
			return b;
		}
		else// 确认
		{
			List<ContactsAtta> list3 = JsonUtils.jsonToList(papData, ContactsAtta.class);
			boolean b = intercourseService.ConfirmContactsMain(conMain, list3);
			return b;
		}
	}

	/**
	 * 跳转详细
	 *
	 */
	@Log(value = "查看财务往来详细")
	@RequestMapping("/getReconciliationDetailList")
	public String getReconciliationDetailList(String Fid, Model model, String type)
	{

		ContactsMain contactsMain = intercourseService.getReconciliationDetailList(Fid);
		List<ContactsMone> ContactsMone = intercourseService.getReconciliationDetailListMone(Fid);
		List<ContactsMones> ContactsMones = intercourseService.getReconciliationDetailListMones(Fid);
		Map<String, Object> map = QualSuppService.queryAllQualSupp();
		List<QualSupp> list = (List<QualSupp>) map.get("data");
		model.addAttribute("SuppList", list);
		model.addAttribute("contactsMain", contactsMain);
		model.addAttribute("mone", JsonUtils.beanToJson(ContactsMone));
		model.addAttribute("mones", JsonUtils.beanToJson(ContactsMones));
		model.addAttribute("type", type);
		return "fam/intercourse/ReconciliationDetail";
	}

	/**
	 * 确认页面跳转详情
	 *
	 *
	 */
	@Log(value = "查看财务往来确认详细")
	@RequestMapping("/getIntercourseConfirmList")
	public String getIntercourseConfirmList(String Fid, Model model, String type)
	{

		ContactsMain contactsMain = intercourseService.getReconciliationDetailList(Fid);
		List<ContactsMone> ContactsMone = intercourseService.getReconciliationDetailListMone(Fid);
		List<ContactsMones> ContactsMones = intercourseService.getReconciliationDetailListMones(Fid);
		Map<String, Object> map = QualSuppService.queryAllQualSupp();
		List<QualSupp> list = (List<QualSupp>) map.get("data");
		model.addAttribute("SuppList", list);
		model.addAttribute("contactsMain", contactsMain);
		model.addAttribute("type", type);
		model.addAttribute("mone", JsonUtils.beanToJson(ContactsMone));
		model.addAttribute("mones", JsonUtils.beanToJson(ContactsMones));
		return "fam/intercourseConfirm/confirmDetail";
	}

	@RequestMapping("/setConfirmList")
	public void setConfirmList(String Fid)
	{
		intercourseService.setConfirmList(Fid);
	}

	/**
	 *
	 * 批量删除
	 *
	 * @param fids
	 * @return
	 */
	@Log(value = "删除财务往来列表数据")
	@ResponseBody
	@RequestMapping("/deleteReconciliationByFid")
	public boolean deleteReconciliationByFid(String[] fids)
	{
		boolean b = intercourseService.deleteReconciliationByFid(fids);
		return b;
	}

	/**
	 *
	 *
	 * 提交订单
	 *
	 */
	@Log(value = "删除财务往来提交")
	@ResponseBody
	@RequestMapping("/resetReconciliationByFid")
	public boolean resetReconciliationByFid(String[] fids)
	{
		boolean b = intercourseService.resetReconciliationByFid(fids);
		return b;
	}

	/**
	 *
	 * 财务往来确认提交
	 *
	 */
	@Log(value = "删除财务往来确认提交")
	@ResponseBody
	@RequestMapping("/financialConfirmation")
	public boolean financialConfirmation(String[] fids)
	{
		boolean b = intercourseService.financialConfirmation(fids);
		return b;
	}

	/**
	 *
	 * 通过接口新增账款
	 *
	 */
	@Log(value = "通过接口新增账款")
	@ResponseBody
	@RequestMapping("/SapInterAdd")
	public String SuppSapId(String SuppSapId, String ClosDate)
	{
		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("LIFNR", SuppSapId);
		json.put("ZJZDAT", ClosDate);
		ja.add(json);

		SAPInvoke invoke = new SAPInvoke();
		String rs = invoke.invoke(SAPInterfaceUtil.RFC_ACC_DOCUMENT_EXPORT, ja.toJSONString());

		return rs;
	}

	/**
	 *
	 * confirmReturn 往来确认退回
	 *
	 */
	@Log(value = "财务往来确认退回")
	@ResponseBody
	@RequestMapping("/confirmReturn")
	public boolean confirmReturn(String Fid, String Textarea, String Radio)
	{
		boolean a = intercourseService.confirmReturn(Fid, Textarea, Radio);
		return a;

	}

	/**
	 * 以excel表的格式导出财务往来
	 *
	 * @param costIds
	 * @param req
	 * @param res
	 * @return
	 */
	@Log(value = "以excel表的格式导出财务往来")
	@RequestMapping("/exportCostInfo")
	public String exportCostInfo(String Fids, HttpServletRequest req, HttpServletResponse res)
	{
		ServletOutputStream os = null;
		try
		{
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier"))
			{
				username = user.getSuppNo();
			}
			String fileName = username + "-财务往来对账-" + time + ".xls";
			// 根据费用编号的查询费用信息，返回一个List集合
			ContactsMain ContactsMain = ContactsMapper.getReconciliationList(Fids);
			String sheetName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			// 新建文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 设置列头样式
			HSSFCellStyle columnHeadStyle = wb.createCellStyle();
			columnHeadStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
			// 新建工作表
			HSSFSheet sheet = wb.createSheet(sheetName);
			ExportExcelForCostInfo.createExcelOfCostInfo(ContactsMain, sheet, wb);

			ExportExcelForCostInfo.setAttachmentFileName(req, res, fileName);
			os = res.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 文件上传
	 *
	 * @param files
	 * @param direCode
	 * @return
	 */
	@Log(value = "附件上传")
	@ResponseBody
	@RequestMapping("/docPaperOfOrderUpload")
	public RestCode docUpload(@RequestParam(required = false, value = "file") MultipartFile[] files, String direCode)
	{
		OutputStream os = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		Directory dire = directoryService.getDireByCode(direCode);
		List<Document> docs = new ArrayList<Document>();
		if (dire == null)
		{
			return RestCode.error("请选择正确的文件路径编码！");
		}
		if (files != null && files.length > 0)
		{
			try
			{
				NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(url, username, password);
				for (MultipartFile file : files)
				{
					String fileName = file.getOriginalFilename();
					String newName = UUIDUtil.getUUID() + fileName;
					String realPath = url.concat(dire.getDireFname());
					String realName = realPath.concat(newName);
					SmbFile smbPath = new SmbFile(realPath, auth);
					SmbFile smbFile = new SmbFile(realName, auth);
					if (!smbPath.exists())
					{
						smbPath.mkdirs();
					}
					os = smbFile.getOutputStream();
					bos = new BufferedOutputStream(os);
					is = file.getInputStream();
					byte[] temp = new byte[1024];
					while ((is.read(temp)) > 0)
					{
						bos.write(temp, 0, temp.length);
					}
					bos.flush();
					Document doc = new Document();
					doc.setId(UUIDUtil.getUUID());
					doc.setDireCode(direCode);
					doc.setFileUrl(realName);
					doc.setFileName(newName);
					doc.setRealName(fileName);
					SysUserDO user = UserCommon.getUser();
					String userType = user.getUserType();
					String username = user.getUsername();
					if (!StringUtils.isEmpty(userType) && "supplier".equals(userType))
					{
						username = user.getSuppNo();
					}
					doc.setCreateUser(username);
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
			catch (MalformedURLException e)
			{
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			}
			finally
			{
				IoUtil.closeIo(bos, os, is);
			}
		}
		documentService.saveDocs(docs);
		return new RestCode().put("data", docs);
	}

	/***
	 *
	 *
	 * 附件查看
	 *
	 */
	@Log(value = "附件查看")
	@ResponseBody
	@RequestMapping("/queryAttr")
	public List<ContactsAtta> queryAttr(String fid)
	{
		List<ContactsAtta> attr = intercourseService.getqueryAttr(fid);
		return attr;
	};

	@RequestMapping("/getFinaTranAuditHtml")
	public String getFinaTranAuditHtml(Model model, TaskParamsDO taskPDO)
	{
		ContactsMain contactsMain = intercourseService.getReconciliationDetailList(taskPDO.getSdata1());
		List<ContactsMone> ContactsMone = intercourseService.getReconciliationDetailListMone(taskPDO.getSdata1());
		List<ContactsMones> ContactsMones = intercourseService.getReconciliationDetailListMones(taskPDO.getSdata1());
		Map<String, Object> map = QualSuppService.queryAllQualSupp();
		List<QualSupp> list = (List<QualSupp>) map.get("data");
		model.addAttribute("SuppList", list);
		model.addAttribute("contactsMain", contactsMain);
		model.addAttribute("mone", JsonUtils.beanToJson(ContactsMone));
		model.addAttribute("mones", JsonUtils.beanToJson(ContactsMones));
		model.addAttribute("taskPDO", taskPDO);
		model.addAttribute("type", "3");
		return "fam/intercourse/FinaTranAudit";
	}
}
