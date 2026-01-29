package org.example;

import java.time.LocalDateTime;
import java.util.Objects;

public class StockMovement {
    private int id;
    private Ingredient ingredient;
    private double quantity;
    private String unit;
    private LocalDateTime movementDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return id == that.id && Double.compare(quantity, that.quantity) == 0 && Objects.equals(ingredient, that.ingredient) && Objects.equals(unit, that.unit) && Objects.equals(movementDate, that.movementDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredient, quantity, unit, movementDate);
    }

    public StockMovement(int id, Ingredient ingredient, double quantity, String unit, LocalDateTime movementDate) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.movementDate = movementDate;
    }

    public int getId() {
        return id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }
}
