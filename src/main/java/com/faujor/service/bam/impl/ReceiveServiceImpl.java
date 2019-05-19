package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.common.RFC.SAPInvoke;
import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.bam.ReceiveMapper;
import com.faujor.dao.master.rm.DelieveOccupyMapper;
import com.faujor.dao.sapcenter.bam.OrderMapper1;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.bam.delivery.OutData;
import com.faujor.entity.bam.delivery.OutDataDO;
import com.faujor.entity.bam.receive.EkbeDO;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.rm.OperationRecord;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.ReceiveService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.common.CodeService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.SAPInterfaceUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "receiveService")
public class ReceiveServiceImpl implements ReceiveService {

	@Autowired
	private ReceiveMapper receiveMapper;
	@Autowired
	private DeliveryMapper deliveryMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private CodeService codeService;
	@Autowired
	private OrderMapper1 orderMapper1;
	@Autowired
	private AsyncLogService asyncLogService;
	@Autowired
	private DelieveOccupyMapper delieveOccupyMapper;
	
	@Override
	public Map<String, Object> queryReceiveByPage(Map<String, Object> map) {
		List<Receive> list = receiveMapper.queryReceiveByPage(map);
		int count = receiveMapper.queryReceiveByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean deleteReceiveByReceId(String[] receIds) {
		receiveMapper.deleteReceMateByReceId(receIds);
		int i = receiveMapper.deleteReceiveByReceId(receIds);
		if (i == receIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addReceive(Receive rece, List<ReceMate> list) {
		int i = receiveMapper.addReceive(rece);
		int count = 0;
		for (ReceMate rm : list) {
			int j = 0;
			rm.setReceId(rece.getReceId());
			j = receiveMapper.addReceMate(rm);
			count += j;
		}
		if (i == 1 && count == list.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Receive queryReceiveByReceId(String receId) {
		Receive rece = receiveMapper.queryReceiveByReceId(receId);
		return rece;
	}

	@Override
	public List<ReceMate> queryReceMatesByReceId(String receId) {
		return receiveMapper.queryReceMatesByReceId(receId);
	}

	@Override
	@Transactional
	public boolean updateReceiveByReceId(Receive rece, String type, List<ReceMate> list) {
		receiveMapper.updateReceiveByReceId(rece);
		String[] receIds = new String[1];
		receIds[0] = rece.getReceId();
		List<String> mateIDs = receiveMapper.findMateIDsByReceId(rece.getReceId());
		List<ReceMate> addList = new ArrayList<ReceMate>();
		int i = 0;
		for (ReceMate receMate : list) {
			receMate.setReceId(rece.getReceId());
			if ("add".equals(type) || "regress".equals(type)) {
				receMate.setIsOccypy("no");
			} else {
				receMate.setIsOccypy("yes");
			}
			if (mateIDs.contains(receMate.getId())) {
				// 如果包含，则更新
				i += receiveMapper.updateReceMate(receMate);
				mateIDs.remove(receMate.getId());
			} else {
				// 新增物料
				String uuid = UUIDUtil.getUUID();
				receMate.setId(uuid);
				addList.add(receMate);
			}
		}

		if (mateIDs.size() > 0)
			receiveMapper.removeReceMateByIDs(mateIDs);
		if (addList.size() > 0)
			i += receiveMapper.batchSaveReceMate(addList);
		// int j = receiveMapper.deleteReceMateByReceId(receIds);
		// int count = 0;
		// for (ReceMate rm : list) {
		// int k = 0;
		// rm.setReceId(rece.getReceId());
		// k = receiveMapper.addReceMate(rm);
		// count += k;
		// }
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public String asyncToSAP(Receive rece, String receMateData) {
		List<ReceMate> list = JsonUtils.jsonToList(receMateData, ReceMate.class);
		Date date = rece.getReceDate();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String lfdat = df.format(date);
		JSONArray ja = new JSONArray();
		for (ReceMate rm : list) {
			JSONObject json = new JSONObject();
			json.put("BOLNR", rece.getReceCode());
			json.put("LFDAT", lfdat);
			json.put("EBELN", rm.getOrderId());
			json.put("EBELP", rm.getFrequency());
			json.put("MATNR", rm.getMateCode());
			json.put("MENGE", rm.getReceNumber());
			json.put("LGORT", rm.getStorLocation());
			ja.add(json);
		}
		SAPInvoke invoke = new SAPInvoke();
		String backJson = invoke.invoke(SAPInterfaceUtil.RFC_IBD_DELIVERY_IMPORT, ja.toJSONString());
		System.out.println("收货调用SAP接口返回值为:"+backJson);
		String result = "";
		if(backJson == null || "".equals(backJson)){
			result = "调用SAP接口异常";
		}else{
			List<OutData> list2 = JsonUtils.jsonToList(backJson, OutData.class);
			for (OutData outData : list2) {
				if ("S".equals(outData.getFlag())) {
					if(!StringUtils.isEmpty(outData.getDeliveryNo())){
						receiveMapper.updateReceiveMateByOutData(outData);
					}else{
						if ("".equals(result)) {
							result += outData.getOrderNo() + ":收货调用SAP接口返回的内向交货单号为空";
						} else {
							result += ";" + outData.getOrderNo() + ":收货调用SAP接口返回的内向交货单号为空";
						}
					}
				} else {
					if ("".equals(result)) {
						result += outData.getOrderNo() + ":" + outData.getErrorMsg();
					} else {
						result += ";" + outData.getOrderNo() + ":" + outData.getErrorMsg();
					}
				}
			}
		}
		if (StringUtils.isEmpty(result)) {
			rece.setAsyncStatus("同步成功");
			receiveMapper.updateReceAsyncStatusByReceCode(rece);
		} else {
			rece.setAsyncStatus("同步失败");
			rece.setErrorMsg(result);
			receiveMapper.updateReceAsyncStatusByReceCode(rece);
		}
		return result;
	}

	@Override
	@Transactional
	public Map<String, Object> writeoffReceive(String receCode, String inboDeliCodes, String deliCode) {
		List<String> list = JsonUtils.jsonToList(inboDeliCodes, String.class);
		Set<String> set = new HashSet<>();
		for (String str : list) {
			set.add(str);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray ja = new JSONArray();
		for (String s : set) {
			JSONObject json = new JSONObject();
			json.put("BOLNR", receCode);
			json.put("VBELN", s);
			ja.add(json);
		}
		SAPInvoke invoke = new SAPInvoke();
		String backJson = invoke.invoke(SAPInterfaceUtil.ZSRMRFC_IBD_DELIVERY_DELETE, ja.toJSONString());
		System.out.println("冲销调用SAP接口返回值为:"+backJson);
		if ("".equals(backJson) || backJson == null) {
			map.put("judge", false);
			map.put("result", "");
			map.put("msg", "收货单冲销失败,调用SAP接口异常");
			return map;
		}
		List<OutDataDO> jsonToList = JsonUtils.jsonToList(backJson, OutDataDO.class);
		if (jsonToList.size() > 0) {
			String result = "";
			int count = 0;
			for (OutDataDO odd : jsonToList) {
				if ("S".equals(odd.getFlag())) {
					count++;
				} else {
					if ("".equals(result)) {
						result += odd.getInboDeliCode()+":"+odd.getMessage();
					} else {
						result += ";" + odd.getInboDeliCode()+":"+odd.getMessage();
					}
				}
			}
			if (count == jsonToList.size()) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("status", "已冲销");
				param.put("receCode", receCode);
				int i = receiveMapper.updateStatusOfReceiveByReceCode(param);
				//根据送货单号查询送货单信息
				Delivery delivery = deliveryMapper.queryOneDeliveryByDeliCode(deliCode);
				if("0".equals(delivery.getDeliType())) {//直发送货
					param.put("status", "已发货");
				}else {//预约送货，特殊送货
					param.put("status", "已签到");
				}
				param.put("deliCode", deliCode);
				int j = deliveryMapper.updateDeliStatusByDeliCode(param);
				if (i == 1 && j == 1) {
					map.put("judge", true);
					map.put("msg", "收货单冲销成功");
					map.put("result", "");
				} else {
					map.put("judge", false);
					map.put("msg", "状态修改失败");
					map.put("result", "");
				}
			} else {
				map.put("judge", false);
				map.put("msg", "内向交货单冲销失败");
				map.put("result", result);
			}
		} else {
			map.put("judge", false);
			map.put("msg", "内向交货单冲销失败");
			map.put("result", "");
		}

		return map;
	}

	@Override
	public Map<String, Object> queryAllReceiveByPage(Map<String, Object> map) {
		List<Receive> list = receiveMapper.queryAllReceiveByPage(map);
		int count = receiveMapper.queryAllReceiveByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	public Map<String, Object> queryDeliveryByDeliCode(String deliCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Delivery deli = deliveryMapper.queryDeliveryByDeliCode(deliCode);
		if (deli != null) {
			List<DeliMate> list = deliveryMapper.queryDeliMateByDeliCode(deliCode);
			List<ReceMate> result = new ArrayList<ReceMate>();
			for (DeliMate deliMate : list) {
				ReceMate receMate = new ReceMate();
				receMate.setReceNumber(deliMate.getDeliNumber());
				receMate.setOrderId(deliMate.getOrderId());
				receMate.setMateCode(deliMate.getMateCode());
				receMate.setMateName(deliMate.getMateName());
				receMate.setDeliNumber(deliMate.getDeliNumber());
				receMate.setUnit(deliMate.getUnit());
				receMate.setProdPatchNum(deliMate.getProdPatchNum());
				receMate.setRemark(deliMate.getRemark());
				receMate.setFrequency(deliMate.getFrequency());
				// 工厂和库位
				String orderId = deliMate.getOrderId();
				String mateCode = deliMate.getMateCode();
				String frequency = deliMate.getFrequency();
				//OrderMate orderMate = orderMapper.findOrderMateByOrderNoAndMateCode(orderId, mateCode);
				OrderMate orderMate = orderMapper.findOrderMateByOrderNoAndMateCodeAndFrequence(orderId, mateCode,frequency);
				receMate.setStorLocation(orderMate.getLibrPosi());
				receMate.setFactoryAddr(orderMate.getFactoryAddr());
				result.add(receMate);
			}
			map.put("judge", true);
			map.put("list", result);
			map.put("deli", deli);
			return map;
		} else {
			map.put("judge", false);
			return map;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> createReceive(Receive rece, String receMateData, String type) {
		SysUserDO user = UserCommon.getUser();
		List<ReceMate> list = JsonUtils.jsonToList(receMateData, ReceMate.class);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		map.put("deliCode", rece.getDeliCode());
		String receCode = codeService.getCodeByCodeType("receiveNo");
		rece.setReceCode(receCode);
		rece.setCreateId(user.getUserId().toString());
		rece.setCreator(user.getName());
		if ("add".equals(type) || "regress".equals(type)) {
			// 验退 或者待收货
			if ("regress".equals(type)) {
				rece.setStatus("已验退");
				map.put("status", "已验退");
			} else {
				rece.setStatus("已保存");
				map.put("status", "待收货");
			}
			// boolean b = addReceive(rece, list);
			int i = receiveMapper.addReceive(rece);
			int count = 0;
			for (ReceMate rm : list) {
				int j = 0;
				rm.setReceId(rece.getReceId());
				rm.setIsOccypy("no");
				j = receiveMapper.addReceMate(rm);
				count += j;
			}
			int c = deliveryMapper.updateDeliStatusByDeliCode(map);
			if (i == 1 && count == list.size() && c == 1){
				result.put("restCode", RestCode.ok());
			}else{
				result.put("restCode", RestCode.error());
			}
			return result;
		} else {
			rece.setStatus("已收货");
			map.put("status", "已收货");
			// boolean b = addReceive(rece, list);
			int i = receiveMapper.addReceive(rece);
			int count = 0;
			for (ReceMate rm : list) {
				int j = 0;
				rm.setReceId(rece.getReceId());
				rm.setIsOccypy("yes");
				j = receiveMapper.addReceMate(rm);
				count += j;
			}
			int c = deliveryMapper.updateDeliStatusByDeliCode(map);
			// boolean e = orderService.updateLineProject(list);
			// 预约送货，特殊送货,直发送货都需要调接口
			String str = asyncToSAP(rece, receMateData);
			if (i == 1 && c == 1 && count == list.size()) {
				if (StringUtils.isEmpty(str)) {
					result.put("restCode", RestCode.ok());
					return result;
				} else {
					result.put("restCode", RestCode.ok(2, str));
					result.put("receCode", receCode);
					return result;
				}
			}
			result.put("restCode", RestCode.error());
			return result;
		}
	}

	@Override
	@Transactional
	public RestCode updateReceive(Receive rece, String receMateData, String type, String deliCode2) {
		List<ReceMate> list = JsonUtils.jsonToList(receMateData, ReceMate.class);
		if ("add".equals(type) || "regress".equals(type)) {
			if (!deliCode2.equals(rece.getDeliCode())) {// 送货单号被修改
				// 原来的送货单状态变为 “已签到”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("status", "已签到");
				map1.put("deliCode", deliCode2);
				int c = deliveryMapper.updateDeliStatusByDeliCode(map1);
				// 新关联的送货单的状态变为 “待收货”，或者是已验退
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("status", "待收货");
				if ("regress".equals(type)) {
					rece.setStatus("已验退");
					map2.put("status", "已验退");
				}
				map2.put("deliCode", rece.getDeliCode());
				int d = deliveryMapper.updateDeliStatusByDeliCode(map2);
				boolean b = updateReceiveByReceId(rece, type, list);
				if (b && d == 1 && c == 1)
					return RestCode.ok();
				return RestCode.error();
			} else {
				boolean b = updateReceiveByReceId(rece, type, list);
				if (b)
					return RestCode.ok();
				return RestCode.error();
			}
		} else {
			int c = 1;
			if (!deliCode2.equals(rece.getDeliCode())) {// 送货单号被修改
				// 原来的送货单状态变为 “已签到”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("status", "已签到");
				map1.put("deliCode", deliCode2);
				c = deliveryMapper.updateDeliStatusByDeliCode(map1);
			}
			rece.setStatus("已收货");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("status", "已收货");
			map2.put("deliCode", rece.getDeliCode());
			int d = deliveryMapper.updateDeliStatusByDeliCode(map2);
			boolean b = updateReceiveByReceId(rece, type, list);
			// boolean e = orderService.updateLineProject(list);
			// 预约送货，特殊送货,直发送货都需要调接口
			String str = asyncToSAP(rece, receMateData);
			if (b && d == 1 && c == 1) {
				if (StringUtils.isEmpty(str)) {
					return RestCode.ok();
				} else {
					return RestCode.ok(2, str);
				}
			}
			return RestCode.error();
		}
	}

	@Override
	public int idenToReceive(AsyncLog al) {
		List<ReceMate> list = receiveMapper.findReceMate();
		int i = 0;
		for (ReceMate receMate : list) {
			List<EkbeDO> ekbeList = orderMapper1.findEkbeByReceMate(receMate);
			if (ekbeList.size() > 0) {
				BigDecimal num = new BigDecimal(0);
				for (EkbeDO ekbeDO : ekbeList) {
					// 正反标识 101+,102-
					String iden = ekbeDO.getPnIden();
					BigDecimal menge = new BigDecimal(ekbeDO.getMenge());
					if ("101".equals(iden)) {
						num = num.add(menge);
					} else if ("102".equals(iden)) {
						num = num.subtract(menge);
					}
					// num >0 则
					int r = num.compareTo(BigDecimal.ZERO);
					if (r == 1) {
						i += receiveMapper.updateReceiveMateByReceMate(receMate);
					}
				}
			}
		}
		al.setAsyncNum(i);
		al.setAsyncStatus("同步成功");
		asyncLogService.updateAsyncLog(al);
		return i;
	}

	@Override
	@Transactional
	public boolean updateStatusOfReceiveByReceCode(Map<String, Object> map) {
		int i = receiveMapper.updateStatusOfReceiveByReceCode(map);
		if(i==1) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateReceMateInboDeliCodeAndIsOccupy(ReceMate receMate) {
		boolean b = false;
		String releCode = receMate.getId();
		String recordType ="收货单";
		String message = "修改失败";
		int i = receiveMapper.updateReceMateInboDeliCodeAndIsOccupy(receMate);
		SysUserDO user = UserCommon.getUser();
		if(i==1) {
			b = true;
			message = "修改收货单的内向交货单为："+receMate.getInboDeliCode()+",占用状态为："+receMate.getIsOccypy();
		}else {
			b = false;
			message = "修改失败";
		}
		OperationRecord record = new OperationRecord();
		record.setId(UUIDUtil.getUUID());
		record.setUserId(user.getUserId());
		record.setUserName(user.getName());
		record.setCreateDate(new Date());
		record.setRecordType(recordType);
		record.setReleCode(releCode);
		record.setMessage(message);
		delieveOccupyMapper.addOperationRecord(record);
		return b;
		
	}

	@Override
	public ReceMate queryReceMateMessById(String id) {
		return receiveMapper.queryReceMateMessById(id);
	}

	@Override
	@Transactional
	public boolean deleteReceiveByReceId2(String[] receIds, String deliCodes2) {
		List<String> list = JsonUtils.jsonToList(deliCodes2, String.class);
		boolean b = deleteReceiveByReceId(receIds);
		Map<String, Object> map = new HashMap<String, Object>();
		int count =0;
		for (String s : list) {
			map.put("deliCode", s);
			//根据送货单号查询送货单信息
			Delivery delivery = deliveryMapper.queryOneDeliveryByDeliCode(s);
			if("0".equals(delivery.getDeliType())) {//直发送货
				map.put("status", "已发货");
			}else {//预约送货，特殊送货
				map.put("status", "已签到");
			}
			int c =0;
			c = deliveryMapper.updateDeliStatusByDeliCode(map);
			count +=c;
		}
		if(b && count == list.size()) {
			return true;
		}else {
			return false;
		}
	}

}
