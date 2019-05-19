package com.faujor.test.bam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.bam.ReceiveMessageMapper;
import com.faujor.entity.bam.ReceiveMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceiveMessageTest {

	@Autowired
	private ReceiveMessageMapper mapper;
	
	@Test
	public void test1(){
		ReceiveMessage rece = mapper.queryReceiveMessByReceUnit("北京CDC");
		System.out.println(rece);
	}
	@Test
	public void testinsert(){
		ReceiveMessage receMess = new ReceiveMessage();
		receMess.setReceUnit("上海CDC");
		mapper.addReceiveMessage(receMess );
	}
	@Test
	public void testquery(){
		ReceiveMessage rm = mapper.queryReceMessById("1");
		System.out.println(rm);
	}
	@Test
	public void testupdate(){
		ReceiveMessage r = new ReceiveMessage();
		r.setPhone("15489706573");
		r.setId("1");
		r.setReceUnit("无锡CDC");
		r.setReceAddr("无锡市梅村镇锡甘路北21号");
		r.setContact("吴强");
		mapper.udpateReceiveMessage(r );
	}
	@Test
	public void test2(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", 1);
		map.put("end", 2);
		List<ReceiveMessage> list = mapper.queryReceiveMessByPage(map );
		for (ReceiveMessage rm : list) {
			System.out.println(rm);
		}
		int i = mapper.queryReceiveMessByPageCount(map);
		System.out.println(i);
	}
	
}
