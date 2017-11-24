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

#### Command to import a table from MYSQL database to a HIVE database  
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--hive-import \
--hive-database varun

#### Command to import all tables from MYSQL database to a HIVE database  
sqoop import-all-tables \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--hive-import \
--num-mappers 2 \
--autoreset-to-one-mapper \
--hive-database varun_import_all

#### Command to import a table from MYSQL database to a HIVE database with appending the logs 
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table products \
--hive-import \
--hive-database varun 2>jobdetails.err 1>jobdetails.out

#### Command to import a table from MYSQL database to a HIVE database with a field delimiter
sqoop import \
--connect jdbc:mysql://127.0.0.1:3306/retail_db \
--username root \
--password cloudera \
--table orders \
--hive-import \
--hive-database varun \
--fields-terminated-by '|'

#### Command to export to a table in MYSQL database from a HIVE database
sqoop export \
--connect jdbc:mysql://127.0.0.1:3306/retail_export \
--username root \
--password cloudera \
--table orders_wlabs \
--export-dir /user/hive/warehouse/varun_import_all.db/orders
