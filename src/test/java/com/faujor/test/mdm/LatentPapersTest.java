package com.faujor.test.mdm;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.mdm.LatentPapersMapper;
import com.faujor.entity.document.Document;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.service.document.TemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LatentPapersTest {

	@Autowired
	private LatentPapersMapper mapper;
	@Autowired
	private TemplateService temService;
	@Test
	public void test1(){
		String suppId = "fa560de5578f4b6d81342ac6ab682b92";
		List<LatentPapers> list = mapper.queryManyLatentPapersBySuppId(suppId);
		for(LatentPapers lp:list){
			System.out.println(lp);
		}
		
	}
	@Test
	public void test2(){
		long l =(long) 119;
		List<Document> list = temService.getDocByMenuId(l);
		for(Document d :list){
			System.out.println(d);
		}
	}
	@Test
	public void test3(){
		Integer a =0;
		Integer b =100;
		Integer c =200;
		a=b-c;
		System.out.println(a);
	}
	
	
}
