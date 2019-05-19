package com.faujor.service.bam.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.dao.master.bam.ContTempMapper;
import com.faujor.dao.master.basic.BasicMapper;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.dao.master.document.TempLogMapper;
import com.faujor.entity.bam.ContTemp;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.service.bam.ContTempService;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@Service
public class ContTempServiceImpl implements ContTempService {
	

	@Autowired
	private ContTempMapper contTempMapper;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private TempLogMapper tempLogMapper;
	@Autowired
	private BasicMapper basicMapper;
	
	@Override
	public LayuiPage<ContTemp> getTempByPage(Map<String, Object> map) {
		LayuiPage<ContTemp> page=new LayuiPage<ContTemp>();
		List<ContTemp> rows = contTempMapper.getTempByPage(map);
		Integer num = contTempMapper.getTempNum(map);
		page.setCount(num);
		page.setData(rows);
		return page;
	}

	@Override
	public ContTemp getTemp(Map<String, Object> map) {
		ContTemp temp = contTempMapper.getTemp(map);
		return temp;
	}

	@Override
	public void saveTemp(ContTemp temp, List<Document> docs) {
		SysUserDO user = UserCommon.getUser();
		String uuid = UUIDUtil.getUUID();
		String tempNo=temp.getTempNo();
		temp.setId(uuid);
		temp.setCreateTime(new Date());
		temp.setCreateUser(user.getUsername());
		temp.setCreater(user.getName());
		String tempStatus = temp.getTempStatus();
		if(StringUtils.isEmpty(tempStatus)){
			tempStatus="WFB";
		}
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode", tempStatus);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		if(dics!=null && dics.size()>0){
			temp.setStatusName(dics.get(0).getDicName());
		}
		params.put("cateCode", "CONTTYPE");
		params.put("dicCode", temp.getContType());
		List<Dic> dics2 = basicMapper.findDicByCodeParams(params);
		if(dics2!=null && dics2.size()>0){
			temp.setContTypeName(dics2.get(0).getDicName());
		}
		//保存模板信息
		contTempMapper.saveTemp(temp);
		//与版本信息做关联
		String versionBasis = temp.getVersionBasis();
		if(versionBasis!=null){
			Document doc =new Document();
			doc.setDocCate("bam_cont_temp");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("bam_cont_temp");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		if("WFB".equals(tempStatus)){
			log.setOperation("创建");
		}else{
			log.setOperation("创建并发布");
		}
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(tempNo);
		tempLogMapper.saveTempLog(log);
	}

	@Override
	public void updateTemp(ContTemp temp, List<Document> docs) {
		SysUserDO user = UserCommon.getUser();
		String tempNo=temp.getTempNo();
		temp.setModifyTime(new Date());
		temp.setModifyUser(user.getUsername());
		temp.setModifier(user.getName());
		//保存模板信息
		String tempStatus = temp.getTempStatus();
		if(StringUtils.isEmpty(tempStatus)){
			tempStatus="WFB";
		}
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode",tempStatus);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		if(dics!=null && dics.size()>0){
			temp.setStatusName(dics.get(0).getDicName());
		}
		params.put("cateCode", "CONTTYPE");
		params.put("dicCode", temp.getContType());
		List<Dic> dics2 = basicMapper.findDicByCodeParams(params);
		if(dics2!=null && dics2.size()>0){
			temp.setContTypeName(dics2.get(0).getDicName());
		}
		contTempMapper.updateTemp(temp);
		//与版本信息做关联
		String versionBasis = temp.getVersionBasis();
		if(versionBasis!=null){
			Document doc =new Document();
			doc.setDocCate("bam_cont_temp");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("bam_cont_temp");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		log.setOperation("修改");
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(tempNo);
		tempLogMapper.saveTempLog(log);
	}

	@Override
	public List<TempLog> getTempLogByTempNo(String tempNo) {
		List<TempLog> list = tempLogMapper.getTempLogByTempNo(tempNo);
		return list;
	}

	@Override
	public RestCode deleteFile(List<String> docIds) {
		Map<String, Object> params=new HashMap<String,Object>();
		for (String docId : docIds) {
			params.put("docId", docId);
			Document doc = documentMapper.getDoc(params);
			if(doc!=null){
				String fileUrl = doc.getFileUrl();
				String fileName = doc.getFileName();
				FtpUtil.deleteFile(fileUrl, fileName);
			}
		}
		for (String docId : docIds) {
			documentMapper.deleteDoc(docId);
		}
		return new RestCode();
	}

	@Override
	@Transactional
	public void deleteContTemp(List<String> tempNos) {
		Map<String, Object> map=new HashMap<String,Object>();

		map.put("docCate", "bam_cont_temp");
		for (String tempNo : tempNos) {
			//删除合同模板
			contTempMapper.deleteTempByNo(tempNo);
			//删除关联的附件信息
			map.put("linkNo", tempNo);
			List<Document> docs = documentMapper.getDocByLinkNo(map);
			for (Document doc : docs) {
				FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
			}
			documentMapper.deleteDocByLinkNo(map);
			//删除日志记录
			tempLogMapper.delLogByTempNo(tempNo);
		}
	}

	@Override
	@Transactional
	public void changeTempStatus(List<String> tempNos, String status) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode",status);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		String statusName="";
		if(dics!=null && dics.size()>0){
			statusName=dics.get(0).getDicName();
		}
		for (String tempNo : tempNos) {
			ContTemp contTemp=new ContTemp();
			contTemp.setTempNo(tempNo);
			contTemp.setTempStatus(status);
			contTemp.setStatusName(statusName);
			contTempMapper.changeTempSatus(contTemp);
			TempLog log=new TempLog();
			log.setId(UUIDUtil.getUUID());
			if(statusName.equals("已发布")){
				log.setOperation("发布");
			}else if(statusName.equals("未发布")){
				log.setOperation("撤回");
			}
			log.setOperator(user.getUsername());
			log.setOperateTime(new Date());
			log.setTempNo(tempNo);
			tempLogMapper.saveTempLog(log);
		}
	}
}
