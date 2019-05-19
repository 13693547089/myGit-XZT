package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.common.AsyncLog;
import com.faujor.utils.RestCode;

public interface ReceiveService {

	/**
	 * 查询收货列表的数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryReceiveByPage(Map<String, Object> map);

	/**
	 * 删除收货单
	 * 
	 * @param receIds
	 * @return
	 */
	public boolean deleteReceiveByReceId(String[] receIds);

	/**
	 * 新建收货单
	 * 
	 * @param rece
	 * @param list
	 * @return
	 */
	public boolean addReceive(Receive rece, List<ReceMate> list);

	/**
	 * 根据收货单主键查询收货单的详细信息
	 * 
	 * @param receId
	 * @return
	 */
	public Receive queryReceiveByReceId(String receId);

	/**
	 * 根据收货单的主键查询收货单下的物资信息
	 * 
	 * @param receId
	 * @return
	 */
	public List<ReceMate> queryReceMatesByReceId(String receId);

	/**
	 * 根据收货单的主键修改收货单信息
	 * 
	 * @param rece
	 * @param list
	 * @return
	 */
	public boolean updateReceiveByReceId(Receive rece, String type, List<ReceMate> list);

	/**
	 * 根据送货单号查询送货单信息
	 * 
	 * @param deliCode
	 * @return
	 */
	Map<String, Object> queryDeliveryByDeliCode(String deliCode);

	public String asyncToSAP(Receive rece, String receMateData);

	/**
	 * 收货单冲销
	 * 
	 * @param receCode
	 * @param inboDeliCodes
	 * @return
	 */
	public Map<String, Object> writeoffReceive(String receCode, String inboDeliCodes, String deliCode);

	/**
	 * 收货列表查看界面
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryAllReceiveByPage(Map<String, Object> map);

	/**
	 * 新建收货
	 * 
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @return
	 */
	public Map<String, Object> createReceive(Receive rece, String receMateData, String type);

	/**
	 * 编辑收货单
	 * 
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @param deliCode2
	 * @return
	 */
	public RestCode updateReceive(Receive rece, String receMateData, String type, String deliCode2);

	/**
	 * 为收货单打上停止占用标识
	 * 
	 * @param al
	 * 
	 * @return
	 */
	public int idenToReceive(AsyncLog al);
	/**
	 * 根据收货单号修改收货单状态
	 * @param map
	 * @return
	 */
	public boolean updateStatusOfReceiveByReceCode(Map<String, Object> map);
	/**
	 * 修改收货单的内向交货单号和is_occupy占用状态
	 * @param receMate
	 * @return
	 */
	public boolean updateReceMateInboDeliCodeAndIsOccupy(ReceMate receMate);
	/**
	 * 根据id查询收货单物料信息
	 * @param id
	 * @return
	 */
	public ReceMate queryReceMateMessById(String id);
	/**
	 * 删除收货单
	 * @param receIds
	 * @param deliCodes2
	 * @return
	 */
	public boolean deleteReceiveByReceId2(String[] receIds, String deliCodes2);
}
