-- Example MySQL users for microservices

CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'product_pass';
CREATE USER IF NOT EXISTS 'order_user'@'%' IDENTIFIED BY 'order_pass';
CREATE USER IF NOT EXISTS 'user_user'@'%' IDENTIFIED BY 'user_pass';

GRANT ALL PRIVILEGES ON productdb.* TO 'product_user'@'%';
GRANT ALL PRIVILEGES ON orderdb.* TO 'order_user'@'%';
GRANT ALL PRIVILEGES ON userdb.* TO 'user_user'@'%';

FLUSH PRIVILEGES;
