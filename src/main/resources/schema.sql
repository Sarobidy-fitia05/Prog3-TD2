CREATE TYPE dish_type_enum AS ENUM (
    'START',
    'MAIN',
    'DESSERT'
);
CREATE TYPE category_enum AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
);
CREATE TABLE dish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    dish_type dish_type_enum NOT NULL
);
CREATE TABLE ingredient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    price DOUBLE PRECISION NOT NULL,
    category category_enum NOT NULL,
    id_dish INTEGER,

         CONSTRAINT fk_dish
         FOREIGN KEY (id_dish)
         REFERENCES dish(id)
         ON DELETE SET NULL
);