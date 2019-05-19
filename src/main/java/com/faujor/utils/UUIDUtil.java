package com.faujor.utils;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * 生成32位的UUID
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
