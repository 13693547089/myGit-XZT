package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.bam.AppointMapper;
import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.bam.StraMessageMapper;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.StraMessAndMateDO;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.mdm.QualSuppService;

@Service("orderMateCheckServiceImpl")
public class OrderMateCheckServiceImpl implements OrderMateCheckService {

	@Autowired
	private DeliveryMapper deliveryMapper;
	@Autowired
	private AppointMapper appointMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private StraMessageMapper starMessMapper;
	@Autowired
	private QualSuppService qualSuppService;

	//在计算可用量的时候用到了供应商子范围
	@Override
	public StraMessAndMateDO calculateActualOrderNumber(String orderNo, String mateCode, String suppRange,Double unpaNumber) {
		StraMessAndMateDO result = new StraMessAndMateDO();
		// 锁定直发通知单的数量
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("mateCode", mateCode);
		params.put("suppRange", suppRange);
		result.setOrderNo(orderNo);
		result.setMateCode(mateCode);
		result.setUnpaNumber(unpaNumber);
		BigDecimal unpa = new BigDecimal(unpaNumber);
		List<StraMessAndMateDO> list = starMessMapper.findOccupyNumberByParams(params);
		if (list.size() > 0) {
			// A算法
			BigDecimal occ = new BigDecimal(0);
			for (StraMessAndMateDO straMessAndMateDO : list) {
				Double occupyNumber = straMessAndMateDO.getOccupyNumber();
				BigDecimal bigOcc = new BigDecimal(occupyNumber);
				occ = occ.add(bigOcc);
			}
			BigDecimal r = unpa.subtract(occ);
			// int compareTo = r.compareTo(BigDecimal.ZERO);// -1 小于 0 等于 1 大于
			// if (compareTo == -1) {
			// result.setCalculNumber((double) 0);
			// result.setMsg("计算的订单可用量小于零");
			// } else {
			// double d = r.doubleValue();
			// result.setCalculNumber(d);
			// }
			// B算法
			StraMessAndMateDO occupy = list.get(0);
			Double lastUnpaNumber = occupy.getUnpaNumber();// 上一张送货单记录的未交量
			Double lastCalculNumber = occupy.getCalculNumber();
			// 上一张送货单记录的计算了
			Double lastOccupyNumber = occupy.getOccupyNumber();
			// 上一张送货单或者直发通知单的占有量
			BigDecimal bdUnpa = new BigDecimal(unpaNumber);
			BigDecimal bdLastUnpa = new BigDecimal(lastUnpaNumber);
			int i = bdUnpa.compareTo(bdLastUnpa);
			if (i == 0) {
				// 如果这次取到的订单在外量和上一张送货单记录的订单在外量一样，则用上次记录的订单在外量减去送货数量
				double calcul = lastCalculNumber - lastOccupyNumber;
				System.out.println("推荐订单算法:" + r + "~~~~~~~~~~~~~" + calcul);
				result.setCalculNumber(calcul);
			} else {
				int k = 0;
				for (StraMessAndMateDO occu : list) {
					k += occu.getOccupyNumber();
				}
				double calcul = unpaNumber - k;
				if (calcul >= 0) {
					System.out.println("推荐订单算法:" + r + "!!!~~~~~~~~~~!!!" + calcul);
					result.setCalculNumber(calcul);
				} else {
					result.setCalculNumber((double) 0);
					result.setMsg("计算的订单可用量小于零");
				}
			}
		} else {
			result.setCalculNumber(unpaNumber);
		}
		return result;
	}

	@Override
	public Map<String, Object> recommendPurchaseOrder(String mapgCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Appoint appoint = appointMapper.queryAppointByAppoCode(mapgCode);
		if (appoint != null) {
			result.put("appoint", appoint);
			List<AppoMate> list = appoint.getAppoMates();
			if (list.size() > 0) {
				String suppId = appoint.getSuppId();
				String suppRange = appoint.getSuppRange();
				param.put("suppRange", suppRange);
				suppId = suppId.replace("s", "");
				int i = 1000;
				List<DeliMate> deliMates = new ArrayList<DeliMate>();
				String str = "";
				for (AppoMate appoMate : list) {
					String mateCode = appoMate.getMateCode();
					String mateName = appoMate.getMateName();
					Double mateNumber = appoMate.getMateNumber();
					String remark = appoMate.getRemark();
					Double unpa;
					// 获取到最古老的一条采购订单(现在修改为，查询出所有的采购订单，然后循环，直到满足预约数量)
					param.put("sapId", suppId);
					param.put("mateCode", mateCode);
					List<OrderRele> orderList = orderMapper.findOrderListBySapIdAndMateCode(param);
					if (orderList.size() > 0) {
						int j = i;
						int count = 0;
						int total = 0;
						orderLoop: for (OrderRele orderRele : orderList) {
							// 采购订单编码
							String orderNo = orderRele.getContOrdeNumb();
							// 获取采购订单详情
							// OrderMate orderMate =
							// orderMapper.findOrderMateByOrderNoAndMateCode(orderNo,
							// mateCode);
							List<OrderMate> mates = orderRele.getMates();
							if (mates.size() > 0) {
//								OrderMate orderMate = mates.get(0);
								for (OrderMate orderMate : mates) {
									DeliMate dm = new DeliMate();
									total++;
									// 采购订单上的未交数量
									unpa = orderMate.getUnpaQuan();
									StraMessAndMateDO mate = calculateActualOrderNumber(orderNo, mateCode,suppRange, unpa);
									// 通过和历史采购订单比较获取到的未交量
									Double unpaNumber = mate.getUnpaNumber();
									// 通过和历史采购订单比较获取到的计算未交量
									Double calculNumber = mate.getCalculNumber();
									double cal = calculNumber;
									dm.setFrequency(orderMate.getFrequency());
									dm.setUnit(orderMate.getCompany());
									dm.setSort(Double.toString(j++));
									dm.setUnpaNumber(unpaNumber);
									dm.setCalculNumber(calculNumber);
									// 取订单未交量和预约数量的最小值作为送货数量
									BigDecimal bd1 = new BigDecimal(mateNumber);
									BigDecimal bd2 = new BigDecimal(calculNumber);
									int r = bd1.compareTo(bd2);// -1(bd1<bd2);0(bd1=bd2);1(bd1>bd2)
									if (r == -1 || r == 0) {
										// 如果预约的小于等于计算未交量，则取预约的作为送货数量，并结束循环
										dm.setDeliNumber(mateNumber);
										dm.setOrderId(orderRele.getContOrdeNumb());
										dm.setMateCode(mateCode);
										dm.setMateName(mateName);
										dm.setSubeDate(orderRele.getSubeDate());
										dm.setAppoNumber(mateNumber);
										dm.setRemark(remark);
										deliMates.add(dm);
										break orderLoop;
									} else {
										// 如果预约的大于计算未交量，则取计算未交量作为送货数量，并继续循环
										dm.setDeliNumber(calculNumber);
										dm.setOrderId(orderRele.getContOrdeNumb());
										dm.setMateCode(mateCode);
										dm.setMateName(mateName);
										dm.setSubeDate(orderRele.getSubeDate());
										dm.setAppoNumber(mateNumber);
										dm.setRemark(remark);
										if (cal != 0) {
											deliMates.add(dm);
										} else {
											count++;
										}
										mateNumber = mateNumber - calculNumber;
									}

								}
							}
						}
						if (count == total) {
							str += mateCode + ",";
						}
					} else {
						String msg = (String) result.get("msg");
						if (StringUtils.isEmpty(msg)) {
							result.put("msg", mateCode + "未查询到符合条件的采购订单!");
						} else {
							msg += mateCode + "未查询到符合条件的采购订单";
							result.put("msg", msg);
						}
					}
					i += 1000;
				}
				result.put("str", str);
				result.put("deliMates", deliMates);
			} else {
				result.put("msg", "此预约单无预约物料信息！");
			}

		} else {
			result.put("msg", "未查到此预约单");
		}
		return result;
	}

	@Override
	public Map<String, Object> checkOutAppoMate(String suppId, List<String> mates, String oddCode) {
		// 根据查询供应商和物料编码查询送货单（状态为：已发货，已签到，待收货）
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		// 这个供应商的这个物料在送货单（状态为：已发货，已签到，待收货）中已经有了，
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		String str2 = "";
		String str3 = "";
		int count = 0;
		int total = 0;
		List<String> statusList2 = new ArrayList<String>();
		statusList2.add("已保存");
		map.put("statusList", statusList2);
		map.put("suppId", suppId);
		map.put("deliCode", oddCode);// 送货单号
		map.put("messId", oddCode);// 直发通知单主键
		if (supp != null)
			map.put("zzoem", supp.getSapId());
		for (String mateCode : mates) {
			map.put("mateCode", mateCode);
			// 查询已保存的送货单
			List<DeliMate> deliMates2 = deliveryMapper.queryDeliMateBySuppIdAndMateCode(map);
			if (deliMates2.size() > 0) {// 有保存的送货单，则不让新建送货单
				List<Delivery> delis = deliveryMapper.queryDeliveryBySuppIdAndMateCode(map);
				str2 += "物料 " + mateCode + "所在的送货单：";
				for (int i = 0; i < delis.size(); i++) {
					str2 += delis.get(i).getDeliCode();
					if (i < delis.size() - 1) {
						str2 += ",";
					}
					if (i == delis.size() - 1) {
						str2 += "处于保存状态需要提交后才能新建";
					}
				}
			} else {// 无，则可以创建送货单
				count++;
			}
			// 查询已保存的直发通知单
			List<MessMate> Messmates = starMessMapper.queryStarMessMateByZzoemAndMateCode(map);
			if (Messmates.size() > 0) {// 有保存的直发通知，则不让新建直发通知单
				List<StraMessage> straMess = starMessMapper.queryStarMessByZzoemAndMateCode(map);
				str3 += "物料 " + mateCode + "所在的直发通知：";
				for (int i = 0; i < straMess.size(); i++) {
					str3 += straMess.get(i).getMessCode();
					if (i < straMess.size() - 1) {
						str3 += ",";
					}
					if (i == straMess.size() - 1) {
						str3 += "处于保存状态需要提交后才能新建";
					}
				}
			} else {// 无，则可以新建
				total++;
			}
		}
		if (count == mates.size() && total == mates.size()) {// 预约申请的所有物料都不在已保存的送货单中
			result.put("judge", true);
		} else {// 只要有一条物料在已保存的送货单中，则不能新建送货单
			result.put("result", str2 + str3);
			result.put("judge", false);
		}
		return result;
	}

	@Override
	public Map<String, Object> recommendPurchaseOrderForStraMates(Map<String, Object> result, List<MessMate> list,String suppRange) {

		List<MessMate> recommendList = new ArrayList<MessMate>();
		double total = 0;
		double totalAmount = 0;
		String semiMateCodes = "";
		String str = "";

		// 记录已经做过推荐的订单
		Map<String, List<OrderMate>> map = new HashMap<>();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("suppRange", suppRange);
		// 循环调拨单的列表，去找到对应的采购订单，并推荐
		for (MessMate alloMate : list) {
			boolean isRepeat = false;
			// 成品物料编码
			String alloMC = alloMate.getMateCode();
			// 半成品物料编码
			String semiAlloMC = alloMate.getSemiMateCode();
			if (StringUtils.isEmpty(semiAlloMC)) {
				result.put("mateMsg", alloMC + "无半成品！");
			} else {
				if (semiMateCodes.contains(semiAlloMC)) {
					isRepeat = true;
				} else {
					semiMateCodes += semiAlloMC + ",";
				}
				// 调拨单的数量
				Double mateNumber = alloMate.getMateNumber();
				Double unpa;
				// 获取到最古老的一条采购订单(现在修改为，查询出所有的采购订单，然后循环，直到满足预约数量)
				String zzoem = alloMate.getZzoem();
				param.put("sapId", zzoem);
				param.put("mateCode", semiAlloMC);
				List<OrderRele> orderList = orderMapper.findOrderListBySapIdAndMateCode(param);
				if (orderList.size() > 0) {
					orderLoop: for (OrderRele orderRele : orderList) {
						// 采购订单编码
						String orderNo = orderRele.getContOrdeNumb();
						BigDecimal hasOccupy = new BigDecimal(0);
						// 判断是否需要要推荐
						boolean isReco = false;
						if (isRepeat) {
							// 循环已经推荐的数据进行判断是否需要继续推荐
							for (MessMate rl : recommendList) {
								String poId = rl.getPoId();
								// 判断是否用了这个采购订单
								if (poId.equals(orderNo)) {
									// 如果相等，则判断是否可用未交量是否已经用完了
									Double r1 = rl.getCalculNumber();
									Double r2 = rl.getSemiMateNumber();
									BigDecimal bg1 = new BigDecimal(r1);
									BigDecimal bg2 = new BigDecimal(r2);
									int i = bg2.compareTo(bg1);// -1<,0=,1>
									if (i == -1) {
										// 未占用完
										isReco = true;
										hasOccupy = hasOccupy.add(bg2);
									} else {
										isReco = false;
										break;
									}
								} else {
									isReco = true;
								}
							}
						} else {
							isReco = true;
						}
						if (isReco) {
							// 获取采购订单详情
							List<OrderMate> mates = orderRele.getMates();
							if (mates.size() > 0) {
//								OrderMate orderMate = mates.get(0);
								for (OrderMate orderMate : mates) {
									// 推荐采购订单
									MessMate recoMate = new MessMate();
									// 成品物料信息
									recoMate.setMateCode(alloMC);// 成品物料编码
									recoMate.setMateName(alloMate.getMateName());// 成品物料名称
									recoMate.setOrderId(alloMate.getOrderId());// 调拨单号
									recoMate.setFrequency(alloMate.getFrequency());// 项次
									recoMate.setUnit(alloMate.getUnit());// 单位
									// 采购订单上的未交数量
									unpa = orderMate.getUnpaQuan();
									StraMessAndMateDO calcMate = calculateActualOrderNumber(orderNo, semiAlloMC,suppRange, unpa);
									Double calculNumber = calcMate.getCalculNumber();
									BigDecimal bigCalcNum = new BigDecimal(calculNumber);
									int j = bigCalcNum.compareTo(BigDecimal.ZERO);
									if (j == 0)
										break;
									
									String key = orderNo+"-"+orderMate.getFrequency();
									List<OrderMate> occupyList = map.get(alloMC);
									if (occupyList == null || occupyList.size() == 0) {
										occupyList = new ArrayList<>();
									}
									recoMate.setMateNumber(mateNumber);// 调拨单的数量，相当于预约数量
									// 采购订单的信息
									recoMate.setPoId(orderRele.getContOrdeNumb());
									recoMate.setSemiMateCode(semiAlloMC);
									recoMate.setSemiFrequency(orderMate.getFrequency());
									recoMate.setSemiMateName(alloMate.getSemiMateName());
									recoMate.setSemiUnit(orderMate.getCompany());
									recoMate.setSubeDate(orderRele.getSubeDate());
									// 取订单未交量和预约数量的最小值作为送货数量
									// 通过和历史采购订单比较获取到的未交量
									Double unpaNumber = calcMate.getUnpaNumber();
									recoMate.setUnpaNumber(unpaNumber);// 未交数量
									// 通过和历史采购订单比较获取到的计算未交量
									// 比较计算未交量与原本未交量，取小的
									BigDecimal bigUnpa = new BigDecimal(unpaNumber);

									int i = bigUnpa.compareTo(bigCalcNum);
									// 这个作为计算未交量，可用量
									BigDecimal bigCalc = i <= 0 ? bigUnpa : bigCalcNum;
									
									BigDecimal hasOccupy1 = new BigDecimal(0);
									if (occupyList.size() > 0) {
										// 计算该订单的占用
										for (OrderMate ol : occupyList) {
											String mainId = ol.getMainId();
											if(key.equals(mainId)) {
												Double purcQuan = ol.getPurcQuan();
												BigDecimal bigDecimal = new BigDecimal(purcQuan.doubleValue());
												hasOccupy1 = hasOccupy1.add(bigDecimal);
											}
										}
									}

									BigDecimal bd2 = bigCalc.subtract(hasOccupy1);// 订单计算未交量（订单可用量）
									recoMate.setCalculNumber(bd2.doubleValue());// 计算订单未交量
									BigDecimal bd1 = new BigDecimal(mateNumber);
									int r = bd1.compareTo(bd2);// -1(bd1<bd2);0(bd1=bd2);1(bd1>bd2)
									int t = bd2.compareTo(new BigDecimal(0));// 订单的计算未交量和0比较
																				// -1(bd2<0);0(bd2=0);1(bd2>0)
									if (r == -1 || r == 0) {
										// 如果预约的小于等于计算未交量，则取预约的作为送货数量，并结束循环
										recoMate.setSemiMateNumber(mateNumber);
										Double mateAmount = alloMate.getMateAmount();
										if (mateAmount == null) {
											recoMate.setSemiMateAmount("0.000");// 计算方量
										} else {
											BigDecimal bdMateNumber = new BigDecimal(mateNumber);
											BigDecimal bdMateAmount = new BigDecimal(mateAmount);
											BigDecimal bd = bdMateNumber.multiply(bdMateAmount);
											bd = bd.divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
											recoMate.setSemiMateAmount(bd.toString());// 计算方量
										}
										total += recoMate.getSemiMateNumber();// 实际送货数量之和
										totalAmount += Double.parseDouble(recoMate.getSemiMateAmount());
										recommendList.add(recoMate);
										OrderMate orderMate2 = new OrderMate();
										orderMate2.setMainId(key);
										orderMate2.setPurcQuan(mateNumber);
										occupyList.add(orderMate2);
										map.put(alloMC, occupyList);
										break orderLoop;
									} else {
										if (bd2.doubleValue() != 0) {
											// 如果预约的大于计算未交量，则取计算未交量作为送货数量，并继续循环
											recoMate.setSemiMateNumber(bd2.doubleValue());// ------修改过
											Double mateAmount = alloMate.getMateAmount();
											if (mateAmount == null) {
												recoMate.setSemiMateAmount("0.000");// 计算方量
											} else {
												BigDecimal bdMateAmount = new BigDecimal(mateAmount);
												BigDecimal bd = bd2.multiply(bdMateAmount);
												bd = bd.divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
												recoMate.setSemiMateAmount(bd.toString());// 计算方量
											}
											if (t == 1) {// 计算未交量大于零
												total += recoMate.getSemiMateNumber();// 实际送货数量之和
												totalAmount += Double.parseDouble(recoMate.getSemiMateAmount());
												recommendList.add(recoMate);
												OrderMate orderMate2 = new OrderMate();
												orderMate2.setMainId(key);
												orderMate2.setPurcQuan(mateNumber);
												occupyList.add(orderMate2);
												map.put(alloMC, occupyList);
											} else {
												str += semiAlloMC + ",";
											}
											mateNumber = mateNumber - bd2.doubleValue();
										}
									}
								}
							}
						}
					}
				} else {
					String msg = (String) result.get("msg");
					if (StringUtils.isEmpty(msg)) {
						result.put("mateMsg", alloMC + "未查询到符合条件的采购订单!");
					} else {
						msg += alloMC + "未查询到符合条件的采购订单";
						result.put("mateMsg", msg);
					}
				}
			}
		}
		result.put("strError", str);
		result.put("total", total);
		DecimalFormat df = new DecimalFormat();
		String ta = df.format(totalAmount);
		result.put("totalAmount", ta);
		result.put("messMateList", recommendList);
		return result;
	}

	@Override
	public List<StraMessAndMateDO> findOccupyNumberByParams(Map<String, Object> params) {
		return starMessMapper.findOccupyNumberByParams(params);
	}

}
