package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.bam.delivery.OutData;

public interface ReceiveMapper {

	/**
	 * 查询收货列表的数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Receive> queryReceiveByPage(Map<String, Object> map);

	/**
	 * 收货列表数据的总条数
	 * 
	 * @param map
	 * @return
	 */
	public int queryReceiveByPageCount(Map<String, Object> map);

	/**
	 * 删除收货单
	 * 
	 * @param receIds
	 * @return
	 */
	public int deleteReceiveByReceId(String[] receIds);

	/**
	 * 删除收货单下的物资
	 * 
	 * @param receIds
	 * @return
	 */
	public int deleteReceMateByReceId(String[] receIds);

	/**
	 * 新建收货单
	 * 
	 * @param rece
	 * @return
	 */
	public int addReceive(Receive rece);

	/**
	 * 添加物资到收货单下
	 * 
	 * @param rm
	 * @return
	 */
	public int addReceMate(ReceMate rm);

	/**
	 * 根据收货单的编号查询收货单的详细信息
	 * 
	 * @param receId
	 * @return
	 */
	public Receive queryReceiveByReceId(String receId);

	/**
	 * 根据收货单的主键查询收货单下物资的信息
	 * 
	 * @param receId
	 * @return
	 */
	public List<ReceMate> queryReceMatesByReceId(String receId);

	/**
	 * 根据收货单的主键修改收货单的信息
	 * 
	 * @param rece
	 * @return
	 */
	public int updateReceiveByReceId(Receive rece);

	/**
	 * 根据接口返回值更新内向交货单数据
	 * 
	 * @param list2
	 * @return
	 */
	public int updateReceiveMateByOutData(OutData outData);

	/**
	 * 根据收货单id获取收货单中物料数据主键IDs
	 * 
	 * @param receId
	 * @return
	 */
	@Select("select id from bam_rece_mate where rece_id = #{receId}")
	public List<String> findMateIDsByReceId(String receId);

	/**
	 * 更新收货单的数据
	 * 
	 * @param receMate
	 * @return
	 */
	public int updateReceMate(ReceMate receMate);

	/**
	 * 根据ids删除收单物料
	 * 
	 * @param mateIDs
	 * @return
	 */
	public int removeReceMateByIDs(List<String> mateIDs);

	/**
	 * 批量保存数据
	 * 
	 * @param addList
	 * @return
	 */
	public int batchSaveReceMate(List<ReceMate> addList);

	/**
	 * 修改收货单的状态
	 * 
	 * @param map
	 * @return
	 */
	public int updateStatusOfReceiveByReceCode(Map<String, Object> map);

	/**
	 * 收货列表查看界面
	 * 
	 * @param map
	 * @return
	 */
	public List<Receive> queryAllReceiveByPage(Map<String, Object> map);

	/**
	 * 收货列表查看界面数据条数
	 * 
	 * @param map
	 * @return
	 */
	public int queryAllReceiveByPageCount(Map<String, Object> map);

	/**
	 * 获取未打占用标识的收货单，状态为已收货
	 * 
	 * @return
	 */
	public List<ReceMate> findReceMate();

	/**
	 * 更新收货单详情数据
	 * 
	 * @param receMate
	 * @return
	 */
	public int updateReceiveMateByReceMate(ReceMate receMate);
	/**
	 * 修改收货单的同步状态
	 * @param rece
	 */
	public void updateReceAsyncStatusByReceCode(Receive rece);
	/**
	 * 根据预约单号/直发单号获取收货单信息
	 * @param code
	 * @return
	 */
	public List<Receive> getReceiveListByAppoCode(String code);
	/**
	 * 根据送货单号查询收货单信息
	 * @param code
	 * @return
	 */
	public List<Receive> queryReceiveListByDeliCode(String code);
	/**
	 *根据收货单号查询收货单信息
	 * @param code
	 * @return
	 */
	public Receive queryOneReceiveByReceCode(String code);
	/**
	 * 修改收货单的内向交货单号和is_occupy占用状态
	 * @param receMate
	 * @return
	 */
	public int updateReceMateInboDeliCodeAndIsOccupy(ReceMate receMate);
	/**
	 * 根据id查询收货单物料信息
	 * @param id
	 * @return
	 */
	public ReceMate queryReceMateMessById(String id);

}
