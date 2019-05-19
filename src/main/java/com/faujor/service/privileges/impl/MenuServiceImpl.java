package com.faujor.service.privileges.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.common.MenuMapper;
import com.faujor.dao.master.common.RoleMenuMapper;
import com.faujor.entity.common.LarryTree;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.Tree;
import com.faujor.service.privileges.MenuService;
import com.faujor.service.privileges.RoleService;
import com.faujor.utils.BuildLarryTree;
import com.faujor.utils.BuildTree;

@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	MenuMapper menuMapper;
	@Autowired
	RoleMenuMapper roleMenuMapper;
	@Autowired
	RoleService roleService;

	/**
	 * @param 用户ID
	 * @return 树形菜单
	 */
	@Cacheable
	public Tree<MenuDO> getSysMenuTree(Long id) {
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.listMenuByUserId(id);
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("url", sysMenuDO.getUrl());
			attributes.put("icon", sysMenuDO.getIcon());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为0
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	public List<MenuDO> list() {
		List<MenuDO> menus = menuMapper.listMenu();
		return menus;
	}

	public int remove(Long id) {
		int result = menuMapper.remove(id);
		return result;
	}

	public int save(MenuDO menu) {
		int r = menuMapper.save(menu);
		return r;
	}

	public int update(MenuDO menu) {
		int r = menuMapper.update(menu);
		return r;
	}

	public MenuDO get(Long id) {
		MenuDO menuDO = menuMapper.getMenu(id);
		return menuDO;
	}

	public Tree<MenuDO> getTree() {
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.listMenu();
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			// Map<String, Object> attribute = new HashMap<>();
			// attribute.put("url", sysMenuDO.getUrl());
			// attribute.put("icon", sysMenuDO.getIcon());
			// List<Map<String, Object>> attributes = new ArrayList<>();
			// attributes.add(attribute);
			// tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	public Tree<MenuDO> getTree(Long id) {
		// 根据roleId查询权限
		List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(id);
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.listMenu();
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> state = new HashMap<String, Object>();
			Long menuId = sysMenuDO.getMenuId();
			if (menuIds.contains(menuId)) {
				List<MenuDO> chlids =menuMapper.queryChild(menuId.toString());
				if(chlids.size()==0){
					state.put("selected", true);
				}
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	public Set<String> listPerms(Long userId) {
		List<String> perms = menuMapper.listUserPerms(userId);
		Set<String> permsSet = new HashSet<String>();
		for (String perm : perms) {
			if (StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	public List<LarryTree<MenuDO>> listMenuTree(Long id) {
		List<LarryTree<MenuDO>> trees = new ArrayList<LarryTree<MenuDO>>();
		List<Long> roleIds = roleService.roleIdsByUserId(id);
		// List<MenuDO> menuDOs = menuMapper.listMenuByUserId(id);
		List<MenuDO> menuDOs = menuMapper.listMenuByRoleIds(roleIds);
		for (MenuDO sysMenuDO : menuDOs) {
			LarryTree<MenuDO> tree = new LarryTree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setpId(sysMenuDO.getParentId().toString());
			tree.setTitle(sysMenuDO.getName());
			tree.setFont(sysMenuDO.getIcon());
			tree.setIcon(sysMenuDO.getIcon());
			tree.setUrl(sysMenuDO.getUrl());
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		List<LarryTree<MenuDO>> list = BuildLarryTree.buildTree(trees);
		return list;
	}

}
