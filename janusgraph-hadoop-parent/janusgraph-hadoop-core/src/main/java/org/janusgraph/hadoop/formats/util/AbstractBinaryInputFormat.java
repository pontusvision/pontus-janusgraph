// Copyright 2017 JanusGraph Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.janusgraph.hadoop.formats.util;

import org.janusgraph.diskstorage.Entry;
import org.janusgraph.diskstorage.StaticBuffer;
import org.janusgraph.diskstorage.configuration.ModifiableConfiguration;
import org.janusgraph.hadoop.config.ModifiableHadoopConfiguration;
import org.janusgraph.hadoop.config.JanusGraphHadoopConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.tinkerpop.gremlin.hadoop.structure.io.HadoopPoolsConfigurable;

public abstract class AbstractBinaryInputFormat extends InputFormat<StaticBuffer, Iterable<Entry>> implements HadoopPoolsConfigurable {

    protected Configuration hadoopConf;
    protected ModifiableHadoopConfiguration mrConf;
    protected ModifiableConfiguration janusgraphConf;


    /*
      LPPM - adding synchronized to fix this:

      [2018-11-13 13:14:32,088] WARN Lost task 6.0 in stage 1.0 (TID 56, 10.54.82.195, executor 0): java.util.ConcurrentModificationException
        at java.util.Hashtable$Enumerator.next(Hashtable.java:1387)
        at org.apache.hadoop.conf.Configuration.iterator(Configuration.java:2451)
        at org.apache.tinkerpop.gremlin.hadoop.structure.util.ConfUtil.makeApacheConfiguration(ConfUtil.java:39)
        at org.apache.tinkerpop.gremlin.hadoop.structure.io.HadoopPools.initialize(HadoopPools.java:56)
        at org.apache.tinkerpop.gremlin.hadoop.structure.io.HadoopPoolsConfigurable.setConf(HadoopPoolsConfigurable.java:33)
        at org.janusgraph.hadoop.formats.util.AbstractBinaryInputFormat.setConf(AbstractBinaryInputFormat.java:34)
        at org.janusgraph.hadoop.formats.hbase.HBaseBinaryInputFormat.setConf(HBaseBinaryInputFormat.java:69)
        at org.janusgraph.hadoop.formats.util.GiraphInputFormat.setConf(GiraphInputFormat.java:74)
        at org.apache.spark.rdd.NewHadoopRDD$$anon$1.<init>(NewHadoopRDD.scala:172)
        at org.apache.spark.rdd.NewHadoopRDD.compute(NewHadoopRDD.scala:134)
        at org.apache.spark.rdd.NewHadoopRDD.compute(NewHadoopRDD.scala:69)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:38)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:38)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:38)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:38)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:38)
        at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:323)
        at org.apache.spark.rdd.RDD.iterator(RDD.scala:287)
        at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:87)
        at org.apache.spark.scheduler.Task.run(Task.scala:108)
        at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:335)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)
 (org.apache.spark.scheduler.TaskSetManager)

     */
    @Override
    public synchronized  void setConf(final Configuration config) {
        HadoopPoolsConfigurable.super.setConf(config);
        this.mrConf = ModifiableHadoopConfiguration.of(JanusGraphHadoopConfiguration.MAPRED_NS, config);
        this.hadoopConf = config;
        this.janusgraphConf = mrConf.getJanusGraphConf();
    }

    @Override
    public Configuration getConf() {
        return hadoopConf;
    }
}
