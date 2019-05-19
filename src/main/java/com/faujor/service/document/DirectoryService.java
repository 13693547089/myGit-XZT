package com.faujor.service.document;

import java.util.List;

import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.document.Directory;

public interface DirectoryService {
	/**
	 * 获取目录的属性结构
	 * @return
	 */
	List<LayuiTree<Directory>> getDireTree();
	/**
	 * 根据路径编码获取改文件路径
	 * @param direCode
	 * @return
	 */
	Directory getDireByCode(String direCode);
	/**
	 * 根据路径ID获取该路径
	 * @param id
	 * @return
	 */
	Directory getDireById(String id);
}
