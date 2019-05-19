package com.faujor.service.bam.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.CutLiaisonForBaoCaiMapper;
import com.faujor.dao.master.bam.CutProductMapper;
import com.faujor.dao.master.bam.OrderMonthMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.bam.CutBaoCaiField;
import com.faujor.entity.bam.CutBaoCaiMate;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutBaoCai;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.ProdMateDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.bam.PdrService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.UserCommon;
import oracle.sql.BLOB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Service(value="CutLiaisonForBaoCaiService")
public class CutLiaisonForBaoCaiServiceImpl implements CutLiaisonForBaoCaiService {

	@Autowired
	private CutLiaisonForBaoCaiMapper cutLiaisonMapper;
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
	
	@Override
	public Map<String, Object> queryBaoCaiCurLiaisonByPage(Map<String, Object> map) {
		 Map<String, Object> page = new HashMap<String,Object>();
		 List<CutBaoCai> list = cutLiaisonMapper.queryBaoCaiCutLiaisonByPage(map);
		 int count = cutLiaisonMapper.queryBaoCaiCutLiaisonByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean addBaoCaiCutLiaison(CutBaoCai cutLiai, List<CutBaoCaiMate> list) {
		List<CutBaoCaiMate> list2 = getBaoCaiMainStruNum(cutLiai,list);
		int i = cutLiaisonMapper.addBaoCaiCutLiaison(cutLiai);
		insertBaoCaiFieldsBlob(cutLiai);
		int count=0;
		for(CutBaoCaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addBaoCaiCutLiaiMate(clm);
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
	public void insertBaoCaiFieldsBlob(CutBaoCai cutLiai){
		CutBaoCai cl = cutLiaisonMapper.queryBaoCaiCutLiaiFieldsBlobByLiaiId(cutLiai.getLiaiId());
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
	private List<CutBaoCaiMate> getBaoCaiMainStruNum(CutBaoCai cutLiai, List<CutBaoCaiMate> list) {
		Set<CutBaoCaiField> set  = new HashSet<CutBaoCaiField>();
		String Headerfields = cutLiai.getFields();
		JSONArray headerJA = JSONArray.parseArray(Headerfields);
		for(int i= 0;i<headerJA.size();i++){
			CutBaoCaiField field = headerJA.getObject(i, CutBaoCaiField.class);
			set.add(field);
		}
		for(CutBaoCaiMate mate :list){
			String fields = mate.getFields();
			System.out.println("=================fields" + fields);
//			String mainStru = mate.getMainStru();
//			int indexOf = mainStru.indexOf(" ");
//			String className = mainStru.substring(indexOf+1);
//			String f = className+"合计";
			/*String field2="";
			for (CutBaoCaiField c : set) {
//				if(f.equals(c.getTitle())){
					field2 = c.getField();
				System.out.println("=================字段" + field2);
				break;
//				}
			}*/
			JSONArray jj = JSONArray.parseArray(fields);
			if(jj.size()!=0){
				outer:
					for (int i = 0; i < jj.size(); i++) {
						JSONObject object = jj.getJSONObject(i);
						Set<String> keySet = object.keySet();
						for (String str : keySet) {
							/*if(field2.equals(str)){
								String object2 = (String) object.get(str);
								if(object2==null || "".equals(object2) || "NaN".equals(object2)){
									object2 ="0";
								}
								Double b = Double.parseDouble(object2);
//								mate.setMainStruNum(b);
								break outer;
							}*/
						}
					}
			}else{
//				mate.setMainStruNum(0D);
			}
		}
		
		return list;
	}

	@Override
	@Transactional
	public boolean deleteBaoCaiCutLiaisonByliaiIds(String[] liaiIds) {
		int j = cutLiaisonMapper.deleteBaoCaiCutLiaiMateByLiaiIds(liaiIds);
		int i = cutLiaisonMapper.deleteBaoCaiCutLiaisonByliaiIds(liaiIds);
		if(i==liaiIds.length){
			/*TaskParamsDO params = new TaskParamsDO();
			for(int k =0;k<liaiIds.length;k++){
				params.setSdata1(liaiIds[k]);
				taskService.removeTaskBySdata1(params );
			}*/
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateBaoCaiCutLiaiStatusByliaiIds(Map<String,Object> map) {
		int i = cutLiaisonMapper.updateBaoCaiCutLiaiStatusByliaiIds(map);
		int k = (int) map.get("size");
		if(i==k){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public CutBaoCai queryBaoCaiCutLiaisonByLiaiId(String liaiId) {
		return cutLiaisonMapper.queryBaoCaiCutLiaisonByLiaiId(liaiId);
	}

	@Override
	public Map<String, Object> getBaoCaiContactSheet(String liaiId) {
		List<CutBaoCaiMate> list = cutLiaisonMapper.queryBaoCaiCutLiaiMateByLiaiId(liaiId);
		JSONArray ja = new JSONArray();
		List<String> ermSuppList = new ArrayList<String>();
		for (CutBaoCaiMate clm : list) {
			JSONObject jo = new JSONObject();
			jo.put("mateCode",clm.getMateCode());
			jo.put("id",clm.getLiaiMateId());
			jo.put("mateName", clm.getMateName());
			jo.put("oemSuppName", clm.getOemSuppName());
			jo.put("oemSuppCode", clm.getOemSuppCode());
			jo.put("isSpecial", clm.getIsSpecial());
			jo.put("version", clm.getVersion());
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
			String oemName = clm.getOemSuppName();
			if (oemName != null){
				Boolean flag = ermSuppList.contains(oemName);
				if (!flag){
					ermSuppList.add(oemName);
					System.out.println("oemName===========================" + oemName);
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", ja);
		map.put("count", ja.size());
		map.put("code", "0");
		map.put("msg", "");
		map.put("ermSuppList", ermSuppList);
		return map;
	}

	@Override
	public JSONArray queryBaoCaiLiaiMateFields(String liaiId) {
		CutBaoCai cutLiai2 = cutLiaisonMapper.queryBaoCaiCutLiaisonByLiaiId(liaiId);
		String fields = cutLiai2.getFields();
		if(!StringUtils.isEmpty(fields)){
			JSONArray jj = JSONArray.parseArray(fields);
			return jj;
		}
		CutBaoCai cutLiai = cutLiaisonMapper.queryBaoCaiCutLiaiFieldsBlobByLiaiId(liaiId);
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
		/*String s = "";
		try {
			s = new String(byteArray,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
	    String s = new String(byteArray);
	    JSONArray jj = JSONArray.parseArray(s);
		return jj;
	}

	@Override
	public Map<String, Object> queryBaoCaiCutLiaisonForManageByPage(Map<String, Object> map) {
		 Map<String, Object> page = new HashMap<String,Object>();
		 List<CutBaoCai> list = cutLiaisonMapper.queryBaoCaiCutLiaisonForManageByPage(map);
		 int count = cutLiaisonMapper.queryBaoCaiCutLiaisonForManageByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean updateBaoCaiStatusOfCutLiaisonByLiaiId(Map<String, Object> map) {
		int i = cutLiaisonMapper.updateBaoCaiStatusOfCutLiaisonByLiaiId(map);
		int j = (int) map.get("size");
		if(i==j){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> udpateBaoCaiCutLiaiMate(CutBaoCai cutLiai, List<CutBaoCaiMate> list, String type) {
		Map<String,Object> result = new HashMap<String,Object>();
		if("2".equals(type)){
			List<String> liaiIds = new ArrayList<String>();
			liaiIds.add(cutLiai.getLiaiId());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", "已提交");
//			map.put("status", "已保存");
			map.put("liaiIds", liaiIds);
			int i = cutLiaisonMapper.updateBaoCaiStatusOfCutLiaisonByLiaiId(map);
		}
		List<CutBaoCaiMate> list2 = getBaoCaiMainStruNum(cutLiai,list);
		String[] liaiId = new String[1];
		liaiId[0]=cutLiai.getLiaiId();
		int k = cutLiaisonMapper.deleteBaoCaiCutLiaiMateByLiaiIds(liaiId);
		int count=0;
		for(CutBaoCaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addBaoCaiCutLiaiMate(clm);
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
	public List<CutBaoCai> queryBaoCaiManyCutLiaisonByLiaiIds(List<String> liaiIds) {
		return cutLiaisonMapper.queryBaoCaiManyCutLiaisonByLiaiIds(liaiIds);
	}

	@Override
	public List<CutBaoCaiMate> queryBaoCaiManyCutLiaiMateByLiaiIds(List<String> liaiIds) {
		return cutLiaisonMapper.queryBaoCaiManyCutLiaiMateByLiaiIds(liaiIds);
	}

	@Override
	public Map<String, Object> queryBaoCaiFinMaterialByPage(Map<String, Object> map) {
		List<MateDO> list = materialMapper.queryFinMaterialByPage(map);
		int count = materialMapper.queryFinMaterialByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public Map<String, Object> queryBaoCaiSemiFinMateByMateCode(List<String> mateCodes) {
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
	public Map<String, Object> queryBaoCaiSpeCutLiaisonByPage(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String,Object>();
		List<CutBaoCai> list = cutLiaisonMapper.queryBaoCaiSpeCutLiaisonByPage(map);
		int count = cutLiaisonMapper.queryBaoCaiSpeCutLiaisonByPageCount(map);
		 page.put("data", list);
		 page.put("code", 0);
		 page.put("count", count);
		 page.put("msg", "");
		return page;
	}

	@Override
	public Map<String, Object> queryBaoCaiSpecialCutLiaiMate(String cutMonth) {
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
				list = getBaoCaiCountNum(list,cutMonth,null);
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
	public List<ProdMateDO> getBaoCaiCountNum(List<ProdMateDO> list,String cutMonth,String suppId){
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
	public boolean updateBaoCaiSpeCutLiaison(CutBaoCai cutLiai,List<CutBaoCaiMate> list) {
		List<CutBaoCaiMate> list2 = getBaoCaiMainStruNum(cutLiai,list);
		String[] liaiId = new String[1];
		liaiId[0]=cutLiai.getLiaiId();
		int k = cutLiaisonMapper.deleteBaoCaiCutLiaiMateByLiaiIds(liaiId);
		int count=0;
		for(CutBaoCaiMate clm:list2){
			int j=0;
			clm.setLiaiId(cutLiai.getLiaiId());
			clm.setIsSpecial(cutLiai.getIsSpecial());
			j=cutLiaisonMapper.addBaoCaiCutLiaiMate(clm);
			count+=j;
		}
		if(count==list2.size() && k>0){
			return true;
		}else{
			return false;
		}
	}
	
	//更新逻辑  proList:新数据集，list：旧数据集。
	public JSONArray getBaoCaiNewCutMateList(List<ProdMateDO> proList,List<CutBaoCaiMate> list){
		//需要添加的物料信息
		List<CutBaoCaiMate> addList = new ArrayList<CutBaoCaiMate>();
		//需要删除的物料信息
		List<CutBaoCaiMate> removeList = new ArrayList<CutBaoCaiMate>();
		//获取删除的物料数据
		for(CutBaoCaiMate cm:list){
			int count=0;
			String cmc = cm.getMateCode();
			String cmv = cm.getVersion()== null ? "" : cm.getVersion();
			for(ProdMateDO pm:proList){
				String pmc = pm.getMateCode();
				String pmv = pm.getVersion() == null ? "" : pm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					/*cm.setSumOutNum(pm.getSumOutNum());
					cm.setSumInveNum(pm.getSumInveNum());
					cm.setSumProdNum(pm.getSumProdNum());
					cm.setCutAim(pm.getCutAim());
					cm.setMainStru(pm.getMainStru());*/
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
			for(CutBaoCaiMate cm:list){
				String cmc = cm.getMateCode();
				String cmv = cm.getVersion()== null ? "" : cm.getVersion();
				if(pmc.equals(cmc)&& pmv.equals(cmv)){
					
				}else{
					count ++;
				}
			}
			if(count == list.size()){
				CutBaoCaiMate cutLiaiMate = new CutBaoCaiMate();
				cutLiaiMate.setMateCode(pm.getMateCode());
				cutLiaiMate.setMateName(pm.getMateName());
				cutLiaiMate.setVersion(pm.getVersion());
				addList.add(cutLiaiMate);
			}
		}
		list.addAll(addList);
		list.removeAll(removeList);
		JSONArray ja = new JSONArray();
		for (CutBaoCaiMate clm : list) {
			JSONObject jo = new JSONObject();
			jo.put("mateCode",clm.getMateCode());
			jo.put("id",clm.getLiaiMateId());
//			jo.put("seriesExpl", clm.getSeriesExpl());
			jo.put("mateName", clm.getMateName());
			/*jo.put("outNum", clm.getOutNum());
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
			jo.put("boxNumber", clm.getBoxNumber());*/
			jo.put("version", clm.getVersion());

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
	public boolean checkoutBaoCaiCutLiaiByMonthAndSuppId(String cutMonth, String suppId) {
		List<String> list = cutLiaisonMapper.checkoutBaoCaiCutLiaiByMonthAndSuppId(cutMonth,suppId);
		if(list.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<String> queryBaoCaiCutLiaiCodeList() {
		return cutLiaisonMapper.queryBaoCaiCutLiaiCodeList();
	}

}
