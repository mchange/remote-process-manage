```shell
#!/bin/bash

host=''

if [ ! $1 ]; then
	echo "monitor this-host-ip"
	exit 1
else
	host=$1
fi

# 将输出信息先base64然后在gzip压缩
running_process=`ps aux | base64 -w 0 | gzip > ./running_process.gz`

netstat=`netstat -tanpl | base64 -w 0 | gzip > ./netstat.gz`

curl -X POST -F "host=$host" -F "process=@running_process.gz" -F "netstat=@netstat.gz" http://yourdomain.com
```