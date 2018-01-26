/*
Get the customers who have not placed any orders, sorted by customer_lname and then customer_fname
*/

// Solve using Core RDD operations

import scala.io.Source

// Reading data from local file system
val ordersRaw = Source.fromFile("/data/retail_db/orders/part-00000").getLines.toList
val customersRaw = Source.fromFile("/data/retail_db/customers/part-00000").getLines.toList

// Converting to RDD
val orders = sc.parallelize(ordersRaw)
val customers = sc.parallelize(customersRaw)

val ordersMap = orders.map(rec => (rec.split(",")(2).toInt, 1))
val customersMap = customers.map(rec => (rec.split(",")(0).toInt, (rec.split(",")(2), rec.split(",")(1))))

val customersLeftOuterJoinOrders = customersMap.leftOuterJoin(ordersMap)

val inactiveCustomersSorted = customersLeftOuterJoinOrders.
                                filter(rec => rec._2._2 == None).
                                map(rec => rec._2).
                                sortByKey()

// Saving the output data
inactiveCustomersSorted.
    map(rec => rec._1._1 + ", " + rec._1._2).
    saveAsTextFile("/user/varunu28/inactiveConsumerExerciseSolution")

// Solve using DataFrame

// Creating the DataFrames and registering the tables
val ordersDF = orders.
                map(o => (o.split(",")(0).toInt, o.split(",")(1), o.split(",")(2).toInt, o.split(",")(3))).
                toDF("order_id", "order_date", "order_customer_id", "order_status")

ordersDF.registerTempTable("orders")
sqlContext.sql("SELECT * FROM orders").show

val customersDF = customers.
                    map(c => (c.split(",")(0).toInt, c.split(",")(1), c.split(",")(2))).
                    toDF("customer_id", "customer_fname", "customer_lname")

customersDF.registerTempTable("customers")
sqlContext.sql("SELECT * FROM customers").show

// Main query
sqlContext.
        sql("SELECT customer_fname, customer_lname " + 
            "FROM customers LEFT OUTER JOIN orders ON orders.order_customer_id=customers.customer_id " +
            "WHERE order_id IS NULL " +
            "ORDER BY customer_fname, customer_lname").
        rdd.
        map(rec => rec.mkString(", ")).
        saveAsTextFile("/user/varunu28/inactiveConsumerExerciseSqlSolution")