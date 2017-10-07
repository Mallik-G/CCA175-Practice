// To save data as a hadoop file

import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce.lib.output._

sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/products").
map(rec => (new IntWritable(rec.split(",")(0).toInt),new Text(rec))).
saveAsNewAPIHadoopFile("/user/cloudera/product_hadoop_format",classOf[IntWritable],classOf[Text],classOf[SequenceFileOutputFormat[IntWritable,Text]])

// To read a hadoop file using newAPI

import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce.lib.input._

sc.newAPIHadoopFile("/user/cloudera/product_hadoop_format",classOf[SequenceFileInputFormat[IntWritable,Text]],classOf[IntWritable],classOf[Text]).
map(rec => rec.toString()).
collect().
foreach(println)
