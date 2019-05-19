package com.faujor.service.mdm.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.mdm.QuoteTempMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QuoteTemp;
import com.faujor.entity.mdm.QuoteTempDetails;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QuoteTempService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("quoteTempService")
public class QuoteTempServiceImpl implements QuoteTempService {
	@Autowired
	private QuoteTempMapper tempMapper;
	@Autowired
	private CodeService codeService;

	@Override
	public Map<String, Object> tempList(RowBounds rb, Map<String, Object> params) {
		List<QuoteTemp> list = tempMapper.tempListMap(rb, params);
		int i = tempMapper.countTempList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", i);
		result.put("data", list);
		result.put("code", "0");
		result.put("msg", "");
		return result;
	}

	@Override
	public List<QuoteTemp> tempList() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<QuoteTemp> list = tempMapper.tempList(params);
		return list;
	}

	@Override
	public List<QuoteTempDetails> tempDetailsListByTempId(String tempId) {
		List<QuoteTempDetails> list = tempMapper.tempDetailsListByTempId(tempId);
		return list;
	}

	@Override
	public QuoteTemp findTempById(String tempId) {
		QuoteTemp temp = tempMapper.findTempById(tempId);
		return temp;
	}

	@Override
	public int removeSeg(String id, String parentId) {
		int k = 0;
		if (!parentId.equals("0")) {
			k += tempMapper.removeTempDetailsById(id);
		} else {
			QuoteTempDetails qtd = new QuoteTempDetails();
			qtd.setMenuId(id);
			qtd.setParentId(parentId);
			k += tempMapper.removeTempDetails(qtd);
		}

		return k;
	}

	@Override
	public int saveSeg(QuoteTempDetails details) {
		String id = details.getMenuId();
		if (id == null) {
			id = UUIDUtil.getUUID();
		}
		details.setMenuId(id);
		details.setCreateTime(new Date());
		SysUserDO user = UserCommon.getUser();
		details.setCreator(user.getUserId().toString());
		details.setCreatorName(user.getUsername());
		int k = tempMapper.saveTempDetails(details);
		return k;
	}

	@Override
	public QuoteTempDetails findTempDetailsById(String detailsId) {
		QuoteTempDetails td = tempMapper.findTempDetailsById(detailsId);
		return td;
	}

	@Override
	public int save(QuoteTemp temp, String jsonData) {
		String tempCode = temp.getTempCode();
		if (StringUtils.isEmpty(tempCode))
			temp.setTempCode(codeService.getCodeByCodeType("quoteTemp"));
		Integer v = temp.getdVersion();
		SysUserDO user = UserCommon.getUser();
		if (v == null) {
			temp.setdVersion(0);
			temp.setCreateTime(new Date());
			temp.setCreator(user.getUserId().toString());
			temp.setCreatorName(user.getName());
			if (jsonData != null) {
				List<QuoteTempDetails> detailsList = JsonUtils.jsonToList(jsonData, QuoteTempDetails.class);
				for (QuoteTempDetails qtd : detailsList) {
					qtd.setTempId(temp.getId());
				}
				tempMapper.batchSaveTempDetails(detailsList);
			}
			return tempMapper.saveTemp(temp);
		} else {
			v++;
			temp.setdVersion(v);
			temp.setModifyTime(new Date());
			temp.setModifier(user.getUserId().toString());
			temp.setModifierName(user.getName());
			if (jsonData != null) {
				List<String> tdIDs = tempMapper.tempDetailsIDsByTempId(temp.getId());
				List<QuoteTempDetails> detailsList = JsonUtils.jsonToList(jsonData, QuoteTempDetails.class);
				List<QuoteTempDetails> addList = new ArrayList<QuoteTempDetails>();
				for (QuoteTempDetails qtd : detailsList) {
					String id = qtd.getMenuId();
					qtd.setTempId(temp.getId());
					if (tdIDs.contains(id)) {
						tempMapper.updateTempDetails(qtd);
						int index = tdIDs.indexOf(id);
						tdIDs.remove(index);
					} else {
						addList.add(qtd);
					}
				}
				if (addList.size() > 0)
					tempMapper.batchSaveTempDetails(addList);
				if (tdIDs.size() > 0)
					tempMapper.batchRemoveTempDetailsByIDs(tdIDs);
			}

			return tempMapper.updateTemp(temp);
		}
	}

	@Override
	public int removeTemp(String id) {
		QuoteTemp temp = new QuoteTemp();
		temp.setId(id);
		int k = tempMapper.removeTempDetailsByTempId(id);
		int i = 0;
		if (k >= 0) {
			i += tempMapper.removeTemp(temp);
		}
		return i;
	}

	@Override
	public int batchRemoveTemp(List<String> list) {
		int i = tempMapper.batchRemoveTempDetailsByTempIds(list);
		int k = 0;
		if (i >= 0) {
			k = tempMapper.batchRemoveTemp(list);
		}
		return k;
	}

	@Override
	public int checkCode(String code, String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("id", id);
		int i = tempMapper.checkCode(params);
		return i;
	}

	@Override
	public List<QuoteTempDetails> copyTemp(String tempId, String target) {
		List<QuoteTempDetails> list = tempMapper.tempDetailsListByTempId(tempId);
		List<QuoteTempDetails> newList = new ArrayList<QuoteTempDetails>();
		Map<String, String> parentMap = new HashMap<String, String>();
		for (QuoteTempDetails details : list) {
			QuoteTempDetails qtd = new QuoteTempDetails();
			String id = UUIDUtil.getUUID();
			qtd.setMenuId(id);
			qtd.setTempId(target);
			qtd.setAsseCode(details.getAsseCode());
			qtd.setAsseName(details.getAsseName());
			qtd.setSegmCode(details.getSegmCode());
			qtd.setSegmName(details.getSegmName());
			qtd.setMateCode(details.getMateCode());
			qtd.setDetailsNum(details.getDetailsNum());
			qtd.setUnit(details.getUnit());
			qtd.setStandard(details.getStandard());
			qtd.setRemark(details.getRemark());
			String parentId = details.getParentId();
			if ("0".equals(parentId)) {
				parentMap.put(details.getMenuId(), qtd.getMenuId());
			} else {
				parentId = parentMap.get(details.getParentId());
			}
			qtd.setParentId(parentId);
			newList.add(qtd);
		}
		return newList;
	}

	@Override
	public List<QuoteTemp> findTempListForSelected() {
		return tempMapper.findTempListForSelected();
	}

}
