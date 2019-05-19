package com.faujor.service.fam.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.dao.master.fam.PurchReconMapper;
import com.faujor.dao.sapcenter.fam.SapPurchReconMapper;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.fam.PurchRecon;
import com.faujor.entity.fam.PurchReconDebit;
import com.faujor.entity.fam.PurchReconInvoce;
import com.faujor.entity.fam.PurchReconMate;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.fam.PurchReconService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service
public class PurchReconServiceImpl implements PurchReconService {
	@Autowired
	private PurchReconMapper purchReconMapper;
	@Autowired
	private SapPurchReconMapper sapPurchReconMapper;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private TaskService taskService;
	@Override
	public LayuiPage<PurchRecon> getFinancePurchReconByPage(Map<String, Object> map) {
		LayuiPage<PurchRecon> page = new LayuiPage<PurchRecon>();
		List<PurchRecon> data = purchReconMapper.getFinancePurchReconByPage(map);
		int count = purchReconMapper.getFinancePurchReconCount(map);
		page.setCount(count);
		page.setData(data);
		return page;
	}

	@Override
	public LayuiPage<PurchRecon> getSuppPurchReconByPage(Map<String, Object> map) {
		// 获取登录供应商的编码
		LayuiPage<PurchRecon> page = new LayuiPage<PurchRecon>();
		List<PurchRecon> data = purchReconMapper.getSuppPurchReconByPage(map);
		int count = purchReconMapper.getSuppPurchReconCount(map);
		page.setCount(count);
		page.setData(data);
		return page;
	}

	@Override
	public PurchRecon getReconByCode(String reconCode) {
		PurchRecon purchRecon = purchReconMapper.getPurchReconByCode(reconCode);
		return purchRecon;
	}

	@Override
	@Transactional
	public void saveRecon(PurchRecon recon, List<PurchReconMate> mates, List<PurchReconDebit> debits) {
		Map<String, Object> map = new HashMap<>();
		String reconCode = recon.getReconCode();
		String status = recon.getReconStatus();
		String reconStatusDesc = getStatusDesc(status);
		recon.setReconStatusDesc(reconStatusDesc);
		recon.setReconStatusDesc(reconStatusDesc);
		SysUserDO user = UserCommon.getUser();
		recon.setCreateTime(new Date());
		//recon.setId(UUIDUtil.getUUID());
		recon.setCreateUser(user.getUsername());
		recon.setCreater(user.getName());
		// 保存物资信息
		purchReconMapper.savePurchRecon(recon);
		for (PurchReconMate mate : mates) {
			mate.setId(UUIDUtil.getUUID());
			mate.setReconCode(reconCode);
			purchReconMapper.saveReconMate(mate);
		}
		// 保存对账单信息
		for (PurchReconDebit debit : debits) {
			debit.setReconCode(reconCode);
			String attFile = debit.getAttFile();
			if (!StringUtils.isEmpty(attFile)) {
				JSONArray ja = JSONArray.parseArray(attFile);
				if (ja.size() > 0) {
					for (int i = 0; i < ja.size(); i++) {
						JSONObject obj = ja.getJSONObject(i);
						String docId = obj.getString("docId");
						Document doc = new Document();
						doc.setId(docId);
						doc.setLinkNo(reconCode);
						documentMapper.updateDocLink(doc);
					}
				}
			}
			purchReconMapper.saveReonDebit(debit);
		}
	}

	@Override
	@Transactional
	public void updateRecon(PurchRecon recon, List<PurchReconMate> mates, List<PurchReconDebit> debits) {
		SysUserDO user = UserCommon.getUser();
		String reconCode = recon.getReconCode();
		String status = recon.getReconStatus();
		String reconStatusDesc = getStatusDesc(status);
		recon.setReconStatusDesc(reconStatusDesc);
		recon.setModifyTime(new Date());
		recon.setModifyUser(user.getUsername());
		recon.setModifier(user.getName());
		purchReconMapper.updatePurchRecon(recon);
		// 更新对账单物资信息
		purchReconMapper.delMateByReconCode(reconCode);
		for (PurchReconMate mate : mates) {
			mate.setId(UUIDUtil.getUUID());
			mate.setReconCode(reconCode);
			purchReconMapper.saveReconMate(mate);
		}
		// 更新对账单扣款信息
		purchReconMapper.delDebitByReconCode(reconCode);
		for (PurchReconDebit debit : debits) {
			debit.setReconCode(reconCode);
			String attFile = debit.getAttFile();
			if (!StringUtils.isEmpty(attFile)) {
				JSONArray ja = JSONArray.parseArray(attFile);
				if (ja.size() > 0) {
					for (int i = 0; i < ja.size(); i++) {
						JSONObject obj = ja.getJSONObject(i);
						String docId = obj.getString("docId");
						Document doc = new Document();
						doc.setId(docId);
						doc.setLinkNo(reconCode);
						documentMapper.updateDocLink(doc);
					}
				}
			}
			purchReconMapper.saveReonDebit(debit);
		}
	}

	@Override
	@Transactional
	public void delRecon(List<String> reconCodes) {
		TaskParamsDO params = new TaskParamsDO();
		for (String reconCode : reconCodes) {
			PurchRecon recon = purchReconMapper.getPurchReconByCode(reconCode);
			purchReconMapper.deletePurchResonByCode(reconCode);
			purchReconMapper.delMateByReconCode(reconCode);
			purchReconMapper.delDebitByReconCode(reconCode);
			//删除任务数据
			params.setSdata1(recon.getId());
			taskService.removeTaskBySdata1(params );
		}
	}

	@Override
	@Transactional
	public void changeReconState(List<String> reconCodes, String status) {
		String reconStatusDesc = getStatusDesc(status);
		Map<String, Object> map = new HashMap<String, Object>();
		for (String reconCode : reconCodes) {
			map.put("reconCode", reconCode);
			map.put("reconStatus", status);
			map.put("reconStatusDesc", reconStatusDesc);
			purchReconMapper.changeReconStatus(map);
		}
	}

	@Override
	public List<PurchReconMate> getMatesByReconCode(String reconCode) {
		List<PurchReconMate> mates = purchReconMapper.getMateByReconCode(reconCode);
		return mates;
	}

	@Override
	public List<PurchReconDebit> getDebitsByReconCode(String reconCode) {
		List<PurchReconDebit> debits = purchReconMapper.getDebitByReconCode(reconCode);
		return debits;
	}

	/**
	 * 根据状态编码获取状态描述
	 * 
	 * @param status
	 * @return
	 */
	public String getStatusDesc(String status) {
		String reconStatusDesc = "";
		if ("YBC".equals(status)) {
			reconStatusDesc = "已保存";
		} else if ("YTJ".equals(status)) {
			reconStatusDesc = "已提交";
		} else if ("YQR".equals(status)) {
			reconStatusDesc = "已确认";
		} else if ("DQR".equals(status)){
			reconStatusDesc = "待确认";
		} else if ("YTH".equals(status)){
			reconStatusDesc = "已退回";
		}
		return reconStatusDesc;
	}

	@Override
	public List<PurchReconMate> getSapPurchReconMatesByMap(Map<String, Object> map) {
		List<PurchReconMate> mates = sapPurchReconMapper.getPurchReconMateByMap(map);
		return mates;
	}

	@Override
	public LayuiPage<PurchReconInvoce> getInvoiceByPage(Map<String, Object> map) {
		// 获取登录供应商的编码
		LayuiPage<PurchReconInvoce> page = new LayuiPage<PurchReconInvoce>();
		List<PurchReconInvoce> data = purchReconMapper.getInvoiceByPage(map);
		int count = purchReconMapper.getSuppPurchReconCount(map);
		page.setCount(count);
		page.setData(data);
		return page;
	}

	@Override
	public List<PurchReconInvoce> getInvocesByReconCode(String reconCode) {
		List<PurchReconInvoce> data = purchReconMapper.getInvocesByReconCode(reconCode);
		return data;
	}

	@Override
	public int saveConfirmReconInfo(String reconCode, List<PurchReconInvoce> list) {
		
		Map<String, Object> map=new HashMap<>();
		map.put("reconCode", reconCode);
		map.put("reconStatus", "YTJ");
		map.put("reconStatusDesc", "已提交");
		purchReconMapper.changeReconStatus(map);
		int size = list.size();
		if(list==null || size==0){
			return 0;
		}
		BigDecimal bg0=new BigDecimal(0);
		for (PurchReconInvoce invoce : list) {
			invoce.setReconCode(reconCode);
			BigDecimal taxMoney = invoce.getTaxMoney();
			BigDecimal totalMoney = invoce.getTotalMoney();
			if(taxMoney==null){
				invoce.setTaxMoney(bg0);
			}
			if(totalMoney==null){
				invoce.setTotalMoney(bg0);
			}
		}
		purchReconMapper.delInvoceByReconCode(reconCode);
		int i = purchReconMapper.batchSaveConfirmReconInfo(list);
		return i;
	}

	@Override
	public List<PurchReconMate> getPurchMateByMap(Map<String, Object> map) {
		return purchReconMapper.getPurchMateByMap(map);
	}

	@Override
	public PurchRecon getReconById(String id) {
		return purchReconMapper.getReconById(id);
	}
}
