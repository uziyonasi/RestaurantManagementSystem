package com.uzi.restaurant.management.system.api.model;

import java.util.List;

public class Bill {
    private Integer tableNumber;
    private List<BillItem> billItems;
    private int totalSum;

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void addToTotalSum(int price) {
        totalSum+=price;
    }
}
