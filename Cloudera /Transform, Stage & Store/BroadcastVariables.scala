// Broadcast variables

val inputBaseDir = "file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db"

val orders = sc.textFile(inputBaseDir + "/orders")
val ordersMap = orders.map(rec => (rec.split(",")(0).toInt,rec.split(",")(1)))

val bv = sc.broadcast(ordersMap.collectAsMap())

val order_itemsMap = sc.textFile(inputBaseDir + "/order_items").map(rec => (bv.value.get(rec.split(",")(1).toInt).get,rec.split(",")(4).toDouble))

order_itemsMap.reduceByKey(_+_).collect().foreach(println)
