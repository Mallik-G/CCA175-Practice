val orders = sc.textFile("file:///home/cloudera/Desktop/data/retail_db/orders")
val ordersMap = orders.map((rec => (rec.split(",")(3), 1)))
val ordersMapReduceByKey = ordersMap.reduceByKey((agg, value) => agg+value)

// Print the result
ordersMapReduceByKey.collect().foreach(println)
