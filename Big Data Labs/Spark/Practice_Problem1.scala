// Read orders data
val orders = sc.textFile("/public/retail_db/orders")

// Read order_items data
val order_items = sc.textFile("/public/retail_db/order_items")

// Read products data
val products = sc.textFile("/public/retail_db/products")

// Filtering based on order status
val ordersFiltered = orders.
						filter(order => (order.split(",")(3) == "CLOSED" || order.split(",")(3) == "COMPLETE"))

// Map to get order id and order status
val ordersMap = ordersFiltered.map(order => (order.split(",")(0).toInt, order.split(",")(1).split(" ")(0)))

