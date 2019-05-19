package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.bam.InvenPlanMapper;
import com.faujor.dao.master.bam.OrderMonthMapper;
import com.faujor.dao.master.bam.PadPlanMapper;
import com.faujor.dao.master.bam.SuppProdMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InvenPadCompare;
import com.faujor.entity.bam.psm.InvenPlan;
import com.faujor.entity.bam.psm.InvenPlanDetail;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdVo;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.InvenPlanService;
import com.faujor.service.bam.OrderService;
import com.faujor.service.bam.PdrService;
import com.faujor.service.bam.SuppProdService;
import com.faujor.service.common.CodeService;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.BigDecimalUtil;
import com.faujor.utils.DateUtils;
import com.faujor.utils.PrivilegesCommon;
import com.faujor.utils.RestCode;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;


@Service
public class InvenPlanServiceImpl implements InvenPlanService {
	@Autowired
	private InvenPlanMapper invenPlanMapper;
	@Autowired
	private SuppProdMapper suppProdMapper;
	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private PdrService pdrService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SuppProdService suppProdService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private PadPlanMapper padPlanMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private PrivilegesCommon privilegesCommon;
	@Autowired
	private PdrService pdrMapper;
	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private OrderMonthMapper orderMonthMapper;
	
	@Override
	public LayuiPage<InvenPlan> getInvenPlanByPage(Map<String, Object> map) {
		LayuiPage<InvenPlan> page = new LayuiPage<InvenPlan>();
		List<InvenPlan> data = invenPlanMapper.getInvenPlanByPage(map);
		int count = invenPlanMapper.getInvenPlanNum(map);
		page.setData(data);
		page.setCount(count);
		return page;
	}

	@Override
	public InvenPlan getInvenPlanById(String id) {
		InvenPlan invenPlan = invenPlanMapper.getInvenPlanById(id);
		return invenPlan;
	}
	@Override
	public InvenPlan getInvenPlanByCode(String planCode) {
		InvenPlan invenPlan = invenPlanMapper.getInvenPlanByCode(planCode);
		return invenPlan;
	}

	@Override
	@Transactional
	public void saveInvenPlan(InvenPlan invenPlan, List<InvenPlanDetail> planDetails) {
		// 保存备货计划主表信息
		SysUserDO user = UserCommon.getUser();
		invenPlan.setCreateUser(user.getName());
		invenPlan.setCreater(user.getUserId()+"");
		invenPlan.setCreateTime(new Date());
		invenPlanMapper.saveInvenPlan(invenPlan);
		String mainId = invenPlan.getId();
		String planCode = invenPlan.getPlanCode();
		Date planMonth = invenPlan.getPlanMonth();
		Calendar cal=Calendar.getInstance();
		cal.setTime(planMonth);
		cal.add(Calendar.MONTH,-1);
		Date oldMonth=cal.getTime();
		// 保存备货计划从表信息
		for (InvenPlanDetail invenPlanDetail : planDetails) {
			invenPlanDetail.setMainId(mainId);
			invenPlanDetail.setPlanCode(planCode);
			invenPlanMapper.saveInvenPlanDetail(invenPlanDetail);
			String mateCode = invenPlanDetail.getMateCode();
			//更新上个月的 安全库存率
			Map<String, Object> map=new HashMap<>();
			map.put("planMonth", oldMonth);
			map.put("mateCode",mateCode );
			BigDecimal deliveryPlan = invenPlanDetail.getDeliveryPlan();
			List<InvenPlanDetail> invenPlanDetails = invenPlanMapper.getPlanDetailByMateAndMonth(map);
			for (InvenPlanDetail detail : invenPlanDetails) {
				map.put("nextDlvNum", deliveryPlan);
				BigDecimal beforeEndStock = detail.getEndStock();
				String beforeSafeScale="";
				if(deliveryPlan!=null && !deliveryPlan.equals(0)){
					beforeSafeScale=BigDecimalUtil.getPercentage(beforeEndStock, deliveryPlan);
				}
				map.put("safeScale", beforeSafeScale);
				map.put("id", detail.getId());
				invenPlanMapper.updateSafeScale(map);
			}
		}
	}

	@Override
	@Transactional
	public void updateInvenPlan(InvenPlan invenPlan, List<InvenPlanDetail> planDetails) {
		// 更新主表信息
		String mainId = invenPlan.getId();
		String planCode = invenPlan.getPlanCode();
		InvenPlan oldPlan = invenPlanMapper.getInvenPlanById(mainId);
		
		SysUserDO user = UserCommon.getUser();
		invenPlan.setModifyTime(new Date());
		invenPlan.setModifyUser(user.getName());
		invenPlan.setModifier(user.getUserId()+"");
		invenPlanMapper.updateInvenPlan(invenPlan);
		Date oldMonth = oldPlan.getPlanMonth();
		Date newMonth = invenPlan.getPlanMonth();
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(newMonth);
		cal.add(Calendar.MONTH,-1);
		Date beforeMonth=cal.getTime();
		
		//如果新的月份和旧的月份不相等时 删除原来所有的数据  重新插入
		if(newMonth==null || oldMonth==null || oldMonth.compareTo(newMonth)==0){
			// 更新从表信息 有则更新 无则新增 不存在删除的情况
			for (InvenPlanDetail invenPlanDetail : planDetails) {
				String id = invenPlanDetail.getId();
				InvenPlanDetail dbdetail = invenPlanMapper.getPlanDetailById(id);
				if (dbdetail == null) {
					invenPlanDetail.setMainId(mainId);
					invenPlanDetail.setPlanCode(planCode);
					invenPlanMapper.saveInvenPlanDetail(invenPlanDetail);
				} else {
					invenPlanMapper.updateInvenPlanDetail(invenPlanDetail);
				}
				//更新上个月的 安全库存率
				String mateCode = invenPlanDetail.getMateCode();
				Map<String, Object> map=new HashMap<>();
				map.put("planMonth", beforeMonth);
				map.put("mateCode",mateCode );
				BigDecimal deliveryPlan = invenPlanDetail.getDeliveryPlan();
				List<InvenPlanDetail> invenPlanDetails = invenPlanMapper.getPlanDetailByMateAndMonth(map);
				for (InvenPlanDetail detail : invenPlanDetails) {
					map.put("nextDlvNum", deliveryPlan);
					BigDecimal beforeEndStock = detail.getEndStock();
					String beforeSafeScale="";
					if(deliveryPlan!=null && !deliveryPlan.equals(0)){
						beforeSafeScale=BigDecimalUtil.getPercentage(beforeEndStock, deliveryPlan);
					}
					map.put("safeScale", beforeSafeScale);
					map.put("id", detail.getId());
					invenPlanMapper.updateSafeScale(map);
				}
			}
		}else{
			invenPlanMapper.removeInvenPlanDetailsByMainId(mainId);
			invenPlanMapper.delInvenPlanDetailByMainId(mainId);
			for (InvenPlanDetail invenPlanDetail : planDetails) {
				invenPlanDetail.setMainId(mainId);
				invenPlanDetail.setPlanCode(planCode);
				invenPlanMapper.saveInvenPlanDetail(invenPlanDetail);
				//更新上个月的 安全库存率
				String mateCode = invenPlanDetail.getMateCode();
				Map<String, Object> map=new HashMap<>();
				map.put("planMonth", beforeMonth);
				map.put("mateCode",mateCode );
				BigDecimal deliveryPlan = invenPlanDetail.getDeliveryPlan();
				List<InvenPlanDetail> invenPlanDetails = invenPlanMapper.getPlanDetailByMateAndMonth(map);
				for (InvenPlanDetail detail : invenPlanDetails) {
					map.put("nextDlvNum", deliveryPlan);
					BigDecimal beforeEndStock = detail.getEndStock();
					String beforeSafeScale="";
					if(deliveryPlan!=null && !deliveryPlan.equals(0)){
						beforeSafeScale=BigDecimalUtil.getPercentage(beforeEndStock, deliveryPlan);
					}
					map.put("safeScale", beforeSafeScale);
					map.put("id", detail.getId());
					invenPlanMapper.updateSafeScale(map);
				}
			}
		}
	}

	@Override
	public RestCode delInvenPlan(String id) {
		return null;
	}

	@Override
	public List<SuppProd> getPlanDetailByMainId(Map<String, Object> map) {
		List<SuppProd> list = invenPlanMapper.getInvenPlanDetaiByMainId(map);
		return list;
	}

	@Override
	public Integer getStatusNumsByMainIds(List<String> ids, String status) {
		Integer count = invenPlanMapper.getStatusNumsByMainIds(ids, status);
		return count;
	}

	@Override
	public void changeInvenPlanStatus(List<String> ids, String status) {
		invenPlanMapper.changeInvenPlanStatus(ids, status);
	}

	@Override
	@Transactional
	public void cancleDecompose(String id, String status) {
		suppProdMapper.delSuppProdByPlanDetailId(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		map.put("safeScale", "");
		map.put("endStock", null);
		map.put("prodPlan", null);
		invenPlanMapper.updatePlanDetail(map);
		// invenPlanMapper.changeInvenPlanDetailStatus(id, status);
	}

	@Override
	@Transactional
	public void delInvenPlan(List<String> ids) {
		TaskParamsDO params = new TaskParamsDO();
		for (String invenPlanId : ids) {

			 List<SuppProd> suppProds = suppProdMapper.getSuppProdByInvenId(invenPlanId);
			 for (SuppProd suppProd : suppProds) {
				 String suppProdId = suppProd.getId();
				 suppProdMapper.delSuppProdPlanByMainId(suppProdId);
			}
			 suppProdMapper.delSuppProdByMainId(invenPlanId);
			 invenPlanMapper.delInvenPlanDetailByMainId(invenPlanId);
			 invenPlanMapper.delInvenPlanById(invenPlanId);

			 //删除任务
			 params.setSdata1(invenPlanId);
			 taskService.removeTaskBySdata1(params);
		}
	}

	@Override
	public List<BaseEntity> getSeriesByUserId(List<Long> userIds) {
		List<BaseEntity> series = materialMapper.getMateSeriesByUserIds(userIds);
		return series;
	}

	@Override
	public RestCode isExistMonthMates(Map<String, Object> map) {
		int count = invenPlanMapper.getMounthMateExistCount(map);
		if (count > 0) {
			return RestCode.error("存在已经在本月备货计划计划中存在的物料！");
		}
		return new RestCode();
	}

	@Override
	public List<SuppProd> initalPlanDetailsData(String planMonth, String mainId) {
		String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		Date month = DateUtils.parse(planMonth, "yyyy-MM");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("planMonth", planMonth);
		List<SuppProd> list = invenPlanMapper.findPlanDetailsDataByParams(params);
		String tempCode = "";// 记录物料编码
		int suppNum = 0;// 记录供应商数量
		BigDecimal aveNum = new BigDecimal(0);// 记录平均值
		BigDecimal lastNum = new BigDecimal(0);// 记录最后一个
		int count = 0;// 记录该物料处理的次数
		List<String> mateSupp = invenPlanMapper.findInvenPlanDetailsMateCodeAndSuppNoByMainID(mainId);
		List<SuppProd> result = new ArrayList<SuppProd>();
		for (SuppProd suppProd : list) {
			String mateCode = suppProd.getMateCode();
			String newMateSupp = mateCode + "," + suppProd.getSuppNo();
			if (mateSupp.contains(newMateSupp)) {
				Map<String, Object> query = new HashMap<String, Object>();
				query.put("mainId", mainId);
				query.put("mateCode", suppProd.getMateCode());
				query.put("suppNo", suppProd.getSuppNo());
				// 分配交货计划
				if ("".equals(tempCode) || !tempCode.equals(mateCode)) {
					// 该物料第一次分配，需要查询数据；
					params.put("mateCode", mateCode);
					suppNum = invenPlanMapper.countSuppNumByParams(params);
					tempCode = mateCode;
					BigDecimal deliveryPlan = suppProd.getDeliveryPlan() != null ? suppProd.getDeliveryPlan()
							: new BigDecimal(0);
					// 平均分配排产计划
					BigDecimal[] divideAndRemainder = deliveryPlan.divideAndRemainder(new BigDecimal(suppNum));
					aveNum = divideAndRemainder[0];
					lastNum = divideAndRemainder[1].add(aveNum);
					count++;
				} else {
					// 判断是否是某物料的最后一个供应商
					if ((suppNum - count) == 1) {
						count = 0;
					} else {
						count++;
					}
				}
				suppProd = invenPlanMapper.findSuppProdByParams(query);
			} else {
				suppProd.setPlanMonth(month);
				suppProd.setCreator(userId);
				suppProd.setId(UUIDUtil.getUUID());
				String suppNo = suppProd.getSuppNo();
				// 分配交货计划
				if ("".equals(tempCode) || !tempCode.equals(mateCode)) {
					// 该物料第一次分配，需要查询数据；
					params.put("mateCode", mateCode);
					suppNum = invenPlanMapper.countSuppNumByParams(params);
					tempCode = mateCode;
					BigDecimal deliveryPlan = suppProd.getDeliveryPlan() != null ? suppProd.getDeliveryPlan()
							: new BigDecimal(0);
					// 平均分配排产计划
					BigDecimal[] divideAndRemainder = deliveryPlan.divideAndRemainder(new BigDecimal(suppNum));
					aveNum = divideAndRemainder[0];
					lastNum = divideAndRemainder[1].add(aveNum);
					count++;
					suppProd.setDeliveryPlan(aveNum);
				} else {
					// 判断是否是某物料的最后一个供应商
					if ((suppNum - count) == 1) {
						count = 0;
						suppProd.setDeliveryPlan(lastNum);
					} else {
						count++;
						suppProd.setDeliveryPlan(aveNum);
					}
				}
				suppProd.setMainId(mainId);
				// 获取期初订单（当前月的话取上个月的未完成订单否则有疑问）
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("suppNo", suppNo);
				map.put("mateCode", mateCode);
				map.put("planMonth", month);
				Double unpaidNum = orderService.getUnpaidNum(map);
				// 期初订单
				suppProd.setBeginOrder(new BigDecimal(unpaidNum));
				// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
				BigDecimal beginStock = new BigDecimal(0);
				if (currMonthStr.equals(planMonth)) {
					PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
					if (pdrDetail != null && pdrDetail.getStockQty() != null) {
						beginStock = new BigDecimal(pdrDetail.getStockQty());
					}
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(month);
					cal.add(Calendar.MONTH, -1);
					map.put("planMonth", cal.getTime());
					List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
					if (prodList != null && prodList.size() > 0) {
						SuppProd suppProd2 = prodList.get(0);
						if (suppProd2 != null && suppProd2.getEndStock() != null) {
							beginStock = suppProd2.getEndStock();
						}
					}
				}
				// 期初库存
				suppProd.setBeginStock(beginStock);
				// 期初可生产订单 = 期初订单-期初库存
				BigDecimal beginEnableOrder = new BigDecimal(unpaidNum).subtract(beginStock);
				suppProd.setBeginEnableOrder(beginEnableOrder);
			}
			result.add(suppProd);
		}
		return result;
	}

	@Override
	public List<SuppProd> getReportByMate(SuppProd suppProd, Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownId", userId);
		params.put("orgCode", "");
		params.put("isContainOwn", true);
		List<UserDO> userIds = orgService.manageSubordinateUsers(params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userIds", userIds);
		map.put("suppProd", suppProd);
		List<SuppProd> list = invenPlanMapper.getReportByMate(map);
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal bg100 = new BigDecimal(100);
		for (SuppProd suppProd2 : list) {
			String safeScale = "";
			BigDecimal nextDeliveryNum = suppProd2.getNextDeliveryNum();
			BigDecimal endStock = suppProd2.getEndStock();
			if (endStock == null) {
				endStock = bg0;
			}
			if (nextDeliveryNum != null && nextDeliveryNum.compareTo(bg0) != 0) {
				safeScale = endStock.divide(nextDeliveryNum, 4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)
						+ "%";
			}
			suppProd2.setSafeScale(safeScale);
		}
		return list;
	}

	@Override
	public List<SuppProd> getReportBySupp(Map<String, Object> params) {
		List<SuppProd> list = invenPlanMapper.getReportBySupp(params);
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal bg100 = new BigDecimal(100);
		for (SuppProd suppProd2 : list) {
			String safeScale = "";
			BigDecimal nextDeliveryNum = suppProd2.getNextDeliveryNum();
			BigDecimal endStock = suppProd2.getEndStock();
			if (endStock == null) {
				endStock = bg0;
			}
			if (nextDeliveryNum != null && nextDeliveryNum.compareTo(bg0) != 0) {
				safeScale = endStock.divide(nextDeliveryNum, 4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)
						+ "%";
			}
			suppProd2.setSafeScale(safeScale);
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public RestCode saveInvenPlanInfo(InvenPlan invenPlan, List<SuppProd> details) {
		String mainId = invenPlan.getId();
		Date planMonth2 = invenPlan.getPlanMonth();
		BigDecimal bg0 = new BigDecimal(0);
		// 校验数据合法性 获取物料列表
		Set<String> mateCodeSet = new HashSet<>();
		for (SuppProd suppProd : details) {
			String mateCode = suppProd.getMateCode();
			mateCodeSet.add(mateCode);
		}
		// 获取这些物料在数据在数据库中的数据
		Map<String, Object> map = new HashMap<>();
		map.put("planMonth", planMonth2);
		// 校验子表数据
		if (mateCodeSet != null && mateCodeSet.size() > 0) {
			map.put("mateCodes", mateCodeSet);
			// 获取数据库子表数据 如果存在则做校验
			List<SuppProd> suppProds = invenPlanMapper.getInvenPlanDetaiByMainId(map);
			int size = 0;
			if (suppProds == null || suppProds.size() == 0) {
				suppProds = details;
			} else {
				// 数据重新获取
				size = suppProds.size();
				for (int i = size - 1; i >= 0; i--) {
					SuppProd suppProd2 = suppProds.get(i);
					for (SuppProd suppProd : details) {
						if (suppProd2.equals(suppProd)) {
							suppProd2 = suppProd;
							suppProds.set(i, suppProd2);
						}
					}

				}
			}
			// 循环校验每个物料总和是否合法
			String tempMateCode = suppProds.get(0).getMateCode();
			BigDecimal mateDlvNum = new BigDecimal(0);
			for (int i = 0; i < size; i++) {
				SuppProd suppProd = suppProds.get(i);
				String mateCode = suppProd.getMateCode();
				// 循环到结束位置
				if (!mateCode.equals(tempMateCode)) {
					//
					map.put("planMonth", DateUtils.format(planMonth2, "yyyy-MM"));
					map.put("mateCode", tempMateCode);
					map.put("status", "已提交");
					List<PadPlanDetail> padPlans = padPlanMapper.getPadPlanDetailByMap(map);
					if (padPlans != null && padPlans.size() > 0) {
						PadPlanDetail padPlanDetail = padPlans.get(0);
						Float padPlanQty = padPlanDetail.getPadPlanQty();
						if (padPlanQty == null) {
							padPlanQty = 0F;
						}
						if (mateDlvNum.compareTo(new BigDecimal(padPlanQty)) != 0) {
							return RestCode.error("物料" + tempMateCode + "计划交货的物料数量(" + padPlanQty + ")与供应商分配的物料数量("
									+ mateDlvNum + ")不相等，请检查修正后保存！");
						}
					} else {
						return RestCode.error("物料" + tempMateCode + "在生产交货计划中不存在！");
					}
					tempMateCode = mateCode;
					mateDlvNum = bg0;
				}
				BigDecimal deliveryPlan = suppProd.getDeliveryPlan();
				if (deliveryPlan != null) {
					mateDlvNum = mateDlvNum.add(deliveryPlan);
				}
			}
		}
		SysUserDO user = UserCommon.getUser();
		InvenPlan plan = invenPlanMapper.getInvenPlanById(mainId);
		int i = 0;
		if (plan != null) {
			// 走更新
			invenPlanMapper.updateInvenPlan(invenPlan);
			// 判断是否需要删除之前的备货计划详情数据
			Date oldMonth = plan.getPlanMonth();
			int month = oldMonth.getMonth();
			Date planMonth = invenPlan.getPlanMonth();
			int month2 = planMonth.getMonth();
			if (month2 == month) {
				// 相等则走更新
				// 获取详情表的ID的list
				List<String> detailsIDs = invenPlanMapper.findInvenPlanDetailIDsByMainId(mainId);
				List<SuppProd> addList = new ArrayList<SuppProd>();
				for (SuppProd suppProd : details) {
					String id = suppProd.getId();
					if (detailsIDs.contains(id)) {
						i += invenPlanMapper.updateInvenPlanDetails(suppProd);
					} else {
						addList.add(suppProd);
					}
				}
				if (addList.size() > 0)
					i += invenPlanMapper.batchSaveInvenPlanDetails(addList);
			} else {
				// 删除并新增
				int k = invenPlanMapper.removeInvenPlanDetailsByMainId(mainId);
				if (k > 0)
					i += invenPlanMapper.batchSaveInvenPlanDetails(details);
			}
		} else {
			// 新增
			String code = codeService.getCodeByCodeType("InvenPlanNo");
			invenPlan.setPlanCode(code);
			invenPlan.setCreater(user.getUserId().toString());
			invenPlan.setCreateUser(user.getName());
			invenPlan.setCreateTime(new Date());
			invenPlanMapper.saveInvenPlan(invenPlan);
			i += invenPlanMapper.batchSaveInvenPlanDetails(details);
		}
		String status = invenPlan.getStatus();
		if ("已提交".equals(status)) {
			Date planMonth = invenPlan.getPlanMonth();
			Calendar cal = Calendar.getInstance();
			cal.setTime(planMonth);
			cal.add(Calendar.MONTH, -1);
			// 上个月时间
			Date beforeDate = new Date(cal.getTimeInMillis());
			// 更新上个月的安全库存率
			Map<String, Object> params = new HashMap<>();
			params.put("planMonth", beforeDate);
			for (SuppProd suppProd : details) {
				params.put("mateCode", suppProd.getMateCode());
				params.put("suppNo", suppProd.getSuppNo());
				List<SuppProd> beforeProds = suppProdMapper.getSuppProdByMap(params);
				for (SuppProd beforeProd : beforeProds) {
					// 当月的交货计划
					BigDecimal deliveryPlan = suppProd.getDeliveryPlan();
					beforeProd.setNextDeliveryNum(deliveryPlan);
					BigDecimal beforeEndStock = beforeProd.getEndStock();
					String beforeSafeScale = "";
					if (deliveryPlan != null && !deliveryPlan.equals(0)) {
						beforeSafeScale = beforeEndStock.divide(deliveryPlan).multiply(new BigDecimal(100)) + "%";
					}
					params.put("safeScale", beforeSafeScale);
					params.put("nextDeliveryNum", deliveryPlan);
					suppProdMapper.changeSafeScale(params);
				}
			}
		}
		return new RestCode();
	}

	@Override
	public int countInvenPlanByIdAndPlanMonth(InvenPlan plan) {
		SysUserDO user = UserCommon.getUser();
		plan.setCreater(user.getUserId().toString());
		int i = invenPlanMapper.countInvenPlanByIdAndPlanMonth(plan);
		return i;
	}

	@Override
	public List<SuppProd> getPlanDetailForLeader(String planMonth) {
		Map<String, Object> params = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		Date month = DateUtils.parse(planMonth, "yyyy-MM");
		params.put("planMonth", month);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ownId", user.getUserId());
		map.put("orgCode", "");
		map.put("isContainOwn", true);
		List<UserDO> users = orgService.manageSubordinateUsers(map);
		params.put("users", users);
		List<SuppProd> list = invenPlanMapper.getPlanDetailForLeader(params);
		return list;
	}

	@Override
	public RestCode saveInvenPlanAuditInfo(InvenPlan invenPlan, List<SuppProd> details) {
		String mainId = invenPlan.getId();
		Date planMonth2 = invenPlan.getPlanMonth();
		BigDecimal bg0 = new BigDecimal(0);
		// 校验数据合法性 获取物料列表
		Set<String> mateCodeSet = new HashSet<>();
		for (SuppProd suppProd : details) {
			String mateCode = suppProd.getMateCode();
			mateCodeSet.add(mateCode);
		}
		// 获取这些物料在数据在数据库中的数据
		Map<String, Object> map = new HashMap<>();
		map.put("planMonth", planMonth2);
		// 校验子表数据
		if (mateCodeSet != null && mateCodeSet.size() > 0) {
			map.put("mateCodes", mateCodeSet);
			// 获取数据库子表数据 如果存在则做校验
			List<SuppProd> suppProds = invenPlanMapper.getInvenPlanDetaiByMainId(map);
			int size = 0;
			if (suppProds == null || suppProds.size() == 0) {
				suppProds = details;
			} else {
				// 数据重新获取
				size = suppProds.size();
				for (int i = size - 1; i >= 0; i--) {
					SuppProd suppProd2 = suppProds.get(i);
					for (SuppProd suppProd : details) {
						if (suppProd2.equals(suppProd)) {
							suppProd2 = suppProd;
							suppProds.set(i, suppProd2);
						}
					}

				}
			}
			// 循环校验每个物料总和是否合法
			String tempMateCode = suppProds.get(0).getMateCode();
			BigDecimal mateDlvNum = new BigDecimal(0);
			for (int i = 0; i < size; i++) {
				SuppProd suppProd = suppProds.get(i);
				String mateCode = suppProd.getMateCode();
				// 循环到结束位置
				if (!mateCode.equals(tempMateCode)) {
					//
					map.put("planMonth", DateUtils.format(planMonth2, "yyyy-MM"));
					map.put("mateCode", tempMateCode);
					map.put("status", "已提交");
					List<PadPlanDetail> padPlans = padPlanMapper.getPadPlanDetailByMap(map);
					if (padPlans != null && padPlans.size() > 0) {
						PadPlanDetail padPlanDetail = padPlans.get(0);
						Float padPlanQty = padPlanDetail.getPadPlanQty();
						if (padPlanQty == null) {
							padPlanQty = 0F;
						}
						if (mateDlvNum.compareTo(new BigDecimal(padPlanQty)) != 0) {
							return RestCode.error("物料" + tempMateCode + "计划交货的物料数量(" + padPlanQty + ")与供应商分配的物料数量("
									+ mateDlvNum + ")不相等，请检查修正后保存！");
						}
					} else {
						return RestCode.error("物料" + tempMateCode + "在生产交货计划中不存在！");
					}
					tempMateCode = mateCode;
					mateDlvNum = bg0;
				}
				BigDecimal deliveryPlan = suppProd.getDeliveryPlan();
				if (deliveryPlan != null) {
					mateDlvNum = mateDlvNum.add(deliveryPlan);
				}
			}
		}
		int i = 0;
		for (SuppProd suppProd : details) {
			// 循环更新
			i += invenPlanMapper.updateInvenPlanDetails(suppProd);
		}
		String status = invenPlan.getStatus();
		if ("已审核".equals(status)) {
			// 更新本条备货数据状态和他下属的采购员所有的状态
			invenPlanMapper.updateInvenPlan(invenPlan);
			// 根据采购组长id和计划月份更新
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("planMonth", invenPlan.getPlanMonth());
			SysUserDO user = UserCommon.getUser();
			params.put("leaderId", user.getUserId());
			invenPlanMapper.updateInvenPlanDetailsByParams(params);
		}
		return new RestCode();
	}

	@Override
	public List<Dic> getItemList(Map<String, Object> map) {
		return invenPlanMapper.getItemList(map);
	}

	@Override
	public List<MateDO> getMateSelectList(Map<String, Object> map) {
		return invenPlanMapper.getMateSelectList(map);
	}

	@Override
	public List<BaseEntity> getProdSeriers(Map<String, Object> map) {
		return invenPlanMapper.getProdSeriers(map);
	}

	@Override
	public List<QualSupp> getSuppList(Map<String, Object> map) {
		return invenPlanMapper.getSuppList(map);
	}
	
	
	//------------------------根据汇总生成备货计划详情开始3-----------------------------
	
	/**
	 * 根据月份初始化备货详情数据
	 * @param planMonth
	 * @param mainId
	 * @return
	 */
	@Override
	public List<InvenPlanDetail> initPlanDetailsData(String planMonth ) {
		SysUserDO user = UserCommon.getUser();
		Date month = DateUtils.parse(planMonth, "yyy-MM");
		Long userId = user.getUserId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("buyerId", userId);
		params.put("planMonth", planMonth);
		List<InvenPlanDetail> list = invenPlanMapper.findPlanDetailsByParams(params);
		for (InvenPlanDetail detail : list) {
			// 来自生产交货计划
			detail.setStatus("未分解");
			detail.setId(UUIDUtil.getUUID());
			detail.setPlanMonth(month);
			// 获取期初订单数据 期初库存
			String mateCode = detail.getMateCode();
			params.put("planMonth", month);
			params.put("mateCode", detail.getMateCode());
			//获取期初库存
			List<QualSupp> qualSupps = qualSuppMapper.queryQualSuppOfMateByMateCode(params);
			Map<String, Object> map=new HashMap<>();
			map.put("mateCode", mateCode);
			map.put("planMonth", month);
			List<String> suppNos=new ArrayList<>();
			BigDecimal beginStock=new BigDecimal(0);
			//两种获取方式  当前月以及当前月以前 从产能上报获取   当前月以后获取上个月的期末库存
			
			String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
			if (currMonthStr.equals(planMonth) || month.before(new Date())) {
				for (QualSupp qualSupp : qualSupps) {
					String suppNo = qualSupp.getSapId();
					suppNos.add(suppNo);
					map.put("suppNo", suppNo);
					PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
					if (pdrDetail != null && pdrDetail.getStockQty() != null) {
						beginStock = BigDecimalUtil.add(beginStock, new BigDecimal(pdrDetail.getStockQty()));
					}
				}
			}else{
				for (QualSupp qualSupp : qualSupps) {
					String suppNo = qualSupp.getSapId();
					suppNos.add(suppNo);
				}
				Calendar cal=Calendar.getInstance();
				cal.setTime(month);
				cal.add(Calendar.MONTH, -1);

				map.put("planMonth",cal.getTime());
				InvenPlanDetail lastDetail = invenPlanMapper.getPlanDetailByMateCode(map);
				if(lastDetail!=null){
					beginStock=lastDetail.getEndStock();
				}
			}
			map.put("planMonth",month);
			// 获取期初订单数据 期初库存
			BigDecimal beginOrder=new BigDecimal(0);
			if(suppNos!=null && suppNos.size()>0){
				params.put("suppNos",suppNos );
				Double unpaidNum = orderMonthMapper.selectUndeliveredNumByMap(params);
				beginOrder = new BigDecimal(unpaidNum);
				params.remove("suppNos");
			}
			//BigDecimal beginStock = detail.getBeginStock();
			detail.setBeginOrder(beginOrder);
			detail.setBeginStock(beginStock);
			detail.setBeginEnableOrder(BigDecimalUtil.subtract(beginOrder, beginStock));
			// 获取下一个月的交货计划（有疑问）
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(month);
			calendar.add(Calendar.MONTH, 1);
			params.put("planMonth", DateUtils.format(new Date(calendar.getTimeInMillis()), "yyyy-MM"));
			List<PadPlanDetail> padDetails = padPlanMapper.getPadPlanDetailByMap(params);
			if (padDetails != null && padDetails.size() > 0) {
				PadPlanDetail padDetail = padDetails.get(0);
				if (padDetail != null) {
					Float padPlanQty = padDetail.getPadPlanQty();
					detail.setNextMonthDeliveryNum(new BigDecimal(padPlanQty == null ? 0 : padPlanQty));
				}
			}
		}
		return list;
	}
	
	@Override
	public List<InvenPlanDetail> getMatePlanDetailByMainId(Map<String, Object> map) {
		List<InvenPlanDetail> list = invenPlanMapper.getMatePlanDetaiByMainId(map);
		//判断分配数量是否一致
		for (InvenPlanDetail invenPlanDetail : list) {
			String planDetailId = invenPlanDetail.getId();
			BigDecimal deliveryPlan = invenPlanDetail.getDeliveryPlan();
			BigDecimal sumDlvNum = suppProdMapper.getPlanDlvNumByPlanDetailId(planDetailId);
			boolean isEqual = BigDecimalUtil.equalVal(sumDlvNum, deliveryPlan);
			invenPlanDetail.setEqual(isEqual);
		}
		return list;
	}

	@Override
	public List<Dic> getItemInfo(Map<String, Object> map) {
		return invenPlanMapper.getItemInfo(map);
	}

	@Override
	public List<MateDO> getMateSelectInfo(Map<String, Object> map) {
		return invenPlanMapper.getMateSelectInfo(map);
	}

	@Override
	public List<BaseEntity> getSelectProdSeriers(Map<String, Object> map) {
		return invenPlanMapper.getSelectProdSeriers(map);
	}

	@Override
	public List<SuppProd> getSuppProdBySuppNo(Map<String, Object> params) {
		return invenPlanMapper.getSuppProdBySuppNo(params);
	}

	//------------------------根据汇总生成备货计划详情结束3-----------------------------
	

	//------------------------根据备货计划的更新开始-----------------------------
	@Override
	@Transactional
	public void updateInvenMate(String planCode) {
		//根据备货计划编码获取备货计划
		InvenPlan invenPlan = invenPlanMapper.getInvenPlanByCode(planCode);
		Date planMonth = invenPlan.getPlanMonth();
		String creater = invenPlan.getCreater();
		String invenPlanId = invenPlan.getId();
		//获取备货计划物料详情
		Map<String, Object> map = new HashMap<>();
		map.put("mainId", invenPlanId);
		List<InvenPlanDetail> matePlanDetails = invenPlanMapper.getMatePlanDetaiByMainId(map);
		//获取生产交货计划新的物料数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", creater);
		params.put("buyerId", creater);
		params.put("planMonth", DateUtils.format(planMonth, "yyyy-MM"));
		List<InvenPlanDetail> list = invenPlanMapper.findPlanDetailsByParams(params);
		//数据更新或者插入
		map.put("planCode", planCode);
		map.put("creater", creater);

		for (InvenPlanDetail invenPlanDetail : list) {
			String mateCode2 = invenPlanDetail.getMateCode();
			map.put("mateCode", mateCode2);
			InvenPlanDetail oldDetail = invenPlanMapper.getPlanDetailByMateCode(map);
			if(oldDetail==null){
//				新添加数据  所有的数据都需要重新获取
				invenPlanDetail.setStatus("未分解");
				invenPlanDetail.setId(UUIDUtil.getUUID());
				invenPlanDetail.setPlanMonth(planMonth);
				invenPlanDetail.setMainId(invenPlanId);
				invenPlanDetail.setPlanCode(planCode);
				// 获取期初订单数据 期初库存
				String mateCode = invenPlanDetail.getMateCode();
				params.put("planMonth", planMonth);
				params.put("mateCode", mateCode2);
				//获取期初库存
				List<QualSupp> qualSupps = qualSuppMapper.queryQualSuppOfMateByMateCode(params);
				Map<String, Object> beginStockMap=new HashMap<>();
				beginStockMap.put("mateCode", mateCode);
				beginStockMap.put("planMonth", planMonth);
				List<String> suppNos=new ArrayList<>();
				BigDecimal beginStock=new BigDecimal(0);
				String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
				if (currMonthStr.equals(planMonth) || planMonth.before(new Date())) {
					for (QualSupp qualSupp : qualSupps) {
						String suppNo = qualSupp.getSapId();
						suppNos.add(suppNo);
						beginStockMap.put("suppNo", suppNo);
						PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(beginStockMap);
						if (pdrDetail != null && pdrDetail.getStockQty() != null) {
							beginStock = BigDecimalUtil.add(beginStock, new BigDecimal(pdrDetail.getStockQty()));
						}
					}
				}else{
					for (QualSupp qualSupp : qualSupps) {
						String suppNo = qualSupp.getSapId();
						suppNos.add(suppNo);
					}
					Calendar cal=Calendar.getInstance();
					cal.setTime(planMonth);
					cal.add(Calendar.MONTH, -1);

					beginStockMap.put("planMonth",cal.getTime());
					InvenPlanDetail lastDetail = invenPlanMapper.getPlanDetailByMateCode(map);
					if(lastDetail!=null){
						beginStock=lastDetail.getEndStock();
					}
				}			
				// 获取期初订单数据 期初库存
				BigDecimal beginOrder=new BigDecimal(0);
				if(suppNos!=null && suppNos.size()>0){
					params.put("suppNos",suppNos );
					Double unpaidNum = orderMonthMapper.selectUndeliveredNumByMap(params);
					beginOrder = new BigDecimal(unpaidNum);
					params.remove("suppNos");
				}
				//BigDecimal beginStock = detail.getBeginStock();
				invenPlanDetail.setBeginOrder(beginOrder);
				invenPlanDetail.setBeginStock(beginStock);
				invenPlanDetail.setBeginEnableOrder(BigDecimalUtil.subtract(beginOrder, beginStock));
				// 获取下一个月的交货计划（有疑问）
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(planMonth);
				calendar.add(Calendar.MONTH, 1);
				params.put("planMonth", DateUtils.format(new Date(calendar.getTimeInMillis()), "yyyy-MM"));
				List<PadPlanDetail> padDetails = padPlanMapper.getPadPlanDetailByMap(params);
				if (padDetails != null && padDetails.size() > 0) {
					PadPlanDetail padDetail = padDetails.get(0);
					if (padDetail != null) {
						Float padPlanQty = padDetail.getPadPlanQty();
						invenPlanDetail.setNextMonthDeliveryNum(new BigDecimal(padPlanQty == null ? 0 : padPlanQty));
					}
				}
				invenPlanMapper.saveInvenPlanDetail(invenPlanDetail);
			}else{
				String planDetailId = oldDetail.getId();
				String mateCode = invenPlanDetail.getMateCode();
				params.put("planMonth", planMonth);
				params.put("mateCode", mateCode2);
				//获取期初库存
				List<QualSupp> qualSupps = qualSuppMapper.queryQualSuppOfMateByMateCode(params);
				deleteSuppProdByQualSupp(planDetailId, qualSupps);
				Map<String, Object> beginStockMap=new HashMap<>();
				beginStockMap.put("mateCode", mateCode);
				beginStockMap.put("planMonth", planMonth);
				List<String> suppNos=new ArrayList<>();
				BigDecimal newBeginStock=new BigDecimal(0);
				String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
				if (currMonthStr.equals(planMonth) || planMonth.before(new Date())) {
					for (QualSupp qualSupp : qualSupps) {
						String suppNo = qualSupp.getSapId();
						suppNos.add(suppNo);
						beginStockMap.put("suppNo", suppNo);
						PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(beginStockMap);
						if (pdrDetail != null && pdrDetail.getStockQty() != null) {
							newBeginStock = BigDecimalUtil.add(newBeginStock, new BigDecimal(pdrDetail.getStockQty()));
						}
					}
				}else{
					for (QualSupp qualSupp : qualSupps) {
						String suppNo = qualSupp.getSapId();
						suppNos.add(suppNo);
					}
					Calendar cal=Calendar.getInstance();
					cal.setTime(planMonth);
					cal.add(Calendar.MONTH, -1);

					beginStockMap.put("planMonth",cal.getTime());
					InvenPlanDetail lastDetail = invenPlanMapper.getPlanDetailByMateCode(beginStockMap);
					if(lastDetail!=null){
						newBeginStock=lastDetail.getEndStock();
					}
				}
				// 获取期初订单数据 期初库存
				BigDecimal newBeginOrder=new BigDecimal(0);
				if(suppNos!=null && suppNos.size()>0){
					params.put("suppNos",suppNos );
					Double unpaidNum = orderMonthMapper.selectUndeliveredNumByMap(params);
					newBeginOrder = new BigDecimal(unpaidNum);
					params.remove("suppNos");
				}
				//BigDecimal beginStock = detail.getBeginStock();
				oldDetail.setBeginOrder(newBeginOrder);
				oldDetail.setBeginStock(newBeginStock);
				oldDetail.setBeginEnableOrder(BigDecimalUtil.subtract(newBeginOrder, newBeginStock));
				// 获取下一个月的交货计划（有疑问）
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(planMonth);
				calendar.add(Calendar.MONTH, 1);
				params.put("planMonth", DateUtils.format(new Date(calendar.getTimeInMillis()), "yyyy-MM"));
				List<PadPlanDetail> padDetails = padPlanMapper.getPadPlanDetailByMap(params);
				if (padDetails != null && padDetails.size() > 0) {
					PadPlanDetail padDetail = padDetails.get(0);
					if (padDetail != null) {
						Float padPlanQty = padDetail.getPadPlanQty();
						oldDetail.setNextMonthDeliveryNum(new BigDecimal(padPlanQty == null ? 0 : padPlanQty));
					}
				}
//				原有数据     更新交货计划  期末库存 安全库存率
				BigDecimal newDeliveryPlan = invenPlanDetail.getDeliveryPlan();
				BigDecimal prodPlan = oldDetail.getProdPlan();
				BigDecimal nextMonthDeliveryNum = oldDetail.getNextMonthDeliveryNum();
				BigDecimal beginStock = oldDetail.getBeginStock();
				BigDecimal newEndStock = BigDecimalUtil.subtract(BigDecimalUtil.add(beginStock, prodPlan), newDeliveryPlan);
				oldDetail.setDeliveryPlan(newDeliveryPlan);
				oldDetail.setEndStock(newEndStock);
				
				oldDetail.setRanking(invenPlanDetail.getRanking());
				oldDetail.setProdSeriesCode(invenPlanDetail.getProdSeriesCode());
				oldDetail.setProdSeriesDesc(invenPlanDetail.getProdSeriesDesc());
				oldDetail.setItemCode(invenPlanDetail.getItemCode());
				oldDetail.setItemName(invenPlanDetail.getItemName());
				
				oldDetail.setSafeScale(BigDecimalUtil.getPercentage(newEndStock, nextMonthDeliveryNum));
				invenPlanMapper.updateInvenPlanDetail(oldDetail);
			}
		}
		//数据删除
		List<String> delIds=new ArrayList<>();
		for (InvenPlanDetail invenPlanDetail : matePlanDetails) {
			String mateCode = invenPlanDetail.getMateCode();
			//默认为不需要删除的数据  双重循环  看物料是否应该删除
			boolean flag=true;
			for (InvenPlanDetail oldDetail : list) {
				String mateCode2 = oldDetail.getMateCode();
				if(mateCode.equals(mateCode2)){
					flag=false;
				}
			}
			if(flag){
				String detailId = invenPlanDetail.getId();
				delIds.add(detailId);
			}
		}
		if(delIds!=null && delIds.size()>0){
			invenPlanMapper.delInvenPlanDetail(delIds);
			for (String planDetailId : delIds) {
				List<SuppProd> suppProds = suppProdMapper.getSuppProdByPlanDetailId(planDetailId);
				for (SuppProd suppProd : suppProds) {
					//删除排产计划数据  以及排产详情数据
					String suppProdId = suppProd.getId();
					suppProdMapper.delSuppProdPlanByMainId(suppProdId);
					suppProdMapper.delSuppProdById(suppProdId);
				}
			}
		}
		//更新排产数据
		map.clear();
		map.put("mainId", invenPlanId);
		String currMonthStr=DateUtils.format(new Date(), "yyyy-MM");
		String planMonthStr=DateUtils.format(planMonth, "yyyy-MM");
		List<InvenPlanDetail> invenPlanDetails = invenPlanMapper.getMatePlanDetaiByMainId(map);
		for (InvenPlanDetail invenPlanDetail : invenPlanDetails) {
			String detailId = invenPlanDetail.getId();
			String ranking = invenPlanDetail.getRanking();
			String itemCode = invenPlanDetail.getItemCode();
			String itemName = invenPlanDetail.getItemName();
			String prodSeriesCode = invenPlanDetail.getProdSeriesCode();
			String prodSeriesDesc = invenPlanDetail.getProdSeriesDesc();
			String mateCode = invenPlanDetail.getMateCode();
			
			List<SuppProd> suppList = suppProdMapper.getSuppProdByPlanDetailId(detailId);
			for (SuppProd suppProd : suppList) {
				String suppNo = suppProd.getSuppNo();
				map.put("suppNo", suppNo);
				map.put("mateCode", mateCode);
				map.put("planMonth", planMonth);
				//更新供应商简称
				QualSupp qualSupp = qualSuppMapper.queryQualSuppBySapId(suppNo);
				suppProd.setRanking(ranking);
				suppProd.setItemCode(itemCode);
				suppProd.setItemName(itemName);
				if(qualSupp!=null){
					suppProd.setSuppName(qualSupp.getSuppAbbre());
				}
				suppProd.setProdSeriesCode(prodSeriesCode);
				suppProd.setProdSeriesDesc(prodSeriesDesc);
				//期初订单
				Double undeliveredNum = orderMonthMapper.selectUndeliveredNumByMap(map);
				BigDecimal beginOrder=new BigDecimal(undeliveredNum);
				suppProd.setBeginOrder(beginOrder);
				// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
				BigDecimal beginStock = new BigDecimal(0);
				if (currMonthStr.equals(planMonthStr) || planMonth.before(new Date())) {
					PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
					if (pdrDetail != null && pdrDetail.getStockQty() != null) {
						beginStock = new BigDecimal(pdrDetail.getStockQty());
					}
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(planMonth);
					cal.add(Calendar.MONTH, -1);
					map.put("planMonth", cal.getTime());
					List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
					if (prodList != null && prodList.size() > 0) {
						SuppProd suppProd2 = prodList.get(0);
						if (suppProd2 != null && suppProd2.getEndStock() != null) {
							beginStock = suppProd2.getEndStock();
						}
					}
				}
				suppProd.setBeginStock(beginStock);
				//期初可生产订单
				suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginOrder,beginStock ));
				//期末预计库存
				BigDecimal prodPlan = suppProd.getProdPlan();
				BigDecimal deliveryPlan = suppProd.getDeliveryPlan();
				BigDecimal endStock = BigDecimalUtil.subtract(BigDecimalUtil.add(beginStock, prodPlan), deliveryPlan);
				suppProd.setEndStock(endStock);
				Calendar cal = Calendar.getInstance();
				cal.setTime(planMonth);
				cal.add(Calendar.MONTH, 1);
				map.put("planMonth", cal.getTime());
				List<SuppProd> nextProds = suppProdMapper.getSuppProdByMap(map);
				BigDecimal nextDeliveryPlan=BigDecimal.ZERO;
				for (SuppProd nextProd : nextProds) {
					//当月的交货计划
					BigDecimal num = nextProd.getDeliveryPlan();
					nextDeliveryPlan=BigDecimalUtil.add(num, nextDeliveryPlan);
				}
				String safeScale=suppProd.getSafeScale();
				safeScale=BigDecimalUtil.getPercentage(endStock, nextDeliveryPlan);
				suppProd.setSafeScale(safeScale);
				suppProd.setNextDeliveryNum(nextDeliveryPlan);
				suppProdMapper.updateSuppProd(suppProd);
			}
		}
		
	}
	//------------------------根据备货计划的更新结束-----------------------------

	
	//------------------------备货计划导入开始-----------------------------

	@Override
	@Transactional
	public List<SuppProdVo> getExportTempData(String planCode) {
		// 获取当前物料的供应商

		String currMonthStr = DateUtils.format(new Date(), "yyyy-MM");
		List<SuppProdVo> list=new ArrayList<>();
		InvenPlan invenPlan = invenPlanMapper.getInvenPlanByCode(planCode);
		Date planMonth = invenPlan.getPlanMonth();
		String planMonthStr = DateUtils.format(planMonth, "yyyy-MM");
		String creater = invenPlan.getCreater();
		Long userId = Long.parseLong(creater);
		String invenPlanId = invenPlan.getId();
		//获取当前月的物料
		Map<String, Object> map=new HashMap<>();
		map.put("mainId", invenPlanId);
		List<InvenPlanDetail> details = invenPlanMapper.getMatePlanDetaiByMainId(map);
		for (InvenPlanDetail detail : details) {
			//判断是否分解   如果已分解怎不需要平均分配交货计划    如果已排产 则查看是否有新增的供应商   如果新增则 生产交货计划均为0
			String planDetailId = detail.getId();
			String status = detail.getStatus();
			String mateCode = detail.getMateCode();
			String mateDesc = detail.getMateDesc();
			BigDecimal deliveryPlan = detail.getDeliveryPlan();
			String detailId = detail.getId();
			String ranking = detail.getRanking();
			Material mate = materialMapper.queryMaterialByMateCode(mateCode);
			String itemCode = detail.getItemCode();
			String itemName = detail.getItemName();
			String prodSeriesCode = detail.getProdSeriesCode();
			String prodSeriesDesc = detail.getProdSeriesDesc();
			if(status!=null && "未分解".equals(status)){
				// 获取当前物料的供应商
				map.clear();
				map.put("buyerId", userId);
				map.put("mateCode", mateCode);
				List<QualSupp> supps = qualSuppMapper.queryQualSuppOfMateByMateCode(map);
				int size = supps.size();
				if (size > 0) {
					// 平均分配排产计划
					BigDecimal[] divideAndRemainder = deliveryPlan.divideAndRemainder(new BigDecimal(size));
					BigDecimal divideNum = divideAndRemainder[0];
					BigDecimal remianNum = divideAndRemainder[1];
					for (int i = 0; i < size; i++) {
						QualSupp qualSupp = supps.get(i);
						SuppProdVo suppProd = new SuppProdVo();
						String suppNo = qualSupp.getSapId();
						suppProd.setSuppNo(suppNo);
						suppProd.setSuppName(qualSupp.getSuppAbbre());
						suppProd.setPlanMonth(planMonth);
						suppProd.setMainId(invenPlanId);
						suppProd.setPlanDetailId(detailId);
						suppProd.setMateCode(mateCode);
						suppProd.setMateDesc(mateDesc);
						suppProd.setRank(ranking);
						suppProd.setBoxNumber(mate.getBoxNumber());
						suppProd.setPackNumber(mate.getPackNumber());
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						// 分配交货计划
						if (i == size - 1) {
							suppProd.setPlanDlvNum(divideNum.add(remianNum));
						} else {
							suppProd.setPlanDlvNum(divideNum);
						}
						// 获取期初订单
						map.clear();
						map.put("suppNo", suppNo);
						map.put("mateCode", mateCode);
						map.put("planMonth", planMonth);
						Double undeliveredNum = orderMonthMapper.selectUndeliveredNumByMap(map);
						suppProd.setBeginOrder(new BigDecimal(undeliveredNum));
						// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
						BigDecimal beginStock = new BigDecimal(0);
						if (currMonthStr.equals(planMonthStr)) {
							PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
							if (pdrDetail != null && pdrDetail.getStockQty() != null) {
								beginStock = new BigDecimal(pdrDetail.getStockQty());
							}
						} else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(planMonth);
							cal.add(Calendar.MONTH, -1);
							map.put("planMonth", cal.getTime());
							List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
							if (prodList != null && prodList.size() > 0) {
								SuppProd suppProd2 = prodList.get(0);
								if (suppProd2 != null && suppProd2.getEndStock() != null) {
									beginStock = suppProd2.getEndStock();
								}
							}
						}
						suppProd.setBeginStock(beginStock);
						suppProd.setStatus("未排产");
						list.add(suppProd);
					}			
				}
			}else{
				map.clear();
				map.put("buyerId", userId);
				map.put("mateCode", mateCode);
				List<QualSupp> supps = qualSuppMapper.queryQualSuppOfMateByMateCode(map);
				deleteSuppProdByQualSupp(detailId, supps);
				
				List<SuppProd> suppProds = suppProdService.getSuppProdByPlanDetailId(detailId);
				//获取供应商的物料数量和
				BigDecimal suppDlvNum = suppProdMapper.getPlanDlvNumByPlanDetailId(planDetailId);
				BigDecimal divideNum=BigDecimal.ZERO;
				BigDecimal remianNum=BigDecimal.ZERO;
				if(supps!=null && supps.size()>0){
					BigDecimal[] divideAndRemainder = (BigDecimalUtil.subtract(deliveryPlan,suppDlvNum)).divideAndRemainder(new BigDecimal(supps.size()));
					divideNum = divideAndRemainder[0];
					remianNum = divideAndRemainder[1];
				}
				//最后一个的数量是除得到的数加上余数
				for (int j = 0; j < supps.size(); j++) {
					QualSupp qualSupp=supps.get(j);
					SuppProd oldSuppProd=null;
					String sapId = qualSupp.getSapId();
					//默认不包含改供应商
					boolean flag=false;
					for (SuppProd suppProd : suppProds) {
						String suppNo = suppProd.getSuppNo();
						if(suppNo.equals(sapId)){
							flag=true;
							oldSuppProd=suppProd;
						}
					}
					SuppProdVo suppProd = new SuppProdVo();
					String suppNo = qualSupp.getSapId();
					suppProd.setSuppNo(suppNo);
					suppProd.setSuppName(qualSupp.getSuppAbbre());
					suppProd.setPlanMonth(planMonth);
					suppProd.setMainId(invenPlanId);
					suppProd.setPlanDetailId(detailId);
					suppProd.setMateCode(mateCode);
					suppProd.setMateDesc(mateDesc);
					suppProd.setRank(ranking);
					suppProd.setBoxNumber(mate.getBoxNumber());
					suppProd.setPackNumber(mate.getPackNumber());
					suppProd.setItemCode(itemCode);
					suppProd.setItemName(itemName);
					suppProd.setProdSeriesCode(prodSeriesCode);
					suppProd.setProdSeriesDesc(prodSeriesDesc);
					//不包含的情况下 
					if(!flag){
						suppProd.setPlanDlvNum(new BigDecimal(0));
						// 获取期初订单
						map.clear();
						map.put("suppNo", suppNo);
						map.put("mateCode", mateCode);
						map.put("planMonth", planMonth);
						Double undeliveredNum = orderMonthMapper.selectUndeliveredNumByMap(map);
						suppProd.setBeginOrder(new BigDecimal(undeliveredNum));
						// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
						BigDecimal beginStock = new BigDecimal(0);
						if (currMonthStr.equals(planMonthStr)) {
							PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
							if (pdrDetail != null && pdrDetail.getStockQty() != null) {
								beginStock = new BigDecimal(pdrDetail.getStockQty());
							}
						} else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(planMonth);
							cal.add(Calendar.MONTH, -1);
							map.put("planMonth", cal.getTime());
							List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
							if (prodList != null && prodList.size() > 0) {
								SuppProd suppProd2 = prodList.get(0);
								if (suppProd2 != null && suppProd2.getEndStock() != null) {
									beginStock = suppProd2.getEndStock();
								}
							}
						}
						if(j==supps.size()-1){
							suppProd.setPlanDlvNum(BigDecimalUtil.add(divideNum, remianNum));
						}else{
							suppProd.setPlanDlvNum(divideNum);
						}
						suppProd.setBeginStock(beginStock);
						suppProd.setStatus("未排产");
					}else{
						//更新原来的期初订单  期初库存 期初可生产订单 期末预计库存  安全库存率
						// 获取期初订单
						map.clear();
						map.put("suppNo", suppNo);
						map.put("mateCode", mateCode);
						map.put("planMonth", planMonth);
						Double undeliveredNum = orderMonthMapper.selectUndeliveredNumByMap(map);
						suppProd.setBeginOrder(new BigDecimal(undeliveredNum));
						// 获取期初库存（当月的期初库存来自备货计划 否则来自上个月备货计划的期末库存）
						BigDecimal beginStock = new BigDecimal(0);
						if (currMonthStr.equals(planMonthStr)) {
							PdrDetail pdrDetail = pdrService.getPdrDetailBySuppMateDate(map);
							if (pdrDetail != null && pdrDetail.getStockQty() != null) {
								beginStock = new BigDecimal(pdrDetail.getStockQty());
							}
						} else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(planMonth);
							cal.add(Calendar.MONTH, -1);
							map.put("planMonth", cal.getTime());
							List<SuppProd> prodList = suppProdService.getSuppProdByMap(map);
							if (prodList != null && prodList.size() > 0) {
								SuppProd suppProd2 = prodList.get(0);
								if (suppProd2 != null && suppProd2.getEndStock() != null) {
									beginStock = suppProd2.getEndStock();
								}
							}
						}
						suppProd.setBeginStock(beginStock);
						suppProd.setPlanPrdNum(oldSuppProd.getProdPlan());
						suppProd.setStatus(oldSuppProd.getStatus());
						BigDecimal oldDlv = oldSuppProd.getDeliveryPlan();
						if(j==supps.size()-1){
							suppProd.setPlanDlvNum(BigDecimalUtil.addNums(divideNum,remianNum, oldDlv));
						}else{
							suppProd.setPlanDlvNum(BigDecimalUtil.add(divideNum, oldDlv));
						}
					}
					list.add(suppProd);
				}	
			}
		}
		//获取今后12个月的预算数据
		if( list!=null &&  list.size()>0){
			String mateCode=list.get(0).getMateCode();
			List<SuppProdVo> tempList=new ArrayList<>();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				SuppProdVo suppProd=list.get(i);
				String mateCode2 = suppProd.getMateCode();
				if(!mateCode2.equals(mateCode) ){
					updateSuppProdVo(tempList, planMonth);
					mateCode=mateCode2;
					tempList=new ArrayList<>();
					tempList.add(suppProd);
				}else{
					tempList.add(suppProd);
				}
				if(i==size-1){
					updateSuppProdVo(tempList, planMonth);
				}
			}

		}
		return list;
	}	
	//	根据月份获取该月的  生产计划  交货计划
	public void updateSuppProdVo(List<SuppProdVo> list,Date planMonth){
		//推后1个月的数据
		getSuppProdData(list,planMonth,1);
		//推后2个月的数据
		getSuppProdData(list,planMonth,2);
		//推后3个月的数据
		getSuppProdData(list,planMonth,3);
		//推后4个月的数据
		getSuppProdData(list,planMonth,4);
		//推后5个月的数据
		getSuppProdData(list,planMonth,5);
		//推后6个月的数据
		getSuppProdData(list,planMonth,6);
		//推后7个月的数据
		getSuppProdData(list,planMonth,7);
		//推后8个月的数据
		getSuppProdData(list,planMonth,8);
		//推后9个月的数据
		getSuppProdData(list,planMonth,9);
		//推后10个月的数据
		getSuppProdData(list,planMonth,10);
		//推后11个月的数据
		getSuppProdData(list,planMonth,11);
		//推后12个月的数据
		getSuppProdData(list,planMonth,12);
	} 
	
	/**
	 * 更新预测的12个月的生产交货计划
	 * @param list
	 * @param planMonth
	 * @param addMonth
	 * @param user
	 */
	public void getSuppProdData(List<SuppProdVo> list,Date planMonth,int addMonth){
		Calendar cal=Calendar.getInstance();
		cal.setTime(planMonth);
		cal.add(Calendar.MONTH,addMonth);
		if(list==null|| list.size()==0){
			return;
		}
		int size = list.size();
	
		SuppProdVo prod=list.get(0);
		String mateCode = prod.getMateCode();
		//判断当月的备货计划是否存在该物料
		Map<String, Object> map=new HashMap<>();
		map.put("mateCode", mateCode);
		map.put("planMonth", cal.getTime());
		//获取物料的全国库存
		BigDecimal nationalStock = invenPlanMapper.getNationalStock(map);
		
		InvenPlanDetail detail = invenPlanMapper.getPlanDetailByMateCode(map);
		if(detail!=null){
			String status = detail.getStatus();
			BigDecimal deliveryPlan = detail.getDeliveryPlan();
			String planDetailId = detail.getId();
			if(status!=null && "未分解".equals(status)){
				if (size > 0) {
					// 平均分配排产计划
					BigDecimal[] divideAndRemainder = deliveryPlan.divideAndRemainder(new BigDecimal(size));
					BigDecimal divideNum = divideAndRemainder[0];
					BigDecimal remianNum = divideAndRemainder[1];
					for (int i = 0; i < size; i++) {								
						SuppProdVo suppProdVo = list.get(i);
						suppProdVo.setNationStock(nationalStock);
						// 分配交货计划
						BigDecimal planDlvNum=divideNum;
						if (i == size - 1) {
							planDlvNum=divideNum.add(remianNum);
						}
						updateDlvprodData(suppProdVo,null,planDlvNum,addMonth);
					}
				}
			}else{
				//删除需要已经解除供应商的数据
				List<QualSupp> supps=new ArrayList<>();
				for (SuppProdVo suppProdVo : list) {
					QualSupp qualSupp = new QualSupp();
					qualSupp.setSapId(suppProdVo.getSuppNo());
					supps.add(qualSupp);
				}
				deleteSuppProdByQualSupp(planDetailId, supps);
				//获取供应商的交货计划的数量
				BigDecimal suppDlvNum = suppProdMapper.getPlanDlvNumByPlanDetailId(planDetailId);
				BigDecimal[] divideAndRemainder = (BigDecimalUtil.subtract(deliveryPlan,suppDlvNum)).divideAndRemainder(new BigDecimal(size));
				BigDecimal divideNum = divideAndRemainder[0];
				BigDecimal remianNum = divideAndRemainder[1];
				for (int i = 0; i < size; i++) {
					SuppProdVo suppProdVo =list.get(i);
					suppProdVo.setNationStock(nationalStock);
					map.put("suppNo", suppProdVo.getSuppNo());
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						SuppProd suppProd = suppProds.get(0);
						BigDecimal oldDlvPlan = suppProd.getDeliveryPlan();
						if(i==size-1){
							updateDlvprodData(suppProdVo,suppProd.getProdPlan(),BigDecimalUtil.addNums(oldDlvPlan, remianNum,divideNum),addMonth);
						}else{
							updateDlvprodData(suppProdVo,suppProd.getProdPlan(),BigDecimalUtil.add(oldDlvPlan, divideNum),addMonth);
						}
					}
				}
			}
		}
	}
	/**
	 * 更新某个月的生产交货计划
	 * @param vo
	 * @param prodNum
	 * @param dlvNum
	 * @param addMonth
	 */
	public void updateDlvprodData(SuppProdVo vo ,BigDecimal prodNum,BigDecimal dlvNum,int addMonth){
		switch (addMonth) {
		case 1:
			vo.setAddOnePlanPrdNum(prodNum);
			vo.setAddOnePlanDlvNum(dlvNum);
			break;
		case 2:
			vo.setAddTwoPlanPrdNum(prodNum);
			vo.setAddTwoPlanDlvNum(dlvNum);
			break;
		case 3:
			vo.setAddThreePlanPrdNum(prodNum);
			vo.setAddThreePlanDlvNum(dlvNum);
			break;
		case 4:
			vo.setAddFourPlanPrdNum(prodNum);
			vo.setAddFourPlanDlvNum(dlvNum);
			break;
		case 5:
			vo.setAddFivePlanPrdNum(prodNum);
			vo.setAddFivePlanDlvNum(dlvNum);
			break;
		case 6:
			vo.setAddSixPlanPrdNum(prodNum);
			vo.setAddSixPlanDlvNum(dlvNum);
			break;
		case 7:
			vo.setAddSevenPlanPrdNum(prodNum);
			vo.setAddSevenPlanDlvNum(dlvNum);
			break;
		case 8:
			vo.setAddEightPlanPrdNum(prodNum);
			vo.setAddEightPlanDlvNum(dlvNum);
			break;
		case 9:
			vo.setAddNinePlanPrdNum(prodNum);
			vo.setAddNinePlanDlvNum(dlvNum);
			break;
		case 10:
			vo.setAddTenPlanPrdNum(prodNum);
			vo.setAddTenPlanDlvNum(dlvNum);
			break;
		case 11:
			vo.setAddElevenPlanPrdNum(prodNum);
			vo.setAddElevenPlanDlvNum(dlvNum);
			break;
		case 12:
			vo.setAddTwelvePlanPrdNum(prodNum);
			vo.setAddTwelvePlanDlvNum(dlvNum);
			break;
		default:
			break;
		}
	}	

	@Override
	public List<SuppProdVo> checkTempData(List<SuppProdVo> list,List<SuppProdVo> totalList,Date planMonth) {
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		InvenPlanDetail latestPlan = invenPlanMapper.getLatestPlan(userId+"");
		Date latestMonth = latestPlan.getPlanMonth();
		//获取这个人创建的最新的备货计划
		Calendar cal=Calendar.getInstance();
		//校验数据的非空
		for (int i=0;i<list.size();i++) {
			SuppProdVo suppProdVo=list.get(i);
			cal.setTime(planMonth);
			RestCode restCode =new RestCode();
			String code="0";
			StringBuffer  msg=new StringBuffer();			
			BigDecimal planPrdNum = suppProdVo.getPlanPrdNum();
			if(planPrdNum==null){
				code="1";
				msg.append(DateUtils.format(planMonth, "yyyyMM")+"生产计划不能为空！");
			}
			//推后1个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addOnePlanPrdNum = suppProdVo.getAddOnePlanPrdNum();
			if(addOnePlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后2个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addTwoPlanPrdNum = suppProdVo.getAddTwoPlanPrdNum();
			if(addTwoPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后3个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addThreePlanPrdNum = suppProdVo.getAddThreePlanPrdNum();
			if(addThreePlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后4个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addFourPlanPrdNum = suppProdVo.getAddFourPlanPrdNum();
			if(addFourPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后5个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addFivePlanPrdNum = suppProdVo.getAddFivePlanPrdNum();
			if(addFivePlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后6个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addSixPlanPrdNum = suppProdVo.getAddSixPlanPrdNum();
			if(addSixPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后7个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addSevenPlanPrdNum = suppProdVo.getAddSevenPlanPrdNum();
			if(addSevenPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后8个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addEightPlanPrdNum = suppProdVo.getAddEightPlanPrdNum();
			if(addEightPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后9个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addNinePlanPrdNum = suppProdVo.getAddNinePlanPrdNum();
			if(addNinePlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后10个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addTenPlanPrdNum = suppProdVo.getAddTenPlanPrdNum();
			if(addTenPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后11个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addElevenPlanPrdNum = suppProdVo.getAddElevenPlanPrdNum();
			if(addElevenPlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			//推后12个月的生产计划
			cal.add(Calendar.MONTH, 1);
			if(cal.getTime().after(latestMonth)){
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
				continue;
			}
			BigDecimal addTwelvePlanPrdNum = suppProdVo.getAddTwelvePlanPrdNum();
			if(addTwelvePlanPrdNum==null){
				code="1";
				msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"生产计划不能为空！");
			}
			restCode.put("code", code);
			restCode.put("msg", msg.toString());
			suppProdVo.setRestCode(restCode);
		}
		//查看必填项校验是否通过
		for (SuppProdVo suppProdVo2 : list) {
			RestCode restCode = suppProdVo2.getRestCode();
			String code = restCode.get("code").toString();
			if(!"0".equals(code)){
				return list;
			}
		}
		Map<String,Object> map=new HashMap<>();
		//交货计划数量的校验
		for (SuppProdVo suppProdVo : totalList) {
			cal.setTime(planMonth);
			RestCode restCode =new RestCode();
			String code="0";
			StringBuffer  msg=new StringBuffer();
			
			String mateCode = suppProdVo.getMateCode();
			if(mateCode!=null && !"".equals(mateCode)){
				map.put("mateCode", mateCode);
				//当月的
				map.put("planMonth", planMonth);
				InvenPlanDetail planDetail = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal planDlvNum = suppProdVo.getPlanDlvNum();
				if(planDetail!=null){
					BigDecimal deliveryPlan = planDetail.getDeliveryPlan();
					if(planDlvNum==null || planDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				
				//推后1个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail1 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addOnePlanDlvNum = suppProdVo.getAddOnePlanDlvNum();
				if(planDetail1!=null){
					BigDecimal deliveryPlan = planDetail1.getDeliveryPlan();
					if(addOnePlanDlvNum==null || addOnePlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后2个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail2 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addTwoPlanDlvNum = suppProdVo.getAddTwoPlanDlvNum();
				if(planDetail2!=null){
					BigDecimal deliveryPlan = planDetail2.getDeliveryPlan();
					if(addTwoPlanDlvNum==null || addTwoPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后3个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail3 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addThreePlanDlvNum = suppProdVo.getAddThreePlanDlvNum();
				if(planDetail3!=null){
					BigDecimal deliveryPlan = planDetail3.getDeliveryPlan();
					if(addThreePlanDlvNum==null || addThreePlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后4个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail4 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addFourPlanDlvNum = suppProdVo.getAddFourPlanDlvNum();
				if(planDetail4!=null){
					BigDecimal deliveryPlan = planDetail4.getDeliveryPlan();
					if(addFourPlanDlvNum==null || addFourPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后5个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail5 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addFivePlanDlvNum = suppProdVo.getAddFivePlanDlvNum();
				if(planDetail5!=null){
					BigDecimal deliveryPlan = planDetail5.getDeliveryPlan();
					if(addFivePlanDlvNum==null || addFivePlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后6个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail6 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addSixPlanDlvNum = suppProdVo.getAddSixPlanDlvNum();
				if(planDetail6!=null){
					BigDecimal deliveryPlan = planDetail6.getDeliveryPlan();
					if(addSixPlanDlvNum==null || addSixPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后7个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail7 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addSevenPlanDlvNum = suppProdVo.getAddSevenPlanDlvNum();
				if(planDetail7!=null){
					BigDecimal deliveryPlan = planDetail7.getDeliveryPlan();
					if(addSevenPlanDlvNum==null || addSevenPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后8个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail8 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addEightPlanDlvNum = suppProdVo.getAddEightPlanDlvNum();
				if(planDetail8!=null){
					BigDecimal deliveryPlan = planDetail8.getDeliveryPlan();
					if(addEightPlanDlvNum==null || addEightPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后9个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail9 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addNinePlanDlvNum = suppProdVo.getAddNinePlanDlvNum();
				if(planDetail9!=null){
					BigDecimal deliveryPlan = planDetail9.getDeliveryPlan();
					if(addNinePlanDlvNum==null || addNinePlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后10个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail10 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addTenPlanDlvNum = suppProdVo.getAddTenPlanDlvNum();
				if(planDetail10!=null){
					BigDecimal deliveryPlan = planDetail10.getDeliveryPlan();
					if(addTenPlanDlvNum==null || addTenPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后11个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail11 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addElevenPlanDlvNum = suppProdVo.getAddElevenPlanDlvNum();
				if(planDetail11!=null){
					BigDecimal deliveryPlan = planDetail11.getDeliveryPlan();
					if(addElevenPlanDlvNum==null || addElevenPlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				//推后1个月的生产计划
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					restCode.put("code", code);
					restCode.put("msg", msg.toString());
					suppProdVo.setRestCode(restCode);
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail12 = invenPlanMapper.getPlanDetailByMateCode(map);
				BigDecimal addTwelvePlanDlvNum = suppProdVo.getAddTwelvePlanDlvNum();
				if(planDetail12!=null){
					BigDecimal deliveryPlan = planDetail12.getDeliveryPlan();
					if(addTwelvePlanDlvNum==null || addTwelvePlanDlvNum.compareTo(deliveryPlan)!=0){
						code="1";
						msg.append(DateUtils.format(cal.getTime(), "yyyyMM")+"交货计划分配数据与产销数据不符！");
					}
				}
				restCode.put("code", code);
				restCode.put("msg", msg.toString());
				suppProdVo.setRestCode(restCode);
			}
		}
		//供应商物料添加备货计划校验信息
		for (SuppProdVo suppProdVo2 : totalList) {
			String mateCode = suppProdVo2.getMateCode();
			if(mateCode!=null && !"".equals(mateCode)){
				for (SuppProdVo suppProdVo3 : list) {
					String mateCode2 = suppProdVo3.getMateCode();
					if(mateCode.equals(mateCode2)){
						suppProdVo3.setRestCode(suppProdVo2.getRestCode());
					}
				}
			}
		}
		return list;
	}

	@Override
	@Transactional
	public void saveTempData(List<SuppProdVo> list, List<SuppProdVo> totalList,Date planMonth) {
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		InvenPlanDetail latestPlan = invenPlanMapper.getLatestPlan(userId+"");
		Date latestMonth = latestPlan.getPlanMonth();
		Map<String, Object> map=new HashMap<String, Object>();
		Calendar cal=Calendar.getInstance();
		List<SuppProd> addList=new ArrayList<>();				
		if(list!=null && list.size()>0){
			//更新备货计划物料数据
			for (SuppProdVo suppProdVo : list) {
				cal.setTime(planMonth);
				String mateCode = suppProdVo.getMateCode();
				String mateDesc = suppProdVo.getMateDesc();
				String suppNo = suppProdVo.getSuppNo();
				String suppName = suppProdVo.getSuppName();

				//当前月的备货详情
				map.put("mateCode", mateCode);
				map.put("suppNo", suppNo);
				map.put("planMonth", planMonth);
				InvenPlanDetail planDetail = invenPlanMapper.getPlanDetailByMateCode(map);
				String itemCode="";
				String itemName="";
				String prodSeriesCode="";
				String prodSeriesDesc="";
				
				if(planDetail!=null){
					itemCode = planDetail.getItemCode();
					itemName = planDetail.getItemName();
					prodSeriesCode = planDetail.getProdSeriesCode();
					prodSeriesDesc = planDetail.getProdSeriesDesc();

					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getBeginStock());
						suppProd.setBeginOrder(suppProdVo.getBeginOrder());
						suppProd.setBeginEnableOrder(suppProdVo.getBeginEnableOrder());
						suppProd.setProdPlan(suppProdVo.getPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddOnePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getEndStock());
						suppProd.setSafeScale(suppProdVo.getSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getBeginStock());
						suppProd.setBeginOrder(suppProdVo.getBeginOrder());
						suppProd.setBeginEnableOrder(suppProdVo.getBeginEnableOrder());
						suppProd.setProdPlan(suppProdVo.getPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddOnePlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getSafeScale());
						addList.add(suppProd);
					}
				}
				//下1个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail1 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail1!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddOnePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddOnePlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTwoPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddOnePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddOneSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail1, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddOnePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddOnePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddOnePlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTwoPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddOneSafeScale());
						addList.add(suppProd);
					}
				}
				//下2个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail2 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail2!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddOnePlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddOnePlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddTwoPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTwoPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddThreePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTwoPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddTwoSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail2, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddOnePlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddTwoPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTwoPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTwoPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddThreePlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddTwoSafeScale());
						addList.add(suppProd);
					}
				}
				//下3个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail3 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail3!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddTwoPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddTwoPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddThreePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddThreePlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddFourPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddThreePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddThreeSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail3, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddTwoPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddThreePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddThreePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddThreePlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddFourPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddThreeSafeScale());
						addList.add(suppProd);
					}
				}
				//下4个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail4 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail4!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddThreePlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddThreePlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddFourPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddFourPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddFivePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddFourPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddFourSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail4, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddThreePlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddFourPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddFourPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddFourPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddFivePlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddFourSafeScale());
						addList.add(suppProd);
					}
				}
				//下5个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail5 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail5!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddFourPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddFourPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddFivePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddFivePlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddSixPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddFivePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddFiveSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail5, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddFourPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddFivePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddFivePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddFivePlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddSixPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddFiveSafeScale());
						addList.add(suppProd);
					}
				}
				//下6个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail6 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail6!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddFivePlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddFivePlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddSixPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddSixPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddSevenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddSixPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddSixSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail6, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddFivePlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddSixPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddSixPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddSixPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddSevenPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddSixSafeScale());
						addList.add(suppProd);
					}
				}
				//下7个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail7 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail7!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddSixPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddSixPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddSevenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddSevenPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddEightPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddSevenPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddSevenSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail7, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddSixPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddSevenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddSevenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddSevenPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddEightPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddSevenSafeScale());
						addList.add(suppProd);
					}
				}
				//下8个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail8 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail8!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddSevenPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddSevenPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddEightPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddEightPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddNinePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddEightPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddEightSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail8, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddSevenPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddEightPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddEightPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddEightPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddNinePlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddEightSafeScale());
						addList.add(suppProd);
					}
				}
				//下9个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail9 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail9!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddEightPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddEightPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddNinePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddNinePlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddNinePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddNineSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail9, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddEightPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddNinePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddNinePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddNinePlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTenPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddNineSafeScale());
						addList.add(suppProd);
					}
				}
				//下10个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail10 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail10!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddNinePlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddNinePlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddTenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTenPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddElevenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTenPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddTenSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail10, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddNinePlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddTenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTenPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddElevenPlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddTenSafeScale());
						addList.add(suppProd);
					}
				}
				//下11个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail11 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail11!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddTenPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddTenPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddElevenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddElevenPlanDlvNum());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTwelvePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddElevenPlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddElevenSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail11, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddTenPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddElevenPlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddElevenPlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddElevenPlanEndStock());
						suppProd.setNextDeliveryNum(suppProdVo.getAddTwelvePlanDlvNum());
						suppProd.setSafeScale(suppProdVo.getAddElevenSafeScale());
						addList.add(suppProd);
					}
				}
				//下12个月备货详情
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(latestMonth)){
					continue;
				}
				map.put("planMonth", cal.getTime());
				InvenPlanDetail planDetail12 = invenPlanMapper.getPlanDetailByMateCode(map);
				if(planDetail12!=null){
					//判断排产计划是否存在
					SuppProd suppProd=new SuppProd();
					List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
					if(suppProds!=null && suppProds.size()>0){
						suppProd=suppProds.get(0);
						suppProd.setItemCode(itemCode);
						suppProd.setItemName(itemName);
						suppProd.setSuppName(suppName);
						suppProd.setProdSeriesCode(prodSeriesCode);
						suppProd.setProdSeriesDesc(prodSeriesDesc);
						suppProd.setBeginStock(suppProdVo.getAddElevenPlanEndStock());
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(new BigDecimal(0), suppProdVo.getAddElevenPlanEndStock()));
						suppProd.setProdPlan(suppProdVo.getAddTwelvePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTwelvePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTwelvePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddOneSafeScale());
						suppProdMapper.updateSuppProd(suppProd);
					}else{
						updateSuppProdData(suppProd, planDetail12, suppNo, suppName, mateCode, mateDesc, itemCode, itemName, cal, userId, prodSeriesCode, prodSeriesDesc);
						BigDecimal beginStock = suppProdVo.getAddElevenPlanEndStock();
						BigDecimal beginOrder = new BigDecimal(0);
						suppProd.setBeginStock(beginStock);
						suppProd.setBeginOrder(beginOrder);
						suppProd.setBeginEnableOrder(BigDecimalUtil.subtract(beginStock, beginOrder));
						suppProd.setProdPlan(suppProdVo.getAddTwelvePlanPrdNum());
						suppProd.setDeliveryPlan(suppProdVo.getAddTwelvePlanDlvNum());
						suppProd.setEndStock(suppProdVo.getAddTwelvePlanEndStock());
						suppProd.setSafeScale(suppProdVo.getAddTwelveSafeScale());
						addList.add(suppProd);
					}
				}	
			}
			//更新排产数据
			for (SuppProdVo suppProdVo : totalList) {
				String mateCode = suppProdVo.getMateCode();
				cal.setTime(planMonth);
				map.put("planMonth", cal.getTime());
				map.put("mateCode", mateCode);
				if(mateCode!=null && !"".equals(mateCode)){
					//更新当月数据
					InvenPlanDetail planDetail = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail!=null){
						String status = planDetail.getStatus();
						if("未分解".equals(status)){
							planDetail.setStatus("已分解");
						}
						planDetail.setProdPlan(suppProdVo.getPlanPrdNum());
						planDetail.setDeliveryPlan(suppProdVo.getPlanDlvNum());
						planDetail.setEndStock(suppProdVo.getEndStock());
						planDetail.setSafeScale(suppProdVo.getSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail);
					}
					//下1个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail1 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail1!=null){
						String status = planDetail1.getStatus();
						if("未分解".equals(status)){
							planDetail1.setStatus("已分解");
						}
						BigDecimal beginStock = suppProdVo.getEndStock();
						planDetail1.setBeginStock(beginStock);
						planDetail1.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail1.getBeginEnableOrder(), beginStock));
						planDetail1.setNextMonthDeliveryNum(suppProdVo.getAddTwoPlanDlvNum());
						planDetail1.setProdPlan(suppProdVo.getAddOnePlanPrdNum());
						planDetail1.setDeliveryPlan(suppProdVo.getAddOnePlanDlvNum());
						planDetail1.setEndStock(suppProdVo.getAddOnePlanEndStock());
						planDetail1.setSafeScale(suppProdVo.getAddOneSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail1);
					}
					//下2个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail2 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail2!=null){
						String status = planDetail2.getStatus();
						if("未分解".equals(status)){
							planDetail2.setStatus("已分解");
						}
						BigDecimal beginStock = suppProdVo.getAddOnePlanEndStock();
						planDetail2.setBeginStock(beginStock);
						planDetail2.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail2.getBeginEnableOrder(), beginStock));
						planDetail2.setNextMonthDeliveryNum(suppProdVo.getAddThreePlanDlvNum());
						planDetail2.setProdPlan(suppProdVo.getAddTwoPlanPrdNum());
						planDetail2.setDeliveryPlan(suppProdVo.getAddTwoPlanDlvNum());
						planDetail2.setEndStock(suppProdVo.getAddTwoPlanEndStock());
						planDetail2.setSafeScale(suppProdVo.getAddTwoSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail2);
					}
					//下3个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail3 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail3!=null){
						String status = planDetail3.getStatus();
						if("未分解".equals(status)){
							planDetail3.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddTwoPlanEndStock();
						planDetail3.setBeginStock(beginStock);
						planDetail3.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail3.getBeginEnableOrder(), beginStock));
						planDetail3.setNextMonthDeliveryNum(suppProdVo.getAddFourPlanDlvNum());
						
						planDetail3.setProdPlan(suppProdVo.getAddThreePlanPrdNum());
						planDetail3.setDeliveryPlan(suppProdVo.getAddThreePlanDlvNum());
						planDetail3.setEndStock(suppProdVo.getAddThreePlanEndStock());
						planDetail3.setSafeScale(suppProdVo.getAddThreeSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail3);
					}
					//下4个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail4 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail4!=null){
						String status = planDetail4.getStatus();
						if("未分解".equals(status)){
							planDetail4.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddThreePlanEndStock();
						planDetail4.setBeginStock(beginStock);
						planDetail4.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail4.getBeginEnableOrder(), beginStock));
						planDetail4.setNextMonthDeliveryNum(suppProdVo.getAddFivePlanDlvNum());
						
						planDetail4.setProdPlan(suppProdVo.getAddFourPlanPrdNum());
						planDetail4.setDeliveryPlan(suppProdVo.getAddFourPlanDlvNum());
						planDetail4.setEndStock(suppProdVo.getAddFourPlanEndStock());
						planDetail4.setSafeScale(suppProdVo.getAddFourSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail4);
					}
					//下5个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail5 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail5!=null){
						String status = planDetail5.getStatus();
						if("未分解".equals(status)){
							planDetail5.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddFourPlanEndStock();
						planDetail5.setBeginStock(beginStock);
						planDetail5.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail5.getBeginEnableOrder(), beginStock));
						planDetail5.setNextMonthDeliveryNum(suppProdVo.getAddSixPlanDlvNum());
						
						
						planDetail5.setProdPlan(suppProdVo.getAddFivePlanPrdNum());
						planDetail5.setDeliveryPlan(suppProdVo.getAddFivePlanDlvNum());
						planDetail5.setEndStock(suppProdVo.getAddFivePlanEndStock());
						planDetail5.setSafeScale(suppProdVo.getAddFiveSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail5);
					}
					//下6个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail6 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail6!=null){
						String status = planDetail6.getStatus();
						if("未分解".equals(status)){
							planDetail6.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddFivePlanEndStock();
						planDetail6.setBeginStock(beginStock);
						planDetail6.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail6.getBeginEnableOrder(), beginStock));
						planDetail6.setNextMonthDeliveryNum(suppProdVo.getAddSevenPlanDlvNum());
						
						planDetail6.setProdPlan(suppProdVo.getAddSixPlanPrdNum());
						planDetail6.setDeliveryPlan(suppProdVo.getAddSixPlanDlvNum());
						planDetail6.setEndStock(suppProdVo.getAddSixPlanEndStock());
						planDetail6.setSafeScale(suppProdVo.getAddSixSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail6);
					}
					//下7个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail7 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail7!=null){
						String status = planDetail7.getStatus();
						if("未分解".equals(status)){
							planDetail7.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddSixPlanEndStock();
						planDetail7.setBeginStock(beginStock);
						planDetail7.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail7.getBeginEnableOrder(), beginStock));
						planDetail7.setNextMonthDeliveryNum(suppProdVo.getAddEightPlanDlvNum());
						
						planDetail7.setProdPlan(suppProdVo.getAddSevenPlanPrdNum());
						planDetail7.setDeliveryPlan(suppProdVo.getAddSevenPlanDlvNum());
						planDetail7.setEndStock(suppProdVo.getAddSevenPlanEndStock());
						planDetail7.setSafeScale(suppProdVo.getAddSevenSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail7);
					}
					//下8个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail8 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail8!=null){
						String status = planDetail8.getStatus();
						if("未分解".equals(status)){
							planDetail8.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddSevenPlanEndStock();
						planDetail8.setBeginStock(beginStock);
						planDetail8.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail8.getBeginEnableOrder(), beginStock));
						planDetail8.setNextMonthDeliveryNum(suppProdVo.getAddNinePlanDlvNum());
						
						planDetail8.setProdPlan(suppProdVo.getAddEightPlanPrdNum());
						planDetail8.setDeliveryPlan(suppProdVo.getAddEightPlanDlvNum());
						planDetail8.setEndStock(suppProdVo.getAddEightPlanEndStock());
						planDetail8.setSafeScale(suppProdVo.getAddEightSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail8);
					}
					//下9个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail9 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail9!=null){
						String status = planDetail9.getStatus();
						if("未分解".equals(status)){
							planDetail9.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddEightPlanEndStock();
						planDetail9.setBeginStock(beginStock);
						planDetail9.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail9.getBeginEnableOrder(), beginStock));
						planDetail9.setNextMonthDeliveryNum(suppProdVo.getAddTenPlanDlvNum());
						
						planDetail9.setProdPlan(suppProdVo.getAddNinePlanPrdNum());
						planDetail9.setDeliveryPlan(suppProdVo.getAddNinePlanDlvNum());
						planDetail9.setEndStock(suppProdVo.getAddNinePlanEndStock());
						planDetail9.setSafeScale(suppProdVo.getAddNineSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail9);
					}
					//下10个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail10 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail10!=null){
						String status = planDetail10.getStatus();
						if("未分解".equals(status)){
							planDetail10.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddNinePlanEndStock();
						planDetail10.setBeginStock(beginStock);
						planDetail10.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail10.getBeginEnableOrder(), beginStock));
						planDetail10.setNextMonthDeliveryNum(suppProdVo.getAddElevenPlanDlvNum());
						
						planDetail10.setProdPlan(suppProdVo.getAddTenPlanPrdNum());
						planDetail10.setDeliveryPlan(suppProdVo.getAddTenPlanDlvNum());
						planDetail10.setEndStock(suppProdVo.getAddTenPlanEndStock());
						planDetail10.setSafeScale(suppProdVo.getAddTenSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail10);
					}
					//下11个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail11 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail11!=null){
						String status = planDetail11.getStatus();
						if("未分解".equals(status)){
							planDetail11.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddTenPlanEndStock();
						planDetail11.setBeginStock(beginStock);
						planDetail11.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail11.getBeginEnableOrder(), beginStock));
						planDetail11.setNextMonthDeliveryNum(suppProdVo.getAddTwelvePlanDlvNum());
						
						planDetail11.setProdPlan(suppProdVo.getAddElevenPlanPrdNum());
						planDetail11.setDeliveryPlan(suppProdVo.getAddElevenPlanDlvNum());
						planDetail11.setEndStock(suppProdVo.getAddElevenPlanEndStock());
						planDetail11.setSafeScale(suppProdVo.getAddElevenSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail11);
					}
					//下12个月的数据
					cal.add(Calendar.MONTH,1);
					if(cal.getTime().after(latestMonth)){
						continue;
					}
					map.put("planMonth", cal.getTime());
					InvenPlanDetail planDetail12 = invenPlanMapper.getPlanDetailByMateCode(map);
					if(planDetail12!=null){
						String status = planDetail12.getStatus();
						if("未分解".equals(status)){
							planDetail12.setStatus("已分解");
						}
						
						BigDecimal beginStock = suppProdVo.getAddElevenPlanEndStock();
						planDetail12.setBeginStock(beginStock);
						planDetail12.setBeginEnableOrder(BigDecimalUtil.subtract(planDetail12.getBeginEnableOrder(), beginStock));
						
						planDetail12.setProdPlan(suppProdVo.getAddTwelvePlanPrdNum());
						planDetail12.setDeliveryPlan(suppProdVo.getAddTwelvePlanDlvNum());
						planDetail12.setEndStock(suppProdVo.getAddTwelvePlanEndStock());
						planDetail12.setSafeScale(suppProdVo.getAddTwelveSafeScale());
						invenPlanMapper.updateInvenPlanDetail(planDetail12);
					}
				}
			}
			if(addList!=null && addList.size()>0){
				suppProdMapper.saveSuppProds(addList);
			}
		}
	}
	//------------------------备货计划导入结束-----------------------------

	public void updateSuppProdData(SuppProd suppProd,InvenPlanDetail planDetail,String suppNo,
			String suppName,String mateCode,String mateDesc,
			String itemCode,String itemName,
			Calendar cal,Long userId,String prodSeriesCode,String prodSeriesDesc ){
		suppProd.setId(UUIDUtil.getUUID());
		suppProd.setMainId(planDetail.getMainId());
		suppProd.setStatus("未排产");
		suppProd.setSuppNo(suppNo);
		suppProd.setSuppName(suppName);
		suppProd.setMateCode(mateCode);
		suppProd.setMateDesc(mateDesc);
		suppProd.setRanking(planDetail.getRanking());
		suppProd.setPlanMonth(cal.getTime());
		suppProd.setItemCode(itemCode);
		suppProd.setItemName(itemName);
		suppProd.setCreator(userId);
		suppProd.setProdSeriesCode(prodSeriesCode);
		suppProd.setProdSeriesDesc(prodSeriesDesc);
		suppProd.setPlanDetailId(planDetail.getId());
	}

	@Override
	public InvenPlanDetail getPlanDetailById(String planDetailId) {
		return invenPlanMapper.getPlanDetailById(planDetailId);
	}

	@Override
	public int deleteSuppProdByQualSupp(String planDetailId, List<QualSupp> supps) {
		Map<String, Object> map=new HashMap<>();
		map.put("planDetailId", planDetailId);
		map.put("supps", supps);
		int num = suppProdMapper.deleteSuppProdByQualSupp(map);
		return num;
	}
//---------------------------备货计划差异比较------------------------------------
	@Override
	public List<InvenPadCompare> getGetComPareByPlanCode(String planCode,Date planMonth) {
		//获取差异物料
		List<InvenPadCompare> list = invenPlanMapper.getGetComPareByPlanCode(planCode);
		Calendar cal=Calendar.getInstance();
		cal.setTime(planMonth);
		//获取当月生产交货计划数据
		List<InvenPadCompare> list0 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list0, 0);
		//获取1月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list1 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list1, 1);
		//获取2月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list2 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list2, 2);
		//获取3月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list3 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list3, 3);		
		//获取4月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list4 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list4, 4);
		
		//获取5月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list5 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list5,5);	
		//获取6月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list6 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list6, 6);		
		//获取7月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list7 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list7, 7);			
		//获取8月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list8 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list8, 8);			
		//获取9月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list9 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list9, 9);
		//获取10月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list10 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list10, 10);		
		//获取11月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list11 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list11, 11);		
		//获取12月生产交货计划数据
		cal.add(Calendar.MONTH, 1);
		List<InvenPadCompare> list12 = invenPlanMapper.getGetComPareByMonthAndMate(list, cal.getTime());
		setDlvData(list, list12, 12);
		return list;
	}
	/**
	 * 更新比较差异的交货信息
	 * @param list
	 * @param list1
	 * @param num
	 */
	public void setDlvData(List<InvenPadCompare> list,List<InvenPadCompare> list1,int num){
		for (InvenPadCompare invenPadCompare : list) {
			String mateCode = invenPadCompare.getMateCode();
			String padMateCode = invenPadCompare.getPadMateCode();
			for (InvenPadCompare campareData : list1) {
				String mateCode0 = campareData.getMateCode();
				String padMateCode0 = campareData.getPadMateCode();
				if(StringUtil.equals(mateCode, mateCode0) && StringUtil.equals(padMateCode, padMateCode0)){	
					switch (num) {
					case 0:
						invenPadCompare.setInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setPadDlvNum(campareData.getPadDlvNum());
						break;
					case 1:
						invenPadCompare.setAddOneInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddOnePadDlvNum(campareData.getPadDlvNum());
						break;
					case 2:
						invenPadCompare.setAddTwoInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddTwoPadDlvNum(campareData.getPadDlvNum());
						break;
					case 3:
						invenPadCompare.setAddThreeInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddThreePadDlvNum(campareData.getPadDlvNum());
						break;
					case 4:
						invenPadCompare.setAddFourInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddFourPadDlvNum(campareData.getPadDlvNum());
						break;
					case 5:
						invenPadCompare.setAddFiveInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddFivePadDlvNum(campareData.getPadDlvNum());
						break;
					case 6:
						invenPadCompare.setAddSixInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddSixPadDlvNum(campareData.getPadDlvNum());
						break;
					case 7:
						invenPadCompare.setAddSevenInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddSevenPadDlvNum(campareData.getPadDlvNum());
						break;
					case 8:
						invenPadCompare.setAddEightInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddEightPadDlvNum(campareData.getPadDlvNum());
						break;
					case 9:
						invenPadCompare.setAddNineInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddNinePadDlvNum(campareData.getPadDlvNum());
						break;
					case 10:
						invenPadCompare.setAddTenInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddTenPadDlvNum(campareData.getPadDlvNum());
						break;
					case 11:
						invenPadCompare.setAddElevenInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddElevenPadDlvNum(campareData.getPadDlvNum());
						break;
					case 12:
						invenPadCompare.setAddTwelveInvenDlvNum(campareData.getInvenDlvNum());
						invenPadCompare.setAddTwelvePadDlvNum(campareData.getPadDlvNum());
						break;
					default:
						break;
					}										
				}
			}
		}	
	}
}
