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

import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.Tree;
import com.faujor.service.privileges.MenuService;
import com.faujor.utils.RestCode;

@RequestMapping("/sys/menu")
@Controller
public class MenuController{
	@Autowired
	MenuService menuService;
	
	@GetMapping()
	String menu(Model model) {
		return "privileges/menu/menu";
	}
	@RequestMapping("/list")
	@ResponseBody
	List<MenuDO> list() {
		List<MenuDO> menus = menuService.list();
		//SysUserDO user=(SysUserDO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//获取当前用户的user对象
		return menus;
	}
	
	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId") Long pId) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(pId).getName());
		}
		return "privileges/menu/add";
	}
	
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		model.addAttribute("menu", menuService.get(id));
		return "privileges/menu/edit";
	}
	
	@PostMapping("/remove")
	@ResponseBody
	RestCode remove(Long id) {
		
		if (menuService.remove(id) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "删除失败");
		}
	}
	
	@PostMapping("/save")
	@ResponseBody
	RestCode save(MenuDO menu) {
		if (menuService.save(menu) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "保存失败");
		}
	}
	@PostMapping("/update")
	@ResponseBody
	RestCode update(MenuDO menu) {
		if (menuService.update(menu) > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error(1, "更新失败");
		}
	}
	
	@GetMapping("/tree")
	@ResponseBody
	Tree<MenuDO> tree() {
		Tree<MenuDO> tree = new Tree<MenuDO>();
		tree = menuService.getTree();
		return tree;
	}

	@GetMapping("/tree/{roleId}")
	@ResponseBody
	Tree<MenuDO> tree(@PathVariable("roleId") Long roleId) {
		Tree<MenuDO> tree = new Tree<MenuDO>();
		tree = menuService.getTree(roleId);
		return tree;
	}
}

