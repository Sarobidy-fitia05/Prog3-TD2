package org.example;

import java.util.Objects;

public class Ingredient {
    private Integer id;
    private String name;
    private CategorieEnum categorieEnum;
    private Double price;
    private Dish dish;
    private Double quantity;

    public Ingredient() {
    }

    public Ingredient(Integer id, String name, CategorieEnum categorieEnum, Double price, Dish dish, Double quantity) {
        this.id = id;
        this.name = name;
        this.categorieEnum = categorieEnum;
        this.price = price;
        this.dish = dish;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategorieEnum getCategorieEnum() {
        return categorieEnum;
    }

    public Double getPrice() {
        return price;
    }

    public Dish getDish() {
        return dish;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategorieEnum(CategorieEnum categorieEnum) {
        this.categorieEnum = categorieEnum;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && categorieEnum == that.categorieEnum && Objects.equals(price, that.price) && Objects.equals(dish, that.dish) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categorieEnum, price, dish, quantity);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categorieEnum=" + categorieEnum +
                ", price=" + price +
                ", dish=" + dish +
                ", quantity=" + quantity +
                '}';
    }
}
