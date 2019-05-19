package com.faujor.service.rm.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.dao.master.rm.ReportMapper;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserVO;
import com.faujor.entity.rm.BillReport;
import com.faujor.entity.rm.BillReportDetails;
import com.faujor.entity.rm.LoginFrequence;
import com.faujor.entity.rm.LoginOperateVO;
import com.faujor.entity.rm.LoginReport;
import com.faujor.entity.rm.ProductMarketReport;
import com.faujor.entity.rm.ProductMarketVO;
import com.faujor.entity.rm.PurchaseOrder;
import com.faujor.entity.rm.PurchaseOrderVO;
import com.faujor.entity.rm.SqlParams;
import com.faujor.service.rm.ReportService;
import com.faujor.utils.PageUtils;

@Service("reportService")
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;
	@Autowired
	private OrgMapper orgMapper;

	@Override
	public BillReport getBillReport(String startDate, String endDate) {
		BillReport billReport = new BillReport();
		SqlParams sqlParams = new SqlParams();
		sqlParams.setStart_date(startDate);
		sqlParams.setEnd_date(endDate);

		// 报价单
		sqlParams.setMain_table_name("BAM_QUOTE");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");

		sqlParams.setDetails_table_name("BAM_QUOTE_MATE");
		sqlParams.setOn_condition("t1.QUOTE_CODE = t2.QUOTE_CODE");
		Integer quoteNum = reportMapper.countMainData(sqlParams);
		billReport.setQuoteNum(quoteNum);
		Integer quoteDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setQuoteDetailsNum(quoteDetailsNum);

		// 采购合同
		sqlParams.setMain_table_name("BAM_CONT");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("CONT_TYPE");
		Integer purchaseContractNum = reportMapper.countMainData(sqlParams);
		billReport.setPurchaseContractNum(purchaseContractNum);
		sqlParams.setDetails_table_name("DOC_DOCUMENT");
		sqlParams.setOn_condition(" t1.CONT_NO = t2.LINK_NO AND t2.DOC_CATE = 'bam_cont' ");
		Integer purchaseContractDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setPurchaseContractDetailsNum(purchaseContractDetailsNum);

		// 采购订单
		BillReport br = reportMapper.countPurchaseOrderNum(sqlParams);
		billReport.setPurchaseOrderNum(br.getPurchaseOrderNum());
		billReport.setPurchaseOrderDetailsNum(br.getPurchaseOrderDetailsNum());

		// 打切联络单
		sqlParams.setMain_table_name("BAM_CUT_LIAI");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer cutLiaiNum = reportMapper.countMainData(sqlParams);
		billReport.setCutLiaiNum(cutLiaiNum);
		sqlParams.setDetails_table_name("BAM_CUT_LIAI_MATE");
		sqlParams.setOn_condition(" t1.LIAI_ID = t2.LIAI_ID ");
		Integer cutLiaiDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setCutLiaiDetailsNum(cutLiaiDetailsNum);

		// 打切计划
		sqlParams.setMain_table_name("BAM_CUT_PLAN");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer cutPlanNum = reportMapper.countMainData(sqlParams);
		billReport.setCutPlanNum(cutPlanNum);
		sqlParams.setDetails_table_name("BAM_CUT_PLAN_MATE");
		sqlParams.setOn_condition(" t1.PLAN_ID = t2.PLAN_ID ");
		Integer cutPlanDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setCutPlanDetailsNum(cutPlanDetailsNum);

		// 生产/交货计划
		sqlParams.setMain_table_name("PS_PAD_PLAN");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("string");
		sqlParams.setDate_type("month");
		sqlParams.setAdd_where_column("");
		Integer prodDeliNum = reportMapper.countMainData(sqlParams);
		billReport.setProdDeliNum(prodDeliNum);
		sqlParams.setDetails_table_name("PS_PAD_PLAN_DETAIL");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		Integer prodDeliDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setProdDeliDetailsNum(prodDeliDetailsNum);

		// 备货计划
		sqlParams.setMain_table_name("PS_SUPP_INVEN_PLAN");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer stockPrePlanNum = reportMapper.countMainData(sqlParams);
		billReport.setStockPrePlanNum(stockPrePlanNum);
		sqlParams.setDetails_table_name("PS_SUPP_PROD");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		Integer stockPrePlanDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setStockPrePlanDetailsNum(stockPrePlanDetailsNum);

		// 排产计划
		sqlParams.setMain_table_name("PS_SUPP_PROD");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer schedulPlanNum = reportMapper.countMainData(sqlParams);
		billReport.setSchedulPlanNum(schedulPlanNum);

		// 产能上报
		sqlParams.setMain_table_name("PS_PDR");
		sqlParams.setWhere_column("PRODUCE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setDate_type("");
		sqlParams.setAdd_where_column("");
		Integer capacityReportNum = reportMapper.countMainData(sqlParams);
		billReport.setCapacityReportNum(capacityReportNum);
		sqlParams.setDetails_table_name("PS_PDR_DETAIL");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		Integer capacityReportDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setCapacityReportDetailsNum(capacityReportDetailsNum);

		// 预约申请
		sqlParams.setMain_table_name("BAM_APPOINT");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer appoApplyNum = reportMapper.countMainData(sqlParams);
		billReport.setAppoApplyNum(appoApplyNum);
		sqlParams.setDetails_table_name("BAM_APPO_MATE");
		sqlParams.setOn_condition(" t1.APPO_ID = t2.APPO_ID ");
		Integer appoApplyDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setAppoApplyDetailsNum(appoApplyDetailsNum);

		// 直发通知
		sqlParams.setMain_table_name("BAM_MESSAGE");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer directNoticeNum = reportMapper.countMainData(sqlParams);
		billReport.setDirectNoticeNum(directNoticeNum);
		sqlParams.setDetails_table_name("BAM_MESS_MATE");
		sqlParams.setOn_condition(" t1.MESS_ID = t2.MESS_ID ");
		Integer directNoticeDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setDirectNoticeDetailsNum(directNoticeDetailsNum);

		// 送货单
		sqlParams.setMain_table_name("BAM_DELIVERY");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer deliveryNum = reportMapper.countMainData(sqlParams);
		billReport.setDeliveryNum(deliveryNum);
		sqlParams.setDetails_table_name("BAM_DELI_MATE");
		sqlParams.setOn_condition(" t1.DELI_ID = t2.DELI_ID ");
		Integer deliveryDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setDeliveryDetailsNum(deliveryDetailsNum);

		// 收货单
		sqlParams.setMain_table_name("BAM_RECEIVE");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer receiveNum = reportMapper.countMainData(sqlParams);
		billReport.setReceiveNum(receiveNum);
		sqlParams.setDetails_table_name("BAM_RECE_MATE");
		sqlParams.setOn_condition(" t1.RECE_ID = t2.RECE_ID ");
		Integer reveiveDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setReveiveDetailsNum(reveiveDetailsNum);

		// 财务稽核
		sqlParams.setMain_table_name("FAM_AUDIT_ORDER");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer financialAuditNum = reportMapper.countMainData(sqlParams);
		billReport.setFinancialAuditNum(financialAuditNum);
		sqlParams.setDetails_table_name("FAM_AUDIT_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.AUDIT_ID ");
		Integer financialAuditDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setFinancialAuditDetailsNum(financialAuditDetailsNum);

		// 财务往来
		sqlParams.setMain_table_name("FAM_FINA_INTE");
		sqlParams.setWhere_column("CREAT_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer financialTransNum = reportMapper.countMainData(sqlParams);
		billReport.setFinancialTransNum(financialTransNum);
		sqlParams.setDetails_table_name("FAM_FINA_ATTA");
		sqlParams.setOn_condition(" t1.FID = t2.MAIN_ID ");
		Integer financialTransDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setFinancialTransDetailsNum(financialTransDetailsNum);

		// 采购对账
		sqlParams.setMain_table_name("FAM_PURCH_RECON");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer purchRecoNum = reportMapper.countMainData(sqlParams);
		billReport.setPurchRecoNum(purchRecoNum);
		sqlParams.setDetails_table_name("FAM_PURCH_RECON_MATE");
		sqlParams.setOn_condition(" t1.RECON_CODE = t2.RECON_CODE ");
		Integer purchRecoDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setPurchRecoDetailsNum(purchRecoDetailsNum);

		// OEM包材订单
		sqlParams.setMain_table_name("BAM_ORDER_PACK");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer OEMOrderNum = reportMapper.countMainData(sqlParams);
		billReport.setOEMOrderNum(OEMOrderNum);
		sqlParams.setDetails_table_name("BAM_ORDER_PACK_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.ORDER_PACK_ID ");
		Integer OEMOrderDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setOEMOrderDetailsNum(OEMOrderDetailsNum);

		// 包材联络单
		sqlParams.setMain_table_name("BAM_CUT_BAOCAI");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer OEMLiaiNum = reportMapper.countMainData(sqlParams);
		billReport.setOEMLiaiNum(OEMLiaiNum);
		sqlParams.setDetails_table_name("BAM_CUT_BAOCAI_MATE");
		sqlParams.setOn_condition(" t1.LIAI_ID = t2.LIAI_ID ");
		Integer OEMLiaiDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setOEMLiaiDetailsNum(OEMLiaiDetailsNum);

		// 包材产能月报
		sqlParams.setMain_table_name("PS_CAP_REP");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("");
		Integer OEMCapacityReportNum = reportMapper.countMainData(sqlParams);
		billReport.setOEMCapacityReportNum(OEMCapacityReportNum);
		sqlParams.setDetails_table_name("PS_CAP_REP_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		Integer OEMCapacityReportDetailsNum = reportMapper.countDetailsData(sqlParams);
		billReport.setOEMCapacityReportDetailsNum(OEMCapacityReportDetailsNum);

		return billReport;
	}

	@Override
	public List<BillReportDetails> findDetailsData(String firstMonth, String secondMonth, String thirdMonth) {
		SqlParams sqlParams = new SqlParams();
		sqlParams.setFirstMonth(firstMonth);
		sqlParams.setSecondMonth(secondMonth);
		sqlParams.setThirdMonth(thirdMonth);
		List<BillReportDetails> target = new ArrayList<>();

		// 报价单
		sqlParams.setMain_table_name("BAM_QUOTE");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("BAM_QUOTE_MATE");
		sqlParams.setOn_condition("t1.QUOTE_CODE = t2.QUOTE_CODE");
		target = getBillReportDetailsList(target, sqlParams, "报价单");

		// 采购订单
		sqlParams.setMain_table_name("BAM_ORDE_RELE");
		sqlParams.setWhere_column("SUBE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setAdd_where_column("t.ORTYPE = 'NB' and t.PURCH_ORG = '2000'");
		// 状态字段一样
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("BAM_ORDE_MATE");
		sqlParams.setOn_condition(" t1.CONT_ORDE_NUMB = t2.MAIN_ID ");
		sqlParams.setAdd_detials_where_column("t1.ORTYPE = 'NB' and t1.PURCH_ORG = '2000'");
		target = getBillReportDetailsList(target, sqlParams, "采购订单");

		// 打切联络单
		sqlParams.setMain_table_name("BAM_CUT_LIAI");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setAdd_where_column("");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("BAM_CUT_LIAI_MATE");
		sqlParams.setOn_condition(" t1.LIAI_ID = t2.LIAI_ID ");
		sqlParams.setAdd_detials_where_column("");
		target = getBillReportDetailsList(target, sqlParams, "打切联络单");

		// 打切计划
		sqlParams.setMain_table_name("BAM_CUT_PLAN");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("BAM_CUT_PLAN_MATE");
		sqlParams.setOn_condition(" t1.PLAN_ID = t2.PLAN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "打切计划");

		// 生产/交货计划
		sqlParams.setMain_table_name("PS_PAD_PLAN");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("string");
		sqlParams.setDate_type("month");
		sqlParams.setDetails_table_name("PS_PAD_PLAN_DETAIL");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "生产/交货计划");

		// 备货计划
		sqlParams.setMain_table_name("PS_SUPP_INVEN_PLAN");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("PS_SUPP_PROD");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "备货计划");

		// 排产计划
		sqlParams.setMain_table_name("PS_SUPP_PROD");
		sqlParams.setWhere_column("PLAN_MONTH");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("");
		target = getBillReportDetailsList(target, sqlParams, "排产计划");

		// 产能上报
		sqlParams.setMain_table_name("PS_PDR");
		sqlParams.setWhere_column("PRODUCE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setDate_type("");
		sqlParams.setDetails_table_name("PS_PDR_DETAIL");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "产能上报");

		// 预约申请
		sqlParams.setMain_table_name("BAM_APPOINT");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("APPO_STATUS");
		sqlParams.setDetails_table_name("BAM_APPO_MATE");
		sqlParams.setOn_condition(" t1.APPO_ID = t2.APPO_ID ");
		target = getBillReportDetailsList(target, sqlParams, "预约申请");

		// 直发通知
		sqlParams.setMain_table_name("BAM_MESSAGE");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("MESS_STATUS");
		sqlParams.setDetails_table_name("BAM_MESS_MATE");
		sqlParams.setOn_condition(" t1.MESS_ID = t2.MESS_ID ");
		target = getBillReportDetailsList(target, sqlParams, "直发通知");

		// 送货单
		sqlParams.setMain_table_name("BAM_DELIVERY");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("BAM_DELI_MATE");
		sqlParams.setOn_condition(" t1.DELI_ID = t2.DELI_ID ");
		target = getBillReportDetailsList(target, sqlParams, "送货单");

		// 收货单
		sqlParams.setMain_table_name("BAM_RECEIVE");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("BAM_RECE_MATE");
		sqlParams.setOn_condition(" t1.RECE_ID = t2.RECE_ID ");
		target = getBillReportDetailsList(target, sqlParams, "收货单");

		// 财务稽核
		sqlParams.setMain_table_name("FAM_AUDIT_ORDER");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("AUDIT_STATUS");
		sqlParams.setDetails_table_name("FAM_AUDIT_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.AUDIT_ID ");
		target = getBillReportDetailsList(target, sqlParams, "财务稽核");

		// 财务往来
		sqlParams.setMain_table_name("FAM_FINA_INTE");
		sqlParams.setWhere_column("CREAT_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("FAM_FINA_ATTA");
		sqlParams.setOn_condition(" t1.FID = t2.MAIN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "财务往来");

		// 采购对账
		sqlParams.setMain_table_name("FAM_PURCH_RECON");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("RECON_STATUS_DESC");
		sqlParams.setDetails_table_name("FAM_PURCH_RECON_MATE");
		sqlParams.setOn_condition(" t1.RECON_CODE = t2.RECON_CODE ");
		target = getBillReportDetailsList(target, sqlParams, "采购对账");

		// OEM包材订单
		sqlParams.setMain_table_name("BAM_ORDER_PACK");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("STATUS");
		sqlParams.setDetails_table_name("BAM_ORDER_PACK_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.ORDER_PACK_ID ");
		target = getBillReportDetailsList(target, sqlParams, "OEM包材订单");

		// 包材联络单
		sqlParams.setMain_table_name("BAM_CUT_BAOCAI");
		sqlParams.setWhere_column("CREATE_DATE");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("BAM_CUT_BAOCAI_MATE");
		sqlParams.setOn_condition(" t1.LIAI_ID = t2.LIAI_ID ");
		target = getBillReportDetailsList(target, sqlParams, "包材联络单");

		// 包材产能月报
		sqlParams.setMain_table_name("PS_CAP_REP");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setDetails_table_name("PS_CAP_REP_MATE");
		sqlParams.setOn_condition(" t1.ID = t2.MAIN_ID ");
		target = getBillReportDetailsList(target, sqlParams, "包材产能月报");

		// 采购合同
		sqlParams.setMain_table_name("BAM_CONT");
		sqlParams.setWhere_column("CREATE_TIME");
		sqlParams.setDate_formate("date");
		sqlParams.setStatus_column("CONT_STATUS_NAME");
		sqlParams.setAdd_where_column("t.CONT_TYPE = 'CG'");
		sqlParams.setDetails_table_name("DOC_DOCUMENT");
		sqlParams.setAdd_detials_where_column("t1.CONT_TYPE = 'CG'");
		sqlParams.setOn_condition(" t1.CONT_NO = t2.LINK_NO AND t2.DOC_CATE = 'bam_cont' ");
		target = getBillReportDetailsList(target, sqlParams, "采购合同");
		return target;
	}

	private List<BillReportDetails> getBillReportDetailsList(List<BillReportDetails> target, SqlParams sqlParams,
			String modelName) {
		List<BillReportDetails> findMainData = reportMapper.findModelDetailsData(sqlParams);
		for (BillReportDetails details : findMainData) {
			details.setModelName(modelName);
			target.add(details);
		}
		return target;
	}

	@Override
	public LoginReport findLoginReportRatio(SqlParams sqlParams) {
		List<LoginReport> list = reportMapper.findLoginReportRatio(sqlParams);
		LoginReport loginReport = new LoginReport();
		String firstMonth = sqlParams.getFirstMonth();
		String secondMonth = sqlParams.getSecondMonth();
		String thirdMonth = sqlParams.getThirdMonth();
		String fourMonth = sqlParams.getFourMonth();

		for (LoginReport lr : list) {
			String monthDate = lr.getMonthDate();
			BigDecimal loginCount = lr.getLoginCount();
			BigDecimal operateCount = lr.getOperateCount();
			if (monthDate.equals(firstMonth)) {
				loginReport.setFirst_login_count(loginCount);
				loginReport.setFirst_operate_count(operateCount);
			} else if (monthDate.equals(secondMonth)) {
				loginReport.setSecond_login_count(loginCount);
				loginReport.setSecond_operate_count(operateCount);
			} else if (monthDate.equals(thirdMonth)) {
				loginReport.setThird_login_count(loginCount);
				loginReport.setThird_operate_count(operateCount);
			} else if (monthDate.equals(fourMonth)) {
				loginReport.setLoginCount(loginCount);
				loginReport.setOperateCount(operateCount);
			}
		}

		// 计算百分比， first月份最大
		// firstMonth 的登录环比
		LoginReport r1 = calculatePercent(loginReport.getSecond_login_count(), loginReport.getFirst_login_count());
		loginReport.setFirst_login_status(r1.getFirst_login_status());
		loginReport.setFirst_login_percent(r1.getFirst_login_percent());
		loginReport.setFirst_login_src(r1.getFirst_login_src());
		// firstMonth 的操作环比
		LoginReport r2 = calculatePercent(loginReport.getSecond_operate_count(), loginReport.getFirst_operate_count());
		loginReport.setFirst_operate_status(r2.getFirst_login_status());
		loginReport.setFirst_operate_percent(r2.getFirst_login_percent());
		loginReport.setFirst_operate_src(r2.getFirst_login_src());
		// secondMonth 的登录环比
		LoginReport r3 = calculatePercent(loginReport.getThird_login_count(), loginReport.getSecond_login_count());
		loginReport.setSecond_login_status(r3.getFirst_login_status());
		loginReport.setSecond_login_percent(r3.getFirst_login_percent());
		loginReport.setSecond_login_src(r3.getFirst_login_src());
		// secondMonth 的操作环比
		LoginReport r4 = calculatePercent(loginReport.getThird_operate_count(), loginReport.getSecond_operate_count());
		loginReport.setSecond_operate_status(r4.getFirst_login_status());
		loginReport.setSecond_operate_percent(r4.getFirst_login_percent());
		loginReport.setSecond_operate_src(r4.getFirst_login_src());
		// thirdMonth 的登录环比
		LoginReport r5 = calculatePercent(loginReport.getLoginCount(), loginReport.getThird_login_count());
		loginReport.setThird_login_status(r5.getFirst_login_status());
		loginReport.setThird_login_percent(r5.getFirst_login_percent());
		loginReport.setThird_login_src(r5.getFirst_login_src());
		// thirdMonth 的操作环比
		LoginReport r6 = calculatePercent(loginReport.getOperateCount(), loginReport.getThird_operate_count());
		loginReport.setThird_operate_status(r6.getFirst_login_status());
		loginReport.setThird_operate_percent(r6.getFirst_login_percent());
		loginReport.setThird_operate_src(r6.getFirst_login_src());
		return loginReport;
	}

	/**
	 * 计算环比 登录的环比
	 * 
	 * @param last
	 * @param current
	 * @return
	 */
	private LoginReport calculatePercent(BigDecimal last, BigDecimal current) {
		// 公式（本月-上月)/上月*100%，-1表示小于，0是等于，1是大于
		LoginReport loginReport = new LoginReport();
		if (last == null || current == null) {
			loginReport.setFirst_login_status("无数据");
			loginReport.setFirst_login_src("/img/empty_data.png");
			return loginReport;
		}
		int i = last.compareTo(BigDecimal.ZERO);
		int j = current.compareTo(BigDecimal.ZERO);
		if (i == -1 || i == 0 || j == -1 || j == 0) {
			loginReport.setFirst_login_status("无数据");
			loginReport.setFirst_login_src("/img/empty_data.png");
		} else {
			int k = current.compareTo(last);
			if (k == -1) {
				loginReport.setFirst_login_status("下跌");
				BigDecimal subtract = last.subtract(current);
				BigDecimal divide = subtract.divide(last, 4, BigDecimal.ROUND_CEILING);
				BigDecimal result = divide.multiply(new BigDecimal(100));
				result = result.setScale(2);
				loginReport.setFirst_login_percent(result + "%");
				loginReport.setFirst_login_src("/img/fall.png");
			} else if (k == 0) {
				loginReport.setFirst_login_status("持平");
				loginReport.setFirst_login_percent("0%");
				loginReport.setFirst_login_src("/img/balance.png");
			} else {
				loginReport.setFirst_login_status("上涨");
				BigDecimal subtract = current.subtract(last);
				BigDecimal divide = subtract.divide(last, 4, BigDecimal.ROUND_CEILING);
				BigDecimal result = divide.multiply(new BigDecimal(100));
				result = result.setScale(2);
				loginReport.setFirst_login_percent(result + "%");
				loginReport.setFirst_login_src("/img/rise.png");
			}
		}
		return loginReport;
	}

	@Override
	public List<List<Object>> findLoginOperateFrequence(SqlParams sqlParams) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("user_dept", "20000001");
//		List<UserVO> user = orgMapper.findUserIdsByParams(map);
//		if (user.size() > 0)
//			sqlParams.setParamsList(user);
		List<LoginFrequence> list = reportMapper.findLoginOperateFrequence(sqlParams);
		List<List<Object>> result = new ArrayList<>();
		List<Object> dimension = new ArrayList<>();
		dimension.add("月份");
		dimension.add("内部登录");
		dimension.add("内部操作");
		dimension.add("外部登录");
		dimension.add("外部操作");
		result.add(dimension);
		for (LoginFrequence lf : list) {
			List<Object> item = new ArrayList<>();
			Integer ilc = lf.getInner_login_count();
			Integer ioc = lf.getInner_operate_count();
			Integer olc = lf.getOuter_login_count();
			Integer ooc = lf.getOuter_operate_count();
			item.add(lf.getMonth_date());
			item.add(ilc);
			item.add(ioc);
			item.add(olc);
			item.add(ooc);
			result.add(item);
		}
		return result;
	}

	@Override
	public List<List<Object>> findTopTenInfo(SqlParams sqlParams) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_dept", "20000001");
		List<UserVO> user = orgMapper.findUserIdsByParams(map);
		if (user.size() > 0)
			sqlParams.setParamsList(user);
		
		List<LoginFrequence> list = reportMapper.findTopTenInfo(sqlParams);
		List<List<Object>> result = new ArrayList<>();
		for (LoginFrequence lf : list) {
			List<Object> item = new ArrayList<>();
			String user_name = lf.getUser_name();
			String user_count = lf.getUser_count();
			item.add(user_name);
			item.add(user_count);
			result.add(item);
		}
		return result;
	}

	@Override
	public List<List<Object>> findPurchaseOrderData(String dateStr) {

		List<List<Object>> result = new ArrayList<>();

		List<Object> item = new ArrayList<>();
		item.add("月份");
		item.add("行项目合计");
		item.add("单据量合计");
		item.add("行项目合计环比");
		item.add("单据量环比");
		item.add("行项目合计同比");
		item.add("单据量同比");
		result.add(item);

		// 第一个月
		String firstMonth = StringUtils.isEmpty(dateStr) ? getLastMonth(null, "month") : dateStr;
		PurchaseOrderVO firstVO = reportMapper.findPODataByMonth(firstMonth);
		String firstYoyMonth = getLastMonth(firstMonth, "year");
		PurchaseOrderVO firstYoyVO = reportMapper.findPODataByMonth(firstYoyMonth);
		String secondMonth = getLastMonth(firstMonth, "month");
		PurchaseOrderVO secondVO = reportMapper.findPODataByMonth(secondMonth);

		PurchaseOrder firstPO = calculPOInfo(firstMonth, firstVO, secondVO, firstYoyVO);
		item = new ArrayList<>();
		item.add(firstMonth);
		item.add(firstPO.getPo_item_count());
		item.add(firstPO.getPo_count());
		item.add(firstPO.getPo_item_chain_ratio());
		item.add(firstPO.getPo_chain_ratio());
		item.add(firstPO.getPo_item_YOY_ratio());
		item.add(firstPO.getPo_YOY_ratio());
		result.add(item);

		String secondYoyMonth = getLastMonth(secondMonth, "year");
		PurchaseOrderVO secondYoyVO = reportMapper.findPODataByMonth(secondYoyMonth);
		String thirdMonth = getLastMonth(secondMonth, "month");
		PurchaseOrderVO thirdVO = reportMapper.findPODataByMonth(thirdMonth);

		PurchaseOrder secondPO = calculPOInfo(secondMonth, secondVO, thirdVO, secondYoyVO);
		item = new ArrayList<>();
		item.add(secondMonth);
		item.add(secondPO.getPo_item_count());
		item.add(secondPO.getPo_count());
		item.add(secondPO.getPo_item_chain_ratio());
		item.add(secondPO.getPo_chain_ratio());
		item.add(secondPO.getPo_item_YOY_ratio());
		item.add(secondPO.getPo_YOY_ratio());
		result.add(item);

		String thirdYoyMonth = getLastMonth(thirdMonth, "year");
		PurchaseOrderVO thirdYoyVO = reportMapper.findPODataByMonth(thirdYoyMonth);
		String fourMonth = getLastMonth(thirdMonth, "month");
		PurchaseOrderVO fourVO = reportMapper.findPODataByMonth(fourMonth);

		PurchaseOrder thirdPO = calculPOInfo(thirdMonth, thirdVO, fourVO, thirdYoyVO);
		item = new ArrayList<>();
		item.add(thirdMonth);
		item.add(thirdPO.getPo_item_count());
		item.add(thirdPO.getPo_count());
		item.add(thirdPO.getPo_item_chain_ratio());
		item.add(thirdPO.getPo_chain_ratio());
		item.add(thirdPO.getPo_item_YOY_ratio());
		item.add(thirdPO.getPo_YOY_ratio());
		result.add(item);

		String fourYoyMonth = getLastMonth(fourMonth, "year");
		PurchaseOrderVO fourYoyVO = reportMapper.findPODataByMonth(fourYoyMonth);
		String fiveMonth = getLastMonth(fourMonth, "month");
		PurchaseOrderVO fiveVO = reportMapper.findPODataByMonth(fiveMonth);

		PurchaseOrder fourPO = calculPOInfo(fourMonth, fourVO, fiveVO, fourYoyVO);
		item = new ArrayList<>();
		item.add(fourMonth);
		item.add(fourPO.getPo_item_count());
		item.add(fourPO.getPo_count());
		item.add(fourPO.getPo_item_chain_ratio());
		item.add(fourPO.getPo_chain_ratio());
		item.add(fourPO.getPo_item_YOY_ratio());
		item.add(fourPO.getPo_YOY_ratio());
		result.add(item);

		String fiveYoyMonth = getLastMonth(fiveMonth, "year");
		PurchaseOrderVO fiveYoyVO = reportMapper.findPODataByMonth(fiveYoyMonth);
		String sixMonth = getLastMonth(fiveMonth, "month");
		PurchaseOrderVO sixVO = reportMapper.findPODataByMonth(sixMonth);

		PurchaseOrder fivePO = calculPOInfo(fiveMonth, fiveVO, sixVO, fiveYoyVO);
		item = new ArrayList<>();
		item.add(fiveMonth);
		item.add(fivePO.getPo_item_count());
		item.add(fivePO.getPo_count());
		item.add(fivePO.getPo_item_chain_ratio());
		item.add(fivePO.getPo_chain_ratio());
		item.add(fivePO.getPo_item_YOY_ratio());
		item.add(fivePO.getPo_YOY_ratio());
		result.add(item);

		String sixYoyMonth = getLastMonth(sixMonth, "year");
		PurchaseOrderVO sixYoyVO = reportMapper.findPODataByMonth(sixYoyMonth);
		String sevenMonth = getLastMonth(sixMonth, "month");
		PurchaseOrderVO sevenVO = reportMapper.findPODataByMonth(sevenMonth);
		PurchaseOrder sixPO = calculPOInfo(sixMonth, sixVO, sevenVO, sixYoyVO);
		item = new ArrayList<>();
		item.add(sixMonth);
		item.add(sixPO.getPo_item_count());
		item.add(sixPO.getPo_count());
		item.add(sixPO.getPo_item_chain_ratio());
		item.add(sixPO.getPo_chain_ratio());
		item.add(sixPO.getPo_item_YOY_ratio());
		item.add(sixPO.getPo_YOY_ratio());
		result.add(item);

		return result;
	}

	@Override
	public List<List<Object>> findProductMarketData(String dateStr) {
		List<List<Object>> list = new ArrayList<>();
		List<Object> item = new ArrayList<>();
		item.add("月份");

		item.add("生产交货计划明细同比");
		item.add("备货计划明细与排产计划同比");
		item.add("产能上报明细同比");

		item.add("生产交货计划明细环比");
		item.add("备货计划明细与排产计划环比");
		item.add("产能上报明细环比");

		item.add("生产交货计划明细");
		item.add("备货计划明细与排产计划");
		item.add("产能上报明细");
		list.add(item);

		// 第一个月
		String firstMonth = StringUtils.isEmpty(dateStr) ? getLastMonth(null, "month") : dateStr;
		ProductMarketVO firstVO = reportMapper.findProductMarkerByMonth(firstMonth);
		String firstYoyMonth = getLastMonth(firstMonth, "year");
		ProductMarketVO firstYoyVO = reportMapper.findProductMarkerByMonth(firstYoyMonth);
		String secondMonth = getLastMonth(firstMonth, "month");
		ProductMarketVO secondVO = reportMapper.findProductMarkerByMonth(secondMonth);

		ProductMarketReport firstPO = calculPMInfo(firstMonth, firstVO, secondVO, firstYoyVO);
		item = new ArrayList<>();
		item.add(firstMonth);

		item.add(firstPO.getPad_yoy_ratio());
		item.add(firstPO.getSip_yoy_ratio());
		item.add(firstPO.getPdr_yoy_ratio());

		item.add(firstPO.getPad_chain_ratio());
		item.add(firstPO.getSip_chain_ratio());
		item.add(firstPO.getPdr_chain_ratio());

		item.add(firstPO.getPad_count());
		item.add(firstPO.getSip_count());
		item.add(firstPO.getPdr_count());
		list.add(item);

		String secondYoyMonth = getLastMonth(secondMonth, "year");
		ProductMarketVO secondYoyVO = reportMapper.findProductMarkerByMonth(secondYoyMonth);
		String thirdMonth = getLastMonth(secondMonth, "month");
		ProductMarketVO thirdVO = reportMapper.findProductMarkerByMonth(thirdMonth);

		ProductMarketReport secondPO = calculPMInfo(secondMonth, secondVO, thirdVO, secondYoyVO);
		item = new ArrayList<>();
		item.add(secondMonth);

		item.add(secondPO.getPad_yoy_ratio());
		item.add(secondPO.getSip_yoy_ratio());
		item.add(secondPO.getPdr_yoy_ratio());

		item.add(secondPO.getPad_chain_ratio());
		item.add(secondPO.getSip_chain_ratio());
		item.add(secondPO.getPdr_chain_ratio());

		item.add(secondPO.getPad_count());
		item.add(secondPO.getSip_count());
		item.add(secondPO.getPdr_count());
		list.add(item);

		String thirdYoyMonth = getLastMonth(thirdMonth, "year");
		ProductMarketVO thirdYoyVO = reportMapper.findProductMarkerByMonth(thirdYoyMonth);
		String fourMonth = getLastMonth(thirdMonth, "month");
		ProductMarketVO fourVO = reportMapper.findProductMarkerByMonth(fourMonth);

		ProductMarketReport thirdPO = calculPMInfo(thirdMonth, thirdVO, fourVO, thirdYoyVO);
		item = new ArrayList<>();
		item.add(thirdMonth);

		item.add(thirdPO.getPad_yoy_ratio());
		item.add(thirdPO.getSip_yoy_ratio());
		item.add(thirdPO.getPdr_yoy_ratio());

		item.add(thirdPO.getPad_chain_ratio());
		item.add(thirdPO.getSip_chain_ratio());
		item.add(thirdPO.getPdr_chain_ratio());

		item.add(thirdPO.getPad_count());
		item.add(thirdPO.getSip_count());
		item.add(thirdPO.getPdr_count());
		list.add(item);

		String fourYoyMonth = getLastMonth(fourMonth, "year");
		ProductMarketVO fourYoyVO = reportMapper.findProductMarkerByMonth(fourYoyMonth);
		String fiveMonth = getLastMonth(fourMonth, "month");
		ProductMarketVO fiveVO = reportMapper.findProductMarkerByMonth(fiveMonth);

		ProductMarketReport fourPO = calculPMInfo(fourMonth, fourVO, fiveVO, fourYoyVO);
		item = new ArrayList<>();
		item.add(fourMonth);

		item.add(fourPO.getPad_yoy_ratio());
		item.add(fourPO.getSip_yoy_ratio());
		item.add(fourPO.getPdr_yoy_ratio());

		item.add(fourPO.getPad_chain_ratio());
		item.add(fourPO.getSip_chain_ratio());
		item.add(fourPO.getPdr_chain_ratio());

		item.add(fourPO.getPad_count());
		item.add(fourPO.getSip_count());
		item.add(fourPO.getPdr_count());
		list.add(item);

		String fiveYoyMonth = getLastMonth(fiveMonth, "year");
		ProductMarketVO fiveYoyVO = reportMapper.findProductMarkerByMonth(fiveYoyMonth);
		String sixMonth = getLastMonth(fiveMonth, "month");
		ProductMarketVO sixVO = reportMapper.findProductMarkerByMonth(sixMonth);

		ProductMarketReport fivePO = calculPMInfo(fiveMonth, fiveVO, sixVO, fiveYoyVO);
		item = new ArrayList<>();
		item.add(fiveMonth);

		item.add(fivePO.getPad_yoy_ratio());
		item.add(fivePO.getSip_yoy_ratio());
		item.add(fivePO.getPdr_yoy_ratio());

		item.add(fivePO.getPad_chain_ratio());
		item.add(fivePO.getSip_chain_ratio());
		item.add(fivePO.getPdr_chain_ratio());

		item.add(fivePO.getPad_count());
		item.add(fivePO.getSip_count());
		item.add(fivePO.getPdr_count());
		list.add(item);

		String sixYoyMonth = getLastMonth(sixMonth, "year");
		ProductMarketVO sixYoyVO = reportMapper.findProductMarkerByMonth(sixYoyMonth);
		String sevenMonth = getLastMonth(sixMonth, "month");
		ProductMarketVO sevenVO = reportMapper.findProductMarkerByMonth(sevenMonth);
		ProductMarketReport sixPO = calculPMInfo(sixMonth, sixVO, sevenVO, sixYoyVO);
		item = new ArrayList<>();
		item.add(sixMonth);

		item.add(sixPO.getPad_yoy_ratio());
		item.add(sixPO.getSip_yoy_ratio());
		item.add(sixPO.getPdr_yoy_ratio());

		item.add(sixPO.getPad_chain_ratio());
		item.add(sixPO.getSip_chain_ratio());
		item.add(sixPO.getPdr_chain_ratio());

		item.add(sixPO.getPad_count());
		item.add(sixPO.getSip_count());
		item.add(sixPO.getPdr_count());
		list.add(item);
		return list;
	}

	/**
	 * 计算获取 采购订单同比环比
	 * 
	 * @param currMonth
	 * @param lastMonth
	 * @param yoyMonth
	 * @return
	 */
	private PurchaseOrder calculPOInfo(String monthDate, PurchaseOrderVO currMonth, PurchaseOrderVO lastMonth,
			PurchaseOrderVO yoyMonth) {
		PurchaseOrder po = new PurchaseOrder();
		po.setMonth_date(monthDate);
		if (currMonth == null)
			return po;
		BigDecimal currPoCount = currMonth.getPoCount();
		BigDecimal currPoItemCount = currMonth.getPoItemCount();
		po.setPo_count(currPoCount.intValue());
		po.setPo_item_count(currPoItemCount.intValue());
		// 同比 (本期-同年本期)/同年本期
		if (yoyMonth != null) {
			BigDecimal yoyPoCount = yoyMonth.getPoCount();
			Float poFloat = calculateOthersPercent(yoyPoCount, currPoCount);
			po.setPo_YOY_ratio(poFloat);
			BigDecimal yoyPoItemCount = yoyMonth.getPoItemCount();
			Float poItemFloat = calculateOthersPercent(yoyPoItemCount, currPoItemCount);
			po.setPo_item_YOY_ratio(poItemFloat);
		}

		// 环比 (本期-上期)/上期
		if (lastMonth != null) {
			BigDecimal lastPoCount = lastMonth.getPoCount();
			Float poFloat1 = calculateOthersPercent(lastPoCount, currPoCount);
			po.setPo_chain_ratio(poFloat1);
			BigDecimal lastPoItemCount = lastMonth.getPoItemCount();
			Float poItemFloat1 = calculateOthersPercent(lastPoItemCount, currPoItemCount);
			po.setPo_item_chain_ratio(poItemFloat1);
		}
		return po;
	}

	/**
	 * 计算 产销管理的同比和环比
	 * 
	 * @param currMonth
	 * @param lastMonth
	 * @param yoyMonth
	 * @return
	 */
	private ProductMarketReport calculPMInfo(String monthDate, ProductMarketVO currMonth, ProductMarketVO lastMonth,
			ProductMarketVO yoyMonth) {
		ProductMarketReport pm = new ProductMarketReport();
		pm.setMonth_date(monthDate);
		if (currMonth == null)
			return pm;

		BigDecimal currPadCount = currMonth.getPad_count();
		BigDecimal currPdrCount = currMonth.getPdr_count();
		BigDecimal currSipCount = currMonth.getSip_count();
		pm.setPad_count(currPadCount == null ? null : currPadCount.intValue());
		pm.setPdr_count(currPdrCount == null ? null : currPdrCount.intValue());
		pm.setSip_count(currSipCount == null ? null : currSipCount.intValue());

		// 同比 (本期-同年本期)/同年本期
		if (yoyMonth != null) {
			BigDecimal yoyPadCount = yoyMonth.getPad_count();
			BigDecimal yoyPdrCount = yoyMonth.getPdr_count();
			BigDecimal yoySipCount = yoyMonth.getSip_count();
			Float padFloat = calculateOthersPercent(yoyPadCount, currPadCount);
			pm.setPad_yoy_ratio(padFloat);
			Float pdrFloat = calculateOthersPercent(yoyPdrCount, currPdrCount);
			pm.setPdr_yoy_ratio(pdrFloat);
			Float sipFloat = calculateOthersPercent(yoySipCount, currSipCount);
			pm.setSip_yoy_ratio(sipFloat);
		}

		// 环比 (本期-上期)/上期
		if (lastMonth != null) {
			BigDecimal lastPadCount = lastMonth.getPad_count();
			BigDecimal lastPdrCount = lastMonth.getPdr_count();
			BigDecimal lastSipCount = lastMonth.getSip_count();
			Float padFloat = calculateOthersPercent(lastPadCount, currPadCount);
			pm.setPad_chain_ratio(padFloat);
			Float pdrFloat = calculateOthersPercent(lastPdrCount, currPdrCount);
			pm.setPdr_chain_ratio(pdrFloat);
			Float sipFloat = calculateOthersPercent(lastSipCount, currSipCount);
			pm.setSip_chain_ratio(sipFloat);
		}
		return pm;
	}

	/**
	 * 得到上月的数据
	 * 
	 * @param dateStr
	 * @param type
	 * @return
	 */
	private String getLastMonth(String dateStr, String type) {
		Date date = null;
		if (StringUtils.isEmpty(dateStr)) {
			date = new Date();
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ("month".equals(type)) {
			// 上月的
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		} else {
			// 同年
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
		}
		Date time = calendar.getTime();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(time);
		return format;
	}

	/**
	 * 计算环比 订单，
	 * 
	 * @param last
	 * @param current
	 * @return
	 */
	private Float calculateOthersPercent(BigDecimal last, BigDecimal current) {
		// 公式（本月-上月)/上月*100%，-1表示小于，0是等于，1是大于
		if (last == null || current == null) {
			return null;
		}
		int i = last.compareTo(BigDecimal.ZERO);
		int j = current.compareTo(BigDecimal.ZERO);
		if (i == -1 || i == 0 || j == -1 || j == 0)
			return null;
		BigDecimal subtract = current.subtract(last);
		BigDecimal divide = subtract.divide(last, 4, BigDecimal.ROUND_CEILING);
		BigDecimal result = divide.multiply(new BigDecimal(100));
		result = result.setScale(2);
		return result.floatValue();
	}

	@Override
	public PageUtils<LoginOperateVO> findLoginList(String startDay, String endDay, String user_type, String user_dept,
			String username) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("startDay", startDay);
		paramsMap.put("endDay", endDay);
		paramsMap.put("user_type", user_type);
		paramsMap.put("user_dept", user_dept);
		paramsMap.put("username", username);
		Map<String, Object> queryParams = queryParams(paramsMap);
		@SuppressWarnings("unchecked")
		List<UserVO> userList = (List<UserVO>) queryParams.get("userList");
		if (userList.size() == 0)
			return new PageUtils<>(null, 0, "", "0");
		paramsMap.put("userIds", userList);
		List<LoginOperateVO> list = reportMapper.findLoginList(paramsMap);
		for (LoginOperateVO lov : list) {
			Long userId = lov.getUserId();
			String object = (String) queryParams.get(userId.toString());
			if (!StringUtils.isEmpty(object))
				lov.setUserDept(object);
		}
		PageUtils<LoginOperateVO> pageUtils = new PageUtils<>(list, list.size(), "", "0");
		return pageUtils;
	}

	public Map<String, Object> queryParams(Map<String, Object> map) {
		List<UserVO> list = orgMapper.findUserIdsByParams(map);
		Map<String, Object> result = new HashMap<>();
		if (list != null && list.size() > 0) {
			for (UserVO userVO : list) {
				Integer userId = userVO.getUserId();
				String sfname = userVO.getSfname();
				if (!StringUtils.isEmpty(sfname)) {
					String[] split = sfname.split("/");
					for (String s : split) {
						if (s.indexOf(".dept") > 0) {
							String r = s.replace(".dept", "");
							userVO.setSfname(r);
							result.put(userId.toString(), r);
						}
					}
				}
			}
		} else {
			list = new ArrayList<>();
		}
		result.put("userList", list);
		return result;
	}

	@Override
	public PageUtils<LoginOperateVO> findLoginDetails(String startDay, String endDay, String username, Long userId) {
		Map<String, String> params = new HashMap<>();
		params.put("startDay", startDay);
		params.put("endDay", endDay);
		params.put("username", username);
		List<LoginOperateVO> list = reportMapper.findLoginDetails(params);

		OrgDo orgDo = orgMapper.findOrgByUserId(userId);
		if (orgDo != null) {
			String sfname = orgDo.getSfname();
			String dept = "";
			if (!StringUtils.isEmpty(sfname)) {
				String[] split = sfname.split("/");
				for (String str : split) {
					if (str.indexOf(".dept") > 0)
						dept = str.replace(".dept", "");
				}
			}
			if (!StringUtils.isEmpty(dept)) {
				for (LoginOperateVO lov : list) {
					lov.setUserDept(dept);
				}
			}
		}
		return new PageUtils<>(list, list.size(), "", "0");
	}

}
