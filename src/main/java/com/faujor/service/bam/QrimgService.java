package com.faujor.service.bam;

import com.faujor.entity.bam.Qrimg;

public interface QrimgService {

	
	/**
	 * 增加二维码图片
	 * @param img
	 * @return
	 */
	public boolean  insertQrimg(Qrimg img);
	
	/**
	 * 根据编码查询Qrimg 二维码的地址
	 * @param qrCode
	 * @return
	 */
	public Qrimg queryQrimgByQrCode(String qrCode);
}
