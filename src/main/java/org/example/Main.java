package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;
public class Main {
    public static <connection> void main(String[] args) {
            String url = "jdbc:postgresql://localhost:5432/mini_dish_db";
        String user = "mini_dish_db_manager";
        String password = "123456";

        try {
            Integer testId = 1;

            System.out.println("== Test findDishById & getGrossMargin ==");

            Dish d = dr.findDishById(testId);
            if (d == null) {
                System.out.println("Plat non trouvé pour id=" + testId);
            } else {
                System.out.println("Plat récupéré: " + d);
                try {
                    Double margin = d.getGrossMargin(); 
                    System.out.println("Marge brute: " + margin);
                } catch (IllegalStateException ex) {
                    System.out.println("Erreur calcul marge: " + ex.getMessage());
                }
            }

            System.out.println("\n== Test saveDish (création/modification) ==");

            Dish newDish = new Dish(null, "Nouvelle salade", 1200.0, null);
            Dish saved = dr.saveDish(newDish);
            System.out.println("Plat créé: " + saved);

            saved.setPrice(2200.0);
            Dish saved2 = dr.saveDish(saved);
            System.out.println("Plat mis à jour (prix défini): " + saved2);
            try {
                System.out.println("Nouvelle marge: " + saved2.getGrossMargin());
            } catch (IllegalStateException ex) {
                System.out.println("Impossible de calculer la marge: " + ex.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}