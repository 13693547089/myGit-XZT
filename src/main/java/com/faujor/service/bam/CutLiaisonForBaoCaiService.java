package com.faujor.service.bam;

import com.alibaba.fastjson.JSONArray;
import com.faujor.entity.bam.CutBaoCaiMate;
import com.faujor.entity.bam.CutBaoCai;

import java.util.List;
import java.util.Map;

public interface CutLiaisonForBaoCaiService {

	/**
	 * 打切联络单列表
	 * @param map
	 * @return
	 */
	Map<String,Object> queryBaoCaiCurLiaisonByPage(Map<String, Object> map);
	/**
	 * 新增打切联络单
	 * @param cutLiai
	 * @param list
	 * @return
	 */
	boolean addBaoCaiCutLiaison(CutBaoCai cutLiai, List<CutBaoCaiMate> list);
	/**
	 * 删除打切联络单
	 * @param liaiIds
	 * @return
	 */
	boolean deleteBaoCaiCutLiaisonByliaiIds(String[] liaiIds);
	/**
	 * 提交打切联络单
	 * @return
	 */
	boolean updateBaoCaiCutLiaiStatusByliaiIds(Map<String, Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单的详细信息
	 * @param liaiId
	 * @return
	 */
	CutBaoCai queryBaoCaiCutLiaisonByLiaiId(String liaiId);
	/**
	 *  获取打切联络单的物料信息
	 * @param liaiId
	 * @return
	 */
	Map<String, Object> getBaoCaiContactSheet(String liaiId);
	/**
	 * 解析获取字段
	 * @param liaiId
	 * @return
	 */
	JSONArray queryBaoCaiLiaiMateFields(String liaiId);
	/**
	 * 打切联络单确认列表数据
	 * @param map
	 * @return
	 */
	Map<String, Object> queryBaoCaiCutLiaisonForManageByPage(Map<String, Object> map);
	/**
	 * 确认打切联络单
	 * @param map
	 * @return
	 */
	boolean updateBaoCaiStatusOfCutLiaisonByLiaiId(Map<String, Object> map);
	/**
	 * 编辑打切联络单
	 * @param cutLiai
	 * @param list
	 * @param type
	 * @return
	 */
	Map<String,Object> udpateBaoCaiCutLiaiMate(CutBaoCai cutLiai, List<CutBaoCaiMate> list, String type);
	/**
	 * 根据打切联络单的主键查询打切联络单信息
	 * @param liaiIds
	 * @return
	 */
	List<CutBaoCai> queryBaoCaiManyCutLiaisonByLiaiIds(List<String> liaiIds);
	/**
	 * 根据打切联络单的主键查询打切联络单下物料的信息
	 * @param liaiIds
	 * @return
	 */
	List<CutBaoCaiMate> queryBaoCaiManyCutLiaiMateByLiaiIds(List<String> liaiIds);
	/**
	 * 查询所有成品物料（0005）分页
	 * @param map
	 * @return
	 */
	Map<String, Object> queryBaoCaiFinMaterialByPage(Map<String, Object> map);
	/**
	 * 根据成品物料查询半成品物料信息
	 * @param mateCodes
	 * @return
	 */
	Map<String, Object> queryBaoCaiSemiFinMateByMateCode(List<String> mateCodes);
	/**
	 * 特殊打切联络单数据来源
	 * @param map
	 * @return
	 */
	Map<String, Object> queryBaoCaiSpeCutLiaisonByPage(Map<String, Object> map);
	/**
	 * 创建特殊打切时，获取特殊打切品物料数据
	 * @return
	 */
	Map<String, Object> queryBaoCaiSpecialCutLiaiMate(String cutMonth);
	/**
	 * 修改保存特殊打切联络单
	 * @param list
	 * @return
	 */
	boolean updateBaoCaiSpeCutLiaison(CutBaoCai cutLiai, List<CutBaoCaiMate> list);

	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param cutMonth
	 * @param suppId
	 * @return
	 */
	public boolean checkoutBaoCaiCutLiaiByMonthAndSuppId(String cutMonth, String suppId);

	/**
	 * 查询已提交的包材打切联络单号
	 * @return
	 */
	List<String> queryBaoCaiCutLiaiCodeList();
	
}
