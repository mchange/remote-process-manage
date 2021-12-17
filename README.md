
[RPM 管理前台](https://github.com/mchange/rpm-web)

> 事先声明：如果你在阅读本文的过程中产生任何异议，不要怀疑，你一定是对的。

事情的起因是我们的系统前几天刚经历了一次严重的事故。准确的说这是我们帮其他公司（B 公司）维护的一套系统，它托管的 IDC 机房的维护人员误将机柜的电源断电，导致所有服务器下线了。看到这里如果你想笑，那就笑吧。

B 公司的这套系统是我们刚接手维护的，唯一的一份应用部署文档的修改日期还是几年前。就在事故发送的前一天下午，我逐一登录 90 多台服务器，由于时间紧，我没有逐个核对验证文档的准确性，而是先用`ps`命令将进程信息保存到了文件里。真是无巧不成书，第二天我们就是参照着这个进程快照文件才得以将系统完整复原。

如今的经济形式不容乐观，不少半大不小的公司在苦苦维护系统的时候都选择压缩人工成本。伴随着人员的离职，系统的脉络开始模糊、细节开始丢失，不可避免的开始腐化。作为从业人员，我们需要一个快速有效的办法来面对这一现状。

# 2. 设计思路

## 2.1 思路

基本原则：**（尽量）不要在服务器上做（过多的）修改。**

最小目标：**确定部署命令和位置；尽量梳理依赖关系**。

![](https://static001.geekbang.org/infoq/c7/c7fa883b48eb3918e08b2f06cea00dcb.png)

## 2.2 方案

> Linux 中的 ps 命令是 Process Status 的缩写。ps 命令用来列出系统中当前运行的那些进程。

> netstat 是一个基于命令行界面的网络实用工具，可显示当前的网络状态，包括传输控制协议层的连线状况、路由表、网络接口状态和网络协议的统计信息等。

### 远程采集

在每台服务器上部署一个采集脚本。通过`ps`命令收集进程信息，通过`netstat`命令收集网络信息，将两组信息发送给本地的 web 系统，由 web 系统进行解析和统计。 这样我们就实现了**最小的服务器端改动**。

![](https://static001.geekbang.org/infoq/91/910168b3771c759eda056ae201d1cbfa.png)

**采集脚本：**
```
    #!/bin/bash

    host=''

    if [ ! $1 ]; then
      echo "monitor 127.0.0.1"
      exit 1
    else
      host=$1
    fi  

    # 将输出信息先base64然后在gzip压缩
    running_process=`ps aux | base64 -w 0 | gzip > ./running_process.gz`

    netstat=`netstat -tanpl | base64 -w 0 | gzip > ./netstat.gz`

    curl -X POST -F "host=$host" -F "process=@running_process.gz" -F "netstat=@netstat.gz" http://your.website

```

### 本地解析

1. **解析网络信息**，根据 State 状态解析出当前进程的角色，`LISTEN` 状态的进程定义为“**服务方（Provider）**”；其他的进程为“**消费方（Consumer）**”；
2. **解析进程信息**，抽象成“**进程（Process）**”对象；
3. 通过关键字段构建服务方、消费方和进程之间的联系。

![](https://static001.geekbang.org/infoq/cb/cbd0ab3254c307a0afe81add50316884.png)

# 3\. 实现效果

![](https://static001.geekbang.org/infoq/ea/ea430dcb01eda7a0eda92cad581c175b.png)

![](https://static001.geekbang.org/infoq/f3/f3efde34fded48a3bbd8c00f926321fd.png)

![](https://static001.geekbang.org/infoq/ce/ce303644eabbce38d25be13bc0f58aaf.png)

![](https://static001.geekbang.org/infoq/c3/c3bc471ca4e7741ec543e5ec07585e3b.png)

# 4\. 技术要点

## 4.1 技术栈

### 前端

* [vue-admin-template](https://github.com/PanJiaChen/vue-admin-template/)

### 后端

* Spring boot
* Jpa
* sqlite
* [DB Browser for SQLite](https://github.com/sqlitebrowser/sqlitebrowser)

## 4.2 技术要点

### 使用 gzip 压缩

如果服务器运行的进程较多，我们采集的信息就会很大，使用 curl post 发送的时候就会报错：
```
    -bash: /usr/bin/curl: Argument list too long
```


这时我们需要将结果先用 base64 转码然后再用 gzip 压缩成文件，最后通过 curl post 上传文件的方式，将信息传递给后台。
```
    running_process=`ps aux | base64 -w 0 | gzip > ./running_process.gz`
```


后台解压过程如下：
```java
    public String collect(@RequestParam String host, @RequestParam MultipartFile process,  
     @RequestParam MultipartFile netstat) throws IOException {  
       String processContent = getZipContent(process.getInputStream());  
       String netstatContent = getZipContent(netstat.getInputStream());
       processContent = processContent.replaceAll("\n", "");  
       processContent = new String(Base64Utils.decodeFromString(processContent));
    }

    // gzip解压缩
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

```

### vue 前后分离项目，后端使用 jpa 动态查询问题

例如前端定义 form 如下：
```
    // process表单数据

    form: {
      host: '',
      pid: '',
      userName: '',
      command:''
    }
```

后台 Process 定义如下：
```java
    public class Process {  

       private String id;  
       private String host;  
       private String pid;  
       private String userName;  
       private String startDate;  
       private String creatDate;  
       private String command;  
       private String remark;

       // getter&setter
    }

```

当我们通过 controller 接收到请求后，空字段是“”而不是 null。
```java
    public Result getProcess(Process process) {  
       return new Result(20000, processService.findAll(process));  
    }

    // 忽略空值
    ExampleMatcher.matching().withIgnoreNullValues()；

```

在 jpa 中，可以指定忽略为 null 的参数不作为查询条件，但是不能忽略空字符串，下面通过反射，将空字符串转换成 null，即可。
```java
    // 值替换
    Class clazz = process.getClass();  
    Field[] fields = clazz.getDeclaredFields();  
    for (Field field : fields) {  
       field.setAccessible(true);  
     try {  
          if ("".equals(String.valueOf(field.get(process)))) {  
             field.set(process, null);  
     }  
       } catch (Exception e) {  
          e.printStackTrace();  
     }  
    }

    // 忽略null值
    ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
    	// 模糊查询
      .withMatcher("command",GenericPropertyMatchers.contains());

    Example<Process> example = Example.of(process, matcher);
    List<Process> datas = processRepository.findAll(example);

```