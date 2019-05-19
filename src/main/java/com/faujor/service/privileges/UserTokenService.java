package com.faujor.service.privileges;


import org.springframework.stereotype.Service;

import com.faujor.entity.common.User;

/**
 * 对 token 进行操作的接口
 * @author gongjie
 * @date 2017/10/13.
 */
@Service
public interface UserTokenService {
	 /**
     * 验证登陆
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    public User login(User user);   
}
