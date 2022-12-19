package com.uzi.restaurant.management.system.api.model;

import java.util.List;

public class Order implements Comparable<Order>{
    private Integer tableNumber;
    private List<DishItem> dishItems;
    private String specialComments;
    private Integer priority;
    private OrderStatus orderStatus;

    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<DishItem> getDishItems() {
        return dishItems;
    }

    public void setDishItems(List<DishItem> dishItems) {
        this.dishItems = dishItems;
    }

    public String getSpecialComments() {
        return specialComments;
    }

    public void setSpecialComments(String specialComments) {
        this.specialComments = specialComments;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public int compareTo(Order order) {
        if (this.priority < order.priority) {
            return 1;
        } else if (this.priority > order.priority) {
            return -1;
        }
        return 0;
    }
}
