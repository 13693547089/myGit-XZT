package com.faujor.dao.master.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.QuoteStruDO;
import com.faujor.entity.mdm.QuoteStruDetails;

public interface QuoteStruMapper {

	/**
	 * 报价结构列表
	 * 
	 * @param rb
	 * @param pages
	 * @return
	 */
	List<QuoteStruDO> findQuoteStruListByPage(RowBounds rb, Map<String, Object> pages);

	int countQuoteStruListByPage(Map<String, Object> pages);

	List<QuoteStruDO> findQuoteStruDoList(QuoteStruDO qsd);

	QuoteStruDO findQuoteStruDoById(String id);

	List<QuoteStruDetails> findQuoteStruDetailsByStruId(String struId);

	int saveStruData(QuoteStruDO quoteStru);

	int updateStruData(QuoteStruDO quoteStru);

	int batchSaveStruDetails(Map<String, Object> params);

	@Select("select id from mdm_quote_stru_details where stru_id = #{id}")
	List<String> findQuoteStruDetailsIdsByStruId(String id);

	int batchRemoveDetails(List<String> oldIds);

	int batchUpdateStruDetails(List<QuoteStruDetails> updateList);

	int updateStruDetails(QuoteStruDetails q);

	List<QuoteStruDO> findMateStruList(Long userId);

	int batchRemoveDetailsByStruIds(List<QuoteStruDO> list);

	int batchRemoveStruData(List<QuoteStruDO> list);

	List<QuoteStruDetails> getDetailsByMateId(Map<String, Object> map);

}
