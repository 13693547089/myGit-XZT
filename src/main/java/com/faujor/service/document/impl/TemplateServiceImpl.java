package com.faujor.service.document.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.common.ftp.FtpUtil;
import com.faujor.dao.master.common.MenuMapper;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.dao.master.document.TempLogMapper;
import com.faujor.dao.master.document.TempMenuMapper;
import com.faujor.dao.master.document.TemplateMapper;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.entity.document.TempMenu;
import com.faujor.entity.document.Template;
import com.faujor.service.document.TemplateService;
import com.faujor.utils.BuildTree;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
@Service
public class TemplateServiceImpl implements TemplateService {
	@Autowired
	private TemplateMapper templateMapper;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private TempMenuMapper tempMenuMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private TempLogMapper tempLogMapper;
	@Override
	public LayuiPage<Template> getTempByPage(Map<String, Object> map) {
		LayuiPage<Template> page= new LayuiPage<Template>();
		Integer count = templateMapper.getTempNum(map);
		List<Template> data = templateMapper.getTempByPage(map);
		page.setData(data);
		page.setCount(count);
		return page;
	}
	@Override
	public Template getTemp(Map<String, Object> map) {
		return templateMapper.getTemp(map);
	}
	
	@Override
	@Transactional
	public void saveTemp(Template temp, List<Document> docs, List<TempMenu> tempMenus) {
		SysUserDO user = UserCommon.getUser();
		String uuid = UUIDUtil.getUUID();
		temp.setId(uuid);
		temp.setAttachNum(docs.size());
		temp.setCreateTime(new Date());
		temp.setCreateUser(user.getUsername());
		temp.setCreater(user.getName());
		//保存模板信息
		templateMapper.saveTemp(temp);
		//删除模板关联页面
		Map<String, Object> params=new HashMap<String, Object>();
		String tempNo = temp.getTempNo();
		params.put("tempNo", tempNo);
		tempMenuMapper.deleteTempMenuByTempNo(params);
		for (TempMenu tempMenu : tempMenus) {
			tempMenu.setTempNo(tempNo);
			tempMenu.setId(UUIDUtil.getUUID());
			tempMenuMapper.saveTempMenu(tempMenu);
		}
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("doc_template");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		log.setOperation("创建");
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(tempNo);
		tempLogMapper.saveTempLog(log);
	}
	public Tree<MenuDO> getTree(String tempNo) {
		// 根据roleId查询权限
		List<MenuDO> menus = tempMenuMapper.listMenuByTempNo(tempNo);
		List<Long> menuIds = new ArrayList<Long>();
		for (MenuDO menu : menus) {
			menuIds.add(menu.getMenuId());
		}
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.listMenu();
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> state = new HashMap<String, Object>();
			Long menuId = sysMenuDO.getMenuId();
			if (menuIds.contains(menuId)) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}
	@Override
	public List<MenuDO> getMenuList(String tempNo) {
		List<MenuDO> menus = tempMenuMapper.listMenuByTempNo(tempNo);
		return menus;
	}
	@Override
	public List<MenuDO> getCkeckedMenus(List<Long> menuIds) {
		List<MenuDO> menus=new ArrayList<MenuDO>();
		for (Long menuId : menuIds) {
			MenuDO menu = menuMapper.getMenu(menuId);
			if(menu!=null){
				menus.add(menu);
			}
		}
		return menus;
	}
	@Override
	public void updateTemp(Template temp, List<Document> docs, List<TempMenu> tempMenus) {
		SysUserDO user = UserCommon.getUser();
		temp.setAttachNum(docs.size());
		temp.setModifyTime(new Date());
		temp.setModifyUser(user.getUsername());
		temp.setModifier(user.getName());
		//保存模板信息
		templateMapper.updateTemp(temp);
		//删除模板关联页面
		Map<String, Object> params=new HashMap<String, Object>();
		String tempNo = temp.getTempNo();
		params.put("tempNo", tempNo);
		tempMenuMapper.deleteTempMenuByTempNo(params);
		for (TempMenu tempMenu : tempMenus) {
			tempMenu.setTempNo(tempNo);
			tempMenu.setId(UUIDUtil.getUUID());
			tempMenuMapper.saveTempMenu(tempMenu);
		}
		//更新附件信息
		for (Document doc : docs) {
			doc.setDocCate("doc_template");
			doc.setLinkNo(tempNo);
			documentMapper.updateDocLink(doc);
		}
		//模板更新日志
		TempLog log=new TempLog();
		log.setId(UUIDUtil.getUUID());
		log.setOperation("修改");
		log.setOperator(user.getUsername());
		log.setOperateTime(new Date());
		log.setTempNo(tempNo);
		tempLogMapper.saveTempLog(log);
	}
	@Override
	public List<TempLog> getTempLogByTempNo(String tempNo) {
		List<TempLog> logs = tempLogMapper.getTempLogByTempNo(tempNo);
		return logs;
	}
	@Override
	public RestCode deleteFile(List<String> docIds) {
		Map<String, Object> params=new HashMap<String,Object>();
		boolean flag =true;
		for (String docId : docIds) {
			params.put("docId", docId);
			Document doc = documentMapper.getDoc(params);
			boolean deleteFile = FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
			if(deleteFile){
				documentMapper.deleteDoc(docId);
			}else{
				flag=false;
			}
		}
		if(!flag){
			return RestCode.error("删除失败！");
		}
		return new RestCode();
	}
	@Override
	@Transactional
	public void deleteTemp(List<String> tempNos) {
		Map<String, Object> map=new HashMap<String ,Object>();
		map.put("docCate", "doc_template");
		for (String tempNo : tempNos) {
			//删除模板主数据
			templateMapper.deleteTempByNo(tempNo);
			//删除附件列表
		
			map.put("linkNo", tempNo);
			List<Document> docs = documentMapper.getDocByLinkNo(map);
			for (Document doc : docs) {
				FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
			}
			documentMapper.deleteDocByLinkNo(map);
			//删除关联页面
			map.put("tempNo", tempNo);
			tempMenuMapper.deleteTempMenuByTempNo(map);
			//删除日志
			tempLogMapper.delLogByTempNo(tempNo);
		}

	}
	@Override
	public List<Document> getDocByMenuId(Long menuId) {
		List<Document> list = documentMapper.getDocByMenuId(menuId);
		return list;
	}
}
