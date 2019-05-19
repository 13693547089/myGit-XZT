package com.faujor.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * rest返回结果
 */
public class RestCode extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public RestCode() {
		put("code", 0);
		put("msg", "操作成功");
	}

	public static RestCode noUpdate() {
		return noUpdate(-1, "无更新");
	}

	public static RestCode noUpdate(String msg) {
		return noUpdate(500, msg);
	}

	public static RestCode noUpdate(int code, String msg) {
		RestCode r = new RestCode();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static RestCode error() {
		return error(1, "操作失败");
	}

	public static RestCode error(String msg) {
		return error(500, msg);
	}

	public static RestCode error(int code, String msg) {
		RestCode r = new RestCode();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static RestCode ok(String msg) {
		RestCode r = new RestCode();
		r.put("msg", msg);
		return r;
	}

	public static RestCode ok(int code, String msg) {
		RestCode r = new RestCode();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static RestCode ok(Map<String, Object> map) {
		RestCode r = new RestCode();
		r.putAll(map);
		return r;
	}

	public static RestCode ok() {
		return new RestCode();
	}

	public RestCode put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
