package com.faujor.web.bam;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.OrderEnclosure;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.OrderService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.document.TemplateService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.IoUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

@Controller
public class OrderController {
	@Value("${file_upload_url}")
	private String url;
	@Value("${file_upload_username}")
	private String username;
	@Value("${file_upload_password}")
	private String password;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private AsyncLogService asyncLogService;

	/**
	 * 跳转到预约申请列表
	 *
	 * @param model
	 * @return
	 */
	@Log(value = "获取供应商采购页面")
	@RequestMapping("/getReleaseListHtml")
	public String getReleaseListHtml(final Model model) {
		return "bam/order/Release/ReleaseList";
	}

	@Log(value = "获取采购员采购页面")
	@RequestMapping("/getPurchaseListHtml")
	public String getPurchaseListHtml(final Model model) {
		return "bam/order/Purchase/PurchaseList";
	}

	/**
	 * 分页展示
	 */
	@Log(value = "获取从采购订单信息")
	@ResponseBody
	@RequestMapping("/queryReleaseList")
	public Map<String, Object> queryReleaseList(Integer page, Integer limit, String supply, String Buyer,
			String ordeNumb, String dateSta, String dateEnd, String Supp, String ordeType, String deliState,
			String purchOrg, String publishName) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(supply)) {
			map.put("supply", "%" + supply + "%");
		}
		if (!StringUtils.isEmpty(Buyer)) {
			map.put("Buyer", "%" + Buyer + "%");
		}
		if (!StringUtils.isEmpty(ordeNumb)) {
			map.put("ordeNumb", "%" + ordeNumb + "%");
		}
		if (!StringUtils.isEmpty(ordeType)) {
			map.put("ordeType", ordeType);
		}
		if (!StringUtils.isEmpty(deliState)) {
			map.put("deliState", deliState);
		}
		if (!StringUtils.isEmpty(dateSta)) {
			map.put("dateSta", DateUtils.parse(dateSta, "yyyy-MM-dd"));
		}
		if (!StringUtils.isEmpty(dateEnd)) {
			map.put("dateEnd", DateUtils.parse(dateEnd, "yyyy-MM-dd"));
		}
		if (!StringUtils.isEmpty(purchOrg))
			map.put("purchOrg", "%" + purchOrg + "%");
		if (!StringUtils.isEmpty(publishName))
			map.put("publishName", "%" + publishName + "%");
		map.put("start", start);
		map.put("end", end);
		map.put("orderType", "NB");
		map.put("purchOrgCode", "2000");
		if ("Supp".equals(Supp)) {
			Map<String, Object> page2 = orderService.queryReleaseListByPageSupp(map);
			return page2;
		} else {
			Map<String, Object> page2 = orderService.queryReleaseListByPage(map);
			return page2;
		}

	}

	@Log(value = "获取采购订单详情信息")
	@RequestMapping("/getReleaseDetail")
	public String getReleaseDetail(final String fid, final Model model, String type) {
		final Integer count = 0;
		// final OrderRele order = orderService.queryOrderReleByFid(fid);
		OrderRele order = orderService.findOrderReleByOrderNo(fid);
		model.addAttribute("order", order);
		model.addAttribute("count", count);
		System.out.println(type);
		if ("Purchase".equals(type)) {
			Double limitThan = order.getLimitThan();
			if(limitThan == null) {
				order.setLimitThan(100.00D);
			}
			return "bam/order/Purchase/PurchaseDetail";
		} else {
			return "bam/order/Release/ReleaseDetail";
		}

	}

	/**
	 * 订单物料信息
	 *
	 * @param fid
	 * @return
	 */
	@Log(value = "查询订单行项目信息")
	@ResponseBody
	@RequestMapping("/getMatesDataOfOrder")
	public List<OrderMate> getMatesDataOfOrder(final String fid) {
		// 获取物料数据
		List<OrderMate> mates = orderService.findOrderMateByOrderNo(fid);
		// final OrderRele order = orderService.queryOrderReleByFid(fid);
		// final List<OrderMate> mates = order.getMates();
		return mates;
	}

	/**
	 * 订单附件信息
	 *
	 * @param fid
	 * @return
	 */
	@Log(value = "查询订单附件信息")
	@ResponseBody
	@RequestMapping("/getPaperDataOfOrder")
	public List<OrderEnclosure> getPaperDataOfOrder(final String fid) {
		//
		// final OrderRele order = orderService.queryOrderReleByFid(fid);
		// List<OrderEnclosure> list = order.getEnclosure();
		List<OrderEnclosure> list = orderService.findOrderEnclosureByOrderNo(fid);

		return list;
	}

	/**
	 * 修改订单状态
	 *
	 * @param mone
	 * @return
	 */
	@Log(value = "修改订单状态以及添加备注,修改下单限比")
	@ResponseBody
	@RequestMapping("/updateLatentOrderBySuppId")
	public void updateLatentSuppBySuppId(OrderRele orderRele, String paperJson, String type) {
		List<OrderEnclosure> list = JsonUtils.jsonToList(paperJson, OrderEnclosure.class);
		orderService.updateLatentSuppBySuppId(list, type, orderRele);

	}

	/**
	 * 删除附件
	 *
	 * @param mone
	 * @return
	 */
	@Log(value = "删除附件")
	@ResponseBody
	@RequestMapping("/setDeleteEncl")
	public void setDeleteEncl(final String docId) {

		orderService.setDeleteEncl(docId);

	}

	private Integer Integer(final String mone) {
		// TODO Auto-generated method stub
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
	public RestCode docUpload(@RequestParam(required = false, value = "file") MultipartFile[] files, String direCode) {
		OutputStream os = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		Directory dire = directoryService.getDireByCode(direCode);
		List<Document> docs = new ArrayList<Document>();
		if (dire == null) {
			return RestCode.error("请选择正确的文件路径编码！");
		}
		if (files != null && files.length > 0) {
			try {
				NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(url, username, password);
				for (MultipartFile file : files) {
					String fileName = file.getOriginalFilename();
					String newName = UUIDUtil.getUUID() + fileName;
					String realPath = url.concat(dire.getDireFname());
					String realName = realPath.concat(newName);
					SmbFile smbPath = new SmbFile(realPath, auth);
					SmbFile smbFile = new SmbFile(realName, auth);
					if (!smbPath.exists()) {
						smbPath.mkdirs();
					}
					os = smbFile.getOutputStream();
					bos = new BufferedOutputStream(os);
					is = file.getInputStream();
					byte[] temp = new byte[1024];
					while ((is.read(temp)) > 0) {
						bos.write(temp, 0, temp.length);
					}
					bos.flush();
					Document doc = new Document();
					doc.setId(UUIDUtil.getUUID());
					doc.setDireCode(direCode);
					doc.setFileUrl(realName);
					doc.setFileName(newName);
					doc.setRealName(fileName);
					doc.setCreateUser(UserCommon.getUser().getUsername());
					doc.setCreateTime(new Date());
					doc.setDocSize(file.getSize() / 1024 + "Kb");
					String[] split = fileName.split(".");
					if (split.length > 0) {
						doc.setDocType(split[split.length - 1]);
					}
					docs.add(doc);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				return RestCode.error(e.getMessage());
			} finally {
				IoUtil.closeIo(bos, os, is);
			}
		}
		documentService.saveDocs(docs);
		return new RestCode().put("data", docs);
	}

	/***
	 * 文件下载
	 *
	 * @param docId
	 *            文件ID
	 * @return
	 */
	@Log(value = "附件下载")
	@RequestMapping("/downLoadPaperOfOrderDoc")
	public String downLoadDoc(String docId, String docName, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("docId", docId);
		map.put("docName", docName);
		Document doc = documentService.getDoc(map);
		String fileUrl = doc.getFileUrl();
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		SmbFileInputStream sfis = null;
		OutputStream os = null;
		if (doc != null) {
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				String filename = doc.getRealName();
				filename = URLEncoder.encode(filename, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(url, username, password);
				SmbFile smbFile = new SmbFile(fileUrl, auth);
				if (smbFile.isFile()) {
					smbFile.connect();
					sfis = new SmbFileInputStream(smbFile);
					bis = new BufferedInputStream(sfis);
					os = response.getOutputStream();
					int length;
					while ((length = bis.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}
					os.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IoUtil.closeIo(os, bis, sfis);
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
	@Log(value = "删除附件")
	@RequestMapping("/deletePaperFileOfOrder")
	@ResponseBody
	public RestCode deleteFile(String jsonStr) {
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = templateService.deleteFile(docIds);
		return restCode;
	}

	/**
	 * 打开同步条件
	 *
	 * @return
	 */
	@RequestMapping("/openCondDialog")
	public String openCondDialog() {
		return "bam/order/Purchase/asyncCondition";
	}

	/**
	 *
	 * 查询托普数据 selectTopData
	 */
	@Log(value = "采购订单同步")
	@ResponseBody
	@RequestMapping("/selectTopData")
	public int selectTopData(String orderNo, String asyncDate) {
		// 插入同步记录
		AsyncLog al = new AsyncLog();
		String alId = UUIDUtil.getUUID();
		al.setId(alId);
		al.setAsyncName("采购订单主数据同步");
		asyncLogService.saveAsyncLog(al);
		int i = orderService.asyncPurchaseOrder(orderNo, asyncDate, al);
		return i;
	}

	/**
	 *
	 * 订单回退给供应商
	 *
	 */
	@Log(value = "订单回退给供应商")
	@ResponseBody
	@RequestMapping("/setPaperDataFallback")
	public boolean setPaperDataFallback(String fid) {
		orderService.setPaperDataFallback(fid);

		return true;
	}

	/**
	 *
	 * 订单确认
	 *
	 */
	@Log(value = "订单确认")
	@ResponseBody
	@RequestMapping("/setPaperDataConfirm")
	public boolean setPaperDataConfirm(String fid,String limitThan) {
		orderService.setPaperDataConfirm(fid,limitThan);
		return true;
	}

	/**
	 * 跳转到采购订单审核页面
	 *
	 * @param model
	 * @param taskPDO
	 * @return
	 */
	@Log(value = "跳转到采购订单审核页面")
	@RequestMapping("/getOrderAuditHtml")
	public String getOrderAuditHtml(Model model, TaskParamsDO taskPDO) {
		SysUserDO user = UserCommon.getUser();

		OrderRele order = orderService.findOrderReleByID(taskPDO.getSdata1());
		model.addAttribute("taskPDO", taskPDO);
		model.addAttribute("order", order);
		model.addAttribute("userType", user.getUserType());
		return "bam/order/Purchase/OrderAudit";
	}

	/**
	 * 根据采购订单主键修改采购订单的状态
	 *
	 * @param status
	 * @param fid
	 * @return
	 */
	@Log(value = "修改采购订单状态")
	@ResponseBody
	@RequestMapping("/updateOrderStatusByfid")
	public boolean updateOrderStatusByfid(String status, String fid) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		if ("已发布".equals(status)) {
			// 记录发布人的编号和名称
			map.put("userId", user.getUserId());
			map.put("name", user.getName());
		}
		map.put("status", status);
		map.put("fid", fid);
		boolean b = orderService.updateOrderStatusByfid(map);
		return b;
	}
	/**
	 * 只修改采购订单的下单限比
	 * @param orderRele
	 * @return
	 */
	@Log(value = "只修改采购订单的下单限比")
	@ResponseBody
	@RequestMapping("/updateLimitThanOfOrderByFid")
	public boolean updateLimitThanOfOrderByFid( OrderRele orderRele) {
		return orderService.updateLimitThanOfOrderByFid(orderRele);
	}

	/**
	 * 导出采购订单
	 * 
	 * @return
	 */
	// @Log(value = "采购订单导出")
	@ResponseBody
	@RequestMapping("/exportOrderInfo")
	public String exportOrderInfo(String objjson,String zzoem, HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			// 文件名从称
			String fileName = "采购订单-" + time + ".xlsx";
			// 获取表头，例如：无锡CDC送货预约表
			OrderReleDO order = JsonUtils.jsonToPojo(objjson, OrderReleDO.class);
			if(!StringUtils.isEmpty(zzoem)) {
				order.setZzoem(zzoem);
				SysUserDO user = UserCommon.getUser();
				order.setSuppName(user.getSuppNo());
			}
			Date startDate = order.getStartDate();
			Date endDate = order.getEndDate();
			String format1 = DateUtils.format(startDate, "yyyy/MM/dd");
			String format2 = DateUtils.format(endDate, "yyyy/MM/dd");
			Date parse1 = DateUtils.parse(format1, "yyyy/MM/dd");
			Date parse2 = DateUtils.parse(format2, "yyyy/MM/dd");
			order.setStartDate(parse1);
			order.setEndDate(parse2);
			List<OrderRele> list = orderService.exportOrderInfo(order);
			Workbook wb = null;
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\采购订单导出模版.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			if (list != null) {
				int listSize = list.size();
				// 设置标题
				ExcelUtil.setValue(sheetForAuth, 0, 0, "采购订单信息", null);
				ExcelUtil.getCellStyle(sheetForAuth, 1, 1);
				CellStyle cellStyle = ExcelUtil.getCellStyle(sheetForAuth, 1, 1);
				;
				for (int i = 0; i < listSize; i++) {
					int rowId = i + 2;
					OrderRele orderRele = list.get(i);
					String orderNo = orderRele.getContOrdeNumb();
					orderNo = StringUtils.isEmpty(orderNo) ? "" : orderNo;
					ExcelUtil.setValue(sheetForAuth, rowId, 0, orderNo, cellStyle);
					String status = orderRele.getStatus();
					status = StringUtils.isEmpty(status) ? "" : status;
					ExcelUtil.setValue(sheetForAuth, rowId, 1, status, cellStyle);
					Date subeDate = orderRele.getSubeDate();
					if (subeDate != null) {
						String sube = DateUtils.format(subeDate, "yyyy-MM-dd");
						ExcelUtil.setValue(sheetForAuth, rowId, 2, sube, cellStyle);
					}
					String suppNumb = orderRele.getSuppNumb();
					suppNumb = StringUtils.isEmpty(suppNumb) ? "" : suppNumb;
					ExcelUtil.setValue(sheetForAuth, rowId, 3, suppNumb, cellStyle);
					String suppName = orderRele.getSuppName();
					suppName = StringUtils.isEmpty(suppName) ? "" : suppName;
					ExcelUtil.setValue(sheetForAuth, rowId, 4, suppName, cellStyle);
					String ortype = orderRele.getOrtype();
					ortype = StringUtils.isEmpty(ortype) ? "" : ortype;
					ExcelUtil.setValue(sheetForAuth, rowId, 5, ortype, cellStyle);
					String releNumb = orderRele.getReleNumb();
					if (!StringUtils.isEmpty(releNumb)) {
						String[] split = releNumb.split("/");
						ExcelUtil.setValue(sheetForAuth, rowId, 6, split[0], cellStyle);
						ExcelUtil.setValue(sheetForAuth, rowId, 7, split[1], cellStyle);
					} else {
						ExcelUtil.setValue(sheetForAuth, rowId, 6, 0, cellStyle);
						ExcelUtil.setValue(sheetForAuth, rowId, 7, 0, cellStyle);
					}
					String deliType = orderRele.getDeliType();
					deliType = StringUtils.isEmpty(deliType) ? "" : deliType;
					ExcelUtil.setValue(sheetForAuth, rowId, 8, deliType, cellStyle);
					String purchOrg = orderRele.getPurchOrg();
					purchOrg = StringUtils.isEmpty(purchOrg) ? "" : purchOrg;
					ExcelUtil.setValue(sheetForAuth, rowId, 9, purchOrg, cellStyle);
					String publishName = orderRele.getPublishName();
					publishName = StringUtils.isEmpty(publishName) ? "" : publishName;
					ExcelUtil.setValue(sheetForAuth, rowId, 10, publishName, cellStyle);
					String remarks = orderRele.getRemarks();
					remarks = StringUtils.isEmpty(remarks) ? "" : remarks;
					ExcelUtil.setValue(sheetForAuth, rowId, 11, remarks, cellStyle);
				}
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream;charset=UTF-8");

			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
