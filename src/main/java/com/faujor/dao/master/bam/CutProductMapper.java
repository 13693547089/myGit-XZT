package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.CutProduct;
import com.faujor.entity.bam.CutProductDO;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.ProdMateDO;

public interface CutProductMapper {

	/**
	 * 打切品维护列表数据
	 * @param map
	 * @return
	 */
	public List<ProdMateDO> queryCutProductByPage(Map<String,Object> map);
	
	/**
	 * 打切品维护列表数据
	 * @param map
	 * @return
	 */
	public int queryCutProductByPageCount(Map<String,Object> map);
	/**
	 * 移除打切品
	 * @param prodIds
	 * @return
	 */
	public int deleteCutProductByprodId(String[] prodIds);
	/**
	 * 查询所有打切品
	 * @return
	 */
	public List<ProdMateDO> queryAllCutProduct();
	/**
	 * 添加打切品
	 * @param cp
	 * @return
	 */
	public int addCutProduct(CutProduct cp);
	/**
	 * 查询所有打切结构
	 * @return
	 */
	public List<CutStructure> queryAllCutStru();

	/**
	 * 查询包材打切结构
	 * @return
	 */
	public List<CutStructure> queryBaoCaiCutStru();
	
	/**
	 * 新建打切结构
	 * @param c
	 * @return
	 */
	public int addCutStru(CutStructure c);
	
	/**
	 * 删除所有打切结构
	 * @return
	 */
	public int deleteAllCutStru();
	/**
	 * 获取类号数量
	 * @return
	 */
	public List<String> queryClassCodes();
	/**
	 * 查询相同类号中最大的内容号
	 * @return
	 */
	public String queryMaxcontentCodeOfClassCode(String classCode);
	/**
	 * 根据内容号查询类号
	 * @param contentCode
	 * @return
	 */
	public String queryClassCodeByContentCode(String contentCode);
	/**
	 * 查询一个打切品信息
	 * @param mateId
	 * @return
	 */
	public ProdMateDO queryOneProdMateDOByProdId(String prodId);
	/**
	 *  修改打切品品物料版本
	 * @param prodId
	 * @param version
	 * @return
	 */
	public int updateCutProductVer(ProdMateDO prod);
	/**
	 * 根据版本查询打切物料的信息
	 * @param version
	 * @return
	 */
	public CutProduct queryProdMateByVersion(String mateId,String version);
	/**
	 * 为打切物料查询主包材 (类号+类名)
	 * @return
	 */
	public List<CutStructure> queryCutStruForCutProd();
	/**
	 * 查询打切品成品物料对应的半成品物料
	 * @return
	 */
	public List<ProdMateDO> queryMatesOfCutProduct();

	/**
	 * 查询打切品成品物料对应的半成品物料(包含供应商)
	 * @return
	 */
	public List<ProdMateDO> queryMatesOfCutProductWithSupp(String suppId);
	/**
	 * 查询导出打切品的数据
	 * @param mate
	 * @return
	 */
	public List<CutProductDO> queryCutProduct(ProdMateDO mate);
	/**
	 * 根据物料主键和是否特殊的标识查询物料是否在打切品中
	 * @param param
	 * @return
	 */
	public ProdMateDO queryCutProd(Map<String, Object> param);
	/**
	 * 查询打切品中所有特殊自制打切品
	 * @return
	 */
	public List<ProdMateDO> querySpecialCutLiaiMate();
	/**
	 * 获取上个月打切可生产订单
	 * @param mateCode
	 * @param suppId
	 * @param lastCutMonth
	 * @return
	 */
	public Integer getLastProdNumByMateCodeAndSuppIdAndCutMonth(@Param("mateCode")String mateCode, @Param("suppId")String suppId, @Param("lastCutMonth")String lastCutMonth);
	/**
	 * 获取上个月打切可生产订单
	 * @param mateCode
	 * @param lastCutMonth
	 * @return
	 */
	public Integer getLastProdNumByMateCodeAndCutMonth(@Param("mateCode")String mateCode, @Param("lastCutMonth")String lastCutMonth);
	/**
	 * 根据编号查询物料版本，版本为空则为‘QQQ’
	 * @param prodId
	 * @return
	 */
	public String queryOneProdMateByProdId(String prodId);

	public List<ProdMateDO> queryMatesOfCutLiaiBySuppIdAndCutMonth(@Param("suppId")String suppId, @Param("cutMonth")String cutMonth);
	/**
	 * 根据包材供应商编号，查询符合条件的打切品物料信息
	 * @param suppId
	 * @return
	 */
	public List<ProdMateDO> queryMatesOfCutProductWithBaoCaiSupp(String suppId);
}
