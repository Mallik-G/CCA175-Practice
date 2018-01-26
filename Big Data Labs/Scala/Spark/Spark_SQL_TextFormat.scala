// Starting spark-sql on labs
spark-sql --master yarn --conf spark.ui.port=12457

// Creating a database
create database varunu28_retail_db_txt;

// Create table orders
CREATE TABLE orders(
        order_id int,
        order_date string,
        order_customer_id int,
        order_status string 
    ) row format delimited fields terminated by ','
    stored as textfile;

Load data local inpath '/data/retail_db/orders' into table orders;

// Create table order_items
CREATE TABLE order_items(
        order_item_id int,
        order_item_order_id int,
        order_item_product_id int,
        order_item_quantity int,
        order_item_subtotal float,
        order_item_product_price float 
    ) row format delimited fields terminated by ','
    stored as textfile;

Load data local inpath '/data/retail_db/order_items' into table order_items;