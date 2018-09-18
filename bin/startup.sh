#!/usr/bin/env bash
bin=`dirname "$0"`
HUSKY_HOME=`cd "$bin/.."; pwd`

echo $HUSKY_HOME

nohup java -jar $HUSKY_HOME/lib/husky-1.0.0.jar > ../husky.log 2>&1 &

PID=`ps aux | grep $HUSKY_HOME | grep -v grep | awk '{print $2}'`

echo "start successfully, pid: $PID"
