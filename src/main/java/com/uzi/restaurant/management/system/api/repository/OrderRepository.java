package com.uzi.restaurant.management.system.api.repository;

import com.uzi.restaurant.management.system.api.model.Order;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

@Repository
public class OrderRepository {
    private PriorityBlockingQueue<Order> priorityBlockingQueue = new PriorityBlockingQueue<>(5);
    private Map<Integer, Order> completedOrders = new LinkedHashMap<>();

    public PriorityBlockingQueue<Order> getPriorityBlockingQueue() {
        return priorityBlockingQueue;
    }

    public void addOrderToQueue(Order order) {
        priorityBlockingQueue.add(order);
    }

    public Map<Integer, Order> getCompletedOrders() {
        return completedOrders;
    }

    public synchronized void addCompletedOrder(Order order) {
        completedOrders.put(order.getTableNumber(), order);
    }
}
