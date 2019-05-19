package com.faujor.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {
	/**
	 * 获取double类型的值
	 * @param num
	 * @return
	 */
	public static Double getDoubleVal(BigDecimal num){
		if(num==null){
			return 0D;
		}else{
			return num.doubleValue();
		}
	}
	/**
	 * 获取double类型的值
	 * @param num
	 * @return
	 */
	public static Double getDoubleVal2(BigDecimal num){
		if(num!=null){
			return num.doubleValue();
		}
		return null;
	}
	/**
	 * 判断是否为空  为空则返回0 否则返回本身
	 * @param num
	 * @return
	 */
	public static BigDecimal nullToBg0(BigDecimal num){
		if(num==null){
			return new BigDecimal(0);
		}else{
			return num;
		}
	}
	/**
	 * 加
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal add(BigDecimal num1,BigDecimal num2){
		if(num1==null){
			num1=new BigDecimal(0);
		}
		if(num2==null){
			num2=new BigDecimal(0);
		}
		return num1.add(num2);
	}
	
	/**
	 * 多个数相加
	 * @param nums
	 * @return
	 */
	public static BigDecimal addNums (BigDecimal... nums){
		BigDecimal result = new BigDecimal(0);
		for (BigDecimal num : nums) {
			if (num == null) {
				num = BigDecimal.ZERO;
			}
			result=result.add(num);
		}
		return result;
	}
	
	/**
	 * 相加并设置精度
	 * @param num1
	 * @param num2
	 * @param scale
	 * @return
	 */
	public static BigDecimal addAndScale(BigDecimal num1,BigDecimal num2,int scale){
		if(num1==null){
			num1=new BigDecimal(0);
		}
		if(num2==null){
			num2=new BigDecimal(0);
		}
		return num1.add(num2).setScale(scale,BigDecimal.ROUND_HALF_DOWN);
	}
	/**
	 * num1 占 num 2的百分比
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static String getPercentage(BigDecimal num1, BigDecimal num2) {
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal bg100 = new BigDecimal(100);
		String percentage = "";
		if (num1 == null) {
			num1 = bg0;
		}
		if (num2 == null) {
			num2 = bg0;
		}
		if (num2.compareTo(bg0) != 0) {
			percentage = num1.divide(num2, 4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2) + "%";
		}
		return percentage;
	}
	/**
	 * 返回num1-num2
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal subtract (BigDecimal num1, BigDecimal num2){
		BigDecimal bg0 = new BigDecimal(0);
		if (num1 == null) {
			num1 = bg0;
		}
		if (num2 == null) {
			num2 = bg0;
		}
		return num1.subtract(num2);
	}
	
	
	/**
	 * 乘法
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal multiply (BigDecimal... nums){
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal result = new BigDecimal(1);
		for (BigDecimal num : nums) {
			if (num == null) {
				num = bg0;
			}
			result=result.multiply(num);
		}
		return result;
	}
	/**
	 * 获取传入参数总和 空则记为0
	 * @param nums
	 * @return
	 */
	public static BigDecimal getSum (BigDecimal... nums){
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		for (BigDecimal num : nums) {
			if (num == null) {
				num = bg0;
			}
			result=result.add(num);
		}
		return result;
	}
	/**
	 * 获取传入参数最大值 空则记为0
	 * @param nums
	 * @return
	 */
	public static BigDecimal getMax (BigDecimal... nums){
		BigDecimal bg0 = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		for (BigDecimal num : nums) {
			if (num == null) {
				num = bg0;
			}
			result=result.compareTo(num)>=0?result:num;
		}
		return result;
	}
	/**
	 * 判断两个BigDecimal值是否相等(空按照0处理)
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static boolean equalVal(BigDecimal num1,BigDecimal num2){
		BigDecimal bg0 = new BigDecimal(0);
		if (num1 == null) {
			num1 = bg0;
		}
		if(num2==null){
			num2=bg0;
		}
		if(num1.compareTo(num2)!=0){
			return false;
		}
		return true;
	}
} 
