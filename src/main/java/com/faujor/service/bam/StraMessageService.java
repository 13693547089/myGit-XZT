package com.faujor.service.bam;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.StraMessage;

public interface StraMessageService {

	/**
	 * 直发通知列表展示
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryStraMessageByPage(Map<String, Object> map);

	/**
	 * 新建直发通知
	 * 
	 * @param straMess
	 * @return
	 */
	boolean addStraMessage(StraMessage straMess, List<MessMate> messMate);

	/**
	 * 批量删除直发通知
	 * 
	 * @param messIds
	 * @return
	 */
	boolean deleteStraMessByMessId(String[] messIds);

	/**
	 * 查询直发通知的详情
	 * 
	 * @param messId
	 * @return
	 */
	StraMessage queryStraMessageByMessId(String messId);

	/**
	 * 编辑直发通知
	 * 
	 * @param straMess
	 * @return
	 */
	boolean updateStraMessageByMessId(StraMessage straMess, List<MessMate> messMate);

	/**
	 * 修改直发通知的状态
	 * 
	 * @param map
	 * @return
	 */
	boolean updateStraMessStatusByMessId(Map<String, Object> map);

	/**
	 * 根据提货单号查询直发通知单的详情和直发通知单下的物资
	 * 
	 * @param mapgCode
	 * @return
	 */
	Map<String, Object> queryStraMessageBymessCode(String mapgCode);

	/**
	 * 根据局直发通知的提货单号修改直发通知的状态
	 * 
	 * @param map
	 * @return
	 */
	boolean updateMessStatusByMessCode(Map<String, Object> map);

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
	StraMessage queryOneStraMessageByMessCode(String mapgCode);

	/**
	 * 作废直发通知
	 * 
	 * @param map
	 * @return
	 */
	boolean cancellStraMessByMessId(Map<String, Object> map);

	/**
	 * 根据调拨单号获取到改挑拨单对应的办成品采购订单
	 * 
	 * 并处理，获取可以用的采购订单
	 * 
	 * @param mm
	 * @return
	 */
	Map<String, Object> getStraMates(MessMate mm);

	boolean updateDeliDate(StraMessage straMess, String type);

	/**
	 * 修改调拨单状态
	 * 
	 * @param alloNo
	 * @return
	 */
	int updateAlloOrderStatus(String alloNo);

}
