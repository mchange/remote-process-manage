package com.rpm.service;

import com.rpm.bean.Process;
import com.rpm.repository.ProcessRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.rpm.utils.BeanUtils;
import com.rpm.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

/**
 * 解析ps信息$
 *
 * @author zhang.zw
 * @date: 2021-12-01 12:01
 **/
@Service
public class ProcessService {

	@Autowired
	ProcessRepository processRepository;

	public boolean parseAndSave(String localhost, String data) {
		System.out.println(data);
		List<String> connections = Arrays.asList(data.split("\n"));
		List<Process> processes = new ArrayList<>();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(new Date());

		for (String conn : connections) {
			String[] items = conn.split("\\s+");
			if (items[0].startsWith("USER")) {
				continue;
			}

			String userName = items[0];
			String pid = items[1];
			String startDate = items[8];
			String command = StringUtils.join(items, 10, " ");

			// pid编号在1000以下的，以及系统进程。不做统计
			if(Long.parseLong(pid) < 1000 || command.startsWith("[") || "-".equals(pid)){
				continue;
			}


			Process process = new Process();
			process.setHost(localhost);
			process.setUserName(userName);
			process.setPid(pid);
			process.setStartDate(startDate);
			process.setCommand(command);
			process.setCreatDate(today);
			processes.add(process);
		}
		if (processes.size() > 0) {
			processRepository.saveAll(processes);
		}
		return true;
	}


	/**
	 * 动态查询
	 * @param process
	 * @return
	 */
	public List<Process> findAll(Process process) {

		BeanUtils.setEmptyToNull(process);

		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
			.withMatcher("command",
				GenericPropertyMatchers.contains());
		Example<Process> example = Example.of(process, matcher);
		List<Process> datas = processRepository.findAll(example);
		return datas;
	}


}
