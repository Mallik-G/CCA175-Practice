#### List Databases
sqoop list-databases \
--connect jdbc:mysql://ms.itversity.com:3306 \
--username retail_user \
--password itversity

#### List Tables
sqoop list-tables \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity

#### Quering a table using eval
sqoop eval \
--connect jdbc:mysql://ms.itversity.com:3306/retail_db \
--username retail_user \
--password itversity \
--query "SELECT * FROM orders LIMIT 10"

#### Creating a table using eval (Only is user has write permission)
sqoop eval \
--connect jdbc:mysql://ms.itversity.com:3306/retail_export \
--username retail_user \
--password itversity \
--query "CREATE TABLE dummy_v (i INT)"
