package org.example;

import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Integer testIngredientId = 1;

        LocalDate dateInterrogation = LocalDate.parse("2024-01-31");
        Instant t = dateInterrogation.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();

        try {
            DataRetriever service = new DataRetriever();
            System.out.println("-------------------exo1-------------------------");
            double result = service.getStockValueAt(testIngredientId, t);

            System.out.println("--- Résultat du Test Stock ---");
            System.out.println("Ingrédient ID : " + testIngredientId);
            System.out.println("Date cible    : " + dateInterrogation);
            System.out.println("Valeur obtenue : " + result);

            if (result == 4.8) {
                System.out.println("Succès : Le résultat correspond à la base de données !");
            } else {
                System.out.println("Attention : Le résultat diffère (Vérifiez les mouvements 'OUT')");
            }

        } catch (Exception e) {
            System.err.println("Le test a échoué :");
            e.printStackTrace();
        }

        System.out.println("----------------------------exo2 a-----------------------------------");
        Integer testDishId = 1;
        double expectedTotal = 950.0;

        DataRetriever retriever = new DataRetriever();

        try {
            System.out.println("=== TEST : getDishCost (Push-down) ===");

            Double actualCost = retriever.getDishCost(testDishId);

            System.out.println("ID du Plat        : " + testDishId);
            System.out.println("Coût attendu (SQL): " + expectedTotal);
            System.out.println("Coût obtenu (Java): " + actualCost);

            if (actualCost != null && Math.abs(actualCost - expectedTotal) < 0.001) {
                System.out.println("\n SUCCÈS : Le résultat Java correspond parfaitement à PostgreSQL !");
            } else {
                System.out.println("\n ÉCHEC : Les résultats ne correspondent pas.");
            }

        } catch (Exception e) {
            System.err.println("Erreur durant l'exécution du test :");
            e.printStackTrace();
        }
        System.out.println("-----------------------------------exo2 b--------------------------------------------");

        Integer dishId = 2;
        double salePrice = 4200.00;
        double cost = 1700.0000;
        double expectedMargin = salePrice - cost;

        try {
            System.out.println("=== TEST MARGE BRUTE : ");
            Double actualMargin = retriever.getGrossMargin(dishId);

            System.out.println("Vente : " + salePrice);
            System.out.println("Marge obtenue : " + actualMargin);

            if (actualMargin != null && Math.abs(actualMargin - expectedMargin) < 0.001) {
                System.out.println(" SUCCÈS : La marge brute calculée par la DB est juste !");
            } else {
                System.out.println(" Différence détectée. Vérifiez le coût des ingrédients pour l'ID 1.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("-----------------------------------exo3--------------------------------------------");

        LocalDate startDate = LocalDate.of(2024, 1, 4);
        LocalDate endDate = LocalDate.of(2024, 1, 6);
        String periodicity = "JOUR";

        try {
            System.out.println("=== STATISTIQUES D'ÉVOLUTION DU STOCK (PAR ID) ===");

            Map<Integer, List<Double>> stats = retriever.getStockEvolutionById(periodicity, startDate, endDate);

            System.out.print(String.format("%-10s", "ID Ingredients"));
            List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
            for (LocalDate date : allDates) {
                System.out.print(String.format("| %-12s", date));
            }
            System.out.println("\n" + "=".repeat(10 + (allDates.size() * 15)));

            stats.forEach((id, values) -> {
                System.out.print(String.format("%-10d", id));
                for (Double val : values) {
                    System.out.print(String.format("| %-12.2f", val));
                }
                System.out.println();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
