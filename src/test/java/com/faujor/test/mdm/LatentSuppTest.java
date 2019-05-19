package com.faujor.test.mdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.mdm.LatentPapersMapper;
import com.faujor.dao.master.mdm.LatentSuppMapper;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;
import com.faujor.service.mdm.LatentSuppService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LatentSuppTest {

	@Autowired
	private LatentSuppService latentSuppService;
	@Autowired
	private LatentSuppMapper mapper;
	@Autowired
	private LatentPapersMapper  latentPapersMapper;
	
	@Test
	public void testInsert(){
		LatentSupp supp = new LatentSupp();
		supp.setSrmId("77777");
		supp.setCategoryId("102");
		supp.setCategory("国内产商");
		supp.setSuppAbbre("安宇");
		supp.setSuppName("安徽安宇乳胶制品有限公司");
		supp.setProvCode("22222");
		supp.setProvName("安徽");
		supp.setCityCode("33333");
		supp.setCityName("蚌埠");
		supp.setPostcode("444444");
		supp.setAddress("安徽省蚌埠市禹和路95号");
		supp.setContacts("张三");
		supp.setPhone("1445434554556");
		supp.setBankCode("555555");
		supp.setBankAbbre("中国银行");
		supp.setBankName("中国银行蚌埠分行");
		supp.setBankAccount("183292939392333332");
		supp.setAccountHolder("安徽安宇乳胶制品有限公司");
		supp.setHolderPhone("0552-4928979");
		supp.setFaxNumber("0552-4928979");
		supp.setEmail("yuhe@gmail.com");
		supp.setPayClauseId("666666");
		supp.setCurrencyId("7777777");
		supp.setTaxeKindId("888888");
		supp.setRemark("备注");
		
		boolean b = latentSuppService.insertLatentSupp(supp);
		System.out.println(b);
	}
	
	@Test
	public void testQueryByPage(){
		Map<String,Object> map =new HashMap<String,Object>();
		LatentSupp supp = new LatentSupp();
		//supp.setStatus("已初审");
		supp.setCategory("国外厂商");
		//supp.setSuppInfo("酸");
		map.put("start", 1);
		map.put("end", 10);
		map.put("latentSupp", supp);
		
		 Map<String, Object> page = latentSuppService.queryLatentSuppByPage(map);
		 List<LatentSupp> list = (List<LatentSupp>) page.get("data");
		int i = Integer.parseInt((String) page.get("count"));
		for(LatentSupp s :list){
			System.out.println(s);
		}
		System.out.println(i);
	}
	
	@Test
	public void queryCount(){
		Map<String,Object> map =new HashMap<String,Object>();
		LatentSupp supp = new LatentSupp();
		supp.setStatus("已初审");
		//supp.setCategory("外");
		//supp.setSuppInfo("酸");
		map.put("start", 1);
		map.put("end", 3);
		map.put("latentSupp", supp);
		List<LatentSupp> list = mapper.queryLatentSuppByPage(map);
		for(LatentSupp s:list){
			System.out.println(s);
		}
		int i = mapper.queryLatentSuppCount(map);
		System.out.println(i);
	}
	@Test
	public void insertPaper(){
		LatentPapers latentPapers = new LatentPapers();
		latentPapers.setPapersName("1");
		latentPapers.setPapersType("2");
		latentPapers.setStartDate(new Date());
		latentPapers.setEndDate(new Date());
		latentPapers.setAcceOldName("33");
		boolean b = latentSuppService.insertLatentPapers(latentPapers);
	}
	@Test
	public void delete(){
		String[] suppId = new String[]{"1cc1060b96574a0da8b1c16ec5968789"};
		int i = latentPapersMapper.deleteLatentPapersBySuppId(suppId);
		int j = mapper.deleteLatentSuppBySuppId(suppId);
		System.out.println(i+"   "+j);
	}
	@Test
	public void queryOneLatentSuppBySuppId(){
		String suppId = "8b8d4146dc4f4a7997bdf0dec181a4a5";
		LatentSupp supp = mapper.queryOneLatentSuppBySuppId(suppId);
		System.out.println(supp);
	}
	@Test
	public void updateLatentSuppBySuppId(){
		LatentSupp supp = new LatentSupp();
		supp.setSuppId("8b8d4146dc4f4a7997bdf0dec181a4a5");
		supp.setCategoryId("GNCS");
		supp.setCategory("国内产商");
		supp.setSuppName("安徽安宇乳胶制品有限公司");
		supp.setSuppAbbre("安宇");
		supp.setPostcode("88888");
		supp.setProvCode("10012");
		supp.setProvName("江西");
		supp.setCityCode("10023");
		supp.setCityName("南昌");
		supp.setContacts("小于");
		supp.setPhone("15890876547");
		supp.setBankCode("666666");
		supp.setBankName("中国银行蚌埠分行");
		supp.setBankAbbre("中国银行");
		supp.setBankAccount("3423y5470529847");
		supp.setAccountHolder("小哥");
		supp.setHolderPhone("13775620892");
		supp.setFaxNumber("323453463");
		supp.setEmail("12373487@163.com");
		supp.setPayClauseId("票到月结30天");
		supp.setCurrencyId("中国人民币");
		supp.setTaxeKindId("17%进项税,中国");
		supp.setRemark("5y87834652458234705");
	    int i = mapper.updateLatentSuppBySuppId(supp);
		System.out.println(i);
	}
	
	@Test
	public void updatetest(){
		LatentSupp supp = new LatentSupp();
		supp.setSuppId("8b23ea92266b43f1aed251715f74ee65");
		supp.setCategoryId("GWCS");
		supp.setCategory("国外产商");
		supp.setPayClauseId("pd30day");
		supp.setCurrencyId("CNY");
		supp.setTaxeKindId("T17%CHINA");
		supp.setRemark("00000000000000");
		int i = mapper.updateLatentSuppAtAudit(supp);
		System.out.println(i);
	}
	@Test
	public void queryToMini(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<Integer> userIds = new ArrayList<Integer>();
		LatentSupp supp = new LatentSupp();
		userIds.add(11);
		map.put("userIds",userIds);
		map.put("latentSupp", supp);
		map.put("start", 1);
		map.put("end", 5);
		List<LatentSupp> list = mapper.queryLatentSuppToMini(map);
		int i = mapper.queryLatentSuppToMiniCount(map);
		System.out.println(i);
		for(LatentSupp s:list){
			System.out.println(s);
		}
	}
	@Test
	public void queryToMini2(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<Integer> userIds = new ArrayList<Integer>();
		LatentSupp supp = new LatentSupp();
		userIds.add(11);
		map.put("userIds",userIds);
		map.put("latentSupp", supp);
		map.put("start", 1);
		map.put("end", 5);
		Map<String, Object> page = latentSuppService.queryLatentSuppToMini(map);
		List<LatentSupp> list =(List<LatentSupp>) page.get("data");
		int i = Integer.parseInt((String) page.get("count"));
		System.out.println(i);
		for(LatentSupp s:list){
			System.out.println(s);
		}
	}
	
	
}
