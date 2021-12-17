package com.rpm.service;

import com.rpm.bean.Consumer;
import com.rpm.bean.Provider;
import com.rpm.repository.ConsumerRepository;
import com.rpm.repository.ProviderRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.rpm.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 解析netstat信息$
 *
 * @author zhang.zw
 * @date: 2021-12-01 12:00
 **/
@Service
public class NetStatService {

	@Autowired
	private ProviderRepository providerRepository;
	@Autowired
	private ConsumerRepository consumerRepository;

	public boolean parseAndSave(String localhost, String data) {
		List<String> connections = Arrays.asList(data.split("\n"));

		HashMap<String, Provider> providerMap = new HashMap<>();
		HashMap<String, Consumer> consumerMap = new HashMap<>();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(new Date());

		for (String conn : connections) {
			String[] items = conn.split("\\s+");
			if (!items[0].startsWith("tcp")) {
				continue;
			}
			// 3：host、4：remote、6：pid
			String host = items[3].replaceAll("::ffff:","");
			String remote = items[4].replaceAll("::ffff:","");
			String pid = items[6];
			String program = "";
			if(!"-".equals(pid)){
				program = pid.split("\\/")[1];
				pid = pid.split("\\/")[0];
			}

			String hostIP = IPUtils.getIp(host);

			// 将监听本地端口的作为服务方
			if(IPUtils.isLoopback(hostIP)){
				// provider
				Provider provider = new Provider();
				provider.setHost(localhost);
				provider.setPort(IPUtils.getPort(host));
				provider.setPid(pid);
				provider.setProgram(program);
				provider.setCreatDate(today);

				String key = provider.getPort();
				if(!providerMap.containsKey(key)){
					providerMap.put(key, provider);
				}

			}else{
				// consumer
				// 只收集内网的连接
				if(!IPUtils.isInternalIp(IPUtils.getIp(remote))){
					continue;
				}
				Consumer client = new Consumer();
				client.setHost(localhost);
				client.setPid(pid);
				client.setProgram(program);
				client.setRemoteHost(IPUtils.getIp(remote));
				client.setRemotePort(IPUtils.getPort(remote));
				client.setCreatDate(today);

				String key = client.getPid() + "-" + remote;
				// 存储一个服务的多个线程连接同一个远程服务，此处去重
				if(!consumerMap.containsKey(key)){
					consumerMap.put(key, client);
				}

			}
		}

		providerRepository.saveAll(providerMap.values());
		consumerRepository.saveAll(consumerMap.values());

		return true;
	}


}
