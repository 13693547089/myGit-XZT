package com.faujor.web.privileges;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.AssignDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.RoleDO;
import com.faujor.service.privileges.AssignService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@RequestMapping("/sys/assign")
@Controller
public class AssignController {
	@Autowired
	private AssignService assignService;
	@Autowired
	private OrgService orgService;

	@RequestMapping("/role")
	public String assignByRole() {
		return "privileges/assign/assignByRole";
	}

	/**
	 * 角色列表
	 * 
	 * @param role
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/roleList")
	@ResponseBody
	public Map<String, Object> roleList(RoleDO role, int page, int limit) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		String roleName = role.getRoleName();
		if (!StringUtils.isEmpty(roleName)) {
			roleName = "%" + roleName + "%";
			role.setRoleName(roleName);
		}
		return assignService.roleList(role, rb);
	}

	/**
	 * 按角色分配的组织机构列表
	 * 
	 * @param role
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/assignListByRoleId")
	@ResponseBody
	public Map<String, Object> assignListByRoleId(RoleDO role, int page, int limit) {
		String roleName = role.getRoleName();
		if (!StringUtils.isEmpty(roleName)) {
			roleName = "%" + roleName + "%";
			role.setRoleName(roleName);
		}
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return assignService.assignListByRoleId(rb, role);
	}

	/**
	 * 根据角色id更新
	 * 
	 * @param assign
	 * @return
	 */
	@PostMapping("/updateByRoleId")
	@ResponseBody
	public RestCode updateByRoleId(AssignDO assign) {
		int i = assignService.updateByRoleId(assign);
		if (i > 0) {
			return RestCode.ok();
		} else if (i == -1) {
			return RestCode.noUpdate();
		} else {
			return RestCode.error();
		}
	}

	/**
	 * 按照组织机构id删除
	 * 
	 * @param orgIds
	 * @param roleId
	 * @return
	 */
	@PostMapping("/removeByOrgIds")
	@ResponseBody
	public RestCode removeByOrgIds(String orgIds, long roleId) {
		List<Long> orgId = JsonUtils.jsonToList(orgIds, long.class);
		int i = assignService.removeByOrgIds(orgId, roleId);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 按组织机构分配的组织机构列表
	 * 
	 * @return
	 */
	@RequestMapping("/org")
	public String assignByOrg() {
		return "privileges/assign/assignByOrg";
	}

	@RequestMapping("/orgList")
	@ResponseBody
	public List<LayuiTree<OrgDo>> orgList(String orgName) {
		List<LayuiTree<OrgDo>> list = new ArrayList<LayuiTree<OrgDo>>();
		LayuiTree<OrgDo> tree = assignService.orgList(orgName);
		list.add(tree);
		return list;
	}

	/**
	 * 按组织机构分配的角色列表
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping("/assignListByOrgId")
	@ResponseBody
	public Map<String, Object> assignListByOrgId(String orgId) {
		return assignService.assignListByOrgId(orgId);
	}

	/**
	 * 选择组织机构
	 * 
	 * @param model
	 * @param roleId
	 * @return
	 */
	@GetMapping("/orgSelect")
	public String orgSelect(Model model, String roleId) {
		model.addAttribute("roleId", roleId);
		return "privileges/assign/orgSelect";
	}

	@GetMapping("/select")
	@ResponseBody
	Tree<OrgDo> tree(String roleId) {
		Tree<OrgDo> tree = new Tree<OrgDo>();
		tree = orgService.getOrgSelectTree(roleId);
		return tree;
	}

	@GetMapping("/roleSelect")
	public String roleSelect(Model model, long orgId) {
		model.addAttribute("orgId", orgId);
		return "privileges/assign/roleSelect";
	}

	@GetMapping("/selected")
	@ResponseBody
	public List<JSONObject> roleSelected(long orgId) {
		List<JSONObject> result = assignService.roleSelected(orgId);
		return result;
	}

	/**
	 * 按组织机构更新
	 * 
	 * @param orgId
	 * @param roleIds
	 * @return
	 */
	@PostMapping("/updateByOrgId")
	@ResponseBody
	public RestCode updateByOrgId(long orgId, String roleIds) {
		int i = assignService.updateByOrgId(orgId, roleIds);
		if (i > 0) {
			return RestCode.ok();
		} else if (i == -1) {
			return RestCode.noUpdate();
		} else {
			return RestCode.error();
		}
	}

	/**
	 * 按组织机构删除
	 * 
	 * @param roleIds
	 * @param orgId
	 * @return
	 */
	@RequestMapping("/removeByRoleIds")
	@ResponseBody
	public RestCode removeByRoleIds(String roleIds, long orgId) {
		List<Long> roleId = JsonUtils.jsonToList(roleIds, long.class);
		int i = assignService.removeByRoleIds(roleId, orgId);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
