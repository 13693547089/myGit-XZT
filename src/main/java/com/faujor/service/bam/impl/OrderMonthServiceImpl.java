package com.faujor.service.bam.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.OrderMonthMapper;
import com.faujor.service.bam.OrderMonthService;

@Service
public class OrderMonthServiceImpl implements OrderMonthService {
	@Autowired
	private OrderMonthMapper orderMonthMapper;

	@Override
	public Double selectUndeliveredNumByMap(Map<String, Object> map) {
		return orderMonthMapper.selectUndeliveredNumByMap(map);
	}

	@Override
	public int ScheduledAsybcMonthOrder() {
		int i = orderMonthMapper.ScheduledAsybcMonthOrder();
		return i;
	}

}
