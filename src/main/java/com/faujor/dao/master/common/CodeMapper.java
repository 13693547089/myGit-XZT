package com.faujor.dao.master.common;

import com.faujor.entity.common.Code;

public interface CodeMapper {
	/**
	 * 根据编码类型 获取编码配置信息
	 * @param codeType
	 * @return
	 */
	Code getCodeByCodeType(String codeType);
	/**
	 * 更新编码信息
	 * @param code
	 */
	void updateCode(Code code);
}
