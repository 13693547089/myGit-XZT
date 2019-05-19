package com.faujor.dao.cluster.mdm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.cluster.mdm.SupplierDO;

public interface ClusterSuppMapper {

	List<SupplierDO> findSupplierList();
	
	String getSuppRangeDescBySapIdAndSuppRange(@Param("sapId")String sapId,@Param("suppRange")String suppRange);
}
