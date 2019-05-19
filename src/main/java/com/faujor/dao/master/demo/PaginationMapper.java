package com.faujor.dao.master.demo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.Material;

public interface PaginationMapper {
	List<Material> materialList(RowBounds rb);
}
