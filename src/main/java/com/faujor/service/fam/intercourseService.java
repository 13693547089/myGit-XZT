package com.faujor.service.fam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.fam.ContactsAtta;
import com.faujor.entity.fam.ContactsMain;
import com.faujor.entity.fam.ContactsMone;
import com.faujor.entity.fam.ContactsMones;


public interface intercourseService
{

	/**
	 * 保存订单
	 *
	 * @param conMain
	 * @param list
	 * @return
	 */
	/*boolean insertContactsMain(ContactsMain conMain, List<ContactsMone> list, List<ContactsMones> list2, List<ContactsAtta> list3,
			String SuppId);*/
	boolean insertContactsMain(ContactsMain conMain, List<ContactsMone> list, List<ContactsMones> list2);
	Map<String, Object> queryReconciliationList(Map<String, Object> map, int offset, Integer limit);

	ContactsMain getReconciliationDetailList(String fid);

	/**
	 * 删除对账单
	 *
	 * @param fids
	 * @return
	 */
	boolean deleteReconciliationByFid(String[] fids);

	public boolean setConfirmList(String fid);

	/**
	 *
	 *
	 * 批量提交
	 *
	 */
	boolean resetReconciliationByFid(String[] fids);

	/*
	 *
	 * 财务往来确认提交
	 *
	 */
	boolean financialConfirmation(String[] fids);

	/**
	 * 获取对账金额mone
	 *
	 *
	 */
	List<ContactsMone> getReconciliationDetailListMone(String fid);

	/**
	 *
	 * 获取对账金额MONES
	 *
	 */
	List<ContactsMones> getReconciliationDetailListMones(String fid);

	/**
	 *
	 * 获取附件
	 *
	 */
	List<ContactsAtta> getqueryAttr(String fid);

	/**
	 *
	 * 清除字表
	 *
	 */
	void deleteContactsMone(String Fid);

	void deleteContactsMones(String Fid);

	void deleteContactsMain(String Fid);

	boolean confirmReturn(String Fid, String Textarea, String Radio);

	/**
	 *
	 * 获取往来确认页面数据
	 *
	 */
	Map<String, Object> queryReconciliationconfirmList(Map<String, Object> map, int offset, Integer limit);
	
	boolean updateContactsMain(ContactsMain conMain, List<ContactsMone> list, List<ContactsMones> list2);
	/**
	 * 确认提交财务往来对账
	 * @param conMain
	 * @param list3
	 * @return
	 */
	boolean ConfirmContactsMain(ContactsMain conMain, List<ContactsAtta> list3);
}

