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

import com.faujor.entity.privileges.RoleDO;
import com.faujor.service.privileges.RoleService;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/sys/role")
public class RoleController {
	@Autowired
	RoleService roleService;
	
	@GetMapping()
	String role() {
		return "privileges/role/role";
	}
	
	@GetMapping("/list")
	@ResponseBody()
	List<RoleDO> list() {
		List<RoleDO> roles = roleService.list();
		return roles;
	}
	
	@GetMapping("/add")
	String add() {
		return "privileges/role/add";
	}
	
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		RoleDO roleDO = roleService.get(id);
		model.addAttribute("role", roleDO);
		return  "privileges/role/edit";
	}
	
	@PostMapping("/save")
	@ResponseBody()
	RestCode save(RoleDO role) {
		if (roleService.save(role) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "保存失败");
		}
	}

	@PostMapping("/update")
	@ResponseBody()
	RestCode update(RoleDO role) {
		if (roleService.update(role) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "保存失败");
		}
	}
	
	@PostMapping("/remove")
	@ResponseBody()
	RestCode save(Long id) {
		if (roleService.remove(id) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "删除失败");
		}
	}
}
