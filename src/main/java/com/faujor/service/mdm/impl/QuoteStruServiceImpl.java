package com.faujor.service.mdm.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.dao.master.mdm.QuoteStruMapper;
import com.faujor.dao.master.mdm.QuoteTempMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QuoteStruDO;
import com.faujor.entity.mdm.QuoteStruDetails;
import com.faujor.entity.mdm.QuoteTempDetails;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.mdm.QuoteStruService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("quoteStruService")
public class QuoteStruServiceImpl implements QuoteStruService {
	@Autowired
	private QuoteStruMapper quoteStruMapper;
	@Autowired
	private QuoteTempMapper quoteTempMapper;
	@Autowired
	private MaterialMapper mateMapper;
	@Autowired
	private OrgService orgService;

	@Override
	public Map<String, Object> quoteStruList(RowBounds rb, QuoteStruDO qsd) {
		Map<String, Object> params = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		params.put("ownId", user.getUserId());
		params.put("orgCode", "PURCHAROR");
		params.put("isContainOwn", true);
		List<UserDO> users = orgService.manageSubordinateUsers(params);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pages = new HashMap<String, Object>();
		pages.put("struDo", qsd);
//		List<MateDO> mate = mateMapper.findMaterialByUsers(users);
		pages.put("mate", users);
//		pages.put("users", users);
		List<QuoteStruDO> list = quoteStruMapper.findQuoteStruListByPage(rb, pages);
		int count = quoteStruMapper.countQuoteStruListByPage(pages);
		map.put("data", list);
		map.put("count", count);
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public List<QuoteStruDetails> findStruDetailsByStruId(String struId) {

		return quoteStruMapper.findQuoteStruDetailsByStruId(struId);
	}

	@Override
	public int saveStruData(QuoteStruDO quoteStru, List<QuoteStruDetails> list) {
		String id = quoteStru.getId();
		// 新增
		id = UUIDUtil.getUUID();
		quoteStru.setId(id);
		quoteStru.setCreateTime(new Date());
		SysUserDO user = UserCommon.getUser();
		quoteStru.setCreator(user.getUserId().toString());
		quoteStru.setCreatorName(user.getName());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("struId", id);
		params.put("list", list);
		int k = quoteStruMapper.batchSaveStruDetails(params);
		int i = 0;
		if (k > 0)
			i = quoteStruMapper.saveStruData(quoteStru);
		return i;
	}

	@Override
	public QuoteStruDO findStruById(String id) {
		return quoteStruMapper.findQuoteStruDoById(id);
	}

	@Override
	public int updateStruData(QuoteStruDO quoteStru, List<QuoteStruDetails> list) {
		int i = 0;
		// 修改
		quoteStru.setModifyTime(new Date());
		SysUserDO user = UserCommon.getUser();
		quoteStru.setModifier(user.getUserId().toString());
		quoteStru.setModifierName(user.getName());
		i += quoteStruMapper.updateStruData(quoteStru);
		List<String> oldIds = quoteStruMapper.findQuoteStruDetailsIdsByStruId(quoteStru.getId());
		List<QuoteStruDetails> addList = new ArrayList<QuoteStruDetails>();
		List<QuoteStruDetails> updateList = new ArrayList<QuoteStruDetails>();
		for (QuoteStruDetails n : list) {
			if (oldIds.contains(n.getId())) {
				updateList.add(n);
				int k = oldIds.indexOf(n.getId());
				oldIds.remove(k);
			} else {
				addList.add(n);
			}
		}
		// 批量删除
		if (oldIds.size() > 0)
			quoteStruMapper.batchRemoveDetails(oldIds);
		if (addList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("struId", quoteStru.getId());
			params.put("list", addList);
			i += quoteStruMapper.batchSaveStruDetails(params);
		}
		if (updateList.size() > 0)
			for (QuoteStruDetails q : updateList) {
				i += quoteStruMapper.updateStruDetails(q);
			}
		return i;
	}

	@Override
	public Map<String, Object> mateStruList(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<QuoteStruDO> list = new ArrayList<QuoteStruDO>();
		SysUserDO user = UserCommon.getUser();
		if ("modify".equals(type)) {
			QuoteStruDO struDO = new QuoteStruDO();
			struDO.setCreator(user.getUserId().toString());
			list = quoteStruMapper.findQuoteStruDoList(struDO);
		} else {
			list = quoteStruMapper.findMateStruList(user.getUserId());
		}
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public int saveBatchModify(QuoteStruDO qsd, String mateList) {
		// qsd中存储的是模板信息，mateList报价结构的列表
		List<QuoteStruDO> list = JsonUtils.jsonToList(mateList, QuoteStruDO.class);
		String quoteTempId = qsd.getQuoteTempId();
		List<QuoteTempDetails> qtdList = quoteTempMapper.tempDetailsListByTempId(quoteTempId);
		int i = quoteStruMapper.batchRemoveDetailsByStruIds(list);
		int k = 0;
		if (i >= 0) {
			for (QuoteStruDO qs : list) {
				qs.setQuoteTempId(quoteTempId);
				quoteStruMapper.updateStruData(qs);
				List<QuoteStruDetails> detailsList = copyTempDetailsToStruDetails(qtdList, qs.getId());
				// 批量变更为新的模板结构
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("struId", qs.getId());
				params.put("list", detailsList);
				quoteStruMapper.batchSaveStruDetails(params);
				k++;
			}
		}
		return k;
	}

	@Override
	public List<QuoteStruDetails> copyTempDetailsToStruDetails(List<QuoteTempDetails> list, String struId) {
		List<QuoteStruDetails> qsdList = new ArrayList<QuoteStruDetails>();
		Map<String, String> parentMap = new HashMap<String, String>();
		for (QuoteTempDetails td : list) {
			QuoteStruDetails qsd = new QuoteStruDetails();
			qsd.setId(UUIDUtil.getUUID());
			qsd.setMenuId(td.getMenuId());
			qsd.setStruId(struId);
			qsd.setSegmCode(td.getSegmCode());
			qsd.setSegmName(td.getSegmName());
			qsd.setAsseCode(td.getAsseCode());
			qsd.setAsseName(td.getAsseName());
			qsd.setMateCode(td.getMateCode());
			qsd.setDetailsNum(td.getDetailsNum());
			qsd.setUnit(td.getUnit());
			qsd.setStandard(td.getStandard());
			qsd.setMaterial(td.getMaterial());
			qsd.setRemark(td.getRemark());
			String parentId = td.getParentId();
			if ("0".equals(parentId)) {
				parentMap.put(td.getMenuId(), qsd.getId());
			} else {
				parentId = parentMap.get(td.getParentId());
			}
			qsd.setParentId(parentId);
			qsdList.add(qsd);
		}
		return qsdList;
	}

	@Override
	@Transactional
	public int batchRemoveStruData(List<QuoteStruDO> list) {
		int i = quoteStruMapper.batchRemoveDetailsByStruIds(list);
		int k = quoteStruMapper.batchRemoveStruData(list);
		return i + k;
	}

	@Override
	public int saveBatchCreate(QuoteStruDO qsd, String struList) {
		List<QuoteStruDO> list = JsonUtils.jsonToList(struList, QuoteStruDO.class);
		String tempId = qsd.getQuoteTempId();
		List<QuoteTempDetails> qtdList = quoteTempMapper.tempDetailsListByTempId(tempId);
		int i = 0;
		for (QuoteStruDO l : list) {
			l.setQuoteTempId(tempId);
			// 批量保存报价结构数据
			int k = quoteStruMapper.saveStruData(l);
			if (k > 0) {
				List<QuoteStruDetails> list2 = copyTempDetailsToStruDetails(qtdList, l.getId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("struId", l.getId());
				params.put("list", list2);
				int j = quoteStruMapper.batchSaveStruDetails(params);
				if (j > 0)
					i++;
			}
		}
		return i;
	}

}
