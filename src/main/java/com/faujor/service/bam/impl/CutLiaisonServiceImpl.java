package com.faujor.service.bam.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.CutLiaisonForBaoCaiMapper;
import com.faujor.dao.master.bam.CutLiaisonMapper;
import com.faujor.dao.master.bam.CutProductMapper;
import com.faujor.dao.master.bam.OrderMonthMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.bam.CutBaoCaiMate;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.ProdMateDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutLiaisonService;
import com.faujor.service.bam.CutPlanService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.bam.PdrService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

import jcifs.dcerpc.msrpc.netdfs;
import oracle.sql.BLOB;
@Service(value="CutLiaisonService")
public class CutLiaisonServiceImpl implements CutLiaisonService {

	@Autowired
	private CutLiaisonMapper cutLiaisonMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private CutProductMapper cutProductMapper;
	@Autowired
	private OrderMonthMapper orderMonthMapper;
	@Autowired
	private PdrService pdrService;
	@Autowired
	private CutProductService cutProductService;
	@Autowired
	private CutLiaisonForBaoCaiService cutLiaisonForBaoCaiService;
	
	@Override
	public Map<String, Object> queryCurLiaisonByPage(Map<String, Object> map) {
		 Map<String, Object> page = new HashMap<String,Object>();
		 List<CutLiaison> list = cutLiaisonMapper.queryCutLiaisonByPage(map);
		 int count = cutLiaisonMapper.queryCutLiaisonByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean addCutLiaison(CutLiaison cutLiai, List<CutLiaiMate> list) {
		List<CutLiaiMate> list2 = getMainStruNum(cutLiai,list);
		int i = cutLiaisonMapper.addCutLiaison(cutLiai);
		insertFieldsBlob(cutLiai);
		int count=0;
		for(CutLiaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addCutLiaiMate(clm);
			count+=j;
		}
		if(i==1 && count==list2.size()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将物料数据的表头字符串信息存入到数据库的blob数据类型的fields_blob字段中
	 * @param cutLiai
	 */
	public void insertFieldsBlob(CutLiaison cutLiai){
		CutLiaison cl = cutLiaisonMapper.queryCutLiaiFieldsBlobByLiaiId(cutLiai.getLiaiId());
		BLOB fieldsBlob = (BLOB) cl.getFieldsBlob();
		String headerFields = cutLiai.getFields();
		FileInputStream fis = null;  
	    OutputStream ops = null;  
	    try {  
	        //ops = blobColumn.getBinaryOutputStream();//暂时使用这个废弃的方法   
	        ops = fieldsBlob.setBinaryStream(0);//ojdbc14支持,ojdbc6,5都不支持 
	        //fis = new FileInputStream(file);  
	        byte[] byteArray = null; 
	        byteArray  = headerFields.getBytes();
	        //data = FileCopyUtils.copyToByteArray(fis);  
	        ops.write(byteArray);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if(fis!=null){  
	                fis.close();  
	            }  
	            if(ops!=null){  
	                ops.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    } 
	}
	/**
	 * 获取打切物料的主包材数
	 * @param cutLiai
	 * @param list
	 * @return
	 */
	private List<CutLiaiMate> getMainStruNum(CutLiaison cutLiai, List<CutLiaiMate> list) {
		Set<CutLiaiField> set  = new HashSet<CutLiaiField>();
		String Headerfields = cutLiai.getFields();
		JSONArray headerJA = JSONArray.parseArray(Headerfields);
		for(int i= 0;i<headerJA.size();i++){
			CutLiaiField field = headerJA.getObject(i, CutLiaiField.class);
			set.add(field);
		}
		for(CutLiaiMate mate :list){
			String fields = mate.getFields();
			String mainStru = mate.getMainStru();
			int indexOf = mainStru.indexOf(" ");
			String className = mainStru.substring(indexOf+1);
			String f = className+"合计";
			String field2="";
			for (CutLiaiField c : set) {
				if(f.equals(c.getTitle())){
					field2 = c.getField();
					break;
				}
			}
			JSONArray jj = JSONArray.parseArray(fields);
			if(jj.size()!=0){
				outer:
					for (int i = 0; i < jj.size(); i++) {
						JSONObject object = jj.getJSONObject(i);
						Set<String> keySet = object.keySet();
						for (String str : keySet) {
							if(field2.equals(str)){
								String object2 = (String) object.get(str);
								if(object2==null || "".equals(object2) || "NaN".equals(object2)){
									object2 ="0";
								}
								Double b = Double.parseDouble(object2);
								mate.setMainStruNum(b);
								break outer;
							}
						}
					}
			}else{
				mate.setMainStruNum(0D);
			}
		}
		
		return list;
	}

	@Override
	@Transactional
	public boolean deleteCutLiaisonByliaiIds(String[] liaiIds) {
		int j = cutLiaisonMapper.deleteCutLiaiMateByLiaiIds(liaiIds);
		int i = cutLiaisonMapper.deleteCutLiaisonByliaiIds(liaiIds);
		if(i==liaiIds.length){
			TaskParamsDO params = new TaskParamsDO();
			for(int k =0;k<liaiIds.length;k++){
				params.setSdata1(liaiIds[k]);
				taskService.removeTaskBySdata1(params );
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateCutLiaiStatusByliaiIds(Map<String,Object> map) {
		int i = cutLiaisonMapper.updateCutLiaiStatusByliaiIds(map);
		int k = (int) map.get("size");
		if(i==k){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public CutLiaison queryCutLiaisonByLiaiId(String liaiId) {
		return cutLiaisonMapper.queryCutLiaisonByLiaiId(liaiId);
	}

	@Override
	public Map<String, Object> getContactSheet(String liaiId) {
		List<CutLiaiMate> list = cutLiaisonMapper.queryCutLiaiMateByLiaiId(liaiId);
		JSONArray ja = new JSONArray();
		for (CutLiaiMate clm : list) {
			JSONObject jo = new JSONObject();
			jo.put("mateCode",clm.getMateCode());
			jo.put("id",clm.getLiaiMateId());
			jo.put("mateName", clm.getMateName());
			jo.put("seriesExpl", clm.getSeriesExpl());
			jo.put("outNum", clm.getOutNum());
			jo.put("inveNum", clm.getInveNum());
			Integer prodNum = clm.getProdNum();
			if(prodNum == null) {
				prodNum = 0;
			}
			Integer lastProdNum = clm.getLastProdNum();
			if(lastProdNum == null) {
				lastProdNum = 0;
			}
			if(prodNum > lastProdNum) {
				jo.put("prodNum", "<span style='color:red;'>"+clm.getProdNum()+"</span>");
			}else {
				jo.put("prodNum", clm.getProdNum());
			}
			jo.put("lastProdNum", clm.getLastProdNum());
			jo.put("boxNumber", clm.getBoxNumber());
			jo.put("version", clm.getVersion());
			jo.put("cutAim", clm.getCutAim());
			jo.put("mainStru", clm.getMainStru());
			jo.put("sumOutNum", clm.getSumOutNum());
			jo.put("sumInveNum", clm.getSumInveNum());
			jo.put("sumProdNum", clm.getSumProdNum());
			jo.put("remark", clm.getRemark());
			String fields = clm.getFields();
			JSONArray jj = JSONArray.parseArray(fields);
			for (int i = 0; i < jj.size(); i++) {
				JSONObject object = jj.getJSONObject(i);
				Set<String> keySet = object.keySet();
				for (String str : keySet) {
					jo.put(str, object.get(str));
				}
			}
			ja.add(jo);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", ja);
		map.put("count", ja.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public JSONArray queryLiaiMateFields(String liaiId) {
		CutLiaison cutLiai2 = cutLiaisonMapper.queryCutLiaisonByLiaiId(liaiId);
		String fields = cutLiai2.getFields();
		if(!StringUtils.isEmpty(fields)){
			JSONArray jj = JSONArray.parseArray(fields);
			return jj;
		}
		CutLiaison cutLiai = cutLiaisonMapper.queryCutLiaiFieldsBlobByLiaiId(liaiId);
		BLOB fieldsBlob = (BLOB) cutLiai.getFieldsBlob();
		InputStream in = null; 
		try {  
		    in=fieldsBlob.getBinaryStream();  
		} catch (SQLException e) {  
		    e.printStackTrace();  
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
	    byte[] buffer = new byte[8192];
	    int n = 0;
	    try {
			while (-1 != (n = in.read(buffer))) {
			    output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte[] byteArray = output.toByteArray();
//	    String s= "";
//		try {
//			s = new String(byteArray,"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String s = new String(byteArray);
	    JSONArray jj = JSONArray.parseArray(s);
		return jj;
	}

	@Override
	public Map<String, Object> queryCutLiaisonForManageByPage(Map<String, Object> map) {
		 Map<String, Object> page = new HashMap<String,Object>();
		 List<CutLiaison> list = cutLiaisonMapper.queryCutLiaisonForManageByPage(map);
		 int count = cutLiaisonMapper.queryCutLiaisonForManageByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean updateStatusOfCutLiaisonByLiaiId(Map<String, Object> map) {
		int i = cutLiaisonMapper.updateStatusOfCutLiaisonByLiaiId(map);
		int j = (int) map.get("size");
		if(i==j){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> udpateCutLiaiMate(CutLiaison cutLiai, List<CutLiaiMate> list, String type) {
		Map<String,Object> result = new HashMap<String,Object>();
		if("2".equals(type)){
			List<String> liaiIds = new ArrayList<String>();
			liaiIds.add(cutLiai.getLiaiId());
			Map<String,Object> map = new HashMap<String,Object>();
			//map.put("status", "已提交");
			map.put("status", "已保存");
			map.put("liaiIds", liaiIds);
			int i = cutLiaisonMapper.updateStatusOfCutLiaisonByLiaiId(map);
		}
		List<CutLiaiMate> list2 = getMainStruNum(cutLiai,list);
		String[] liaiId = new String[1];
		liaiId[0]=cutLiai.getLiaiId();
		int k = cutLiaisonMapper.deleteCutLiaiMateByLiaiIds(liaiId);
		int count=0;
		for(CutLiaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addCutLiaiMate(clm);
			count+=j;
		}
		if(count==list2.size()){
			result.put("judge", true);
		}else{
			result.put("judge", false);
			result.put("msg", "打切联络单提交失败！");
		}
		return result;
	}

	@Override
	public List<CutLiaison> queryManyCutLiaisonByLiaiIds(List<String> liaiIds) {
		return cutLiaisonMapper.queryManyCutLiaisonByLiaiIds(liaiIds);
	}

	@Override
	public List<CutLiaiMate> queryManyCutLiaiMateByLiaiIds(List<String> liaiIds) {
		return cutLiaisonMapper.queryManyCutLiaiMateByLiaiIds(liaiIds);
	}

	@Override
	public Map<String, Object> queryFinMaterialByPage(Map<String, Object> map) {
		List<MateDO> list = materialMapper.queryFinMaterialByPage(map);
		int count = materialMapper.queryFinMaterialByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public Map<String, Object> querySemiFinMateByMateCode(List<String> mateCodes) {
		Map<String, Object> map = new HashMap<String, Object>();
	    List<MateDO> list= materialMapper.querySemiFinMateByMateCodes(mateCodes);
		int size = list.size();
		if(size > 0){
			map.put("judge", true);
			map.put("list", list);
		}else{
			map.put("judge", false);
			map.put("msg", "未找到对应的半成品物料");
		}
		return map;
	}

	@Override
	public Map<String, Object> querySpeCutLiaisonByPage(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String,Object>();
		List<CutLiaison> list = cutLiaisonMapper.querySpeCutLiaisonByPage(map);
		int count = cutLiaisonMapper.querySpeCutLiaisonByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	public Map<String, Object> querySpecialCutLiaiMate(String cutMonth) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<ProdMateDO> list = cutProductMapper.querySpecialCutLiaiMate();
		if(list.size()>0){
			String str="";
			int count=0;
			for (ProdMateDO p : list) {
				String mainStru = p.getMainStru();
				if(mainStru == null || mainStru == ""){
					str +=p.getMateCode()+" ";
					count ++;
				}
			}
			if(count == 0){
				////通过list 集合中的物料数据查询总订单在外量，总成品库存，总可生产订单（还需要月份）
				list = getCountNum(list,cutMonth,null);
				map.put("judge", true);
				map.put("list", list);
			}else{
				map.put("judge", false);
				map.put("msg", str+"物料没有主包材,请添加后再操作");
			}
		}else{
			map.put("judge", false);
			map.put("msg", "打切品维护列表中没有特殊自制打切品，请维护后再操作。");
		}
		return map;
	}
	/**
	 * 计算总订单在外量(期初订单)，总成品库存(月底结账库存)，总可生产订单（总订单在外量-总成品库存）
	 * @return
	 */
	public List<ProdMateDO> getCountNum(List<ProdMateDO> list,String cutMonth,String suppId){
		Date month = DateUtils.parse(cutMonth, "yyy-MM");
		//QualSupp supp = qualSuppMapper.queryOneQualSuppBySuppId(suppId);
		//List<String> suppNos=new ArrayList<>();
		//suppNos.add(supp.getSapId());
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("suppNos",suppNos );
		params.put("planMonth", month);
		Map<String, Object> map=new HashMap<>();
		//map.put("suppNo", supp.getSapId());
		map.put("planMonth", month);
		for (ProdMateDO pm : list) {
			params.put("mateCode", pm.getMateCode());
			//获取期初订单  一个供应商某月某个物料的期初订单
			Double unpaidNum = orderMonthMapper.selectUndeliveredNumByMap(params);
			//总订单在外量
			pm.setSumOutNum(unpaidNum);
			map.put("mateCode", pm.getMateCode());
			//获取月底结账库存
			Double sumInveNum2 =  pdrService.getSumInveNumByMateDate(map);
			//总成品库存
			double sumInveNum = sumInveNum2;
			pm.setSumInveNum(sumInveNum);
			double sumOutNum = unpaidNum;
			double sumProNum = sumOutNum - sumInveNum < 0 ? 0: sumOutNum - sumInveNum;
			//总可生产订单
			pm.setSumProdNum(sumProNum);
			//上月打切可生产订单
			String lastCutMonth = cutProductService.getLastDate(cutMonth);//获取当前月份上一个月份日期
			Integer lastProdNum = cutProductMapper.getLastProdNumByMateCodeAndCutMonth(pm.getMateCode(),lastCutMonth);
			pm.setLastProdNum(lastProdNum == null ? 0:lastProdNum);
		}
		return list;
	}
	
	

	@Override
	@Transactional
	public boolean updateSpeCutLiaison(CutLiaison cutLiai,List<CutLiaiMate> list) {
		List<CutLiaiMate> list2 = getMainStruNum(cutLiai,list);
		String[] liaiId = new String[1];
		liaiId[0]=cutLiai.getLiaiId();
		int k = cutLiaisonMapper.deleteCutLiaiMateByLiaiIds(liaiId);
		int count=0;
		for(CutLiaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addCutLiaiMate(clm);
			count+=j;
		}
		if(count==list2.size() && k>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateSpeCutLiaison2(CutLiaison cutLiai, List<CutLiaiMate> list) {
		List<String> liaiIds = new ArrayList<String>();
		liaiIds.add(cutLiai.getLiaiId());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", cutLiai.getStatus());
		map.put("liaiIds", liaiIds);
		int i = cutLiaisonMapper.updateStatusOfCutLiaisonByLiaiId(map);
		boolean b = updateSpeCutLiaison(cutLiai,list);
		if(b && i==1){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	@Transactional
	public boolean deleteSpeCutLiaisonByliaiIds(String[] liaiIds) {
		int j = cutLiaisonMapper.deleteCutLiaiMateByLiaiIds(liaiIds);
		int i = cutLiaisonMapper.deleteCutLiaisonByliaiIds(liaiIds);
		if(i==liaiIds.length){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public void updateMainStruNumOfCutLiaiMate(String liaiId) {
		CutLiaison cutLiai = cutLiaisonMapper.queryCutLiaisonByLiaiId(liaiId);
		List<CutLiaiMate> mates = cutLiaisonMapper.queryCutLiaiMateByLiaiId(cutLiai.getLiaiId());
		if(mates.size()>0){
			List<CutLiaiMate> mates2 = getMainStruNum(cutLiai, mates);
			for (CutLiaiMate cm : mates2) {
				cutLiaisonMapper.updateCutLiaiMateMainStruNum(cm);
			}
		}
		
	}

	@Override
	public Map<String, Object> updateCutLiaiMateBySuppAndMonth(String suppId, 
			String cutMonth,String liaiId,String headerFiled) {
		Map<String,Object> map = new HashMap<String,Object>();
		//新的打切联络单物料信息
		List<ProdMateDO> proList = cutProductMapper.queryMatesOfCutLiaiBySuppIdAndCutMonth(suppId, cutMonth);
		for (ProdMateDO m : proList) {
			String mainStru = m.getMainStru();
			if(StringUtils.isEmpty(mainStru)){
				map.put("msg", "存在有的物料没有主包材,请添加后再操作");
				map.put("judge", false);
				return map;
			}
			String version = m.getVersion();
			if("QQQ".equals(version)) {
				m.setVersion("");
			}
		}
		//计算总订单在外量(期初订单)，总成品库存(月底结账库存)，总可生产订单（总订单在外量-总成品库存）
		proList = cutProductService.getCountNum(proList, cutMonth, suppId);
		//旧的打切联络单物料信息
		List<CutLiaiMate> list = cutLiaisonMapper.queryCutLiaiMateByLiaiId(liaiId);
		JSONArray ja = getNewCutMateList2(proList,list,headerFiled);
		map.put("judge", true);
		map.put("data", ja);
		return map;
		
	}

	private JSONArray getNewCutMateList2(List<ProdMateDO> proList, List<CutLiaiMate> list, String headerFiled) {
		//需要添加的物料信息
		List<ProdMateDO> addList = new ArrayList<ProdMateDO>();
		//需要删除的物料信息
		List<CutLiaiMate> removeList = new ArrayList<CutLiaiMate>();
		//获取删除的物料数据
		for(CutLiaiMate cm:list){
			int count=0;
			String cmc = cm.getMateCode();
			String cmv = cm.getVersion()== null ? "" : cm.getVersion();
			for(ProdMateDO pm:proList){
				String pmc = pm.getMateCode();
				String pmv = pm.getVersion() == null ? "" : pm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					cm.setSumOutNum(pm.getSumOutNum());
					cm.setSumInveNum(pm.getSumInveNum());
					cm.setSumProdNum(pm.getSumProdNum());
					cm.setCutAim(pm.getCutAim());
					cm.setMainStru(pm.getMainStru());
				}else{
					count ++;
				}
			}
			if(count == proList.size()){
				removeList.add(cm);
			}
		}
		//获取新增物料数据
		for(ProdMateDO pm:proList){
			int count=0;
			String pmc = pm.getMateCode();
			String pmv = pm.getVersion() == null ? "" : pm.getVersion();
			for(CutLiaiMate cm:list){
				String cmc = cm.getMateCode();
				String cmv = cm.getVersion()== null ? "" : cm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					
				}else{
					count ++;
				}
			}
			if(count == list.size()){
				addList.add(pm);
			}
		}
		proList.removeAll(addList);
		list.removeAll(removeList);
		JSONArray ja = new JSONArray();
		for (CutLiaiMate clm : list) {
			JSONObject jo = new JSONObject();
			jo.put("mateCode",clm.getMateCode());
			jo.put("id",clm.getLiaiMateId());
			jo.put("seriesExpl", clm.getSeriesExpl());
			jo.put("mateName", clm.getMateName());
			jo.put("outNum", clm.getOutNum());
			jo.put("inveNum", clm.getInveNum());
			Integer prodNum = clm.getProdNum();
			if(prodNum == null) {
				prodNum = 0;
			}
			Integer lastProdNum = clm.getLastProdNum();
			if(lastProdNum == null) {
				lastProdNum = 0;
			}
			if(prodNum > lastProdNum) {
				jo.put("prodNum", "<span style='color:red;'>"+clm.getProdNum()+"</span>");
			}else {
				jo.put("prodNum", clm.getProdNum());
			}
			jo.put("lastProdNum", clm.getLastProdNum());
			jo.put("boxNumber", clm.getBoxNumber());
			jo.put("version", clm.getVersion());
			jo.put("cutAim", clm.getCutAim());
			jo.put("mainStru", clm.getMainStru());
			jo.put("sumOutNum", clm.getSumOutNum());
			jo.put("sumInveNum", clm.getSumInveNum());
			jo.put("sumProdNum", clm.getSumProdNum());
			jo.put("remark", clm.getRemark());
			String fields = clm.getFields()== null ? "" : clm.getFields();
			JSONArray jj = JSONArray.parseArray(fields);
			if(jj != null){
				for (int i = 0; i < jj.size(); i++) {
					JSONObject object = jj.getJSONObject(i);
					Set<String> keySet = object.keySet();
					for (String str : keySet) {
						jo.put(str, object.get(str));
					}
				}
			}
			ja.add(jo);
		}
		//获取包材联络单中物料对应的最新数据
		ja = deliWithUpdateOldData(ja,proList,headerFiled);
		if(addList.size()>0) {
			ja = dealWithCiteData(ja,addList,headerFiled);
		}
		return ja;
	}

	private JSONArray deliWithUpdateOldData(JSONArray ja,List<ProdMateDO> proList, String headerFiled) {
		Map<String, List<CutBaoCaiMate>> map = new HashMap<String, List<CutBaoCaiMate>>();
		BigDecimal zero = new BigDecimal(0);
		for (ProdMateDO pm : proList) {
			String mateCode = pm.getMateCode();
			String version = pm.getVersion();
			List<CutBaoCaiMate> mateList = pm.getMateList();
			String key = mateCode +"-"+version;
			map.put(key, mateList);
		}
		
		for(int h=0;h<ja.size();h++) {
			JSONObject object = ja.getJSONObject(h);
			//物料编码
			String mateCode = (String) object.get("mateCode");
			if(mateCode == null) {mateCode="";}
			//版本
			String version = (String) object.get("version");
			if(version == null) {version="";}
			//物料箱入数
			Integer boxNumber = (Integer) object.get("boxNumber");
			if(boxNumber == null) {boxNumber=0;}
			//本月可生产订单
			String prodNum = (String) object.get("prodNum");
			Double prodNumD = 0D;
			if(prodNum != null) {
				if(prodNum.contains("<")){
					int index1 = prodNum.indexOf(">", 0);
					int index2 = prodNum.indexOf("<", 1);
					String prodNumValue = prodNum.substring(index1+1,index2);
					prodNumD = Double.valueOf(prodNumValue);
				}else {
					prodNumD = Double.valueOf(prodNum);
				}
			}
			
			BigDecimal prodNumBig = new BigDecimal(prodNumD);
			String key = mateCode +"-"+version;
			List<CutBaoCaiMate> mateList = map.get(key);
			Map<String, Object> calssValueMap = new HashMap<String, Object>();
			//这个循环是为了获取包材对应的数量，存储在calssValueMap中
			for (CutBaoCaiMate cb : mateList) {
				Map<String, Object> classMap = new HashMap<String, Object>();
				String liaiId = cb.getLiaiId();
				//获取包材采购订单物料表的头部信息
				JSONArray cbJa = cutLiaisonForBaoCaiService.queryBaoCaiLiaiMateFields(liaiId);
				for(int j= 0;j<cbJa.size();j++){
					CutLiaiField field = cbJa.getObject(j, CutLiaiField.class);
					String fie = field.getField();
					String title = field.getTitle();
					if(!StringUtils.isEmpty(title)) {
						if(title.contains("库存包材厂")) {
							String className = title.substring(0,title.length()-6);
							classMap.put(fie,className);
						}
					}
				}
				String fields = cb.getFields();
				JSONArray jj = JSONArray.parseArray(fields);
				if(jj.size()!=0){
					for (int j = 0; j < jj.size(); j++) {
						JSONObject obj = jj.getJSONObject(j);
						Set<String> keySe = obj.keySet();
						for (String st : keySe) {
							String className = (String) classMap.get(st);
							if(!StringUtils.isEmpty(className)) {
								String object2 = (String) obj.get(st);
								if(object2==null || "".equals(object2) || "NaN".equals(object2)){
									object2 ="0";
								}
								Double b = Double.parseDouble(object2);
								Double v = (Double) calssValueMap.get(className);
								if(v == null) {
									calssValueMap.put(className, b);//外箱：400
								}else {
									calssValueMap.put(className, v+b);
								}
							}
							
						}
					}
				}
				
			}
			JSONArray header = JSONArray.parseArray(headerFiled);
			Map<String, Object> oemMap = new HashMap<String, Object>();
			for(int i= 0;i<header.size();i++){
				CutLiaiField field = header.getObject(i, CutLiaiField.class);
				String fie = field.getField();
				String title = field.getTitle();
				if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(fie)) {
					if(title.contains("OEM箱") && fie.contains("B")) {
						oemMap.put(title, fie);
					}
				}
			}
			for(int i= 0;i<header.size();i++){
				CutLiaiField field = header.getObject(i, CutLiaiField.class);
				String fie = field.getField();
				String title = field.getTitle();
				if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(fie)) {
					if(title.contains("包材厂") && fie.contains("A")) {
						String className = title.substring(0,title.length()-4);
						String oem = className+"OEM箱";
						String oemfie = (String) oemMap.get(oem);
						String oemNum = (String) object.get(oemfie);//套袋OEM箱
						Double oemNumD = 0D;
						if(oemNum!=null) {
							oemNumD = Double.valueOf(oemNum);
						}
						BigDecimal oemNumBig = new BigDecimal(oemNumD);
						Double b = (Double) calssValueMap.get(className);
						if(b == null) {
							b=0D;
						}
						object.put(fie, b.toString());//套袋包材厂个A
						String nextField = "B"+fie.substring(1);//B302套袋包材厂箱B
						String content = fie.contains("M") ? fie.substring(1,fie.indexOf("M")) : fie.substring(1);
						int k = Integer.parseInt(content)/100;
						String totalField= fie.contains("M")? "C"+(k*100+1)+"MASTER" : "C"+(k*100+1);//C301//合计
						String differenceField = "D"+totalField.substring(1);//差异
						if(fie.contains("MASTER")) {
							//表示1:1折算
							object.put(nextField, b.toString());
							BigDecimal bBig = BigDecimal.valueOf(b);
							BigDecimal totalBig = bBig.add(oemNumBig);
							BigDecimal totalSetScale = totalBig.setScale(2, BigDecimal.ROUND_HALF_UP);
							object.put(totalField, totalSetScale.toString());
							//差异值 = 本月可生产订单-合计
							BigDecimal subtract = prodNumBig.subtract(totalBig);
							BigDecimal setScale = subtract.setScale(2, BigDecimal.ROUND_HALF_UP);
							int compareTo = setScale.compareTo(zero);
							if(compareTo<0) {
								object.put(differenceField, "<span style='color:red;'>"+setScale.toString()+"</span>");
							}else {
								object.put(differenceField, setScale.toString());
							}
						}else {
							//需要除以箱入数
							BigDecimal bDecimal = BigDecimal.valueOf(b);
							if(boxNumber !=null  && boxNumber !=0) {
								BigDecimal box = BigDecimal.valueOf(boxNumber);//箱入数
								BigDecimal divide = bDecimal.divide(box, 2, RoundingMode.HALF_UP);
								Double d = divide.doubleValue();
								object.put(nextField, d.toString());
								BigDecimal dBig = BigDecimal.valueOf(d);
								BigDecimal totalBig = dBig.add(oemNumBig);
								BigDecimal totalSetScale = totalBig.setScale(2, BigDecimal.ROUND_HALF_UP);
								object.put(totalField, totalSetScale.toString());
								//差异值 = 本月可生产订单-合计
								BigDecimal subtract = prodNumBig.subtract(totalBig);
								BigDecimal setScale = subtract.setScale(2, BigDecimal.ROUND_HALF_UP);
								int compareTo = setScale.compareTo(zero);
								if(compareTo<0) {
									object.put(differenceField, "<span style='color:red;'>"+setScale.toString()+"</span>");
								}else {
									object.put(differenceField, setScale.toString());
								}
							}
						}
						
					}
				}
		 }
			
			
			
			
			
			
		}
		return ja;
	}

	private JSONArray dealWithCiteData(JSONArray ja, List<ProdMateDO> addList, String headerFiled) {
		JSONArray header = JSONArray.parseArray(headerFiled);
		for (ProdMateDO pm : addList) {
			JSONObject jo = new JSONObject();
			String mateCode = pm.getMateCode();
			jo.put("mateCode",mateCode);
			jo.put("mateName", pm.getMateName());
			jo.put("seriesExpl", pm.getSeriesExpl());
			jo.put("lastProdNum", pm.getLastProdNum());
			Integer boxNumber = pm.getBoxNumber();
			jo.put("boxNumber", pm.getBoxNumber());
			jo.put("version", pm.getVersion());
			jo.put("cutAim", pm.getCutAim());
			jo.put("mainStru", pm.getMainStru());
			jo.put("sumOutNum", pm.getSumOutNum());
			jo.put("sumInveNum", pm.getSumInveNum());
			jo.put("sumProdNum", pm.getSumProdNum());
			List<CutBaoCaiMate> mateList = pm.getMateList();
			Map<String, Object> calssValueMap = new HashMap<String, Object>();
			//这个循环是为了获取包材对应的数量，存储在calssValueMap中
			for (CutBaoCaiMate cb : mateList) {
				Map<String, Object> classMap = new HashMap<String, Object>();
				String liaiId = cb.getLiaiId();
				//获取包材采购订单物料表的头部信息
				JSONArray cbJa = cutLiaisonForBaoCaiService.queryBaoCaiLiaiMateFields(liaiId);
				for(int i= 0;i<cbJa.size();i++){
					CutLiaiField field = cbJa.getObject(i, CutLiaiField.class);
					String fie = field.getField();
					String title = field.getTitle();
					if(!StringUtils.isEmpty(title)) {
						if(title.contains("库存包材厂")) {
							String className = title.substring(0,title.length()-6);
							classMap.put(fie,className);
						}
					}
				}
				String fields = cb.getFields();
				JSONArray jj = JSONArray.parseArray(fields);
				if(jj.size()!=0){
					for (int i = 0; i < jj.size(); i++) {
						JSONObject object = jj.getJSONObject(i);
						Set<String> keySet = object.keySet();
						for (String str : keySet) {
							String className = (String) classMap.get(str);
							if(!StringUtils.isEmpty(className)) {
								String object2 = (String) object.get(str);
								if(object2==null || "".equals(object2) || "NaN".equals(object2)){
									object2 ="0";
								}
								Double b = Double.parseDouble(object2);
								Double v = (Double) calssValueMap.get(className);
								if(v == null) {
									calssValueMap.put(className, b);//外箱：400
								}else {
									calssValueMap.put(className, v+b);
								}
							}
							
						}
					}
				}
				
			}
			 for(int i= 0;i<header.size();i++){
					CutLiaiField field = header.getObject(i, CutLiaiField.class);
					String fie = field.getField();
					String title = field.getTitle();
					if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(fie)) {
						if(title.contains("包材厂") && fie.contains("A")) {
							String className = title.substring(0,title.length()-4);
							Double b = (Double) calssValueMap.get(className);
							if(b == null) {
								b=0D;
							}
							jo.put(fie, b.toString());//套袋包材厂个A
							String nextField = "B"+fie.substring(1);//B302套袋包材厂箱B
							String content = fie.contains("M") ? fie.substring(1,fie.indexOf("M")) : fie.substring(1);
							int k = Integer.parseInt(content)/100;
							String totalField= fie.contains("M")? "C"+(k*100+1)+"MASTER" : "C"+(k*100+1);//C301//合计
							String differenceField = "D"+totalField.substring(1);//差异
							if(fie.contains("MASTER")) {
								//表示1:1折算
								jo.put(nextField, b.toString());
								jo.put(totalField, b.toString());
								if(b>0) {
									jo.put(differenceField, "<span style='color:red;'>-"+b.toString()+"</span>");
								}else {
									jo.put(differenceField, b.toString());
								}
							}else {
								//需要除以箱入数
								BigDecimal bDecimal = BigDecimal.valueOf(b);
								if(boxNumber !=null  && boxNumber !=0) {
									BigDecimal box = BigDecimal.valueOf(boxNumber);//箱入数
									BigDecimal divide = bDecimal.divide(box, 2, RoundingMode.HALF_UP);
									Double d = divide.doubleValue();
									jo.put(nextField, d.toString());
									jo.put(totalField, d.toString());
									if(d>0) {
										jo.put(differenceField, "<span style='color:red;'>-"+d.toString()+"</span>");
									}else {
										jo.put(differenceField, d.toString());
									}
								}
							}
						}
					}
			 }
			
			ja.add(jo);
		}
		return ja;
	}

	@Override
	public Map<String, Object> updateSpeCutLiaiMateByMonth(String liaiId, 
			String cutMonth) {
		Map<String, Object> map = querySpecialCutLiaiMate(cutMonth);
		boolean b = (boolean) map.get("judge");
		if(b){
			//新的特殊打切联络单的物料数据
			List<ProdMateDO> proList = (List<ProdMateDO>) map.get("list");
			//旧的特殊打切联络单物料信息
			List<CutLiaiMate> list = cutLiaisonMapper.queryCutLiaiMateByLiaiId(liaiId);
			JSONArray ja = getNewCutMateList(proList,list);
			map.put("data", ja);
			return map;
		}else{
			return map;
		}
	}
	
	//更新逻辑  proList:新数据集，list：旧数据集。
	public JSONArray getNewCutMateList(List<ProdMateDO> proList,
			List<CutLiaiMate> list){
		//需要添加的物料信息
		List<CutLiaiMate> addList = new ArrayList<CutLiaiMate>();
		//需要删除的物料信息
		List<CutLiaiMate> removeList = new ArrayList<CutLiaiMate>();
		//获取删除的物料数据
		for(CutLiaiMate cm:list){
			int count=0;
			String cmc = cm.getMateCode();
			String cmv = cm.getVersion()== null ? "" : cm.getVersion();
			for(ProdMateDO pm:proList){
				String pmc = pm.getMateCode();
				String pmv = pm.getVersion() == null ? "" : pm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					cm.setSumOutNum(pm.getSumOutNum());
					cm.setSumInveNum(pm.getSumInveNum());
					cm.setSumProdNum(pm.getSumProdNum());
					cm.setCutAim(pm.getCutAim());
					cm.setMainStru(pm.getMainStru());
				}else{
					count ++;
				}
			}
			if(count == proList.size()){
				removeList.add(cm);
			}
		}
		//获取新增物料数据
		for(ProdMateDO pm:proList){
			int count=0;
			String pmc = pm.getMateCode();
			String pmv = pm.getVersion() == null ? "" : pm.getVersion();
			for(CutLiaiMate cm:list){
				String cmc = cm.getMateCode();
				String cmv = cm.getVersion()== null ? "" : cm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					
				}else{
					count ++;
				}
			}
			if(count == list.size()){
				CutLiaiMate cutLiaiMate = new CutLiaiMate();
				cutLiaiMate.setMateCode(pm.getMateCode());
				cutLiaiMate.setMateName(pm.getMateName());
				cutLiaiMate.setBoxNumber(pm.getBoxNumber());
				cutLiaiMate.setVersion(pm.getVersion());
				cutLiaiMate.setCutAim(pm.getCutAim());
				cutLiaiMate.setMainStru(pm.getMainStru());
				cutLiaiMate.setSumOutNum(pm.getSumOutNum());
				cutLiaiMate.setSumInveNum(pm.getSumInveNum());
				cutLiaiMate.setSumProdNum(pm.getSumProdNum());
				cutLiaiMate.setSeriesExpl(pm.getSeriesExpl());
				cutLiaiMate.setLastProdNum(pm.getLastProdNum());
				addList.add(cutLiaiMate);
			}
		}
		list.addAll(addList);
		list.removeAll(removeList);
		JSONArray ja = new JSONArray();
		for (CutLiaiMate clm : list) {
			JSONObject jo = new JSONObject();
			jo.put("mateCode",clm.getMateCode());
			jo.put("id",clm.getLiaiMateId());
			jo.put("seriesExpl", clm.getSeriesExpl());
			jo.put("mateName", clm.getMateName());
			jo.put("outNum", clm.getOutNum());
			jo.put("inveNum", clm.getInveNum());
			Integer prodNum = clm.getProdNum();
			if(prodNum == null) {
				prodNum = 0;
			}
			Integer lastProdNum = clm.getLastProdNum();
			if(lastProdNum == null) {
				lastProdNum = 0;
			}
			if(prodNum > lastProdNum) {
				jo.put("prodNum", "<span style='color:red;'>"+clm.getProdNum()+"</span>");
			}else {
				jo.put("prodNum", clm.getProdNum());
			}
			jo.put("lastProdNum", clm.getLastProdNum());
			jo.put("boxNumber", clm.getBoxNumber());
			jo.put("version", clm.getVersion());
			jo.put("cutAim", clm.getCutAim());
			jo.put("mainStru", clm.getMainStru());
			jo.put("sumOutNum", clm.getSumOutNum());
			jo.put("sumInveNum", clm.getSumInveNum());
			jo.put("sumProdNum", clm.getSumProdNum());
			jo.put("remark", clm.getRemark());
			String fields = clm.getFields()== null ? "" : clm.getFields();
			JSONArray jj = JSONArray.parseArray(fields);
			if(jj != null){
				for (int i = 0; i < jj.size(); i++) {
					JSONObject object = jj.getJSONObject(i);
					Set<String> keySet = object.keySet();
					for (String str : keySet) {
						jo.put(str, object.get(str));
					}
				}
			}
			ja.add(jo);
		}
		return ja;
	}

	@Override
	public boolean checkoutCutLiaiByMonthAndSuppId(String cutMonth, String suppId) {
		List<String> list = cutLiaisonMapper.checkoutCutLiaiByMonthAndSuppId(cutMonth,suppId);
		if(list.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean cancellCutLiaisonByLiaiIds(List<String> list) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "已作废");
		map.put("liaiIds", list);
		map.put("canceDate", new Date());
		map.put("user", user.getName());
		int i = cutLiaisonMapper.updateCutLiaiStatusByliaiIds(map);
		if(i==list.size()){
			return true;
		}else{
			return false;
		}
	}

	

	
	

}
