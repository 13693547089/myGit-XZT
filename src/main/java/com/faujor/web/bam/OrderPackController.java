package com.faujor.web.bam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.bam.*;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.OrderPackService;
import com.faujor.service.bam.OrderService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.UserCommon;

@Controller
public class OrderPackController {
	@Autowired
	private OrderPackService orderPackService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private OrderService orderService;
	/**
	 * 跳转到包材采购订单列表
	 * @param model
	 * @return
	 */
	@Log(value = "获取包材采购订单列表页面")
	@RequestMapping("/getOrderPackListHtml")
	public String getOrderPackListHtml(Model model) {
		List<Dic> orderPackStatus = basicService.findDicListByCategoryCode("OrderPackStatus");
		model.addAttribute("orderPackStatus", orderPackStatus);
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);
		return "bam/order/OrderPack/orderPackList";
	}
	
	/**
	 * 获取包材采购订单列表
	 * @param orderPackVO
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取包材采购订单列表")
	@ResponseBody
	@RequestMapping("/queryOrderPackByPage")
	public Map<String,Object> queryOrderPackByPage(OrderPackVO orderPackVO,Integer limit,Integer page){
		if(page == null){page=1;}
		if(limit == null){limit =10;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("orderPackVO", orderPackVO);
		Map<String, Object> page2 = orderPackService.queryOrderPackByPage(map);
		return page2;
	}

	/**
	 * 跳转到包材采购订单创建,编辑,查看页面
	 *@param type , type=1:表示创建，type=2:表示编辑，type=3：表示查看
	 * @param model
	 * @return
	 */
	@Log(value = "获取包材采购订单创建页面")
	@RequestMapping("/getOrderPackDetailHtml")
	public String getOrderPackAddHtml(Model model,String type,String id,String oemOrderCode) {
		SysUserDO user = UserCommon.getUser();
		String userType = user.getUserType();
		System.out.println("用户类型 ： " + userType);
		if("supplier".equals(userType)) {
			OrderPackVO orderPackVO = new OrderPackVO();
			//OEM供应商,创建时，当前OEM供应商信息，有关OEM供应商的NB类型OEM订单集合
			QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			orderPackVO.setId(id);
			orderPackVO.setOemSapId(supp.getSapId());
			orderPackVO.setOemSuppName(supp.getSuppName());
			model.addAttribute("orderPackVO", orderPackVO);
			model.addAttribute("userType", "supplier");
			if ("1".equals(type)){
				//根据供应商编号查询NB类型的采购订单编码集合
				List<String> orderCodeList = orderPackService.queryOrderMessListByOrderTypeAndSapId("NB",supp.getSapId());
				model.addAttribute("orderCodeList", orderCodeList);
				model.addAttribute("type", "1");
			}else if ("2".equals(type)){//编辑
				OrderPackVO vo = orderPackService.queryOrderPackById(id);
				orderPackVO.setLimitThan(vo.getLimitThan());
				orderPackVO.setOemOrderCode(oemOrderCode);
				List<String> orderCodeList = new ArrayList<>();
				orderCodeList.add(oemOrderCode);
				model.addAttribute("orderCodeList", orderCodeList);
				model.addAttribute("orderPackVO", orderPackVO);
				model.addAttribute("type", "2");
			}else if ("3".equals(type)){//查看
				OrderPackVO vo = orderPackService.queryOrderPackById(id);
				orderPackVO.setLimitThan(vo.getLimitThan());
				orderPackVO.setOemOrderCode(oemOrderCode);
				List<String> orderCodeList = new ArrayList<>();
				orderCodeList.add(oemOrderCode);
				model.addAttribute("orderCodeList", orderCodeList);
				model.addAttribute("orderPackVO", orderPackVO);
				model.addAttribute("type", "3");
			}
			model.addAttribute("userType", "supplier");
		}else {//采购员
			List<QualSupp> qualSuppList = orderPackService.queryAllQualSuppListByUserId(user.getUserId().toString());
//			if(qualSuppList.size()>0) {
				model.addAttribute("qualSuppList", qualSuppList);
//			}
			OrderPackVO orderPackVO = new OrderPackVO();
			List<OrderPackForm> orderPackFormList = orderPackService.queryOrderPackFormByCode(oemOrderCode);
			if ("2".equals(type)){//编辑
				OrderPackVO vo = orderPackService.queryOrderPackById(id);
				orderPackVO.setLimitThan(vo.getLimitThan());
				orderPackVO.setId(id);
				orderPackVO.setOemSapId(orderPackFormList.get(0).getSuppCode());
				orderPackVO.setOemSuppName(orderPackFormList.get(0).getSuppName());
				orderPackVO.setOemOrderCode(oemOrderCode);
				List<String> orderCodeList = new ArrayList<>();
				orderCodeList.add(oemOrderCode);
				model.addAttribute("orderCodeList", orderCodeList);
				model.addAttribute("orderPackVO", orderPackVO);
				model.addAttribute("type", "2");
			}else if ("3".equals(type)){//查看
				OrderPackVO vo = orderPackService.queryOrderPackById(id);
				orderPackVO.setLimitThan(vo.getLimitThan());
				orderPackVO.setOemSapId(orderPackFormList.get(0).getSuppCode());
				orderPackVO.setOemSuppName(orderPackFormList.get(0).getSuppName());
				orderPackVO.setOemOrderCode(oemOrderCode);
				List<String> orderCodeList = new ArrayList<>();
				orderCodeList.add(oemOrderCode);
				model.addAttribute("orderCodeList", orderCodeList);
				model.addAttribute("orderPackVO", orderPackVO);
				model.addAttribute("type", "3");
			}
			model.addAttribute("userType", "buyer");
		}
		model.addAttribute("user", user);
		model.addAttribute("type", type);
		return "bam/order/OrderPack/orderPackDetail";
	}

	@Log(value = "获取供应商订单编码列表")
	@ResponseBody
	@RequestMapping("/querySuppOrderCodeList")
	public Map<String, Object> querySuppOrderCodeList(String suppSapCode) {
		List<String> orderCodeList = orderPackService.queryOrderMessListByOrderTypeAndSapId("NB",suppSapCode);
		Map<String, Object> map = new HashMap<>();
		map.put("orderCodeList", orderCodeList);
		return map;
	}

	/**
	 * 根据采购订单编号获取采购订信息
	 * @param oemOrderCode
	 * @return
	 */
	@Log(value = "根据采购订单编号获取采购订信息")
	@ResponseBody
	@RequestMapping("/queryOrderPackMessage")
	public Map<String, Object> queryOrderPackMessage(String oemOrderCode) {
		Map<String, Object> resultMap = orderPackService.queryOrderPackMessage(oemOrderCode);
		return resultMap;
	}

	/**
	 * 跳转到 包材采购订单添加页面
	 * @return
	 */
	@Log(value = "获取包材采购订单添加页面")
	@RequestMapping(value = "/getAddBaoCaiOrderHtml")
	public String getAddBaoCaiOrderHtml(Model model,String oemOrderCode,String type) {
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);
//		根据采购订单编号获取料号信息用于弹出框下拉
		List<OrderMate> resultList = orderPackService.queryMateNumbByOrderCode(oemOrderCode);
//		List<String> mateNumbList = new ArrayList<>();
//		List<String> mateNameList = new ArrayList<>();
		List<Map<String,String>> mateNumbNameList = new ArrayList<>();
		List<OrderPackMess> asseCodeNameList = new ArrayList<>();
		for (int i = 0; i < resultList.size(); i++) {
//			mateNumbList.add(resultList.get(i).getMateNumb());
//			mateNameList.add(resultList.get(i).getProdName());
            String mateNumb = resultList.get(i).getMateNumb();
            String prodName = resultList.get(i).getProdName();
            boolean flag = true;
            for (int j = 0; j < mateNumbNameList.size(); j++) {
                String mateNumbExist = mateNumbNameList.get(j).get("mateNumb");
                if (mateNumb.equals(mateNumbExist)){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String,String> map =  new HashMap<>();
                map.put("mateNumb",mateNumb);
                map.put("mateName",prodName);
                mateNumbNameList.add(map);
            }

			String asseCode = resultList.get(i).getAsseCode();
			String asseName = resultList.get(i).getAsseName();
			if (asseCode != null && !"".equals(asseCode) && asseName != null && !"".equals(asseName)) {
				OrderPackMess orderPackMess = new OrderPackMess();
				orderPackMess.setMateCode(resultList.get(i).getMateNumb());
				orderPackMess.setPackCode(asseCode);
				orderPackMess.setPackName(asseName);
				asseCodeNameList.add(orderPackMess);
			}
		}
		if ("edit".equals(type)){
            model.addAttribute("type", "edit");
        }else{
            model.addAttribute("type", "add");
        }
//		model.addAttribute("mateNumbList", mateNumbList);
//		model.addAttribute("mateNameList", mateNameList);
        model.addAttribute("mateNumbNameList", mateNumbNameList);
		model.addAttribute("asseCodeNameList", JSONObject.toJSON(asseCodeNameList));
		return "bam/order/OrderPack/addBaoCaiOrder";
	}

	/**
	 * 根据料号获取品名
	 * @param mateNumb
	 * @return
	 */
	@Log(value = "根据采购订单编号获取采购订信息")
	@ResponseBody
	@RequestMapping("/queryMateNameByMateNumb")
	public Map<String, Object> queryMateNameByMateNumb(String mateNumb) {
		String mateName = orderPackService.queryMateNameByMateNumb(mateNumb);
		Map<String, Object> map = new HashMap<>();
		map.put("mateName", mateName);
		return map;
	}

	/**
	 * 保存包材订单
	 * @param orderPackJson
	 * @param packMessJson
	 * @param packMateJson
	 * @return
	 */
	@Log(value = "保存包材订单")
	@ResponseBody
	@RequestMapping("/saveOrderPackMate")
	public RestCode saveOrderPackMate(String orderPackJson,String packMessJson,String packMateJson,Integer type) {
		SysUserDO user = UserCommon.getUser();
		String userId = user.getUserId().toString();
		String userName = user.getUsername();
		List<OrderPackForm> orderPackFormList = JSON.parseArray(orderPackJson, OrderPackForm.class);
		List<OrderPackMess> packMessList = JSON.parseArray(packMessJson, OrderPackMess.class);
		List<OrderPackMate> packMateList = JSON.parseArray(packMateJson, OrderPackMate.class);
		if (type == 1) {//创建保存
			String orderPackId = UUIDUtil.getUUID();
			orderPackFormList.get(0).setId(orderPackId);//只有一行
			orderPackFormList.get(0).setCreator(userId);//只有一行
			orderPackFormList.get(0).setCreate_name(userName);//只有一行
			for (int i = 0; i < packMessList.size(); i++) {
				OrderPackMess item = packMessList.get(i);
				// 设置ID
				item.setId(UUIDUtil.getUUID());
				item.setOrderPackId(orderPackId);
			}
			for (int i = 0; i < packMateList.size(); i++) {
				OrderPackMate item = packMateList.get(i);
				// 设置ID
				item.setId(UUIDUtil.getUUID());
				item.setOrderPackId(orderPackId);
			}
			RestCode restCode = orderPackService.saveOrderPackMate(orderPackFormList,packMessList,packMateList);
		}else {//编辑保存
			String orderPackID = orderPackFormList.get(0).getId();
			RestCode restCode = orderPackService.saveOrderPackMateByEdit(orderPackID,packMateList,packMessList);
		};
		return new RestCode();
	}

	/**
	 * 删除订单
	 * @param Ids
	 * @return
	 */
	@Log(value ="删除订单")
	@ResponseBody
	@RequestMapping("/deleteOrderPackById")
	public boolean deleteOrderPackById(String[] Ids){
		boolean b = orderPackService.deleteOrderPackById(Ids);
		return b;
	}

	/**
	 * 提交
	 *
	 * @param oemOrderCode
	 * @return
	 */
	@Log(value = "提交订单")
	@ResponseBody
	@RequestMapping("/submitOrder")
	public RestCode submitOrder(String oemOrderCode) {
		String status = "已提交";
		orderPackService.updateOrderStatus(status, oemOrderCode);
		return new RestCode();
	}

	@Log(value = "根据下单限比更新数据")
	@ResponseBody
	@RequestMapping("/updateOrderPackMess")
	public Map<String, Object> updateOrderPackMess(String packMessJson,String limitThan,String oemOrderCode,String packId) {
		List<OrderPackMess> packMessList = JSON.parseArray(packMessJson, OrderPackMess.class);
		Map<String, Object> resultMap = orderPackService.updateOrderPackMess(packMessList, oemOrderCode,packId);
		return resultMap;
	}
}
