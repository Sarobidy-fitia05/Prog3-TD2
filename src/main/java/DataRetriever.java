import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {
    Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT d.id as dish_id, d.name as dish_name, d.dish_type, 
                           d.selling_price as dish_price,
                           di.quantity_required, di.unit,
                           i.id as ing_id, i.name as ing_name, i.price as ing_price, 
                           i.category, i.required_quantity
                    FROM dish d
                    LEFT JOIN dish_ingredient di ON d.id = di.id_dish
                    LEFT JOIN ingredient i ON di.id_ingredient = i.id
                    WHERE d.id = ?
                    ORDER BY i.name
                    """);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Dish dish = null;
            while (resultSet.next()) {
                if (dish == null) {
                    dish = new Dish();
                    dish.setId(resultSet.getInt("dish_id"));
                    dish.setName(resultSet.getString("dish_name"));
                    dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));
                    dish.setPrice(resultSet.getObject("dish_price") == null
                            ? null : resultSet.getDouble("dish_price"));
                }

                if (resultSet.getObject("ing_id") != null) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ing_id"));
                    ingredient.setName(resultSet.getString("ing_name"));
                    ingredient.setPrice(resultSet.getDouble("ing_price"));
                    ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));

                    Object requiredQuantity = resultSet.getObject("required_quantity");
                    ingredient.setQuantity(requiredQuantity == null ? null : resultSet.getDouble("required_quantity"));

                    if (dish.getIngredients() == null) {
                        dish.setIngredients(new ArrayList<>());
                    }
                    dish.getIngredients().add(ingredient);

                    Double quantityRequired = resultSet.getDouble("quantity_required");
                    String unit = resultSet.getString("unit");
                    if (!resultSet.wasNull()) {
                        dish.addIngredientQuantity(ingredient.getId(), quantityRequired, unit);
                    }
                }
            }

            dbConnection.closeConnection(connection);
            if (dish != null) {
                return dish;
            }
            throw new RuntimeException("Dish not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Dish saveDish(Dish toSave) {
        String upsertDishSql = """
                INSERT INTO dish (id, name, dish_type, selling_price)
                VALUES (?, ?, ?::dish_type, ?)
                ON CONFLICT (id) DO UPDATE
                SET name = EXCLUDED.name,
                    dish_type = EXCLUDED.dish_type,
                    selling_price = EXCLUDED.selling_price
                RETURNING id
                """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            Integer dishId;

            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "dish", "id"));
                }
                ps.setString(2, toSave.getName());
                ps.setString(3, toSave.getDishType().name());
                if (toSave.getPrice() != null) {
                    ps.setDouble(4, toSave.getPrice());
                } else {
                    ps.setNull(4, Types.DOUBLE);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    dishId = rs.getInt(1);
                }
            }

            List<Ingredient> newIngredients = toSave.getIngredients();
            saveDishIngredients(conn, dishId, newIngredients, toSave.getIngredientQuantities(), toSave.getIngredientUnits());

            conn.commit();
            return findDishById(dishId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        if (newIngredients == null || newIngredients.isEmpty()) {
            return List.of();
        }
        List<Ingredient> savedIngredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            String insertSql = """
                        INSERT INTO ingredient (id, name, category, price, required_quantity)
                        VALUES (?, ?, ?::ingredient_category, ?, ?)
                        RETURNING id
                    """;
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Ingredient ingredient : newIngredients) {
                    if (ingredient.getId() != null) {
                        ps.setInt(1, ingredient.getId());
                    } else {
                        ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                    }
                    ps.setString(2, ingredient.getName());
                    ps.setString(3, ingredient.getCategory().name());
                    ps.setDouble(4, ingredient.getPrice());
                    if (ingredient.getQuantity() != null) {
                        ps.setDouble(5, ingredient.getQuantity());
                    }else {
                        ps.setNull(5, Types.DOUBLE);
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        int generatedId = rs.getInt(1);
                        ingredient.setId(generatedId);
                        savedIngredients.add(ingredient);
                    }
                }
                conn.commit();
                return savedIngredients;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeConnection(conn);
        }
    }

    private void saveDishIngredients(Connection conn, Integer dishId,
                                     List<Ingredient> ingredients,
                                     java.util.Map<Integer, Double> quantities,
                                     java.util.Map<Integer, String> units) throws SQLException {

        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }

        if (ingredients == null || ingredients.isEmpty()) {
            return;
        }

        String insertSql = """
                INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (Ingredient ingredient : ingredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, ingredient.getId());

                Double quantity = quantities != null ? quantities.get(ingredient.getId()) : null;
                String unit = units != null ? units.get(ingredient.getId()) : null;

                if (quantity != null) {
                    ps.setDouble(3, quantity);
                } else {
                    ps.setDouble(3, 1.0);
                }

                if (unit != null && !unit.isEmpty()) {
                    ps.setString(4, unit);
                } else {
                    ps.setString(4, "KG");
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<Ingredient> findIngredientByDishId(Integer idDish) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT i.id, i.name, i.price, i.category, i.required_quantity,
                           di.quantity_required as dish_quantity, di.unit
                    FROM ingredient i
                    JOIN dish_ingredient di ON i.id = di.id_ingredient
                    WHERE di.id_dish = ?
                    """);
            preparedStatement.setInt(1, idDish);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setPrice(resultSet.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));

                Object requiredQuantity = resultSet.getObject("required_quantity");
                ingredient.setQuantity(requiredQuantity == null
                        ? null : resultSet.getDouble("required_quantity"));

                ingredients.add(ingredient);
            }
            dbConnection.closeConnection(connection);
            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sql = "SELECT pg_get_serial_sequence(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
        if (sequenceName == null) {
            throw new IllegalArgumentException(
                    "Any sequence found for " + tableName + "." + columnName
            );
        }
        updateSequenceNextValue(conn, tableName, columnName, sequenceName);

        String nextValSql = "SELECT nextval(?)";

        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
        String setValSql = String.format(
                "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                sequenceName, columnName, tableName
        );

        try (PreparedStatement ps = conn.prepareStatement(setValSql)) {
            ps.executeQuery();
        }
    }

    public Double getDishCost(Integer dishId) {
        Dish dish = findDishById(dishId);
        if (dish == null || dish.getIngredients() == null) {
            return 0.0;
        }

        double totalCost = 0.0;
        for (Ingredient ingredient : dish.getIngredients()) {
            Double quantity = dish.getIngredientQuantity(ingredient.getId());
            if (quantity != null && ingredient.getPrice() != null) {
                totalCost += ingredient.getPrice() * quantity;
            }
        }

        return Math.round(totalCost * 100.0) / 100.0;
    }

    public Double getGrossMargin(Integer dishId) {
        Dish dish = findDishById(dishId);
        if (dish == null) {
            throw new RuntimeException("Plat non trouvé avec l'id: " + dishId);
        }

        if (dish.getPrice() == null) {
            throw new RuntimeException("Le plat '" + dish.getName() + "' n'a pas de prix de vente");
        }

        Double cost = getDishCost(dishId);
        Double sellingPrice = dish.getPrice();

        Double margin = sellingPrice - cost;
        return Math.round(margin * 100.0) / 100.0;
    }

    public void testTD3Exercice() {
        System.out.println("=== TEST EXERCICE TD3 ===");

        System.out.println("\n1. Calcul des coûts :");
        System.out.println("Salade fraîche (ID 1): " + getDishCost(1) + " (attendu: 250.00)");
        System.out.println("Poulet grillé (ID 2): " + getDishCost(2) + " (attendu: 4500.00)");
        System.out.println("Riz aux légumes (ID 3): " + getDishCost(3) + " (attendu: 0.00)");
        System.out.println("Gâteau au chocolat (ID 4): " + getDishCost(4) + " (attendu: 1400.00)");
        System.out.println("Salade de fruits (ID 5): " + getDishCost(5) + " (attendu: 0.00)");

        System.out.println("\n2. Calcul des marges brutes :");
        try {
            System.out.println("Salade fraîche: " + getGrossMargin(1) + " (attendu: 3250.00)");
        } catch (RuntimeException e) {
            System.out.println("Salade fraîche: Exception - " + e.getMessage());
        }

        try {
            System.out.println("Poulet grillé: " + getGrossMargin(2) + " (attendu: 7500.00)");
        } catch (RuntimeException e) {
            System.out.println("Poulet grillé: Exception - " + e.getMessage());
        }

        try {
            System.out.println("Riz aux légumes: " + getGrossMargin(3) + " (attendu: Exception)");
        } catch (RuntimeException e) {
            System.out.println("Riz aux légumes: ✓ Exception - " + e.getMessage());
        }

        try {
            System.out.println("Gâteau au chocolat: " + getGrossMargin(4) + " (attendu: 6600.00)");
        } catch (RuntimeException e) {
            System.out.println("Gâteau au chocolat: Exception - " + e.getMessage());
        }

        try {
            System.out.println("Salade de fruits: " + getGrossMargin(5) + " (attendu: Exception)");
        } catch (RuntimeException e) {
            System.out.println("Salade de fruits: ✓ Exception - " + e.getMessage());
        }
    }

    public List<Dish> findAllDishes() {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<Dish> dishes = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT id FROM dish ORDER BY name"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dish dish = findDishById(rs.getInt("id"));
                dishes.add(dish);
            }

            dbConnection.closeConnection(connection);
            return dishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}