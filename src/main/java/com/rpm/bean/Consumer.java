package com.rpm.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 服务调用方$
 *
 * @author zhang.zw
 * @date: 2021-12-01 11:05
 **/
@Entity
@Table(name = "consumer")
public class Consumer {
	@Id
	@GenericGenerator(name = "system-uuid3", strategy ="uuid")
	@GeneratedValue(generator = "system-uuid3")
	private String id;
	private String host;
	private String pid;
	private String program;
	private String remoteHost;
	private String remotePort;
	private String creatDate;
	private String remark;

	public Consumer() {
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

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
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

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	@Override
	public String toString() {
		return "Consumer{" +
			"id='" + id + '\'' +
			", host='" + host + '\'' +
			", pid='" + pid + '\'' +
			", program='" + program + '\'' +
			", remoteHost='" + remoteHost + '\'' +
			", remotePort='" + remotePort + '\'' +
			", creatDate='" + creatDate + '\'' +
			", remark='" + remark + '\'' +
			'}';
	}
}
