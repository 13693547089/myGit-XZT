package com.faujor.dao.master.fam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.fam.ContactsAtta;
import com.faujor.entity.fam.ContactsMain;
import com.faujor.entity.fam.ContactsMone;
import com.faujor.entity.fam.ContactsMones;



public interface ContactsMapper
{

	/**
	 * 添加往来对账
	 *
	 * @param conMain
	 * @return
	 */
	public int insertContactsMain(ContactsMain conMain);

	public int insertContactsMone(ContactsMone conMone);

	public int insertContactMones(ContactsMones conMones);

	public int insertContactAtta(ContactsAtta Stta);

	public List<ContactsMain> queryContactsMain(Map<String, Object> map, RowBounds rb);

	public List<ContactsMain> queryContactsConfirmMain(Map<String, Object> map, RowBounds rb);

	public ContactsMain getReconciliationDetailList(String fid);


	public ContactsMain getReconciliationList(String Fids);

	/**
	 * @param fid
	 */
	public void setConfirmList(String fid);

	/**
	 * 根据对账单主键删除对账单
	 *
	 * @param fids
	 * @return
	 */
	public int deleteReconciliationByFid(String[] fids);

	/**
	 * 根据对账单的主键删除对账单下的对账分支
	 *
	 * @param fids
	 * @return
	 */
	public int deleteContactsMonesByFid(String[] fids);

	/**
	 * 根据对账单的主键删除对账单下的对账金额
	 *
	 * @param fids
	 * @return
	 */
	public int deleteContactsMoneByFid(String[] fids);

	/**
	 *
	 * 批量提交
	 *
	 */
	public int resetReconciliationByFid(String[] fids);

	/**
	 *
	 *
	 * 财务往来提交
	 */
	public int financialConfirmation(String[] fids);

	/**
	 *
	 *
	 * 获取对账金额mone
	 */
	public List<ContactsMone> getReconciliationDetailListMone(String Fid);

	/**
	 *
	 *
	 * 获取对账金额mones
	 */
	public List<ContactsMones> getReconciliationDetailListMones(String Fid);

	/**
	 *
	 * 获取杜建
	 *
	 */
	public List<ContactsAtta> getqueryAttr(String fid);

	/**
	 *
	 * 清除字表
	 *
	 */
	public void deleteContactsMone(String Fid);

	public void deleteContactsMones(String Fid);

	public void deleteContactsMain(String Fid);

	/**
	 *
	 * 根据供应商ID查询供应商名称
	 *
	 */
	String SelectSuppName(String SuppId);


	/**
	 *
	 * 分页展示
	 *
	 */
	public int contactsListByPageCount(Map<String, Object> map);

	public int contactsListConfirmCount(Map<String, Object> map);

	public void confirmReturn(@Param("Fid") String Fid, @Param("Textarea") String Textarea, @Param("Radio") String Radio);
	/**
	 * 修改财务往来对账
	 * @param conMain
	 * @return
	 */
	public int updateContactsMain(ContactsMain conMain);
	/**
	 * 只修改财务往来对账主数据的两个字段
	 * @param conMain
	 * @return
	 */
	public int updateContactsMain2(ContactsMain conMain);

}
