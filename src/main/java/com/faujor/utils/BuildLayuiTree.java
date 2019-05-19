package com.faujor.utils;

import java.util.ArrayList;
import java.util.List;

import com.faujor.entity.common.LayuiTree;

public class BuildLayuiTree {
	public static <T> LayuiTree<T> build(List<LayuiTree<T>> nodes, String rootName) {
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
			root.setName(rootName);
			root.setParentId("");
			root.setHasParent(false);
			root.setChildren(topNodes);
			root.setSpread(true);
		}
		return root;
	}

	public static <T> List<LayuiTree<T>> buildTree(List<LayuiTree<T>> nodes) {
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
		return topNodes;
	}
}
