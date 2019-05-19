package com.faujor.core.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class NativeFilter implements Filter{
	@Override
	public void destroy() {
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse rep,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		System.out.println("此处可以自定义过滤器:"+request.getRequestURI());
		filterChain.doFilter(request,rep);
	}
	@Override
	public void init(FilterConfig fc) throws ServletException {
	}
}
