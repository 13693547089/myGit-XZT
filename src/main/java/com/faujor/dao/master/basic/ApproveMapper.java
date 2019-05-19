package com.faujor.dao.master.basic;

import java.util.List;

import com.faujor.entity.basic.ApproveDO;

public interface ApproveMapper {

	/**
	 * 保存审批信息
	 * 
	 * @param approve
	 * @return
	 */
	int saveApproveInfo(ApproveDO approve);

	/**
	 * 根据业务主键获取审批信息
	 * 
	 * @param mainId
	 * @return
	 */
	List<ApproveDO> findApproveListByMainId(String mainId);

}
