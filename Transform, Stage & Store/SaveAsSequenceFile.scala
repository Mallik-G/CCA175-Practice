// To save data as a sequence file

import org.apache.hadoop.io._

sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/products").map(rec => (rec.split(",")(0).toInt,rec)).saveAsSequenceFile("/user/cloudera/product_seq")

// Reading a sequence file

sc.sequenceFile("/user/cloudera/product_seq", classOf[IntWritable],classOf[Text]).map(rec => rec.toString()).collect().foreach(println)
