package com.faujor.core.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.faujor.core.plugins.redis.RedisClient;

@WebListener
public class OnLineCount implements HttpSessionListener {

//	@Autowired
//	private RedisClient redisClient;

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		String sessionId = session.getId();
		session.setAttribute("sessionId", sessionId);
		System.out.println("OnLineCount.sessionCreated()" + sessionId);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		String sessionId = session.getId();
//		redisClient.del(sessionId);
		// 清除所有缓存
		System.out.println("OnLineCount.sessionDestroyed()" + sessionId);
	}

}
