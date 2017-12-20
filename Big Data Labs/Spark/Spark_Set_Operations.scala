val orders = sc.textFile("/public/retail_db/orders")

// Get customers who placed order in 2013 Aug
val cust2013Aug = orders.
						filter(order => order.split(",")(1).contains("2013-08")).
						map(rec => rec.split(",")(2).toInt)

// Get customers who placed order in 2013 Sep
val cust2013Sep = orders.
						filter(order => order.split(",")(1).contains("2013-09")).
						map(rec => rec.split(",")(2).toInt)

// Get customers who placed order in 2013 Aug and Sep
val cust2013SepAndAug = cust2013Aug.intersection(cust2013Sep)

// Get customers who placed order in 2013 Aug or Sep
val cust2013SepOrAug = cust2013Aug.union(cust2013Sep).distinct

// Get customers who placed order in 2013 Aug but not in Sep
val cust2013AugButNotSep = cust2013Aug.map(c => (c, 1)).
								leftOuterJoin(cust2013Sep.map(c => (c, 1))).
								filter(rec => rec._2._2 == None).
								map(rec => rec._1).
								distinct