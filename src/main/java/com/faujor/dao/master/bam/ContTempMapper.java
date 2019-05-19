package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.ContTemp;
import com.faujor.entity.document.Template;

public interface ContTempMapper {
	/**
	 * 分页获取模板数据
	 * @param map
	 * @return
	 */
	List<ContTemp> getTempByPage(Map<String, Object> map);
	/**
	 * 获取分页总数据条数
	 * @param map
	 * @return
	 */
	Integer getTempNum(Map<String, Object> map);
	/**
	 * 保存模板信息
	 * @param template
	 */
	void saveTemp(ContTemp contTemp);
	/**
	 * 删除模板信息
	 * @param code
	 */
	void deleteTempByNo(String tempNo);
	/**
	 * 更新模板信息
	 * @param template
	 */
	void updateTemp(ContTemp contTemp);
	/**
	 * 获取模板对象
	 * @param map
	 * @return
	 */
	ContTemp getTemp(Map<String, Object> map);
	/**
	 * 修改合同模板的发布状态
	 * @param contTemp
	 */
	void changeTempSatus(ContTemp contTemp);
	
}
