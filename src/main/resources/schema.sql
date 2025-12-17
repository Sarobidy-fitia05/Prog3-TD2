-- table de produit
CREATE TABLE product (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    creation_datetime TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );

-- table de categorie de produit
CREATE TABLE product_category (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_id INTEGER NOT NULL REFERENCES product(id) ON DELETE CASCADE
    );