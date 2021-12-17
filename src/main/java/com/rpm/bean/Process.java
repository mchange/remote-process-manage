package com.rpm.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 服务端进程$
 *
 * @author zhang.zw
 * @date: 2021-12-01 11:06
 **/
@Entity
@Table(name = "process")
public class Process {

	@Id
	@GenericGenerator(name = "system-uuid2", strategy ="uuid")
	@GeneratedValue(generator = "system-uuid2")
	private String id;
	private String host;
	private String pid;
	private String userName;
	private String startDate;
	private String creatDate;
	private String command;
	private String remark;

	public Process() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	@Override
	public String toString() {
		return "Process{" +
			"id='" + id + '\'' +
			", host='" + host + '\'' +
			", pid='" + pid + '\'' +
			", userName='" + userName + '\'' +
			", startDate='" + startDate + '\'' +
			", creatDate='" + creatDate + '\'' +
			", command='" + command + '\'' +
			", remark='" + remark + '\'' +
			'}';
	}
}
