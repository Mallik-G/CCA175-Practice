# Problem Scenario1

## Expected Intermediate Result: Order_Date , Order_status, total_orders, total_amount. In plain english, please find total orders and total amount per status per day. The result should be sorted by order date in descending, order status in ascending and total amount in descending and total orders in ascending.

#### Importing orders data
sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table orders \
--as-avrodatafile \
--compression-codec org.apache.hadoop.io.compress.SnappyCodec \
--target-dir /user/varunu28/problem1/orders

#### Importing order_items data
sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table order_items \
--as-avrodatafile \
--compression-codec org.apache.hadoop.io.compress.SnappyCodec \
--target-dir /user/varunu28/problem1/order-items

#### Launching the spark-shell specific to avro format
spark-shell --packages com.databricks:spark-avro_2.10:2.0.1
import com.databricks.spark.avro._
import sqlContext.implicits._

#### Reading data, creating dataframes and registering tables
val orders = sqlContext.read.avro("/user/varunu28/problem1/orders")
val order_items = sqlContext.read.avro("/user/varunu28/problem1/order-items")

#### Main Solution using Spark-SQL
orders.registerTempTable("orders")
order_items.registerTempTable("order_items")

val SQLresult = sqlContext.
sql("SELECT TO_DATE(FROM_UNIXTIME((order_date)/1000)) AS order_date, order_status, COUNT(order_id) AS total_orders, SUM(order_item_subtotal) AS total_amount FROM orders JOIN order_items ON order_id=order_item_order_id GROUP BY order_date, order_status ORDER BY order_date DESC, order_status, total_amount DESC, total_orders DESC")

#### Main Solution using DataFrame Operations
val ordersJoin = orders.join(order_items,orders("order_id")===order_items("order_item_order_id"))

val resultsDF = ordersJoin.
groupBy(to_date(from_unixtime(col("order_date")/1000)).alias("order_formatted_date"),col("order_status")).
agg(round(sum("order_item_subtotal"),2).alias("total_amount"),countDistinct("order_id").alias("total_orders")).
orderBy(col("order_formatted_date").desc,col("order_status"),col("total_amount").desc,col("total_orders"))

#### Config for parquet and gzip
sqlContext.setConf("spark.sql.parquet.compression.codec","gzip");

#### Saving the file
SQLresult.write.parquet("/user/varunu28/problem1/solution/sqlAns")