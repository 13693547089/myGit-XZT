package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.mdm.QualSupp;

public interface ReceiveMessageService {

	/**
	 * 根据接收单位获取接收单位信息
	 * 
	 * @param receUnit
	 * @return
	 */
	ReceiveMessage queryReceiveMessByReceUnit(String receUnit);

	/**
	 * 收货信息列表数据
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryReceiveMessByPage(Map<String, Object> map);

	/**
	 * 删除收货信息
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteReceMessById(String[] ids);

	/**
	 * 保存收货信息
	 * 
	 * @param receMess
	 * @return
	 */
	boolean addReceiveMessage(ReceiveMessage receMess);

	/**
	 * 根据主键查询收货信息
	 * 
	 * @param id
	 * @return
	 */
	ReceiveMessage queryReceMessById(String id);

	/**
	 * 修改收货信息
	 * 
	 * @param receMess
	 * @return
	 */
	boolean udpateReceiveMessage(ReceiveMessage receMess);

	/**
	 * 查询所有接收单位
	 * 
	 * @return
	 */
	List<String> queryAllReceUnitOfReceiveMess();

	/**
	 * 根据参数获取供应商信息
	 * 
	 * @param string
	 * @return
	 */
	List<QualSupp> findSuppInfo(String string);

	/**
	 * 获取收货单位，预约发布导出功能使用
	 * 
	 * @param userId
	 * @return
	 */
	String queryReceUnitbyPost(Long userId);

	/**
	 * 查询获取收货地址列表
	 * 
	 * @return
	 */
	List<ReceiveMessage> findReceiveAddr();
}
