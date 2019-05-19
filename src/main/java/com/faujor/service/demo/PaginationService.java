package com.faujor.service.demo;

import java.util.List;
import java.util.Map;

import com.faujor.entity.mdm.Material;

public interface PaginationService {
	List<Material> materialList();

	Map<String, Object> queryMaterialByPage(int offset, Integer limit);
}
