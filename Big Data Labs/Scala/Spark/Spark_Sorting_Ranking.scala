val products = sc.textFile("/public/retail_db/products")

val productsMap = products.map(product => (product.split(",")(1).toInt, product))

// Basic sortByKey
val productsSortedByCategoryId = productsMap.sortByKey()

// Sorting by Composite Key
val productsMap = products.
					filter(product => product.split(",")(4) != "").
					map(product => ((product.split(",")(1).toInt, product.split(",")(4).toFloat), product))
val productsSortedByCompositeKey = productsMap.sortByKey()

// Ranking - SortByKey & takeOrdered
val productsMap = products.
					filter(product => product.split(",")(4) != "").
					map(product => ((product.split(",")(4).toFloat, product), product))

val productsSortedByPrice = productsMap.sortByKey(false)

products.
	filter(product => product.split(",")(4) != "").
	takeOrdered(10)(Ordering[Float].reverse.on(product => product.split(",")(4).toFloat)).
	foreach(println)

// groupByKey (Get top N priced products with in each category)
val productsMap = products.
					filter(product => product.split(",")(4) != "").
					map(product => ((product.split(",")(1).toInt, product)))

val productsGroupByCategory = productsMap.groupByKey

// Get topN prices
val productsIterable = productsGroupByCategory.first._2

// Get topN priced products
def getTopNPricedProducts(productsIterable: Iterable[String], topN: Int): Iterable[String] = {
	val productPrices = productsIterable.map(p => p.split(",")(4).toFloat).toSet
	val topNPrices = productPrices.toList.sortBy(p => -p).take(topN)
	val productsSorted = productsIterable.toList.sortBy(product => product.split(",")(4).toFloat)
	val minOfTopPrices = topNPrices.min
	val topNPricedProducts = productsSorted.takeWhile(product => product.split(",")(4).toFloat >= minOfTopPrices)

	topNPricedProducts
} 

getTopNPricedProducts(productsIterable, 5)

val top3PricedProdPerCategory = productsGroupByCategory.flatMap(rec => getTopNPricedProducts(rec._2, 3))