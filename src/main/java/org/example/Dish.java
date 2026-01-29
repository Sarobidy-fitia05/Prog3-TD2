package org.example;

import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id ;
    private Double price;
    private String name;
    private DishTypeEnum dishTypeEnum;
    private List<Ingredient> ingredients;
    private List<StockMovement> stockMovementList;

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }


    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    private List<DishIngredient> dishIngredients;


    public Dish() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", dishTypeEnum=" + dishTypeEnum +
                ", ingredients=" + ingredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(price, dish.price) && Objects.equals(name, dish.name) && dishTypeEnum == dish.dishTypeEnum && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, dishTypeEnum, ingredients);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishTypeEnum(DishTypeEnum dishTypeEnum) {
        this.dishTypeEnum = dishTypeEnum;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        if (ingredients == null) {
            this.ingredients = null;
            return;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).setDish(this);
        }
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public DishTypeEnum getDishTypeEnum() {
        return dishTypeEnum;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Dish(Integer id, Double price, String name, DishTypeEnum dishTypeEnum, List<Ingredient> ingredients) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.dishTypeEnum = dishTypeEnum;
        this.ingredients = ingredients;
    }
    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Price is null");
        }
        return price ;
    }
}
