package com.faujor.dao.master.bam;

import com.faujor.entity.bam.Qrimg;

public interface QrimgMapper {

	/**
	 * 增加二维码图片
	 * @param img
	 * @return
	 */
	public int  insertQrimg(Qrimg img);
	
	/**
	 * 根据编码查询Qrimg 二维码的地址
	 * @param qrCode
	 * @return
	 */
	public Qrimg queryQrimgByQrCode(String qrCode);
}
