package com.rpm.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果$
 *
 * @author zhang.zw
 * @date: 2021-12-02 14:44
 **/
public class Result {
	private long code;
	private Map<String, Object> data = new HashMap<>();
	private String msg;

	public Result(long code, String msg){
		this.code = code;
		this.msg = msg;

	}
	public Result(long code, List items){
		this.code = code;
		this.data.put("total", data.size());
		this.data.put("items", items);
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
