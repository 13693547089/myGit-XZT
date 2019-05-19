package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.bam.Quote;
import com.faujor.entity.bam.QuoteAttr;
import com.faujor.entity.bam.QuoteMate;
import com.faujor.entity.bam.QuotePrice;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.mdm.QuoteStruDetails;
import com.faujor.utils.RestCode;

public interface QuoteService {
	/**
	 * 保存报价单数据
	 * @param quote
	 * @param quoteMates
	 */
	void saveQuote(Quote quote,List<QuoteMate> quoteMates,List<QuoteAttr> quoteAttrs);
	/**
	 * 更新报价单数据
	 * @param quote
	 * @param quoteMates
	 */
	void updateQuote(Quote quote,List<QuoteMate> quoteMates,List<QuoteAttr> quoteAttrs);
	/**
	 * 删除报价单数据
	 * @param quoteCode
	 */
	void delQuote(List<String> quoteCodes);
	/**
	 * 分页获取报价单数据
	 * @param map
	 * @return
	 */
	LayuiPage<Quote> getQuoteByPage(Map<String, Object> map);
	/**
	 * 根据报价单号获取报价单物料信息
	 * @param quoteCode
	 * @return
	 */
	List<QuoteMate> getQuoteMatesByQuoteCode(String quoteCode);
	/**
	 * 更新报价单状态
	 * @param map
	 */
	void updateQuoteStatus(String status,List<String> quoteCodes);
	/**
	 * 根据报价单号获取报价单信息
	 * @param quoteCode
	 * @return
	 */
	Quote getQuoteByQuoteCode(String quoteCode);
	
	/**
	 * 根据物料Id获取物料报价信息
	 * @param map
	 * @return
	 */
	List<QuotePrice> getQuotePriceByMateId(Map<String, Object> map);
	/**
	 * 将报价单提交oa审批
	 * @param quoteCodes
	 * @return
	 */
	RestCode submitQuoteToOA(List<String> quoteCodes);
	/**
	 * oa审批完成调用接口
	 * @param data
	 * @return
	 */
	JSONObject oaAuditQuote(String data);
	/**
	 * 更新报价单的有效期
	 * @param map
	 * @return
	 */
	int updateValidDate(List<QuoteMate> mates);
	/**
	 * 更新附件列表
	 * @param attrs
	 * @param quoteCode
	 * @return
	 */
	int updateQuoteFile(List<QuoteAttr> attrs,String quoteCode);

	
	/**
	 * 校验物料的有效期
	 * @param quoteCodes
	 * @return
	 */
	RestCode validDate(List<String> quoteCodes);
	/**
	 * 根据报价单主键查询报价单信息
	 * @param id
	 * @return
	 */
	Quote getQuoteById(String id);
	
	/**
	 * 根据报价单号获取报价单附件列表
	 * @param quoteCode
	 * @return
	 */
	List<QuoteAttr>  getQuoteAttrByQuoteCode(String quoteCode);
	
	
	/**
	 * 删除附件
	 * @param docIds
	 * @return
	 */
	 RestCode deleteFile(List<String> docIds);
}
