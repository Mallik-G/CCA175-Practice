// Develop word count program

// Launching spark-shell
spark-shell \
--master yarn \
--conf spark.ui.port=12437 \
--packages com.databricks:spark-avro_2.10:2.0.1 \
--num-executors 10 \
--executor-memory 3G \
--executor-cores 2

// Solution using Spark-Core API

// Reading text file and splitting it in tuple
val lines = sc.textFile("/public/randomtextwriter")
val words = lines.flatMap(line => line.split(" "))
val tuple = words.map(word => (word, 1))

// Reduce Logic
val wordCount = tuple.reduceByKey((total, value) => total + value, 8)
val wordCountDF = wordCount.toDF("word", "count")

import com.databricks.spark.avro._

wordCountDF.write.avro("/user/varunu28/exercise5Sol")

// Solution using Spark-SQL

// Creating Dataframe and registering table
val words_DF = words.toDF("word")
words_DF.registerTempTable("words")

// Main query
sqlContext.
        sql("SELECT word, COUNT(word) FROM words GROUP BY word").
        write.
        avro("/user/varunu28/exercise5SqlSol")