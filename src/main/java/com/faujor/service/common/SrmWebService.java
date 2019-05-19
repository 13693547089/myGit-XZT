package com.faujor.service.common;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SrmWebService {
	/**
	 * 接口发布统一入库
	 * @param action
	 * @param data
	 * @return
	 */
	@WebMethod
	String invokeWs(@WebParam(name="action")String action,@WebParam(name="data")String data);
}
