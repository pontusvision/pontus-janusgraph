#!/bin/bash
export HADOOP_HOME=`pwd`
./bin/hbase --config conf master --localRegionServers=1 start
