package com.faujor.service.mdm.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.mdm.QualPapersMapper;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualProc;
import com.faujor.service.mdm.QualPapersService;

@Service(value = "QualPapersService")
public class QualPapersServiceImpl implements QualPapersService {

	@Autowired
	private QualPapersMapper qualPapersMapper;
	
	@Override
	public List<QualPapers> queryQualPapersBySuppId(String suppId) {
		List<QualPapers> list = qualPapersMapper.queryQualPapersBySuppId(suppId);
		return list;
	}

	@Override
	public List<QualProc> queryQualProcBySuppId(String suppId) {
		return qualPapersMapper.queryQualProcBySuppId(suppId);
	}
	
	@Override
	public List<QualProc> queryQualProcBySapId(String sapId) {
		return qualPapersMapper.queryQualProcBySapId(sapId);
	}

	@Override
	public List<QualProc> queryQualProcBySapId2(String sapId) {
		
		return qualPapersMapper.queryQualProcBySapId2(sapId);
	}

}
