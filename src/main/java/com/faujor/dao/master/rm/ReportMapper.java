package com.faujor.dao.master.rm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.rm.BillReport;
import com.faujor.entity.rm.BillReportDetails;
import com.faujor.entity.rm.LoginFrequence;
import com.faujor.entity.rm.LoginOperateVO;
import com.faujor.entity.rm.LoginReport;
import com.faujor.entity.rm.ProductMarketVO;
import com.faujor.entity.rm.PurchaseOrderVO;
import com.faujor.entity.rm.SqlParams;

public interface ReportMapper {

	/**
	 * 主数据计数
	 * 
	 * @param sqlParams
	 * @return
	 */
	Integer countMainData(SqlParams sqlParams);

	/**
	 * 行项目计数
	 * 
	 * @param sqlParams
	 * @return
	 */
	Integer countDetailsData(SqlParams sqlParams);

	/**
	 * 单据数据详情
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<BillReportDetails> findModelDetailsData(SqlParams sqlParams);

	/**
	 * 登录，操作环比统计
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<LoginReport> findLoginReportRatio(SqlParams sqlParams);

	/**
	 * 登录，操作的频率
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<LoginFrequence> findLoginOperateFrequence(SqlParams sqlParams);

	/**
	 * top 10
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<LoginFrequence> findTopTenInfo(SqlParams sqlParams);

	/**
	 * 查询某个月的po 计数数据
	 * 
	 * @param dateStr
	 * @return
	 */
	PurchaseOrderVO findPODataByMonth(String dateStr);

	/**
	 * 根据月份获取 某月的计数数据
	 * 
	 * @param dateStr
	 * @return
	 */
	ProductMarketVO findProductMarkerByMonth(String dateStr);

	/**
	 * 登录使用详情列表
	 * 
	 * @param paramsMap
	 * @return
	 */
	List<LoginOperateVO> findLoginList(Map<String, Object> paramsMap);

	/**
	 * 登录使用详情
	 * 
	 * @param params
	 * @return
	 */
	List<LoginOperateVO> findLoginDetails(Map<String, String> params);

	/**
	 * 获取采购订单数量
	 * @param sqlParams 
	 * 
	 * @return
	 */
	BillReport countPurchaseOrderNum(SqlParams sqlParams);
}
