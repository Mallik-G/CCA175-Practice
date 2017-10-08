case class Orders(
order_id: Int,
order_date: String,
order_customer_id: Int,
order_status: String);

case class OrderItems(
order_item_id: Int,
order_item_order_id: Int,
order_item_product_id: Int,
order_item_quantity: Int,
order_item_subtotal: Double,
order_item_price: Double)

// Orders DF

val orders = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/orders")

val ordersDF = orders.map(rec => {
val r = rec.split(",")
Orders(r(0).toInt,r(1),r(2).toInt,r(3))
}).toDF


// Order Items DF

val order_items = sc.textFile("file:///home/cloudera/Desktop/CCA175 Commands/data/retail_db/order_items")

val orders_itemsDF = order_items.map(rec => {
val r = rec.split(",")
OrderItems(r(0).toInt,r(1).toInt,r(2).toInt,r(3).toInt,r(4).toDouble,r(5).toDouble)
}).toDF

// Quering the DF
ordersDF.registerTempTable("orders")
sqlContext.sql("select * from orders limit 10").collect().foreach(println)

// Filter in DF
val ordersFiltered = ordersDF.filter(ordersDF("order_status") === "COMPLETE" || ordersDF("order_status") === "CLOSED")

// Join in DF
val orderJoin = ordersFiltered.Join(order_itemsDF, ordersFiltered("order_id") === order_itemsDF("order_item_order_id"))

// Group By in DF
orderJoin.groupBy("order_date").
agg(sum("order_item_subtotal")).
show()

// Doing the above operation using SQL
ordersDF.registerTempTable("orders")
order_itemsDF.registerTempTable("order_items")

sqlContext.sql("SELECT order_date, SUM(order_item_subtotal) FROM orders JOIN order_items ON order_id = order_item_order_id WHERE order_status IN ('COMPLETED','CLOSED') GROUP BY order_date").show()
