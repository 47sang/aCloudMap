#!/bin/bash
source /etc/profile

pid=`ps -ef | grep port=1808 | grep -v grep | awk '{print $2}'`

echo "大盘云图->部署前的pid进程 :$pid"

# 关闭已经启动的jar进程
if [ -n "$pid" ]
then
  kill -9 $pid
else
  echo "大盘云图->进程没有启动"
fi
sleep 1s

nohup java -jar /opt/acloudmap/acloudmap-1.0.jar --server.port=1808 &

echo "大盘云图->脚本执行完毕"

#sleep 1s

#tail -f /opt/gali/nohup.out