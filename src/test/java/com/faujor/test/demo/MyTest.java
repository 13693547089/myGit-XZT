package com.faujor.test.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.alibaba.fastjson.JSONObject;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;

public class MyTest {
	public static void main(String[] args) {
//		String param="{\"quoteCode\":\"123456789\",\"status\":1,\"message\":\"Hello World!\"}";
//		
//		String wsdl="http://yw.joruns.com:9010/services/srm?wsdl";
//		String method="invokeWs";
//		String result=invokeWs(wsdl, method, "token123",param);
//		System.out.println(result);
//		String str="1234567";
//		System.out.println(str.substring(2));
		

		
//		JSONObject obj=new JSONObject();
//		obj.put("status", "S");
//		obj.put("quoteCode", "6180300023");
//		obj.put("message", "审批通过");
//		System.out.println(obj.toJSONString());
		
		BigDecimal bg=new BigDecimal(1000);
		System.out.println(bg.negate());
		
		//输出调用结果
	}
	
	
	public static String  invokeWs(String wsdl,String method,Object...objs){
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsdl);
		try {
			Object object = client.invoke(method,objs);
			return JsonUtils.beanToJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void testCalender() {

		Date date = DateUtils.parse("2020-2", "yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		List<Date> list = new ArrayList<Date>();
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		list.add(new Date(calendar.getTimeInMillis()));
		for (int j = 1; j < actualMaximum; j++) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date currDate = new Date(calendar.getTimeInMillis());
			list.add(currDate);
		}
		for (Date date2 : list) {
			System.out.println(DateUtils.format(date2, "yyyy-MM-dd"));
		}
	}
	
	public static void testCalender1() {

		Date date = DateUtils.parse("2017-2", "yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		for (int j = 1; j < 13; j++) {
			calendar.add(Calendar.MONTH, 1);
			System.err.println(DateUtils.format(calendar.getTime(),"yyyy-MM"));
		}
	}
	
	public static void testCalender2() {

		Date date = DateUtils.parse("2017-2", "yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		for (int j = 1; j < 13; j++) {
			calendar.add(Calendar.MONTH, 1);
			Date currDate=new Date(calendar.getTimeInMillis());
			System.err.println(DateUtils.format(currDate, "yyyy")+"--------------------"+DateUtils.format(currDate, "M"));
		}
	}
	

	
	
	public static void testBigDecimal() {
		BigDecimal amt = new BigDecimal(1001.01);
		BigDecimal[] results = amt.divideAndRemainder(BigDecimal.valueOf(20));
		System.out.println(results[0]);
		System.out.println(results[1]);
	}
	
}
