package com.faujor.dao.master.bam;

import com.faujor.entity.bam.psm.CapRepMate;

import java.util.List;

public interface CapRepMateMapper {
	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(String id);
    /**
     * 插入记录
     * @param record
     * @return
     */
    int insert(CapRepMate record);
    /**
     * 根据ID进行查询
     * @param id
     * @return
     */
    CapRepMate selectByPrimaryKey(String id);
    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(CapRepMate record);
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
    int batchInsert(List<CapRepMate> list);
    /**
     * 根据mainId进行查询
     * @param mainId
     * @return
     */
    List<CapRepMate> selectByMainId(String mainId);
    /**
     * 根据进行查询
     * @param mainId
     * @return
     */
    List<CapRepMate> selectBySuppNo(String suppNo);
}