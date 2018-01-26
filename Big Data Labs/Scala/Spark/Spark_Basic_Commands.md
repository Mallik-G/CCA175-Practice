#### Entering spark shell
spark-shell --master yarn --conf spark.ui.port=12654

#### Steps To Initialize Spark Context Programmatically
import org.apache.spark.{SparkConf, SparkContext}
val conf = new SparkConf().setAppName("Daily Revenue").setMaster("yarn-client")
val sc = new SparkContext(conf)

#### Get all conf parameters
sc.getConf.getAll.foreach(println)

#### Creating RDD from HDFS
val orders = sc.textFile("/public/retail_db/orders")
val order_items = sc.textFile("/public/retail_db/order_items")

#### Creating RDD from Local File System
val products = scala.io.Source.fromFile("/data/retail_db/products/part-00000").getLines.toList
val productsRDD = sc.parallelize(products)

#### Taking a sample from a RDD
orders.takeSample(true, 100)

#### Row level transformation (MAP => Get date from orders in Integer format)
orders.map(e => (e.split(",")(1).substring(0,10).replace("-", "")).toInt)

#### Creating a Key-Value pair using Map
orders.map(order => {
	val o = order.split(",")
	(o(0).toInt, o(1).substring(0,10).replace("-", "").toInt)
})

#### Row level transformation (FlatMap => WordCount)
val l = List("Hello","Spark can create distributed datasets","from any storage source supported by Hadoop")
val l_rdd = sc.parallelize(l)
val l_flatMap = l_rdd.flatMap(ele => ele.split(" "))
val word_count = l_flatMap.map(word => (word, "")).countByKey