package com.faujor.service.basic;

import java.util.List;
import java.util.Map;

import com.faujor.entity.basic.Dic;
import com.faujor.entity.basic.DicCategory;

public interface BasicService {
	/**
	 * 通过 参数 获取所有的字典分类信息_1
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> findDicCategoryByParams_1(Map<String, Object> params);

	/**
	 * 通过 参数 获取字典信息列表_1
	 * 
	 * @param params
	 * @return
	 */
	List<Dic> findDicByParams_1(Map<String, String> params);

	/**
	 * 通过 参数id 获取字典分类、字典信息列表
	 * 
	 * @param params
	 * @return
	 */
	DicCategory findDicCategoryById(String id);

	/**
	 * 通过 参数 获取所有的字典分类信息
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> findDicCategoryByParams(Map<String, Object> params);

	/**
	 * 通过 参数 获取字典信息列表
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> findDicByParams(Map<String, String> params);

	/**
	 * 用于校验分类编码不重复
	 * 
	 * @param param
	 * @return
	 */
	int findDicCategoryByIdANDCode(Map<String, String> param);

	int saveDicInfo(DicCategory category);

	int deleteCategoryInfo(String selected);

	/**
	 * 通过 字典分类编码 获取该分类下的所有字典信息
	 * 
	 * @param cateCode
	 * @return
	 */
	List<Dic> findDicListByCategoryCode(String cateCode);

	/**
	 * map cateCode dicName
	 * 
	 * @param map
	 * @return
	 */
	List<Dic> findDicListByCategoryCode(Map<String, Object> map);

	/**
	 * 通过 字典分类的编码和字典的编码 获取唯一字典数据
	 * 
	 * @param dicCode
	 * @param cateCode
	 * @return
	 */
	Dic findDicByDicCodeANDCategoryCode(String dicCode, String cateCode);

	/**
	 * 根据分类id获取字典信息
	 * 
	 * @param cateId
	 * @return
	 */
	List<Dic> findDicListByCateId(String cateId);

	/**
	 * 获取从数据
	 * 
	 * @param id
	 * @return
	 */
	List<Dic> findDicListByParentId(String id);

	/**
	 * 根据id获取dic
	 * 
	 * @param id
	 * @return
	 */
	Dic findDicInfoById(String id);

	/**
	 * 保存数据
	 * 
	 * @param cate
	 * @param dic
	 * @return
	 */
	String saveDicAndCategoryInfo(DicCategory cate, Dic dic);

	/**
	 * 删除字典
	 * 
	 * @param id
	 * @return
	 */
	String deleteDicInfo(String id);

	/**
	 * 保存 普通字典信息
	 * 
	 * @param category
	 * @return
	 */
	String saveNormalDicInfo(DicCategory category);

}
