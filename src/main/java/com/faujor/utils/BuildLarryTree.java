package com.faujor.utils;

import java.util.ArrayList;
import java.util.List;

import com.faujor.entity.common.LarryTree;
import com.faujor.entity.common.LayuiTree;


public class BuildLarryTree {
	public static <T> LayuiTree<T> build(List<LayuiTree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<LayuiTree<T>> topNodes = new ArrayList<LayuiTree<T>>();
		for (LayuiTree<T> children : nodes) {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (LayuiTree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					parent.setHasChildren(true);
					children.setHasParent(true);
					continue;
				}
			}
		}
		LayuiTree<T> root = new LayuiTree<T>();
		if (topNodes.size() == 1) {
			root = topNodes.get(0);
		} else {
			root.setId("-1");
			root.setParentId("");
			root.setHasParent(false);
			root.setChildren(topNodes);
		}
		return root;
	}
	
	public static <T> List<LarryTree<T>> buildTree(List<LarryTree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<LarryTree<T>> topNodes = new ArrayList<LarryTree<T>>();
		for (LarryTree<T> children : nodes) {
			String pid = children.getpId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (LarryTree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					continue;
				}
			}
		}
		return topNodes;
	}
}
