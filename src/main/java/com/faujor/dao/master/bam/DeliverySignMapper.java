package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.delivery.DeliverySignDO;

public interface DeliverySignMapper {

	List<Delivery> findDeliveryByParams(Map<String, Object> map);

	int saveSignInfo(DeliverySignDO dsd);

}
