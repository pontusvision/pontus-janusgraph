#!/bin/bash
export HADOOP_HOME=`pwd`
./bin/hbase --config conf master --localRegionServers=1 --minRegionServers=1 --masters=1 start
