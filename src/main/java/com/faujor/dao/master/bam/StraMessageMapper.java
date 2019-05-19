package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.StraMessAndMateDO;
import com.faujor.entity.bam.StraMessDO;
import com.faujor.entity.bam.StraMessage;

public interface StraMessageMapper {

	/**
	 * 新建直发通知
	 * 
	 * @param starMess
	 * @return
	 */
	public int addStraMessage(StraMessage straMess);

	/**
	 * 添加直发通知下的物资
	 * 
	 * @param messMate
	 * @return
	 */
	public int addMessMate(MessMate messMate);

	/**
	 * 直发通知列表分页展示
	 * 
	 * @param map
	 * @return
	 */
	public List<StraMessDO> queryStraMessageByPage(Map<String, Object> map);

	/**
	 * 直发通知信息数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryStraMessageByPageCount(Map<String, Object> map);

	/**
	 * 批量删除直发通知
	 * 
	 * @param messIds
	 * @return
	 */
	public int deleteStraMessByMessId(String[] messIds);

	/**
	 * 批量删除直发通知下的物资信息
	 * 
	 * @param messIds
	 * @return
	 */
	public int deleteMessMateByMessId(String[] messIds);

	/**
	 * 查询直发通知的详细信息
	 * 
	 * @param messId
	 * @return
	 */
	public StraMessage queryStraMessageByMessId(String messId);

	/**
	 * 修改直发通知
	 * 
	 * @param straMess
	 * @return
	 */
	public int updateStraMessageByMessId(StraMessage straMess);

	/**
	 * 修改直发通知状态
	 * 
	 * @param map
	 * @return
	 */
	public int updateStraMessStatusByMessId(Map<String, Object> map);

	/**
	 * 根据提货单号查询直发通知单的详情和直发通知单下的物资
	 * 
	 * @param mapgCode
	 * @return
	 */
	public StraMessage queryStraMessageBymessCode(String messCode);

	/**
	 * 根据局直发通知的提货单号修改直发通知的状态
	 * 
	 * @param map
	 * @return
	 */
	public int updateMessStatusByMessCode(Map<String, Object> map);

	/**
	 * 查询所有已通知的直发通知
	 * 
	 * @return
	 */
	public List<StraMessage> queryAllNotifiedStraMessage(Map<String, Object> map);

	/**
	 * 根据提货单号查询直发通知的信息
	 * 
	 * @param mapgCode
	 * @return
	 */
	public StraMessage queryOneStraMessageByMessCode(String mapgCode);

	/**
	 * 作废直发通知
	 * 
	 * @param map
	 * @return
	 */
	public int cancellStraMessByMessId(Map<String, Object> map);

	/**
	 * 获取占有量
	 * 
	 * @param params
	 * @return
	 */
	public List<StraMessAndMateDO> findOccupyNumberByParams(Map<String, Object> params);

	/**
	 * 根据zzoem和半成品物料编码查询已保存状态的直发通知单的信息
	 * 
	 * @param map
	 * @return
	 */
	public List<MessMate> queryStarMessMateByZzoemAndMateCode(Map<String, Object> map);

	public List<StraMessage> queryStarMessByZzoemAndMateCode(Map<String, Object> map);

	/**
	 * 根据调拨单，获取成品物料，并转化成半成品物料
	 * 
	 * @param alloNo
	 * @return
	 */
	public List<MessMate> findMessMateByAlloNO(String alloNo);

	public boolean updateDeliDate(StraMessage straMess);

}
