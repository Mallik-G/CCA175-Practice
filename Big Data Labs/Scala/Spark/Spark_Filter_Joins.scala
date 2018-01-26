// Reading Data
val orders = sc.textFile("/public/retail_db/orders")
val order_items = sc.textFile("/public/retail_db/order_items")

// Filtering in Spark (Getting CLOSED & COMPLETE orders)
orders.filter(e => e.split(",")(3)=="COMPLETE" || e.split(",")(3)=="CLOSED")

// Inner Joins in Spark (Joining orders & order_items)
val ordersMap = orders.map(order => (order.split(",")(0).toInt, (order.split(",")(1).substring(0,10))))

val orderItemsMap = order_items.map(order_item => {
	val oi = order_item.split(",")
	(oi(1).toInt,oi(4).toFloat)
})

val ordersJoin = ordersMap.join(orderItemsMap)

// Outer Joins in Spark (Get all orders which do not have an entry order_items)

val ordersMap = orders.map(order => (order.split(",")(0).toInt, order))
val orderItemsMap = order_items.map(order_item => {
	val oi = order_item.split(",")
	(oi(1).toInt,order_item)
})

// Left Outer Join 
val ordersLeftOuterJoin = ordersMap.leftOuterJoin(orderItemsMap)
							.filter(order => order._2._2 == None)
							.map(order => order._2._1)

// Right Outer Join 
val ordersRightOuterJoin = orderItemsMap.rightOuterJoin(ordersMap)
							.filter(order => order._2._1 == None)
							.map(order => order._2._2)
							.take(10).foreach(println)
