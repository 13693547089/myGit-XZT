package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.CutProduct;
import com.faujor.entity.bam.CutProductDO;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.ProdMateDO;

public interface CutProductService {

	/**
	 * 打切维护品列表
	 * @param map
	 * @return
	 */
	Map<String,Object> queryCutProductByPage(Map<String,Object> map);
	/**
	 * 移除打切品
	 * @param prodIds
	 * @return
	 */
	boolean deleteCutProductByprodId(String[] prodIds);
	/**
	 * 查询所有打切品
	 * @return
	 */
	List<ProdMateDO> queryAllCutProduct();
	/**
	 * 添加打切品
	 * @param mateIds
	 * @return
	 */
	boolean addCutProduct(String[] mateIds);
	/**
	 * 查询所有打切结构
	 * @return
	 */
	List<CutStructure> queryAllCutStru();
	/**
	 * 查询包材打切结构
	 * @return
	 */
	List<CutStructure> queryBaoCaiCutStru();
	/**
	 * 新建打切结构
	 * @param list
	 * @return
	 */
	boolean addCutStru(List<CutStructure> list);
	/**
	 * 获取类号的数量
	 * @return
	 */
	List<String> queryClassCodes();
	/**
	 * 查询相同类号中最大的内容号
	 * @param classCode
	 * @return
	 */
	String queryMaxcontentCodeOfClassCode(String classCode);
	/**
	 * 获取一个类中最大的内容号
	 * @param contentCode
	 * @return
	 */
	String queryMaxContentCode(String contentCode);
	/**
	 * 
	 * @param suppId
	 * @return
	 */
	Map<String,Object> queryAllCutProductOfQualSupp(String suppId,String cutMonth);

	Map<String,Object> queryAllCutProductWithSuppOfQualSupp(String suppId,String cutMonth);

	/**
	 * 查询一个打切品信息
	 * @param mateId
	 * @return
	 */
	ProdMateDO queryOneProdMateDOByProdId(String prodId);
	/**
	 * 添加不同版本的物料打切品
	 * @param mateId
	 * @param version
	 * @return
	 */
	Map<String,Object> addCutProduct2(ProdMateDO prod);
	/**
	 * 修改打切品品物料版本
	 * @param prodId
	 * @param version
	 * @return
	 */
	Map<String,Object>  updateCutProductVer(ProdMateDO prod);
	/**
	 * 根据版本查询打切物料的信息
	 * @param version
	 * @return
	 */
	public CutProduct queryProdMateByVersion(String mateId,String version);
	/**
	 * 为打切物料查询主包材(类号+类名)
	 * @return
	 */
	public List<CutStructure> queryCutStruForCutProd();
	/**
	 * 查询导出打切品的数据
	 * @param mate
	 * @return
	 */
	List<CutProductDO> queryCutProduct(ProdMateDO pm);
	/**
	 * 处理选中的物料数据过滤，并添加到打切品维护列表中
	 * @param list
	 * @return
	 */
	Map<String, Object> dealMates(List<MateDO> list);
	/**
	 * 使用当前月份,得到上一个月的月份:月份的格式是:yyyy-MM
	 * @param currentDate
	 * @return
	 */
	public String getLastDate(String currentDate);
	Map<String, Object> queryMateOfCutLiai(String suppId, String cutMonth, String headerFiled);
	public List<ProdMateDO> getCountNum(List<ProdMateDO> list,String cutMonth,String suppId);
}
