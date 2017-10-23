-- connect to mySql and run as root USER

-- create database
DROP DATABASE IF EXISTS stock_dev;
CREATE DATABASE stock_dev;

DROP DATABASE IF EXISTS stock_prod;
CREATE DATABASE stock_prod;

-- create database access acounts
CREATE USER 'stock_dev_user'@'localhost' IDENTIFIED BY 'dev';
CREATE USER 'stock_prod_user'@'localhost' IDENTIFIED BY 'prod';

-- grant privelieges to users
GRANT CREATE ON stock_dev.* to 'stock_dev_user'@'localhost';
GRANT SELECT ON stock_dev.* to 'stock_dev_user'@'localhost';
GRANT INSERT ON stock_dev.* to 'stock_dev_user'@'localhost';
GRANT DELETE ON stock_dev.* to 'stock_dev_user'@'localhost';
GRANT UPDATE ON stock_dev.* to 'stock_dev_user'@'localhost';

GRANT CREATE ON stock_prod.* to 'stock_prod_user'@'localhost';
GRANT SELECT ON stock_prod.* to 'stock_prod_user'@'localhost';
GRANT INSERT ON stock_prod.* to 'stock_prod_user'@'localhost';
GRANT DELETE ON stock_prod.* to 'stock_prod_user'@'localhost';
GRANT UPDATE ON stock_prod.* to 'stock_prod_user'@'localhost';