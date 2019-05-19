package com.faujor.core.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Bean
	ApiInterceptor localInterceptor() {
		return new ApiInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(localInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/login", "/error", "/suppReg/**", "/CheckAccess", "/AccessToken", "/top_srm");
		super.addInterceptors(registry);
	}
}
