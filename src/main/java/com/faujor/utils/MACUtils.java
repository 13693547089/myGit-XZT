package com.faujor.utils;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
 * 物理地址是48位，别和ipv6搞错了
 */
public class MACUtils {
	public static String getLocalMac(String ip) {
		// TODO Auto-generated method stub
		// 获取网卡，获取地址

		// byte[] mac =
		// NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		// StringBuffer sb = new StringBuffer("");
		// for (int i = 0; i < mac.length; i++) {
		// if (i != 0) {
		// sb.append("-");
		// }
		// // 字节转换为整数
		// int temp = mac[i] & 0xff;
		// String str = Integer.toHexString(temp);
		// if (str.length() == 1) {
		// sb.append("0" + str);
		// } else {
		// sb.append(str);
		// }
		// }
//		String macAddr = null;
//		try {
//			Process process = Runtime.getRuntime().exec("nbtstat -a " + ip);
//			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
//			Matcher matcher;
//			for (String strLine = br.readLine(); strLine != null; strLine = br.readLine()) {
//				matcher = pattern.matcher(strLine);
//				if (matcher.find()) {
//					macAddr = matcher.group();
//					break;
//				}
//			}
//			return macAddr;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return "未找到MAC地址";
	}
}
