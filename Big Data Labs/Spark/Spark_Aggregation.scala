val orders = sc.textFile("/public/retail_db/orders")
val order_items = sc.textFile("/public/retail_db/order_items")

// Aggregations - using Actions
orders.map(order => (order.split(",")(3),"")).countByKey.foreach(println)
val order_items_revenue = order_items.map(order_item => order_item.split(",")(4).toFloat)
val totalRevenue = order_items_revenue.reduce((a,v) => a + v)
val maxRevenue = order_items_revenue.reduce((a,v) => {(if (a < v) v else a)})
val minRevenue = order_items_revenue.reduce((a,v) => {(if (a > v) v else a)})

// Aggregations - GroupBy Key
val order_itemsMap = order_items.map(oi => (oi.split(",")(1).toInt, oi.split(",")(4).toFloat))
val order_itemsGBK = order_itemsMap.groupByKey
order_itemsGBK.map(rec => (rec._1,rec._2.toList.sum)).take(10).foreach(println)

// Sort in ascending order of values
val ordersSortedByRevenue = order_itemsGBK.flatMap(rec => {rec._2.toList.sortBy(o => -o).map(k => (rec._1,k))})
ordersSortedByRevenue.take(10).foreach(println)