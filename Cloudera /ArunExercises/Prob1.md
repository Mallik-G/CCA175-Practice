#### SQOOP Import
sqoop import --connect jdbc:mysql://127.0.0.1:3306/retail_db --username root --password cloudera --table orders --warehouse-dir /user/arunPrac/prob1 --as-avrodatafile --compression-codec org.apache.hadoop.io.compress.SnappyCodec

sqoop import --connect jdbc:mysql://127.0.0.1:3306/retail_db --username root --password cloudera --table order_items --warehouse-dir /user/arunPrac/prob1 --as-avrodatafile --compression-codec org.apache.hadoop.io.compress.SnappyCodec

#### Reading files as a dataframe
**import com.databricks.spark.avro._**
var ordersDF = sqlContext.read.avro("/user/arunPrac/prob1/orders");
var orderItemDF = sqlContext.read.avro("/user/arunPrac/prob1/order_items");

#### Finding intermediate result using SparkSQL
ordersDF.registerTempTable("orders")
orderItemDF.registerTempTable("order_items")

var sqlResult=sqlContext.sql("SELECT to_date(from_unixtime(cast(order_date/1000 as bigint))) order_formatted_date, order_status, count(DISTINCT(order_id)) total_orders, sum(order_item_subtotal) total_amount FROM orders JOIN order_items ON order_id=order_item_order_id GROUP BY to_date(from_unixtime(cast(order_date/1000 as bigint))),order_status ORDER BY order_formatted_date desc,order_status,total_amount desc,total_orders")

#### Storing as parquet with gzip
**sqlContext.setConf("spark.sql.parquet.compression.codec","gzip")**
sqlResult.write.parquet("/user/arunPrac/prob1/result4b-gzip")

#### Storing as parquet with snappy compression
**sqlContext.setConf("spark.sql.parquet.compression.codec","snappy")**
sqlResult.write.parquet("/user/arunPrac/prob1/result4b-snappy")

#### Storing as a csv file
sqlResult.map(rec => rec(0)+","+rec(1)+","+rec(2)+","+rec(3)).saveAsTextFile("/user/arunPrac/prob1/result4b-csv")
