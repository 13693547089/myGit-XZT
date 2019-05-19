package com.faujor.dao.cluster.mdm;

import java.util.List;

import com.faujor.entity.cluster.mdm.MaterialDO;
import com.faujor.entity.cluster.mdm.MaterialUnitDO;

public interface ClusterMaterialMapper {

	List<MaterialDO> clusterMaterialList(MaterialDO ma);

	List<MaterialUnitDO> test();

	List<MaterialDO> findClusterMaterialList(MaterialDO md);
}
