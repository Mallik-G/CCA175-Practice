#### Load data in a hive table from local file

load data local inpath 
'/home/cloudera/Desktop/CCA175 Commands/data/retail_db/orders'
into table orders;

#### Creating hive context in spark shell
import org.apache.spark.sql.hive.HiveContext
val hiveContext = new HiveContext(sc)
