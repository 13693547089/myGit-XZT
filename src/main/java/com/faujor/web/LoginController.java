package com.faujor.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.faujor.common.annotation.Log;
import com.faujor.core.plugins.redis.RedisClient;
import com.faujor.entity.common.LarryTree;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.privileges.MenuService;
import com.faujor.service.privileges.SysUserService;
import com.faujor.utils.HttpClientUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.MD5Utils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
public class LoginController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private RedisClient redisClient;

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/main")
	public String main() {
		return "main";
	}

	@Log(value = "登录系统")
	@GetMapping("/srm")
	public String index(Model model, HttpServletRequest request) {
		// int i = request.getSession().getMaxInactiveInterval();
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);
		HttpSession session = request.getSession();
		String id = session.getId();
		redisClient.set(id, user.getUsername());
		return "home";
	}

	@Log(value = "登录系统")
	@GetMapping("/")
	public String index1(Model model, HttpServletRequest request) {
		// int i = request.getSession().getMaxInactiveInterval();
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);
		HttpSession session = request.getSession();
		String id = session.getId();
		redisClient.set(id, user.getUsername());
		return "home";
	}

	@ResponseBody
	@RequestMapping("/getLarryMenuTree")
	public RestCode getLarryMenuTree(Model model) {
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		if ("supplier".equals(user.getUserType()))
			userId = user.getOrgSpersonId();
		List<LarryTree<MenuDO>> menus = menuService.listMenuTree(userId);
		return new RestCode().put("data", menus);
	}

	/**
	 * 注销的时候，清除redis中的sessionId
	 * 
	 * @param request
	 * @return
	 */
	@Log(value = "注销系统")
	@RequestMapping("/redisLogout")
	@ResponseBody
	public RestCode redisLogout(HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		redisClient.del(sessionId);
		return RestCode.ok();
	}

	@GetMapping("/otherpage")
	public String home(Model model) {
		return "otherpage";
	}

	@GetMapping("/account")
	public String account(Model model) {
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);
		return "home/account";
	}

	// 验证用户的有效性
	@RequestMapping("/CheckAccess")
	@ResponseBody
	public String checkUserInfo(HttpServletRequest request, @RequestParam("LoginName") String LoginName,
			@RequestParam("Pwd") String Pwd) {
		SysUserDO sysUserDO = new SysUserDO();
		sysUserDO.setUsername(LoginName);
		List<SysUserDO> list = userService.findUsersByParams(sysUserDO);
		String password = MD5Utils.encrypt(Pwd);
		String code = "1", msg = "用户名不存在";
		JSONObject jb = new JSONObject();
		if (list != null && list.size() != 0) {
			int size = list.size();
			if (size == 1) {
				SysUserDO user = list.get(0);
				String password2 = user.getPassword();
				if (password2.equals(password)) {
					code = "0";
					msg = "验证通过";
				} else {
					code = "2";
					msg = "密码错误";
				}
			} else {
				boolean flag = false;
				for (SysUserDO u : list) {
					String password2 = u.getPassword();
					if (password2.equals(password)) {
						flag = true;
					}
				}
				if (flag) {
					code = "0";
					msg = "验证通过";
				} else {
					code = "2";
					msg = "密码错误";
				}
			}
		}
		jb.put("code", code);
		jb.put("msg", msg);
		return jb.toJSONString();
	}

	@GetMapping("/top_srm")
	public String index22(Model model, HttpServletRequest request, String ticket) {
		HttpSession session = request.getSession();
		String id = session.getId();
		System.out.println("srm1======session_id:" + id);
		// int i = request.getSession().getMaxInactiveInterval();
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("user", user);

		redisClient.set(id, user.getUsername());
		return "home";
	}

	/**
	 * 从portal中访问srm
	 * 
	 * @param Ticket
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/AccessToken")
	@ResponseBody
	public String AccessToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("Ticket") String ticket) {
		String id = request.getSession().getId();
		System.out.println("access_token========session:" + id);
		JSONObject json = new JSONObject();
		json.put("Code", 1);
		json.put("Msg", "http://ejp8i3.natappfree.cc/srm");
		String account = this.getPortalAccount(ticket);
		if (StringUtils.isEmpty(account))
			return "";
		JSONObject jb = JsonUtils.jsonToPojo(account, JSONObject.class);
		Map<String, String> data = (Map<String, String>) jb.get("Data");
		String username = data.get("ExternalUserID");
		SysUserDO user = userService.findUserByUsername(username);
		if (user == null)
			return "";
		String url = "http://yw.faujor.com:9010/top_srm?ticket=" + ticket;
//		String url = "http://herejoin.natapp4.cc/top_srm?ticket=" + ticket;
		return url;
	}

	/**
	 * 从portal中获取用户信息
	 * 
	 * @param ticket
	 * @return
	 */
	private String getPortalAccount(String ticket) {
		String url = "http://sso.service.top-china.cn/API/GetAccount";
		JSONObject jb = new JSONObject();
		jb.put("Ticket", ticket);
		jb.put("AppCode", "SRM");
		jb.put("ClientIP", "http://yw.joruns.com:9010");
		String result = HttpClientUtil.doPostJson(url, jb.toJSONString());
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param id
	 * @param newPwd
	 * @return
	 */
	@RequestMapping("/changePwd")
	@ResponseBody
	public RestCode changePwd(Long id, String newPwd) {
		UserDO user = new UserDO();
		user.setId(id);
		user.setPlainCode(newPwd);
		String encrypt = MD5Utils.encrypt(newPwd);
		user.setPassword(encrypt);
		int i = userService.changePwd(user);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
