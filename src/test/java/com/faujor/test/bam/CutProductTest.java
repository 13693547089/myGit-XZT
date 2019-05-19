package com.faujor.test.bam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.bam.CutProductMapper;
import com.faujor.entity.bam.CutProduct;
import com.faujor.entity.bam.CutStructure;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.ProdMateDO;
import com.faujor.service.bam.CutProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CutProductTest {

	@Autowired
	private CutProductMapper mapper;
	@Autowired
	private CutProductService service;
	@Test
	public void testquery(){
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 5);
		map.put("mate", mate);
		 List<ProdMateDO> list = mapper.queryCutProductByPage(map);
		
		for(ProdMateDO p :list){
			System.out.println(p);
		}
		int i = mapper.queryCutProductByPageCount(map);
		System.out.println(i);
	}
	@Test
	public void testquery2(){
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 5);
		map.put("mate", mate);
		Map<String, Object> map2 = service.queryCutProductByPage(map);
		List<MateDO> list = (List<MateDO>) map2.get("data");
		int i = (int) map2.get("count");
		for(MateDO p :list){
			System.out.println(p);
		}
		System.out.println(i);
	}
	
	@Test
	public void queryCount(){
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 5);
		map.put("mate", mate);
	}
	
	public void queryAllCut(){
		List<ProdMateDO> list = mapper.queryAllCutProduct();
		for(ProdMateDO m :list){
			System.out.println(m);
		}
	}
	@Test
	public void queryAllCutStru(){
		List<CutStructure> list = mapper.queryAllCutStru();
		for(CutStructure c:list){
			System.out.println(c);
		}
	}
	@Test
	public void testInsert(){
		CutStructure c = new CutStructure();
		c.setClassCode("1");
		int i = mapper.addCutStru(c );
	}
	@Test
	public void testqueryClassCodes(){
	    List<String> list = mapper.queryClassCodes();
	    for(String s:list){
	    	System.out.println(s);
	    }
	}
	@Test
	public void testquery3(){
		String string = mapper.queryMaxcontentCodeOfClassCode("10");
		System.out.println(string);
	}
	@Test
	public void testquery4(){
    CutProduct p = mapper.queryProdMateByVersion("0c9da9d8cd734d338a816459ee8b9a46","1.0");
		System.out.println(p);
	}
	@Test
	public void testquery5(){
		List<CutStructure> list = mapper.queryCutStruForCutProd();
		for (CutStructure c : list) {
			System.out.println(c);
		}
	}
	@Test
	public void testquery6(){
		List<ProdMateDO> list = mapper.queryMatesOfCutProduct();
		for (ProdMateDO p : list) {
			System.out.println(p);
		}
		System.out.println(list.size());
	}
	
	
	
	
	
	
	
	
}
