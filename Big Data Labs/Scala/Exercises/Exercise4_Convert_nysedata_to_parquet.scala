// Convert nyse data to parquet file format 

// Getting data to HDFS
// hadoop fs -put /data/nyse /user/varunu28/nyse 

spark-shell --master yarn --conf spark.ui.port=12437 --num-executors 4

// Reading data and converting it to dataframe
val nyse = sc.textFile("/user/varunu28/nyse").
                coalesce(4).
                map(stock => {
                    val s = stock.split(",")
                    (s(0), s(1), s(2).toFloat, s(3).toFloat, s(4).toFloat, s(5).toFloat, s(6).toInt)
                }).
                toDF("stockticker", "transactiondate", "openprice", "highprice", "lowprice", "closeprice", "volume")

// Setting partitions
sqlContext.setConf("spark.sql.shuffle.partitions", "4")

// Saving the file
nyse.save("/user/varunu28/nyse_parquet", "parquet")

// Verifying the output
sqlContext.read.parquet("/user/varunu28/nyse_parquet").show