package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final Connection connection;

    public DataRetriever() {
        this.connection = DBConnection.getConnection();
    }
    public Dish findDishById(Integer id) {
        String sql = """
        SELECT d.id AS dish_id, d.name AS dish_name,
               i.id AS ingredient_id, i.name AS ingredient_name, i.category
        FROM dish d
        LEFT JOIN dish_ingredient di ON d.id = di.dish_id
        LEFT JOIN ingredient i ON di.ingredient_id = i.id
        WHERE d.id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                if (dish == null) {
                    dish = new Dish();
                    dish.setId(rs.getInt("dish_id"));
                    dish.setName(rs.getString("dish_name"));
                }

                if (rs.getInt("ingredient_id") != 0) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("ingredient_id"));
                    ingredient.setName(rs.getString("ingredient_name"));
                    ingredient.setCategorieEnum(
                            CategorieEnum.valueOf(rs.getString("category"))
                    );
                    ingredients.add(ingredient);
                }
            }

            if (dish != null) {
                dish.setIngredients(ingredients);
            }

            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Ingredient> findIngredients(int page, int size) {
        String sql = "SELECT * FROM ingredient LIMIT ? OFFSET ?";

        List<Ingredient> ingredients = new ArrayList<>();
        int offset = page * size;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setCategorieEnum(
                        CategorieEnum.valueOf(rs.getString("category"))
                );
                ingredients.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {

        String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
        String insertSql = "INSERT INTO ingredient(name, category) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            for (Ingredient ingredient : newIngredients) {
                try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
                    checkPs.setString(1, ingredient.getName());
                    ResultSet rs = checkPs.executeQuery();
                    rs.next();

                    if (rs.getInt(1) > 0) {
                        throw new RuntimeException(
                                "Ingredient déjà existant : " + ingredient.getName()
                        );
                    }
                }
            }

            for (Ingredient ingredient : newIngredients) {
                try (PreparedStatement insertPs =
                             connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                    insertPs.setString(1, ingredient.getName());
                    insertPs.setString(2, ingredient.getCategorieEnum().name());
                    insertPs.executeUpdate();

                    ResultSet keys = insertPs.getGeneratedKeys();
                    if (keys.next()) {
                        ingredient.setId(keys.getInt(1));
                    }
                }
            }

            connection.commit();
            return newIngredients;

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Dish saveDish(Dish dishToSave) {

        try {
            connection.setAutoCommit(false);

            // 1) Vérifier si existe
            String checkSql = "SELECT COUNT(*) FROM dish WHERE id = ?";
            PreparedStatement checkPs = connection.prepareStatement(checkSql);
            checkPs.setInt(1, dishToSave.getId());
            ResultSet rs = checkPs.executeQuery();
            rs.next();

            boolean exists = rs.getInt(1) > 0;

            // 2) UPDATE ou INSERT
            if (exists) {
                String updateSql = "UPDATE dish SET name = ?, price = ?, dish_type = ? WHERE id = ?";
                PreparedStatement updatePs = connection.prepareStatement(updateSql);
                updatePs.setString(1, dishToSave.getName());
                updatePs.setDouble(2, dishToSave.getPrice());
                updatePs.setString(3, dishToSave.getDishTypeEnum().name());
                updatePs.setInt(4, dishToSave.getId());
                updatePs.executeUpdate();
            } else {
                String insertSql = "INSERT INTO dish(name, price, dish_type) VALUES (?, ?, ?)";
                PreparedStatement insertPs = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                insertPs.setString(1, dishToSave.getName());
                insertPs.setDouble(2, dishToSave.getPrice());
                insertPs.setString(3, dishToSave.getDishTypeEnum().name());
                insertPs.executeUpdate();

                ResultSet keys = insertPs.getGeneratedKeys();
                if (keys.next()) {
                    dishToSave.setId(keys.getInt(1));
                }
            }

            // 3) gérer dish_ingredient
            String deleteSql = "DELETE FROM dish_ingredient WHERE dish_id = ?";
            PreparedStatement deletePs = connection.prepareStatement(deleteSql);
            deletePs.setInt(1, dishToSave.getId());
            deletePs.executeUpdate();

            String insertDI = "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)";
            for (Ingredient ing : dishToSave.getIngredients()) {
                PreparedStatement insertPs = connection.prepareStatement(insertDI);
                insertPs.setInt(1, dishToSave.getId());
                insertPs.setInt(2, ing.getId());
                insertPs.executeUpdate();
            }

            connection.commit();
            return dishToSave;

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<Dish> findDishsByIngredientName(String ingredientName) {
        String sql = "SELECT d.* FROM dish d " +
                "JOIN ingredient i ON i.id_dish = d.id " +
                "WHERE i.name LIKE ?";

        List<Dish> dishes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + ingredientName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setPrice(rs.getDouble("price"));
                d.setDishTypeEnum(DishTypeEnum.valueOf(rs.getString("dish_type")));
                dishes.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dishes;
    }


}
