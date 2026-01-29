-- -----------------------------
-- new_schema.sql
-- Normalisation ManyToMany : Dish - Ingredient
-- -----------------------------

-- DROP tables si elles existent (pour repartir à zéro)
DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS dish CASCADE;

-- Table Dish (plat)
CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      dish_type VARCHAR(50) NOT NULL,
                      sale_price NUMERIC(10,2) NULL
);

-- Table Ingredient (ingrédient)
CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            price NUMERIC(10,2) NOT NULL,
                            category VARCHAR(50) NOT NULL
);

-- Table de jointure DishIngredient
CREATE TABLE dish_ingredient (
                                 id SERIAL PRIMARY KEY,
                                 dish_id INTEGER NOT NULL,
                                 ingredient_id INTEGER NOT NULL,
                                 quantity NUMERIC(10,2) NOT NULL,
                                 unit VARCHAR(20) NOT NULL,
                                 CONSTRAINT fk_dish
                                     FOREIGN KEY (dish_id)
                                         REFERENCES dish(id)
                                         ON DELETE CASCADE,
                                 CONSTRAINT fk_ingredient
                                     FOREIGN KEY (ingredient_id)
                                         REFERENCES ingredient(id)
                                         ON DELETE CASCADE,
                                 CONSTRAINT unique_dish_ingredient
                                     UNIQUE(dish_id, ingredient_id)
);
--------------------------------------------------------------------------------------
-- créer le sequence
CREATE SEQUENCE order_ref_seq
    START 1
INCREMENT 1;

--création de la table order
CREATE TABLE "order" (
    id SERIAL PRIMARY KEY ,
    reference varchar(10) UNIQUE ,
    creation_datetime timestamp
);
--fonction qui génere la reference
CREATE OR REPLACE FUNCTION generate_order_reference()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.reference IS NULL THEN
        NEW.reference := 'ORD' || LPAD(NEXTVAL('order_ref_seq')::TEXT, 5, '0');
ELSE
        IF NEW.reference !~ '^ORD[0-9]{5}$' THEN
            RAISE EXCEPTION 'Reference invalide !';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
   --Créetion de la table dish order
        CREATE TABLE DishOrder (
            id SERIAL PRIMARY KEY ,
            id_order INTEGER NOT NULL ,
            id_dish INTEGER NOT NULL ,
            quantty NUMERIC(10,2),

            CONSTRAINT fk_order
                FOREIGN KEY (id_order)
                    REFERENCES dish(id)
                    ON DELETE CASCADE,
            CONSTRAINT fk_dish
                FOREIGN KEY (id_dish)
                    REFERENCES dish(id)
                    ON DELETE CASCADE,
        );


-- Table pour les mouvements de stock
CREATE TABLE stock_movement (
                                id SERIAL PRIMARY KEY,
                                id_ingredient INTEGER NOT NULL,
                                quantity NUMERIC(10,2) NOT NULL,
                                type VARCHAR(3) NOT NULL CHECK (type IN ('IN', 'OUT')),
                                unit VARCHAR(10) NOT NULL,
                                creation_datetime TIMESTAMP NOT NULL DEFAULT NOW(),
                                FOREIGN KEY (id_ingredient) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- Table pour le stock initial
CREATE TABLE initial_stock (
                               id_ingredient INTEGER PRIMARY KEY,
                               quantity NUMERIC(10,2) NOT NULL,
                               unit VARCHAR(10) NOT NULL,
                               last_updated TIMESTAMP NOT NULL DEFAULT NOW(),
                               FOREIGN KEY (id_ingredient) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- Insérez les données initiales du stock
INSERT INTO initial_stock (id_ingredient, quantity, unit) VALUES
                                                              (1, 5.0, 'KG'),   -- Laitue
                                                              (2, 4.0, 'KG'),   -- Tomate
                                                              (3, 10.0, 'KG'),  -- Poulet
                                                              (4, 3.0, 'KG'),   -- Chocolat
                                                              (5, 2.5, 'KG');   -- Beurre

-- Insérez les mouvements de test
INSERT INTO stock_movement (id, id_ingredient, quantity, type, unit, creation_datetime) VALUES
                                                                                            (6, 1, 0.2, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (7, 2, 0.15, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (8, 3, 1.0, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (9, 4, 0.3, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (10, 5, 0.2, 'OUT', 'KG', '2024-01-06 12:00:00');
-- ===========================================
-- ANNEXE 2 - Tables pour la gestion des commandes
-- ===========================================

-- Table pour les commandes (Order)
CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         reference VARCHAR(50) UNIQUE NOT NULL,
                         total_ht NUMERIC(10,2) NOT NULL,
                         total_ttc NUMERIC(10,2) NOT NULL,
                         creation_datetime TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Table pour les plats commandés (DishOrder)
CREATE TABLE dish_order (
                            id SERIAL PRIMARY KEY,
                            id_order INTEGER NOT NULL,
                            id_dish INTEGER NOT NULL,
                            quantity INTEGER NOT NULL,
                            FOREIGN KEY (id_order) REFERENCES "order"(id) ON DELETE CASCADE,
                            FOREIGN KEY (id_dish) REFERENCES dish(id) ON DELETE CASCADE
);

-- Séquence pour générer les références de commande
CREATE SEQUENCE order_reference_seq START 1;

-- Fonction pour générer la référence automatiquement
CREATE OR REPLACE FUNCTION generate_order_reference()
RETURNS TRIGGER AS $$
BEGIN
    NEW.reference := 'ORD' || LPAD(NEXTVAL('order_reference_seq')::TEXT, 5, '0');
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Déclencheur pour générer automatiquement la référence
CREATE TRIGGER trg_generate_order_reference
    BEFORE INSERT ON "order"
    FOR EACH ROW
    EXECUTE FUNCTION generate_order_reference();


-- ===========================================
-- Évaluation - Tables pour la gestion des tables
-- ===========================================

-- Table pour les tables de restaurant
CREATE TABLE restaurant_table (
                                  id SERIAL PRIMARY KEY,
                                  number INTEGER UNIQUE NOT NULL
);

-- Insertion de quelques tables de test
INSERT INTO restaurant_table (number) VALUES (1), (2), (3), (4), (5);

-- Modification de la table order pour ajouter les références à la table
ALTER TABLE "order"
    ADD COLUMN id_table INTEGER,
ADD COLUMN arrival_datetime TIMESTAMP,
ADD COLUMN departure_datetime TIMESTAMP,
ADD FOREIGN KEY (id_table) REFERENCES restaurant_table(id);

-- Création d'un index pour optimiser les recherches de disponibilité
CREATE INDEX idx_order_table_dates ON "order"(id_table, arrival_datetime, departure_datetime);




