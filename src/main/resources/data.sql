INSERT INTO dish (name, dish_type) VALUES
                                       ('Salade Fraîche', 'START'),
                                       ('Poulet Rôti', 'MAIN'),
                                       ('Gâteau au chocolat', 'DESSERT');
INSERT INTO ingredient (name, price, category, id_dish) VALUES
                                                            ('Laitue', 1000.0, 'VEGETABLE', 1),
                                                            ('Tomate', 800.0, 'VEGETABLE', 1),

                                                            ('Poulet', 5000.0, 'ANIMAL', 2),

                                                            ('Chocolat', 3000.0, 'OTHER', 3),
                                                            ('Farine', 1500.0, 'OTHER', 3),
                                                            ('Sucre', 1200.0, 'OTHER', 3);