package com.faujor.test.bam.quote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QuoteJson {
	public static void main(String[] args) {
		
		JSONObject quote=new JSONObject();
		quote.put("userName", "admin");
		quote.put("quoteCode", "Q20180201001");
		quote.put("quoteDate", "2018-02-01");
		quote.put("suppNo", "S20180201001");
		quote.put("quoteType", "FL");
		quote.put("quoteBase", "smb://172.119.0.13/产品研发部/21 脱普srm/文档/文档管理/模板管理/523bd031af944eda88a55f02202e0ecf产销.jpg");
		quote.put("remark", "报价单备注");
		
		JSONArray  mateArr=new JSONArray();
		JSONObject mate=new JSONObject();
		mate.put("index", "1");
		mate.put("mateNo", "M20180201001");
		mate.put("suppScope", "范围一");
		mate.put("remark", "测试物料备注0001");
		mateArr.add(mate);
		
		JSONArray segmArr=new JSONArray();
		JSONObject segm=new JSONObject();
		segm.put("segmCode", "10");
		segm.put("Subtotal", 1000.25);
		segmArr.add(segm);
		
		mate.put("item", segmArr);
		quote.put("item", mateArr);
		System.out.println(quote.toJSONString());
	}
}
