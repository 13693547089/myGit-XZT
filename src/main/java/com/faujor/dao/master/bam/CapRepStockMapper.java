package com.faujor.dao.master.bam;

import com.faujor.entity.bam.psm.CapRepStock;
import java.util.List;

public interface CapRepStockMapper {
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
    int insert(CapRepStock record);
    /**
     * 查询
     * @param id
     * @return
     */
    CapRepStock selectByPrimaryKey(String id);
    /**
     * 根据主键进行查询
     * @param mainId
     * @return
     */
    List<CapRepStock> selectByMainId(String mainId);
    /**
     * 根据主键进行更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(CapRepStock record);
    /**
     * 根据mainId进行删除
     * @param mainId
     * @return
     */
    int deleteByMainId(String mainId);
    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(List<CapRepStock> list);


}