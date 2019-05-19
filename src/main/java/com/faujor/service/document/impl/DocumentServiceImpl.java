package com.faujor.service.document.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.common.ftp.FtpUtil;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.document.Document;
import com.faujor.service.document.DocumentService;
import com.faujor.utils.RestCode;
@Service
public class DocumentServiceImpl implements DocumentService {
	
	@Autowired
	private DocumentMapper documentMapper;
	@Override
	public LayuiPage<Document> getDocByPage(Map<String, Object> map) {
		LayuiPage<Document> page= new LayuiPage<Document>();
		Integer count = documentMapper.getDocNum(map);
		List<Document> data = documentMapper.getDocByPage(map);
		page.setData(data);
		page.setCount(count);
		return page;
	}
	@Override
	public Document getDoc(Map<String, Object> map) {
		Document doc = documentMapper.getDoc(map);
		return doc;
	}
	@Override
	@Transactional
	public void saveDocs(List<Document> docs) {
		for (Document document : docs) {
			documentMapper.saveDoc(document);
		}
	}
	@Override
	public List<Document> getDocByLinkNo(Map<String, Object> map) {
		List<Document> docs = documentMapper.getDocByLinkNo(map);
		return docs;
	}
	@Override
	public void deleteDocs(List<String> docIds) {
		for (String docId : docIds) {
			documentMapper.deleteDoc(docId);
		}
	}
	@Transactional
	@Override
	public RestCode deleteFile(List<String> docIds) {
		Map<String, Object> params=new HashMap<String,Object>();
		boolean flag =true;
		for (String docId : docIds) {
			params.put("docId", docId);
			Document doc = documentMapper.getDoc(params);
			boolean deleteFile = FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
			if(deleteFile){
				documentMapper.deleteDoc(docId);
			}else{
				flag=false;
			}
		}
		if(!flag){
			return RestCode.error("删除失败！");
		}
		return new RestCode();
	}
	
}
