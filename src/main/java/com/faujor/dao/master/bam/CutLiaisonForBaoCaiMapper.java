package com.faujor.dao.master.bam;

import com.faujor.entity.bam.CutBaoCaiMate;
import com.faujor.entity.bam.CutBaoCai;
import com.faujor.entity.bam.CutLiaisonVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CutLiaisonForBaoCaiMapper {

	/**
	 * 打切联络单列表数据
	 * @param map
	 * @return
	 */
	public List<CutBaoCai> queryBaoCaiCutLiaisonByPage(Map<String, Object> map);

	/**
	 * 打切联络单的数量
	 * @param map
	 * @return
	 */
	public int queryBaoCaiCutLiaisonByPageCount(Map<String, Object> map);
	/**
	 * 新增打切联络单
	 * @param cutLiai
	 * @return
	 */
	public int addBaoCaiCutLiaison(CutBaoCai cutLiai);
	/**
	 * 添加打切联络单下的物料
	 * @param clm
	 * @return
	 */
	public int addBaoCaiCutLiaiMate(CutBaoCaiMate clm);
	/**
	 * 删除打切联络单
	 * @param liaiIds
	 * @return
	 */
	public int deleteBaoCaiCutLiaisonByliaiIds(String[] liaiIds);
	/**
	 * 删除打切联络单下的物料
	 * @param liaiIds
	 * @return
	 */
	public int deleteBaoCaiCutLiaiMateByLiaiIds(String[] liaiIds);

	/**
	 * 提交打切联络单
	 * @param map
	 * @return
	 */
	public int updateBaoCaiCutLiaiStatusByliaiIds(Map<String, Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单的详细信息
	 * @param liaiId
	 * @return
	 */
	public CutBaoCai queryBaoCaiCutLiaisonByLiaiId(String liaiId);

	public List<CutBaoCaiMate> queryBaoCaiCutLiaiMateByLiaiId(String liaiId);
	/**
	 * 打切联络单确认列表数据
	 * @param map
	 * @return
	 */
	public List<CutBaoCai> queryBaoCaiCutLiaisonForManageByPage(Map<String, Object> map);
	/**
	 * 打切联络单确认列表数据的数量
	 * @param map
	 * @return
	 */
	public int queryBaoCaiCutLiaisonForManageByPageCount(Map<String, Object> map);
	/**
	 * 确认打切联络单
	 * @param map
	 * @return
	 */
	public int updateBaoCaiStatusOfCutLiaisonByLiaiId(Map<String, Object> map);
	/**
	 * 根据打切联络单的主键查询打切联络单信息
	 * @param liaiIds
	 * @return
	 */
	public List<CutBaoCai> queryBaoCaiManyCutLiaisonByLiaiIds(List<String> liaiIds);
	/**
	 * 根据打切联络单的主键查询打切联络单下物料的信息
	 * @param liaiIds
	 * @return
	 */
	public List<CutBaoCaiMate> queryBaoCaiManyCutLiaiMateByLiaiIds(List<String> liaiIds);
	/**
	 * 根据计划月份查询打切联络单信息
	 * @return
	 */
	public List<CutBaoCai> queryBaoCaiListCutLiaisonByCutMonth(String planMonth);
	/**
	 * 特殊打切联络单数据来源
	 * @param map
	 * @return
	 */
	public List<CutBaoCai> queryBaoCaiSpeCutLiaisonByPage(Map<String, Object> map);
	/**
	 * 特殊打切联络单数量
	 * @param map
	 * @return
	 */
	public int queryBaoCaiSpeCutLiaisonByPageCount(Map<String, Object> map);
	/**
	 * 根据打切联络单主键，查询blob字段
	 * @param liaiId
	 * @return
	 */
	public CutBaoCai queryBaoCaiCutLiaiFieldsBlobByLiaiId(String liaiId);
	/**
	 * 查询所有打切联络单
	 * @return
	 */
	public List<CutBaoCai> queryAllCutLiaison();
	/**
	 * 根据打切联络单物料的主键修改主包材数量
	 * @param cm
	 */
	public void updateBaoCaiCutLiaiMateMainStruNum(CutBaoCaiMate cm);
	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param cutMonth
	 * @param suppId
	 * @return
	 */
	public List<String> checkoutBaoCaiCutLiaiByMonthAndSuppId(String cutMonth, String suppId);
	/**
	 * 获取字段和对应数据
	 * @param mateId
	 * @param mateVersion
	 * @param planMonth
	 * @return
	 */
	public List<CutLiaisonVO> getBaoCaiHeaderFieldsAndFieldsByMateCodeAndVersionAndCutMonth(@Param("finMateId") String mateId, @Param("mateVersion") String mateVersion,
                                                                                      @Param("cutMonth") String planMonth);
	/**
	 * 查询已提交的包材打切联络单号
	 * @return
	 */
	public List<String> queryBaoCaiCutLiaiCodeList();
	/**
	 * 根据引用单号和供应商编号查询物料数据
	 * @param citeLiaiCode
	 * @param suppId
	 * @return
	 */
	public List<CutBaoCaiMate> queryBaoCaiCutLiaiMateByLiaiCodeAndSuppId(@Param("citeLiaiCode")String citeLiaiCode, @Param("suppId")String suppId);

	public CutBaoCai queryBaoCaiListCutLiaisonByCutMonth2(String cutMonth);
}
