#### Import orders table from mysql as text file to the destination /user/varunu28/problem5/text. Fields should be terminated by a tab character ("\t") character and lines should be terminated by new line character ("\n"). 

```
sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table orders \
--target-dir /user/varunu28/problem5/text \
--fields-terminated-by "\t" \
--lines-terminated-by "\n"
```

#### Import orders table from mysql  into hdfs to the destination /user/varunu28/problem5/avro. File should be stored as avro file.

```
sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table orders \
--target-dir /user/varunu28/problem5/avro \
--as-avrodatafile
```

#### Import orders table from mysql  into hdfs  to folders /user/varunu28/problem5/parquet. File should be stored as parquet file

```
sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table orders \
--target-dir /user/varunu28/problem5/parquet \
--as-parquetfile
```

## Transform/Convert data-files at /user/varunu28/problem5/avro and store the converted file at the following locations and file formats

#### save the data to hdfs using snappy compression as parquet file at /user/varunu28/problem5/parquet-snappy-compress
#### save the data to hdfs using gzip compression as text file at /user/varunu28/problem5/text-gzip-compress
#### save the data to hdfs using no compression as sequence file at /user/varunu28/problem5/sequence
#### save the data to hdfs using snappy compression as text file at /user/varunu28/problem5/text-snappy-compress

```
// Launching the spark-shell specific to avro format
spark-shell --packages com.databricks:spark-avro_2.10:2.0.1
import com.databricks.spark.avro._
import sqlContext.implicits._

var dataFile = sqlContext.read.avro("/user/varunu28/problem5/avro")
sqlContext.setConf("spark.sql.parquet.compression.codec","snappy")

dataFile.repartition(1).write.parquet("/user/varunu28/problem5/parquet-snappy-compress")

dataFile.
    map(x=> x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)).
    saveAsTextFile("/user/varunu28/problem5/text-gzip-compress",
                classOf[org.apache.hadoop.io.compress.GzipCodec])

dataFile.
    map(x=> (x(0).toString,x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3))).
    saveAsSequenceFile("/user/varunu28/problem5/sequence")

dataFile.
    map(x=> x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)).
    saveAsTextFile("/user/varunu28/problem5/text-snappy-compress",
    classOf[org.apache.hadoop.io.compress.SnappyCodec])
```


## Transform/Convert data-files at /user/varunu28/problem5/parquet-snappy-compress and store the converted file at the following locations and file formats

#### save the data to hdfs using no compression as parquet file at /user/varunu28/problem5/parquet-no-compress
#### save the data to hdfs using snappy compression as avro file at /user/varunu28/problem5/avro-snappy

```
val dataFile = sqlContext.read.parquet("/user/varunu28/problem5/parquet-snappy-compress")
sqlContext.setConf("spark.sql.parquet.compression.codec","uncompressed")

dataFile.write.parquet("/user/varunu28/problem5/parquet-no-compress")

sqlContext.setConf("spark.sql.avro.compression.codec","snappy")
dataFile.write.avro("/user/varunu28/problem5/avro-snappy", "snappy")
```

## Transform/Convert data-files at /user/varunu28/problem5/avro-snappy and store the converted file at the following locations and file formats
#### save the data to hdfs using no compression as json file at /user/varunu28/problem5/json-no-compress
#### save the data to hdfs using gzip compression as json file at /user/varunu28/problem5/json-gzip

```
val avroDataFile = sqlContext.read.avro("/user/varunu28/problem5/avro-snappy")
sqlContext.setConf("spark.sql.avro.compression.codec","uncompressed")

avroDataFile.write.json("/user/varunu28/problem5/json-no-compress")

sqlContext.setConf("spark.sql.json.compression.codec","gzip")

avroDataFile.write.json("/user/varunu28/problem5/json-gzip")
```

## Transform/Convert data-files at  /user/varunu28/problem5/json-gzip and store the converted file at the following locations and file formats

#### save the data to as comma separated text using gzip compression at   /user/varunu28/problem5/csv-gzip

```
val jsonFile = sqlContext.read.json("/user/varunu28/problem5/json-gzip")

jsonFile.
    map(rec => rec(0)+","+rec(1)+","+rec(2)+","+rec(3)).
    saveAsTextFile("/user/varunu28/problem5/csv-gzip", 
                    classOf[org.apache.hadoop.io.compress.GzipCodec])
```