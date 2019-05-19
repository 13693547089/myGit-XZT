package com.faujor.test.mdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.MateUnit;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.UserSuppMate;
import com.faujor.service.common.CodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaterialTest {

	@Autowired
	private MaterialMapper mapper;
	@Autowired
	private CodeService codeService;
	@Test
	public void testInsert1(){
		Material mate = new Material();
		mate.setMateName("木头");
		int i = mapper.insertMaterial(mate);
		System.out.println(i);
		
		
	}
	@Test
	public void test2(){
		Material mate = new Material();
		//mate.setMateInfo("MHS(1片装)原料");
		//mate.setMateTypeInfo("包装材料1");
		///mate.setMateGroupInfo("6001");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", 1);
		map.put("end", 5);
		map.put("mate", mate);
		List<MateDO> list = mapper.findMateDO(map);
		for(MateDO m:list){
			System.out.println(m);
		}
		int i = mapper.queryMaterialCount(map);
		System.out.println(i);
		
		
	}
	
	/*@Test
	public void test3(){
		Material mate = mapper.queryOneMaterialByMateId("127bb9610c5e45eb97a786463520d10b");
		System.out.println(mate);
		List<MateUnit> mateUnits = mate.getMateUnits();
		for(MateUnit u:mateUnits){
			System.out.println(u);
		}
	}*/
	
	@Test
	public void test4(){
		Map<String,Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		//mate.setMateInfo("MHS(11片装)原料");
		map.put("userId", 11);
		map.put("mate", mate);
		List<MateDO> list = mapper.queryMaterialOfUser(map);
		for(MateDO m:list){
			System.out.println(m);
		}
	}
	@Test
	public void test5(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> mateIds = new ArrayList<String>();
		mateIds.add("127bb9610c5e45eb97a786463520d10b");
		mateIds.add("1d403e6d53aa433780192f721bca4106");
		map.put("userId", 11);
		map.put("mateIds", mateIds);
		int i = mapper.deleteMaterialOfUser(map);
		System.out.println(i);
	}
	@Test
	public void test6(){
		Map<String,Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("userId", 1);
		map.put("suppId", "7915d5baf27a4679b035443abedb2305");
		map.put("mate", mate);
		List<MateDO> list = mapper.queryMaterialOfUserAndSupp(map);
		for(MateDO m:list){
			System.out.println(m);
		}
	}
	@Test
	public void test7(){
		Map<String,Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		List<String> mateIds = new ArrayList<String>();
		mateIds.add("8e03273e81414b239b58c486d33a5478");
		map.put("userId", 37);
		map.put("suppId", "s0000100008");
		map.put("mateIds", mateIds);
		int i = mapper.deleteMaterialOfUserAndSupp(map);
		System.out.println(i);
	}
	@Test
	public void test8(){
		UserSuppMate usm = new UserSuppMate();
		usm.setBuyerId(1);
		usm.setSuppId("4e3a8ad8fc044e8b88f96404b416f432");
		usm.setMateId("565c17c19f864413959ac8feacca96e9");
		int i = mapper.addMaterialForUserAndSupp(usm);
		System.out.println(i);
	}
	@Test
	public void test9(){
		UserSuppMate usm = new UserSuppMate();
		usm.setBuyerId(11);
		usm.setMateId("40ec8c42cca548a7bc9c521bf00e320d");
		int i = mapper.addMaterialOfUser(usm);
		System.out.println(i);
	}
	@Test
	public void test10(){
		List<MateDO> list = mapper.queryAllMaterialOfSupp("4e3a8ad8fc044e8b88f96404b416f432");
		for(MateDO m:list){
			System.out.println(m);
		}
	}
	@Test
	public void test11(){
		List<MateDO> list = mapper.queryAllMaterial();
		for(MateDO m:list){
			System.out.println(m);
		}
	}
	@Test
	public void test12(){
		List<MateDO> list = mapper.queryAllMaterialOfSuppBySapId("1");
		for(MateDO m :list){
			System.out.println(m);
		}
	}
	
	/*
	 *获取编码 
	 */
	@Test
	public void test13(){
		String string = codeService.getCodeByCodeType("suppNo");
		System.out.println(string);
	}
	@Test
	public void test14(){
		List<MateUnit> list = mapper.queryMateUnitOfMaterialByMateId("0402.90.370");
		for(MateUnit m :list){
			System.out.println(m);
		}
	}
	@Test
	public void test15(){
		Material mate = mapper.queryMaterialByMateCode("000000000012000030");
		System.out.println(mate);
	}
	@Test
	public void test16(){
		List<Material> list = mapper.queryManyMaterialByMateCode("000000000012000000");
		System.out.println(list.get(0));
		
	}
	@Test
	public void test17(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 5);
		map.put("userId", "1");
		map.put("mate", mate);
		List<MateDO> mate2 = mapper.queryMaterialOfUser(map );
		for (MateDO m : mate2) {
			System.out.println(m);
		}
		int count = mapper.queryMaterialOfUserCount(map);
		System.out.println(count);
		
	}
	@Test
	public void test18(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 10);
		map.put("sapId", "0000100001");
		map.put("mate", mate);
		List<MateDO> list = mapper.queryAllMaterialOfSuppBySapIdByPage(map);
		for (MateDO m : list) {
			System.out.println(m);
		}
		int count = mapper.queryAllMaterialOfSuppBySapIdByPageCount(map);
		System.out.println(count);
		
	}
	@Test
	public void test19(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		Material mate = new Material();
		map.put("start", 1);
		map.put("end", 10);
		map.put("sapId", "0000100001");
		map.put("mate", mate);
		List<MateDO> list = mapper.queryMateOfSuppMateConfig(map);
		for (MateDO m : list) {
			System.out.println(m);
		}
		
		int i = mapper.queryMateOfSuppMateConfigCount(map);
		System.out.println(i);
		
	}
	
	
	@Test
	public void test20(){
		List<MateDO> list = mapper.queryAllMaterialListOfUser("37");
		int count=0;
		for (MateDO mateDO : list) {
			count++;
			System.out.println(mateDO);
		}
		System.out.println(count);
	}
	
	
	
	
	
	
}
