package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.QuotePrice;

public interface QuotePriceMapper {
	/**
	 * 保存物料报价信息
	 * @param quotePrice
	 */
	void saveQuotePrice(QuotePrice quotePrice);
	/**
	 * 根据物料的Id获取该物料的报价
	 * @param map
	 * @return
	 */
	List<QuotePrice> getPricesByMateId(Map<String, Object> map);
	/**
	 * 根据物料ID删除物料的报价信息 
	 * @param mateId
	 */
	void delQuotePriceByMateId(String mateId);
	/**
	 * 根据报价单编号删除所有的报价信息
	 * @param quoteCode
	 */
	void delQuoteMateByQuoteCode(String quoteCode);
	/**
	 * 获取段小计
	 * @param quoteMateId
	 * @return
	 */
	List<QuotePrice> getSubTotalByQuoteMateId(String quoteMateId);
	
	/**
	 * 根据段位编码获取编码详情
	 * @param segmCode
	 * @param string 
	 * @return
	 */
	List<QuotePrice> getPricesBySegmCode(@Param("quoteMateId")String quoteMateId, @Param("segmCode")String segmCode);

}







