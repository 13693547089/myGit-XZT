package com.faujor.dao.master.mdm;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.mdm.OemPackSupp;

public interface MdmConfigMapper {

	/**
	 * 根据参数获取 已经配置的物料编码
	 * 
	 * @param oemPackSupp
	 * @return
	 */
	List<String> findSelectedMateCodesByOemPackSupp(OemPackSupp oemPackSupp);

	/**
	 * 删除数据
	 * 
	 * @param ops
	 * @return
	 */
	int deleteOemPackSuppByOPS(OemPackSupp ops);

	/**
	 * 批量保存
	 * 
	 * @param list
	 * @return
	 */
	int batchSaveOemPackSupp(List<OemPackSupp> list);

	/**
	 * 供应商配置列表
	 * 
	 * @param ops
	 * @param rb
	 * @return
	 */
	List<OemPackSupp> findOemPackSuppList(OemPackSupp ops, RowBounds rb);

	int countOemPackSuppList(OemPackSupp ops);

	/**
	 * 移除 供应商配置关系
	 * 
	 * @param list
	 * @return
	 */
	int removeOemPackSupp(List<OemPackSupp> list);

}
