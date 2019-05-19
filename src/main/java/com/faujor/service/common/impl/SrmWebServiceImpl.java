package com.faujor.service.common.impl;

import java.util.Date;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.common.InterfaceLogMapper;
import com.faujor.entity.common.InterfaceLogMain;
import com.faujor.service.bam.DeliverySignService;
import com.faujor.service.bam.QuoteService;
import com.faujor.service.common.SrmWebService;

@Component
@WebService(targetNamespace = "http://common.service.faujor.com/", endpointInterface = "com.faujor.service.common.SrmWebService")
public class SrmWebServiceImpl implements SrmWebService {
	@Autowired
	private QuoteService quoteService;
	@Autowired
	private DeliverySignService signService;	
	@Autowired
	private InterfaceLogMapper logMapper;

	@Override
	public String invokeWs(String action, String data) {

		InterfaceLogMain log=new InterfaceLogMain();
		log.setInterfaceNum(action);
		log.setInJson(data);
		log.setInvokeTime(new Date());
		
		JSONObject obj = new JSONObject();
		if (StringUtils.isEmpty(action)) {
			obj.put("STATUS", "E");
			obj.put("MESSAGE", "请输入的接口编号！");
			log.setStatus("E");
			log.setMessage("请输入的接口编号！");
			log.setOutJson(obj.toJSONString());
			logMapper.saveLog(log);
			return obj.toJSONString();
		} else if ("srm01".equals(action)) {
			String outStr = quoteService.oaAuditQuote(data).toJSONString();
			log.setInterfaceDesc("OA接口数据回传");
			log.setOutJson(outStr);
			log.setInvoker("OA系统");
			JSONObject returnObj=JSONObject.parseObject(outStr);
			log.setStatus(returnObj.getString("STATUS"));
			log.setMessage(returnObj.getString("MESSAGE"));
			logMapper.saveLog(log);
			return outStr;
		} else if ("sign01".equals(action)) {
			// 查询送货单信息
			return signService.findDeliveryByDeliCode(data);
		} else if ("sign02".equals(action)) {
			// 保存签到信息
			return signService.saveSignInfo(data);
		} else {
			obj.put("STATUS", "E");
			obj.put("MESSAGE", "请输入的接口编号！");
			log.setStatus("E");
			log.setMessage("请输入的接口编号！");
			log.setOutJson(obj.toJSONString());
			logMapper.saveLog(log);
			return obj.toJSONString();
		}
	}
}
