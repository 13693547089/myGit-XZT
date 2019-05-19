package com.faujor.service.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.dao.master.task.ProcessConfigMapper;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.service.task.ProcessConfigService;
import com.faujor.utils.BuildTree;
import com.faujor.utils.UUIDUtil;

@Service(value = "processConfigService")
public class ProcessConfigServiceImpl implements ProcessConfigService {
	@Autowired
	private ProcessConfigMapper configMapper;
	@Autowired
	private OrgMapper orgMapper;

	@Override
	public Map<String, Object> findProcessConfigList(RowBounds rb) {
		List<ProcessConfig> list = configMapper.findProcessConfigList(rb);
		int count = configMapper.findProcessConfigCount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		return map;
	}

	@Override
	public ProcessConfig findProcessConfigById(String id) {
		return configMapper.findProcessConfigById(id);
	}

	@Override
	public Map<String, Object> findConfigDetailsByProcessId(String processId) {
		List<ProcessConfigDetails> list = configMapper.findConfigDetailsByProcessId(processId);
		int count = configMapper.countConfigDetailsByProcessId(processId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public int removeConfigByIds(String configIds) {
		String[] strs = configIds.split(",");
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			String id = strs[i];
			if (!StringUtils.isEmpty(id)) {
				ids.add(id);
				configMapper.removeDetailsByConfigId(id);
			}
		}
		int i = configMapper.batchRemoveConfigByIDs(ids);
		return i;
	}

	@Override
	public List<Tree<OrgDo>> getExecutorSelectTree(String executor) {
		List<Tree<OrgDo>> trees = new ArrayList<Tree<OrgDo>>();
		List<OrgDo> orgDos = orgMapper.orgList();
		for (OrgDo sysMenuDO : orgDos) {
			Tree<OrgDo> tree = new Tree<OrgDo>();
			tree.setId(String.valueOf(sysMenuDO.getMenuId()));
			tree.setParentId(String.valueOf(sysMenuDO.getParentId()));
			tree.setText(sysMenuDO.getSname());
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		Tree<OrgDo> q = BuildTree.build(trees);
		List<Tree<OrgDo>> t = new ArrayList<Tree<OrgDo>>();
		t.add(q);
		return t;
	}

	@Override
	public int saveConfigInfo(ProcessConfig config, List<ProcessConfigDetails> detailsList) {
		String configId = config.getId();
		int i = 0;
		if (StringUtils.isEmpty(configId)) {
			// 新增
			configId = UUIDUtil.getUUID();
			config.setId(configId);
			// 保存从表数据
			List<ProcessConfigDetails> list = new ArrayList<ProcessConfigDetails>();
			for (ProcessConfigDetails t : detailsList) {
				t.setConfId(configId);
				t.setId(UUIDUtil.getUUID());
				list.add(t);
			}
			i += configMapper.saveDetailsInfo(list);
			i += configMapper.save(config);
		} else {
			// 从表数据做三部分处理，一部分新增，一部分删除，一部分更新
			List<ProcessConfigDetails> details = configMapper.findConfigDetailsByProcessId(configId);
			List<String> ids = configMapper.findConfigDetailsIdsByProcessId(configId);
			List<ProcessConfigDetails> addList = new ArrayList<ProcessConfigDetails>();
			List<ProcessConfigDetails> delList = new ArrayList<ProcessConfigDetails>();
			// 循环提交上来的结果集，得到要更新、删除和添加的数据
			String detailsIds = "";
			for (ProcessConfigDetails d1 : detailsList) {
				if (ids.contains(d1.getId())) {
					configMapper.updateDetailsInfo(d1);
				} else {
					d1.setId(UUIDUtil.getUUID());
					d1.setConfId(configId);
					addList.add(d1);
				}
				detailsIds += d1.getId() + ",";
			}
			for (ProcessConfigDetails d : details) {
				if (detailsIds.indexOf(d.getId()) == -1) {
					delList.add(d);
				}
			}

			if (addList.size() > 0) {
				i += configMapper.saveDetailsInfo(addList);
			}
			if (delList.size() > 0) {
				i += configMapper.removeDetailsInfo(delList);
			}
			i += configMapper.update(config);
		}
		return i;
	}

	@Override
	public ProcessConfigDetails findConfigDetailsById(String detailsId) {
		ProcessConfigDetails pcd = configMapper.findConfigDetailsById(detailsId);
		return pcd;
	}

	@Override
	public List<ProcessConfigDetails> findConfigDetailsByProcessCode(String processCode) {
		List<ProcessConfigDetails> list = configMapper.findConfigDetailsByProcessCode(processCode);
		return list;
	}

	@Override
	public List<ProcessConfigDetails> findConfigDetailsListByProcess(ProcessConfig config) {
		List<ProcessConfigDetails> list = configMapper.findConfigDetailsListByProcess(config);
		return list;
	}
}
