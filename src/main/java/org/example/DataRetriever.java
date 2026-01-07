package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection dbConnection = new DBConnection();

    // ==========================
    // FIND DISH BY ID (Long)
    // ==========================
    public Dish findDishById(Long id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT d.id, d.name, d.price, d.category FROM dish d WHERE d.id = ?")) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Dish(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            CategoryEnum.valueOf(resultSet.getString("category")),
                            new ArrayList<>()
                    );
                }
            }

            throw new RuntimeException("Dish.id = " + id + " not found");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // FIND INGREDIENTS (PAGINATION)
    // ==========================
    public List<Ingredient> findIngredients(int page, int size) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM ingredient LIMIT ? OFFSET ?")) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            ResultSet rs = ps.executeQuery();
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        CategoryEnum.valueOf(rs.getString("category"))
                ));
            }

            return ingredients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // CREATE INGREDIENTS
    // ==========================
    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {

        String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
        String insertSql = "INSERT INTO ingredient(name, category) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql);
                 PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

                for (Ingredient ingredient : newIngredients) {
                    checkPs.setString(1, ingredient.getName());
                    ResultSet rs = checkPs.executeQuery();
                    rs.next();

                    if (rs.getInt(1) > 0) {
                        throw new RuntimeException("Ingrédient déjà existant : " + ingredient.getName());
                    }

                    insertPs.setString(1, ingredient.getName());
                    insertPs.setString(2, ingredient.getCategory().name());
                    insertPs.executeUpdate();
                }

                conn.commit();
                return newIngredients;

            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // SAVE DISH (AVEC INGREDIENTS)
    // ==========================
    public Dish saveDish(Dish dish) {

        String insertDish = "INSERT INTO dish(name) VALUES (?) RETURNING id";
        String updateDish = "UPDATE dish SET name = ? WHERE id = ?";
        String deleteLinks = "DELETE FROM dish_ingredient WHERE dish_id = ?";
        String insertLink = "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);

            if (dish.getId() == 0) {
                PreparedStatement ps = conn.prepareStatement(insertDish);
                ps.setString(1, dish.getName());
                ResultSet rs = ps.executeQuery();
                rs.next();
                dish.setId(rs.getInt(1));
            } else {
                PreparedStatement ps = conn.prepareStatement(updateDish);
                ps.setString(1, dish.getName());
                ps.setInt(2, dish.getId());
                ps.executeUpdate();
            }

            PreparedStatement deletePs = conn.prepareStatement(deleteLinks);
            deletePs.setInt(1, dish.getId());
            deletePs.executeUpdate();

            PreparedStatement linkPs = conn.prepareStatement(insertLink);
            for (Ingredient ingredient : dish.getIngredients()) {
                linkPs.setInt(1, dish.getId());
                linkPs.setInt(2, ingredient.getId());
                linkPs.executeUpdate();
            }

            conn.commit();
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Dish> findDishsByIngredientName(String ingredientName) {

        String sql = """
            SELECT DISTINCT d.id, d.name
            FROM dish d
            JOIN dish_ingredient di ON d.id = di.dish_id
            JOIN ingredient i ON di.ingredient_id = i.id
            WHERE i.name ILIKE ?
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ingredientName + "%");
            ResultSet rs = ps.executeQuery();

            List<Dish> dishes = new ArrayList<>();
            while (rs.next()) {
                dishes.add(new Dish(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        new ArrayList<>()));
            }

            return dishes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredientsByCriteria(
            String ingredientName,
            CategoryEnum category,
            String dishName,
            int page,
            int size) {

        StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT i.*
            FROM ingredient i
            LEFT JOIN dish_ingredient di ON i.id = di.ingredient_id
            LEFT JOIN dish d ON di.dish_id = d.id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (ingredientName != null) {
            sql.append(" AND i.name ILIKE ?");
            params.add("%" + ingredientName + "%");
        }

        if (category != null) {
            sql.append(" AND i.category = ?");
            params.add(category.name());
        }

        if (dishName != null) {
            sql.append(" AND d.name ILIKE ?");
            params.add("%" + dishName + "%");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        CategoryEnum.valueOf(rs.getString("category"))
                ));
            }

            return ingredients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Dish findDishById(Integer id) throws SQLException {
        String sql = "SELECT id, name, dish_cost, price FROM Dish WHERE id = ?";
        try (Connection c = dbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer dishId = rs.getInt("id");
                    String name = rs.getString("name");
                    // Supposons que la colonne coût s'appelle dish_cost (adapter si besoin)
                    Double dishCost = rs.getObject("dish_cost") == null ? null : rs.getDouble("dish_cost");
                    Double price = rs.getObject("price") == null ? null : rs.getDouble("price");
                    Dish d = new Dish(dishId, name, dishCost, price);
                    return d;
                } else {
                    return null;
                }
            }
        }
    }

    public Dish saveDish(Dish dishToSave) throws SQLException {
        if (dishToSave == null) throw new IllegalArgumentException("dishToSave ne peut pas être null");

        if (dishToSave.getId() == null) {
            String insert = "INSERT INTO Dish (name, dish_cost, price) VALUES (?, ?, ?)";
            try (Connection c = dbConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, dishToSave.getName());
                if (dishToSave.getDishCost() == null) ps.setNull(2, Types.DECIMAL);
                else ps.setDouble(2, dishToSave.getDishCost());
                if (dishToSave.getPrice() == null) ps.setNull(3, Types.DECIMAL);
                else ps.setDouble(3, dishToSave.getPrice());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        dishToSave.setId(keys.getInt(1));
                    }
                }
                return dishToSave;
            }
        } else {
            String update = "UPDATE Dish SET name = ?, dish_cost = ?, price = ? WHERE id = ?";
            try (Connection c = dbConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(update)) {
                ps.setString(1, dishToSave.getName());
                if (dishToSave.getDishCost() == null) ps.setNull(2, Types.DECIMAL);
                else ps.setDouble(2, dishToSave.getDishCost());
                if (dishToSave.getPrice() == null) ps.setNull(3, Types.DECIMAL);
                else ps.setDouble(3, dishToSave.getPrice());
                ps.setInt(4, dishToSave.getId());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    String insert = "INSERT INTO Dish (name, dish_cost, price) VALUES (?, ?, ?)";
                    try (PreparedStatement pin = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                        pin.setString(1, dishToSave.getName());
                        if (dishToSave.getDishCost() == null) pin.setNull(2, Types.DECIMAL);
                        else pin.setDouble(2, dishToSave.getDishCost());
                        if (dishToSave.getPrice() == null) pin.setNull(3, Types.DECIMAL);
                        else pin.setDouble(3, dishToSave.getPrice());
                        pin.executeUpdate();
                        try (ResultSet keys = pin.getGeneratedKeys()) {
                            if (keys.next()) {
                                dishToSave.setId(keys.getInt(1));
                            }
                        }
                    }
                }
                return dishToSave;
            }
        }
}

