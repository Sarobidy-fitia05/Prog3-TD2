package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();
        // -------------------------------
        // a) findDishById(1)
        try {
            System.out.println("a) Test findDishById(1)");
            System.out.println(dr.findDishById(1));
        } catch (Exception e) {
            System.out.println("a) Erreur : " + e.getMessage());
        }

        // -------------------------------
        // b) findDishById(999) -> exception
        try {
            System.out.println("\nb) Test findDishById(999)");
            System.out.println(dr.findDishById(999));
        } catch (Exception e) {
            System.out.println("b) Erreur attendue : " + e.getMessage());
        }

        // -------------------------------
        // c) findIngredients(page=2, size=2)
        System.out.println("\nc) Test findIngredients(2,2)");
        System.out.println(dr.findIngredients(2, 2));

        // -------------------------------
        // d) findIngredients(page=3, size=5)
        System.out.println("\nd) Test findIngredients(3,5)");
        System.out.println(dr.findIngredients(3, 5));

        // -------------------------------
        // e) findDishsByIngredientName("eur")
        System.out.println("\ne) Test findDishsByIngredientName(\"eur\")");
        System.out.println(dr.findDishsByIngredientName("eur"));

        // --------------------------------------------
        // i) createIngredients([Fromage, Oignon])
        try {
            System.out.println("\ni) Test createIngredients([Fromage, Oignon])");
            List<Ingredient> newIngredients = new ArrayList<>();
            newIngredients.add(new Ingredient(null, "Fromage", CategorieEnum.DAIRY, 1200.0, null, 0.0));
            newIngredients.add(new Ingredient(null, "Oignon", CategorieEnum.VEGETABLE, 500.0, null, 0.0));
            System.out.println(dr.createIngredients(newIngredients));
        } catch (Exception e) {
            System.out.println("i) Erreur : " + e.getMessage());
        }

        // -------------------------------
        // j) createIngredients([Carotte, Laitue]) -> exception
        try {
            System.out.println("\nj) Test createIngredients([Carotte, Laitue])");
            List<Ingredient> newIngredients2 = new ArrayList<>();
            newIngredients2.add(new Ingredient(null, "Carotte", CategorieEnum.VEGETABLE, 2000.0, null, 0.0));
            newIngredients2.add(new Ingredient(null, "Laitue", CategorieEnum.VEGETABLE, 2000.0, null, 0.0));
            System.out.println(dr.createIngredients(newIngredients2));
        } catch (Exception e) {
            System.out.println("j) Erreur attendue : " + e.getMessage());
        }

        // -------------------------------
        // k) saveDish(Soupe de légumes + Oignon)
        try {
            System.out.println("\nk) Test saveDish(Soupe de légumes + Oignon)");
            Dish soupe = new Dish(null, 0.0, "Soupe de légumes", DishTypeEnum.START, new ArrayList<>());
            soupe.getIngredients().add(new Ingredient(0, "Oignon", CategorieEnum.VEGETABLE, 500.0, null, 0.0));
            System.out.println(dr.saveDish(soupe));
        } catch (Exception e) {
            System.out.println("k) Erreur : " + e.getMessage());
        }

        // -------------------------------
        // l) saveDish(id=1 Salade fraîche + Oignon + Laitue + Tomate + Fromage)
        try {
            System.out.println("\nl) Test saveDish(id=1 Salade fraîche + Oignon + Laitue + Tomate + Fromage)");
            Dish salade = new Dish(1, 0.0, "Salade fraîche", DishTypeEnum.START, new ArrayList<>());
            salade.getIngredients().add(new Ingredient(0, "Oignon", CategorieEnum.VEGETABLE, 500.0, null, 0.0));
            salade.getIngredients().add(new Ingredient(1, "Laitue", CategorieEnum.VEGETABLE, 100.0, null, 0.0));
            salade.getIngredients().add(new Ingredient(2, "Tomate", CategorieEnum.VEGETABLE, 150.0, null, 0.0));
            salade.getIngredients().add(new Ingredient(0, "Fromage", CategorieEnum.DAIRY, 1200.0, null, 0.0));
            System.out.println(dr.saveDish(salade));
        } catch (Exception e) {
            System.out.println("l) Erreur : " + e.getMessage());
        }

        // -------------------------------
        // m) saveDish(id=1 Salade de fromage + Fromage)
        try {
            System.out.println("\nm) Test saveDish(id=1 Salade de fromage + Fromage)");
            Dish salade2 = new Dish(1, 0.0, "Salade de fromage", DishTypeEnum.START, new ArrayList<>());
            salade2.getIngredients().add(new Ingredient(0, "Fromage", CategorieEnum.DAIRY, 1200.0, null, 0.0));
            System.out.println(dr.saveDish(salade2));
        } catch (Exception e) {
            System.out.println("m) Erreur : " + e.getMessage());
        }
    }
}
