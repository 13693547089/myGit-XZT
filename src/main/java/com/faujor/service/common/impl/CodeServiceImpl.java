package com.faujor.service.common.impl;

import java.text.DecimalFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.common.CodeMapper;
import com.faujor.entity.common.Code;
import com.faujor.service.common.CodeService;
import com.faujor.utils.DateUtils;



@Service
public class CodeServiceImpl implements CodeService {
	@Autowired
	private CodeMapper codeMapper;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public String getCodeByCodeType(String codeType) {
		StringBuffer sb=new StringBuffer();
		Code code = codeMapper.getCodeByCodeType(codeType);
		Date lastTime=code.getLastTime();
		Integer suffixNum = code.getSuffixNum();
		Integer suffixLength = code.getSuffixLength();
		String prefix = code.getPrefix();
		Date now=new Date();
		String currDateStr = DateUtils.format(now, "yyMM");
		String lastDateStr = DateUtils.format(lastTime, "yyMM");
		//获取编码
		//前缀
		sb.append(prefix);
		//添加时间戳字段
		sb.append(currDateStr);
		//获取后缀 如果当前日期与数据库日期不同 获取数据库的后缀值  数据库后缀更新为当前值加1 更新数据库时间为当前时间
		//如果当前日期与数据库日期相同 则当前后缀重置为1  数据库后缀更新为2  更新数据库时间为当前时间
		if(currDateStr.equals(lastDateStr)){
			String suffix=getSuffixStr(suffixNum, suffixLength);
			sb.append(suffix);
		}else {
			suffixNum=1;
			String suffix=getSuffixStr(suffixNum, suffixLength);
			sb.append(suffix);
		}
		code.setSuffixNum(suffixNum+1);
		code.setLastTime(now);
		codeMapper.updateCode(code);
		return sb.toString();
	}
	
	public String getSuffixStr(Integer num,Integer suffixLength){
		StringBuffer formatSb=new StringBuffer();
		for(int i=0;i<suffixLength;i++){
			formatSb.append("0");
		}
		DecimalFormat decimalFormat=new DecimalFormat(formatSb.toString());
		String str = decimalFormat.format(num);
		return str;
	}
}
