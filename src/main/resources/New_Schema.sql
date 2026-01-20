ALTER TABLE ingredient DROP COLUMN id_dish;
CREATE TABLE dish_ingredient (
    id SERIAL PRIMARY KEY,
    id_dish INTEGER NOT NULL REFERENCES dish(id),
    id_ingredient INTEGER NOT NULL REFERENCES ingredient(id),
    quantity_required DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    UNIQUE(id_dish, id_ingredient)
);
INSERT INTO dish_ingredient (id, id_dish, id_ingredient, quantity_required, unit) VALUES
(1, 1, 1, 0.20, 'KG'),
(2, 1, 2, 0.15, 'KG'),
(3, 2, 3, 1.00, 'KG'),
(4, 4, 4, 0.30, 'KG'),
(5, 4, 5, 0.20, 'KG');


UPDATE dish SET selling_price = 3500.00 WHERE id = 1;
UPDATE dish SET selling_price = 12000.00 WHERE id = 2;
UPDATE dish SET selling_price = NULL WHERE id = 3;
UPDATE dish SET selling_price = 8000.00 WHERE id = 4;
UPDATE dish SET selling_price = NULL WHERE id = 5;