package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.Contract;

public interface ContMapper {
	/**
	 * 分页获取合同数据
	 * @param map
	 * @return
	 */
	List<Contract> getContByPage(Map<String, Object> map);
	/**
	 * 获取分页总数据条数
	 * @param map
	 * @return
	 */
	Integer getContNum(Map<String, Object> map);
	/**
	 * 保存合同信息
	 * @param template
	 */
	void saveCont(Contract cont);
	/**
	 * 删除合同信息
	 * @param code
	 */
	void deleteContById(String contId);
	/**
	 * 更新合同信息
	 * @param template
	 */
	void updateCont(Contract cont);
	/**
	 * 获取合同对象
	 * @param map
	 * @return
	 */
	Contract getCont(Map<String, Object> map);
	/**
	 * 修改合同的发布状态
	 * @param contTemp
	 */
	void changeContSatus(Contract cont);
	/**
	 * 查询某个合同编码是否存在
	 * @param id
	 * @param contCode
	 * @return
	 */
	int checkIsExist(@Param("id")String id, @Param("contCode")String contCode);
	
}
