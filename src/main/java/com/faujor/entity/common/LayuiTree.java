package com.faujor.entity.common;

import java.util.ArrayList;
import java.util.List;

public class LayuiTree<T> {
	/**
	 * 树的id
	 */
	private String id;
	/**
	 * 树的名称
	 */
	private String name;
	/**
	 * 是否展开
	 */
	private boolean spread = false;
	/**
	 * 节点链接
	 */
	private String href;
	/**
	 * 是否有父节点
	 */
	private boolean hasParent = false;

	private String parentId;
	/**
	 * 是否有子节点
	 */
	private boolean hasChildren = false;
	/**
	 * 子节点集合
	 */
	private List<LayuiTree<T>> children = new ArrayList<LayuiTree<T>>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<LayuiTree<T>> getChildren() {
		return children;
	}

	public void setChildren(List<LayuiTree<T>> children) {
		this.children = children;
	}
}
