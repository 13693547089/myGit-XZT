package com.faujor.test.mdm;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.mdm.QualPapersMapper;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualProc;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QualPapersTest {

	@Autowired
	private QualPapersMapper mapper;
	
	@Test
	public void insertPaper(){
		QualPapers qualPapers = new QualPapers();
		qualPapers.setPapersName("1");
		qualPapers.setPapersType("2");
		qualPapers.setStartDate(new Date());
		qualPapers.setEndDate(new Date());
		qualPapers.setAcceOldName("33");
		int i = mapper.insertQualPapers(qualPapers);
		System.out.println(i);
	}
	@Test
	public void test2(){
		List<QualPapers> list = mapper.queryQualPapersBySuppId("4e3a8ad8fc044e8b88f96404b416f432");
		for(QualPapers p :list){
			System.out.println(p);
		}
	}
	@Test
	public void test3(){
		List<QualProc> list = mapper.queryQualProcBySuppId("4e3a8ad8fc044e8b88f96404b416f432");
		for(QualProc p :list){
			System.out.println(p);
		}
	}
	
	
}
