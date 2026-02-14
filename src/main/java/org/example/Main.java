package org.example;

import java.time.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

//        DataRetriever dr = new DataRetriever();
//        System.out.println("=== TEST DE LA GESTION DES TABLES ===");
//        System.out.println("=====================================\n");
//
//        // 1. Test des tables disponibles
//        System.out.println("1. TEST: Liste des tables disponibles maintenant");
//        System.out.println("-----------------------------------------------");
//        try {
//            List<RestaurantTable> tablesDisponibles = dr.getAvailableTablesAtDateTime(LocalDateTime.now());
//            System.out.println("Tables disponibles: " + tablesDisponibles.size());
//            for (RestaurantTable table : tablesDisponibles) {
//                System.out.println("  - Table " + table.getNumber());
//            }
//        } catch (Exception e) {
//            System.out.println("ERREUR: " + e.getMessage());
//        }
//
//        // 2. Test création d'une commande avec table
//        System.out.println("\n\n2. TEST: Création d'une commande avec table");
//        System.out.println("------------------------------------------");
//        try {
//            // Trouver une table
//            RestaurantTable table1 = dr.findTableByNumber(1);
//
//            // Créer un plat commandé
//            Dish dish = dr.findDishById(1); // Supposons que le plat ID 1 existe
//            DishOrder dishOrder = new DishOrder();
//            dishOrder.setDish(dish);
//            dishOrder.setQuantity(2);
//
//            // Créer la commande
//            Order nouvelleCommande = new Order();
//            nouvelleCommande.setTotalHt(50.0);
//            nouvelleCommande.setTotalTtc(60.0);
//            nouvelleCommande.setCreationDatetime(LocalDateTime.now());
//            nouvelleCommande.setTable(table1);
//            nouvelleCommande.setArrivalDatetime(LocalDateTime.now().plusHours(1));
//            nouvelleCommande.setDepartureDatetime(LocalDateTime.now().plusHours(3));
//            nouvelleCommande.setDishOrders(List.of(dishOrder));
//
//            // Sauvegarder
//            Order commandeSauvee = dr.saveOrder(nouvelleCommande);
//            System.out.println("SUCCÈS: Commande créée");
//            System.out.println("  Référence: " + commandeSauvee.getReference());
//            System.out.println("  Table: " + commandeSauvee.getTable().getNumber());
//            System.out.println("  Arrivée: " + commandeSauvee.getArrivalDatetime());
//            System.out.println("  Départ: " + commandeSauvee.getDepartureDatetime());
//
//        } catch (Exception e) {
//            System.out.println("ÉCHEC: " + e.getMessage());
//        }
//
//        // 3. Test table non disponible
//        System.out.println("\n\n3. TEST: Tentative de réservation table déjà prise");
//        System.out.println("---------------------------------------------------");
//        try {
//            // Essayer de réserver la même table aux mêmes heures
//            RestaurantTable table1 = dr.findTableByNumber(1);
//
//            Dish dish = dr.findDishById(2);
//            DishOrder dishOrder2 = new DishOrder();
//            dishOrder2.setDish(dish);
//            dishOrder2.setQuantity(1);
//
//            Order commandeConflict = new Order();
//            commandeConflict.setTotalHt(30.0);
//            commandeConflict.setTotalTtc(36.0);
//            commandeConflict.setCreationDatetime(LocalDateTime.now());
//            commandeConflict.setTable(table1);
//            commandeConflict.setArrivalDatetime(LocalDateTime.now().plusHours(1));
//            commandeConflict.setDepartureDatetime(LocalDateTime.now().plusHours(3));
//            commandeConflict.setDishOrders(List.of(dishOrder2));
//
//            dr.saveOrder(commandeConflict);
//            System.out.println("ÉCHEC: La commande aurait dû être refusée!");
//
//        } catch (RuntimeException e) {
//            System.out.println("SUCCÈS: Commande refusée comme prévu");
//            System.out.println("  Message d'erreur: " + e.getMessage());
//        }
//
//        // 4. Test suggestion tables alternatives
//        System.out.println("\n\n4. TEST: Suggestion de tables alternatives");
//        System.out.println("------------------------------------------");
//        try {
//            // Prendre une autre table
//            RestaurantTable table2 = dr.findTableByNumber(2);
//
//            Dish dish = dr.findDishById(1);
//            DishOrder dishOrder3 = new DishOrder();
//            dishOrder3.setDish(dish);
//            dishOrder3.setQuantity(3);
//
//            Order commandeTable2 = new Order();
//            commandeTable2.setTotalHt(75.0);
//            commandeTable2.setTotalTtc(90.0);
//            commandeTable2.setCreationDatetime(LocalDateTime.now());
//            commandeTable2.setTable(table2);
//            commandeTable2.setArrivalDatetime(LocalDateTime.now().plusHours(2));
//            commandeTable2.setDepartureDatetime(LocalDateTime.now().plusHours(4));
//            commandeTable2.setDishOrders(List.of(dishOrder3));
//
//            dr.saveOrder(commandeTable2);
//            System.out.println("SUCCÈS: Table 2 réservée pour 14h-16h");
//
//            // Maintenant essayer de prendre table 2 aux mêmes heures
//            Order commandeConflict2 = new Order();
//            commandeConflict2.setTotalHt(40.0);
//            commandeConflict2.setTotalTtc(48.0);
//            commandeConflict2.setCreationDatetime(LocalDateTime.now());
//            commandeConflict2.setTable(table2);
//            commandeConflict2.setArrivalDatetime(LocalDateTime.now().plusHours(2));
//            commandeConflict2.setDepartureDatetime(LocalDateTime.now().plusHours(4));
//            commandeConflict2.setDishOrders(List.of(dishOrder3));
//
//            dr.saveOrder(commandeConflict2);
//            System.out.println("ÉCHEC: La commande aurait dû être refusée!");
//
//        } catch (RuntimeException e) {
//            System.out.println("SUCCÈS: Commande refusée avec suggestions");
//            System.out.println("  Message: " + e.getMessage());
//        }
//
//        // 5. Test aucune table disponible
//        System.out.println("\n\n5. TEST: Aucune table disponible");
//        System.out.println("-------------------------------");
//        try {
//            // Prendre toutes les tables restantes
//            List<RestaurantTable> toutesTables = dr.getAvailableTablesAtDateTime(
//                    LocalDateTime.now().plusHours(5)
//            );
//
//            if (!toutesTables.isEmpty()) {
//                RestaurantTable derniereTable = toutesTables.get(0);
//
//                Order derniereCommande = new Order();
//                derniereCommande.setTotalHt(100.0);
//                derniereCommande.setTotalTtc(120.0);
//                derniereCommande.setCreationDatetime(LocalDateTime.now());
//                derniereCommande.setTable(derniereTable);
//                derniereCommande.setArrivalDatetime(LocalDateTime.now().plusHours(5));
//                derniereCommande.setDepartureDatetime(LocalDateTime.now().plusHours(7));
//
//                dr.saveOrder(derniereCommande);
//                System.out.println("SUCCÈS: Dernière table réservée");
//
//                // Essayer de réserver à nouveau
//                Order commandeImpossible = new Order();
//                commandeImpossible.setTotalHt(50.0);
//                commandeImpossible.setTotalTtc(60.0);
//                commandeImpossible.setCreationDatetime(LocalDateTime.now());
//                commandeImpossible.setTable(derniereTable);
//                commandeImpossible.setArrivalDatetime(LocalDateTime.now().plusHours(5));
//                commandeImpossible.setDepartureDatetime(LocalDateTime.now().plusHours(7));
//
//                dr.saveOrder(commandeImpossible);
//                System.out.println("ÉCHEC: La commande aurait dû être refusée!");
//            } else {
//                System.out.println("INFO: Aucune table disponible pour tester ce cas");
//            }
//
//        } catch (RuntimeException e) {
//            System.out.println("SUCCÈS: Commande refusée - aucune table disponible");
//            System.out.println("  Message: " + e.getMessage());
//        }
//
//        // 6. Test recherche de commande
//        System.out.println("\n\n6. TEST: Recherche d'une commande existante");
//        System.out.println("-------------------------------------------");
//        try {
//            // Créer une commande pour la rechercher
//            RestaurantTable table3 = dr.findTableByNumber(3);
//
//            Dish dish = dr.findDishById(1);
//            DishOrder dishOrder4 = new DishOrder();
//            dishOrder4.setDish(dish);
//            dishOrder4.setQuantity(1);
//
//            Order commandeTest = new Order();
//            commandeTest.setTotalHt(25.0);
//            commandeTest.setTotalTtc(30.0);
//            commandeTest.setCreationDatetime(LocalDateTime.now());
//            commandeTest.setTable(table3);
//            commandeTest.setArrivalDatetime(LocalDateTime.now().plusHours(6));
//            commandeTest.setDepartureDatetime(LocalDateTime.now().plusHours(8));
//            commandeTest.setDishOrders(List.of(dishOrder4));
//
//            Order commandeCree = dr.saveOrder(commandeTest);
//            String reference = commandeCree.getReference();
//
//            // Rechercher la commande
//            Order commandeTrouvee = dr.findOrderByReference(reference);
//            System.out.println("SUCCÈS: Commande trouvée");
//            System.out.println("  Référence: " + commandeTrouvee.getReference());
//            System.out.println("  Table: " + commandeTrouvee.getTable().getNumber());
//            System.out.println("  Total TTC: " + commandeTrouvee.getTotalTtc());
//
//        } catch (Exception e) {
//            System.out.println("ÉCHEC: " + e.getMessage());
//        }
//
//        // 7. Test commande sans table (doit échouer)
//        System.out.println("\n\n7. TEST: Commande sans table spécifiée");
//        System.out.println("--------------------------------------");
//        try {
//            Order commandeSansTable = new Order();
//            commandeSansTable.setTotalHt(20.0);
//            commandeSansTable.setTotalTtc(24.0);
//            commandeSansTable.setCreationDatetime(LocalDateTime.now());
//            // Pas de table définie!
//
//            dr.saveOrder(commandeSansTable);
//            System.out.println("ÉCHEC: La commande aurait dû être refusée!");
//
//        } catch (RuntimeException e) {
//            System.out.println("SUCCÈS: Commande refusée comme prévu");
//            System.out.println("  Message: " + e.getMessage());
//        }
//
//        System.out.println("\n=== FIN DES TESTS ===");
        Integer testIngredientId = 1;

        // Conversion de la date '2024-01-31' en Instant (au début de la journée ou fin de journée)
        // Ici, on prend la fin de journée pour inclure tous les mouvements du 31
        LocalDate dateInterrogation = LocalDate.parse("2024-01-31");
        Instant t = dateInterrogation.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();

        // 2. Appel de la méthode
        try {
            // Remplacez 'VotreClasse' par le nom de la classe où se trouve getStockValueAt
            DataRetriever service = new DataRetriever();
            double result = service.getStockValueAt(testIngredientId, t);

            // 3. Affichage du résultat
            System.out.println("--- Résultat du Test Stock ---");
            System.out.println("Ingrédient ID : " + testIngredientId);
            System.out.println("Date cible    : " + dateInterrogation);
            System.out.println("Valeur obtenue : " + result);

            // Vérification simple
            if (result == 4.8) {
                System.out.println("Succès : Le résultat correspond à la base de données !");
            } else {
                System.out.println("Attention : Le résultat diffère (Vérifiez les mouvements 'OUT')");
            }

        } catch (Exception e) {
            System.err.println("Le test a échoué :");
            e.printStackTrace();
        }
    }

}
