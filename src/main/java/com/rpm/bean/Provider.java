package com.rpm.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 服务提供方$
 *
 * @author zhang.zw
 * @date: 2021-12-01 11:05
 **/
@Entity
@Table(name = "provider")
public class Provider {

	@Id
	@GenericGenerator(name = "system-uuid1", strategy ="uuid")
	@GeneratedValue(generator = "system-uuid1")
	private String id;
	private String host;
	private String port;
	private String pid;
	private String program;
	private String creatDate;
	private String remark;

	public Provider() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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
		return "Provider{" +
			"id='" + id + '\'' +
			", host='" + host + '\'' +
			", port='" + port + '\'' +
			", pid='" + pid + '\'' +
			", program='" + program + '\'' +
			", creatDate='" + creatDate + '\'' +
			", remark='" + remark + '\'' +
			'}';
	}
}
