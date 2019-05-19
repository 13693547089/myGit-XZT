package com.faujor.test.bam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.bam.StraMessageMapper;
import com.faujor.dao.master.common.MenuMapper;
import com.faujor.dao.master.fam.AuditMapper;
import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.StraMessDO;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.fam.AuditMate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StraMessageTest {

	@Autowired
	private StraMessageMapper mapper;
	@Autowired
	private AuditMapper auditMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Test
	public void testInsert(){
		StraMessage straMess = new StraMessage();
		straMess.setMessCode("70001");
		straMess.setMessStatus("已保存");
		int i = mapper.addStraMessage(straMess);
		System.out.println(i);
	}
	@Test
	public void querybyPage(){
		StraMessage straMess = new StraMessage();
		straMess.setMessStatus("已保存");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start",1);
		map.put("end", 5);
		map.put("createId","1");
		map.put("straMess", straMess);
		List<StraMessDO> list = mapper.queryStraMessageByPage(map);
		for(StraMessDO m:list){
			System.out.println(m);
		}
		int count = mapper.queryStraMessageByPageCount(map);
		System.out.println(count);
	}
	
	@Test
	public void testQueryByMessId(){
	 StraMessage straMessage = mapper.queryStraMessageByMessId("3ee7e4f447344e4db619dc3f012099fc");
	 List<MessMate> list = straMessage.getMessMates();
	 for(MessMate m:list){
		 System.out.println(m);
	 }
	 System.out.println(straMessage);
		
		
	}
	@Test
	public void testUpdateByMessId(){
		StraMessage straMess = new StraMessage();
		straMess.setMessId("9a2662dac9eb4be29391a0b396b0c74a");
		straMess.setSuppName("安宇");
		mapper.updateStraMessageByMessId(straMess );
	}
	
	@Test
	public void testqueryStraMessage(){
		StraMessage straMess = mapper.queryStraMessageBymessCode("20180202163325");
		System.out.println(straMess);
		List<MessMate> list = straMess.getMessMates();
		for(MessMate m:list){
			System.out.println(m);
		}
	}
	@Test
	public void testupdateStatus(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("messStatus", "待发货");
		map.put("messCode", "20180208110626");
		int i = mapper.updateMessStatusByMessCode(map);
		System.out.println(i);
	}
	@Test
	public void query2(){
		Map<String,Object> param =  new HashMap<String,Object>();
		Calendar c=Calendar.getInstance();  
		c.add(Calendar.MONTH, -1);  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String gtimelast = sdf.format(c.getTime()); //上月  
		System.out.println(gtimelast);  
		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);  
		System.out.println(lastMonthMaxDay);  
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);  
		//按格式输出  
		String gtime = sdf.format(c.getTime()); //上月最后一天  
		System.out.println(gtime);  
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");  
		String gtime2 = sdf2.format(c.getTime()); //上月第一天  
		System.out.println(gtime2);  
		param.put("startDate", gtime2);
		param.put("endDate", gtime);
		param.put("suppId", "s0000100001");
		param.put("mateId", "0412141fabcc4264a421a0b323b4ee51");
		AuditMate mate = auditMapper.queryLastMonthBala(param);
		System.out.println(mate);
	}
	@Test
	public void test12(){
		List<MenuDO> queryChild = menuMapper.queryChild("142");
		for (MenuDO m : queryChild) {
			System.out.println(m);
		}
	}
	
}
