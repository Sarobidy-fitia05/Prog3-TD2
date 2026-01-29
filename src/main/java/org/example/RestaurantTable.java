package org.example;

import java.util.List;
import java.util.Objects;

public class RestaurantTable {
    private Integer id;
    private Integer number;
    private List<Order> orders;

    public RestaurantTable() {
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", number=" + number +
                ", orders=" + orders +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, orders);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public RestaurantTable(Integer id, Integer number, List<Order> orders) {
        this.id = id;
        this.number = number;
        this.orders = orders;
    }
}
