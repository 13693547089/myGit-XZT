package com.faujor.service.bam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.QrimgMapper;
import com.faujor.entity.bam.Qrimg;
import com.faujor.service.bam.QrimgService;

@Service(value = "qrimgService")
public class QrimgServiceImpl implements QrimgService {

	@Autowired
	private QrimgMapper qrimgMapper;
	
	@Override
	public boolean insertQrimg(Qrimg img) {
		int i = qrimgMapper.insertQrimg(img);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Qrimg queryQrimgByQrCode(String qrCode) {
		Qrimg qrimg = qrimgMapper.queryQrimgByQrCode(qrCode);
		return qrimg;
	}

}
