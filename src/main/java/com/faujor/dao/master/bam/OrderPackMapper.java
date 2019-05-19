package com.faujor.dao.master.bam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.*;
import org.apache.ibatis.annotations.Param;

public interface OrderPackMapper {

	/**
	 *  获取包材采购订单列表
	 * @param map
	 * @return
	 */
	List<OrderPackVO> queryOrderPackByPage(Map<String, Object> map);

	/**
	 *  获取所有包材采购订单列表
	 * @param map
	 * @return
	 */
	List<OrderPackMate> queryALLOrderPackByPackIds(String[] ids);
	/**
	 *  获取包材采购订单列表数据的总数量
	 * @param map
	 * @return
	 */
	int queryOrderPackByPageCount(Map<String, Object> map);

	/**
	 * 根据订单编号查询采购订信息
	 * @param oemOrderCode
	 * @return
	 */
	public OrderPackVO queryOrderMessageByOEMOrderCode(String oemOrderCode);

	/**
	 * 获取某个订单的物料
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderMate> queryOrderMateByContOrdeNumb(String contOrdeNumb);

	/**
	 * 获取包材信息
	 *
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderPackMess> queryBaocaiInfoByContOrdeNumb(String contOrdeNumb);

	/**
	 * 获取包材信息(根据包材信息表)
	 *
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderPackMess> queryBaocaiInfo2ByContOrdeNumb(String contOrdeNumb);

	/**
	 * 获取包材订单信息
	 *
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderPackMate> queryBaocaiOrderDetailByContOrdeNumb(String contOrdeNumb);

	/**
	 * 删除订单信息
	 *
	 * @param ids
	 * @return
	 */
	public int deleteOrderPackMateById(String[] ids);

	/**
	 * 删除订单信息
	 *
	 * @param ids
	 * @return
	 */
	public int deleteOrderPackMessById(String[] ids);

	/**
	 * 删除订单信息
	 *
	 * @param ids
	 * @return
	 */
	public int deleteOrderPackById(String[] ids);

	/**
	 * 根据订单编号查询料号
	 * @param oemOrderCode
	 * @return
	 */
	public List<OrderMate> queryMateNumbByOrderCode(String oemOrderCode);

	/**
	 * 根据料号查询品名
	 * @param mateCode
	 * @return
	 */
	public String queryMateNameByMateNumb(String mateCode);

	/**
	 * 保存包材订单主表
	 * @param orderPackFormList
	 * @return
	 */
	public int saveOrderPackForm(List<OrderPackForm> orderPackFormList);/**

	 * 保存包材信息
	 * @param packMessList
	 * @return
	 */
	public int saveOrderPackMess(List<OrderPackMess> packMessList);/**

	 * 保存包材订单
	 * @param orderList
	 * @return
	 */
	public int saveOrderPackMate(List<OrderPackMate> orderList);

	/**
	 * 修改状态(多参数处理@Param)
	 * @param status
	 * @param oemOrderCode
	 * @return
	 */
	public int updateOrderStatus(@Param("status")String status, @Param("oemOrderCode")String oemOrderCode);

	/**
	 * 获取包材订单信息
	 *
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderPackMate> queryOrderPackMateByOrderPackID(String orderPackID);

	/**
	 * 删除订单信息
	 *
	 * @param ids
	 * @return
	 */
	public int deleteOrderPackMateByMainId(String[] ids);

	/**
	 * 修改状态(多参数处理@Param)
	 * @param userId
	 * @param userName
	 * @return
	 */
	public int updateOrderParckUserTime(@Param("userId")String userId, @Param("userName")String userName,@Param("orderPackID")String orderPackID);

	/**
	 * 获取包材订单信息
	 */
	public List<OrderPackForm> queryOrderPackFormByCode(String oemOrderCode);


	/**
	 * 根据供应商编号查询NB类型的采购订单编码集合
	 * @param orderType
	 * @param sapId
	 * @return
	 */
	public List<String> queryOrderMessListByOrderTypeAndSapId(@Param("orderType")String orderType, @Param("sapId")String sapId);

	/**
	 * 修改状态(多参数处理@Param)
	 */
	public int updateOrderMessOrderedAndNoOrderedNum(@Param("id")String id, @Param("packTotalNum")Double packTotalNum, @Param("placedNum")Double placedNum, @Param("residueNum")Double residueNum);



	/**
	 * 修改状态(多参数处理@Param)
	 */
	public int updateOrderPackMate(OrderPackMate orderPackMate);

	/**
	 * 修改下单限比
	 */
	public int updateOrderPackLimitThan(@Param("packId")String packId, @Param("limitThan")BigDecimal limitThan);

	OrderPackVO queryOrderPackById(String id);
}
