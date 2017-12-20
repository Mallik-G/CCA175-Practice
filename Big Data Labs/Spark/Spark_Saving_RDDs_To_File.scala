val orders = sc.textFile("/public/retail_db/orders")

val orderCountByStatus = orders.
							map(order => (order.split(",")(3), 1)).
							reduceByKey((total, element) => total + element)

// Saving file in text file format							
orderCountByStatus.
	map(rec => rec._1 + "\t" + rec._2).
	saveAsTextFile("/user/varunu28/order_count_by_status1")


// Saving file in text file format with snappy compression
orderCountByStatus.
	saveAsTextFile("/user/varunu28/order_count_by_statusSnappy", 
		classOf[org.apache.hadoop.io.compress.SnappyCodec])

// Saving into different file formats using DataFrames
val ordersDf = sqlContext.read.json("/public/retail_db_json/orders")

// Saving as parquet
ordersDf.save("/user/varunu28/orders_parquet", "parquet")

// Reading as parquet
sqlContext.load("/user/varunu28/orders_parquet", "parquet").show()

// Saving as orc using write
ordersDf.write.orc("/user/varunu28/orders_orc")

// Reading as orc
sqlContext.read.orc("/user/varunu28/orders_orc").show()