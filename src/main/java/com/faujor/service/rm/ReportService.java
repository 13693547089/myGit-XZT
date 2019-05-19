package com.faujor.service.rm;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.rm.BillReport;
import com.faujor.entity.rm.BillReportDetails;
import com.faujor.entity.rm.LoginFrequence;
import com.faujor.entity.rm.LoginOperateVO;
import com.faujor.entity.rm.LoginReport;
import com.faujor.entity.rm.ProductMarketReport;
import com.faujor.entity.rm.PurchaseOrder;
import com.faujor.entity.rm.SqlParams;
import com.faujor.utils.PageUtils;

public interface ReportService {

	/**
	 * 单据记录统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	BillReport getBillReport(String startDate, String endDate);

	/**
	 * 单据记录统计详情
	 * 
	 * @param firstMonth
	 * @param thirdMonth
	 * @param secondMonth
	 * @return
	 */
	List<BillReportDetails> findDetailsData(String firstMonth, String secondMonth, String thirdMonth);

	/**
	 * 登录，操作环比
	 * 
	 * @param sqlParams
	 * @return
	 */
	LoginReport findLoginReportRatio(SqlParams sqlParams);

	/**
	 * 登录操作频率
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<List<Object>> findLoginOperateFrequence(SqlParams sqlParams);

	/**
	 * top 10
	 * 
	 * @param sqlParams
	 * @return
	 */
	List<List<Object>> findTopTenInfo(SqlParams sqlParams);

	/**
	 * 采购订单数据
	 * 
	 * @param dateStr
	 * @return
	 */
	List<List<Object>> findPurchaseOrderData(String dateStr);

	/**
	 * 产销管理报表数据
	 * 
	 * @param dateStr
	 * @return
	 */
	List<List<Object>> findProductMarketData(String dateStr);

	/**
	 * 登录使用详情
	 * 
	 * @param endDay
	 * @param user_type
	 * @param user_dept
	 * @param username
	 * @param startDay
	 * @return
	 */
	PageUtils<LoginOperateVO> findLoginList(String startDay, String endDay, String user_type, String user_dept,
			String username);

	/**
	 * 登录使用详情
	 * 
	 * @param startDay
	 * @param endDay
	 * @param username
	 * @param userId 
	 * @return
	 */
	PageUtils<LoginOperateVO> findLoginDetails(String startDay, String endDay, String username, Long userId);

}
