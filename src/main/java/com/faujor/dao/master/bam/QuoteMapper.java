package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.Quote;
import com.faujor.entity.bam.QuoteAttr;
import com.faujor.entity.bam.QuoteMate;

public interface QuoteMapper {
	/**
	 * 保存报价单
	 * @param quote
	 */
	void saveQuote(Quote quote);
	/**
	 * 删除报价单
	 * @param quoteCode
	 */
	void delQuoteByCode(String quoteCode);
	/**
	 * 更新报价单
	 * @param quote
	 */
	void updateQuote(Quote quote);
	/**
	 * 更新报价单状态
	 * @param map
	 */
	void updateQuoteStatus(Map<String, Object> map);
	/**
	 * 根据报价单编码获取报价单信息
	 * @param quoteCode
	 * @return
	 */
	Quote getQuoteByCode(String quoteCode);
	
	/**
	 * 分页获取报价单
	 * @param map
	 * @return
	 */
	List<Quote> getQuoteByPage(Map<String, Object> map);
	/**
	 * 获取报价单数量
	 * @param map
	 * @return
	 */
	Integer getQuoteNum(Map<String, Object> map);
	/**
	 * 更新报价单的有效期
	 * @param map
	 * @return
	 */
	int updateValidDate(QuoteMate mate);
	/**
	 * 根据报价单主键查询报价单信息
	 * @param id
	 * @return
	 */
	Quote getQuoteById(String id);
	
	//-----------------------附件开始-----------------------------
	/**
	 * 添加附件信息
	 * @param attr
	 * @return
	 */
	int insertAttr(QuoteAttr attr);
	/**
	 * 删除附件信息
	 * @param ids
	 * @return
	 */
	int delAttr(@Param("ids")List<String> ids);
	/**
	 * 根据报价单编码删除报价单附件列表
	 * @param quoteCode
	 * @return
	 */
	int delAttrByQuoteCode(String  quoteCode);
	/**
	 * 根据报价单编码获取报价单附件列表
	 * @param quoteCode
	 * @return
	 */
	List<QuoteAttr>  getQuoteAttrByQuoteCode(String quoteCode);
	
	//------------------------附件结束----------------------------

}
