package com.faujor.service.rm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.rm.AppoDeliMapper;
import com.faujor.entity.rm.AppoDeli;
import com.faujor.service.rm.AppoDeliService;
import com.faujor.utils.DateUtils;
@Service(value = "appoDeliService")
public class AppoDeliServiceImpl implements AppoDeliService {

	@Autowired
	private AppoDeliMapper appoDeliMapper;
	
	@Override
	public Map<String, Object> queryAppoDeliByPage(Map<String, Object> map) {
		List<AppoDeli> list = appoDeliMapper.queryAppoDeliByPage(map);
		int count = appoDeliMapper.queryAppoDeliByPageCount(map);
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	
	public AppoDeli getCreateDate(Date stratCreate, Date endCreate) {
		AppoDeli appoDeli = new AppoDeli();
		long appoStartTime = stratCreate.getTime();
		long appoEndTime = endCreate.getTime();
		if(appoStartTime<=appoEndTime){
			String appoformat = DateUtils.format(stratCreate, "yyyy-MM-dd");
			String appoformat2 = DateUtils.format(endCreate, "yyyy-MM-dd");
			Date appoparse = DateUtils.parse(appoformat+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date appoparse2 = DateUtils.parse(appoformat2+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			appoDeli.setAppoCreStartDate(appoparse);
			appoDeli.setAppoCreEndDate(appoparse2);
		}else if(appoStartTime >appoEndTime){
			String appoformat = DateUtils.format(stratCreate, "yyyy-MM-dd");
			String appoformat2 = DateUtils.format(endCreate, "yyyy-MM-dd");
			Date appoparse2 = DateUtils.parse(appoformat2+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date appoparse = DateUtils.parse(appoformat+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			appoDeli.setAppoCreStartDate(appoparse2);
			appoDeli.setAppoCreEndDate(appoparse);
		}
		return appoDeli;
	}

	@Override
	public List<AppoDeli> queryAppoDeliListByMap(Map<String, Object> map) {
		return	appoDeliMapper.queryAppoDeliListByMap(map);
	}

	@Override
	public AppoDeli proceTime(AppoDeli appoDeli) {
		if(appoDeli.getAppoCreStartDate() != null && appoDeli.getAppoCreEndDate() != null){
			AppoDeli a1 = getCreateDate(appoDeli.getAppoCreStartDate(), appoDeli.getAppoCreEndDate());
			appoDeli.setAppoCreStartDate(a1.getAppoCreStartDate());
			appoDeli.setAppoCreEndDate(a1.getAppoCreEndDate());
		}
		if(appoDeli.getDeliCreStartDate() != null && appoDeli.getDeliCreEndDate() != null){
			AppoDeli a2 = getCreateDate(appoDeli.getDeliCreStartDate(),appoDeli.getDeliCreEndDate());
			appoDeli.setDeliCreStartDate(a2.getAppoCreStartDate());
			appoDeli.setDeliCreEndDate(a2.getAppoCreEndDate());
		}
		if(appoDeli.getReceCreStartDate() != null && appoDeli.getReceCreEndDate() != null){
			AppoDeli a3 = getCreateDate(appoDeli.getReceCreStartDate(),appoDeli.getReceCreEndDate());
			appoDeli.setReceCreStartDate(a3.getAppoCreStartDate());
			appoDeli.setReceCreEndDate(a3.getAppoCreEndDate());
		}
		if(appoDeli.getAppoStartDate() != null && appoDeli.getAppoEndDate() != null){
			AppoDeli a2 = transDate(appoDeli.getAppoStartDate(),appoDeli.getAppoEndDate());
			appoDeli.setAppoStartDate(a2.getAppoStartDate());
			appoDeli.setAppoEndDate(a2.getAppoEndDate());
		}
		if(appoDeli.getDeliStartDate() != null && appoDeli.getDeliEndDate() != null){
			AppoDeli a2 = transDate(appoDeli.getDeliStartDate(),appoDeli.getDeliEndDate());
			appoDeli.setDeliStartDate(a2.getAppoStartDate());
			appoDeli.setDeliEndDate(a2.getAppoEndDate());
		}
		if(appoDeli.getReceStartDate() != null && appoDeli.getReceEndDate() != null){
			AppoDeli a2 = transDate(appoDeli.getReceStartDate(),appoDeli.getReceEndDate());
			appoDeli.setReceStartDate(a2.getAppoStartDate());
			appoDeli.setReceEndDate(a2.getAppoEndDate());
		}
		return appoDeli;
	}
	
	/**
	 * 转换时间格式
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public AppoDeli transDate(Date startDate,Date endDate){
		AppoDeli appoDeli = new AppoDeli();
		String format = DateUtils.format(startDate, "yyyy-MM-dd");
		String format2 = DateUtils.format(endDate, "yyyy-MM-dd");
		Date parse = DateUtils.parse(format, "yyyy-MM-dd");
		Date parse2= DateUtils.parse(format2, "yyyy-MM-dd");
		appoDeli.setAppoStartDate(parse);
		appoDeli.setAppoEndDate(parse2);
		return appoDeli;
	}
	

}
