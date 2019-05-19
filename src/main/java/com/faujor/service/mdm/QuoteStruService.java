package com.faujor.service.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.QuoteStruDO;
import com.faujor.entity.mdm.QuoteStruDetails;
import com.faujor.entity.mdm.QuoteTempDetails;

public interface QuoteStruService {

	/**
	 * 报价结构列表
	 * 
	 * @param rb
	 * @param qsd
	 * @return
	 */
	Map<String, Object> quoteStruList(RowBounds rb, QuoteStruDO qsd);

	List<QuoteStruDetails> findStruDetailsByStruId(String struId);

	int saveStruData(QuoteStruDO quoteStru, List<QuoteStruDetails> list);

	QuoteStruDO findStruById(String id);

	int updateStruData(QuoteStruDO quoteStru, List<QuoteStruDetails> list);

	Map<String, Object> mateStruList(String type);

	int saveBatchModify(QuoteStruDO qsd, String mateList);

	List<QuoteStruDetails> copyTempDetailsToStruDetails(List<QuoteTempDetails> list, String struId);

	int batchRemoveStruData(List<QuoteStruDO> list);

	int saveBatchCreate(QuoteStruDO qsd, String struList);
}
