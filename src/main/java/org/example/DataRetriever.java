package org.example;

import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
        private final DBConnection dbConnection = new DBConnection();


        public Dish findDishById(Long id) {
            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement statement= connection.prepareStatement("select d.id, d.name, d.price from dish d where id = ?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        return new Dish(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDouble("price"),
                                CategoryEnum.valueOf(resultSet.getString("category")),
                                new ArrayList<>() // ingrédients vides pour l’instant
                        );

                    }

                }
                throw new RuntimeException("Dish.id= "+id +"not found")

            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    public List<Ingredient> findIngredients(int page, int size) {
        try (Connection connection = dbConnection.getConnection()
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM ingredient LIMIT ? OFFSET ?")) {

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
    }    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
        String insertSql = "INSERT INTO ingredient(name, category) VALUES (?, ?)";

        try (Connection conn = dbConnection.getDBConnection()) {
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
    public Dish saveDish(Dish dish) {
        String insertDish = "INSERT INTO dish(name) VALUES (?) RETURNING id";
        String updateDish = "UPDATE dish SET name = ? WHERE id = ?";
        String deleteLinks = "DELETE FROM dish_ingredient WHERE dish_id = ?";
        String insertLink = "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = dbConnection.getDBConnection()) {
            conn.setAutoCommit(false);

            if (dish.getId() == null) {
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

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ingredientName + "%");
            ResultSet rs = ps.executeQuery();

            List<Dish> dishes = new ArrayList<>();
            while (rs.next()) {
                dishes.add(new Dish(rs.getInt("id"), rs.getString("name")));
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

        try (Connection conn = dbConnection.getDBConnection();
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
}


}
