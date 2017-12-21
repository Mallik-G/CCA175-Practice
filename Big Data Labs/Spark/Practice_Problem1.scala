// Launch Spark
spark-shell --master yarn \
--num-executors 1 \
--executor-memory 512M \
--conf spark.ui.port=12436

// Read orders data
val orders = sc.textFile("/public/retail_db/orders")

// Read order_items data
val order_items = sc.textFile("/public/retail_db/order_items")

// Filtering based on order status
val ordersFiltered = orders.
                        filter(order => (order.split(",")(3) == "CLOSED" || order.split(",")(3) == "COMPLETE"))

// Map to get order id and order date
val ordersMap = ordersFiltered.map(order => (order.split(",")(0).toInt, order.split(",")(1).split(" ")(0)))

// Map to get order id as key and (product id and subtotal) as value
val order_itemsMap = order_items.map(oi => (oi.split(",")(1).toInt, (oi.split(",")(2).toInt, oi.split(",")(4).toFloat)))

// Joining two maps
val ordersJoin = ordersMap.join(order_itemsMap)

// Mapping ordersJoin
val ordersJoinMap = ordersJoin.map(rec => ((rec._2._1, rec._2._2._1), rec._2._2._2))

val dailyRevenuePerProductId = ordersJoinMap.
                                 reduceByKey((revenue, orderItemSubtotal) => revenue + orderItemSubtotal)

import scala.io.Source

// Read products data
val productsRaw = Source.fromFile("/data/retail_db/products/part-00000").getLines.toList

val products = sc.parallelize(productsRaw)

// Map products to get product id and product name
val productsMap = products.map(p => (p.split(",")(0).toInt,p.split(",")(2)))

// Map dailyRevenuePerProductId
val dailyRevenuePerProductIdMap = dailyRevenuePerProductId.
                                     map(rec => (rec._1._2, (rec._1._1, rec._2)))

// Join dailyRevenuePerProductIdMap and productsMap
val dailyRevenuePerProductJoin = dailyRevenuePerProductIdMap.join(productsMap)

// Sort the data based on date in asc and revenue in desc
val dailyRevenuePerProductSorted = dailyRevenuePerProductJoin.
                    map(rec => ((rec._2._1._1, -rec._2._1._2), (rec._2._1._1, rec._2._1._2, rec._2._2))).
                    sortByKey()

// Get data in desired formate: date, revenue_per_prod, product_name
val dailyRevenuePerProduct = dailyRevenuePerProductSorted.
                                map(rec => rec._2._1 + ", " + rec._2._2 + ", " + rec._2._3)

// Saving the output in HDFS
dailyRevenuePerProduct.saveAsTextFile("/user/varunu28/daily_revenue_text_scala")

// Reading the saved file
sc.textFile("/user/varunu28/daily_revenue_text_scala").take(10).foreach(println)

// Saving file to local file system from HDFS
hadoop fs -get /user/varunu28/daily_revenue_text_scala /home/varunu28/daily_revenue_scala/.