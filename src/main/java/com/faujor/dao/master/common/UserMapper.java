package com.faujor.dao.master.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.UserData;
import com.faujor.entity.privileges.UserDO;

public interface UserMapper {
	@Select("<script>"
			+ "select t.user_id as userId,t.username,t.email,t.mobile,t.status,t.user_id_create as userIdCreate,t.gmt_create as gmtCreate,t.name,"
			+ "t.gmt_modified as gmtModified, t1.username as leaderName, t.user_type as userType "
			+ "from sys_user t left join sys_user t1 on t.leader = t1.user_id <where>"
			+ "<if test=\"userId != null and userId != ''\">" + "and t.user_id = #{userId} " + "</if>"
			+ "<if test=\"username != null and username != ''\">" + "and t.username = #{username} " + "</if>"
			+ "<if test=\"password != null and password != ''\">" + "and t.password = #{password} " + "</if>"
			+ "<if test=\"email != null and email != ''\">" + "and t.email = #{email} " + "</if>"
			+ "<if test=\"mobile != null and mobile != ''\">" + "and t.mobile = #{mobile} " + "</if>"
			+ "<if test=\"status != null and status != ''\">" + "and t.status = #{status} " + "</if>"
			+ "<if test=\"userIdCreate != null and userIdCreate != ''\">" + "and t.user_id_create = #{userIdCreate} "
			+ "</if>" + "<if test=\"gmtCreate != null and gmtCreate != ''\">" + "and t.gmt_create = #{gmtCreate} "
			+ "</if>" + "<if test=\"gmtModified != null and gmtModified != ''\">"
			+ "and t.gmt_modified = #{gmtModified} " + "</if>" + "<if test=\"name != null and name != ''\">"
			+ "and t.name = #{name} " + "</if>" + "</where>" + "<if test=\"offset != null and limit != null\">" + ""
			+ "</if>" + "</script>")
	List<SysUserDO> list(Map<String, Object> param);

	@Select("<script>" + "select count(*) from sys_user " + "<where>" + "<if test=\"userId != null and userId != ''\">"
			+ "and user_id = #{userId} " + "</if>" + "<if test=\"username != null and username != ''\">"
			+ "and username = #{username} " + "</if>" + "<if test=\"password != null and password != ''\">"
			+ "and password = #{password} " + "</if>" + "<if test=\"email != null and email != ''\">"
			+ "and email = #{email} " + "</if>" + "<if test=\"mobile != null and mobile != ''\">"
			+ "and mobile = #{mobile} " + "</if>" + "<if test=\"status != null and status != ''\">"
			+ "and status = #{status} " + "</if>" + "<if test=\"userIdCreate != null and userIdCreate != ''\">"
			+ "and user_id_create = #{userIdCreate} " + "</if>" + "<if test=\"gmtCreate != null and gmtCreate != ''\">"
			+ "and gmt_create = #{gmtCreate} " + "</if>" + "<if test=\"gmtModified != null and gmtModified != ''\">"
			+ "and gmt_modified = #{gmtModified} " + "</if>" + "<if test=\"name != null and name != ''\">"
			+ "and name = #{name} " + "</if>" + "</where>" + "</script>")
	int count(Map<String, Object> map);

	public SysUserDO findByUserName(String username);

	/**
	 * 根据参数查询获取的用户数据
	 * 
	 * @param userDO
	 * @return
	 */
	public List<SysUserDO> findUsersByParams(SysUserDO userDO);

	public List<SysUserDO> findByUserNameList(Map<String, Object> param);

	@Select("select user_id as userId, username, password, email, mobile, status, user_id_create as userIdCreate, gmt_create as gmtCreate, gmt_modified as gmtModified, name, leader from sys_user where user_id = #{id}")
	SysUserDO get(Long id);

	// @Options(useGeneratedKeys = true, keyProperty = "userId")
	@Insert("insert into sys_user (user_id, username, password, email, mobile, status, user_id_create, gmt_create, gmt_modified, name, leader, user_type, supp_no, plain_code)"
			+ "values (#{userId}, #{username}, #{password}, #{email}, #{mobile}, #{status}, #{userIdCreate}, #{gmtCreate}, #{gmtModified}, #{name}, #{leader}, #{userType}, #{suppNo}, #{plainCode})")
	int save(SysUserDO obj);

	@Update("<script>" + "update sys_user " + "<set>" + "<if test=\"userId != null\">user_id = #{userId}, </if>"
			+ "<if test=\"username != null\">username = #{username}, </if>"
			+ "<if test=\"password != null\">password = #{password}, </if>"
			+ "<if test=\"email != null\">email = #{email}, </if>"
			+ "<if test=\"mobile != null\">mobile = #{mobile}, </if>"
			+ "<if test=\"status != null\">status = #{status}, </if>"
			+ "<if test=\"userIdCreate != null\">user_id_create = #{userIdCreate}, </if>"
			+ "<if test=\"gmtCreate != null\">gmt_create = #{gmtCreate}, </if>"
			+ "<if test=\"gmtModified != null\">gmt_modified = #{gmtModified}, </if>"
			+ "<if test=\"leader !=null \"> leader = #{leader}, </if>"
			+ "<if test=\"name != null\">name = #{name}, </if>"
			+ "<if test=\"plainCode != null and plainCode != ''\">plain_code = #{plainCode}, </if>"
			+ "<if test=\"suppNo != null\">supp_no = #{suppNo}, </if>" + "</set>" + "where user_id = #{userId}"
			+ "</script>")
	int update(SysUserDO obj);

	@Delete("delete from sys_user where user_id =#{userId}")
	int remove(Long user_id);

	@Delete("<script>" + "delete from sys_user where user_id in "
			+ "<foreach collection=\"list\" index=\"i\" open=\"(\" separator=\",\" close=\")\" item=\"item\"  >#{item}</foreach>"
			+ "</script>")
	int batchRemove(List<Long> list);

	public int updateUserMsg(UserData ud);

	public UserData findByUserMsg(Map<String, Object> map);

	public SysUserDO findUserById(long userId);

	@Select("select user_id as id, username as userName, password, email, status, name, leader from sys_user where user_id <> #{id} and user_type = 'user'")
	List<UserDO> listNotSelf(Long id);

	@Select("select user_id as id, username as userName, password, email, status, name, leader from sys_user where leader = #{leader}")
	List<UserDO> findUsersByLeader(long leader);

	@Select("select user_id as id, username as userName, password, email, status, name, leader from sys_user")
	List<UserDO> userList();

	List<SysUserDO> userListByParams(RowBounds rb, Map<String, Object> map);

	int countUserListByParams(Map<String, Object> map);

	List<SysUserDO> findUserByIDs(List<Long> iDs);

	int batchSaveUser(List<SysUserDO> userList);

	List<UserDO> findUserList();

	List<UserDO> findUserListByParams(Map<String, Object> map);

	UserDO findUserDOById(@Param("id") long ownId);

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	@Update("update sys_user set password = #{password}, plain_code = #{plainCode} where user_id = #{id}")
	int changePwd(UserDO user);

	/**
	 * 根据用户名和id获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	SysUserDO findUserForTaskByMap(Map<String, Object> map);

	/**
	 * 根据用户名获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	SysUserDO findUserByUsername(String username);
}
