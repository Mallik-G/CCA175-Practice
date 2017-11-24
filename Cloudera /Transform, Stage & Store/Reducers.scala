// Reduce By Key Example

val orders = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/orders")
val ordersMap = orders.map(rec => (rec.split(",")(0),rec.split(",")(1)))

val order_items = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/order_items")
val order_itemsMap = order_items.map(rec => (rec.split(",")(1),rec.split(",")(4).toDouble))

val ordersJoin = ordersMap.join(order_itemsMap)
val ordersJoinMap = ordersJoin.map(rec => rec._2)

ordersJoinMap.reduceByKey((agg,value) => agg+value).take(5).foreach(println)

// Aggregate By Key Example

val orders = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/orders")
val ordersMap = orders.map(rec => (rec.split(",")(0),rec.split(",")(1)))

val order_items = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/order_items")
val order_itemsMap = order_items.map(rec => (rec.split(",")(1),rec.split(",")(4).toDouble))

val ordersJoin = ordersMap.join(order_itemsMap)
val ordersJoinMap = ordersJoin.map(rec => rec._2)

val ordersJoinMapABK = ordersJoinMap.aggregateByKey((0.0,0))(
(intAgg,intVal) => (intAgg._1 + intVal, intAgg._2+1),
(totAgg, totVal) => (totAgg._1 + totVal._1,totAgg._2+totVal._2)
)

ordersJoinMapABK.take(5).foreach(println)

