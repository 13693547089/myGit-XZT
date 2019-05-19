package com.faujor.service.bam.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.cluster.mdm.ClusterSuppMapper;
import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.common.AsyncLogMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.dao.sapcenter.bam.OrderMapper1;
import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderEnclosure;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderMate1;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.OrderRele1;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.common.AsyncContentLogDO;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.privileges.UserDO;
//import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.bam.OrderService;
import com.faujor.service.bam.ReceiveService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import oracle.sql.BLOB;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderMapper1 orderMapper1;
	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private AsyncLogService asyncLogService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private AsyncLogMapper asyncLogMapper;
	@Autowired
	private OrgService orgService;
	@Autowired
	private ClusterSuppMapper clusterSuppMapper;

	double purcNumb = 0.000;
	double overNumb = 0.000;
	String deliState = "";

	@Override
	public Map<String, Object> queryReleaseListByPage(Map<String, Object> map) {
		SysUserDO user = UserCommon.getUser();
		//List<QualSupp> list2 = qualSuppMapper.queryAllQualSuppListByUserId(user.getUserId().toString());
		//查询采购部自己的下级（包含自己）
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);//（包含自己）
		// 获取这个管理员下的采购员
		List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userDO", userList);
		List<QualSupp> list2 = qualSuppMapper.queryQualSuppListByUserDOList(param);
		String suppNameConditon = "";
		for (int i = 0; i < list2.size(); i++) {
			QualSupp entity = list2.get(i);
			suppNameConditon += entity.getSapId() + ",";
		}
		if (!suppNameConditon.equals("")) {
			suppNameConditon = "'" + suppNameConditon.replace(",", "','") + "'";

			// suppNameConditon = suppNameConditon.substring(0,
			// suppNameConditon.length() - 1);
			map.put("suppNames", suppNameConditon);
		}
		
		List<OrderRele> list = orderMapper.queryReleaseListByPage(map);
		int count = orderMapper.queryReleaseListByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		page.put("msg", "");
		return page;
	}

	@Override
	public Map<String, Object> queryReleaseListByPageSupp(Map<String, Object> map) {
		SysUserDO user = UserCommon.getUser();
		String suppCode = user.getSuppNo();
		map.put("supplierCode", suppCode);
		List<OrderRele> list = orderMapper.queryReleaseListByPageSupp(map);
		int count = orderMapper.queryReleaseListByPageCountSupp(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		page.put("msg", "");
		return page;
	}

	@Override
	public OrderRele queryOrderReleByFid(final String fid) {
		String Fid = orderMapper.getOrderFid(fid);
		OrderRele order = orderMapper.queryOrderReleByFid(Fid);
		return order;
	}

	/**
	 * 修改订单状态
	 */
	@Override
	public void updateLatentSuppBySuppId(List<OrderEnclosure> list, String type, OrderRele orderRele) {
		if ("Purchase".equals(type)) {
			//采购员发布订单
			OrderRele order = orderMapper.updatePurchaseOrderId(orderRele);
		}/*else {
			//供应商提交订单
			//type = "order";
			orderMapper.updateLimitThanOfOrder(orderRele);
		}*/
		for (OrderEnclosure orderEnclosure : list) {
			if (orderEnclosure.getFid() == null) {
				orderEnclosure.setMainId(orderRele.getFid());
				orderMapper.insertOrder(orderEnclosure);
			}
		}
	}

	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	public void setDeleteEncl(final String docId) {

		orderMapper.setDeleteEncl(docId);

	}

	@Override
	public List<OrderRele> queryOrderReleOfQualSupp(String sapId) {
		return orderMapper.queryOrderReleOfQualSupp(sapId);
	}

	@Override
	public List<OrderMate> queryOrderMateByContOrdeNumb(String contOrdeNumb) {
		return orderMapper.queryOrderMateByContOrdeNumb(contOrdeNumb);
	}

	@Override
	public OrderRele querycontOrdeNumbOfOrderReleBySapIdAndMateCode(String sapId, String mateCode, Integer num) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sapId", sapId);
		param.put("mateCode", mateCode);
		param.put("suppRange", "");//供应商子范围编码
		param.put("orderNum", num);
		OrderRele orderRele = orderMapper.querycontOrdeNumbOfOrderReleBySapIdAndMateCode(param);
		return orderRele;
	}

	@Override
	public List<OrderDO> queryAllOrderOfMate(QualSupp supp, String mateCode) {
		// List<OrderDO> orderList =
		// orderMapper.queryAllOrderOfMate(supp.getSapId(), mateCode);
		/*
		 * for (OrderDO o : orderList) { String unpaQuan =
		 * o.getUnpaQuan();//未交数量 int unpa = Integer.parseInt(unpaQuan);
		 * Map<String, Object> map =
		 * deliService.judgeUpdateMateUnpaNumber(supp.getSuppId(), mateCode,
		 * o.getContOrdeNumb(),unpa); int count =
		 * (int)map.get("count");//送货单中的送货数量 int unpa2 = unpa -count; if(unpa2 <
		 * 0){ unpa2 =0; } o.setUnpaQuan(Integer.toString(unpa2)); }
		 */

		return null;
	}

	@Override
	public Double getUnpaidNum(Map<String, Object> map) {
		Double unpaidNum = orderMapper.getUnpaidNum(map);
		return unpaidNum;
	}

	@Override
	public Map<String, Object> queryOrderReleOfQualSuppByPage(Map<String, Object> map) {
		List<OrderRele> list = orderMapper.queryOrderReleOfQualSuppByPage(map);
		int count = orderMapper.queryOrderReleOfQualSuppByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		page.put("msg", "");
		return page;
	}

	@Override
	public void setPaperDataFallback(String fid) {
		orderMapper.setPaperDataFallback(fid);
	}

	/**
	 * 订单确认setPaperDataConfirm
	 *
	 */
	@Override
	public void setPaperDataConfirm(String fid,String limitThan) {
		orderMapper.setPaperDataConfirm(fid,limitThan);

	}

	@Override
	public OrderRele queryOrderReleByFid2(String fid) {
		OrderRele order = orderMapper.queryOrderReleByFid(fid);
		return order;
	}

	@Override
	public boolean updateOrderStatusByfid(Map<String, Object> map) {
		int i = orderMapper.updateOrderStatusByfid(map);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 订单修改已交未交数量
	 *
	 */
	@Override
	public boolean updateLineProject(List<ReceMate> receMate) {
		int count = 0;
		for (ReceMate r : receMate) {

			final OrderMate mate = orderMapper.selectLineProject(r);
			Double receNumber = r.getReceNumber();
			Double receNumber2 = receNumber;
			Double quanMate = mate.getQuanMate();
			if (quanMate == null) {
				quanMate = 0.000;
			}
			Double unpaQuan = mate.getUnpaQuan();
			if (unpaQuan == null) {
				unpaQuan = 0.000;
			}
			Double int1 = quanMate;
			Double int2 = unpaQuan;
			Double a = int1 + receNumber2;
			Double b = int2 - receNumber2;
			Double stra = a;
			Double strb = b;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stra", stra);
			map.put("strb", strb);
			map.put("r", r);
			int i = 0;
			i = orderMapper.updateLineProject(map);
			count += i;
		}
		if (count == receMate.size()) {
			return true;
		} else {
			return false;
		}
	}

	// 数据同步

	@Override
	public int asyncPurchaseOrder(String orderNo, String asyncDate, AsyncLog al) {
		int asyNum = 0;
		String asyncName = al.getAsyncUserName();
		if ("martian".equals(orderNo)) {
			asyNum = asyncAllPurchaseOrder(asyncName);
		} else if ("yesterday".equals(orderNo)) {
			asyNum = asyncPurchaseOrderSchedule(al);
		} else if ("InboundDelivery".equals(orderNo)) {
			// 更新内向交货单信息
			asyNum = receiveService.idenToReceive(al);
		} else if ("fighting".equals(orderNo)) {
			// 同步所有有采购订单但是没有行项目的采购订单
//			List<String> list = orderMapper.findNoMateOrder();
			List<OrderRele> list = orderMapper.findNoMateOrder2();
			for (OrderRele order : list) {
				String sapId = order.getSuppName();
				// 根据合同编号查询字表-------------查询订单行项目
				List<OrderMate1> selectTopDetail = orderMapper1.SelectTopDetail(order.getContOrdeNumb());
				if (selectTopDetail.size() > 0) {
					// 插入字表数据
					for (OrderMate1 orderMate1 : selectTopDetail) {
						String suppRange = orderMate1.getSuppRange();//供应商子范围编码
						//根据供应商编码和供应商子范围编码查询供应商子范围描述
						String suppRangeDesc = clusterSuppMapper.getSuppRangeDescBySapIdAndSuppRange(sapId, suppRange);
						orderMate1.setSuppRangeDesc(suppRangeDesc);
						orderMate1 = dealOrderMate(orderMate1);
						saveAsyncContent(null, orderMate1, asyncName, "新增");
					}
					//批量插入订单行项目-------------
					asyNum += orderMapper.batchSaveTopDetails(selectTopDetail);
				} else {
					OrderRele1 or1 = new OrderRele1();
					or1.setContOrdeNumb(orderNo);
					saveAsyncContent(or1, null, asyncName, "移除");
					asyNum += orderMapper.removeOrderByOrderNO(orderNo);
				}
			}
		} else {
			if (!StringUtils.isEmpty(asyncDate)) {
				asyncDate = asyncDate.replace("-", "");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orderNo", orderNo);
			params.put("asyncDate", asyncDate);
			// 根据采购订单号或者 EKPO--AEDAT和EKBE--BUDAT等于这个日期的
			List<OrderRele1> selectTopList = orderMapper1.findTopOrderByOrderNo(params);
			for (OrderRele1 orderRele1 : selectTopList) {
				// 处理主表数据
				orderRele1 = dealOrderRele(orderRele1);
				// 体积
				double mateVolum = 0D;
				String fid = orderRele1.getFid();
				String sapId = orderRele1.getSuppName();
				// 根据合同编号查询字表-------------查询订单行项目
				List<OrderMate1> selectTopDetail = orderMapper1.SelectTopDetail(orderRele1.getContOrdeNumb());
				// 根据合同编号数据库删除字表
				orderMapper.deleteMateList(orderRele1.getContOrdeNumb());
				saveAsyncContent(orderRele1, null, asyncName, "移除行项目");
				if (selectTopDetail.size() > 0) {
					// 插入字表数据
					purcNumb = 0.000;
					overNumb = 0.000;
					for (OrderMate1 orderMate1 : selectTopDetail) {
						String suppRange = orderMate1.getSuppRange();//供应商子范围编码
						//根据供应商编码和供应商子范围编码查询供应商子范围描述
						String suppRangeDesc = clusterSuppMapper.getSuppRangeDescBySapIdAndSuppRange(sapId, suppRange);
						orderMate1.setSuppRangeDesc(suppRangeDesc);
						mateVolum += orderMate1.getMateVolum();
						orderMate1 = dealOrderMate(orderMate1);
						//插入订单行项目数据------------------
						orderMapper.setTopDetail(orderMate1);
						saveAsyncContent(null, orderMate1, asyncName, "新增");
					}
					// 体积
					orderRele1.setMateVolum(mateVolum);
					orderRele1 = dealOrderRele1(orderRele1);
					if (StringUtils.isEmpty(fid)) {
						asyNum += orderMapper.setTopList(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "新增");
					} else {
						asyNum += orderMapper.updateTopOrderRele(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "变更");
					}
				} else {
					asyNum += orderMapper.removeOrderByOrderNO(orderNo);
					saveAsyncContent(orderRele1, null, asyncName, "移除");
				}
			}
		}
		al.setAsyncNum(asyNum);
		al.setAsyncStatus("同步成功");
		asyncLogService.updateAsyncLog(al);
		return asyNum;
	}

	@Override
	public int asyncPurchaseOrderSchedule(AsyncLog al) {
		// 查出前天和昨天的数据
		Date date = new Date();
		Date yd = getDateBefore(date, 1);// 1天前，昨天
		String ydStr = DateUtils.format(yd, "yyyyMMdd");
		Date yyd = getDateBefore(date, 2);
		String yydStr = DateUtils.format(yyd, "yyyyMMdd");
		// 查询出昨天和前天
		int i = 0;
		String asyncName = al.getAsyncUserName();
		// 查询出EKKO的AEDAT和EKBE的BUDAT在这个范围内的数据
		List<OrderRele1> list = orderMapper1.findPurchaseOrderSchedule(ydStr, yydStr);
		if (list.size() > 0) {
			for (OrderRele1 orderRele1 : list) {
				double mateVolum = 0D;
				orderRele1 = dealOrderRele(orderRele1);
				String sapId = orderRele1.getSuppName();//供应商编码
				String orderNo = orderRele1.getContOrdeNumb();
				String fid = orderRele1.getFid();
				//查询订单所有行项目物料数据------------------查询T_SAP_EKPO表的LTSNR（供应商子范围编码）
				List<OrderMate1> mateList = orderMapper1.findTopOrderDetailsByOrderNo(orderNo);
				orderMapper.deleteMateList(orderNo);
				saveAsyncContent(orderRele1, null, asyncName, "移除行项目");
				if (mateList.size() > 0) {
					purcNumb = 0.000;
					overNumb = 0.000;
					for (OrderMate1 orderMate : mateList) {
						String suppRange = orderMate.getSuppRange();//供应商子范围编码
						//根据供应商编码和供应商子范围编码查询供应商子范围描述
						String suppRangeDesc = clusterSuppMapper.getSuppRangeDescBySapIdAndSuppRange(sapId, suppRange);
						orderMate.setSuppRangeDesc(suppRangeDesc);
						// 累加体积
						mateVolum += orderMate.getMateVolum();
						orderMate = dealOrderMate(orderMate);
						saveAsyncContent(null, orderMate, asyncName, "新增");
					}
					// 判断保存还是update
					orderRele1.setMateVolum(mateVolum);
					orderRele1 = dealOrderRele1(orderRele1);
					if (StringUtils.isEmpty(fid)) {
						i += orderMapper.setTopList(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "新增");
					} else {
						i += orderMapper.updateTopOrderRele(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "变更");
					}
					//批量插入采购订单从表数据------------------
					orderMapper.batchSaveTopDetails(mateList);
				} else {
					i += orderMapper.removeOrderByOrderNO(orderNo);
					saveAsyncContent(orderRele1, null, asyncName, "移除");
				}
			}
		}
		al.setAsyncNum(i);
		al.setAsyncStatus("同步成功");
		asyncLogService.updateAsyncLog(al);
		return i;
	}

	public int asyncAllPurchaseOrder(String asyncName) {
		// 脱普数据库数据
		List<String> selectEkpoType = orderMapper1.SelectEkpoType();
		int i = 0;
		for (String str : selectEkpoType) {
			//查询订单主数据
			List<OrderRele1> selectTopList = orderMapper1.SelectTopListFirst(str);
			for (OrderRele1 orderRele1 : selectTopList) {
				// 处理主表数据
				String orderNo = orderRele1.getContOrdeNumb();
				orderRele1 = dealOrderRele(orderRele1);
				String sapId = orderRele1.getSuppName();
				//查询订单物料从表数据--------------------
				List<OrderMate1> selectTopDetail = orderMapper1.findTopOrderDetailsByOrderNo(orderNo);
				// 先删除已经存在
				orderMapper.deleteMateList(orderNo);
				saveAsyncContent(orderRele1, null, asyncName, "移除行项目");
				double mateVolum = 0D;
				if (selectTopDetail.size() > 0) {
					purcNumb = 0.000;
					overNumb = 0.000;
					for (OrderMate1 std : selectTopDetail) {
						String suppRange = std.getSuppRange();//供应商子范围编码
						//根据供应商编码和供应商子范围编码查询供应商子范围描述
						String suppRangeDesc = clusterSuppMapper.getSuppRangeDescBySapIdAndSuppRange(sapId, suppRange);
						std.setSuppRangeDesc(suppRangeDesc);
						// 累加体积
						mateVolum += std.getMateVolum();
						std = dealOrderMate(std);
						saveAsyncContent(null, std, asyncName, "新增");
					}
					//批量插入订单从表数据----------------------------
					orderMapper.batchSaveTopDetails(selectTopDetail);
					// 体积
					orderRele1.setMateVolum(mateVolum);
					orderRele1 = dealOrderRele1(orderRele1);

					String fid = orderRele1.getFid();
					if (StringUtils.isEmpty(fid)) {
						i += orderMapper.setTopList(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "新增");
					} else {
						i += orderMapper.updateTopOrderRele(orderRele1);
						saveAsyncContent(orderRele1, null, asyncName, "变更");
					}
				} else {
					i += orderMapper.removeOrderByOrderNO(orderNo);
					saveAsyncContent(orderRele1, null, asyncName, "移除");
				}
			}
		}
		return i;
	}

	/**
	 * 处理采购订单主表数据
	 *
	 * @param order
	 * @return
	 */
	public OrderRele1 dealOrderRele(OrderRele1 order) {
		// 取供应商编码（Z002 Z006 Z008 取reswk；Z001 Z009 取lifnr）
		// 缺少调拨单的方量计算值
		String bsart = order.getBSART();
		String suppNo = "";
		if (!StringUtils.isEmpty(bsart)) {
			if ("Z002,Z006,Z008".contains(bsart)) {
				suppNo = order.getReswk();
			} else {
				suppNo = order.getLifnr();
			}
		}
		String fid = orderMapper.queryContOrdeNumb(order.getContOrdeNumb());
		// 获取供应商名称
		QualSupp supp = qualSuppMapper.queryQualSuppBySapId(suppNo);
		if (supp != null) {
			order.setSuppName(suppNo);
			order.setSuppNumb(supp.getSuppName());
		}
		if (!StringUtils.isEmpty(fid)) {
			order.setFid(fid);
		}
		return order;
	}

	/**
	 * 处理采购订单行项目数据
	 *
	 * @param mate
	 * @return
	 */
	public OrderMate1 dealOrderMate(OrderMate1 orderMate) {
		String uuid = UUIDUtil.getUUID();
		orderMate.setFid(uuid);
		List<Material> list = materialMapper.queryManyMaterialByMateCode(orderMate.getMateNumb());
		if (list.size() != 0) {
			Material mate = list.get(0);
			orderMate.setProdName(mate.getSkuEnglish());
			orderMate.setBoxEntrNumb(mate.getBoxNumber());
			Double QuanMate = orderMate.getZwmenge();
			Double unpaQuan = orderMate.getMenge();
			String elikz = orderMate.getElikz();
			String purcNumb1 = orderMate.getUnitPric();
			String purcNumbFirst = purcNumb1.substring(0, 1);
			if (purcNumbFirst.equals(".")) {
				purcNumb1 = "0" + purcNumb1;
				orderMate.setUnitPric(purcNumb1);
			}
			// 获取单个物料含税价
			BigDecimal taxAmou = new BigDecimal(orderMate.getTaxAmou());
			BigDecimal purcQuan1 = new BigDecimal(orderMate.getPurcQuan());
			int r = purcQuan1.compareTo(BigDecimal.ZERO);
			if (r != 0) {
				BigDecimal mateTax = taxAmou.divide(purcQuan1, 3, BigDecimal.ROUND_HALF_UP);
				orderMate.setMateTax(mateTax.doubleValue());
			} else {
				orderMate.setMateTax(0D);
			}

			if (elikz.equals("X")) {
				orderMate.setMenge(0.000);
			} else {
				if (QuanMate == null && unpaQuan == null) {
					Double purcQuan = orderMate.getPurcQuan();
					orderMate.setMenge(purcQuan);
					orderMate.setZwmenge(0.000);
				}
			}
			if (orderMate.getPurcQuan() != null) {
				purcNumb += orderMate.getPurcQuan();
			}
			if (orderMate.getZwmenge() != null) {
				overNumb += orderMate.getZwmenge();
			}
			if (!"未结案".equals(deliState)) {
				if ("".equals(orderMate.getElikz()) || " ".equals(orderMate.getElikz())) {
					deliState = "未结案";
				}
				if ("X".equals(elikz)) {
					deliState = "已结案";
				}
			}

		}
		return orderMate;
	}

	public OrderRele1 dealOrderRele1(OrderRele1 order) {
		String releNumb = overNumb + "/" + purcNumb;
		if (overNumb != 0) {
			order.setDeliType("已交");
		} else if (overNumb == 0) {
			order.setDeliType("未交");
		}
		if ("已结案".equals(deliState)) {
			order.setDeliType("已结案");
		}
		order.setReleNumb(releNumb);
		return order;
	}

	public void saveAsyncContent(OrderRele1 order, OrderMate1 mate, String asyncName, String operate) {
		AsyncContentLogDO content = new AsyncContentLogDO();
		String id = UUIDUtil.getUUID();
		content.setId(id);
		content.setAsyncName(asyncName);
		content.setAsyncTime(new Date());
		content.setAsyncOperate(operate);
		String json = "";
		if (order != null) {
			content.setAsyncType("order-main");
			content.setAsyncCode(order.getContOrdeNumb());
			json = JsonUtils.beanToJson(order);
		} else {
			content.setAsyncType("order-details");
			content.setAsyncCode(mate.getMainId());
			content.setAsyncCode2(mate.getFrequency());
			json = JsonUtils.beanToJson(mate);
		}
		int length = json.length();
		if (length <= 2000) {
			content.setAsyncContentStr(json);
			asyncLogMapper.saveAsyncContent(content);
		} else {
			asyncLogMapper.saveAsyncContent(content);
			AsyncContentLogDO c = asyncLogMapper.findAsyncContentById(id);
			BLOB object = (BLOB) c.getAsyncContent();
			FileInputStream fis = null;
			OutputStream ops = null;
			try {
				// ops = blobColumn.getBinaryOutputStream();//暂时使用这个废弃的方法
				ops = object.setBinaryStream(0);// ojdbc14支持,ojdbc6,5都不支持
				// fis = new FileInputStream(file);
				byte[] byteArray = null;
				byteArray = json.getBytes();
				// data = FileCopyUtils.copyToByteArray(fis);
				ops.write(byteArray);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
					if (ops != null) {
						ops.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public OrderRele findOrderReleByOrderNo(String orderNo) {
		OrderRele orderRele = new OrderRele();
		orderRele.setContOrdeNumb(orderNo);
		return orderMapper.findOrderReleByOrderRele(orderRele);
	}

	@Override
	public List<OrderMate> findOrderMateByOrderNo(String orderNo) {

		return orderMapper.findOrderMateByOrderNo(orderNo);
	}

	@Override
	public List<OrderEnclosure> findOrderEnclosureByOrderNo(String orderNo) {
		return orderMapper.findOrderEnclosureByOrderNo(orderNo);
	}

	@Override
	public OrderRele findOrderReleByID(String id) {
		OrderRele orderRele = new OrderRele();
		orderRele.setFid(id);
		return orderMapper.findOrderReleByOrderRele(orderRele);
	}

	@Override
	public List<OrderRele> exportOrderInfo(OrderReleDO order) {
		SysUserDO user = UserCommon.getUser();
		if(!"supplier".equals(user.getUserType())) {
			//查询采购部自己的下级（包含自己）
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ownId", user.getUserId());
			paramMap.put("orgCode", "PURCHAROR");
			paramMap.put("isContainOwn", true);//（包含自己）
			// 获取这个管理员下的采购员
			List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userDO", userList);
			List<QualSupp> list2 = qualSuppMapper.queryQualSuppListByUserDOList(param);
			String suppNameConditon = "";
			for (int i = 0; i < list2.size(); i++) {
				QualSupp entity = list2.get(i);
				suppNameConditon += entity.getSapId() + ",";
			}
			if (!suppNameConditon.equals("")) {
				suppNameConditon = "'" + suppNameConditon.replace(",", "','") + "'";
				
				// suppNameConditon = suppNameConditon.substring(0,
				// suppNameConditon.length() - 1);
//				map.put("suppNames", suppNameConditon);
				order.setRemarks(suppNameConditon);
			}
		}
		List<OrderRele> list = orderMapper.findOrderReleByOrderReleDO(order);
		return list;
	}

	@Override
	public List<String> queryOrderMessListByOrderTypeAndSapId(String orderType, String sapId) {
		
		return orderMapper.queryOrderMessListByOrderTypeAndSapId(orderType,sapId);
	}

	@Override
	public boolean updateLimitThanOfOrderByFid(OrderRele orderRele) {
		int i = orderMapper.updateLimitThanOfOrder(orderRele);
		if(i==1) {
			return true;
		}else {
			return false;
		}
	}
}
