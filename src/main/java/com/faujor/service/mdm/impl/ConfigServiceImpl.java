package com.faujor.service.mdm.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.mdm.MdmConfigMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.OemPackSupp;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.mdm.ConfigService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {
	@Autowired
	private MdmConfigMapper configMapper;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private QualSuppService qualSuppService;

	@Override
	public List<String> findSelectedMateCodesByOemPackSupp(OemPackSupp oemPackSupp) {
		String selected_col = oemPackSupp.getSelected_col();
		if (StringUtils.isEmpty(selected_col))
			oemPackSupp.setSelected_col("t.MATE_CODE");
		return configMapper.findSelectedMateCodesByOemPackSupp(oemPackSupp);
	}

	@Override
	@Transactional
	public int saveOemPackSuppInfo(String jsonData) {
		if (StringUtils.isEmpty(jsonData))
			return 0;

		List<OemPackSupp> list = JsonUtils.jsonToList(jsonData, OemPackSupp.class);
		OemPackSupp ops = list.get(0);
		configMapper.deleteOemPackSuppByOPS(ops);
		int j = configMapper.batchSaveOemPackSupp(list);
		return j;
	}

	@Override
	public Map<String, Object> findOemPackSuppList(OemPackSupp ops, RowBounds rb) {
		List<OemPackSupp> list = configMapper.findOemPackSuppList(ops, rb);
		int i = configMapper.countOemPackSuppList(ops);
		Map<String, Object> result = new HashMap<>();
		result.put("code", "0");
		result.put("msg", "");
		result.put("data", list);
		result.put("count", i);
		return result;
	}

	@SuppressWarnings("resource")
	@Override
	@Transactional
	public String importExcel(String filename, MultipartFile file) {
		boolean isExcel2003 = true;
		String str = "";
		if (filename.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		InputStream is;
		try {
			is = file.getInputStream();
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
			} else {
				wb = new XSSFWorkbook(is);
			}
			Sheet sheet = wb.getSheetAt(0);
			if (sheet != null) {
				str = "文件上传成功";
			} else {
				str = "上传失败sheet为空";
				return str;
			}

			SysUserDO user = UserCommon.getUser();
			if (user == null)
				return "未登录";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ownId", user.getUserId());
			paramMap.put("orgCode", "PURCHAROR");
			paramMap.put("isContainOwn", true);
			// 获取这个管理员下的采购员
			List<UserDO> list1 = orgService.manageSubordinateUsers(paramMap);
			paramMap.put("purcharor", list1);
			MateDO mateDO = new MateDO();
			mateDO.setMateType("0002");
			paramMap.put("mate", mateDO);
			// 这个人管理的所有的半成品物料
			paramMap.put("concat_col", "CONCAT(m.MATE_CODE, CONCAT('-', m.MATE_NAME))");
			List<String> codes = materialService.findConcatColOfPurcharor(paramMap);

			QualSupp qualSupp = new QualSupp();
			qualSupp.setCategory("Z001");
			qualSupp.setConcat_col("CONCAT(t.supp_id, CONCAT('-', t.supp_name))");
			List<String> suppConcatCol = qualSuppService.queryQualSuppConcatColByQualSupp(qualSupp);
			List<OemPackSupp> list = new ArrayList<>();
			for (int r = 1; r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				int k = 0;
				OemPackSupp ops = new OemPackSupp();
				// 从excel第二行开始获取每个单元格的数据
				Cell c0 = row.getCell(0); // OEM供应商编码
				String errTxt = "";
				if (c0 != null && !StringUtils.isEmpty(c0.getStringCellValue())) {
					String oemCode = c0.getStringCellValue();
					ops.setOemSuppCode(oemCode);
				} else {
					k++;
					errTxt += "OEM供应商编码不可为空！";
				}
				Cell c1 = row.getCell(1); // OEM供应商名称
				if (c1 != null && !StringUtils.isEmpty(c1.getStringCellValue())) {
					String oemName = c1.getStringCellValue();
					ops.setOemSuppName(oemName);
				} else {
					k++;
					errTxt += "OEM供应商名称不可为空！";
				}
				Cell c2 = row.getCell(2); // 包材供应商编码
				if (c2 != null && !StringUtils.isEmpty(c2.getStringCellValue())) {
					String packCode = c2.getStringCellValue();
					ops.setPackSuppCode(packCode);
				} else {
					k++;
					errTxt += "包材供应商编码不可为空！";
				}
				Cell c3 = row.getCell(3); // 包材供应商名称
				if (c3 != null && !StringUtils.isEmpty(c3.getStringCellValue())) {
					String packName = c3.getStringCellValue();
					ops.setPackSuppName(packName);
				} else {
					k++;
					errTxt += "包材供应商名称不可为空！";
				}
				Cell c4 = row.getCell(4); // 物料号
				if (c4 != null && !StringUtils.isEmpty(c4.getStringCellValue())) {
					String mateCode = c4.getStringCellValue();
					ops.setMateCode(mateCode);
				} else {
					k++;
					errTxt += "物料号不可为空！";
				}
				Cell c5 = row.getCell(5); // 物料名称
				if (c5 != null && !StringUtils.isEmpty(c5.getStringCellValue())) {
					String mateName = c5.getStringCellValue();
					ops.setMateName(mateName);
				} else {
					k++;
					errTxt += "物料名称不可为空！";
				}
				if (k == 6) {
					continue;
				} else {
					if ("".equals(errTxt)) {
						String mate = ops.getMateCode() + "-" + ops.getMateName();
						String oem = ops.getOemSuppCode() + "-" + ops.getOemSuppName();
						String pack = ops.getPackSuppCode() + "-" + ops.getPackSuppName();
						if (ops.getOemSuppCode().equals(ops.getPackSuppCode())) {
							errTxt += "OEM供应商编码和包材供应商编码不可为同一编码(" + ops.getOemSuppCode() + ")";
						} else if (!codes.contains(mate)) {
							errTxt += "此物料不为您管理的半成品物料(" + ops.getMateCode() + ")";
						} else if (!suppConcatCol.contains(oem)) {
							errTxt += "此OEM供应商不在系统中(" + ops.getOemSuppCode() + ")";
						} else if (!suppConcatCol.contains(pack)) {
							errTxt += "此包材供应商不在系统中(" + ops.getPackSuppCode() + ")";
						} else {
							list.add(ops);
						}
					} else {
						errTxt = r + 1 + ": " + errTxt;
						return errTxt;
					}
				}
			}

			if (list.size() > 0) {
				// 对list进行排序
				Collections.sort(list, new Comparator<OemPackSupp>() {
					@Override
					public int compare(OemPackSupp o1, OemPackSupp o2) {
						String osc1 = o1.getOemSuppCode() + "-" + o1.getPackSuppCode();
						String osc2 = o2.getOemSuppCode() + "-" + o2.getPackSuppCode();
						int i = osc1.compareTo(osc2);
						if (i > 0) {
							return 1;
						} else if (i < 0) {
							return -1;
						}
						return 0;
					}
				});
				List<OemPackSupp> addlist = new ArrayList<>();
				String flag = "";
				for (OemPackSupp oemPackSupp : list) {
					String uuid = UUIDUtil.getUUID();
					oemPackSupp.setId(uuid);
					String oem_pack = oemPackSupp.getOemSuppCode() + "-" + oemPackSupp.getPackSuppCode();
					if ("".equals(flag)) {
						addlist.add(oemPackSupp);
					} else {
						if (oem_pack.equals(flag)) {
							addlist.add(oemPackSupp);
						} else {
							if (addlist.size() > 0) {
								OemPackSupp ops = addlist.get(0);
								configMapper.deleteOemPackSuppByOPS(ops);
								configMapper.batchSaveOemPackSupp(addlist);
							}
							flag = oem_pack;
							addlist = new ArrayList<>();
						}
					}
				}
				if (addlist.size() > 0) {
					OemPackSupp ops = addlist.get(0);
					configMapper.deleteOemPackSuppByOPS(ops);
					configMapper.batchSaveOemPackSupp(addlist);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "导入成功";
	}

	@Override
	public int removeOemPackSupp(List<OemPackSupp> list) {

		return configMapper.removeOemPackSupp(list);
	}

}
