package com.faujor.dao.master.bam;

import com.faujor.entity.bam.psm.CapRep;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CapRepMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(CapRep record);

    CapRep selectByPrimaryKey(String id);

    List<CapRep> selectAll();

    int updateByPrimaryKey(CapRep record);
    /**
     * 分页获取产能上报数据
     * @param map
     * @return
     */
    List<CapRep> getCapRepByPage(Map<String, Object> map);
    /**
     * 获取产能上报的分页数据的数量
     * @param map
     * @return
     */
    Integer getCapRepCount(Map<String, Object> map);
    
    int updateStatus(@Param("ids")List<String> ids,@Param("status")String status);
    
    Integer getCountByMap(Map<String, Object> map);
}