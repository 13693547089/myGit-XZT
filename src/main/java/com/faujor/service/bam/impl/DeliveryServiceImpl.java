package com.faujor.service.bam.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.AppointMapper;
import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.bam.StraMessageMapper;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.StraMessAndMateDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.bam.StraMessageService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Service(value = "deliveryService")
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private DeliveryMapper deliveryMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private OrderMateCheckService orderMateCheckService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private StraMessageService straMessageService;
	@Autowired
	private StraMessageMapper straMessageMapper;
	@Autowired
	private AppointMapper appointMapper;

	@Override
	public Map<String, Object> queryDeliveryByPage(Map<String, Object> map) {
		List<Delivery> list = deliveryMapper.queryDeliveryByPage(map);
		int count = deliveryMapper.queryDeliveryByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean deleteDeliveryBydeliId(String[] deliIds) {
		deliveryMapper.deleteDeliMateBydeliId(deliIds);
		int i = deliveryMapper.deleteDeliveryBydeliId(deliIds);
		if (i == deliIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addDelivery(Delivery deli, List<DeliMate> list) {
		int i = deliveryMapper.insertDelivery(deli);
		int count = 0;
		for (DeliMate dm : list) {
			int j = 0;
			dm.setDeliId(deli.getDeliId());
			j = deliveryMapper.insertDeliMate(dm);
			count += j;
		}
		if (i == 1 && count == list.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Delivery queryDeliveryByDeliId(String deliId) {
		Delivery deli = deliveryMapper.queryDeliveryByDeliId(deliId);
		return deli;
	}

	@Override
	public List<DeliMate> queryDeliMateByDeliId(String deliId) {
		List<DeliMate> list = deliveryMapper.queryDeliMateByDeliId(deliId);
		return list;
	}

	@Override
	@Transactional
	public boolean updateDeliveryByDeliId(Delivery deli, List<DeliMate> deliMate) {
		int i = deliveryMapper.updateDeliveryByDeliId(deli);
		String[] deliIds = new String[1];
		deliIds[0] = deli.getDeliId();
		deliveryMapper.deleteDeliMateBydeliId(deliIds);
		int count = 0;
		for (DeliMate d : deliMate) {
			int k = 0;
			d.setDeliId(deli.getDeliId());
			k = deliveryMapper.insertDeliMate(d);
			count += k;
		}
		if (i == 1 && count == deliMate.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateDeliStatusByDeliCode(Map<String, Object> map) {
		int i = deliveryMapper.updateDeliStatusByDeliCode(map);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> judgeUpdateMateUnpaNumber(String suppId, String mateCode, String orderId, int unpa) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> statusList = new ArrayList<String>();
		statusList.add("已发货");
		statusList.add("已签到");
		statusList.add("待收货");
		map.put("statusList", statusList);
		map.put("suppId", suppId);
		map.put("mateCode", mateCode);
		map.put("orderId", orderId);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String str = format.format(date);
		String staDate = str + " 00:00:00";
		String endDate = str + " 23:59:59";
		map.put("staDate", staDate);
		map.put("endDate", endDate);
		List<DeliMate> deliMates = deliveryMapper.queryDeliMateBySuppIdAndMateCode(map);
		int size = deliMates.size();
		if (size > 0) {// 有数据，则未交数量需要改变
			// 这个供应商的这个物料在送货单（状态为：已发货，已签到，待收货）中已经有了
			int count = 0;
			for (DeliMate dm : deliMates) {
				count += dm.getDeliNumber();
			}
			result.put("count", count);
			// result.put("result", "可以新建送货单");
			// result.put("msg", "可以去查询采购订单，不过采购订单的未交数量需要改变");
		} else {// 无，则未交数量不需要改变，以从采购订单表中查询的为准
				// result.put("result", "可以新建送货单");
				// result.put("msg", "可以去查询采购订单，以从采购订单表中查询的为准");
			result.put("count", 0);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryOrderBySuppIdAndMateCode(String suppId, String mateCode, Integer num,
			boolean oldCodeJudge,String orderId,String suppRange) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		if (supp == null) {
			map.put("msg", "未查询到合格供应商");
			map.put("judge", false);
			return map;
		}
		String sapId = supp.getSapId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sapId", sapId);
		param.put("mateCode", mateCode);
		param.put("suppRange", suppRange);//供应商子范围编码
		param.put("orderId", orderId);
		OrderRele orderRele = null;
		int orderNum = deliveryMapper.queryNumOfOrderId(param);
		param.put("orderNum", orderNum+1);
		if (oldCodeJudge) {//编辑下的添加
			orderRele = orderMapper.querycontOrdeNumbOfOrderReleBySapIdAndMateCode(param);
		} else {//新建下的添加
			orderRele = orderMapper.queryOrderReleBySapIdAndMateCode(param);
		}
		if (orderRele == null) {
			map.put("msg", supp.getSuppName() + "的物料：" + mateCode + "未查询到采购订单");
			map.put("judge", false);
			return map;
		}
		//List<OrderMate> mates = orderMapper.findOrderMateByOrderNo(orderRele.getContOrdeNumb());
		List<OrderMate> mates = orderRele.getMates();
		if (mates.size() <= 0) {
			map.put("msg", supp.getSuppName() + "的采购订单 " + orderRele.getContOrdeNumb() + ",没有物料 " + mateCode);
			map.put("judge", false);
			return map;
		}
		// 获取采购订单号
		String contOrdeNumb = orderRele.getContOrdeNumb();
		// 采购订单上的未交数量
		OrderMate orderMate = mates.get(0);
		Double orderNumber = orderMate.getUnpaQuan();
		DeliMate deliMate = new DeliMate();
		StraMessAndMateDO smamd = orderMateCheckService.calculateActualOrderNumber(contOrdeNumb, mateCode, suppRange,orderNumber);
		double calculNumber = smamd.getCalculNumber();
		if (calculNumber == 0) {
			map.put("judge", false);
			map.put("msg", "未找到符合条件的采购订单");
			return map;
		}
		deliMate.setOrderId(smamd.getOrderNo());
		deliMate.setCalculNumber(smamd.getCalculNumber());
		deliMate.setUnpaNumber(smamd.getUnpaNumber());
		deliMate.setMateName(orderMate.getProdName());
		deliMate.setUnit(orderMate.getCompany());
		deliMate.setFrequency(orderMate.getFrequency());
		deliMate.setSubeDate(orderRele.getSubeDate());
		deliMate.setAppoNumber(0D);
		deliMate.setDeliNumber(0D);
		map.put("judge", true);
		map.put("deliMate", deliMate);
		return map;
	}

	@Override
	@Transactional
	public boolean addDeliveryTwo(Delivery deli, String deliMateData, String type) {
		SysUserDO user = UserCommon.getUser();
		List<DeliMate> list = JsonUtils.jsonToList(deliMateData, DeliMate.class);
		String deliCode = codeService.getCodeByCodeType("deliveryNo");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messCode", deli.getMapgCode());
		/*
		 * String str=""; try { str = MatrixToImageWriter.getQRCode(deliCode); }
		 * catch (Exception e) { e.printStackTrace(); } deli.setQrurl(str);
		 */
		deli.setDeliCode(deliCode);
		deli.setCreateId(user.getUserId().toString());
		deli.setCreator(user.getName());
		if ("add".equals(type)) {
			deli.setStatus("已保存");
			map.put("messStatus", "待发货");
		} else if ("sub".equals(type)) {
			deli.setStatus("已发货");
			map.put("messStatus", "已发货");
		}
		boolean b = addDelivery(deli, list);
		boolean c = straMessageService.updateMessStatusByMessCode(map);
		return b && c;
	}

	@Override
	public List<OrderDO> queryAllOrderOfMate(QualSupp supp, String mateCode,String suppRange) {
		// 查询所有采购订单
		List<OrderDO> list = new ArrayList<OrderDO>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sapId", supp.getSapId());
		param.put("mateCode", mateCode);
		param.put("suppRange", suppRange);//供应商子范围编码
		List<OrderRele> orderList = orderMapper.findOrderListBySapIdAndMateCode(param);
		if (orderList.size() > 0) {
			for (OrderRele or : orderList) {
				// 获取采购订单号
				String contOrdeNumb = or.getContOrdeNumb();
				// 获取采购订单详情
				List<OrderMate> mates = or.getMates();
				//List<OrderMate> mates = orderMapper.findOrderMateByOrderNo(contOrdeNumb);
				if (mates.size() > 0) {
					OrderMate orderMate = mates.get(0);
					// 采购订单上的未交数量
					Double unpa = orderMate.getUnpaQuan();
					StraMessAndMateDO mate = orderMateCheckService.calculateActualOrderNumber(contOrdeNumb, mateCode,suppRange,
							unpa);
					// 通过和历史采购订单比较获取到的未交量
					Double unpaNumber = mate.getUnpaNumber();
					// 通过和历史采购订单比较获取到的计算未交量
					Double calculNumber = mate.getCalculNumber();
					double cal = calculNumber;
					if (cal != 0) {
						OrderDO orderDO = new OrderDO();
						orderDO.setCalculNumber(calculNumber);
						orderDO.setCompany(orderMate.getCompany());
						orderDO.setContOrdeNumb(contOrdeNumb);
						orderDO.setFrequency(orderMate.getFrequency());
						orderDO.setMateNumb(orderMate.getMateNumb());
						orderDO.setSubeDate(or.getSubeDate());
						orderDO.setUnpaQuan(unpaNumber);
						list.add(orderDO);
					}
				}
			}
		}
		return list;
	}

	@Override
	@Transactional
	public boolean cancellDeliveryByDeliIds(List<String> list) {
		int count = 0;
		int straMessTotal = 0;
		int appoTotal = 0;
		int straMessCount = 0;
		int appoCount = 0;
		for (String deliId : list) {
			int j = 0;
			int i = 0;
			int h = 0;
			Delivery delivery = deliveryMapper.queryDeliveryByDeliId(deliId);
			String deliType = delivery.getDeliType();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deliCode", delivery.getDeliCode());
			map.put("status", "已作废");
			j = deliveryMapper.updateDeliStatusByDeliCode(map);
			count += j;
			if ("0".equals(deliType)) {// 直发通知
				straMessTotal++;
				map.put("messStatus", "已通知");
				map.put("messCode", delivery.getMapgCode());
				i = straMessageMapper.updateMessStatusByMessCode(map);
				straMessCount += i;
			} else {// 预约申请
				appoTotal++;
				map.put("appoStatus", "已发布");
				map.put("appoCode", delivery.getMapgCode());
				h = appointMapper.updateAppoStatusByAppoCode(map);
				appoCount += h;
			}
		}
		if (count == list.size() && straMessCount == straMessTotal && appoCount == appoTotal) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int queryNumOfOrderId(String suppId, String mateCode, String orderId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sapId", suppId);
		param.put("mateCode", mateCode);
		param.put("suppRange", "");//供应商子范围编码
		param.put("orderId", orderId);
		return deliveryMapper.queryNumOfOrderId(param);
	}

}
