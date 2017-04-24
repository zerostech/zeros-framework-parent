#!/usr/bin/env bash

BASE_DIR=$(cd `dirname $0`; pwd)
LOG_FILE="$BASE_DIR/log.log"
PID_FILE="$BASE_DIR/pid.pid"

APP_NAME="zeros-wechat-server"
APP_JAR="$APP_NAME.jar"

get_pid() {
    ps -ef|grep ${APP_NAME}|grep -v grep|grep -v kill|awk '{print $2}'
}

start() {
    echo 'Start begin'
    rm -f tpid
    nohup java -jar ${APP_JAR} > "$LOG_FILE" 2>&1 &
    echo $! > tpid
    echo 'Start Success!'
}

stop() {
    tpid=`get_pid`
    if [ ${tpid} ]; then
        echo 'Stop Process...'
        kill -15 $tpid
        sleep 5
    fi
    tpid=`get_pid`
    if [ ${tpid} ]; then
        echo 'Kill Process!'
        kill -9 $tpid
    else
        echo 'Stop Success!'
    fi
}

check() {
    tpid=`get_pid`
    if [ ${tpid} ]; then
        echo 'App is running.'
    else
        echo 'App is NOT running.'
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    check)
        check
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|check}"
        exit 1
esac
