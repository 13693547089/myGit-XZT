package com.faujor.web.sync;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.sync.SyncService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;

/**
 * 生产/交货计划 控制类
 * @author Vincent
 *
 */
@Controller
@RequestMapping(value = "/sync")
public class SyncDealController {
	
	@Autowired
	private SyncService syncService;
	
	@Autowired
	private AsyncLogService asyncLogService;
	/**
	 * 同步操作 界面
	 * @param model
	 * @return
	 */
	@RequestMapping("/syncPage")
	public String syncPage(Model model){
		String currYm = DateUtils.format(new Date(), DateUtils.DATE_YM_PATTERN);
		model.addAttribute("month",currYm);
		
		return "sync/syncPage";
	}
	
	/**
	 * 同步某月的生产交货物料数据
	 * @param padMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/syncPadMaterial")
	public RestCode getPadPlanPageList(String padMonth,String isChecked){
		
		// 同步开始时间
		//String syncStartTime = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
		Date syncStartTime = new Date();
		
		String currTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);

		String[] arrTime = padMonth.split("-");
		// 获取T_ORA_CXJH表数据
		Map<String, Object> map = new HashMap<String, Object>();

		// 保存T_ORA_CXJH表数据至本地库中
		Map<String, Object> parmMap = new HashMap<String, Object>();
		//parmMap.put("year", "");
		//parmMap.put("month", "");

		//int sumCount = syncService.getCxjhMatCount(null);
		//if (sumCount > 0) {
			// 存在已同步的数据情况下，加上本月的条件，否则同步所有数据
			map.put("year", arrTime[0]);
			map.put("month", arrTime[1]);
			
			// 保存同步数据情况下
			parmMap.put("year", arrTime[0]);
			parmMap.put("month", arrTime[1]);
		///}
		List<OraCxjhEntity> syncList = syncService.getMatSyncInfoByCondition(map);

		parmMap.put("list", syncList);
		parmMap.put("crtTime", currTime);
		parmMap.put("isChecked", isChecked);

		try {
			syncService.saveCxjhMatList(parmMap);
			
			// 同步结束时间
			//String syncEndTime = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
			Date syncEndTime = new Date();
			// 同步数量
			int size = syncList.size();
			
			AsyncLog al = new AsyncLog();
			String alId = UUIDUtil.getUUID();
			al.setId(alId);
			al.setStartDate(syncStartTime);
			al.setEndDate(syncEndTime);
			al.setAsyncName("T_ORA_CXJH表数据同步");
			al.setAsyncStatus("同步成功");
			al.setAsyncNum(size);
			asyncLogService.saveAsyncLog2(al);
		} catch (Exception e) {
			Date syncEndTime = new Date();
			AsyncLog al = new AsyncLog();
			String alId = UUIDUtil.getUUID();
			al.setId(alId);
			al.setStartDate(syncStartTime);
			al.setEndDate(syncEndTime);
			al.setAsyncName("T_ORA_CXJH表数据同步");
			al.setAsyncStatus("同步失败");
			al.setErrorMsg(e.getMessage());
			asyncLogService.saveAsyncLog2(al);
			
			return RestCode.ok(-1,"同步失败，请查看【T_ORA_CXJH表数据同步】日志");
		}

		return RestCode.ok(0,"同步成功");
	}
}
