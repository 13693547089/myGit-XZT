package com.faujor.web.sync;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.service.sync.SyncService;
import com.faujor.utils.DateUtils;

/**
 * 定时同步任务操作类
 * @author Vincent
 *
 */
//@Configuration
//@EnableScheduling
public class syncController {

	@Autowired
	private SyncService syncService;
	
	//@Scheduled(fixedRate = 5000) 每5秒执行一次
	/**
	 * 定时同步中间库T_ORA_CXJH表数据
	 */
	@Scheduled(cron = "0 00 23 * * ?") // 每天23:00同步
    public void syncCxjhData() {
		String currTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
		
		String[] arrTime = currTime.split("-");
		// 获取T_ORA_CXJH表数据
		Map<String,Object> map = new HashMap<String,Object>();
		
		// 保存T_ORA_CXJH表数据至本地库中
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("year", "");
		parmMap.put("month", "");
		
		int sumCount = syncService.getCxjhMatCount(null);
		if(sumCount>0){
			// 存在已同步的数据情况下，加上本月的条件，否则同步所有数据
			map.put("year", arrTime[0]);
			map.put("month", arrTime[1]);
			
			// 保存同步数据情况下
			parmMap.put("year", arrTime[0]);
			parmMap.put("month", arrTime[1]);
		}
		List<OraCxjhEntity> syncList = syncService.getMatSyncInfoByCondition(map);
		
		parmMap.put("list",syncList);
		parmMap.put("crtTime",currTime);
		
		//syncService.saveCxjhMatList(parmMap);
    }
	
	/**
	 * 每月最后一日同步生产交货计划报表(进销存报表)至FTP文件服务器
	 */
	@Scheduled(cron = "0 30 0 1 * ?") // 每月一号00:30同步
    public void syncPadPlanReport() {
		syncService.syncPadPlanReport();
	}
}
