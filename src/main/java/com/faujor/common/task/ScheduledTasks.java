//package com.faujor.common.task;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import com.faujor.common.constant.GlobalConstant;
//import com.faujor.dao.master.common.InterfaceLogMapper;
//import com.faujor.entity.common.AsyncLog;
//import com.faujor.entity.common.InterfaceLogMain;
//import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
//import com.faujor.service.bam.OrderMonthService;
//import com.faujor.service.bam.OrderService;
//import com.faujor.service.bam.ReceiveService;
//import com.faujor.service.common.AsyncLogService;
//import com.faujor.service.mdm.MaterialService;
//import com.faujor.service.mdm.QualSuppService;
//import com.faujor.service.sync.SyncService;
//import com.faujor.utils.DateUtils;
//import com.faujor.utils.UUIDUtil;
//
//@Configuration
//public class ScheduledTasks {
//
//	@Autowired
//	private InterfaceLogMapper logMapper;
//	@Autowired
//	private AsyncLogService asyncLogService;
//	@Autowired
//	private RestTemplate restTemplate;
//	/**
//	 * 合格供应商主数据同步定时任务
//	 * 
//	 * 每天12：00
//	 */
//	@Autowired
//	private QualSuppService suppService;
//	/**
//	 * 每天定时调用OA接口地址
//	 */
//	@Scheduled(cron = "0 0 0 * * ?")
//	public void invokeWsdl() {
//		InterfaceLogMain log=new InterfaceLogMain();
//		log.setId(UUIDUtil.getUUID());
//		log.setInterfaceNum("OA定时调用");
//		log.setInterfaceDesc("OA定时调用");
//		log.setInvoker("admin");
//		log.setInvokeTime(new Date());
//		
//		try {
//			restTemplate.getForObject(GlobalConstant.OA_WSDL, String.class);
//			log.setStatus("S");
//			log.setMessage("接口调用成功");
//		} catch (RestClientException e) {
//			e.printStackTrace();
//			log.setOutJson("");
//			log.setStatus("E");;
//			log.setMessage("接口调用失败");
//		}finally {
//			logMapper.saveLog(log);
//		}
//	}
//
//	@Scheduled(cron = "0 0 12 * * ?")
//	public void asyncSuppData() {
//		AsyncLog al = saveAsyncLog("合格供应商主数据同步");
//		suppService.asyncSuppInfo(al);
//	}
//
//	/**
//	 * 物料主数据同步定时任务
//	 * 
//	 * 每天中午12：40、17：40
//	 */
//	@Autowired
//	private MaterialService mateService;
//
//	@Scheduled(cron = "0 40 12 * * ?")
//	public void asyncMateData() {
//		AsyncLog al = saveAsyncLog("物料主数据同步");
//		mateService.asyncMateInfo(null, al);
//	}
//
//	@Scheduled(cron = "0 40 17 * * ?")
//	public void asyncMateData1() {
//		AsyncLog al = saveAsyncLog("物料主数据同步");
//		mateService.asyncMateInfo(null, al);
//	}
//
//	/**
//	 * 采购订单同步定时任务
//	 * 
//	 * 开始时间为凌晨1:40
//	 */
//	@Autowired
//	private OrderService orderService;
//
//	@Scheduled(cron = "0 40 01 * * ?")
//	public void asyncPurchasingOrder() {
//		AsyncLog al = saveAsyncLog("采购订单数据同步");
//		orderService.asyncPurchaseOrderSchedule(al);
//	}
//
//	/**
//	 * 移除占用标识
//	 * 
//	 * 开始时间为凌晨2:20
//	 */
//	@Autowired
//	private ReceiveService receiveService;
//
//	@Scheduled(cron = "0 20 02 * * ?")
//	public void asyncIdenToReceive() {
//		// 更新内向交货单信息
//		AsyncLog al = saveAsyncLog("移除收货单的占用标识");
//		receiveService.idenToReceive(al);
//	}
//
//	/**
//	 * 每月的月初定时保存采购订单未交量不为0的数据
//	 * 
//	 */
//	@Autowired
//	private OrderMonthService monthService;
//
//	// 每月1号凌晨6点
//	@Scheduled(cron = "00 00 06 01 * ?")
//	public void asybcMonthOrder() {
//		// 更新内向交货单信息
//		AsyncLog al = saveAsyncLog("月初自动保存采购订单");
//		int i = monthService.ScheduledAsybcMonthOrder();
//		al.setAsyncNum(i);
//		asyncLogService.updateAsyncLog(al);
//	}
//
//	@Autowired
//	private SyncService syncService;
//
//	/**
//	 * 定时同步中间库T_ORA_CXJH表数据
//	 */
//	@Scheduled(cron = "0 00 23 * * ?") // 每天23:00同步
//	public void syncCxjhData() {
//		// 同步开始时间
//		// String syncStartTime = DateUtils.format(new
//		// Date(),DateUtils.DATE_TIME_PATTERN);
//		Date syncStartTime = new Date();
//
//		String currTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
//
//		String[] arrTime = currTime.split("-");
//		// 获取T_ORA_CXJH表数据
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		// 保存T_ORA_CXJH表数据至本地库中
//		Map<String, Object> parmMap = new HashMap<String, Object>();
//		parmMap.put("year", "");
//		parmMap.put("month", "");
//
//		int sumCount = syncService.getCxjhMatCount(null);
//		if (sumCount > 0) {
//			// 存在已同步的数据情况下，加上本月的条件，否则同步所有数据
//			map.put("year", arrTime[0]);
//			map.put("month", arrTime[1]);
//
//			// 保存同步数据情况下
//			parmMap.put("year", arrTime[0]);
//			parmMap.put("month", arrTime[1]);
//		}
//		List<OraCxjhEntity> syncList = syncService.getMatSyncInfoByCondition(map);
//
//		parmMap.put("list", syncList);
//		parmMap.put("crtTime", currTime);
//		parmMap.put("isChecked", "0");
//
//		try {
//			syncService.saveCxjhMatList(parmMap);
//			// 同步结束时间
//			// String syncEndTime = DateUtils.format(new
//			// Date(),DateUtils.DATE_TIME_PATTERN);
//			Date syncEndTime = new Date();
//			// 同步数量
//			int size = syncList.size();
//
//			AsyncLog al = new AsyncLog();
//			String alId = UUIDUtil.getUUID();
//			al.setId(alId);
//			al.setStartDate(syncStartTime);
//			al.setEndDate(syncEndTime);
//			al.setAsyncName("T_ORA_CXJH表数据同步");
//			al.setAsyncUserName("admin_auto");
//			al.setAsyncStatus("同步成功");
//			al.setAsyncNum(size);
//			asyncLogService.saveAsyncLog2(al);
//
//		} catch (Exception e) {
//			Date syncEndTime = new Date();
//			AsyncLog al = new AsyncLog();
//			String alId = UUIDUtil.getUUID();
//			al.setId(alId);
//			al.setStartDate(syncStartTime);
//			al.setEndDate(syncEndTime);
//			al.setAsyncName("T_ORA_CXJH表数据同步");
//			al.setAsyncUserName("admin_auto");
//			al.setAsyncStatus("同步失败");
//			al.setErrorMsg(e.getMessage());
//			asyncLogService.saveAsyncLog2(al);
//		}
//	}
//
//	/**
//	 * 每月最后一日同步生产交货计划报表(进销存报表)至FTP文件服务器
//	 */
//	@Scheduled(cron = "0 30 03 1 * ?") // 每月一号03:30同步
//	public void syncPadPlanReport() {
//		syncService.syncPadPlanReport();
//	}
//
//	// 插入
//	public AsyncLog saveAsyncLog(String asyncName) {
//		// 插入同步记录
//		AsyncLog al = new AsyncLog();
//		String alId = UUIDUtil.getUUID();
//		al.setId(alId);
//		al.setAsyncName(asyncName);
//		al.setAsyncUserName("admin_auto");
//		asyncLogService.saveAsyncLog(al);
//		return al;
//	}
//	
//	/**
//	 * 同步产能上报信息
//	 */
//	@Scheduled(cron = "0 30 22 * * ?") // 每天晚10:30触发
//	public void syncPdrInfo() {
//		// 同步产能上报
//		syncService.syncPdrInfo();
//		
//		// 同步生产交货计划
//		syncService.syncPadPlanInfo();
//	}
//}
