package com.faujor.service.mdm.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.cluster.mdm.ClusterSuppMapper;
import com.faujor.dao.master.common.KvSequenceMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.mdm.QualPapersMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.cluster.mdm.SuppPurcDO;
import com.faujor.entity.cluster.mdm.SupplierDO;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.KvSequenceDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.mdm.UserSupp;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.common.KvSequenceService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.MD5Utils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "QualSuppService")
public class QualSuppServiceImpl implements QualSuppService {

	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private ClusterSuppMapper clusterSuppMapper;
	@Autowired
	private QualPapersMapper qualPapersMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private BasicService basicService;
	@Autowired
	private KvSequenceService kvService;
	@Autowired
	private KvSequenceMapper kvMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private AsyncLogService asyncLogService;

	@Override
	public Map<String, Object> queryQualSuppByPage(Map<String, Object> map) {
		List<QualSupp> list = qualSuppMapper.queryQualSuppByPage(map);
		int count = qualSuppMapper.queryQualSuppCountbyPage(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public QualSupp queryOneQualSuppbySuppId(String suppId) {
		QualSupp supp = qualSuppMapper.queryOneQualSuppBySuppId(suppId);
		return supp;
	}

	@Override
	public Map<String, Object> queryQualSuppByUserId(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String, Object>();
		List<QualSupp> list = qualSuppMapper.queryQualSuppByUserId(map);
		int count = qualSuppMapper.queryQualSuppByUserIdCount(map);
		page.put("data", list);
		page.put("count", count);
		page.put("msg", "");
		page.put("code", "0");
		return page;
	}

	@Override
	public Map<String, Object> queryQualSuppByUserIds(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String, Object>();
		List<QualSuppDO> list = qualSuppMapper.queryQualSuppByUserIds(map);
		int count = qualSuppMapper.queryQualSuppByUserIdsCount(map);
		page.put("data", list);
		page.put("count", count);
		page.put("msg", "");
		page.put("code", "0");
		return page;
	}

	@Override
	@Transactional
	public boolean deleteQualSuppOfUser(Integer userId, String[] suppIds) {
		List<String> suppIdList = new ArrayList<>();
		for (int i = 0; i < suppIds.length; i++) {
			qualSuppMapper.deleteQualSuppOfUser(userId, suppIds[i]);
			suppIdList.add(suppIds[i]);
		}
		if(suppIdList.size()>0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("suppIdList", suppIdList);
			map.put("userId", userId);
			qualSuppMapper.deleteSuppMateOfUserByUserIdAndSuppIdList(map);
		}
		return true;
	}

	@Override
	public Map<String, Object> queryAllQualSupp() {
		List<QualSupp> list = qualSuppMapper.queryAllQualSupp();
		int count = qualSuppMapper.queryAllQualSuppCount();
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("msg", "");
		page.put("code", 0);
		return page;
	}

	@Override
	@Transactional
	public boolean addQualSuppForUser(Integer userId, String[] suppIds) {
		SysUserDO user = UserCommon.getUser();
		UserSupp us = new UserSupp();
		us.setCreator(user.getName());
		us.setBuyerId(userId);
		int a = 0;
		for (int i = 0; i < suppIds.length; i++) {
			int j = 0;
			us.setSuppId(suppIds[i]);
			j = qualSuppMapper.addQualSuppForUser(us);
			a += j;
		}
		if (a == suppIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public QualSupp queryQualSuppBySuppName(String suppName) {
		QualSupp supp = qualSuppMapper.queryQualSuppBySuppName(suppName);
		return supp;
	}

	@Override
	public QualSupp queryQualSuppBySapId(String sapId) {
		QualSupp qs = qualSuppMapper.queryQualSuppBySapId(sapId);
		return qs;
	}

	@Override
	public List<QualSupp> queryQualSuppOfMateByMateCode(Map<String,Object> map) {
		List<QualSupp> list = qualSuppMapper.queryQualSuppOfMateByMateCode(map);
		return list;
	}

	@Override
	public List<QualSupp> queryQualSuppBySapCodes(List<String> sapCodes) {
		List<QualSupp> supps = qualSuppMapper.queryQualSuppBySapCodes(sapCodes);
		return supps;
	}

	@Override
	public int asyncSuppInfo(AsyncLog al) {
		int i = 0;
		try {
			// 从EDI中获取的供应商数据
			List<String> codes = qualSuppMapper.findQualSuppCode();
			List<SupplierDO> list = clusterSuppMapper.findSupplierList();
			List<SupplierDO> addList = new ArrayList<SupplierDO>();
			KvSequenceDO kv = kvService.getKvSequenceByKeyName("userCode");
			long v = kv.getValName();
			List<SysUserDO> userList = new ArrayList<SysUserDO>();
			OrgDo org = orgMapper.findOrgByOrgCode("SUPPLIER");
			int temp = 200;
			for (SupplierDO sd : list) {
				String code = sd.getSapId();
				String suppId = sd.getSuppId();
				if (codes.contains(code)) {
					// 做更新处理
					i += qualSuppMapper.updateQualSuppFromEDI(sd);
					dealCertInfo(sd, "modify");
					// 证照数据
				} else {
					// 证照数据
					dealCertInfo(sd, "add");
					addList.add(sd);
					// 为供应商数据生成账户
					SysUserDO user = new SysUserDO();
					String sapId = sd.getSapId();
					String first = sapId.replaceFirst("^0*", "");
					user.setSuppNo(sapId);
					user.setUsername("S" + first);
					user.setName(sd.getSuppName());
					user.setPassword(MD5Utils.encrypt("123456"));
					user.setPlainCode("123456");
					user.setEmail(sd.getEmail());
					user.setUserType("supplier");
					user.setStatus(1);
					v++;
					user.setUserId(v);
					user.setOrgSpersonId(org.getSpersonId());
					userList.add(user);
				}
				// 采购数据列表
				List<SuppPurcDO> purcList = sd.getSuppPurcList();
				// 获取改
				List<String> orgRanges = qualPapersMapper.findOrgAndRangeBySuppId(suppId);
				List<SuppPurcDO> addPurcList = new ArrayList<SuppPurcDO>();
				if (purcList.size() > 0) {
					// 使用T_sap_inte_pmc_cg表中的数据
					for (SuppPurcDO purc : purcList) {
						// 判断是否有采购数据
						if (purc.getSapId() != null) {
							String suppRange = purc.getSuppRange() == null ? "" : purc.getSuppRange();
							String orgRange = purc.getPurcOrga() + suppRange;
							if (orgRanges.contains(orgRange)) {
								// 更新
								qualPapersMapper.updateSuppPurcFromEDI(purc);
							} else {
								// 新增
								addPurcList.add(purc);
							}
						} else {
							// 使用pmc中的数据
							String orgCode = sd.getPMC43();
							// 判断是否存在采购数据
							if (orgCode != null) {
								SuppPurcDO spd = new SuppPurcDO();
								spd.setSapId(sd.getSapId());
								spd.setPurcOrga(sd.getPMC43());
								spd.setPayClause(sd.getPMC44());
								spd.setCurrCode(sd.getPMC45());
								spd.setAbcIden(sd.getPMC46());
								spd.setSuppGroup(sd.getPMC47());
								if (orgRanges.contains(orgCode)) {
									// 更新
									qualPapersMapper.updateSuppPurcFromEDI(spd);
								} else {
									spd.setId(UUIDUtil.getUUID());
									addPurcList.add(spd);
								}
							}
						}
					}
				} else {
					// 使用pmc中的数据
					String orgCode = sd.getPMC43();
					SuppPurcDO spd = new SuppPurcDO();
					spd.setSapId(sd.getSapId());
					spd.setPurcOrga(sd.getPMC43());
					spd.setPayClause(sd.getPMC44());
					spd.setCurrCode(sd.getPMC45());
					spd.setAbcIden(sd.getPMC46());
					spd.setSuppGroup(sd.getPMC47());
					if (orgRanges.contains(orgCode)) {
						// 更新
						qualPapersMapper.updateSuppPurcFromEDI(spd);
					} else {
						spd.setId(UUIDUtil.getUUID());
						addPurcList.add(spd);
					}
				}
				if (addPurcList.size() > 0)
					qualPapersMapper.batchSaveSuppPurcFromEDI(addPurcList);
				if (addList.size() == temp) {
					i += qualSuppMapper.batchSaveSuppFromEDI(addList);
					addList = new ArrayList<SupplierDO>();
				}
			}
			if (userList.size() > 0) {
				int q = userMapper.batchSaveUser(userList);
				if (q > 0) {
					kv.setValName(v);
					kvMapper.update(kv);
				}
			}
			if (addList.size() > 0)
				i += qualSuppMapper.batchSaveSuppFromEDI(addList);
			al.setAsyncNum(i);
			al.setAsyncStatus("同步成功");
			asyncLogService.updateAsyncLog(al);
		} catch (Exception e) {
			// 抛出异常
			al.setAsyncStatus("同步异常");
			String message = e.getMessage();
			if (!StringUtils.isEmpty(message)) {
				int length = message.length();
				if (length < 2999) {
					al.setErrorMsg(message);
				} else {
					String errorMsg = message.substring(0, 2999);
					al.setErrorMsg(errorMsg);
				}
			} else {
				al.setErrorMsg("未知原因！");
			}
			asyncLogService.updateAsyncLog(al);
		}
		return i;
	}

	public int dealCertInfo(SupplierDO sd, String type) {
		int i = 0;
		// 合同
		if (!StringUtils.isEmpty(sd.getPmc12())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc12());
			if (!StringUtils.isEmpty(sd.getPmc13())) {
				Date d = formateDate(sd.getPmc13());
				qp.setStartDate(d); // 合同起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc14())) {
				Date d = formateDate(sd.getPmc14());
				qp.setEndDate(d);// 合同截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;

		}
		// 营业执照
		if (!StringUtils.isEmpty(sd.getPmc15())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc15());
			if (!StringUtils.isEmpty(sd.getPmc16())) {
				Date d = formateDate(sd.getPmc16());
				qp.setStartDate(d); // 营业执照起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc17())) {
				Date d = formateDate(sd.getPmc17());
				qp.setEndDate(d);// 营业执照截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		// 印刷经营许可证
		if (!StringUtils.isEmpty(sd.getPmc18())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc18());
			if (!StringUtils.isEmpty(sd.getPmc19())) {
				Date d = formateDate(sd.getPmc19());
				qp.setStartDate(d); // 印刷经营许可证起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc20())) {
				Date d = formateDate(sd.getPmc20());
				qp.setEndDate(d);// 印刷经营许可证截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		// 商品条码印刷资格证
		if (!StringUtils.isEmpty(sd.getPmc21())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc21());
			if (!StringUtils.isEmpty(sd.getPmc22())) {
				Date d = formateDate(sd.getPmc22());
				qp.setStartDate(d); // 商品条码印刷资格证起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc23())) {
				Date d = formateDate(sd.getPmc23());
				qp.setEndDate(d);// 商品条码印刷资格证截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		// QS证件号
		if (!StringUtils.isEmpty(sd.getTaPmc016())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getTaPmc016());
			if (!StringUtils.isEmpty(sd.getPmc24())) {
				Date d = formateDate(sd.getPmc24());
				qp.setStartDate(d); // QS资证起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc25())) {
				Date d = formateDate(sd.getPmc25());
				qp.setEndDate(d);// QS资证截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		// 卫生许可证1号码
		if (!StringUtils.isEmpty(sd.getPmc26())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc26());
			if (!StringUtils.isEmpty(sd.getPmc27())) {
				Date d = formateDate(sd.getPmc27());
				qp.setStartDate(d); // 卫生许可证1起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc28())) {
				Date d = formateDate(sd.getPmc28());
				qp.setEndDate(d);// 卫生许可证1截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		// 卫生许可证2号码
		if (!StringUtils.isEmpty(sd.getPmc29())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc29());
			if (!StringUtils.isEmpty(sd.getPmc30())) {
				Date d = formateDate(sd.getPmc30());
				qp.setStartDate(d); // 卫生许可证2起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc31())) {
				Date d = formateDate(sd.getPmc31());
				qp.setEndDate(d);// 卫生许可证2截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}

		// 迪士尼认证
		if (!StringUtils.isEmpty(sd.getPmc32())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc32());
			if (!StringUtils.isEmpty(sd.getPmc33())) {
				Date d = formateDate(sd.getPmc33());
				qp.setStartDate(d); // 迪士尼认证起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc34())) {
				Date d = formateDate(sd.getPmc34());
				qp.setEndDate(d);// 迪士尼认证截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}

		// 企业标准
		if (!StringUtils.isEmpty(sd.getPmc35())) {
			QualPapers qp = new QualPapers();
			qp.setId(UUIDUtil.getUUID());
			qp.setPapersId(sd.getPmc35());
			if (!StringUtils.isEmpty(sd.getPmc36())) {
				Date d = formateDate(sd.getPmc36());
				qp.setStartDate(d); // 企业标准起始日期
			}
			if (!StringUtils.isEmpty(sd.getPmc37())) {
				Date d = formateDate(sd.getPmc37());
				qp.setEndDate(d);// 企业标准截止日期
			}
			boolean flag = true;
			if ("modify".equals(type)) {
				String suppId = "s" + sd.getSapId();
				QualPapers qp1 = qualPapersMapper.findPapersBySuppIdAndCode(suppId, sd.getPmc12());
				if (qp1 != null) {
					qp.setId(qp1.getId());
					flag = false;
					qualPapersMapper.updateSuppPapersFromEDI(qp);
				}
			}
			if (flag)
				saveCertInfo(qp);
			i++;
		}
		return i;
	}

	public int saveCertInfo(QualPapers qp) {
		Dic dic = basicService.findDicByDicCodeANDCategoryCode(qp.getPapersId(), "ACCENAME");
		if (dic != null) {
			qp.setPapersName(dic.getDicName());
			qp.setPapersType(dic.getRemark());
		}
		return qualPapersMapper.insertQualPapers(qp);
	}

	public Date formateDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	@Override
	public String queryByuerIdBySapIdAndMateCode(String sapId, String mateCode) {
		return qualSuppMapper.queryByuerIdBySapIdAndMateCode(sapId, mateCode);
	}

	@Override
	public List<String> queryAllSuppIdOfQualSupp() {
		return qualSuppMapper.queryAllSuppIdOfQualSupp();
	}

	@Override
	public List<String> findSuppCodesByUsers(List<UserDO> users) {

		return qualSuppMapper.findSuppCodesByUsers(users);
	}

	@Override
	public Map<String, Object> queryAllQualSuppByPage(Map<String, Object> map) {
		List<QualSupp> list = qualSuppMapper.queryAllQualSuppByPage(map);
		int count = qualSuppMapper.queryAllQualSuppByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("msg", "");
		page.put("code", 0);
		return page;
	}

	@Override
	public boolean updatePaperOfQualSuppBySuppId(String suppId, List<QualPapers> list) {
		qualPapersMapper.deletePaperOfQualSuppBySuppId(suppId);
		int count = 0;
		for (QualPapers p : list) {
			int k = 0;
			p.setSuppId(suppId);
			k = qualPapersMapper.insertQualPapers(p);
			count += k;
		}
		if (count == list.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<QualPapers> queryPapersOfQualSuppBysuppId(String suppId) {
		return qualPapersMapper.queryQualPapersBySuppId(suppId);
	}

	@Override
	public List<QualSupp> queryAllQualSuppListOfUser(String userId) {
		return qualSuppMapper.queryAllQualSuppListByUserId(userId);
	}

	@Override
	public List<QualSupp> queryAllQualSuppListOfUsers(Map<String, Object> map) {
		return qualSuppMapper.queryAllQualSuppListByUserIds(map);
	}

	@Override
	public List<QualSuppDO> queryQualSuppByQualSupp(QualSupp qualSupp) {
		return qualSuppMapper.queryQualSuppByQualSupp(qualSupp);
	}

	@Override
	public List<String> queryQualSuppConcatColByQualSupp(QualSupp qualSupp) {
		return qualSuppMapper.queryQualSuppConcatColByQualSupp(qualSupp);
	}

	@Override
	public String getSuppRangeDescBySapIdAndSuppRange(String suppNo, String suppRange) {
		
		return qualSuppMapper.getSuppRangeDescBySapIdAndSuppRange(suppNo,suppRange);
	}

}
