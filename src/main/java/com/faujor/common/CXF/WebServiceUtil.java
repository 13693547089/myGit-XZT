package com.faujor.common.CXF;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.faujor.common.constant.GlobalConstant;
import com.faujor.dao.master.common.InterfaceLogMapper;
import com.faujor.entity.common.InterfaceLogMain;
import com.faujor.entity.common.SysUserDO;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;
@Component
public class WebServiceUtil {

	@Autowired
	private InterfaceLogMapper logMapper;
	private static InterfaceLogMapper logMapperStatic;

	@PostConstruct
	public void init() {
		logMapperStatic = logMapper;
	}
	
	public static String invokeWs(String wsdl, String method,String methodDesc, Object... objs) {
		
		InterfaceLogMain log=new InterfaceLogMain();
		log.setInterfaceNum(method);
		log.setInterfaceDesc(methodDesc);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < objs.length; i++) {
			sb.append(objs[i].toString());
		}
		log.setInJson(sb.toString());
		SysUserDO user = UserCommon.getUser();
		log.setInvoker(user.getUsername());
		log.setInvokeTime(new Date());
		String resStr=null;
		try {
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(wsdl);
			
	        HTTPConduit http = (HTTPConduit) client.getConduit(); 
	        
	        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();        
	        httpClientPolicy.setConnectionTimeout(180000);  //连接超时      
	        httpClientPolicy.setAllowChunking(false);    //取消块编码   
	        httpClientPolicy.setReceiveTimeout(180000);     //获取连接后响应超时  
	        http.setClient(httpClientPolicy);  
			
			Object[] objects = client.invoke(method, objs);
			if (objects == null || objects.length == 0) {
				return null;
			}
			resStr= JsonUtils.beanToJson(objects[0]);
			log.setOutJson(resStr);
			if(method.equals(GlobalConstant.OA_AUDIT)){
				JSONObject obj=JSONObject.parseObject(resStr);
				log.setStatus(obj.getString("sTATUS"));
				log.setMessage(obj.getString("mESSAGE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.setOutJson("");
			log.setStatus("E");;
			log.setMessage("接口调用失败");
		}finally {
			logMapperStatic.saveLog(log);
		}
		return resStr;
	}
}
