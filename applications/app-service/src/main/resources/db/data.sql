
INSERT INTO franchise (name) VALUES ('Franquicia A');
INSERT INTO franchise (name) VALUES ('Franquicia B');


INSERT INTO branch (name, franchise_id) VALUES ('Sucursal Norte', 1);
INSERT INTO branch (name, franchise_id) VALUES ('Sucursal Sur', 1);
INSERT INTO branch (name, franchise_id) VALUES ('Sucursal Centro', 2);


INSERT INTO product (name) VALUES ('Coca-Cola');
INSERT INTO product (name) VALUES ('Pepsi');
INSERT INTO product (name) VALUES ('Agua');


INSERT INTO inventory (branch_id, product_id, stock) VALUES (1, 1, 50);
INSERT INTO inventory (branch_id, product_id, stock) VALUES (1, 2, 30);
INSERT INTO inventory (branch_id, product_id, stock) VALUES (2, 3, 100);
INSERT INTO inventory (branch_id, product_id, stock) VALUES (3, 1, 20);
INSERT INTO inventory (branch_id, product_id, stock) VALUES (3, 2, 80);
