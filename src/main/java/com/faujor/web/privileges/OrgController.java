package com.faujor.web.privileges;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.privileges.SysUserService;
import com.faujor.utils.RestCode;

@RequestMapping("/sys/org")
@Controller
public class OrgController {
	@Autowired
	private OrgService orgService;
	@Autowired
	private SysUserService userService;

	@GetMapping()
	String org() {
		return "privileges/org/org";
	}

	@GetMapping("/list")
	@ResponseBody
	public List<OrgDo> orgList() {
		List<OrgDo> org = orgService.orgList();
		return org;
	}

	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId") long pId) {
		model.addAttribute("pId", pId);
		long userId = -1;
		if (pId == 0) {
			model.addAttribute("pName", "");
			model.addAttribute("pType", "");
		} else {
			OrgDo org = orgService.getOrgById(pId);
			String name = org.getSname();
			userId = org.getSpersonId();
			model.addAttribute("pName", name);
			model.addAttribute("pType", org.getStype());
			model.addAttribute("org", new OrgDo());
		}
		List<UserDO> users = userService.listNotSelf(userId);
		model.addAttribute("users", users);
		return "privileges/org/add";
	}

	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") long id) {
		// List<RoleDO> roles = roleService.list();
		// model.addAttribute("roles", roles);
		OrgDo org = orgService.getOrgById(id);
		long pid = org.getParentId();
		if (pid != 0) {
			OrgDo orgDo = orgService.getOrgById(pid);
			model.addAttribute("upName", orgDo.getSname());
		}
		if (org.getStype().equals("psn")) {
			List<UserDO> users = userService.listNotSelf(org.getSpersonId());
			model.addAttribute("users", users);
			SysUserDO userDO = userService.get(org.getSpersonId());
			model.addAttribute("user", userDO);
		}
		model.addAttribute("org", org);
		return "privileges/org/edit";
	}

	/**
	 * 保存组织机构数据
	 * 
	 * @param org
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	RestCode save(OrgDo org, SysUserDO user) {
		if (orgService.save(org, user) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "保存失败");
		}
	}

	@PostMapping("/orgUpdate")
	@ResponseBody
	RestCode update(OrgDo org, SysUserDO user) {
		if (orgService.update(org, user) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "保存失败");
		}
	}

	@PostMapping("/remove")
	@ResponseBody
	RestCode remove(long id) {
		if (orgService.remove(id) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "删除失败");
		}
	}

	@GetMapping("/checkCode")
	@ResponseBody
	RestCode checkCode(String scode, String sid) {
		int i = orgService.checkCode(scode, sid);
		if (i == 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
