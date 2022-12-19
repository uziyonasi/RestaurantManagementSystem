package com.uzi.restaurant.management.system.api.model;

import java.time.Duration;

public class DishItem {
    private String name;
    private Integer price;
    private Duration preparationTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Duration getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Duration preparationTime) {
        this.preparationTime = preparationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DishItem)) {
            return false;
        }
        DishItem dishItem = (DishItem) o;
        return name.equals(dishItem.name) && price == dishItem.price && preparationTime.equals(dishItem.preparationTime);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * price.hashCode() * preparationTime.hashCode();
    }
}
