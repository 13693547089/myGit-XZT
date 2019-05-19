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

import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderEnclosure;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.bam.ReceMate;
import com.faujor.service.bam.OrderService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest
{

	@Autowired
	private OrderMapper mapper;
	@Autowired
	private OrderService servcie;

	@Test
	public void testQuery()
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 1);
		map.put("end", 5);
		final List<OrderRele> list = mapper.queryReleaseListByPage(map);
		final int i = mapper.queryReleaseListByPageCount(map);
		System.out.println(i);
		for (final OrderRele o : list)
		{
			System.out.println(o);
		}
	}

	@Test
	public void testQuery2()
	{

		final OrderRele order = servcie.queryOrderReleByFid("12345");
		System.out.println(order);
		final List<OrderMate> list = order.getMates();
		for (final OrderMate o : list)
		{
			System.out.println(o);
		}
		System.out.println("99999999999999999999");
//		List<OrderEnclosure> list2 = order.getEnclosure();
//		for (OrderEnclosure o : list2)
//		{
//			System.out.println(o);
//		}

	}

	@Test
	public void testInsert()
	{

		OrderEnclosure orderE = new OrderEnclosure();
		orderE.setAppeType("订单");
		orderE.setMainId("12345");
		orderE.setAppeFile("haha");
		orderE.setAppeName("heihei");
		orderE.setFid("789654");
		mapper.insertOrder(orderE);

	}
	@Test
	public void testquery2()
	{
		List<OrderRele> list = mapper.queryOrderReleOfQualSupp("0000001000");
		for(OrderRele o:list){
			List<OrderMate> mates = o.getMates();
			System.out.println(o);
			for(OrderMate m:mates){
				System.out.println(m);
			}
		}
		
	}
	@Test
	public void testquery3()
	{
		List<OrderMate> list = mapper.queryOrderMateByContOrdeNumb("4600000013");
		for (OrderMate o : list) {
			System.out.println(o);
		}
		
	}
	@Test
	public void testquery4()
	{
		//OrderRele o = mapper.querycontOrdeNumbOfOrderReleBySapIdAndMateCode("0000100312", "90.03.07.259");
		/*System.out.println(o);
		List<OrderMate> list = o.getMates();
		for (OrderMate m : list) {
			System.out.println(m);
		}*/
		
	}
	@Test
	public void testquery5()
	{
		List<OrderDO> list = mapper.queryAllOrderOfMate("0000100312", "90.03.07.259");
		for (OrderDO o : list) {
			System.out.println(o);
		}
	}
	@Test
	public void testquery6()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 1);
		map.put("end", 10);
		map.put("sapId", "DS15");
		OrderReleDO orDO = new OrderReleDO();
		map.put("orderRele", orDO);
		List<OrderRele> list = mapper.queryOrderReleOfQualSuppByPage(map);
		int i = mapper.queryOrderReleOfQualSuppByPageCount(map);
		System.out.println(i);
		for (final OrderRele o : list)
		{
			System.out.println(o);
		}
	}
	
	/*@Test
	public void test8(){
		List<ReceMate> list = new ArrayList<ReceMate>();
		ReceMate receMate1 = new ReceMate();
		receMate1.setOrderId("4500133942");
		receMate1.setMateCode("000000000020001195");
		receMate1.setFrequency("00020");
		receMate1.setReceNumber(100);
		list.add(receMate1);
		ReceMate receMate2= new ReceMate();
		receMate2.setOrderId("4500133942");
		receMate2.setMateCode("000000000020000595");
		receMate2.setFrequency("00010");
		receMate2.setReceNumber(500);
		list.add(receMate2);
		boolean b = servcie.updateLineProject(list );
		System.out.println(b);
	}
	*/
	@Test
	public void teste9(){
		ReceMate receMate1 = new ReceMate();
		receMate1.setOrderId("4500133942");
		receMate1.setMateCode("000000000020001195");
		receMate1.setFrequency("00020");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("stra", "1000");
		map.put("strb", "1000");
		map.put("r", receMate1);
		int i = mapper.updateLineProject(map );
		System.out.println(i);
	}
}
