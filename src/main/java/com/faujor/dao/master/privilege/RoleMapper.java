package com.faujor.dao.master.privilege;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.privileges.Role;
import com.faujor.entity.privileges.RoleDO;

public interface RoleMapper {
	@Select("SELECT role_id, role_name, role_sign, remark, user_id_create, gmt_create, gmt_modified FROM sys_role")
	@Results({ @Result(property = "roleId", column = "role_id"), @Result(property = "roleName", column = "role_name"),
			@Result(property = "roleSign", column = "role_sign") })
	List<RoleDO> listRole();

	@Insert("INSERT INTO sys_role(role_id, role_name, role_sign, remark, user_id_create, gmt_create, gmt_modified)VALUES(#{roleId}, #{roleName}, #{roleSign}, #{remark}, #{userIdCreate}, #{gmtCreate}, #{gmtModified})")
	int save(RoleDO role);

	@Delete("DELETE FROM sys_role WHERE role_id=#{id}")
	int remove(Long id);

	@Select("SELECT role_id, role_name, role_sign, remark, user_id_create, gmt_create, gmt_modified FROM sys_role where role_id = #{roleId}")
	@Results({ @Result(property = "roleId", column = "role_id"), @Result(property = "roleName", column = "role_name"),
			@Result(property = "roleSign", column = "role_sign") })
	RoleDO get(Long id);

	@Update("update sys_role set role_name = #{roleName}, remark=#{remark}, role_sign = #{roleSign} where role_id=#{roleId}")
	int update(RoleDO role);

	/**
	 * 查询某用户是否包含某角色
	 * 
	 * @param params
	 * @param roleCode
	 * @param userId
	 * @return
	 */
	@Select("select t1.role_id, t1.role_name, t1.role_sign, t1.remark from sys_role t1 "
			+ "left join sys_user_role t2 on t2.role_id = t1.role_id "
			+ "where user_id = #{userId} and role_sign = #{roleCode}")
	@Results({ @Result(property = "roleId", column = "role_id"), @Result(property = "roleName", column = "role_name"),
			@Result(property = "roleSign", column = "role_sign") })
	List<Role> isIncludeRole(Map<String, Object> params);

	List<RoleDO> roleListByPage(RowBounds rb, RoleDO role);

	int countRoleListByPage(RoleDO role);

}
