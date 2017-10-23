-- In root user
USE stock_dev;
DROP TABLE IF EXISTS role;
CREATE TABLE role (role_id INT NOT NULL AUTO_INCREMENT, role_name VARCHAR(255),PRIMARY KEY (role_id)) engine=InnoDB;
DROP TABLE IF EXISTS stock;
CREATE TABLE stock (stock_id INT NOT NULL AUTO_INCREMENT, stock_ask_price DOUBLE, stock_bid_price DOUBLE, stock_name VARCHAR(255), stock_symbol VARCHAR(255),PRIMARY KEY (stock_id)) engine=InnoDB;
DROP TABLE IF EXISTS user;
CREATE TABLE user (user_id INT NOT NULL AUTO_INCREMENT, user_name VARCHAR(255),password VARCHAR(255),PRIMARY KEY (user_id)) engine=InnoDB;

DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (user_id INT NOT NULL, role_id INT NOT NULL) engine=InnoDB;
DROP TABLE IF EXISTS user_stock;
CREATE TABLE user_stock (user_id INT NOT NULL, stock_id INT NOT NULL) engine=InnoDB;

ALTER TABLE user_role ADD CONSTRAINT pk_user_role PRIMARY KEY(user_id, role_id);
ALTER TABLE user_role ADD CONSTRAINT fk_user_role_to_role FOREIGN KEY(role_id) REFERENCES role(role_id);
ALTER TABLE user_role ADD CONSTRAINT fk_user_role_to_user FOREIGN KEY(user_id) REFERENCES user(user_id);

ALTER TABLE user_stock ADD CONSTRAINT pk_user_stock PRIMARY KEY(user_id, stock_id);
ALTER TABLE user_stock ADD CONSTRAINT fk_user_stock_to_stock FOREIGN KEY(stock_id) REFERENCES stock(stock_id);
ALTER TABLE user_stock ADD CONSTRAINT fk_user_stock_to_user FOREIGN KEY(user_id) REFERENCES user(user_id);

