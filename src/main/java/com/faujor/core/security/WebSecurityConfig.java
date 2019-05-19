package com.faujor.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.faujor.utils.MD5Utils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	UserDetailsService customUserService() { // 注册UserDetailsService 的bean
		return new CustomUserService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService()).passwordEncoder(new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				String password = (String) rawPassword;
				boolean b = encodedPassword.equals(MD5Utils.encrypt(password));
				return b;
			}

			@Override
			public String encode(CharSequence pwd) {
				String password = (String) pwd;
				return MD5Utils.encrypt(password);
			}
		});

	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()// 定义哪些URL需要被保护、哪些不需要被保护
				// 不需要任何认证就可以访问，其他的路径都必须通过身份验证
				.antMatchers("/css/**", "/larryMS/**", "/img/**", "/druid/**", "/fonts/**", "/endpointAric/**",
						"/welcome", "/suppReg/**", "/services/**", "/CheckAccess", "/AccessToken", "/top_srm")
				.permitAll()
				// 任何尚未匹配的URL只需要验证用户即可访问
				.anyRequest()
				.authenticated()
				.and()
					.formLogin()
						.loginPage("/login").permitAll()
						.failureUrl("/login?error").permitAll()// 登录页面和登录错误页面
					.defaultSuccessUrl("/srm")
				.and()
					.logout().permitAll()
//				.and()
//					.sessionManagement().invalidSessionUrl("/login")
				.and()
					.csrf()
				// /druid/basic.json 请求不做csrf控制
				.ignoringAntMatchers("/**").and().headers()
				// 关闭X-content-Type-Options:nosniff,使druid页面可以正常显示
				.contentTypeOptions().disable().and();
		// 设置可以iframe访问
		http.headers().frameOptions().sameOrigin();
	}

	// 内存缓存用户密码和角色
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService());
		// .inMemoryAuthentication()
		// .withUser("user").password("password").roles("USER");
	}
}
