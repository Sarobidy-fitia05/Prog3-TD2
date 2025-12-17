package org.example;

import java.util.List;

public class Dish {
    private int id;
    private String name;
    private Double price;
    private CategoryEnum category ;
    private List<Ingredient> ingredients;

    public Dish(int id, String name, Double price, CategoryEnum category, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.ingredients = ingredients;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", ingredients=" + ingredients +
                '}';
    }
}
