#### Command to list databases
sqoop list-databases \
--connect jdbc:mysql://127.0.0.1:3306 \
--username root \
--password cloudera 

#### Command to list tables in a database
sqoop list-tables \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera 

#### Command to run query on a table
sqoop eval \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--query "SELECT * FROM orders LIMIT 10"

#### Command to import a table to HDFS using a warehouse dir. Create the warehouse dir beforehand
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--warehouse-dir /usr/cloudera/retail_db

#### Command to import a table to HDFS using a target dir. Don't create the target dir beforehand
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db/orders 

#### Command to import a table to HDFS with appending to a target dir
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db/orders \
--append

#### Command to import a table to HDFS with deleting the target dir if it already exists
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db/orders \
--delete-target-dir

#### Command to import a table to HDFS using a target dir as sequence file
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db_seq/orders \
--as-sequencefile

#### Command to import a table to HDFS using a target dir as parquet file
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db_seq/orders \
--as-parquetfile

#### Command to import a table to HDFS using a target dir as parquet file
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db_seq/orders \
--as-parquetfile

#### Command to import a table to HDFS using a target dir using a boundary query
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--target-dir /usr/cloudera/retail_db_seq/orders \
--boundary-query "SELECT 11111, 68883"

#### Command to import a table to HDFS using a target with help of a query
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--query "SELECT * FROM orders WHERE \$CONDITIONS" \
--target-dir /usr/cloudera/retail_db_seq/orders \
--split-by order_id

#### Command to import a table to HDFS using a target dir as avro data file
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--warehouse-dir /usr/cloudera/retail_db_avro \
--as-avrodatafile
