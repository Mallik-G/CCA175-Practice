#### mysql Login
mysql -u retail_dba -h nn01.itversity.com -p

#### Pre-work

```
CREATE TABLE products_vu AS SELECT * FROM retail_db.products;
ALTER TABLE products_vu ADD PRIMARY KEY (product_id);
ALTER TABLE products_vu ADD COLUMN (product_grade int, product_sentiment varchar(100));
UPDATE products_vu SET product_grade = 1  WHERE product_price > 500;
UPDATE products_vu SET product_sentiment  = 'WEAK'  WHERE product_price between 300 and  500;
```

#### Using sqoop, import products_vu table from MYSQL into hdfs such that fields are separated by a '|' and lines are separated by '\n'. Null values are represented as -1 for numbers and "NOT-AVAILABLE" for strings. Only records with product id greater than or equal to 1 and less than or equal to 1000 should be imported and use 3 mappers for importing. The destination file should be stored as a text file to directory  /user/varunu28/problem5/products-text

```
sqoop import \
--connect jdbc:mysql://nn01.itversity.com:3306/retail_export \
--username retail_dba \
--password itversity \
--table products_vu \
--fields-terminated-by '|' \
--lines-terminated-by '\n' \
--target-dir /user/varunu28/problem5/products-text \
--num-mappers 3 \
--input-null-string 'NOT AVAILABLE' \
--input-null-non-string '-1' \
--where 'product_id between 1 and 1000' \
--boundary-query "select min(product_id), max(product_id) from products_vu where product_id between 1 and 1000"
```

#### Using sqoop, import products_vu table from MYSQL into hdfs such that fields are separated by a * and lines are separated by '\n'. Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id less than or equal to 1111 should be imported and use 2 mappers for importing. The destination file should be stored as a text file to directory  /user/varunu28/problem5/products-text-part1. 

```
sqoop import \
--connect jdbc:mysql://nn01.itversity.com:3306/retail_export \
--username retail_dba \
--password itversity \
--table products_vu \
--fields-terminated-by '*' \
--lines-terminated-by '\n' \
--target-dir /user/varunu28/problem5/products-text-part1 \
--num-mappers 2 \
--null-string "NA" \
--null-non-string -1000 \
--where 'product_id <= 1111' \
--delete-target-dir
```

#### Using sqoop, import products_vu table from MYSQL into hdfs such that fields are separated by a * and lines are separated by '\n'. Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id greater than 1111 should be imported and use 5 mappers for importing. The destination file should be stored as a text file to directory  /user/varunu28/problem5/products-text-part2.

```
sqoop import \
--connect jdbc:mysql://nn01.itversity.com:3306/retail_export \
--username retail_dba \
--password itversity \
--table products_vu \
--fields-terminated-by "*" \
--lines-terminated-by "\n" \
--null-non-string -1000 \
--null-string "NA" \
--where 'product_id > 1111' \
--num-mappers 5 \
--target-dir /user/varunu28/problem5/products-text-part2 \
--delete-target-dir
```

#### Using sqoop merge data available in /user/varunu28/problem5/products-text-part1 and /user/varunu28/problem5/products-text-part2 to produce a new set of files in /user/varunu28/problem5/products-text-both-parts

```
sqoop merge \
--class-name products_vu \
--jar-file /tmp/sqoop-varunu28/compile/f81ad143a33a585f2c17f9ba8daba63a/products_vu.jar \
--new-data /user/varunu28/problem5/products-text-part2 \
--onto /user/varunu28/problem5/products-text-part1 \
--target-dir /user/varunu28/problem5/products-text-both-parts \
--merge-key product_id 
```

## Using sqoop do the following. Read the entire steps before you create the sqoop job.

#### create a sqoop job Import products_vu table as text file to directory /user/varunu28/problem5/products-incremental. Import all the records.
```
sqoop job --create first_sqoop_job \
-- import \
--connect "jdbc:mysql://nn01.itversity.com:3306/retail_export" \
--username "retail_dba" \
--password "itversity" \
--table products_vu \
--target-dir /user/varunu28/problem5/products-incremental \
--check-column product_id \
--incremental append \
--last-value 0;
```

#### insert three more records to products_vu from mysql
```
insert into products_vu values (1346,2,'something 1','something 2',300.00,'not avaialble',3,'STRONG');
insert into products_vu values (1347,5,'something 787','something 2',356.00,'not avaialble',3,'STRONG');
insert into products_vu values (1348,5,'something 788','something 2',356.00,'not avaialble',3,'STRONG');
```

#### run the sqoop job again so that only newly added records can be pulled from mysql
```
sqoop job --exec first_sqoop_job
```

#### insert 2 more records to products_vu from mysql
```
insert into products_vu values (1349,2,'something 1','something 2',300.00,'not avaialble',3,'STRONG');
insert into products_vu values (1350,5,'something 789','something 2',356.00,'not avaialble',3,'STRONG');
```

#### run the sqoop job again so that only newly added records can be pulled from mysql
```
sqoop job --exec first_sqoop_job
```

## Using sqoop do the following. Read the entire steps before you create the sqoop job.

#### create a hive table in database named varunu_problem5 using below command 
     create table products_hive  (product_id int, product_category_id int, product_name string, product_description string, product_price float, product_imaage string,product_grade int,  product_sentiment string);

#### create a sqoop job Import products_vu table as hive table to database named varunu_problem5. name the table as products_hive. 

```
sqoop job --create hive_sqoop_job \
-- import \
--connect "jdbc:mysql://nn01.itversity.com:3306/retail_export" \
--username "retail_dba" \
--password "itversity" \
--table products_vu \
--check-column product_id \
--incremental append \
--last-value 0 \
--hive-import \
--hive-table products_hive \
--hive-database varunu_problem5

sqoop job --exec hive_sqoop_job
```

#### insert three more records to products_vu from mysql
```
insert into products_vu values (1351,2,'something 1','something 2',300.00,'not avaialble',3,'STRONG');
insert into products_vu values (1352,5,'something 789','something 2',356.00,'not avaialble',3,'STRONG');
insert into products_vu values (1353,5,'something 789','something 2',356.00,'not avaialble',3,'STRONG');
```

#### run the sqoop job again so that only newly added records can be pulled from mysql
```
sqoop job --exec hive_sqoop_job
```

#### insert 2 more records to products_vu from mysql
```
insert into products_vu values (1353,2,'something 1','something 2',300.00,'not avaialble',3,'STRONG');
insert into products_vu values (1354,5,'something 789','something 2',356.00,'not avaialble',3,'STRONG');
```

#### run the sqoop job again so that only newly added records can be pulled from mysql
```
sqoop job --exec hive_sqoop_job
```