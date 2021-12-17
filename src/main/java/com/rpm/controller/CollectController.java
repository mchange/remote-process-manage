package com.rpm.controller;

import com.rpm.bean.Consumer;
import com.rpm.bean.Process;
import com.rpm.bean.Provider;
import com.rpm.bean.Result;
import com.rpm.repository.ConsumerRepository;
import com.rpm.repository.ProcessRepository;
import com.rpm.repository.ProviderRepository;
import com.rpm.service.NetStatService;
import com.rpm.service.ProcessService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 收集服务$
 *
 * @author zhang.zw
 * @date: 2021-12-01 11:13
 **/
@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class CollectController {

	@Autowired
	private NetStatService netStatService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private ProviderRepository providerRepository;
	@Autowired
	private ProcessRepository processRepository;

	/**
	 * 接受远程脚本发送的信息
	 *
	 * @param host    主机IP
	 * @param process ps aux 信息
	 * @param netstat netstat -tanpl 信息
	 * @return
	 */
	@PostMapping(value = "/")
	public String collect(@RequestParam String host, @RequestParam MultipartFile process,
		@RequestParam MultipartFile netstat) throws IOException {
		String processContent = getZipContent(process.getInputStream());
		String netstatContent = getZipContent(netstat.getInputStream());
		processContent = processContent.replaceAll("\n", "");
		processContent = new String(Base64Utils.decodeFromString(processContent));
		netstatContent = netstatContent.replaceAll("\n", "");
		netstatContent = new String(Base64Utils.decodeFromString(netstatContent));
		return "save " + (netStatService.parseAndSave(host, netstatContent) && processService
			.parseAndSave(host, processContent));
	}

	String getZipContent(InputStream gis){
		GZIPInputStream gisProcess = null;
		try {
			gisProcess = new GZIPInputStream(gis);
			BufferedReader in = new BufferedReader(new InputStreamReader(gisProcess));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = in.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据provider信息查询consumer
	 *
	 * @param providerHost
	 * @param providerPid
	 * @param providerPort
	 * @return
	 */
	@GetMapping("find/provider")
	@ResponseBody
	public Result getProvider(@RequestParam(required = false) String providerHost,
		@RequestParam(required = false) String providerPid,
		@RequestParam(required = false) String providerPort) {
		return new Result(20000,
			providerRepository.findProviderWithConsumer(providerHost, providerPid, providerPort));
	}

	@GetMapping("find/provider/host")
	@ResponseBody
	public Result getProviderHosts() {
		return new Result(20000, providerRepository.findHosts());
	}

	@GetMapping("find/process")
	@ResponseBody
	public Result getProcess(Process process) {
		return new Result(20000, processService.findAll(process));
	}

	@GetMapping("find/provider/detail")
	@ResponseBody
	public Result getProviderDetail(@RequestParam(required = false) String providerHost,
		@RequestParam(required = false) String providerPort) {
		return new Result(20000, providerRepository.findProcessDetail(providerHost, providerPort));
	}


	@GetMapping("find/process/host")
	@ResponseBody
	public Result getProcessHosts() {
		return new Result(20000, processRepository.findHosts());
	}

	@GetMapping("update/provider/remark")
	@ResponseBody
	public Result updateProvider(Provider provider){
		return new Result(20000, providerRepository.updateRemark(provider.getHost(), provider.getPid(), provider.getRemark())+"");
	}

	@GetMapping("find/consumer")
	@ResponseBody
	public Result getConsumer(@RequestParam(required = false) String consumerHost,
		@RequestParam(required = false) String consumerPid,
		@RequestParam(required = false) String providerHost,
		@RequestParam(required = false) String providerPort) {
		return new Result(20000, consumerRepository.findConsumerWithProvider(consumerHost, consumerPid, providerHost, providerPort));
	}

	@GetMapping("update/consumer/remark")
	@ResponseBody
	public Result updateConsumer(Consumer consumer){
		return new Result(20000, consumerRepository.updateRemark(consumer.getHost(), consumer.getPid(), consumer.getRemark())+"");
	}

	@GetMapping("find/consumer/host")
	@ResponseBody
	public Result getConsumerHosts() {
		return new Result(20000, consumerRepository.findHosts());
	}
}
