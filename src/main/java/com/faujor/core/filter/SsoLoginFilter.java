package com.faujor.core.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.privileges.SysUserService;
import com.faujor.utils.HttpClientUtil;
import com.faujor.utils.JsonUtils;

@WebFilter(filterName = "ssoLoginFilter", urlPatterns = { "/*" })
public class SsoLoginFilter implements Filter {
//	@Autowired
	private SysUserService sysUserService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		ServletContext sc = filterConfig.getServletContext();
//		XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils
//				.getWebApplicationContext(sc);
//		if (cxt != null && cxt.getBean("sysUserService") != null && sysUserService != null) {
//			sysUserService = (SysUserService) cxt.getBean("sysUserService");
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		StringBuffer url = req.getRequestURL();
		String urlStr = url.toString();
		System.out.println(urlStr);
		if (urlStr.contains("/top_srm")) {
			
			String ticket = request.getParameter("ticket");
			String account = this.getPortalAccount(ticket);
			if (!StringUtils.isEmpty(account)) {
				JSONObject jb = JsonUtils.jsonToPojo(account, JSONObject.class);
				Map<String, String> data = (Map<String, String>) jb.get("Data");
				String username = data.get("ExternalUserID");
				ServletContext sc = req.getSession().getServletContext();
				WebApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(sc);
				
//				XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils
//						.getWebApplicationContext(sc);
				if (cxt != null && cxt.getBean("sysUserService") != null && sysUserService == null) {
					sysUserService = (SysUserService) cxt.getBean("sysUserService");
				}

				SysUserDO user = sysUserService.findUserByUsername(username);
				if (user != null) {
					String password = user.getPassword();
					User authUser = new User(username, user.getPassword(),
							AuthorityUtils.commaSeparatedStringToAuthorityList("amdin"));
					PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
							authUser, password, authUser.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetails((HttpServletRequest) request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

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
		jb.put("ClientIP", "http://rxr56b.natappfree.cc");
		String result = HttpClientUtil.doPostJson(url, jb.toJSONString());
		return result;
	}
}
