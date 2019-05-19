package com.faujor.service.bam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppoQueryDO;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.utils.UserCommon;

public interface AppointService {

	/**
	 * 新建预约
	 * 
	 * @param appo
	 * @return
	 */
	boolean addAppoint(Appoint appo, List<AppoMate> list, List<AppoCar> carList);

	/**
	 * 预约申请列表
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAppointOfSuppByPage(Map<String, Object> map);

	/**
	 * 删除预约申请
	 * 
	 * @param appoIds
	 * @return
	 */
	boolean deleteAppointByAppoId(String[] appoIds);

	/**
	 * 修改预约申请
	 * 
	 * @param appo
	 * @return
	 */
	boolean upateAppoint(Appoint appo, List<AppoMate> appoMate, List<AppoCar> carList);

	/**
	 * 查询预约申请信息
	 * 
	 * @param appoId
	 * @return
	 */
	Appoint queryAppointByAppoId(String appoId);

	/**
	 * 预约管理分页展示数据
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAppointForManagerByPage(Map<String, Object> map);

	/**
	 * 修改预约的优先级
	 * 
	 * @param appo
	 * @return
	 */
	boolean updateAppointPriorityByAppoId(Appoint appo);

	/**
	 * 修改预约的状态
	 * 
	 * @param map
	 * @return
	 */
	boolean updateAppoStatusByAppoId(Map<String, Object> map);

	/**
	 * 预约发布展示数据
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAppointForIssueByPage(Map<String, Object> map);

	/**
	 * 根据预约日期查询当日的发布状况
	 * 
	 * @param appoDate
	 * @return
	 */
	List<Appoint> queryAppointForIssueByAppoDate(Date appoDate);

	/**
	 * 预约发布保存和发布
	 * 
	 * @param appo
	 * @return
	 */
	Map<String, Object> updateAffirmDate(Appoint appo, String funtype);

	/**
	 * 预约发布保存和发布
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	boolean updateAffirmDateByAppoId(List<Appoint> list, String type);

	/**
	 * 根据预约日期查询预约状况
	 * 
	 * @param appoDate
	 * @return
	 */
	List<Appoint> queryAppoStatByAppoDate(String appoDate);

	/**
	 * 根据预约单号查询预约详情和预约单下的物资
	 * 
	 * @param appoCode
	 * @return
	 */
	Map<String, Object> queryAppointByAppoCode(String appoCode);

	/**
	 * 根据预约单号修改预约的状态
	 * 
	 * @param map
	 * @return
	 */
	boolean updateAppoStatusByAppoCode(Map<String, Object> map);

	/**
	 * 获取预约申请车辆的信息
	 * 
	 * @param appoId
	 * @return
	 */
	List<AppoCar> queryAppoCarOfAppoint(String appoId);

	/**
	 * 预约申请作废
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> cancellAppointForManByAppoId(Map<String, Object> map);

	/**
	 * 查询所有已发布状态的预约申请
	 * 
	 * @return
	 */
	public List<Appoint> queryAllPublishedAppoint(Map<String, Object> map);

	/**
	 * 根据预约单号查询预约申请信息
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

	Map<String, Object> queryAppoByAppoCode(String mapgCode,String DeliCode);

	/**
	 * 修改预约单和送货单的预约日期和确认送货时间
	 * 
	 * @param appo
	 * @return
	 */
	boolean updateAppoDate(Appoint appo, String type);

	/**
	 * 预约查询报表
	 * 
	 * @param map
	 * @return
	 */
	List<AppoQueryDO> queryAppoReportByParams(Map<String, Object> map);

	/**
	 * 引用已拒绝的预约单
	 * @param appoCode
	 * @return
	 */
	Map<String, Object> queryRefuseAppoByAppoCode(String appoCode);
	/**
	 *邮件重发
	 * @param appoints
	 * @return
	 */
	Map<String, Object> sendEmailOfAppoint(List<Appoint> appoints);
	
	
	
}
