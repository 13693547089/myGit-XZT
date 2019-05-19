package com.faujor.utils;


public class StringUtil {
	
	public static final String arr[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
									  "AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ",
									  "BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"};
	/**
	 * 将百分比转换double
	 * @param str
	 * @return
	 */
	public static double getDoubleValue(String str){
		double val=0;
		if(str!=null && !"".equals(str)){
			String strVal = str.substring(0, str.length()-1);
			val=Double.parseDouble(strVal)/100;
		}
		return val;
	}
	/**
	 * 判断两个字符串是否equal 都为空 为true
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1,String str2){
		if(str1==null && str2==null){
			return true;
		}
		if(str1==null || str2==null){
			return false;
		}
		return str1.equals(str2);
	}
	/**
	 * 根据传入的下标回去Excel的列
	 * @param index
	 * @return
	 */
	public static String getExcelStrByIndex(int index){
		return arr[index];
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str){
		if(str==null){
			return true;
		}
		if("".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotNullOrEmpty(String str){
		return !isNullOrEmpty(str);
	}
}
