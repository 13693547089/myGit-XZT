package com.faujor.service.basic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.basic.BasicMapper;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.basic.DicCategory;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.basic.BasicService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "basicService")
public class BasicServiceImpl implements BasicService {
	@Autowired
	private BasicMapper basicMapper;

	@Override
	public Map<String, Object> findDicCategoryByParams_1(Map<String, Object> params) {
		List<DicCategory> dicCategory = basicMapper.findDicCategoryByParams(params);
		int count = basicMapper.countDicCategoryByParams(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", dicCategory);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	@Override
	public List<Dic> findDicByParams_1(Map<String, String> params) {
		String cateId = params.get("cateId");

		List<Dic> dics = new ArrayList<>();
		if (!StringUtils.isEmpty(cateId)) {
			Dic d = new Dic();
			d.setCateId(cateId);
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("dic", d);
			dics = basicMapper.findDicByParams(p);
//			basicMapper.countDicByParams(p);
		}
		return dics;
	}

	@Override
	public DicCategory findDicCategoryById(String id) {
		DicCategory category = basicMapper.findDicCategoryById(id);
		return category;
	}

	@Override
	public Map<String, Object> findDicCategoryByParams(Map<String, Object> params) {
		List<DicCategory> dicCategory = basicMapper.findDicCategoryByParams(params);
		int count = basicMapper.countDicCategoryByParams(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", dicCategory);
		result.put("rows", dicCategory);
		result.put("count", count);
		result.put("total", count);
		result.put("code", "0");
		result.put("msg", "");
		return result;
	}

	@Override
	public Map<String, Object> findDicByParams(Map<String, String> params) {
		String cateId = params.get("cateId");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", "");
		result.put("total", "");
		if (!StringUtils.isEmpty(cateId)) {
			Dic d = new Dic();
			d.setCateId(cateId);
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("dic", d);
			List<Dic> dic = basicMapper.findDicByParams(p);
			int count = basicMapper.countDicByParams(p);
			result.put("rows", dic);
			result.put("total", count);
		}
		return result;
	}

	@Override
	public int findDicCategoryByIdANDCode(Map<String, String> param) {
		return basicMapper.findDicCategoryByIdANDCode(param);
	}

	@Override
	public int saveDicInfo(DicCategory category) {
		String cateId = category.getId();
		SysUserDO user = UserCommon.getUser();
		// 字典list
		List<Dic> list = category.getDicList();
		if (StringUtils.isEmpty(cateId)) {
			// 新增
			// 补充完善表单信息
			String id = UUID.randomUUID().toString().replace("-", "");
			category.setId(id);
			category.setCreateTime(new Date());
			long l = 1;
			category.setIsUsed(l);// 1启用，0是禁用
			category.setCreator(user.getUserId().toString());
			category.setCreatorName(user.getUsername());
			// 字典信息
			for (Dic dic : list) {
				String dicId = UUIDUtil.getUUID();
				dic.setId(dicId);
				dic.setCateId(id);
				dic.setCreator(user.getUserId().toString());
				dic.setCreatorName(user.getUsername());
				dic.setCreateTime(new Date());
				basicMapper.saveDicInfo(dic);
			}
			return basicMapper.saveCategoryInfo(category);
		} else {
			// 更新
			// 更新分类信息
			category.setModifier(user.getUserId().toString());
			category.setModifyTime(new Date());
			category.setModifierName(user.getUsername());

			// 已经添加的字典信息列表
			Map<String, Object> params = new HashMap<String, Object>();
			Dic d = new Dic();
			d.setCateId(cateId);
			params.put("dic", d);
			List<Dic> oldDicList = basicMapper.findDicByParams(params);
			List<String> dicIds = new ArrayList<String>();
			// for (Dic dic : list) {
			// dicIds.add(dic.getId());
			// }
			// // 删除字典信息
			// for (Dic dic : oldDicList) {
			// if (!dicIds.contains(dic.getId())) {
			// basicMapper.deleteDicInfoByID(dic.getId());
			// }
			// }
			for (Dic oldDic : oldDicList) {
				dicIds.add(oldDic.getId());
			}
			for (Dic dic : list) {
				String id = dic.getId();
				if (dicIds.contains(id)) {
					dic.setModifyTime(new Date());
					dic.setModifier(user.getUserId().toString());
					dic.setModifierName(user.getUsername());
					basicMapper.updateDicInfo(dic);
					dicIds.remove(id);
				} else {
					// 新增的字典
					dic.setId(UUIDUtil.getUUID());
					dic.setCateId(cateId);
					dic.setCreateTime(new Date());
					dic.setCreator(user.getUserId().toString());
					dic.setCreatorName(user.getUsername());
					basicMapper.saveDicInfo(dic);
				}
			}
			if (dicIds.size() > 0) {
				for (String id : dicIds) {
					basicMapper.deleteDicInfoByID(id);
				}
			}
			// 字典信息
			// for (Dic dic : list) {
			// if (dic.getId() == null) {
			// // 新增的字典
			// dic.setId(UUIDUtil.getUUID());
			// dic.setCateId(cateId);
			// dic.setCreateTime(new Date());
			// dic.setCreator(user.getUserId().toString());
			// dic.setCreatorName(user.getUsername());
			// basicMapper.saveDicInfo(dic);
			// } else {
			// dic.setModifyTime(new Date());
			// dic.setModifier(user.getUserId().toString());
			// dic.setModifierName(user.getUsername());
			// basicMapper.updateDicInfo(dic);
			// }
			// }
			basicMapper.updateCategoryInfo(category);
			return 100;
		}
	}

	@Override
	public int deleteCategoryInfo(String selected) {
		List<DicCategory> list = JsonUtils.jsonToList(selected, DicCategory.class);
		int i = 100;
		for (DicCategory cate : list) {

			String cateId = cate.getId();
			if (!StringUtils.isEmpty(cateId)) {
				// 级联删除字典信息
				i = basicMapper.deleteDicInfoByCateId(cateId);
				// 删除分类表
				i = basicMapper.deleteDicCategoryByID(cate.getId().toString());
			}

		}
		return i;
	}

	@Override
	public List<Dic> findDicListByCategoryCode(String cateCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cateCode", cateCode);
		List<Dic> list = basicMapper.findDicByCodeParams(params);
		return list;
	}

	@Override
	public List<Dic> findDicListByCategoryCode(Map<String, Object> map) {
		List<Dic> list = basicMapper.findDicByCodeParams(map);
		return list;
	}

	@Override
	public Dic findDicByDicCodeANDCategoryCode(String dicCode, String cateCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cateCode", cateCode);
		params.put("dicCode", dicCode);
		List<Dic> list = basicMapper.findDicByCodeParams(params);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<Dic> findDicListByCateId(String cateId) {
		DicCategory cate = new DicCategory();
		cate.setId(cateId);
		return basicMapper.findDicListByCategory(cate);
	}

	@Override
	public List<Dic> findDicListByParentId(String pid) {
		return basicMapper.findDicListByParentId(pid);
	}

	@Override
	public Dic findDicInfoById(String id) {

		return basicMapper.findDicById(id);
	}

	@Override
	@Transactional
	public String saveDicAndCategoryInfo(DicCategory cate, Dic dic) {
		DicCategory category = basicMapper.findDicCategoryById(cate.getId());
		SysUserDO user = UserCommon.getUser();
		if (category != null) {
			cate.setModifier(user.getUserId().toString());
			cate.setModifierName(user.getName());
			cate.setModifyTime(new Date());
			basicMapper.updateCategoryInfo(cate);
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("code", cate.getCateCode());
			param.put("id", cate.getId());
			int i = basicMapper.findDicCategoryByIdANDCode(param);
			if (i != 0) {
				return "分类编码重复!";
			}
			String uuid = UUIDUtil.getUUID();
			cate.setId(uuid);
			cate.setCreateTime(new Date());
			cate.setCreator(user.getUserId().toString());
			cate.setCreatorName(user.getName());
			basicMapper.saveCategoryInfo(cate);
		}

		Dic dic2 = basicMapper.findDicById(dic.getId());
		if (dic2 != null) {
			dic.setModifier(user.getUserId().toString());
			dic.setModifierName(user.getName());
			dic.setModifyTime(new Date());
			dic.setCateId(cate.getId());
			basicMapper.updateDicInfo(dic);
		} else {
			dic.setCateId(cate.getId());
			dic.setCreateTime(new Date());
			dic.setCreator(user.getUserId().toString());
			dic.setCreatorName(user.getName());
			basicMapper.saveDicInfo(dic);
		}
		return "保存成功";
	}

	@Override
	public String deleteDicInfo(String id) {
		int i = basicMapper.deleteDicInfoByID(id);
		if (i > 0)
			return "删除成功!";
		return "删除失败!";
	}

	@Override
	@Transactional
	public String saveNormalDicInfo(DicCategory category) {
		String id = category.getId();
		DicCategory dicCate = basicMapper.findDicCategoryById(id);
		// 校验分类编码不重复
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", category.getCateCode());
		param.put("id", id);
		int i = basicMapper.findDicCategoryByIdANDCode(param);
		if (i != 0) {
			return "分类编码重复!";
		}
		SysUserDO user = UserCommon.getUser();
		Date date = new Date();
		String userId = user.getUserId().toString();
		String name = user.getName();
		if (dicCate != null) {
			// 更新
			category.setModifier(userId);
			category.setModifierName(name);
			category.setModifyTime(date);
			basicMapper.updateCategoryInfo(category);
			basicMapper.deleteDicInfoByCateId(id);
		} else {
			category.setCreateTime(date);
			category.setCreator(userId);
			category.setCreatorName(name);
			basicMapper.saveCategoryInfo(category);
		}
		List<Dic> dicList = category.getDicList();
		if (dicList != null && dicList.size() > 0)
			basicMapper.batchSaveDicInfo(dicList);
		return "保存成功！";
	}

}
