package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.ReceiveMessage;

public interface ReceiveMessageMapper {

	/**
	 * 根据接收单位获取接收单位信息
	 * 
	 * @param receUnit
	 * @return
	 */
	public ReceiveMessage queryReceiveMessByReceUnit(String receUnit);

	/**
	 * 收货信息列表数据
	 * 
	 * @param map
	 * @return
	 */
	public List<ReceiveMessage> queryReceiveMessByPage(Map<String, Object> map);

	/**
	 * 收货信息数据的数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryReceiveMessByPageCount(Map<String, Object> map);

	/**
	 * 删除收货信息
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteReceMessById(String[] ids);

	/**
	 * 保存收货信息
	 * 
	 * @param receMess
	 * @return
	 */
	public int addReceiveMessage(ReceiveMessage receMess);

	/**
	 * 根据主键查询收货信息
	 * 
	 * @param id
	 * @return
	 */
	public ReceiveMessage queryReceMessById(String id);

	/**
	 * 修改收货信息
	 * 
	 * @param receMess
	 * @return
	 */
	public int udpateReceiveMessage(ReceiveMessage receMess);

	/**
	 * 查询所有接收单位
	 * 
	 * @return
	 */
	public List<String> queryAllReceUnitOfReceiveMess();

	/**
	 * 根据岗位查询收货单位信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<ReceiveMessage> queryReceiveMessByPost(String userId);

	/**
	 * 查询所有收货地址
	 * 
	 * @return
	 */
	public List<ReceiveMessage> findReceiveAddr();

}
