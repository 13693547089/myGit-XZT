package com.faujor.service.sapcenter.bam.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.sapcenter.bam.SapPdrMapper;
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.service.sapcenter.bam.SapPdrService;

/**
 * sap 产能上报实现类
 * @author Vincent
 *
 */
@Service
public class SapPdrServiceImpl implements SapPdrService{
	
	@Autowired
	private SapPdrMapper sapPdrMapper;
	
	/**
	 * 保存 产能上报 信息
	 * @param pdr
	 * @param detailList
	 * @param itemList
	 * @return
	 */
	@Override
	@Transactional
	public int saveSapPdrInfo(Pdr pdr, List<PdrDetail> detailList, List<PdrItem> itemList)  throws Exception{
		// 删除
		sapPdrMapper.delPdrDetailByMainId(pdr.getId());
		sapPdrMapper.delPdrItemByMainId(pdr.getId());
		sapPdrMapper.delPdrById(pdr.getId());
		
		if(detailList.size()>0){
			// 保存明细
			sapPdrMapper.savePdrDetailList(detailList);
		}
		
		if(itemList.size()>0){
			sapPdrMapper.savePdrItemList(itemList);
		}
		
		sapPdrMapper.savePdr(pdr);
		
		return 1;
	}
	
	/**
	 * 删除产能上报信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void delSapPdrInfo(String id) throws Exception{
		sapPdrMapper.delPdrDetailByMainId(id);
		sapPdrMapper.delPdrItemByMainId(id);
		sapPdrMapper.delPdrById(id);
	}
}
