sqoop import \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--table products \
--target-dir /user/varunu28/products \
--fields-terminated-by '|'

hadoop fs -mkdir /user/varunu28/problem2
hadoop fs -mkdir /user/varunu28/problem2/products
hadoop fs -mv /user/varunu28/products/ /user/varunu28/problem2/products/
hadoop fs -chmod 765 /user/varunu28/problem2/products

// Launching the spark-shell specific to avro format
spark-shell --packages com.databricks:spark-avro_2.10:2.0.1
import com.databricks.spark.avro._
import sqlContext.implicits._

// Reading data from HDFS
val products = sc.textFile("/user/varunu28/problem2/products").map(x=> {var d = x.split('|'); (d(0).toInt,d(1).toInt,d(2).toString,d(3).toString,d(4).toFloat,d(5).toString)});

// Case class for preparing DataFrames
case class Product(product_id: Int, product_category_id: Int, product_name: String, product_description: String, product_price: Float, product_image: String)

// Creating the dataframe and registering the table
val productsDF = products.map(x=>Product(x._1,x._2,x._3,x._4,x._5,x._6)).toDF()
productsDF.registerTempTable("products")

// Main Query
val sqlResult = sqlContext.sql("SELECT product_category_id,MAX(product_price) AS max,COUNT(DISTINCT product_id) AS total,ROUND(AVG(product_price), 2) AS avg,MIN(product_price) AS min FROM products WHERE product_price<100 GROUP BY product_category_id")

// Saving the output
sqlContext.setConf("spark.sql.avro.compression.codec","snappy")
sqlResult.write.avro("/user/varunu28/problem2/products/resultSQL")