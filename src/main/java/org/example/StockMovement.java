package org.example;

import java.time.LocalDateTime;
import java.util.Objects;

public class StockMovement {
    private int id;
    private Ingredient ingredient;
    private double quantity;
    private String unit;
    private LocalDateTime movementDate;

    // Constructeur vide
    public StockMovement() {
    }

    // Constructeur avec paramètres
    public StockMovement(int id, Ingredient ingredient, double quantity,
                         String unit, LocalDateTime movementDate) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.movementDate = movementDate;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return id == that.id &&
                Double.compare(quantity, that.quantity) == 0 &&
                Objects.equals(ingredient, that.ingredient) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(movementDate, that.movementDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredient, quantity, unit, movementDate);
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredient=" + (ingredient != null ? ingredient.getName() : "null") +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", movementDate=" + movementDate +
                '}';
    }
}
