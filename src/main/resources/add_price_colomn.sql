ALTER TABLE `Dish` 
  ADD COLUMN IF NOT EXISTS `price` DECIMAL(10,2) NULL;

UPDATE `Dish` SET `price` = 2000.00 WHERE `name` = 'Salade fraîche';
UPDATE `Dish` SET `price` = 6000.00 WHERE `name` = 'Poulet grillé';
UPDATE `Dish` SET `price` = NULL   WHERE `name` = 'Riz au légume';
UPDATE `Dish` SET `price` = NULL   WHERE `name` = 'Gâteau au chocolat';
UPDATE `Dish` SET `price` = NULL   WHERE `name` = 'Salade de fruit';