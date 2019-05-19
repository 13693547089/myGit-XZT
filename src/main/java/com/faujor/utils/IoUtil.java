package com.faujor.utils;

import java.io.Closeable;

public class IoUtil {
	/**
	 * 关闭传递过来的流对象
	 * @param c1
	 * @param args
	 */
	public static void closeIo(Closeable... args) {
		for (int i = 0; i < args.length; i++) {
			Closeable closeable=args[i];
			if(closeable!=null){
				try {
					closeable.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
