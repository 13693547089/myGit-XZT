package com.faujor.service.fam.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.fam.ContactsMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.fam.ContactsAtta;
import com.faujor.entity.fam.ContactsMain;
import com.faujor.entity.fam.ContactsMone;
import com.faujor.entity.fam.ContactsMones;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.fam.intercourseService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.UserCommon;


@Service(value = "intercourseService")
public class intercourseServiceImpl implements intercourseService
{
	@Autowired
	private ContactsMapper ContactsMapper;
	@Autowired
	private QualSuppMapper QualSuppMapper;
	@Autowired
	private TaskService taskService;

	@Override
	public boolean insertContactsMain(ContactsMain conMain, List<ContactsMone> list, List<ContactsMones> list2)
	{
		int i = ContactsMapper.insertContactsMain(conMain);
		int count1 = 0;
		int count2 = 0;
		for (ContactsMone cm : list)
		{
			int j = 0;
			cm.setMainID(conMain.getFid());
			j = ContactsMapper.insertContactsMone(cm);
			count1 += j;
		}
		for (ContactsMones cms : list2)
		{
			int k = 0;
			cms.setMainID(conMain.getFid());
			k = ContactsMapper.insertContactMones(cms);
			count2 += k;
		}
		if (i == 1 && count1 == list.size() && count2 == list2.size())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Map<String, Object> queryReconciliationList(Map<String, Object> map, int offset, Integer limit)
	{
		RowBounds rb = new RowBounds(offset, limit);
		List<ContactsMain> contactsMain = ContactsMapper.queryContactsMain(map, rb);
		final int count = ContactsMapper.contactsListByPageCount(map);
		final Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", contactsMain);
		page.put("count", count);
		page.put("code", 0);
		page.put("msg", "");
		return page;

	}

	/**
	 *
	 *
	 * 往来确认
	 *
	 */
	@Override
	public Map<String, Object> queryReconciliationconfirmList(Map<String, Object> map, int offset, Integer limit)
	{
		RowBounds rb = new RowBounds(offset, limit);
		SysUserDO user = UserCommon.getUser();
		String suppCode = user.getSuppNo();
		map.put("supplierCode", suppCode);
		List<ContactsMain> contactsMain = ContactsMapper.queryContactsConfirmMain(map, rb);
		final int count = ContactsMapper.contactsListConfirmCount(map);
		final Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", contactsMain);
		page.put("count", count);
		page.put("code", 0);
		page.put("msg", "");
		return page;

	}

	@Override
	public ContactsMain getReconciliationDetailList(String fid)
	{

		ContactsMain Contacts = ContactsMapper.getReconciliationDetailList(fid);
		return Contacts;
	}

	@Override
	public boolean deleteReconciliationByFid(String[] fids)
	{
		int a = ContactsMapper.deleteContactsMoneByFid(fids);
		int b = ContactsMapper.deleteContactsMonesByFid(fids);
		int i = ContactsMapper.deleteReconciliationByFid(fids);
		if (i == fids.length)
		{
			TaskParamsDO params = new TaskParamsDO();
			for (int k = 0; k < fids.length; k++)
			{
				params.setSdata1(fids[k]);
				taskService.removeTaskBySdata1(params);
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean setConfirmList(String fid)
	{
		ContactsMapper.setConfirmList(fid);
		return true;
	}

	/**
	 *
	 * 批量提交
	 *
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean resetReconciliationByFid(String[] fids)
	{
		int a = ContactsMapper.resetReconciliationByFid(fids);
		return true;

	}

	/*
	 *
	 * 财务往来确认提交
	 *
	 */
	@Override
	public boolean financialConfirmation(String[] fids)
	{
		int a = ContactsMapper.financialConfirmation(fids);
		return true;

	}

	/**
	 *
	 * 获取对账金额MONE
	 */
	@Override
	public List<ContactsMone> getReconciliationDetailListMone(String Fid)
	{

		List<ContactsMone> Contacts = ContactsMapper.getReconciliationDetailListMone(Fid);
		return Contacts;
	}

	/**
	 *
	 * 获取对账金额MONES
	 *
	 */
	@Override
	public List<ContactsMones> getReconciliationDetailListMones(String Fid)
	{

		List<ContactsMones> Contacts = ContactsMapper.getReconciliationDetailListMones(Fid);
		return Contacts;
	}

	/**
	 * 获取附件
	 *
	 */
	@Override
	public List<ContactsAtta> getqueryAttr(String fid)
	{
		List<ContactsAtta> Atta = ContactsMapper.getqueryAttr(fid);
		return Atta;
	}

	/**
	 * 清除字表
	 *
	 */
	public void deleteContactsMone(String Fid)
	{
		ContactsMapper.deleteContactsMone(Fid);

	}

	public void deleteContactsMones(String Fid)
	{
		ContactsMapper.deleteContactsMones(Fid);

	}

	public void deleteContactsMain(String Fid)
	{

		ContactsMapper.deleteContactsMain(Fid);
	}

	@Override
	public boolean confirmReturn(String Fid, String Textarea, String Radio)
	{
		ContactsMapper.confirmReturn(Fid, Textarea, Radio);
		return true;
	}

	@Override
	public boolean updateContactsMain(ContactsMain conMain, List<ContactsMone> list, List<ContactsMones> list2)
	{
		String suppName = conMain.getSuppName();
		if (suppName == null || suppName == "")
		{
			QualSupp supp = QualSuppMapper.queryQualSuppBySapId(conMain.getSuppNumb());
			conMain.setSuppName(supp.getSuppName());
		}
		;
		int i = ContactsMapper.updateContactsMain(conMain);
		ContactsMapper.deleteContactsMone(conMain.getFid());
		ContactsMapper.deleteContactsMones(conMain.getFid());
		int count1 = 0;
		int count2 = 0;
		for (ContactsMone cm : list)
		{
			int j = 0;
			cm.setMainID(conMain.getFid());
			j = ContactsMapper.insertContactsMone(cm);
			count1 += j;
		}
		for (ContactsMones cms : list2)
		{
			int k = 0;
			cms.setMainID(conMain.getFid());
			k = ContactsMapper.insertContactMones(cms);
			count2 += k;
		}
		if (i == 1 && count1 == list.size() && count2 == list2.size())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean ConfirmContactsMain(ContactsMain conMain, List<ContactsAtta> list3)
	{
		int i = ContactsMapper.updateContactsMain2(conMain);
		int count = 0;
		for (ContactsAtta att : list3)
		{
			int k = 0;
			att.setMainId(conMain.getFid());
			k = ContactsMapper.insertContactAtta(att);
			count += k;
		}
		if (i == 1 && count == list3.size())
		{
			return true;
		}
		else
		{
			return false;
		}
	}


}
