package com.faujor.dao.master.document;

import java.util.List;
import java.util.Map;

import com.faujor.entity.document.Template;

public interface TemplateMapper {
	/**
	 * 分页获取模板数据
	 * @param map
	 * @return
	 */
	List<Template> getTempByPage(Map<String, Object> map);
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
	void saveTemp(Template template);
	/**
	 * 删除模板信息
	 * @param code
	 */
	void deleteTempByNo(String tempNo);
	/**
	 * 更新模板信息
	 * @param template
	 */
	void updateTemp(Template template);
	/**
	 * 获取模板对象
	 * @param map
	 * @return
	 */
	Template getTemp(Map<String, Object> map);
	
}
