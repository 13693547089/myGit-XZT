package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.CutLiaisonVO;

public interface CutLiaisonMapper {

	/**
	 * 打切联络单列表数据
	 * @param map
	 * @return
	 */
	public List<CutLiaison> queryCutLiaisonByPage(Map<String,Object> map);
	
	/**
	 * 打切联络单的数量
	 * @param map
	 * @return
	 */
	public int queryCutLiaisonByPageCount(Map<String,Object> map);
	/**
	 * 新增打切联络单
	 * @param cutLiai
	 * @return
	 */
	public int addCutLiaison(CutLiaison cutLiai);
	/**
	 * 添加打切联络单下的物料
	 * @param clm
	 * @return
	 */
	public int addCutLiaiMate(CutLiaiMate clm);
	/**
	 * 删除打切联络单
	 * @param liaiIds
	 * @return
	 */
	public int deleteCutLiaisonByliaiIds(String[] liaiIds);
	/**
	 * 删除打切联络单下的物料
	 * @param liaiIds
	 * @return
	 */
	public int deleteCutLiaiMateByLiaiIds(String[] liaiIds);
	
	/**
	 * 提交打切联络单
	 * @param map
	 * @return
	 */
	public int updateCutLiaiStatusByliaiIds(Map<String, Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单的详细信息
	 * @param liaiId
	 * @return
	 */
	public CutLiaison queryCutLiaisonByLiaiId(String liaiId);

	public List<CutLiaiMate> queryCutLiaiMateByLiaiId(String liaiId);
	/**
	 * 打切联络单确认列表数据
	 * @param map
	 * @return
	 */
	public List<CutLiaison> queryCutLiaisonForManageByPage(Map<String, Object> map);
	/**
	 * 打切联络单确认列表数据的数量
	 * @param map
	 * @return
	 */
	public int queryCutLiaisonForManageByPageCount(Map<String, Object> map);
	/**
	 * 确认打切联络单
	 * @param map
	 * @return
	 */
	public int updateStatusOfCutLiaisonByLiaiId(Map<String, Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单信息
	 * @param liaiIds
	 * @return
	 */
	public List<CutLiaison> queryManyCutLiaisonByLiaiIds(List<String> liaiIds);
	/**
	 * 根据打切联络单的主键查询打切联络单下物料的信息
	 * @param liaiIds
	 * @return
	 */
	public List<CutLiaiMate> queryManyCutLiaiMateByLiaiIds(List<String> liaiIds);
	/**
	 * 根据计划月份查询打切联络单信息
	 * @param cutMonth
	 * @return
	 */
	public List<CutLiaison> queryListCutLiaisonByCutMonth(String planMonth);
	/**
	 * 特殊打切联络单数据来源
	 * @param map
	 * @return
	 */
	public List<CutLiaison> querySpeCutLiaisonByPage(Map<String, Object> map);
	/**
	 * 特殊打切联络单数量
	 * @param map
	 * @return
	 */
	public int querySpeCutLiaisonByPageCount(Map<String, Object> map);
	/**
	 * 根据打切联络单主键，查询blob字段
	 * @param liaiId
	 * @return
	 */
	public CutLiaison queryCutLiaiFieldsBlobByLiaiId(String liaiId);
	/**
	 * 查询所有打切联络单
	 * @return
	 */
	public List<CutLiaison> queryAllCutLiaison();
	/**
	 * 根据打切联络单物料的主键修改主包材数量
	 * @param cm
	 */
	public void updateCutLiaiMateMainStruNum(CutLiaiMate cm);
	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param cutMonth
	 * @param suppId
	 * @return
	 */
	public List<String> checkoutCutLiaiByMonthAndSuppId(String cutMonth, String suppId);
	/**
	 * 获取字段和对应数据
	 * @param mateId
	 * @param mateVersion
	 * @param planMonth
	 * @return
	 */
	public List<CutLiaisonVO> getHeaderFieldsAndFieldsByMateCodeAndVersionAndCutMonth(@Param("finMateId")String mateId, @Param("mateVersion")String mateVersion,
			@Param("cutMonth")String planMonth);

	/**
	 * 获取最新打切月份的一条数据
	 * @return
	 */
	CutLiaison queryNewCutMonthLiaison();
}
