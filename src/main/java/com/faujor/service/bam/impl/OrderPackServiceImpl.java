package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.faujor.entity.bam.*;
import com.faujor.service.document.TemplateService;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.OrderPackMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.OrderPackService;
import com.faujor.utils.UserCommon;

import javax.transaction.Transactional;

@Service(value="orderPackService")
public class OrderPackServiceImpl implements OrderPackService {

	@Autowired
	private OrderPackMapper orderPackMapper;

	@Autowired
	private QualSuppMapper qualSuppMapper;

	@Autowired
	private TemplateService templateService;

	@Override
	public Map<String, Object> queryOrderPackByPage(Map<String, Object> map) {
		SysUserDO user = UserCommon.getUser();
		String userType = user.getUserType();
		String userId = user.getUserId().toString();
		map.put("userId", userId);
		if(!"supplier".equals(userType)) {
			//采购员
			//查询货源中对应的供应商
			List<QualSupp> qualSuppList = qualSuppMapper.queryAllQualSuppListByUserId(userId);
			if(qualSuppList.size()>0) {
				map.put("qualSuppList", qualSuppList);
			}
		}
		List<OrderPackVO> list = orderPackMapper.queryOrderPackByPage(map);
		int rows = orderPackMapper.queryOrderPackByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", rows);
		return page;
	}
	@Override
	public Map<String, Object> queryOrderPackMessage(String oemOrderCode) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//根据订单编号查询采购订信息
		OrderPackVO orderPackVO = orderPackMapper.queryOrderMessageByOEMOrderCode(oemOrderCode);
		//根据订单编号查询订单
		List<OrderMate> orderMateList = orderPackMapper.queryOrderMateByContOrdeNumb(oemOrderCode);
		//根据订单编号查询包材信息（用于创建页面的下拉过滤）
		List<OrderPackMess> baocaiInfoList = orderPackMapper.queryBaocaiInfoByContOrdeNumb(oemOrderCode);
		//根据订单编号查询包材信息表数据（用于查看面的数据过滤）
		List<OrderPackMess> baocaiInfo2List = orderPackMapper.queryBaocaiInfo2ByContOrdeNumb(oemOrderCode);
		//根据订单编号查询包材订单信息（用于查看面的数据过滤）
		List<OrderPackMate> baocaiOrderDetailList = orderPackMapper.queryBaocaiOrderDetailByContOrdeNumb(oemOrderCode);
		//计算小计(mone)，合计，税额(=合计-小计)
		List caculateList = calculateTotal(orderMateList);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderPackVO", orderPackVO);
		map.put("orderMateList", orderMateList);
		map.put("baocaiInfoList", baocaiInfoList);
		map.put("caculateList", caculateList);
		map.put("baocaiInfo2List", baocaiInfo2List);
		map.put("baocaiOrderDetailList", baocaiOrderDetailList);
		map.put("subeDate", sdf.format(orderPackVO.getSubeDate()));
//		SysUserDO user = UserCommon.getUser();
//		map.put("userId", user.getUserId().toString());
		return map;
	}

	@Override
	public List<OrderMate> queryMateNumbByOrderCode(String oemOrderCode) {
		//根据订单编号查询料号
		List<OrderMate> orderMateList = orderPackMapper.queryMateNumbByOrderCode(oemOrderCode);
		return orderMateList;
	}

	@Override
	public String queryMateNameByMateNumb(String mateCode) {
		//根据订单编号查询料号
		String mateName = orderPackMapper.queryMateNameByMateNumb(mateCode);
		return mateName;
	}

    @Override
    public RestCode saveOrderPackMate(List<OrderPackForm> orderPackFormList, List<OrderPackMess> packMessList, List<OrderPackMate> packMateList) {
		if (orderPackFormList.size() > 0) {
			orderPackMapper.saveOrderPackForm(orderPackFormList);
		}
		if (packMessList.size() > 0) {
			orderPackMapper.saveOrderPackMess(packMessList);
		}
		if (packMateList.size() > 0) {
			orderPackMapper.saveOrderPackMate(packMateList);
		}
        return new RestCode();
    }

	@Override
	public RestCode saveOrderPackMateByEdit(String orderPackID, List<OrderPackMate> packMateList,List<OrderPackMess> packMessList) {
		List<OrderPackMate> baocaiOrderDetailList = orderPackMapper.queryOrderPackMateByOrderPackID(orderPackID);
		String[] ids=new String[baocaiOrderDetailList.size()];
		List<String> removeIds=new ArrayList();
		if (packMateList.size() > 0) {
            for (int i = 0; i < baocaiOrderDetailList.size(); i++) {//找出要删除的数据并给新数据赋值ID
                OrderPackMate old = baocaiOrderDetailList.get(i);
                String oldID = old.getId();
                boolean flag = true;
				for (int j = 0; j < packMateList.size(); j++) {
					OrderPackMate newOrderPackMate = packMateList.get(j);
					String newID = newOrderPackMate.getId();
					if (newID != null && !"".equals(newID) && !"null".equalsIgnoreCase(newID)) {
						if (newID.equals(oldID)) {
							flag = false;
							removeIds.add(newID);
						}
					} else {
						newOrderPackMate.setId(UUIDUtil.getUUID());
						newOrderPackMate.setOrderPackId(orderPackID);
					}
				}
				if (flag) {
					ids[i] = (oldID);
				}
            }
			for (int i = 0; i < removeIds.size(); i++) {//排除并更新旧数据
            	Iterator<OrderPackMate> it = packMateList.iterator();
				while(it.hasNext()){
					OrderPackMate orderPackMate = it.next();
					String newId = orderPackMate.getId();
					if(newId.equals(removeIds.get(i))){
						//更新旧数据
						orderPackMapper.updateOrderPackMate(orderPackMate);
						it.remove();
					}
				}
			}
            if (ids.length > 0) {
			    orderPackMapper.deleteOrderPackMateByMainId(ids);
            }
			//更新包材信息数据
			for (int i = 0; i < packMessList.size(); i++) {
				orderPackMapper.updateOrderMessOrderedAndNoOrderedNum(packMessList.get(i).getId(),
						packMessList.get(i).getPackTotalNum(),
						packMessList.get(i).getPlacedNum(),
						packMessList.get(i).getResidueNum());
			}
			if (packMateList.size() > 0) {
				orderPackMapper.saveOrderPackMate(packMateList);
			}
			SysUserDO user = UserCommon.getUser();
			String userId = user.getUserId().toString();
			String userName = user.getUsername();
			//记录更新人和更新时间
			orderPackMapper.updateOrderParckUserTime(userId,userName,orderPackID);
		}
		return new RestCode();
	}

	private List calculateTotal(List<OrderMate> orderMateList) {
		List list = new ArrayList();
		BigDecimal mone = new BigDecimal(0);
		BigDecimal taxAmou = new BigDecimal(0);
		for (int i = 0; i < orderMateList.size(); i++) {
			BigDecimal moneIn = new BigDecimal(orderMateList.get(i).getMone());
			mone = mone.add(moneIn);
			BigDecimal taxAmouIn = new BigDecimal(orderMateList.get(i).getTaxAmou());
			taxAmou = taxAmou.add(taxAmouIn);

			System.out.println("moneIn" + i + " : " + moneIn);
			System.out.println("taxAmouIn" + i + " : " + taxAmouIn);
		}
		System.out.println("mone " + " : " + mone);
		System.out.println("taxAmou " + " : " + taxAmou);

		BigDecimal a = taxAmou.multiply(new BigDecimal(100));
		BigDecimal b = mone.multiply(new BigDecimal(100));
		BigDecimal c = a.subtract(b);
		BigDecimal tax = c.divide(new BigDecimal(100));
		System.out.println("tax " + " : " + tax);

		list.add(mone);
		list.add(taxAmou);
		list.add(tax);
		return list;
	}

	@Override
	@Transactional
	public boolean deleteOrderPackById(String[] ids) {
		int k = orderPackMapper.deleteOrderPackMessById(ids);
        Map<String, Object> map = new HashMap<String,Object>();
        List<OrderPackMate> list  =orderPackMapper.queryALLOrderPackByPackIds(ids);
        List<String> idList = new ArrayList<String>();
        for (int i = 0;i < list.size();i ++){
            String cceFileId = list.get(i).getAcceFileId();
            if (cceFileId != null){
                idList.add(cceFileId);
            }
        }
        //删除文档
		RestCode restCode = templateService.deleteFile(idList);
		int j = orderPackMapper.deleteOrderPackMateById(ids);
		int i = orderPackMapper.deleteOrderPackById(ids);
		if (i == ids.length) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改 产能上报 主表 的状态
	 * @param status
	 * @param oemOrderCode
	 * @return
	 */
	@Override
	@Transactional
	public int updateOrderStatus(String status, String oemOrderCode) {
		return orderPackMapper.updateOrderStatus(status, oemOrderCode);
	}

	@Override
	public List<QualSupp> queryAllQualSuppListByUserId(String userId) {
		List<QualSupp> qualSuppList = qualSuppMapper.queryAllQualSuppListByUserId(userId);
		return qualSuppList;
	}

	@Override
	public List<OrderPackForm> queryOrderPackFormByCode(String oemOrderCode) {
		List<OrderPackForm> orderPackFormList = orderPackMapper.queryOrderPackFormByCode(oemOrderCode);
		return orderPackFormList;
	}

	@Override
	public List<String> queryOrderMessListByOrderTypeAndSapId(String orderType, String sapId) {

		return orderPackMapper.queryOrderMessListByOrderTypeAndSapId(orderType,sapId);
	}

    @Override
    @Transactional
    public Map<String, Object> updateOrderPackMess(List<OrderPackMess> packMessList, String oemOrderCode,String packId) {
    	//包材信息表数据
    	List<OrderPackMess> baocaiInfoList = orderPackMapper.queryBaocaiInfoByContOrdeNumb(oemOrderCode);
    	Map<String, Object> result = new HashMap<String, Object>();
    	if(baocaiInfoList.size()==0) {
    		result.put("judge", false);
    		result.put("msg", "包材信息列表数据为空，更新失败");
    		return result;
    	}
		Map<String, Double> map = new HashMap<String, Double>();
		for (int j = 0; j < packMessList.size(); j++) {
			OrderPackMess orderPackMess = packMessList.get(j);
			String mateCode = orderPackMess.getMateCode();
			String packCode = orderPackMess.getPackCode();
			Double placedNum = orderPackMess.getPlacedNum();//已下单数量
			String key = mateCode +"_"+packCode;//物料编码+包材编码作为key值，value值为：已下单数据
			map.put(key, placedNum);
		}
		for (int i = 0; i < baocaiInfoList.size(); i++) {
			String mateCode = baocaiInfoList.get(i).getMateCode();
			String packCode = baocaiInfoList.get(i).getPackCode();
			Double packTotalNum = baocaiInfoList.get(i).getPackTotalNum();
			String key = mateCode +"_"+packCode;
			Double placedNum = map.get(key);//获取已下单数据
			if(placedNum == null) {
				placedNum = 0D;
			}
			baocaiInfoList.get(i).setPlacedNum(placedNum);
			BigDecimal residueNum = BigDecimal.valueOf(packTotalNum).subtract(BigDecimal.valueOf(placedNum));//包材总量-已下单数量=剩余可下单数量
			BigDecimal zero = new BigDecimal(0);
			int k = residueNum.compareTo(zero);
			if(k<0) {//剩余可下单数量小于0
				result.put("judge", false);
	    		result.put("msg", "当前包材订单数量超过剩余可下单数量，更新失败");
	    		return result;
			}
			baocaiInfoList.get(i).setResidueNum(residueNum.doubleValue());
			baocaiInfoList.get(i).setId(UUIDUtil.getUUID());
			baocaiInfoList.get(i).setOrderPackId(packId);
		}
		//根据订单编号查询采购订信息
		OrderPackVO vo = orderPackMapper.queryOrderMessageByOEMOrderCode(oemOrderCode);
		//根据订单编号查询订单
		List<OrderMate> orderMateList = orderPackMapper.queryOrderMateByContOrdeNumb(oemOrderCode);
		//计算小计(mone)，合计，税额(=合计-小计)
		List caculateList = calculateTotal(orderMateList);
		BigDecimal limitThan = vo.getLimitThan();
		//修改包材采购订单的下单限比
		orderPackMapper.updateOrderPackLimitThan(packId,limitThan);
		String[] ids = {packId};
		//删除旧的包材信息列表数据
		orderPackMapper.deleteOrderPackMessById(ids);
		//添加最新的包材信息列表数据
		orderPackMapper.saveOrderPackMess(baocaiInfoList);
		result.put("baocaiInfoList", baocaiInfoList);
		result.put("orderPackVO", vo);
		result.put("orderMateList", orderMateList);
		result.put("caculateList", caculateList);
		result.put("judge", true);
        return result;
    }

	@Override
	public OrderPackVO queryOrderMessageByOEMOrderCode(String oemOrderCode) {
		OrderPackVO vo = orderPackMapper.queryOrderMessageByOEMOrderCode(oemOrderCode);
		return vo;
	}

	@Override
	public OrderPackVO queryOrderPackById(String id) {
		OrderPackVO vo = orderPackMapper.queryOrderPackById(id);
		return vo;
	}


}
