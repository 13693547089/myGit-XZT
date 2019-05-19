package com.faujor.dao.master.basic;

import java.util.List;
import java.util.Map;

import com.faujor.entity.basic.Dic;
import com.faujor.entity.basic.DicCategory;

public interface BasicMapper {
	DicCategory findDicCategoryById(String id);

	List<DicCategory> findDicCategoryByParams(Map<String, Object> params);

	int countDicCategoryByParams(Map<String, Object> params);

	List<Dic> findDicByParams(Map<String, Object> params);

	int countDicByParams(Map<String, Object> params);

	Integer findDicCategoryByIdANDCode(Map<String, String> param);

	void saveDicInfo(Dic dic);

	int saveCategoryInfo(DicCategory category);

	int deleteDicInfoByID(String id);

	int deleteDicInfoByCateId(String cateId);

	void updateDicInfo(Dic dic);

	int deleteDicCategoryByID(String id);

	void updateCategoryInfo(DicCategory category);

	List<Dic> findDicByCodeParams(Map<String, Object> params);

	/**
	 * 根据分类的信息，获取字典列表
	 * 
	 * @param cate
	 * @return
	 */
	List<Dic> findDicListByCategory(DicCategory cate);

	/**
	 * 通过父节点id获取子信息
	 * 
	 * @param pid
	 * @return
	 */
	List<Dic> findDicListByParentId(String pid);

	/**
	 * 通过id获取字典信息
	 * 
	 * @param id
	 * @return
	 */
	Dic findDicById(String id);

	/**
	 * 批量保存字典信息
	 * 
	 * @param dicList
	 * @return
	 */
	int batchSaveDicInfo(List<Dic> dicList);

}
