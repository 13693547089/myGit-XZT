package com.faujor.dao.master.document;

import java.util.List;

import com.faujor.entity.document.Directory;

public interface DirectoryMapper {
	/**
	 * 获取所有的目录
	 * @return
	 */
	List<Directory> getAllDire();
	/**
	 * 根据路径编码获取文件路径
	 * @return
	 */
	Directory getDireByCode(String direCode);
	/**
	 * 根据目录ID
	 * @param id
	 * @return
	 */
	Directory getDireById(String id);
}
