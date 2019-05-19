package com.faujor.test.bam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.faujor.dao.master.bam.AppointMapper;
import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.Appoint;
import com.faujor.service.bam.AppointService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointTest {

	@Autowired
	private AppointMapper mapper;
	@Autowired
	private AppointService service;
	@Test
	public void testInsert(){
		Appoint appo = new Appoint();
		appo.setSuppId("000001");
		appo.setSuppName("安微安宇");
		int i = mapper.addAppoint(appo);
		System.out.println(i);
	}
	@Test
	public void testQueryAppoint(){
		Appoint appo = new Appoint();
		appo.setAppoStatus("已确认");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", 1);
		map.put("end", 5);
		map.put("createId", "001");
		map.put("appo", appo);
		
		List<Appoint> list = mapper.queryAppointOfSuppByPage(map );
		for(Appoint a :list){
			System.out.println(a);
		}
		int i = mapper.queryAppointOfSuppByPageCount(map);
		System.out.println(i);
	}
	/*@Test
	public void testAddAppoMate(){
		AppoMate am = new AppoMate();
		am.setAppoId("a92f28857cc4448cb35e738c2dd3b3e1");
		am.setMateCode("30001");
		am.setMateName("MH2原料");
		am.setMateNumber(60);
		am.setMateAmount(60);
		int i = mapper.addAppoMate(am);
		System.out.println(i);
	}*/
	@Test
	public void test4(){
		Appoint appo = mapper.queryAppointByAppoId("9036992f63644eab9964a80a2491e45b");
		List<AppoMate> list = appo.getAppoMates();
		for(AppoMate am:list){
			System.out.println(am);
		}
		System.out.println(appo);
	}
	@Test
	public void test5(){
		int i = mapper.deleteAppoMateByOneAppoId("7da1932ece2344be99bc41710d3a66ba");
		System.out.println(i);
	}
	@Test
	public void test6(){
		Appoint appo = new Appoint();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", 1);
		map.put("end", 5);
		map.put("appo", appo);
		List<Appoint> list = mapper.queryAppointForManagerByPage(map);
		for(Appoint a :list){
			System.out.println(a);
		}
		int i = mapper.queryAppointForManagerByPageCount(map);
		System.out.println(i);
		
	}
	
	@Test
	public void test7(){
		Appoint appo = new Appoint();
		appo.setAppoId("a47609bdd71f4bf5b57d9ce151f8a037");
		appo.setPriority("紧急");
		int i = mapper.updateAppointPriorityByAppoId(appo );
		System.out.println(i);
	}
	@Test
	public void test8(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		List<String> appoIds = new ArrayList<String>();
		appoIds.add("fc958de48c4c40d09f129cb0018aff46");
		appoIds.add("a47609bdd71f4bf5b57d9ce151f8a037");
		map.put("appoIds",appoIds);
		map.put("appoStatus", "已确认");
		int i = mapper.updateAppoStatusByAppoId(map);
		System.out.println(i);
	}
	@Test
	public void test9(){
		Appoint appo = new Appoint();
		appo.setCreateDateStart("2018/2/6 00:00:00");
		appo.setCreateDateEnd("2018/2/6 23:59:59");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", 1);
		map.put("end", 5);
		map.put("appo", appo);
		
		List<Appoint> list = mapper.queryAppointForIssueByPage(map);
		for(Appoint a :list){
			System.out.println("0000");
			System.out.println(a);
		}
		
		
	}
	@Test
	public void test10() throws ParseException{
		String str = "2018/2/28";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date appoDate =format.parse(str);
		List<Appoint> list = mapper.queryAppointForIssueByAppoDate(appoDate);
		
		for(Appoint a:list){
			System.out.println(a);
		}
		
	}
	@Test
	public void test11(){
		Appoint appo = new Appoint();
		appo.setAffirmDate("13:00-15:00");
		appo.setAppoStatus("已发布");
		appo.setAppoId("054cf84530d34f3ca9b0afb6c8abcf64");
		int i = mapper.updateAffirmDate(appo);
		System.out.println(i);
	}
	@Test
	public void test12() throws ParseException{
		String str = "2018/2/10";
		/*SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date appoDate =format.parse(str);*/
		List<Appoint> list = mapper.queryAppoStatByAppoDate(str);
		for(Appoint a:list){
			System.out.println(a);
		}
		List<Appoint> data = service.queryAppoStatByAppoDate(str);
		System.out.println("000000000000");
		for(Appoint a:list){
			System.out.println(a);
		}
	}
	
	@Test
	public void testquerybyappoCode(){
		Appoint appoint = mapper.queryAppointByAppoCode("20180209111539");
		System.out.println(appoint);
		List<AppoMate> list = appoint.getAppoMates();
		for(AppoMate a:list){
			System.out.println(a);
		}
		
	}
	@Test
	public void testupdateappoStatus(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("appoStatus", "待发货");
		map.put("appoCode", "20180209111538");
		mapper.updateAppoStatusByAppoCode(map);
		
	}
	@Test
	public void testquery(){
		List<AppoCar> list = mapper.queryAppoCarOfAppoint("8e0224f7e94b4656b34c6345c05244f9");
		for(AppoCar a:list){
			System.out.println(a);
		}
		
	}
	@Test
	public void testqueryManyAppoint(){
		List<String> appoIds = new ArrayList<String>();
		appoIds.add("65c49193cd8041af80aa284d269f7a19");
		appoIds.add("9c2fe284d26d45ad80b901eb121b8456");
		appoIds.add("6acacdf6c68c4c30b28585d7e3eec5e5");
		List<Appoint> list = mapper.queryManyAppointByAppoIds(appoIds );
		for (Appoint a : list) {
			System.out.println(a);
		}
		
	}
	
	
	
}
