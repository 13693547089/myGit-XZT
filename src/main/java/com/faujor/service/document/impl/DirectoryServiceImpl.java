package com.faujor.service.document.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.document.DirectoryMapper;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.document.Directory;
import com.faujor.service.document.DirectoryService;
import com.faujor.utils.BuildLayuiTree;
@Service
public class DirectoryServiceImpl implements DirectoryService {
	@Autowired
	private DirectoryMapper directoryMapper;
	@Override
	public List<LayuiTree<Directory>> getDireTree() {
		List<LayuiTree<Directory>> trees = new ArrayList<LayuiTree<Directory>>();
		//获取所有的目录
		List<Directory> dires = directoryMapper.getAllDire();
		for (Directory dire : dires) {
			LayuiTree<Directory> tree = new LayuiTree<Directory>();
			tree.setId(String.valueOf(dire.getId()));
			tree.setParentId(String.valueOf(dire.getParentId()));
			tree.setName(dire.getDireName());
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		List< LayuiTree<Directory>> treeList=BuildLayuiTree.buildTree(trees);
		return treeList;
	}
	@Override
	public Directory getDireByCode(String direCode) {
		return directoryMapper.getDireByCode(direCode);
	}
	@Override
	public Directory getDireById(String id) {
		return directoryMapper.getDireById(id);
	}
}
