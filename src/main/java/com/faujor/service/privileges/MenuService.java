package com.faujor.service.privileges;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.faujor.entity.common.LarryTree;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.Tree;


/**
 * 菜单相关业务部分
 */
@Service
public interface MenuService {
	Tree<MenuDO> getSysMenuTree(Long id);
	List<LarryTree<MenuDO>> listMenuTree(Long id);
	Tree<MenuDO> getTree();
	Tree<MenuDO> getTree(Long id);
	List<MenuDO> list();
	int remove(Long id);
	int save(MenuDO menu);
	int update(MenuDO menu);
	MenuDO get(Long id);
	Set<String> listPerms(Long userId);
}
