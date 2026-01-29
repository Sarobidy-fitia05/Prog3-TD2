package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer id;
    private String reference;
    private Double totalHt;
    private Double totalTtc;
    private LocalDateTime creationDatetime;
    private List<DishOrder> dishOrders;
    private RestaurantTable table;
    private LocalDateTime arrivalDatetime;
    private LocalDateTime departureDatetime;

    // CONSTRUCTEUR VIDE - AJOUTEZ-LE
    public Order() {
    }

    // Constructeur avec tous les paramètres (optionnel)
    public Order(Integer id, String reference, Double totalHt, Double totalTtc,
                 LocalDateTime creationDatetime, List<DishOrder> dishOrders,
                 RestaurantTable table, LocalDateTime arrivalDatetime,
                 LocalDateTime departureDatetime) {
        this.id = id;
        this.reference = reference;
        this.totalHt = totalHt;
        this.totalTtc = totalTtc;
        this.creationDatetime = creationDatetime;
        this.dishOrders = dishOrders;
        this.table = table;
        this.arrivalDatetime = arrivalDatetime;
        this.departureDatetime = departureDatetime;
    }

    // Getters et Setters (tous)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Double getTotalHt() { return totalHt; }
    public void setTotalHt(Double totalHt) { this.totalHt = totalHt; }

    public Double getTotalTtc() { return totalTtc; }
    public void setTotalTtc(Double totalTtc) { this.totalTtc = totalTtc; }

    public LocalDateTime getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(LocalDateTime creationDatetime) { this.creationDatetime = creationDatetime; }

    public List<DishOrder> getDishOrders() { return dishOrders; }
    public void setDishOrders(List<DishOrder> dishOrders) { this.dishOrders = dishOrders; }

    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }

    public LocalDateTime getArrivalDatetime() { return arrivalDatetime; }
    public void setArrivalDatetime(LocalDateTime arrivalDatetime) { this.arrivalDatetime = arrivalDatetime; }

    public LocalDateTime getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(LocalDateTime departureDatetime) { this.departureDatetime = departureDatetime; }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(reference, order.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", totalHt=" + totalHt +
                ", totalTtc=" + totalTtc +
                ", creationDatetime=" + creationDatetime +
                ", table=" + (table != null ? table.getNumber() : "null") +
                ", arrivalDatetime=" + arrivalDatetime +
                ", departureDatetime=" + departureDatetime +
                ", dishOrders=" + dishOrders +
                '}';
    }
}