package com.faujor.test.demo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.entity.mdm.Material;
import com.faujor.service.demo.PaginationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {
	@Autowired
	private PaginationService ps;

	@Test
	public void rowTest() {
		List<Material> list = ps.materialList();
	}
	
	@Test
	public void TestCalendar(){
		
		
	}
	
	public static void main(String[] args) {
//		Calendar calendar=Calendar.getInstance();
//		System.out.println(calendar.get(Calendar.DATE));
//		System.out.println();
		
		BigDecimal bg1=new BigDecimal(1000);
		BigDecimal bg2=new BigDecimal(100);
		System.out.println(bg1.subtract(bg2));
	}
}
