package com.faujor.service.bam.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.dao.master.bam.DeliverySignMapper;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.delivery.DeliverySignDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.DeliverySignService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service("deliverySignService")
public class DeliverySignServiceImpl implements DeliverySignService {
	@Autowired
	private DeliveryMapper deliveryMapper;
	@Autowired
	private DeliverySignMapper deliverySignMapper;

	@Override
	public Map<String, Object> getSignData(int offset, int limit, Map<String, Object> params) {
		RowBounds rb = new RowBounds(offset, limit);
		List<Delivery> list = deliveryMapper.findDeliveryByParams(rb, params);
		int count = deliveryMapper.countDeliveryByParams(params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public String saveSignInfo(String data) {
		JSONObject dataobj = JSONObject.parseObject(data);
		String deliCode = dataobj.getString("code");
		String signDate = dataobj.getString("signDate");
		// 更新送货单状态
		Delivery delivery = deliveryMapper.findDeliveryByDeliCode(deliCode);
		String status = delivery.getStatus();
		JSONObject json = new JSONObject();
		if("已收货".equals(status)) {
			json.put("flag", 1);
			json.put("msg", "不允许重复签到！");
			return json.toJSONString();
		}
		if (delivery != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			try {
				date = formatter.parse(signDate);
				delivery.setSignDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			delivery.setStatus("已签到");
			deliveryMapper.updateDeliveryByDeliId(delivery);
		}
		DeliverySignDO dsd = new DeliverySignDO();
		dsd.setDeliCode(deliCode);
		if (StringUtils.isEmpty(dsd.getId()))
			dsd.setId(UUIDUtil.getUUID());
		// 保存签到信息
		dsd.setSignStatus("已签到");
		int i = deliverySignMapper.saveSignInfo(dsd);
		
		if (i > 0) {
			json.put("flag", 0);
			json.put("msg", "签到成功！");
		} else {
			json.put("flag", 1);
			json.put("msg", "签到失败！");
		}
		return json.toJSONString();
	}

	@Override
	public String findDeliveryByDeliCode(String data) {
		JSONObject dataObj = JSONObject.parseObject(data);
		String deliCode = dataObj.getString("code");
		Delivery delivery = deliveryMapper.findDeliveryByDeliCode(deliCode);
		JSONObject json = new JSONObject();
		if (delivery != null) {
			json.put("flag", 0);
			json.put("msg", "获取成功！");
			json.put("data", delivery);
		} else {
			json.put("flag", 1);
			json.put("msg", "无此送货单！");
		}

		return json.toJSONString();
	}

}
