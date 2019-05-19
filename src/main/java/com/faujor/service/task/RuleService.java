package com.faujor.service.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.CutLiaisonMapper;
import com.faujor.dao.master.bam.QuoteMapper;
import com.faujor.dao.master.bam.QuoteMateMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.dao.master.task.TaskMapper;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.Quote;
import com.faujor.entity.bam.QuoteMate;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskDO;

@Service("ruleService")
public class RuleService {

	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CutLiaisonMapper cutLiaisonMapper;
	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private QuoteMapper quoteMapper;
	@Autowired
	private QuoteMateMapper quoteMateMapper;

	public String getTest() {
		return "reflect";
	}

	public boolean condition1(String sdta1) {
		return true;
	}

	public boolean testCondition(String sdata1) {

		return false;
	}

	/**
	 * 获取首节点的创建人
	 * 
	 * @param sdata1
	 * @return
	 */
	public List<SysUserDO> getFirstNodeCreator(String sdata1) {
		List<TaskDO> taskList = taskMapper.findTaskHisBySdata1(sdata1);
		List<SysUserDO> list = new ArrayList<SysUserDO>();
		if (taskList.size() > 0) {
			TaskDO taskDO = taskList.get(0);
			long creator = taskDO.getCreator();
			SysUserDO userDO = userMapper.findUserById(creator);
			list.add(userDO);
		}
		return list;
	}
	
	/**
	 * 打切联络单任务配置规则
	 * 获取货源配置中负责这个供应商中的所有采购员
	 * @param sdata1
	 * @return
	 */
	public List<SysUserDO> getUserListOfSupp(String sdata1){
		CutLiaison cutLiai = cutLiaisonMapper.queryCutLiaisonByLiaiId(sdata1);
		List<String> userIdList = qualSuppMapper.getUserIdsOfSuppBySuppId(cutLiai.getSuppId());
		List<SysUserDO> list = new ArrayList<SysUserDO>();
		for (String userId : userIdList) {
			long id = Long.parseLong(userId);
			SysUserDO userDO = userMapper.findUserById(id);
			if(userDO != null){
				list.add(userDO);
			}
		}
		return list;
	}
	/**
	 * 报价单任务配置规则
	 * 获取货源配置中这个供应商和物料所对应的采购员
	 * @param sdata1
	 * @return
	 */
	public List<SysUserDO> getUserIdsOfSuppAndMate(String sdata1){
		Quote quote = quoteMapper.getQuoteById(sdata1);
		List<QuoteMate> mateList = quoteMateMapper.getAllQuoteMateByQuoteCode(quote.getQuoteCode());
		List<SysUserDO> list = new ArrayList<SysUserDO>();
		if(mateList.size()>0){
			QuoteMate quoteMate = mateList.get(0);
			List<String> userIdList = qualSuppMapper.getUserIdsBySapIdAndMateId(quote.getSuppNo(), quoteMate.getMateNo());
			for (String userId : userIdList) {
				long id = Long.parseLong(userId);
				SysUserDO userDO = userMapper.findUserById(id);
				if(userDO != null){
					list.add(userDO);
				}
			}
		}
		return list;
	}
}
