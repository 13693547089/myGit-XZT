package com.faujor.entity.common;

import java.util.ArrayList;
import java.util.List;
/**
 * larryMsTree
 * @author hql
 * @param <T>
 */
public class LarryTree<T> {
	//树Id
	private String id;
	//树的父节点
	private String pId;
	//树的描述文本
	private String title;
	//字体图标
	private String font;
	//图标
	private String icon;
	//地址
	private String url;
	//是否展开
	private boolean spread =false;
	//子节点
	private List<LarryTree<T>> children=new ArrayList<LarryTree<T>>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isSpread() {
		return spread;
	}
	public void setSpread(boolean spread) {
		this.spread = spread;
	}
	public List<LarryTree<T>> getChildren() {
		return children;
	}
	public void setChildren(List<LarryTree<T>> children) {
		this.children = children;
	}
}
