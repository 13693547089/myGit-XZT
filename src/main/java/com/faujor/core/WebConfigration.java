package com.faujor.core;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.faujor.core.filter.NativeFilter;
import com.faujor.service.privileges.SysUserService;
import com.faujor.service.privileges.impl.SysUserServiceImpl;

@Configuration
public class WebConfigration {
	@Bean
	public RemoteIpFilter remotelpFilter() {
		return new RemoteIpFilter();
	}

	@Bean
	public FilterRegistrationBean filterRegister() {
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new NativeFilter());
		filter.addUrlPatterns("/");
		filter.addInitParameter("paraName", "boot");
		filter.setName("bootNativeFilter");
		filter.setOrder(1);
		return filter;
	}

	@Bean
	public SysUserService sysUserService() {
		return new SysUserServiceImpl();
	}
}
