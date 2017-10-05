// Word count for a paragraph

val text = sc.textFile("file:///home/cloudera/Desktop/sampletext.txt")

val textMap = text.flatMap(rec => (rec.split(" ")))

val textMapCount = textMap.map(rec => (rec,1))

val textReduceByKey = textMapCount.reduceByKey((agg,value) => agg+value)

textReduceByKey.collect().foreach(println)





// 1 Line version

sc.textFile("file:///home/cloudera/Desktop/sampletext.txt").
flatMap(rec => (rec.split(" "))).
map(rec => (rec,1)).
reduceByKey(_ + _).
collect().foreach(println)

// 1 Line verion for storing output in a directory

sc.textFile("file:///home/cloudera/Desktop/sampletext.txt").
flatMap(rec => rec.split(" ")).
map(rec => (rec,1)).
reduceByKey(_ + _).
saveAsTextFile("file:///home/cloudera/Desktop/wcAns")
