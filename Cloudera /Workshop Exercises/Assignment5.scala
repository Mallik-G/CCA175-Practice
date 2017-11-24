//ORDERS
val orders=sc.textFile("file:///home/cloudera/Desktop/data/retail_db/orders")
case class Order(
  order_id: Int,
  order_date: String,
  order_customer_id: Int,
  order_status: String)

val ordersDF=orders.map(rec=>{
val b=rec.split(",") 
Order(b(0).toInt,b(1),b(2).toInt,b(3))
}).toDF

ordersDF.registerTempTable("orders")

//ORDER ITEMS
val order_items=sc.textFile("file:///home/cloudera/Desktop/data/retail_db/order_items")
case class OrderItem(
  order_item_id: Int,
  order_item_order_id: Int,
  order_item_product_id: Int,
  order_item_quantity: Int,
  order_item_subtotal: Float,
  order_item_product_price: Float)

val order_itemsDF=order_items.map(rec=>{
val b=rec.split(",")
OrderItem(b(0).toInt,b(1).toInt,b(2).toInt,b(3).toInt,b(4).toFloat,b(5).toFloat)
}).toDF

order_itemsDF.registerTempTable("order_items")

//PRODUCTS
val products=sc.textFile("file:///home/cloudera/Desktop/data/retail_db/products").filter(record => record.split(",")(0).toInt!=685) 
case class Product(
  product_id: Int,
  product_category_id: Int,
  product_name: String,
  product_description: String,
  product_price: Float,
  product_image: String)

val productsDF=products.map(rec=>{
val b=rec.split(",")
Product(b(0).toInt,b(1).toInt,b(2),b(3),b(4).toFloat,b(5))
}).toDF

productsDF.registerTempTable("products")

//CATEGORIES
val categories=sc.textFile("file:///home/cloudera/Desktop/data/retail_db/categories")
case class Category(
  category_id: Int,
  category_department_id: Int,
  category_name: String)

val categoriesDF=categories.map(rec=>{
val b=rec.split(",")
Category(b(0).toInt,b(1).toInt,b(2))
}).toDF

categoriesDF.registerTempTable("categories")

//DEPARTMENTS
val departments=sc.textFile("file:///home/cloudera/Desktop/data/retail_db/departments")
case class Department(
  department_id: Int,
  department_name: String)

val departmentsDF=departments.map(rec=>{
val b=rec.split(",")
Department(b(0).toInt,b(1))
}).toDF

departmentsDF.registerTempTable("departments")

// Query
sqlContext.sql("SELECT department_name, order_date, SUM(order_item_subtotal) FROM orders JOIN order_items ON order_id=order_item_order_id JOIN products ON order_item_product_id=product_id JOIN categories ON product_category_id=category_id JOIN departments ON category_department_id=department_id WHERE order_status='COMPLETE' OR order_status='CLOSED' GROUP BY order_date,department_name ORDER BY order_date").collect().foreach(println)
