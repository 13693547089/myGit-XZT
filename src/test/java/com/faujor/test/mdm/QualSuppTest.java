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

import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.UserSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.mdm.QualSuppService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QualSuppTest {

	@Autowired
	private QualSuppMapper mapper;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private UserMapper userMapper;;
	
	
	
	
	@Test
	public void testInsert(){
		QualSupp supp = new QualSupp();
		supp.setSapId("333333");
		supp.setSrmId("77777");
		supp.setOaId("555555");
		supp.setCategoryId("GNCS");
		supp.setCategory("国内厂商");
		supp.setSubGroup("国内厂商");
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
		supp.setBusiLicense("营业执照");
		supp.setClientId("客户编码");
		supp.setCompCode("公司代码");
		supp.setAkont("总帐中的统驭科目");
		supp.setProGroup("计划组");
		supp.setStatus("合格");
		supp.setRegisterTime(new Date());
		supp.setAuditTime(new Date());
		supp.setAuditStatus("oa审核状态");
		int i = mapper.insertQualSupp(supp);
		System.out.println(i);
	}
	
	@Test
	public void queryByPage(){
		QualSupp supp = new QualSupp();
		supp.setSuppInfo("酸奶");
		//supp.setCategory("国内厂商");
		supp.setProvcity("福建");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qualSupp", supp);
		map.put("start", 1);
		map.put("end", 10);
		map.put("userId", 1);
		List<QualSupp> list = mapper.queryQualSuppByPage(map);
		for(QualSupp qs:list){
			System.out.println(qs);
		}
	}
	
	@Test
	public void queryByPage2(){
		QualSupp supp = new QualSupp();
		supp.setSuppInfo("酸奶");
		//supp.setCategory("国内厂商");
		supp.setProvcity("福建");
		Map<String,Object>  map = new HashMap<String,Object>();
		map.put("qualSupp", supp);
		map.put("start", 1);
		map.put("end", 10);
		map.put("userId", 1);
		Map<String, Object> page = qualSuppService.queryQualSuppByPage(map);
		List<QualSupp> list= (List<QualSupp>) page.get("data");
		for(QualSupp qs:list){
			System.out.println(qs);
		}
		int  i = Integer.parseInt((String) page.get("count"));
		System.out.println(i);
	}
	
	@Test
	public void test3(){
		
		String suppId="4e3a8ad8fc044e8b88f96404b416f432";
		QualSupp supp = mapper.queryOneQualSuppBySuppId(suppId);
		System.out.println(supp);
	}
	
	@Test
	public void test4(){
		
		/*List<QualSupp> list = mapper.queryQualSuppByUserId(9);
		for(QualSupp s:list){
			System.out.println(s);
		}*/
	}
	@Test
	public void test5(){
		Map<String, Object> param  = new HashMap<String,Object>();
		List<SysUserDO> list = userMapper.list(param);
		for(SysUserDO d:list){
			System.out.println(d.toString());
		}
	}
	@Test
	public void test6(){
		Map<String, Object> param  = new HashMap<String,Object>();
		int i = mapper.deleteQualSuppOfUser(1,"765359056e7844b1ab5df3f279b0a0e9");
		System.out.println(i);
		
	}
	@Test
	public void test7(){
		UserSupp us  = new UserSupp();
		us.setBuyerId(11);
		us.setSuppId("53db6f9ca07f4b91af8f0c59a74e3db3");
		int i = mapper.addQualSuppForUser(us);
		System.out.println(i);
	}
	@Test
	public void test8(){
		QualSupp supp = mapper.queryQualSuppBySuppName("安徽安宇乳胶制品有限公司");
		System.out.println(supp);
	}
	@Test
	public void test9(){
		QualSupp supp = mapper.queryQualSuppBySapId("1");
		System.out.println(supp);
	}
	/*@Test
	public void test10(){
		List<QualSupp> list = mapper.queryQualSuppOfMateByMateCode("000000000012000025");
		for(QualSupp s:list){
			System.out.println(s);
		}
	}*/
	@Test
	public void test11(){
		String str = mapper.queryByuerIdBySapIdAndMateCode("1", "000000000012000025");
		System.out.println(str);
	}
	@Test
	public void test12(){
		List<UserDO> users = new ArrayList<UserDO>();
		UserDO u = new UserDO();
		u.setId(1);
		users.add(u);
		List<String> list = mapper.findSuppCodesByUsers(users );
		for (String s : list) {
			System.out.println(s);
		}
	}
	@Test
	public void test13(){
		Map<String, Object> map = new HashMap<String,Object>();
		QualSupp qualSupp = new QualSupp();
		map.put("start", 1);
		map.put("end", 10);
		map.put("qualSupp", qualSupp);
		List<QualSupp> list = mapper.queryAllQualSuppByPage(map );
		for (QualSupp s : list) {
			System.out.println(s);
		}
		int i = mapper.queryAllQualSuppByPageCount(map);
		System.out.println(i);
	}
	
	@Test
	public void test14(){
		String str = mapper.queryBuyerIdBySuppIdAndMateId("s0000900009", "cb61b94c302844ec83472c09dd685f72");
		System.out.println(str);
	}
	@Test
	public void test15(){
		List<QualSupp> list = mapper.queryAllQualSuppListByUserId("37");
		for (QualSupp qualSupp : list) {
			System.out.println(qualSupp);
		}
	}
	
	
	
}
