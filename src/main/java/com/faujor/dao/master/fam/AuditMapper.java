package com.faujor.dao.master.fam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.fam.AuditMate;
import com.faujor.entity.fam.AuditMould;
import com.faujor.entity.fam.AuditOrder;

public interface AuditMapper {

	List<AuditOrder> findAuditListByParams(AuditOrder ao, RowBounds rb);

	int countAuditListByParams(AuditOrder ao);

	AuditOrder findAuditById(String auditId);

	List<AuditMould> findAuditMouldByAuditId(String auditId);

	List<AuditMate> findAuditMateByAuditId(String auditId);

	@Select("select t.id from fam_audit_mould t where t.audit_id = #{auditId}")
	List<String> findAuditMouldIDsByAuditId(String auditId);

	@Select("select t.id from fam_audit_mate t where t.audit_id = #{auditId}")
	List<String> findAuditMateIDsByAuditId(String auditId);

	/**
	 * 批量插入稽核物料信息
	 * 
	 * @param addMateList
	 * @return
	 */
	int batchSaveAuditMate(List<AuditMate> addMateList);

	int updateAuditMate(AuditMate am);

	int batchRemoveAuditMateByIDs(List<String> omaIDs);

	int updateAuditMould(AuditMould am);

	int batchRemoveAuditMouldByIDs(List<String> omoIDs);

	int batchSaveAuditMould(List<AuditMould> addMouldList);

	int saveAuditOrder(AuditOrder ao);

	int updateAuditOrder(AuditOrder ao);

	@Delete("delete from fam_audit_order where id = #{id}")
	int removeAuditOrder(String id);

	@Delete("delete from fam_audit_mate where audit_id = #{id}")
	int removeAuditMateByAuditId(String id);

	@Delete("delete from fam_audit_mould where audit_id = #{id}")
	int removeAuditMouldByAuditId(String id);

	int batchRemoveAuditOrder(List<String> ids);

	int batchRemoveAuditMateByAuditIDs(List<String> ids);

	int batchRemoveAuditMouldByAuditIDs(List<String> ids);

	List<AuditOrder> auditConfirmData(AuditOrder ao, RowBounds rb);

	int countAuditConfirmData(AuditOrder ao);
	
	/**
	 * 添加物料时，查询上月月末库存
	 * @param param
	 * @return
	 */
	AuditMate queryLastMonthBala(Map<String, Object> param);
	/**
	 * 根据财务稽核的主键修改财务稽核的状态
	 * @param map
	 * @return
	 */
	int updateAuditSatusByAuditId(Map<String, Object> map);

}
