package com.faujor.dao.master.common;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.faujor.entity.common.MenuDO;

public interface MenuMapper {
	@Select("SELECT  menu_id as menuId, parent_id as parentId, name, url, perms, type, icon, order_num as orderNum, gmt_create as gmtCreate, gmt_modified as gmtModifed FROM sys_menu where menu_id =#{id}")
	MenuDO getMenu(Long id);

	@Select("select distinct m.menu_id as menuId, parent_id as parentId, name, url, perms, type, icon, order_num as orderNum, gmt_create as gmtCreate, gmt_modified as gmtModified from sys_menu m left join sys_role_menu rm on m.menu_id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id where ur.user_id = #{id} and m.type in(0,1) order by m.type,order_num")
	List<MenuDO> listMenuByUserId(Long id);

	/**
	 * 根据角色查询出该角色所有的菜单
	 * 
	 * @param roleids
	 * @return
	 */
	@Select("<script> select distinct m.menu_id as menuId, parent_id as parentId, name, url, perms, type, icon, order_num as orderNum, gmt_create as gmtCreate, gmt_modified as gmtModified "
			+ "from sys_menu m left join sys_role_menu r on m.menu_id = r.menu_id where r.role_id in "
			+ "<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\"> "
			+ "#{item} </foreach> and m.type in(0,1) order by m.type,order_num </script>")
	List<MenuDO> listMenuByRoleIds(List<Long> roleIds);

	/**
	 * 根据角色查询出该角色所有的菜单，授权用
	 * 
	 * @param roleIds
	 * @return
	 */
	@Select("<script> select distinct m.menu_id as menuId, parent_id as parentId, name, url, perms, type, icon, order_num as orderNum, gmt_create as gmtCreate, gmt_modified as gmtModified "
			+ "from sys_menu m left join sys_role_menu r on m.menu_id = r.menu_id where r.role_id in "
			+ "<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\"> "
			+ "#{item} </foreach> order by m.type,order_num </script>")
	List<MenuDO> findMenuListForAuthByRoleIds(List<Long> roleIds);

	@Select("select m.perms from sys_menu m left join sys_role_menu rm on m.menu_id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id where ur.user_id = #{id} ")
	List<String> listUserPerms(Long id);

	@Select("SELECT menu_id as menuId, parent_id as parentId, name, url, perms, type, icon, order_num as orderNum, gmt_create as gmtCreate, gmt_modified  as gmtModified FROM sys_menu order by type")
	List<MenuDO> listMenu();

	@Delete("delete from sys_menu where menu_id = #{id}")
	int remove(Long id);

	@Insert("insert into sys_menu(parent_id,name,url,perms,type,order_num) values (#{parentId},#{name},#{url},#{perms},#{type},#{orderNum})")
	int save(MenuDO menu);

	@Update("UPDATE sys_menu SET  name=#{name}, url=#{url},  icon=#{icon},type=#{type},perms=#{perms},order_num=#{orderNum} WHERE menu_id=#{menuId}")
	int update(MenuDO menu);

	/**
	 * 查询菜单是否有子节点
	 * 
	 * @param menuId
	 * @return
	 */
	@Select("select m2.* from sys_menu m1 right join sys_menu m2 on m1.menu_id = m2.parent_id where m1.menu_id=#{menuId}")
	List<MenuDO> queryChild(String menuId);
}
