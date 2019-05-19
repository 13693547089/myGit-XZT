package com.faujor.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
@Component
public class PrivilegesCommon {
	@Autowired
	private QualSuppMapper qualSuppMapper;

	public  List<String> getAllSupplierCode() {
		List<String> list = new ArrayList<String>();
		// 获取当前登录人信息
		SysUserDO user = UserCommon.getUser();
		String type = user.getUserType();
		// 判断用户的类型，是供应商还是内部用户
		if ("supplier".equals(type)) {
			// 供应商
			list.add(user.getSuppNo());
		} else if ("user".equals(type)) {
			// 内部用户
			List<UserDO> userList = UserCommon.findUserList();
			 List<UserDO> childUser = new ArrayList<UserDO>();
			List<UserDO> child = managePubUsers(userList, user.getUserId(),childUser);
			UserDO userDo=new UserDO();
			userDo.setId(user.getUserId());
			child.add(userDo);
			list = qualSuppMapper.findSuppCodesByUsers(child);
		}
		if(list==null || list.size()==0){
			return null;
		}
		return list;
	}

	public  List<String> getAllSubordinateCode() {
		List<String> list = new ArrayList<String>();
		// 获取当前登录人信息
		SysUserDO user = UserCommon.getUser();
		String type = user.getUserType();
		// 判断用户的类型，是供应商还是内部用户
		if ("supplier".equals(type)) {
			// 供应商
			list.add(user.getUsername());
		} else if ("user".equals(type)) {
			// 内部用户
			List<UserDO> userList = UserCommon.findUserList();
			list.add(user.getUsername());
			manageUsers(userList, user.getUserId(),list);
		}
		if(list==null || list.size()==0){
			return null;
		}
		return list;
	}
	
	
	public void  manageUsers(List<UserDO> users, long leader,List<String> childUser) {
		for (UserDO u : users) {
			if ( u.getLeader() == leader) {
				// 遍历下级节点
				childUser.add(u.getUserName());
				manageUsers(users, u.getId(),childUser);
			}
		}
	}
	// 遍历用户表获取所管理的下级组织
	public  List<UserDO> managePubUsers(List<UserDO> users, long leader,List<UserDO> childUser) {
		for (UserDO u : users) {
			if ( u.getLeader() == leader) {
				// 遍历下级节点
				childUser.add(u);
				managePubUsers(users, u.getId(),childUser);
			}
		}
		return childUser;
	}
}
