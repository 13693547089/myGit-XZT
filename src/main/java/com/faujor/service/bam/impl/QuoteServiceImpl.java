package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.common.CXF.WebServiceUtil;
import com.faujor.common.constant.GlobalConstant;
import com.faujor.common.ftp.FtpUtil;
import com.faujor.dao.master.bam.QuoteMapper;
import com.faujor.dao.master.bam.QuoteMateMapper;
import com.faujor.dao.master.bam.QuotePriceMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.dao.master.mdm.QuoteStruMapper;
import com.faujor.dao.master.task.TaskMapper;
import com.faujor.entity.bam.Quote;
import com.faujor.entity.bam.QuoteAttr;
import com.faujor.entity.bam.QuoteMate;
import com.faujor.entity.bam.QuotePrice;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.entity.mdm.QuoteStruDetails;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.QuoteService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("quoteService")
public class QuoteServiceImpl implements QuoteService {
	@Autowired
	private QuoteMapper quoteMapper;
	@Autowired
	private QuoteMateMapper quoteMateMapper;
	@Autowired
	private QuotePriceMapper quotePriceMapper;
	@Autowired
	private QuoteStruMapper quoteStruMapper;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskMapper taskMapper;
	
	
	@Override
	@Transactional
	public void saveQuote(Quote quote, List<QuoteMate> quoteMates,List<QuoteAttr> quoteAttrs) {
		SysUserDO user = UserCommon.getUser();
		/*String uuid = UUIDUtil.getUUID();
		quote.setId(uuid);*/
		String quoteCode = quote.getQuoteCode();
		//附件信息
		String docId = quote.getQuoteBase();
		Document doc=new Document();
		doc.setId(docId);
		doc.setLinkNo(quoteCode);
		doc.setDocCate("bam_quote");
		documentMapper.updateDocLink(doc);
		//保存报价信息
		quote.setCreateUser(user.getUsername());
		quote.setCreateTime(new Date());
		quote.setCreater(user.getName());
		quote.setModifyUser(user.getUsername());
		quote.setModifyTime(new Date());
		quote.setModifier(user.getName());
		String quoteStatus = quote.getStatus();
		if(StringUtils.isEmpty(quoteStatus)){
			quote.setStatus("已保存");
		}
		quoteMapper.saveQuote(quote);
		for (QuoteMate quoteMate : quoteMates) {
			String mateId = UUIDUtil.getUUID();
			quoteMate.setId(mateId);
			quoteMate.setQuoteCode(quoteCode);
			//保存报价物料信息
			quoteMateMapper.saveQuoteMate(quoteMate);
			List<QuotePrice> priceList = quoteMate.getPriceList();
			for (QuotePrice quotePrice : priceList) {
				quotePrice.setId(UUIDUtil.getUUID());
				quotePrice.setQuoteCode(quoteCode);
				quotePrice.setQuoteMateId(mateId);
				//保存报价单下物料的报价信息
				quotePriceMapper.saveQuotePrice(quotePrice);
			}
		}
		//报价单附件信息
		 quoteMapper.delAttrByQuoteCode(quoteCode);
		 if(quoteAttrs!=null){
			 for (QuoteAttr quoteAttr : quoteAttrs) {
				 quoteAttr.setQuoteCode(quoteCode);
				 quoteMapper.insertAttr(quoteAttr);
			 }
		 }
	}

	@Override
	@Transactional
	public void updateQuote(Quote quote, List<QuoteMate> quoteMates,List<QuoteAttr> quoteAttrs) {
		String quoteCode = quote.getQuoteCode();
		SysUserDO user = UserCommon.getUser();
		//更新报价信息
		quote.setModifyUser(user.getUsername());
		quote.setModifyTime(new Date());
		quote.setModifier(user.getName());
		String quoteStatus = quote.getStatus();
		if(StringUtils.isEmpty(quoteStatus)){
			quote.setStatus("已保存");
		}
		quoteMapper.updateQuote(quote);
		//附件信息
		String docId = quote.getQuoteBase();
		Document doc=new Document();
		doc.setId(docId);
		doc.setLinkNo(quoteCode);
		doc.setDocCate("bam_quote");
		documentMapper.updateDocLink(doc);
		//删除报价单物料信息
		quoteMateMapper.delQuoteMateByQuoteCode(quoteCode);
		//删除报价单下物料的报价信息
		quotePriceMapper.delQuoteMateByQuoteCode(quoteCode);
		for (QuoteMate quoteMate : quoteMates) {
			String mateId = UUIDUtil.getUUID();
			quoteMate.setId(mateId);
			quoteMate.setQuoteCode(quoteCode);
			//保存报价物料信息
			quoteMateMapper.saveQuoteMate(quoteMate);
			List<QuotePrice> priceList = quoteMate.getPriceList();
			for (QuotePrice quotePrice : priceList) {
				quotePrice.setId(UUIDUtil.getUUID());
				quotePrice.setQuoteCode(quoteCode);
				quotePrice.setQuoteMateId(mateId);
				//保存报价单下物料的报价信息
				quotePriceMapper.saveQuotePrice(quotePrice);
			}
		}
		//报价单附件信息
		 quoteMapper.delAttrByQuoteCode(quoteCode);
		 if(quoteAttrs!=null){
			 for (QuoteAttr quoteAttr : quoteAttrs) {
				 quoteAttr.setQuoteCode(quoteCode);
				 quoteMapper.insertAttr(quoteAttr);
			 }
		 }
	}
	@Override
	@Transactional
	public void delQuote(List<String> quoteCodes) {
		Map<String, Object> map=new HashMap<>();
		TaskParamsDO params = new TaskParamsDO();
		for (String quoteCode : quoteCodes) {
			Quote quote = quoteMapper.getQuoteByCode(quoteCode);
			//更新报价信息
			quoteMapper.delQuoteByCode(quoteCode);
			//删除任务
			params.setSdata1(quote.getId());
			taskService.removeTaskBySdata1(params);
			String docId=quote.getQuoteBase();
			if(docId!=null){
				map.put("docId", docId);
				Document doc = documentMapper.getDoc(map);
				if(doc !=null){
					FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
				}
				documentMapper.deleteDoc(docId);
			}
			//删除报价单物料信息
			quoteMateMapper.delQuoteMateByQuoteCode(quoteCode);
			//删除报价单下物料的报价信息
			quotePriceMapper.delQuoteMateByQuoteCode(quoteCode);
		}
	}

	@Override
	public LayuiPage<Quote> getQuoteByPage(Map<String, Object> map) {
		LayuiPage<Quote> page=new LayuiPage<Quote>();
		List<Quote> rows = quoteMapper.getQuoteByPage(map);
		Integer count = quoteMapper.getQuoteNum(map);
		page.setData(rows);
		page.setCount(count);
		return page;
	}

	@Override
	public List<QuoteMate> getQuoteMatesByQuoteCode(String quoteCode) {
		Map<String, Object> map=new HashMap<String,Object>();
		//获取报价单下的每个物料并为每个物料挂物料下每个组件的报价信息
		List<QuoteMate> quoteMates = quoteMateMapper.getAllQuoteMateByQuoteCode(quoteCode);
		for (QuoteMate quoteMate : quoteMates) {
			String mateId = quoteMate.getId();
			map.put("quoteMateId", mateId);
			List<QuotePrice> quotePrices = quotePriceMapper.getPricesByMateId(map);
			quoteMate.setPriceList(quotePrices);
		}
		return quoteMates;
	}

	@Override
	@Transactional
	public void updateQuoteStatus(String status,List<String> quoteCodes) {
		Map<String, Object> map=new HashMap<String ,Object>();
		//初审核人
		if(status!=null && status.equals("初审核")){
			SysUserDO user = UserCommon.getUser();
			map.put("firstAuditor", user.getUsername());
			map.put("firstAuditDate", new Date());
		}
		map.put("status", status);
		for (String quoteCode : quoteCodes) {
			map.put("quoteCode", quoteCode);
			quoteMapper.updateQuoteStatus(map);
		}
	}

	@Override
	public Quote getQuoteByQuoteCode(String quoteCode) {
		Quote quote = quoteMapper.getQuoteByCode(quoteCode);
		Map<String, Object> map = new HashMap<>();
		if(!StringUtils.isEmpty(quote.getQuoteBaseTwo())){
			map.put("docId", quote.getQuoteBaseTwo());
			Document doc = documentMapper.getDoc(map);
			quote.setQuoteBaseTwoName(doc.getRealName());
		}
		return quote;
	}

	@Override
	public List<QuotePrice> getQuotePriceByMateId(Map<String, Object> map) {
		List<QuoteStruDetails> struDetails = quoteStruMapper.getDetailsByMateId(map);
		List<QuotePrice> prices=new ArrayList<QuotePrice>();
		for (QuoteStruDetails struDetail : struDetails) {
			QuotePrice price=new QuotePrice();
			BeanUtils.copyProperties(struDetail, price);
			price.setMateNo(map.get("mateId").toString());
			price.setDetailUnit(struDetail.getUnit());
			String detailsNum = struDetail.getDetailsNum();
			price.setDetailsNum(new BigDecimal(StringUtils.isNumber(detailsNum)?detailsNum:"0"));
			prices.add(price);
		}
		return prices;
	}

	@Override
	public RestCode submitQuoteToOA(List<String> quoteCodes) {
		StringBuffer sb=new StringBuffer();
		JSONArray resArr=new JSONArray();
		boolean flag=true;//全部成功！
		for (String quoteCode : quoteCodes) {
			JSONObject resObj = submitQuoteToOA(quoteCode);
			resArr.add(resObj);
		}
		for(int i=0;i<resArr.size();i++){
			JSONObject obj = resArr.getJSONObject(i);
			String status = obj.getString("status");
			if("E".equals(status)){
				flag=false;
				sb.append("报价单"+obj.getString("quoteCode")+obj.getString("msg")+"!</br>");
			}
		}
		if(flag){
			return new RestCode();
		}else{
			return RestCode.error(sb.toString());
		}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public JSONObject submitQuoteToOA(String quoteCode){
		
		Map<String, Object> map=new HashMap<String,Object>();
		JSONObject resultObj=new JSONObject();
		//组织JSOn
		JSONObject obj=new JSONObject();
		JSONObject maintable=new JSONObject();
		JSONArray materielitem=new JSONArray();
		JSONArray price60Arr=new JSONArray();
		Quote quote = quoteMapper.getQuoteByCode(quoteCode);
		maintable.put("quoteCode", quoteCode);
		maintable.put("quoteType", quote.getQuoteType());
		maintable.put("suppNo", quote.getSuppNo());
		maintable.put("remark", quote.getRemark());
		/*String quoteBase = quote.getQuoteBase();
		if(quoteBase!=null){
			map.put("docId", quoteBase);
			Document doc = documentMapper.getDoc(map);
			if(doc!=null){
				quoteBase=doc.getFileUrl()+doc.getFileName();
			}
		}
		maintable.put("quoteBase", quoteBase);*/
		List<String> strList =  new ArrayList<>();
		strList.add("A");
		strList.add("B");
		strList.add("C");
		strList.add("D");
		strList.add("E");
		List<QuoteAttr> attrList = quoteMapper.getQuoteAttrByQuoteCode(quoteCode);
		for (int i=0;i<attrList.size();i++) {
			String attrFile = attrList.get(i).getAttrFile();
			 Document doc = JSONArray.parseObject(attrFile, Document.class);
			 maintable.put("quoteBase"+strList.get(i), doc.getFileUrl()+doc.getFileName());
		}
		String createUser = quote.getCreateUser();
		SysUserDO creater = userMapper.findByUserName(createUser);
		maintable.put("userName", creater.getSuppNo());
		maintable.put("quoteDate", DateUtils.format(quote.getQuoteDate(), "yyyy-MM-dd"));
		obj.put("maintable", maintable);
		List<QuoteMate> mates = quoteMateMapper.getAllQuoteMateByQuoteCode(quoteCode);
		
		for (int i=0;i<mates.size();i++) {
			QuoteMate quoteMate=mates.get(i);
			JSONObject mateObj=new JSONObject();
			JSONArray priceArr=new JSONArray();
			mateObj.put("mateNo", quoteMate.getMateCode());
			mateObj.put("index", i+1);
			mateObj.put("PriceEffectiveDate", DateUtils.format(quoteMate.getStartDate(), "yyyy-MM-dd"));
			mateObj.put("PriceExpirationDate", DateUtils.format(quoteMate.getEndDate(), "yyyy-MM-dd"));
			mateObj.put("remark", quoteMate.getRemark());
			mateObj.put("HideSuppScope", quoteMate.getSuppScope());
			mateObj.put("NewPurchaseRate", GlobalConstant.MULT_TATE);
			mateObj.put("priceitem", priceArr);
			List<QuotePrice> prices = quotePriceMapper.getSubTotalByQuoteMateId(quoteMate.getId());
			for (QuotePrice quotePrice : prices) {
				JSONObject priceObj=new JSONObject();
				String segmCode = quotePrice.getSegmCode();
				if(!"60".equals(segmCode)) {
					priceObj.put("segmCode", quotePrice.getSegmCode());
					priceObj.put("subtotal", quotePrice.getDetailPrice().multiply(new BigDecimal(GlobalConstant.MULT_TATE)).setScale(2,BigDecimal.ROUND_HALF_DOWN));
					priceArr.add(priceObj);
				}else {
					priceObj.put("segmCode", quotePrice.getSegmCode());
					priceObj.put("subtotal", "0");
					priceArr.add(priceObj);
				}
			}
			materielitem.add(mateObj);
			//----------------------添加60段报价信息--------------------
			String quoteMateId = quoteMate.getId();
			List<QuotePrice> quotePrices = quotePriceMapper.getPricesBySegmCode(quoteMateId, "60");
			for (QuotePrice quotePrice : quotePrices) {
				JSONObject priceObj=new JSONObject();
				priceObj.put("HideFREIGHTFREIGHTSCOPE", quotePrice.getMateCode());
				priceObj.put("HideFreightSuppScope", quoteMate.getSuppScope());
				priceObj.put("FreightSuppScopeNew", quoteMate.getMateName());
				priceObj.put("FREIGHTMATENO", quoteMate.getMateCode());
				priceObj.put("FREIGHTSHOWNEWFREIGHTPRICE", quotePrice.getDetailPrice().multiply(new BigDecimal(GlobalConstant.MULT_TATE)).setScale(2,BigDecimal.ROUND_HALF_DOWN));
				price60Arr.add(priceObj);				
			}	
			//----------------------添加60段报价信息--------------------
		}
		obj.put("PROC_SUPPLIERINFO_DT", materielitem);
		obj.put("PROC_SUPPLIERINFOFREIGHT_DT", price60Arr);
		String firstAuditor = quote.getFirstAuditor();
		SysUserDO firstAuditorUser = userMapper.findByUserName(firstAuditor);
		String invokeWs = WebServiceUtil.invokeWs(GlobalConstant.OA_WSDL, GlobalConstant.OA_AUDIT,"提交报价单到OA",GlobalConstant.OA_TOKEN,firstAuditorUser.getSuppNo(),GlobalConstant.OA_SUMMARY,UUIDUtil.getUUID(),obj.toJSONString());
		if(invokeWs==null){
			resultObj.put("status", "E");
			resultObj.put("quoteCode", quoteCode);
			resultObj.put("msg", "接口返回数据异常");
			map.put("quoteCode", quoteCode);
			map.put("status", "OA待审核");
			quoteMapper.updateQuoteStatus(map);
		}else{
			JSONObject resObj=JSONObject.parseObject(invokeWs);
			String status = resObj.getString("sTATUS");
			String msg = resObj.getString("mESSAGE");
			if("S".equals(status)){
				map.put("quoteCode", quoteCode);
				map.put("status", "OA审核");
				quoteMapper.updateQuoteStatus(map);
				resultObj.put("status", "S");
			}else{
				map.put("quoteCode", quoteCode);
				map.put("status", "OA待审核");
				quoteMapper.updateQuoteStatus(map);
				resultObj.put("status", "E");
			}
			resultObj.put("quoteCode", quoteCode);
			resultObj.put("msg", msg);
		}
		return resultObj;
	}
	
	@Override
	public JSONObject oaAuditQuote(String data) {
		JSONObject resultObj=new JSONObject();
		JSONObject dataObj=JSONObject.parseObject(data);
		Map<String, Object> map=new HashMap<String,Object>();
		String  quoteCode= dataObj.getString("quoteCode");
		map.put("quoteCode",quoteCode );
		String status = dataObj.getString("status");
		if("E".equals(status)){
			//生成一条已回退的报价单代办任务
			map.put("status", "已退回");
			map.put("oaAuditDate", new Date());
		}else if("S".equals(status)){
			map.put("status", "已生效");
		}else{
			resultObj.put("STATUS", "E");
			resultObj.put("MESSAGE", "审核状态传递错误！");
			return resultObj;
		}
		Quote quote = quoteMapper.getQuoteByCode(quoteCode);
		if(quote==null){
			resultObj.put("STATUS", "E");
			resultObj.put("MESSAGE", "报价单号无效！");
			return resultObj;
		}else{
			if("E".equals(status)){
				//已退回的报价单需要生成一条已回退的报价单代办任务
				// 为首节点创建任务，通过his表获取首节点信息
				TaskParamsDO query = new TaskParamsDO();
				query.setSdata1(quote.getId());
				query.setNode(1);
				TaskDO taskHis = taskMapper.findTaskHisByParams(query);
				if (taskHis != null) {
					// 生成首节点信息
					TaskDO taskDO = new TaskDO();
					taskDO.setActionUrl(taskHis.getActionUrl());
					taskDO.setId(UUIDUtil.getUUID());
					taskDO.setNode(1);
					taskDO.setExecutor(taskHis.getExecutor());
					taskDO.setExecutorName(taskHis.getExecutorName());
					taskDO.setCreateTime(new Date());
					taskDO.setCreator(1);
					taskDO.setCreatorName("admin1");
					taskDO.setProcessCode(taskHis.getProcessCode());
					taskDO.setSdata1(taskHis.getSdata1());
					taskDO.setTaskName(taskHis.getTaskName());
					taskDO.setStatus("已回退");
					taskDO.setTaskType("back");
					taskDO.setTaskTypeDesc("回退任务");
					taskMapper.saveTask(taskDO);
				}
			}
			//修改报价单状态
			map.put("oaOpinion", dataObj.getString("message"));
			quoteMapper.updateQuoteStatus(map);
			
		}
		resultObj.put("STATUS", "S");
		resultObj.put("MESSAGE", "操作成功！");
		return resultObj;
	}

	@Override
	public int updateValidDate(List<QuoteMate> mates) {
		int num=0;
		for (QuoteMate quoteMate : mates) {
			 int count = quoteMapper.updateValidDate(quoteMate);
			 num+=count;
		}
		return num;
	}
	@Override
	public int updateQuoteFile(List<QuoteAttr> attrs,String quoteCode){
		quoteMapper.delAttrByQuoteCode(quoteCode);
		for (QuoteAttr quoteAttr : attrs) {
			 quoteAttr.setQuoteCode(quoteCode);
			 quoteMapper.insertAttr(quoteAttr);
		}
		return 1;
	}

	
	
	@Override
	public RestCode validDate(List<String> quoteCodes) {
		RestCode restCode=new RestCode();
		SysUserDO user = UserCommon.getUser();
		StringBuffer sb=new StringBuffer();
		for (String quoteCode : quoteCodes) {
			List<QuoteMate> mates = quoteMateMapper.getAllQuoteMateByQuoteCode(quoteCode);
			boolean flag=true;
			for (QuoteMate quoteMate : mates) {
				Date startDate = quoteMate.getStartDate();
				Date endDate = quoteMate.getEndDate();
				if(startDate==null || endDate==null){
					restCode.put("code", 1);
					flag=false;
				}
			}
			if(!flag){
				sb.append("请维护报价单"+quoteCode+"的物料的有效期！");
			}
			/*if("user".equals(user.getUserType())){
				List<QuoteAttr> attrList = quoteMapper.getQuoteAttrByQuoteCode(quoteCode);
				int size = attrList.size();
				if(size == 0 ){
					restCode.put("code", 1);
					sb.append("请维护报价单"+quoteCode+"的附件信息！");
				}
			}*/
		}
		restCode.put("msg", sb.toString());
		return restCode;
	}

	@Override
	public Quote getQuoteById(String id) {
		Quote quote = quoteMapper.getQuoteById(id);
		Map<String, Object> map = new HashMap<>();
		if(!StringUtils.isEmpty(quote.getQuoteBaseTwo())){
			map.put("docId", quote.getQuoteBaseTwo());
			Document doc = documentMapper.getDoc(map);
			quote.setQuoteBaseTwoName(doc.getRealName());
		}
		return quote;
	}

	@Override
	public List<QuoteAttr> getQuoteAttrByQuoteCode(String quoteCode) {
		List<QuoteAttr> list = quoteMapper.getQuoteAttrByQuoteCode(quoteCode);
		return list;
	}
	
	@Transactional
	@Override
	public RestCode deleteFile(List<String> docIds) {
		Map<String, Object> params=new HashMap<String,Object>();
		boolean flag =true;
		for (String docId : docIds) {
			params.put("docId", docId);
			Document doc = documentMapper.getDoc(params);
			boolean deleteFile=true;
			if(doc!=null){
				 deleteFile = FtpUtil.deleteFile(doc.getFileUrl(), doc.getFileName());
			}
			if(deleteFile){
				documentMapper.deleteDoc(docId);
			}else{
				flag=false;
			}
		}
		quoteMapper.delAttr(docIds);
		if(!flag){
			return RestCode.error("删除失败！");
		}
		return new RestCode();
	}
	
}
