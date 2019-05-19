package com.faujor.common.RFC;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.utils.SAPInterfaceUtil;


public class SapTest
{

	public static void main(String[] args)
	{

		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("LIFNR", "0000100163");
		json.put("ZJZDAT", "20180308");
		ja.add(json);


		SAPInvoke invoke = new SAPInvoke();

		String rs = invoke.invoke(SAPInterfaceUtil.RFC_ACC_DOCUMENT_EXPORT, ja.toJSONString());
		System.out.println(rs);

		/*
		 * JCoFunction function = null; JCoDestination destination = new SAPConn().connect(); String result="";//调用接口返回状态
		 * String message="";//调用接口返回信息 try { //调用ZRFC_GET_REMAIN_SUM函数 function =
		 * destination.getRepository().getFunction("ZSRMRFC_IBD_DELIVERY_IMPORT"); JCoParameterList input =
		 * function.getImportParameterList(); // INPUT JSONArray ja = new JSONArray(); JSONObject json = new JSONObject();
		 * json.put("BOLNR", "20180209001"); json.put("LFDAT", "20180210"); json.put("EBELN", "4500001327");
		 * json.put("EBELP", "10"); json.put("MATNR", "0302.02.002"); json.put("MENGE", 15); ja.add(json);
		 *
		 * JSONObject json1 = new JSONObject(); json1.put("BOLNR", "20180209001"); json1.put("LFDAT", "20180210");
		 * json1.put("EBELN", "4500001327"); json1.put("EBELP", "20"); json1.put("MATNR", "0302.02.002");
		 * json1.put("MENGE", 15); ja.add(json1);
		 *
		 * input.setValue("IM_DATA", ja.toJSONString()); function.execute(destination); JCoParameterList list =
		 * function.getExportParameterList();
		 *
		 * result= function.getExportParameterList().getString("ET_DATA");//调用接口返回状态
		 *
		 * System.out.println(result); if(result.equals("E")){
		 * System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message); return; }else{
		 * System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message); JCoParameterList tblexport =
		 * function.getTableParameterList(); //JCoParameterList tblexport =
		 * function.getTableParameterList().getTable("QUERY_H"); String msg = tblexport.toXML();
		 * System.out.println("调用返回表XML--->"+msg); } }catch (Exception e) { e.printStackTrace(); }
		 */
	}
}
