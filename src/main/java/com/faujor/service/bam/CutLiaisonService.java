package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;

public interface CutLiaisonService {

	/**
	 * 打切联络单列表
	 * @param map
	 * @return
	 */
	Map<String,Object> queryCurLiaisonByPage(Map<String,Object> map);
	/**
	 * 新增打切联络单
	 * @param cutLiai
	 * @param list
	 * @return
	 */
	boolean addCutLiaison(CutLiaison cutLiai, List<CutLiaiMate> list);
	/**
	 * 删除打切联络单
	 * @param liaiIds
	 * @return
	 */
	boolean deleteCutLiaisonByliaiIds(String[] liaiIds);
	/**
	 * 提交打切联络单
	 * @param liaiIds
	 * @return
	 */
	boolean updateCutLiaiStatusByliaiIds(Map<String,Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单的详细信息
	 * @param liaiId
	 * @return
	 */
	CutLiaison queryCutLiaisonByLiaiId(String liaiId);
	/**
	 *  获取打切联络单的物料信息
	 * @param liaiId
	 * @return
	 */
	Map<String, Object> getContactSheet(String liaiId);
	/**
	 * 解析获取字段
	 * @param liaiId
	 * @return
	 */
	JSONArray queryLiaiMateFields(String liaiId);
	/**
	 * 打切联络单确认列表数据
	 * @param map
	 * @return
	 */
	Map<String, Object> queryCutLiaisonForManageByPage(Map<String, Object> map);
	/**
	 * 确认打切联络单
	 * @param map
	 * @return
	 */
	boolean updateStatusOfCutLiaisonByLiaiId(Map<String, Object> map);
	/**
	 * 编辑打切联络单
	 * @param cutLiai
	 * @param list
	 * @param type
	 * @return
	 */
	Map<String,Object> udpateCutLiaiMate(CutLiaison cutLiai, List<CutLiaiMate> list, String type);
	/**
	 * 根据打切联络单的主键查询打切联络单信息
	 * @param liaiIds
	 * @return
	 */
	List<CutLiaison> queryManyCutLiaisonByLiaiIds(List<String> liaiIds);
	/**
	 * 根据打切联络单的主键查询打切联络单下物料的信息
	 * @param liaiIds
	 * @return
	 */
	List<CutLiaiMate> queryManyCutLiaiMateByLiaiIds(List<String> liaiIds);
	/**
	 * 查询所有成品物料（0005）分页
	 * @param map
	 * @return
	 */
	Map<String, Object> queryFinMaterialByPage(Map<String, Object> map);
	/**
	 * 根据成品物料查询半成品物料信息
	 * @param mateCodes
	 * @return
	 */
	Map<String, Object> querySemiFinMateByMateCode(List<String> mateCodes);
	/**
	 * 特殊打切联络单数据来源
	 * @param map
	 * @return
	 */
	Map<String, Object> querySpeCutLiaisonByPage(Map<String, Object> map);
	/**
	 * 创建特殊打切时，获取特殊打切品物料数据
	 * @return
	 */
	Map<String, Object> querySpecialCutLiaiMate(String cutMonth);
	/**
	 * 修改保存特殊打切联络单
	 * @param list
	 * @return
	 */
	boolean updateSpeCutLiaison(CutLiaison cutLiai,List<CutLiaiMate> list);
	/**
	 * 修改提交特殊打切联络单
	 * @param cutLiai
	 * @param list
	 * @return
	 */
	boolean updateSpeCutLiaison2(CutLiaison cutLiai, List<CutLiaiMate> list);
	/**
	 * 删除特殊打切联络单
	 * @param liaiIds
	 * @return
	 */
	boolean deleteSpeCutLiaisonByliaiIds(String[] liaiIds);
	
	public void updateMainStruNumOfCutLiaiMate(String liaiId);
	/**
	 *  已保存，已退回的打切联络单，更新物料信息
	 * @param suppId
	 * @param cutMonth
	 * @return
	 */
	Map<String, Object> updateCutLiaiMateBySuppAndMonth(String suppId, String cutMonth,String liaiId,String headerFiled);
	/**
	 * 已保存的特殊打切联络单更新物料数据
	 * @param liaiId
	 * @param cutMonth
	 * @return
	 */
	Map<String, Object> updateSpeCutLiaiMateByMonth(String liaiId, String cutMonth);
	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param cutMonth
	 * @param suppId
	 * @return
	 */
	public boolean checkoutCutLiaiByMonthAndSuppId(String cutMonth,String suppId);
	/**
	 * 作废已确认的打切联络单
	 * @param list
	 * @return
	 */
	boolean cancellCutLiaisonByLiaiIds(List<String> list);
	
	
	
}
