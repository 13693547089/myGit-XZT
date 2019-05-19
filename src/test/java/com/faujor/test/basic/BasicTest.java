package com.faujor.test.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.basic.BasicMapper;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.basic.DicCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicTest {
	@Autowired
	private BasicMapper bm;

	@Test
	public void findDicCategoryByParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		DicCategory cate = new DicCategory();
		params.put("category", cate);
		List<DicCategory> list = bm.findDicCategoryByParams(params);
		int count = bm.countDicCategoryByParams(params);
	}

	@Test
	public void findDicByCodeParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Dic> list = bm.findDicByCodeParams(params);
		for (Dic dic : list) {
			System.out.println(dic.getId());
		}
	}
}
