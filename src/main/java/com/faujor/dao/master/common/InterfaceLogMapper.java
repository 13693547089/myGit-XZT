package com.faujor.dao.master.common;

import com.faujor.entity.common.InterfaceLogMain;

public interface InterfaceLogMapper {
	/**
	 * 保存接口日志
	 * @param log
	 * @return
	 */
	int saveLog(InterfaceLogMain log);
}
