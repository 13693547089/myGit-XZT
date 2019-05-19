package com.faujor.service.mdm.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.mdm.LatentPapersMapper;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.service.mdm.LatentPapersService;
@Service(value = "LatentPapersService")
public class LatentPapersServiceImpl implements LatentPapersService {

	@Autowired
	private LatentPapersMapper latentPapersMapper;
	
	@Override
	public List<LatentPapers> queryManyLatentPapersBySuppId(String suppId) {
		List<LatentPapers> list = latentPapersMapper.queryManyLatentPapersBySuppId(suppId);	
		return list;
	}

}
