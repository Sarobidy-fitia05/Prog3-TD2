CREATE TYPE Category AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TYPE Dish_type AS ENUL ('START', 'MAIN', 'DESSERT');


CREATE TABLE ingredient (
                            id int serial PRIMARY KEY,
                            name varchar(250),
                            price numeric,
                            category Category,
                            id_dish int CONSTRAINT fk_ingredient FOREIGN KEY (id_dish) REFERENCES dish(id)
);
CREATE TABLE dish (
                      id int serial PRIMARY KEY,
                      name VARCHAR(250),
                      dish_type Dish_type
);
