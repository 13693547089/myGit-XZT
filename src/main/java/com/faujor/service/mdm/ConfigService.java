package com.faujor.service.mdm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.web.multipart.MultipartFile;

import com.faujor.entity.mdm.OemPackSupp;

public interface ConfigService {

	/**
	 * 获取已经配置的物料编码
	 * 
	 * @param oemPackSupp
	 * @return
	 */
	List<String> findSelectedMateCodesByOemPackSupp(OemPackSupp oemPackSupp);

	/**
	 * 保存 oem 包材 供应商关系
	 * 
	 * @param jsonData
	 * @return
	 */
	int saveOemPackSuppInfo(String jsonData);

	/**
	 * 供应商配置关系
	 * 
	 * @param ops
	 * @param rb
	 * @return
	 */
	Map<String, Object> findOemPackSuppList(OemPackSupp ops, RowBounds rb);

	/**
	 * Excel导入
	 * 
	 * @param name
	 * @param file
	 * @return
	 * @throws IOException
	 */
	String importExcel(String name, MultipartFile file);

	/**
	 * 删除 供应商配置关系
	 * 
	 * @param list
	 * @return
	 */
	int removeOemPackSupp(List<OemPackSupp> list);

}
