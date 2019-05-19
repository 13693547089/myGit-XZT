package com.faujor.dao.master.bam;

import com.faujor.entity.bam.psm.CapRepOrder;
import java.util.List;

public interface CapRepOrderMapper {
	/**
	 * 根据主键进行删除
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(String id);
    /**
     * 插入记录
     * @param record
     * @return
     */
    int insert(CapRepOrder record);
    /**
     * 根据主键进行查询
     * @param id
     * @return
     */
    CapRepOrder selectByPrimaryKey(String id);
    /**
     * 根据主键进行更新
     */
    int updateByPrimaryKey(CapRepOrder record);
    /**
     * 根据主键进行删除
     * @param mainId
     * @return
     */
    int deleteByMainId(String mainId);
    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(List<CapRepOrder> list);
    /**
     * 根据主键进行查询
     * @param mainId
     * @return
     */
    List<CapRepOrder> selectByMainId(String mainId);


}