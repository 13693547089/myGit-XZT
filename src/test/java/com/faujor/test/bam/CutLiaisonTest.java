package com.faujor.test.bam;

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

import com.faujor.dao.master.bam.CutLiaisonMapper;
import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CutLiaisonTest {

	@Autowired
	private CutLiaisonMapper mapper;
	
	@Test
	public void test1(){
		Map<String, Object> map = new HashMap<String,Object>();
		CutLiaison cutLiai = new CutLiaison();
		map.put("cutLiai", cutLiai);
		map.put("start", 1);
		map.put("end", 5);
		List<CutLiaison> list = mapper.queryCutLiaisonByPage(map );
		for(CutLiaison c:list){
			System.out.println(c);
		}
		int i = mapper.queryCutLiaisonByPageCount(map);
		System.out.println(i);
	}
	@Test
	public void testinsert(){
		CutLiaison c = new CutLiaison();
		c.setCutMonth("2018年3月");
		c.setCreateDate(new Date());
		c.setCreateId("1");
		c.setCreator("admin");
		c.setLiaiCode("1232341342323");
		c.setStatus("已保存");
		c.setSuppId("111111");
		c.setSuppName("安微安宇");
		mapper.addCutLiaison(c );
	}
	@Test
	public void testinsert2(){
		
		CutLiaiMate c = new CutLiaiMate();
		c.setMateCode("11111122");
		c.setMateName("66666");
		c.setOutNum(656666);
		c.setProdNum(76666);
		c.setInveNum(5555);
		c.setFields("1232332");
		mapper.addCutLiaiMate(c );
	}
	@Test
	public void testinsert3(){
		CutLiaison c = mapper.queryCutLiaisonByLiaiId("cd74438c558a4a99a0a3f40fd896bab5");
		System.out.println(c);
	}
	
	@Test
	public void test4(){
		Map<String, Object> map = new HashMap<String,Object>();
		CutLiaison cutLiai = new CutLiaison();
		map.put("cutLiai", cutLiai);
		map.put("start", 1);
		map.put("end", 5);
		List<CutLiaison> list = mapper.queryCutLiaisonForManageByPage(map);
		for(CutLiaison c:list){
			System.out.println(c);
		}
		int i = mapper.queryCutLiaisonForManageByPageCount(map);
		System.out.println(i);
	}
	@Test
	public void testinsert5(){
		List<String> liaiIds = new ArrayList<String>();
		liaiIds.add("e79f0db728a443eeb3e1b36ffb403f28");
		liaiIds.add("6c1bc3374a0e4160bb7f44327547a62b");
		List<CutLiaison> list = mapper.queryManyCutLiaisonByLiaiIds(liaiIds );
		for (CutLiaison c : list) {
			System.out.println(c);
		}
	}
	
	@Test
	public void testinsert6(){
		List<String> liaiIds = new ArrayList<String>();
		liaiIds.add("e79f0db728a443eeb3e1b36ffb403f28");
		liaiIds.add("6c1bc3374a0e4160bb7f44327547a62b");
		List<CutLiaiMate> list = mapper.queryManyCutLiaiMateByLiaiIds(liaiIds);
		for (CutLiaiMate c : list) {
			System.out.println(c);
		}
	}
	@Test
	public void testinsert7(){
		List<CutLiaison> list = mapper.queryListCutLiaisonByCutMonth("2018-12");
		for (CutLiaison c : list) {
			System.out.println(c);
		}
	}
	
	
}
