package com.faujor.dao.master.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.QuoteTemp;
import com.faujor.entity.mdm.QuoteTempDetails;

public interface QuoteTempMapper {
	// 模版数据操作
	int saveTemp(QuoteTemp temp);

	int updateTemp(QuoteTemp temp);

	int removeTemp(QuoteTemp temp);

	List<QuoteTemp> tempList(Map<String, Object> params);

	int countTempList(Map<String, Object> params);

	QuoteTemp findTempById(String id);

	// 模板详情数据操作
	int batchSaveTempDetails(List<QuoteTempDetails> detailsList);

	int removeTempDetailsByTempId(String tempId);

	int removeTempDetails(QuoteTempDetails tempDetails);

	List<QuoteTempDetails> tempDetailsList();

	@Select("select t.id from mdm_quote_temp_details t where t.temp_id = #{tempId}")
	List<String> tempDetailsIDsByTempId(String tempId);

	List<QuoteTempDetails> tempDetailsListByTempId(String tempId);

	@Delete("delete from mdm_quote_temp_details where id = #{id} or parent_id = #{id}")
	int removeTempDetailsById(String id);

	int saveTempDetails(QuoteTempDetails details);

	QuoteTempDetails findTempDetailsById(String detailsId);

	int batchRemoveTempDetailsByTempIds(List<String> list);

	int batchRemoveTemp(List<String> list);

	@Select("select count(*) from mdm_quote_temp where id <> #{id} and temp_code = #{code}")
	int checkCode(Map<String, String> params);

	int updateTempDetails(QuoteTempDetails qtd);

	int batchRemoveTempDetailsByIDs(List<String> tdIDs);

	List<QuoteTemp> findTempListForSelected();

	List<QuoteTemp> tempListMap(RowBounds rb, Map<String, Object> params);

}
