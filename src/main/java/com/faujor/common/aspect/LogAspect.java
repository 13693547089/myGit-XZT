package com.faujor.common.aspect;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.faujor.common.annotation.Log;
import com.faujor.dao.master.basic.LogMapper;
import com.faujor.entity.basic.LogDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.utils.HttpContextUtils;
import com.faujor.utils.IPUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.MACUtils;
import com.faujor.utils.UserCommon;

@Aspect
@Component
public class LogAspect {
	@Autowired
	LogMapper logMapper;

	@Pointcut("@annotation(com.faujor.common.annotation.Log)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveLog(point, time);
		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		LogDO sysLog = new LogDO();
		Log syslog = method.getAnnotation(Log.class);
		if (syslog != null) {
			// 注解上的描述
			sysLog.setOperation(syslog.value());
		}
		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		// 请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = JsonUtils.beanToJson(args[0]);
			if (params.length() > 1000)
				params = params.substring(0, 999);
			sysLog.setParams(params);
		} catch (Exception e) {

		}
		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		// 设置IP地址
		String ipAddr = IPUtils.getIpAddr(request);
		sysLog.setIp(ipAddr);
		String macAddr = MACUtils.getLocalMac(ipAddr);
		sysLog.setMacAddr(macAddr);
		// 用户名
		SysUserDO currUser = UserCommon.getUser();
		if (null == currUser) {
			if (null == sysLog.getParams()) {
				sysLog.setUserId(-1L);
				sysLog.setUserName(sysLog.getParams());
			} else {
				sysLog.setUserId(-1L);
				sysLog.setUserName("未获取到用户信息");
			}
		} else {
			sysLog.setUserId(currUser.getUserId());
			sysLog.setUserName(currUser.getUsername());
			sysLog.setJobNumber(currUser.getSuppNo());
			sysLog.setRealName(currUser.getName());
		}
		sysLog.setTime((int) time);
		// 系统当前时间
		Date date = new Date();
		sysLog.setCreateTime(date);
		// 保存系统日志
		logMapper.saveSysLog(sysLog);
	}
}
