// ID,Case Number,Date,Block,IUCR,Primary Type,Description,Location Description,Arrest,
// Domestic,Beat,District,Ward,Community Area,FBI Code,X Coordinate,Y Coordinate,Year,
// Updated On,Latitude,Longitude,Location

/*
Get monthly count of primary crime type, sorted by month in ascending and number of crimes 
per type in descending order
*/

// Starting the spark-shell
spark-shell --master yarn --conf spark.ui.port=12654

// Solution Using Core API

val crimeData = sc.textFile("/public/crime/csv") 
val header = crimeData.first()
val crimeDataWithoutHeader = crimeData.filter(row => row != header)

val uniqueDates = crimeDataWithoutHeader.map(rec => rec.split(",")(2).split(" ")(0))
					.distinct
					.collect
					.sorted

val crimeDataMap = crimeDataWithoutHeader.map(rec => (((rec.split(",")(2).split(" ")(0).split("/")(2)+rec.split(",")(2).split(" ")(0).split("/")(0)).toInt, rec.split(",")(5)), 1))

val crimeCountPerMonthPerType = crimeDataMap.reduceByKey((total, value) => total + value)

val crimeCountPerMonthPerTypeSorted = crimeCountPerMonthPerType.
										map(rec => ((rec._1._1, -rec._2), rec._1._1 + "\t" + rec._2 + "\t" + rec._1._2)).
										sortByKey().
										map(rec => rec._2)

// Saving the file in compressed format
crimeCountPerMonthPerTypeSorted.
	coalesce(1).
	saveAsTextFile("/user/varunu28/solution1", classOf[org.apache.hadoop.io.compress.GzipCodec])

// Solution using DataFrame

val crimeData = sc.textFile("/public/crime/csv") 
val header = crimeData.first()
val crimeDataWithoutHeader = crimeData.filter(row => row != header)

// Convert to DataFrame
val crimeDataWithDateAndTypeDF = crimeDataWithoutHeader.
									map(rec => (rec.split(",")(2), rec.split(",")(5))).
									toDF("crime_date", "crime_type")

crimeDataWithDateAndTypeDF.registerTempTable("crime_data")

// Preview the data
sqlContext.sql("SELECT * FROM crime_data").show

// Getting the results using Spark SQL
val crimeCountPerMonthPerTypeDF = sqlContext.
sql(
	"SELECT CAST(CONCAT(SUBSTR(crime_date, 7, 4), SUBSTR(crime_date, 0, 2)) as int) date, " +
 	"COUNT(1) crime_count , crime_type FROM crime_data GROUP BY CAST(CONCAT(SUBSTR(crime_date, 7, 4), " +
 	"SUBSTR(crime_date, 0, 2)) as int), crime_type ORDER BY date, crime_count DESC")

// Converting DF to required output format and saving the output
crimeCountPerMonthPerTypeDF.
	rdd.
	map(rec => rec.mkString("\t")).
	coalesce(1).
	saveAsTextFile("/user/varunu28/solution1DF", classOf[org.apache.hadoop.io.compress.GzipCodec])