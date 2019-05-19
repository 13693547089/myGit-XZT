package com.faujor.dao.master.bam;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppoQueryDO;
import com.faujor.entity.bam.Appoint;

public interface AppointMapper {

	/**
	 * 新建预约
	 *
	 * @param appoint
	 * @return
	 */
	public int addAppoint(Appoint appo);

	/**
	 * 预约申请列表分页展示当前供应商的预约
	 *
	 * @param map
	 * @return
	 */
	public List<Appoint> queryAppointOfSuppByPage(Map<String, Object> map);

	/**
	 * 查询预约申请的数量
	 *
	 * @param map
	 * @return
	 */
	public int queryAppointOfSuppByPageCount(Map<String, Object> map);

	/**
	 * 删除已保存状态的预约
	 *
	 * @param appoIds
	 * @return
	 */
	public int deleteAppointByAppoId(String[] appoIds);

	/**
	 * 删除预约申请下的物资信息
	 *
	 * @param appoIds
	 * @return
	 */
	public int deleteAppoMateByAppoId(String[] appoIds);

	/**
	 * 添加预约申请的物资信息
	 *
	 * @param appoMate
	 * @return
	 */
	public int addAppoMate(AppoMate appoMate);

	/**
	 * 修改预约申请
	 *
	 * @param appo
	 * @return
	 */
	public int upateAppoint(Appoint appo);

	/**
	 * 通过预约编号查询预约申请信息
	 *
	 * @param appoId
	 * @return
	 */
	public Appoint queryAppointByAppoId(String appoId);

	/**
	 * 删除预约申请下的物料
	 *
	 * @param appoId
	 * @return
	 */
	public int deleteAppoMateByOneAppoId(String appoId);

	/**
	 * 预约管理分页展示
	 *
	 * @param map
	 * @return
	 */
	public List<Appoint> queryAppointForManagerByPage(Map<String, Object> map);

	/**
	 * 预约管理数据条数
	 *
	 * @param map
	 * @return
	 */
	public int queryAppointForManagerByPageCount(Map<String, Object> map);

	/**
	 * 修改预约的优先级
	 *
	 * @param appoId
	 * @return
	 */
	public int updateAppointPriorityByAppoId(Appoint appo);

	/**
	 * 修改预约的预约状态
	 *
	 * @param map
	 * @return
	 */
	public int updateAppoStatusByAppoId(Map<String, Object> map);

	/**
	 * 预约发布展示数据
	 *
	 * @param map
	 * @return
	 */
	public List<Appoint> queryAppointForIssueByPage(Map<String, Object> map);

	/**
	 * 预约发布数据条数
	 *
	 * @param map
	 * @return
	 */
	public int queryAppointForIssueByPageCount(Map<String, Object> map);

	/**
	 * 根据预约日期查询预约发布状况
	 *
	 * @param appoDate
	 * @return
	 */
	public List<Appoint> queryAppointForIssueByAppoDate(Date appoDate);

	/**
	 * 预约发布保存和发布
	 *
	 * @param appo
	 * @return
	 */
	public int updateAffirmDate(Appoint appo);

	/**
	 * 根据预约日期查询当前日期预约状况
	 *
	 * @param appoDate
	 * @return
	 */
	public List<Appoint> queryAppoStatByAppoDate(String appoDate);

	/**
	 * 根据预约单号查询预约详情以及预约单下的物资
	 *
	 * @param appoCode
	 * @return
	 */
	public Appoint queryAppointByAppoCode(String appoCode);

	/**
	 * 根据预约单号修改预约状态
	 *
	 * @param map
	 * @return
	 */
	public int updateAppoStatusByAppoCode(Map<String, Object> map);

	/**
	 * 添加预约车辆信息
	 *
	 * @param ac
	 * @return
	 */
	public int addAppoCar(AppoCar ac);

	/**
	 * 删除这个预约单的车辆信息
	 *
	 * @param appoId
	 * @return
	 */
	public int deleteAppoCarByAppoId(String appoId);

	/**
	 * 获取预约申请的车辆信息
	 *
	 * @param appoId
	 * @return
	 */
	public List<AppoCar> queryAppoCarOfAppoint(String appoId);

	/**
	 * 删除预约单的车辆信息
	 *
	 * @param appoIds
	 * @return
	 */
	public int deleteAppoCarByManyAppoId(String[] appoIds);

	/**
	 * 预约申请作废
	 *
	 * @param map
	 * @return
	 */
	public int cancellAppointForManByAppoId(Map<String, Object> map);

	/**
	 * 查询所有已发布状态的预约申请
	 *
	 * @return
	 */
	public List<Appoint> queryAllPublishedAppoint(Map<String, Object> map);

	/**
	 * 根据预约单号查询预约申请的信息
	 *
	 * @param appoCode
	 * @return
	 */
	public Appoint queryOneAppointbyAppoCode(String appoCode);

	/**
	 * 根据预约单的List集合主键查询多个预约单
	 *
	 * @param appoIds
	 * @return
	 */
	public List<Appoint> queryManyAppointByAppoIds(List<String> appoIds);

	/**
	 *
	 * 根据搜索条件查询多个预约单
	 *
	 */
	public List<Appoint> queryManyAppointByList(Map<String, Object> map);

	/**
	 * 修改预约单的预约日期和确认送货时间
	 * 
	 * @param appo
	 * @return
	 */
	public boolean updateAppoDate(Appoint appo);

	/**
	 * 添加产销拒绝预约申请的原因
	 * 
	 * @param map
	 * @return
	 */
	public int updateAppoRefuseReasonByAppoIds(Map<String, Object> map);
	/**
	 * 根据预约单号查询已拒绝的预约申请
	 * @param appoCode
	 * @return
	 */
	public Appoint queryRefuseAppoByAppoCode(String appoCode);

	/**
	 * 预约查询报表
	 * 
	 * @param map
	 * @return
	 */
	public List<AppoQueryDO> queryAppoReportByParams(Map<String, Object> map);

	public List<AppoQueryDO> queryAppoReportForStatus(Map<String, Object> map);

	public List<AppoQueryDO> queryAppoReportForDate(Map<String, Object> map);

	public List<AppoQueryDO> queryAppoReportForStra(Map<String, Object> map);
	
	/**
	 * 修改预约单的邮箱发送状态
	 * @param map
	 * @return
	 */
	public int updateEmailStatus(Map<String, Object> map);
	/**
	 * 获取所有车辆类型，逗号分隔
	 * @param appoId
	 * @return
	 */
	public String getAllCarType(String appoId);

}
