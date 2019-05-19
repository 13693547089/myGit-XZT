package com.faujor.service.rm.impl;

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
import com.faujor.dao.master.bam.ReceiveMapper;
import com.faujor.dao.master.bam.StraMessageMapper;
import com.faujor.dao.master.rm.DelieveOccupyMapper;
import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.rm.OperationRecord;
import com.faujor.service.rm.DelieveOccupyService;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "delieveOccupyService")
public class DelieveOccupyServiceImpl implements DelieveOccupyService {

	
	@Autowired
	private DelieveOccupyMapper delieveOccupyMapper;
	@Autowired
	private DeliveryMapper deliveryMapper;
	@Autowired
	private ReceiveMapper receiveMapper;
	@Autowired
	private AppointMapper appointMapper;
	@Autowired
	private StraMessageMapper straMessageMapper;
	@Override
	public Map<String, Object> getAppoDeliData(String code, String codeDesc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if("appoint".equals(codeDesc)) {
			//code是预约单号
			//获取预约单信息
			Appoint appoint = appointMapper.queryOneAppointbyAppoCode(code);
			if(appoint != null) {
				String appoId = appoint.getAppoId();
				List<AppoCar> appoCarList = appointMapper.queryAppoCarOfAppoint(appoId);
				map.put("appoCarList", appoCarList);
			}
			//获取送货单信息
			List<Delivery> deliveryList = deliveryMapper.getDeliveryListByAppoCode(code);
			//获取收货单号
			List<Receive> receiveList = receiveMapper.getReceiveListByAppoCode(code);
			map.put("appoint", appoint);
			map.put("deliveryList", deliveryList);
			map.put("receiveList", receiveList);
		}else if("straMess".equals(codeDesc)){
			//code是直发单号
			//获取直发单信息
			StraMessage straMessage = straMessageMapper.queryOneStraMessageByMessCode(code);
			//获取送货单信息
			List<Delivery> deliveryList = deliveryMapper.getDeliveryListByAppoCode(code);
			//获取收货单号
			List<Receive> receiveList = receiveMapper.getReceiveListByAppoCode(code);
			map.put("straMessage", straMessage);
			map.put("deliveryList", deliveryList);
			map.put("receiveList", receiveList);
		}else if("delivery".equals(codeDesc)) {
			//code是送货单号
			//获取送货单信息
			Delivery delivery = deliveryMapper.queryOneDeliveryByDeliCode(code);
			ArrayList<Object> deliveryList = new ArrayList<>();
			//获取收货单信息
			List<Receive> receiveList = receiveMapper.queryReceiveListByDeliCode(code);
			if(delivery != null) {
				deliveryList.add(delivery);
				//deliType=1:预约送货 deliType=0:直发送货 
				String deliType = delivery.getDeliType();
				String mapgCode = delivery.getMapgCode();
				if("0".equals(deliType)) {
					//获取直发通知单信息 
					StraMessage straMessage = straMessageMapper.queryOneStraMessageByMessCode(mapgCode);
					map.put("straMessage", straMessage);
				}else if("1".equals(deliType)){
					//获取预约单信息
					Appoint appoint = appointMapper.queryOneAppointbyAppoCode(mapgCode);
					if(appoint != null) {
						String appoId = appoint.getAppoId();
						List<AppoCar> appoCarList = appointMapper.queryAppoCarOfAppoint(appoId);
						map.put("appoCarList", appoCarList);
					}
					map.put("appoint", appoint);
				}
			}
			map.put("deliveryList", deliveryList);
			map.put("receiveList", receiveList);
		}else if("receive".equals(codeDesc)) {
			//code是收货单号
			//获取收货单信息
			Receive receive =receiveMapper.queryOneReceiveByReceCode(code);
			List<Receive> receiveList = new ArrayList<>();
			if(receive != null) {
				receiveList.add(receive);
			}
			//获取送货单信息
			List<Delivery> deliveryList = deliveryMapper.queryDeliveryListByReceCode(code);
			if(deliveryList.size()>0) {
				//deliType=1:预约送货 deliType=0:直发送货 
				Delivery delivery = deliveryList.get(0);
				String deliType = delivery.getDeliType();
				String mapgCode = delivery.getMapgCode();
				if("0".equals(deliType)) {
					//获取直发通知单信息 
					StraMessage straMessage = straMessageMapper.queryOneStraMessageByMessCode(mapgCode);
					map.put("straMessage", straMessage);
				}else if("1".equals(deliType)){
					//获取预约单信息
					Appoint appoint = appointMapper.queryOneAppointbyAppoCode(mapgCode);
					if(appoint != null) {
						String appoId = appoint.getAppoId();
						List<AppoCar> appoCarList = appointMapper.queryAppoCarOfAppoint(appoId);
						map.put("appoCarList", appoCarList);
					}
					map.put("appoint", appoint);
				}
			}
			map.put("deliveryList", deliveryList);
			map.put("receiveList", receiveList);
		}else {
			map.put("judge", false);
			map.put("msg", "参数异常");
			return map;
		}
		return map;
	}
	@Override
	@Transactional
	public boolean updateStatus(String code, String status, String type) {
		boolean b = false;
		String releCode = "";
		String recordType ="";
		String message = "参数错误，修改状态失败，没有修改任何单子";
		Map<String,Object> map = new HashMap<String,Object>();
		if("appoint".equals(type)) {
			//修改预约单状态
			releCode = code;
			recordType = "预约单";
			map.put("appoStatus", status);
			map.put("appoCode", code);
			int i = appointMapper.updateAppoStatusByAppoCode(map);
			if (i == 1) {
				b = true;
				message = "修改预约单"+code+"的状态为:"+status;
			} else {
				b = false;
				message = "修改预约单状态失败";
			}
		}else if("straMess".equals(type)){
			releCode = code;
			recordType = "直发单";
			map.put("messStatus", status);
			map.put("messCode", code);
			int i = straMessageMapper.updateMessStatusByMessCode(map);
			if (i == 1) {
				b = true;
				message = "修改直发单"+code+"的状态为:"+status;
			} else {
				b = false;
				message = "修改直发单状态失败";
			}
		}else if("delivery".equals(type)){
			releCode = code;
			recordType = "送货单";
			map.put("status", status);
			map.put("deliCode", code);
			int i = deliveryMapper.updateDeliStatusByDeliCode(map);
			if (i == 1) {
				b = true;
				message = "修改送货单"+code+"的状态为:"+status;
			} else {
				b = false;
				message = "修改送货单状态失败";
			}
		}else if("receive".equals(type)) {
			releCode = code;
			recordType = "收货单";
			map.put("status", status);
			map.put("receCode", code);
			int i = receiveMapper.updateStatusOfReceiveByReceCode(map);
			if (i == 1) {
				b = true;
				message = "修改收货单"+code+"的状态为:"+status;
			} else {
				b = false;
				message = "修改收货单状态失败";
			}
		}
		SysUserDO user = UserCommon.getUser();
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

}
