// Get top 3 crime types based on number of incidents in RESIDENCE area

/*
ID,Case Number,Date,Block,IUCR,Primary Type,Description,Location Description,Arrest,Domestic,Beat,District,Ward,Community Area,FBI Code,X Coordinate,Y Coordinate,Year,Updated On,Latitude,Longitude,Location
*/

// Solution using CORE API

// Starting the spark-shell
spark-shell --master yarn \
--conf spark.ui.port=12654 \
--num-executors 6 \
--executor-cores 2 \
--executor-memory 2G

// Reading the data and removing the header
val crimeRaw = sc.textFile("/public/crime/csv")
val header = crimeRaw.first()
val crime = crimeRaw.filter(rec => rec!=header)

// Performing the logic
val crimeCountForResidence = sc.parallelize(crime.
                                filter(rec => rec.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)(7) == "RESIDENCE").
                                map(rec => (rec.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)(5), 1)).
                                reduceByKey((total, value) => total + value).
                                map(rec => (rec._2, rec._1)).
                                sortByKey(false).
                                take(3))

// Saving to JSON format
crimeCountForResidence.
    map(rec=>(rec._2, rec._1)).
    toDF("crime_type", "crime_count").
    write.json("/user/varunu28/top3CrimeExerciseCoreSolution")

// Verifying Output
sc.textFile("/user/varunu28/top3CrimeExerciseCoreSolution").collect().foreach(println)


// Solution Using Spark SQL

// Converting to DataFrame and registering to table 
val crimeDF = crime.
                map(rec => (rec.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)(5), rec.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)(7))).
                toDF("crime_type", "area")

crimeDF.registerTempTable("crime")

// Verifying Data 
sqlContext.sql("SELECT * FROM crime LIMIT 10").show

// Main Query and Saving the results in JSON format
sqlContext.sql("SELECT crime_type, COUNT(1) AS crime_count " +
                " FROM crime " +
                "WHERE area='RESIDENCE' " +
                "GROUP BY crime_type " +
                "ORDER BY crime_count DESC " + 
                "LIMIT 3").
            write.json("/user/varunu28/top3CrimeExerciseSQLSolution")

// Verifying the Output
sc.textFile("/user/varunu28/top3CrimeExerciseSQLSolution").collect().foreach(println)