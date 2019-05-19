package com.faujor.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.faujor.core.plugins.redis.RedisClient;

/**
 * 自定义拦截器，判断此次请求是否有权限
 * 
 * @author martian
 * @date 2017/10/18.
 */
@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

//	@Autowired
//	private RedisClient redisClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 请求路径
		String uri = request.getRequestURI();
		System.out.println("请求访问路径" + uri);
		// String version= request.getHeader("version");
		// System.out.println("版本号"+version);
		// 非登陆与报错页面 都需要验证token
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null
				&& context.getAuthentication().getPrincipal() != null) {
//			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
//			String username = userDetails.getUsername();
			return true;
		}
		response.sendRedirect("/login?error");
		return false;
	}
}
