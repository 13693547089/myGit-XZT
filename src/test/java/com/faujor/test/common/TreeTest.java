package com.faujor.test.common;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.utils.BuildLayuiTree;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeTest {
	@Autowired
	private OrgMapper orgMapper;
	
	@Test
	public void test() {
		List<LayuiTree<OrgDo>> trees = new ArrayList<LayuiTree<OrgDo>>();
		List<OrgDo> orgDos = orgMapper.orgList();
		for (OrgDo sysMenuDO : orgDos) {
			LayuiTree<OrgDo> tree = new LayuiTree<OrgDo>();
			tree.setId(String.valueOf(sysMenuDO.getMenuId()));
			tree.setParentId(String.valueOf(sysMenuDO.getParentId()));
			tree.setName(sysMenuDO.getSname());
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
//		LayuiTree<OrgDo> q = BuildLayuiTree.build(trees);
	
	}
}
