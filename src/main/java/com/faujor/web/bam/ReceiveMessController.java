package com.faujor.web.bam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UserCommon;

@Controller
public class ReceiveMessController {

	@Autowired
	private ReceiveMessageService receiveMessageService;
	
	/**
	 * 跳转到收货信息列表页面
	 * @return
	 */
	@RequestMapping("/getReceiveMessageListHtml")
	public String getReceiveMessageListHtml(){
		return "bam/receMess/receMessList";
	}
	
	/**
	 * 收货列表数据
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取收货地址信息列表")
	@ResponseBody
	@RequestMapping("/queryReceiveMessByPage")
	public Map<String,Object> queryReceiveMessByPage(Integer limit,Integer page,ReceiveMessage receMess){
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		if(receMess!=null){
			String receUnit = receMess.getReceUnit();
			String freightRange = receMess.getFreightRange();
			if(StringUtil.isNotNullOrEmpty(receUnit)){
				map.put("receUnit", "%"+receUnit+"%");
			}
			if(StringUtil.isNotNullOrEmpty(freightRange)){
				map.put("freightRange", "%"+freightRange+"%");
			}
		}
		map.put("receMess", receMess);
		Map<String, Object> page2 = receiveMessageService.queryReceiveMessByPage(map );
		return page2;
	}
	/**
	 * 删除收货信息
	 * @param ids
	 * @return
	 */
	@Log(value ="删除收货地址信息")
	@ResponseBody
	@RequestMapping("/deleteReceMessById")
	public boolean deleteReceMessById(String[] ids){
		return receiveMessageService.deleteReceMessById(ids);
	}
	/**
	 * 跳转到新建/编辑收货信息页面
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getReceMessAddHtml")
	public String getReceMessAddHtml(String type,Model model,String id){
		ReceiveMessage receMess=null;
		if("1".equals(type)){
			receMess = new ReceiveMessage();
		}else{
			receMess = receiveMessageService.queryReceMessById(id);
		}
		model.addAttribute("type", type);
		model.addAttribute("receMess", receMess);
		return "bam/receMess/receMessAdd";
	}
	/**
	 * 新建保存，编辑保存收货信息
	 * @param receMess
	 * @return
	 */
	@Log(value ="新建/编辑收货地址信息")
	@ResponseBody
	@RequestMapping("/addReceiveMessage")
	public boolean addReceiveMessage(ReceiveMessage receMess,String type){
		SysUserDO user = UserCommon.getUser();
		if("1".equals(type)){
			receMess.setCreateId(user.getUserId().toString());
			receMess.setCreator(user.getName());
			receMess.setCreateDate(new Date());
			return receiveMessageService.addReceiveMessage(receMess);
		}else{
			receMess.setModifieId(user.getUserId().toString());
			receMess.setModifier(user.getName());
			receMess.setUdpateDate(new Date());
			return receiveMessageService.udpateReceiveMessage(receMess);
		}
	}
	
	
}
