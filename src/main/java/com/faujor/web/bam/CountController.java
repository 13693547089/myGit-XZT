package com.faujor.web.bam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.bam.StraMessAndMateDO;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/count")
public class CountController {

	@Autowired
	private OrderMateCheckService orderMateCheckService;

	/**
	 * 跳转到获取订单物料计算未交量的页面
	 * 
	 * @return
	 */
	@RequestMapping("/getCalculNumberHtml")
	public String getCalculNumberHtml() {

		return "/bam/count/countCalculNum";
	}

	/**
	 * 获取订单中物料的计算为交量（订单可用量）
	 * 
	 * @param orderNo
	 * @param mateCode
	 * @param unpaNumber
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMateCalculNumber")
	public StraMessAndMateDO getMateCalculNumber(String orderNo, String mateCode,String suppRange, Double unpaNumber) {
		StraMessAndMateDO result = orderMateCheckService.calculateActualOrderNumber(orderNo, mateCode,suppRange, unpaNumber);
		return result;
	}

	/**
	 * 占有数据
	 * 
	 * @param orderNo
	 * @param mateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOccupyNumberByParams")
	public Map<String, Object> findOccupyNumberByParams(String orderNo, String mateCode,String suppRange) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 锁定直发通知单的数量
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("mateCode", mateCode);
		params.put("suppRange", suppRange);
		List<StraMessAndMateDO> list = orderMateCheckService.findOccupyNumberByParams(params);
		result.put("data", list);
		result.put("count", list.size());
		result.put("msg", "0");
		result.put("code", "0");
		return result;
	}

}
