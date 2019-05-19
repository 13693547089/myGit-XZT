package com.faujor.service.bam;

import com.alibaba.fastjson.JSON;
import com.faujor.entity.bam.*;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.utils.RestCode;

import java.util.List;
import java.util.Map;

public interface OrderPackService {
	
	/**
	 * 获取包材采购订单列表
	 * @param map
	 * @return
	 */
	Map<String, Object> queryOrderPackByPage(Map<String, Object> map);
	/**
	 * 根据采购订单编号获取采购订信息
	 * @param oemOrderCode
	 * @return
	 */
	Map<String, Object> queryOrderPackMessage(String oemOrderCode);
	/**
	 * 根据采购订单编号获取料号用于弹出框下拉
	 * @param oemOrderCode
	 * @return
	 */
	List<OrderMate> queryMateNumbByOrderCode(String oemOrderCode);

    /**
     * 根据料号获取品名
     * @param mateCode
     * @return
     */
    String queryMateNameByMateNumb(String mateCode);
	/**
	 * 保存包材订单
	 * @param orderList
	 * @return
	 */
	RestCode saveOrderPackMate(List<OrderPackForm> orderPackFormList, List<OrderPackMess> packMessList, List<OrderPackMate> packMateList);

	/**
	 * 保存包材订单
	 * @param orderList
	 * @return
	 */
	RestCode saveOrderPackMateByEdit(String orderPackID, List<OrderPackMate> packMateList,List<OrderPackMess> packMessList);

	/**
	 * 删除订单
	 *
	 * @param ids
	 * @return
	 */
	boolean deleteOrderPackById(String[] ids);

	/**
	 * 修改状态
	 * @param status
	 * @param oemOrderCode
	 * @return
	 */
	public int updateOrderStatus(String status,String oemOrderCode);

	/**
	 * 查询货源中对应的供应商
	 */
	public List<QualSupp> queryAllQualSuppListByUserId(String userId);

	/**
	 * 查询货源中对应的供应商
	 */
	public List<OrderPackForm> queryOrderPackFormByCode(String oemOrderCode);

	/**
	 * 根据供应商编号查询NB类型的采购订单编码集合
	 * @param string
	 * @param sapId
	 * @return
	 */
	public List<String> queryOrderMessListByOrderTypeAndSapId(String string, String sapId);

	/**
	 * 根据采购订单编号更新包材信息
	 * @param oemOrderCode
	 * @return
	 */
	Map<String, Object> updateOrderPackMess(List<OrderPackMess> packMessList, String oemOrderCode,String packId);

    OrderPackVO queryOrderMessageByOEMOrderCode(String oemOrderCode);

	/**
	 * 根据包材主表id查询包材主表信息
	 * @param id
	 * @return
	 */
	OrderPackVO queryOrderPackById(String id);
}
