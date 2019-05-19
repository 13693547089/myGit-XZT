package com.faujor.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
	
	/**
	 * 将一个list均分成n个list,主要通过偏移量来实现的
	 * @param source
	 * @return
	 */
	public static <T> List<List<T>> averageAssign(List<T> source,int n){
		List<List<T>> result=new ArrayList<List<T>>();
		int remaider=source.size()%n;  //(先计算出余数)
		int number=source.size()/n;  //然后是商
		int offset=0;//偏移量
		for(int i=0;i<n;i++){
			List<T> value=null;
			if(remaider>0){
				value=source.subList(i*number+offset, (i+1)*number+offset+1);
				remaider--;
				offset++;
			}else{
				value=source.subList(i*number+offset, (i+1)*number+offset);
			}
			result.add(value);
		}
		return result;
	}
	
	/**
	* 按指定大小，分隔集合，将集合按规定个数分为n个部分
	* 
	* @param list
	* @param len
	* @return
	*/
	public static List<List<?>> splitList(List<?> list, int len) {
		
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		
		List<List<?>> result = new ArrayList<List<?>>();
		
		int size = list.size();
		int count = (size + len - 1) / len;
		
		for (int i = 0; i < count; i++) {
		List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}
}
