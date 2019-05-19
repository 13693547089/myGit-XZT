package com.faujor.common.RFC;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.faujor.dao.master.common.InterfaceLogMapper;
import com.faujor.entity.common.InterfaceLogMain;
import com.faujor.entity.common.SysUserDO;
import com.faujor.utils.UserCommon;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

/**
 * RFC函数调用类
 * @author Vincent
 *
 */
@Component
public class SAPInvoke {
	
	@Autowired
	private InterfaceLogMapper logMapper;
	private static InterfaceLogMapper logMapperStatic;

	@PostConstruct
	public void init() {
		logMapperStatic = logMapper;
	}
	private JCoDestination destination = null;
	
	public SAPInvoke(){
		destination = new SAPConn().connect();
	}
	
	/**
	 * 调用
	 * @param methodName 方法名称
	 * @param inData 传入参数
	 * @return
	 */
	public String invoke(String methodName,String inData){
		
		InterfaceLogMain log=new InterfaceLogMain();
		log.setInterfaceNum(methodName);
		log.setInterfaceDesc("调用SAP接口");
		log.setInJson(inData);
		SysUserDO user = UserCommon.getUser();
		log.setInvoker(user.getUsername());
		log.setInvokeTime(new Date());
        String result="";//调用接口返回状态
        try {
        	JCoFunction function = null;
            //调用ZRFC_GET_REMAIN_SUM函数  
            function = destination.getRepository().getFunction(methodName);  
            JCoParameterList input = function.getImportParameterList();  
            // INPUT            
            input.setValue("IM_DATA", inData);
            
            // 执行
            function.execute(destination);
            JCoParameterList exportParameterList = function.getExportParameterList();
            // 获取结果
            result= exportParameterList.getString("ET_DATA");//调用接口返回状态  
            log.setOutJson(result);
        }catch (Exception e) {
        	result = null;
        }finally {
			logMapperStatic.saveLog(log);
		}
        
        return result;
	}
}
