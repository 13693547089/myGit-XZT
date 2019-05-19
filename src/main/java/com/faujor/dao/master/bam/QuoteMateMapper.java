package com.faujor.dao.master.bam;

import java.util.List;

import com.faujor.entity.bam.QuoteMate;

public interface QuoteMateMapper {
	/**
	 * 保存报价单物料信息
	 * @param mate
	 */
	void saveQuoteMate(QuoteMate mate);
	/**
	 * 根据物料单中的报价结构
	 * @param mateId
	 * @return
	 */
	QuoteMate getQuoteMateById(String mateId);
	/**
	 * 根据报价单下的物料ID删除物料
	 * @param mateId
	 */
	void delQuoteMateById(String mateId);
	/**
	 * 删除一个报价单下的所有列表
	 * @param quoteCode
	 */
	void delQuoteMateByQuoteCode(String quoteCode);
	/**
	 * 获取所有的报价物料根据报价单号
	 * @return
	 */
	List<QuoteMate> getAllQuoteMateByQuoteCode(String quoteCode);
}
