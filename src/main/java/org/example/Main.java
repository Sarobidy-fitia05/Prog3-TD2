package org.example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        /* =========================
           a) findDishById(1)
           ========================= */
      /*  System.out.println("a) findDishById(1)");
        Dish dish1 = dr.findDishById(1);
        System.out.println(dish1.getName());
        dish1.getIngredients().forEach(i ->
                System.out.println("- " + i.getName())
        );*/

        /* =========================
           b) findDishById(999)
           ========================= */
       /* System.out.println("\nb) findDishById(999)");
        try {
            dr.findDishById(999);
        } catch (RuntimeException e) {
            System.out.println("Exception OK : " + e.getMessage());
        }*/

        /* =========================
           c) findIngredients(page=2, size=2)
           ========================= */
       /* System.out.println("\nc) findIngredients(page=2, size=2)");
        List<Ingredient> page2 = dr.findIngredients(2, 2);
        page2.forEach(i -> System.out.println(i.getName()));*/

        /* =========================
           d) findIngredients(page=3, size=5)
           ========================= */
        /*System.out.println("\nd) findIngredients(page=3, size=5)");
        List<Ingredient> page3 = dr.findIngredients(3, 5);
        System.out.println("Résultat vide ? " + page3.isEmpty());*/

        /* =========================
           e) findDishsByIngredientName("eur")
           ========================= */
        /*System.out.println("\ne) findDishsByIngredientName(\"eur\")");
        List<Dish> dishesByIng = dr.findDishsByIngredientName("eur");
        dishesByIng.forEach(d -> System.out.println(d.getName()));*/

        /* =========================
           i) createIngredients
           ========================= */
        /*System.out.println("\ni) createIngredients");
        Ingredient fromage = new Ingredient(
                null,
                "Fromage",
                CategorieEnum.DAIRY,
                1200.0,
                null,
                null,
                null
        );
        Ingredient oignon = new Ingredient(
                null,
                "Oignon",
                CategorieEnum.VEGETABLE,
                500.0,
                null,
                null,
                null);*/

       /* List<Ingredient> created = dr.createIngredients(List.of(fromage, oignon));
        created.forEach(i -> System.out.println(i.getName() + " créé"));*/

        /* =========================
           j) createIngredients avec doublon
           ========================= */
        /*System.out.println("\nj) createIngredients avec doublon");
        try {
            Ingredient carotte = new Ingredient(
                    null,
                    "Carotte",
                     CategorieEnum.VEGETABLE,
                    2000.0,
                    null,
                    null
            );
            Ingredient laitue = new Ingredient(
                    null,
                    "Laitue",
                    CategorieEnum.VEGETABLE,
                    2000.0,
                    null,
                    null,
                    null
            );
            dr.createIngredients(List.of(carotte, laitue));
        } catch (RuntimeException e) {
            System.out.println("Exception OK : " + e.getMessage());
        }

        /* =========================
           k) saveDish (nouveau)
           ========================= */
        /*System.out.println("\nk) saveDish - création");
        Dish soupe = new Dish();
        soupe.setName("Soupe de légumes");
        soupe.setDishTypeEnum(DishTypeEnum.START);
        soupe.setIngredients(List.of(oignon));

        dr.saveDish(soupe);
        System.out.println("Plat créé : " + soupe.getName());*/

        /* =========================
           l) saveDish - ajout ingrédients
           ========================= */
        /*System.out.println("\nl) saveDish - ajout ingrédients");
        Dish salade = dr.findDishById(1);
        salade.setIngredients(List.of(oignon, fromage));
        dr.saveDish(salade);
        System.out.println("Salade mise à jour");*/

        /* =========================
           m) saveDish - suppression ingrédients
           ========================= */
        /*System.out.println("\nm) saveDish - suppression ingrédients");
        salade.setIngredients(List.of(fromage));
        dr.saveDish(salade);
        System.out.println("Salade modifiée (reste fromage)");*/


        /* =========================
   TD4 - Tests des stocks
   ========================= */
        System.out.println("\n=== TD4 - Gestion des stocks ===");

// Test de getStockValueAt pour chaque ingrédient
        Instant testTime = Instant.parse("2024-01-06T12:00:00Z");

        System.out.println("\nStock à 2024-01-06 12:00:");
        System.out.println("Laitue: " + dr.getStockValueAt(1, testTime) + " KG");
        System.out.println("Tomate: " + dr.getStockValueAt(2, testTime) + " KG");
        System.out.println("Poulet: " + dr.getStockValueAt(3, testTime) + " KG");
        System.out.println("Chocolat: " + dr.getStockValueAt(4, testTime) + " KG");
        System.out.println("Beurre: " + dr.getStockValueAt(5, testTime) + " KG");

// Test de sauvegarde avec mouvement de stock
        System.out.println("\nTest de saveIngredient avec mouvement:");
        Ingredient laitueWithMovement = new Ingredient();
        laitueWithMovement.setId(1); // Laitue
        laitueWithMovement.setName("Laitue");

// Création d'un mouvement de stock
        StockMovement newMovement = new StockMovement(
                11, // Nouvel ID
                laitueWithMovement,
                0.5,
                "KG",
                LocalDateTime.now()
        );

        laitueWithMovement.setStockMovementList(List.of(newMovement));
        dr.saveIngredient(laitueWithMovement);
        System.out.println("Mouvement de stock ajouté à Laitue");

// Afficher les mouvements pour Laitue
        System.out.println("\nMouvements de stock pour Laitue:");
        List<StockMovement> movements = dr.getStockMovementsForIngredient(1);
        for (StockMovement m : movements) {
            System.out.println("- ID: " + m.getId() +
                    ", Quantité: " + m.getQuantity() +
                    " " + m.getUnit() +
                    ", Date: " + m.getMovementDate());
        }

        /* =========================
   ANNEXE 2 - Tests des commandes
   ========================= */
        System.out.println("\n=== ANNEXE 2 - Gestion des commandes ===");



    }

}
