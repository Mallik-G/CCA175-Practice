// Starting spark-sql on labs
spark-sql --master yarn --conf spark.ui.port=12457

// Creating a database
create database varunu28_retail_db_orc;

// Create table orders in orc
CREATE TABLE orders(
        order_id int,
        order_date string,
        order_customer_id int,
        order_status string 
    ) 
    stored as orc;

insert into table orders select * from varunu28_retail_db_txt.orders;

// Create table order_items in orc
CREATE TABLE order_items(
        order_item_id int,
        order_item_order_id int,
        order_item_product_id int,
        order_item_quantity int,
        order_item_subtotal float,
        order_item_product_price float 
    ) 
    stored as orc;

insert into table order_items select * from varunu28_retail_db_txt.order_items;
