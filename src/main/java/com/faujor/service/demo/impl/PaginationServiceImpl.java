package com.faujor.service.demo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.demo.PaginationMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.service.demo.PaginationService;

@Service("paginationService")
public class PaginationServiceImpl implements PaginationService {
	@Autowired
	private PaginationMapper pageMapper;
	@Autowired
	private MaterialMapper mateMapper;

	@Override
	public List<Material> materialList() {
		int offset = 0;
		int limit = 10;
		RowBounds rb = new RowBounds(offset, limit);
		List<MateDO> material = mateMapper.queryAllMaterial();
		String mateCode = "200004";
		List<Material> list = pageMapper.materialList(rb);
		return list;
	}

	@Override
	public Map<String, Object> queryMaterialByPage(int offset, Integer limit) {
		RowBounds rb = new RowBounds(offset, limit);
		List<Material> list = pageMapper.materialList(rb);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", "100");
		page.put("code", 0);
		return page;
	}

}
