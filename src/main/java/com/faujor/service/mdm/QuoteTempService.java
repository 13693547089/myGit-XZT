package com.faujor.service.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.QuoteTemp;
import com.faujor.entity.mdm.QuoteTempDetails;

public interface QuoteTempService {

	Map<String, Object> tempList(RowBounds rb, Map<String, Object> params);

	List<QuoteTemp> tempList();

	List<QuoteTempDetails> tempDetailsListByTempId(String tempId);

	QuoteTemp findTempById(String tempId);

	/**
	 * 删除当前行和下一节点的数据
	 * 
	 * @param id
	 * @param parentId
	 * @return
	 */
	int removeSeg(String id, String parentId);

	/**
	 * 保存段数据
	 * 
	 * @param details
	 * @return
	 */
	int saveSeg(QuoteTempDetails details);

	/**
	 * 根据详情id获取详情数据
	 * 
	 * @param detailsId
	 * @return
	 */
	QuoteTempDetails findTempDetailsById(String detailsId);

	/**
	 * 保存模版信息
	 * 
	 * @param temp
	 * @param detailsList
	 * @return
	 */
	int save(QuoteTemp temp, String detailsList);

	/**
	 * 逐条删除模板信息
	 * 
	 * @param id
	 * @return
	 */
	int removeTemp(String id);

	/**
	 * 批量删除模板信息
	 * 
	 * @param list
	 * @return
	 */
	int batchRemoveTemp(List<String> list);

	/**
	 * 校验编码是否重复
	 * 
	 * @param code
	 * @param id
	 * @return
	 */
	int checkCode(String code, String id);

	/**
	 * tempId源模板id，target目标模板id
	 * 
	 * @param tempId
	 * @param target
	 * @return
	 */
	List<QuoteTempDetails> copyTemp(String tempId, String target);

	/**
	 * 拼接名称和编码作为下拉选择
	 * 
	 * @return
	 */
	List<QuoteTemp> findTempListForSelected();

}
