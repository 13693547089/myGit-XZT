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
import com.faujor.dao.master.bam.ContMapper;
import com.faujor.dao.master.basic.BasicMapper;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.dao.master.document.TempLogMapper;
import com.faujor.entity.bam.Contract;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.service.bam.ContService;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@Service
public class ContServiceImpl implements ContService {

	@Autowired
	private ContMapper contMapper;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private TempLogMapper tempLogMapper;
	@Autowired
	private BasicMapper basicMapper;
	
	@Override
	public LayuiPage<Contract> getContByPage(Map<String, Object> map) {
		LayuiPage<Contract> page=new LayuiPage<Contract>();
		List<Contract> rows = contMapper.getContByPage(map);
		Integer num = contMapper.getContNum(map);
		page.setCount(num);
		page.setData(rows);
		return page;
	}

	@Override
	public Contract getCont(Map<String, Object> map) {
		Contract cont = contMapper.getCont(map);
		return cont;
	}

	@Override
	public void saveCont(Contract cont, List<Document> docs) {
		SysUserDO user = UserCommon.getUser();
		String uuid = UUIDUtil.getUUID();
		cont.setId(uuid);
		cont.setCreateTime(new Date());
		cont.setCreateUser(user.getUsername());
		String contStatus = cont.getContStatus();
		if(StringUtils.isEmpty(contStatus)){
			contStatus="WFB";
		}
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode", contStatus);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		if(dics!=null && dics.size()>0){
			cont.setContStatusName(dics.get(0).getDicName());
		}
		//保存模板信息
		contMapper.saveCont(cont);
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("bam_cont");
			doc.setLinkNo(uuid);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		log.setOperation("创建");
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(uuid);
		tempLogMapper.saveTempLog(log);
	}

	@Override
	public void updateCont(Contract cont, List<Document> docs) {
		SysUserDO user = UserCommon.getUser();
		String contId=cont.getId();
		cont.setModifyTime(new Date());
		cont.setModifyUser(user.getUsername());
		//保存模板信息
		String contStatus = cont.getContStatus();
		if(StringUtils.isEmpty(contStatus)){
			contStatus="WFB";
		}
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode",contStatus);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		if(dics!=null && dics.size()>0){
			cont.setContStatusName(dics.get(0).getDicName());
		}
		contMapper.updateCont(cont);
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("bam_cont");
			doc.setLinkNo(contId);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		log.setOperation("修改");
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(contId);
		tempLogMapper.saveTempLog(log);
	}

	@Override
	public List<TempLog> getTempLogByContNo(String contId) {
		List<TempLog> list = tempLogMapper.getTempLogByTempNo(contId);
		return list;
	}
	@Override
	public RestCode deleteFile(List<String> docIds) {
		Map<String, Object> params=new HashMap<String,Object>();
		for (String docId : docIds) {
			params.put("docId", docId);
			Document doc = documentMapper.getDoc(params);
			if(doc!=null){
				FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
				documentMapper.deleteDoc(docId);
			}
		}
		return new RestCode();
	}

	@Override
	@Transactional
	public void deleteCont(List<String> contIds) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("docCate", "bam_cont");
		for (String contId : contIds) {
			//删除合同模板
			contMapper.deleteContById(contId);
			//删除关联的附件信息
			map.put("linkNo", contId);
			List<Document> docs = documentMapper.getDocByLinkNo(map);
			for (Document doc : docs) {
				if(doc!=null){
					FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
				}
			}
			documentMapper.deleteDocByLinkNo(map);
			//删除日志记录
			tempLogMapper.delLogByTempNo(contId);
		}
	}

	@Override
	@Transactional
	public void changeContStatus(List<String> contIds, String status) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("cateCode", "CONTTEMPSTATUS");
		params.put("dicCode",status);
		List<Dic> dics = basicMapper.findDicByCodeParams(params);
		String statusName="";
		if(dics!=null && dics.size()>0){
			statusName=dics.get(0).getDicName();
		}
		for (String contId : contIds) {
			Contract cont=new Contract();
			cont.setId(contId);
			cont.setContStatus(status);
			cont.setContStatusName(statusName);
			cont.setModifyUser(user.getUsername());
			cont.setModifyTime(new Date());
			contMapper.changeContSatus(cont);
		}
	}

	@Override
	public boolean checkIsExist(String id, String contCode) {
		int count = contMapper.checkIsExist(id, contCode);
		if(count>0){
			return false;
		}
		return true;
	}
}
