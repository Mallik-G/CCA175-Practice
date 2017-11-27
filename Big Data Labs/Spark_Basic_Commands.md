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

#### Creating RDD from Local File System
val products = scala.io.Source.fromFile("/data/retail_db/products/part-00000").getLines.toList
val productsRDD = sc.parallelize(products)

#### Taking a sample from a RDD
orders.takeSample(true, 100)