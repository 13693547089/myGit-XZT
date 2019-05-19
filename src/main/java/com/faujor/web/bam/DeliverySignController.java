package com.faujor.web.bam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.delivery.DeliverySignDO;
import com.faujor.entity.basic.Dic;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.DeliverySignService;
import com.faujor.service.basic.BasicService;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/sign")
public class DeliverySignController {
	@Autowired
	private DeliverySignService deliverySignService;
	@Autowired
	private BasicService basicService;
	/**
	 * 签到列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model) {
		List<Dic> statusList = basicService.findDicListByCategoryCode("DJZT");
		model.addAttribute("statusList", statusList);
		return "bam/sign/list";
	}

	/**
	 * 获取签到数据
	 * 
	 * @return
	 */
	@RequestMapping("/getSignData")
	@ResponseBody
	public Map<String, Object> getSignData(String page, String limit) {
		// 转化页数和每页条数
		int pageInt = Integer.parseInt(page);
		int limitInt = Integer.parseInt(limit);
		int offset = 0;
		if (page != null) {
			offset = (pageInt - 1) * limitInt;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		return deliverySignService.getSignData(offset, limitInt, map);
	}

	/**
	 * 签到，触发扫描枪，得到扫描信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign")
	public String sign(Model model) {
		// 触发扫描枪
		String deliCode = "";// 扫描枪获取到的送货单号
		String delivery = deliverySignService.findDeliveryByDeliCode(deliCode);
		DeliverySignDO dsd = new DeliverySignDO();
		dsd.setSignTime(new Date());
//		if (delivery != null) {
//			dsd.setDeliCode(delivery.getDeliCode());
//			dsd.setSuppName(delivery.getSuppName());
//		} else {
//			model.addAttribute("noDelivery", "没有此送货单，请检查！");
//		}
		model.addAttribute("dsd", dsd);
		return "bam/sign/sign";
	}

	/**
	 * 保存签到信息
	 * 
	 * @param dsd
	 * @return
	 */
	@RequestMapping("/saveSign")
	@ResponseBody
	public RestCode saveSign(DeliverySignDO dsd) {
		// int i = deliverySignService.saveSignInfo(dsd);
		// if (i > 0)
		// return RestCode.ok();
		return RestCode.error();
	}
}
