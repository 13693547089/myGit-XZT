package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.CutLiaisonForBaoCaiMapper;
import com.faujor.dao.master.bam.CutProductMapper;
import com.faujor.dao.master.bam.OrderMonthMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.bam.CutBaoCaiMate;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutProduct;
import com.faujor.entity.bam.CutProductDO;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.OemPackSupp;
import com.faujor.entity.mdm.ProdMateDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.bam.PdrService;
import com.faujor.service.mdm.ConfigService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.UserCommon;

@Service(value = "CutProductService")
public class CutProductServiceImpl implements CutProductService {

	@Autowired
	private CutProductMapper cutProductMapper;
	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private OrderMonthMapper orderMonthMapper;
	@Autowired
	private PdrService pdrService;
	@Autowired
	private CutLiaisonForBaoCaiService cutLiaisonForBaoCaiService;
	@Autowired
	private ConfigService configService;

	@Override
	public Map<String, Object> queryCutProductByPage(Map<String, Object> map) {
		List<ProdMateDO> list = cutProductMapper.queryCutProductByPage(map);
		int count = cutProductMapper.queryCutProductByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", "0");
		page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean deleteCutProductByprodId(String[] prodIds) {
		int i = cutProductMapper.deleteCutProductByprodId(prodIds);
		if (i == prodIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ProdMateDO> queryAllCutProduct() {
		List<ProdMateDO> list = cutProductMapper.queryAllCutProduct();
		return list;
	}

	@Override
	@Transactional
	public boolean addCutProduct(String[] mateIds) {
		SysUserDO user = UserCommon.getUser();
		int count = 0;
		for (int i = 0; i < mateIds.length; i++) {
			CutProduct cp = new CutProduct();
			cp.setCreateId(user.getUserId().toString());
			cp.setCreator(user.getName());
			cp.setMateId(mateIds[i]);
			// cp.setVersion("1.0");
			int j = 0;
			j = cutProductMapper.addCutProduct(cp);
			count += j;
		}
		if (count == mateIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<CutStructure> queryAllCutStru() {
		return cutProductMapper.queryAllCutStru();
	}

	@Override
	public List<CutStructure> queryBaoCaiCutStru() {
		return cutProductMapper.queryBaoCaiCutStru();
	}

	@Override
	@Transactional
	public boolean addCutStru(List<CutStructure> list) {
		int j = cutProductMapper.deleteAllCutStru();
		int count = 0;
		for (CutStructure c : list) {
			int i = 0;
			i = cutProductMapper.addCutStru(c);
			count += i;
		}
		if (count == list.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> queryClassCodes() {
		List<String> classCodes = cutProductMapper.queryClassCodes();
		return classCodes;
	}

	@Override
	public String queryMaxcontentCodeOfClassCode(String classCode) {
		String str = cutProductMapper.queryMaxcontentCodeOfClassCode(classCode);
		return str;
	}

	@Override
	public String queryMaxContentCode(String contentCode) {
		String classCode = cutProductMapper.queryClassCodeByContentCode(contentCode);
		return cutProductMapper.queryMaxcontentCodeOfClassCode(classCode);
	}

	@Override
	public Map<String, Object> queryAllCutProductOfQualSupp(String suppId, String cutMonth) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProdMateDO> list = cutProductMapper.queryMatesOfCutProduct();
		List<ProdMateDO> proList = new ArrayList<ProdMateDO>();
		for (ProdMateDO m : list) {
			String buyerId = qualSuppMapper.queryBuyerIdBySuppIdAndMateId(suppId, m.getMateId());
			if (buyerId != null) {
				ProdMateDO prodMateDO = cutProductMapper.queryOneProdMateDOByProdId(m.getProdId());
				if (prodMateDO.getMainStru() == null || prodMateDO.getMainStru() == "") {
					map.put("msg", "存在有的物料没有主包材,请添加后再操作");
					map.put("judge", false);
					return map;
				}
				proList.add(m);
			}
		}
		// 通过proList 集合中的物料数据查询总订单在外量，总成品库存，总可生产订单（还需要月份）
		if (proList.size() > 0) {
			proList = getCountNum(proList, cutMonth, suppId);
		}
		map.put("judge", true);
		map.put("proList", proList);
		return map;
	}

	@Override
	public Map<String, Object> queryAllCutProductWithSuppOfQualSupp(String suppId, String cutMonth) {
//		List<ProdMateDO> list = cutProductMapper.queryMatesOfCutProductWithSupp(suppId);
//		List<ProdMateDO> proList = new ArrayList<ProdMateDO>();
//		List<String> ermSuppList = new ArrayList<String>();
//		OemPackSupp oemPackSupp = new OemPackSupp();
//		oemPackSupp.setPackSuppCode(suppId);
//		oemPackSupp.setSelected_col("CONCAT(t.OEM_SUPP_CODE, CONCAT('-', t.MATE_CODE))");
//		List<String> opsMateCodes = configService.findSelectedMateCodesByOemPackSupp(oemPackSupp);
//		for (ProdMateDO m : list) {
//			String mateCode = m.getMateCode();
//			String suppCode = m.getSuppCode();
//			String key = suppCode + "-" + mateCode;
//			if (opsMateCodes.contains(key)) {
//				String buyerId = qualSuppMapper.queryBuyerIdBySuppIdAndMateId(suppId, m.getMateId());
//				if (buyerId != null) {
//					ProdMateDO prodMateDO = cutProductMapper.queryOneProdMateDOByProdId(m.getProdId());
//					if (prodMateDO.getMainStru() == null || prodMateDO.getMainStru() == "") {
//						map.put("msg", "存在有的物料没有主包材,请添加后再操作");
//						map.put("judge", false);
//						return map;
//					}
//					Boolean flag = ermSuppList.contains(m.getSuppName());
//					if (!flag) {
//						ermSuppList.add(m.getSuppName());
//					}
//					proList.add(m);
//				}
//			}
//		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProdMateDO> list = cutProductMapper.queryMatesOfCutProductWithBaoCaiSupp(suppId);
		List<String> ermSuppList = new ArrayList<String>();
		for (ProdMateDO m : list) {
			String mainStru = m.getMainStru();
			if (StringUtils.isEmpty(mainStru)) {
				map.put("msg", "存在有的物料没有主包材,请添加后再操作");
				map.put("judge", false);
				return map;
			}
			Boolean flag = ermSuppList.contains(m.getSuppName());
			if (!flag) {
				ermSuppList.add(m.getSuppName());
			}
		}
		map.put("judge", true);
		map.put("proList", list);
		map.put("ermSuppList", ermSuppList);
		return map;
	}

	/**
	 * 计算总订单在外量(期初订单)，总成品库存(月底结账库存)，总可生产订单（总订单在外量-总成品库存）
	 * 
	 * @return
	 */
	@Override
	public List<ProdMateDO> getCountNum(List<ProdMateDO> list, String cutMonth, String suppId) {
		Date month = DateUtils.parse(cutMonth, "yyyy-MM");
		QualSupp supp = qualSuppMapper.queryOneQualSuppBySuppId(suppId);
		List<String> suppNos = new ArrayList<>();
		suppNos.add(supp.getSapId());
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> param2 = new HashMap<String, Object>();
		params.put("suppNos", suppNos);
		params.put("planMonth", month);
		Map<String, Object> map = new HashMap<>();
		map.put("suppNo", supp.getSapId());
		String date = cutMonth + "-01";// 设置为该月份的1号
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		try {
			cale.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cale.add(Calendar.DATE, -1);// 该月份的1号减去 1天，就变成上个月最后一天
		Date time = cale.getTime();// 获取上个月最后一天
		String lastDay = format.format(cale.getTime());
		map.put("time", time);
		for (ProdMateDO pm : list) {
			params.put("mateCode", pm.getMateCode());
			// 获取期初订单 一个供应商某月某个物料的期初订单
			Double unpaidNum = orderMonthMapper.selectUndeliveredNumByMap(params);
			// 总订单在外量
			pm.setSumOutNum(unpaidNum == null ? 0D : unpaidNum);
			map.put("mateCode", pm.getMateCode());
			// PdrDetail pdrDetail2 = pdrService.getPdrDetailBySuppMateDate(map);
			// 获取月底结账库存(获取上个月最后一天的物料的库存) 从产能上报中获取
			PdrDetail pdrDetail = pdrService.getPdrDetailBySapIdAndMonthAndMateCode(map);
			if (pdrDetail != null && pdrDetail.getStockQty() != null) {
				Float stockQty = pdrDetail.getStockQty() == null ? 0F : pdrDetail.getStockQty();
				BigDecimal b = new BigDecimal(String.valueOf(stockQty));
				double d = b.doubleValue();
				// 总成品库存
				pm.setSumInveNum(d);
			} else {
				// 总成品库存
				pm.setSumInveNum(0D);
			}
			double sumInveNum = pm.getSumInveNum();
			double sumOutNum = unpaidNum;
			double sumProNum = sumOutNum - sumInveNum < 0 ? 0 : sumOutNum - sumInveNum;
			// 总可生产订单
			pm.setSumProdNum(sumProNum);
			// 上月打切可生产订单
			String lastCutMonth = getLastDate(cutMonth);// 获取当前月份上一个月份日期
			Integer lastProdNum = cutProductMapper.getLastProdNumByMateCodeAndSuppIdAndCutMonth(pm.getMateCode(),
					suppId, lastCutMonth);
			pm.setLastProdNum(lastProdNum == null ? 0 : lastProdNum);
		}
		return list;
	}

	// 使用当前月份,得到上一个月的月份:月份的格式是:yyyy-MM
	@Override
	public String getLastDate(String currentDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(currentDate + "-" + "01");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		String month = (c.get(Calendar.MONTH) + 1) >= 10 ? (c.get(Calendar.MONTH) + 1) + ""
				: "0" + (c.get(Calendar.MONTH) + 1);
		String lastDate = c.get(Calendar.YEAR) + "-" + month;

		return lastDate;

	}

	@Override
	public ProdMateDO queryOneProdMateDOByProdId(String prodId) {
		return cutProductMapper.queryOneProdMateDOByProdId(prodId);
	}

	@Override
	@Transactional
	public Map<String, Object> addCutProduct2(ProdMateDO prod) {
		Map<String, Object> map = new HashMap<String, Object>();
		String version = prod.getVersion();
		if (StringUtils.isEmpty(version)) {
			version = "QQQ";// 如果版本是空时，默认作为QQQ版本处理
		}
		CutProduct p = cutProductMapper.queryProdMateByVersion(prod.getMateId(), version);
		if (p != null) {
			map.put("judge", false);
			map.put("msg", "同一个物料不能添加相同的版本");
			return map;
		} else {
			SysUserDO user = UserCommon.getUser();
			CutProduct cp = new CutProduct();
			cp.setCreateId(user.getUserId().toString());
			cp.setCreator(user.getName());
			cp.setMateId(prod.getMateId());
			cp.setVersion(prod.getVersion());
			cp.setCutAim(prod.getCutAim());
			cp.setMainStru(prod.getMainStru());
			cp.setIsSpecial(prod.getIsSpecial());
			cp.setIsHaveSeim(prod.getIsHaveSeim());
			int i = cutProductMapper.addCutProduct(cp);
			if (i == 1) {
				map.put("judge", true);
				return map;
			} else {
				map.put("judge", false);
				map.put("msg", "添加打切品失败");
				return map;
			}
		}
	}

	@Override
	@Transactional
	public Map<String, Object> updateCutProductVer(ProdMateDO prod) {
		Map<String, Object> map = new HashMap<String, Object>();
		String version = prod.getVersion();
		if (StringUtils.isEmpty(prod.getVersion())) {
			version = "QQQ";// 如果版本是空时，默认作为QQQ版本处理
		}
		// 判读是否修改了物料的版本
		String prodMateVersion = cutProductMapper.queryOneProdMateByProdId(prod.getProdId());
		if (!version.equals(prodMateVersion)) {
			// 修改了版本，需要根据物料编码和版本查询数据库是否已存在，已存在则不让修改，不存在则update打切品数据
			CutProduct p = cutProductMapper.queryProdMateByVersion(prod.getMateId(), version);
			if (p != null) {
				map.put("judge", false);
				map.put("msg", "同一个物料不能添加相同的版本");
				return map;
			}
		}
		// 没有修改版本，直接update打切品数据
		int i = cutProductMapper.updateCutProductVer(prod);
		if (i == 1) {
			map.put("judge", true);
			return map;
		} else {
			map.put("judge", false);
			map.put("msg", "修改打切品失败");
			return map;
		}

	}

	@Override
	public CutProduct queryProdMateByVersion(String mateId, String version) {
		return cutProductMapper.queryProdMateByVersion(mateId, version);
	}

	@Override
	public List<CutStructure> queryCutStruForCutProd() {
		return cutProductMapper.queryCutStruForCutProd();
	}

	@Override
	public List<CutProductDO> queryCutProduct(ProdMateDO pm) {
		return cutProductMapper.queryCutProduct(pm);
	}

	@Override
	@Transactional
	public Map<String, Object> dealMates(List<MateDO> list) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String str = "物料：";
		int count = 0;
		int total = 0;
		for (int i = 0; i < list.size(); i++) {
			MateDO mateDO = list.get(i);
			param.put("mateId", mateDO.getMateId());
			param.put("isSpecial", mateDO.getIsSpecial());
			ProdMateDO p = cutProductMapper.queryCutProd(param);
			if (p == null) {
				int j = 0;
				CutProduct cp = new CutProduct();
				cp.setCreateId(user.getUserId().toString());
				cp.setCreator(user.getName());
				cp.setMateId(mateDO.getMateId());
				cp.setIsSpecial(mateDO.getIsSpecial());
				cp.setIsHaveSeim(mateDO.getIsHaveSeim());
				j = cutProductMapper.addCutProduct(cp);
				total += j;
			} else {
				str += mateDO.getMateCode();
				count++;
			}
		}
		if (total == list.size()) {
			map.put("judge", true);
		}
		if (count != 0) {
			map.put("judge", false);
			map.put("msg", str + "无法重复添加");
		}
		return map;
	}

	@Override
	public Map<String, Object> queryMateOfCutLiai(String suppId, String cutMonth, String headerFiled) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProdMateDO> list = cutProductMapper.queryMatesOfCutLiaiBySuppIdAndCutMonth(suppId, cutMonth);
		for (ProdMateDO m : list) {
			String mainStru = m.getMainStru();
			if (StringUtils.isEmpty(mainStru)) {
				map.put("msg", "存在有的物料没有主包材,请添加后再操作");
				map.put("judge", false);
				return map;
			}
			String version = m.getVersion();
			if ("QQQ".equals(version)) {
				m.setVersion("");
			}
		}
		// 通过proList 集合中的物料数据查询总订单在外量，总成品库存，总可生产订单（还需要月份）
		if (list.size() > 0) {
			list = getCountNum(list, cutMonth, suppId);
		}
		// 处理引用包材打切联络单的数据
		map = dealWithCiteData(list, headerFiled);
		map.put("judge", true);
		return map;
	}

	// 处理引用数据
	private Map<String, Object> dealWithCiteData(List<ProdMateDO> proList, String headerFiled) {
		JSONArray header = JSONArray.parseArray(headerFiled);
		JSONArray ja = new JSONArray();
		for (ProdMateDO pm : proList) {
			JSONObject jo = new JSONObject();
			String mateCode = pm.getMateCode();
			jo.put("mateCode", mateCode);
			jo.put("mateName", pm.getMateName());
			jo.put("seriesExpl", pm.getSeriesExpl());
			jo.put("lastProdNum", pm.getLastProdNum());
			Integer boxNumber = pm.getBoxNumber();
			jo.put("boxNumber", pm.getBoxNumber());
			jo.put("version", pm.getVersion());
			jo.put("cutAim", pm.getCutAim());
			jo.put("mainStru", pm.getMainStru());
			jo.put("sumOutNum", pm.getSumOutNum());
			jo.put("sumInveNum", pm.getSumInveNum());
			jo.put("sumProdNum", pm.getSumProdNum());
			List<CutBaoCaiMate> mateList = pm.getMateList();
			Map<String, Object> calssValueMap = new HashMap<String, Object>();
			// 这个循环是为了获取包材对应的数量，存储在calssValueMap中
			for (CutBaoCaiMate cb : mateList) {
				Map<String, Object> classMap = new HashMap<String, Object>();
				String liaiId = cb.getLiaiId();
				// 获取包材采购订单物料表的头部信息
				JSONArray cbJa = cutLiaisonForBaoCaiService.queryBaoCaiLiaiMateFields(liaiId);
				for (int i = 0; i < cbJa.size(); i++) {
					CutLiaiField field = cbJa.getObject(i, CutLiaiField.class);
					String fie = field.getField();
					String title = field.getTitle();
					if (!StringUtils.isEmpty(title)) {
						if (title.contains("库存包材厂")) {
							String className = title.substring(0, title.length() - 6);
							classMap.put(fie, className);
						}
					}
				}
				String fields = cb.getFields();
				JSONArray jj = JSONArray.parseArray(fields);
				if (jj.size() != 0) {
					for (int i = 0; i < jj.size(); i++) {
						JSONObject object = jj.getJSONObject(i);
						Set<String> keySet = object.keySet();
						for (String str : keySet) {
							String className = (String) classMap.get(str);
							if (!StringUtils.isEmpty(className)) {
								String object2 = (String) object.get(str);
								if (object2 == null || "".equals(object2) || "NaN".equals(object2)) {
									object2 = "0";
								}
								Double b = Double.parseDouble(object2);
								Double v = (Double) calssValueMap.get(className);
								if (v == null) {
									calssValueMap.put(className, b);
								} else {
									calssValueMap.put(className, v + b);
								}
							}

						}
					}
				}

			}
			for (int i = 0; i < header.size(); i++) {
				CutLiaiField field = header.getObject(i, CutLiaiField.class);
				String fie = field.getField();
				String title = field.getTitle();
				if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(fie)) {
					if (title.contains("包材厂") && fie.contains("A")) {
						String className = title.substring(0, title.length() - 4);
						Double b = (Double) calssValueMap.get(className);
						if (b == null) {
							b = 0D;
						}
						jo.put(fie, b.toString());// 套袋包材厂个A
						String nextField = "B" + fie.substring(1);// B302套袋包材厂箱B
						String content = fie.contains("M") ? fie.substring(1, fie.indexOf("M")) : fie.substring(1);
						int k = Integer.parseInt(content) / 100;
						String totalField = fie.contains("M") ? "C" + (k * 100 + 1) + "MASTER" : "C" + (k * 100 + 1);// C301//合计
						String differenceField = "D" + totalField.substring(1);// 差异
						if (fie.contains("MASTER")) {
							// 表示1:1折算
							jo.put(nextField, b.toString());
							jo.put(totalField, b.toString());
							if (b > 0) {
								jo.put(differenceField, "<span style='color:red;'>-" + b.toString() + "</span>");
							} else {
								jo.put(differenceField, b.toString());
							}
						} else {
							// 需要除以箱入数
							BigDecimal bDecimal = BigDecimal.valueOf(b);
							if (boxNumber != null && boxNumber != 0) {
								BigDecimal box = BigDecimal.valueOf(boxNumber);// 箱入数
								BigDecimal divide = bDecimal.divide(box, 2, RoundingMode.HALF_UP);
								Double d = divide.doubleValue();
								jo.put(nextField, d.toString());
								jo.put(totalField, d.toString());
								if (d > 0) {
									jo.put(differenceField, "<span style='color:red;'>-" + d.toString() + "</span>");
								} else {
									jo.put(differenceField, d.toString());
								}
							}
						}
					}
				}
			}

			ja.add(jo);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", ja);
		map.put("count", ja.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}
}
