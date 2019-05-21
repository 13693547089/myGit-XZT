package com.faujor.web.privileges;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.RoleDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.privileges.RoleService;
import com.faujor.service.privileges.SysUserService;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@RequestMapping("/sys/user")
@Controller
public class UserController {
	@Autowired
	SysUserService userService;

	@Autowired
	RoleService roleService;

	@GetMapping("")
	String user(Model model) {
		return "privileges/user/user";
	}

	// @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
	@GetMapping("/list")
	@ResponseBody
	Map<String, Object> list(String queryParams, String page, String limit) {
		int p = page != null ? Integer.parseInt(page) : 1;
		int l = Integer.parseInt(limit);
		int offset = (p - 1) * l;
		RowBounds rb = new RowBounds(offset, l);
		// 查询列表数据
		// Query query = new Query(params);
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("params", queryParams);
		List<SysUserDO> sysUserList = userService.list(rb, query);
		int total = userService.count(query);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", sysUserList);
		map.put("count", total);
		map.put("code", "0");
		map.put("msg", "");
		// PageUtils pageUtil = new PageUtils(sysUserList, total);
		return map;
	}

	@GetMapping("/add")
	String add(Model model) {
		List<RoleDO> roles = roleService.list();
		model.addAttribute("roles", roles);
		return "privileges/user/add";
	}

	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		SysUserDO userDO = userService.get(id);
		model.addAttribute("user", userDO);
		List<RoleDO> roles = roleService.list(id);
		model.addAttribute("roles", roles);
		List<UserDO> users = userService.listNotSelf(id);
		model.addAttribute("users", users);
		return "privileges/user/edit";
	}

	@PostMapping("/save")
	@ResponseBody
	RestCode save(SysUserDO user) {
		user.setPassword(user.getPassword());
		if (userService.save(user) > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	@PostMapping("/update")
	@ResponseBody
	RestCode update(SysUserDO user) {
		if (userService.update(user) > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	@PostMapping("/remove")
	@ResponseBody
	RestCode remove(Long id) {
		if (userService.remove(id) > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	@PostMapping("/batchRemove")
	@ResponseBody
	RestCode batchRemove(@RequestParam("ids[]") Long[] userIds) {
		List<Long> Ids = Arrays.asList(userIds);
		int r = userService.batchremove(Ids);
		if (r > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// Query query = new Query(params);
		return !userService.exit(params);// 存在，不通过，false
	}

	@GetMapping("/resetPwd/{id}")
	String resetPwd(@PathVariable("id") Long userId, Model model) {

		SysUserDO userDO = new SysUserDO();
		userDO.setUserId(userId);
		model.addAttribute("user", userDO);
		return "privileges/user/reset_pwd";
	}

	@PostMapping("/resetPwd")
	@ResponseBody
	RestCode resetPwd(SysUserDO user) {
		user.setPassword(user.getPassword());
		if (userService.resetPwd(user) > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	/**
	 * 重置密码
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/reset")
	@ResponseBody
	RestCode reset(SysUserDO user) {
		int i = userService.resetPwd(user);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 初始化密码
	 * 
	 * @return
	 */
	@PostMapping("/encrypt")
	@ResponseBody
	RestCode encrypt() {
		List<SysUserDO> list = userService.findUsers();
		for (SysUserDO user : list) {
			userService.update(user);
		}
		return RestCode.error();
	}
	
	/**
	 * 初始化密码
	 * 
	 * @return
	 */
	@GetMapping("/getUserMessage")
	@ResponseBody
	RestCode getUserMessage() {
		//获取当前登录人信息
		SysUserDO user = UserCommon.getUser();
		Map<String,Object> map = new HashMap<>();
		map.put("user", user);
		return RestCode.ok(map);
	}
}
