package com.faujor.service.bam.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.bam.StraMessageMapper;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.StraMessDO;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.bam.StraMessageService;
import com.faujor.service.mdm.QualSuppService;

@Service(value = "straMessageService")
public class StraMessageServiceImpl implements StraMessageService {

	@Autowired
	private StraMessageMapper straMessageMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderMateCheckService orderMateCheckService;
	@Autowired
	private QualSuppService qualSuppService;

	@Override
	public Map<String, Object> queryStraMessageByPage(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String, Object>();
		List<StraMessDO> list = straMessageMapper.queryStraMessageByPage(map);
		int count = straMessageMapper.queryStraMessageByPageCount(map);
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean addStraMessage(StraMessage straMess, List<MessMate> messMate) {
		int i = straMessageMapper.addStraMessage(straMess);
		int count = 0;
		for (MessMate m : messMate) {
			int j = 0;
			m.setMessId(straMess.getMessId());
			j = straMessageMapper.addMessMate(m);
			count += j;
		}
		if (i == 1 && count == messMate.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteStraMessByMessId(String[] messIds) {
		straMessageMapper.deleteMessMateByMessId(messIds);
		int j = straMessageMapper.deleteStraMessByMessId(messIds);
		if (j == messIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public StraMessage queryStraMessageByMessId(String messId) {
		StraMessage straMessage = straMessageMapper.queryStraMessageByMessId(messId);
		return straMessage;
	}

	@Override
	@Transactional
	public boolean updateStraMessageByMessId(StraMessage straMess, List<MessMate> messMate) {
		int i = straMessageMapper.updateStraMessageByMessId(straMess);
		String[] messIds = new String[1];
		messIds[0] = straMess.getMessId();
		int j = straMessageMapper.deleteMessMateByMessId(messIds);
		int count = 0;
		for (MessMate m : messMate) {
			int k = 0;
			m.setMessId(straMess.getMessId());
			k = straMessageMapper.addMessMate(m);
			count += k;
		}
		if (i == 1 && count == messMate.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateStraMessStatusByMessId(Map<String, Object> map) {
		int i = straMessageMapper.updateStraMessStatusByMessId(map);
		int size = (int) map.get("size");
		if (size == i) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> queryStraMessageBymessCode(String mapgCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		StraMessage straMess = straMessageMapper.queryStraMessageBymessCode(mapgCode);
		if (straMess != null) {
			QualSupp supp = qualSuppService.queryQualSuppBySapId(straMess.getZzoem());
			straMess.setSuppName(supp.getSuppName());
			List<MessMate> list = straMess.getMessMates();
			List<DeliMate> deliMates = new ArrayList<DeliMate>();
			for (MessMate m : list) {
				DeliMate dm = new DeliMate();
				dm.setOrderId(m.getPoId());// 采购订单
				dm.setFrequency(m.getSemiFrequency());// 项次
				dm.setMateCode(m.getSemiMateCode());// 半成品物料编码
				dm.setMateName(m.getSemiMateName());// 半成品物料名称
				dm.setAppoNumber(m.getMateNumber());// 预约数量
				dm.setCalculNumber(m.getCalculNumber());// 计算未交量
				dm.setDeliNumber(m.getSemiMateNumber());// 送货量
				dm.setUnit(m.getSemiUnit());// 半成品单位
				dm.setUnpaNumber(m.getUnpaNumber());// 订单未交量
				deliMates.add(dm);
			}
			map.put("deliMates", deliMates);
			map.put("straMess", straMess);
			map.put("judge", true);
		} else {
			map.put("judge", false);
		}
		return map;
	}

	@Override
	@Transactional
	public boolean updateMessStatusByMessCode(Map<String, Object> map) {
		int i = straMessageMapper.updateMessStatusByMessCode(map);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<StraMessage> queryAllNotifiedStraMessage(Map<String, Object> map) {
		return straMessageMapper.queryAllNotifiedStraMessage(map);
	}

	@Override
	public StraMessage queryOneStraMessageByMessCode(String mapgCode) {
		return straMessageMapper.queryOneStraMessageByMessCode(mapgCode);
	}

	@Override
	@Transactional
	public boolean cancellStraMessByMessId(Map<String, Object> map) {
		int i = straMessageMapper.cancellStraMessByMessId(map);
		int size = (int) map.get("size");
		if (size == i) {
			map.put("alloStatus", null);
			orderMapper.updateAlloOrderStatus(map);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getStraMates(MessMate mm) {
		// 调拨单号
		String alloNo = mm.getOrderId();
		OrderRele order = new OrderRele();
		order.setContOrdeNumb(alloNo);
		// List<OrderMate> list =
		// orderMapper.queryOrderMateByContOrdeNumb(alloNo);
		// 根据调拨单号，获取到调拨单，成品物料，并在SQL中转换成半成品
		List<MessMate> list = straMessageMapper.findMessMateByAlloNO(alloNo);

		List<String> mates = new ArrayList<String>();
		Map<String, Object> result = new HashMap<String, Object>();
		String zzoem = "";
		for (MessMate mate : list) {
			zzoem = mate.getZzoem();
			mates.add(mate.getSemiMateCode());
		}
		result = orderMateCheckService.checkOutAppoMate("s" + zzoem, mates, mm.getMessId());
		boolean judge = (boolean) result.get("judge");
		if (judge) {
			result = orderMateCheckService.recommendPurchaseOrderForStraMates(result, list,mm.getSuppRange());
		}
		return result;
	}

	@Override
	@Transactional
	public boolean updateDeliDate(StraMessage straMess, String type) {
		if ("1".equals(type)) {// 需要修改直发通知的到货日期
			// boolean c = deliMapper.updateDeliDate2(straMess);
			boolean b = straMessageMapper.updateDeliDate(straMess);
			return b;
		} else {
			// 需要修改直发通知的到货日期
			boolean b = straMessageMapper.updateDeliDate(straMess);
			return b;
		}
	}

	@Override
	@Transactional
	public int updateAlloOrderStatus(String alloNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alloStatus", "allo");
		map.put("alloNo", alloNo);
		return orderMapper.updateAlloOrderStatus(map);
	}

}
