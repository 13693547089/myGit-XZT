package com.faujor.test.bam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryTest {

	@Autowired
	private DeliveryMapper mapper;
	
	@Test
	public void testQuery(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("createId", "1");
		map.put("start", 1);
		map.put("end", 5);
		List<Delivery> list = mapper.queryDeliveryByPage(map );
		for(Delivery d:list){
			System.out.println(d);
		}
		
		int i = mapper.queryDeliveryByPageCount(map);
		System.out.println(i);
	}
	@Test
	public void testDelete(){
		String[] deliIds = new String[]{"54e405d614e2447ca7f05094f95fe207","dd9ece6c7db04d2da7d031946e936a72"};
		int i = mapper.deleteDeliveryBydeliId(deliIds );
		System.out.println(i);
	}
	@Test
	public void testInsert(){
		Delivery deli = new Delivery();
		deli.setContact("李四");
		int i = mapper.insertDelivery(deli );
		System.out.println(i);
		DeliMate deliMate = new DeliMate();
		deliMate.setUnit("箱");
		int j = mapper.insertDeliMate(deliMate );
		System.out.println(j);
	}
	@Test
	public void testUpdate(){
		Delivery deli = new Delivery();
		deli.setContact("李四");
		deli.setDeliId("2aa198788a7344808f0a2c9ed94c6b62");
		deli.setPhone("555555555");
		deli.setReceAddr("05555");
		deli.setReceUnit("05555");
		deli.setMapgCode("20180209111538");
		int j = mapper.updateDeliveryByDeliId(deli);
		System.out.println(j);
	}
	@Test
	public void testqueryByDeliCode(){
		Delivery deli = mapper.queryDeliveryByDeliCode("20180223171612");
		System.out.println(deli);
		List<DeliMate> list = mapper.queryDeliMateByDeliCode("20180223171612");
		for(DeliMate d:list){
			System.out.println(d);
		}
		
	}
	@Test
	public void testupdateByDeliCode(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("status", "待收货");
		map.put("deliCode","100001");
		int i = mapper.updateDeliStatusByDeliCode(map );
		System.out.println(i);
		
	}
	@Test
	public void test1(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<String> statusList = new ArrayList<String>();
		statusList.add("已发货");
		statusList.add("已签到");
		statusList.add("待收货");
		map.put("statusList", statusList);
		map.put("suppId", "s0000100001");
		map.put("mateCode", "000000000020000091");
		List<DeliMate> deliMates = mapper.queryDeliMateBySuppIdAndMateCode(map);
		for (DeliMate d : deliMates) {
			System.out.println(d);
		}
	}
	
	
}
