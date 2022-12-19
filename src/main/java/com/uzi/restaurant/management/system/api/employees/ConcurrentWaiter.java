package com.uzi.restaurant.management.system.api.employees;

import com.uzi.restaurant.management.system.api.model.DishItem;
import com.uzi.restaurant.management.system.api.model.Order;
import com.uzi.restaurant.management.system.api.model.OrderStatus;
import com.uzi.restaurant.management.system.api.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentWaiter extends Thread{
    private Logger logger= LoggerFactory.getLogger(ConcurrentWaiter.class);
    private PriorityBlockingQueue<Order> priorityBlockingQueue;
    private OrderRepository orderRepository;
    private AtomicInteger cashFlow;

    public ConcurrentWaiter(PriorityBlockingQueue<Order> priorityBlockingQueue, OrderRepository orderRepository,
                            AtomicInteger cashFlow) {
        this.priorityBlockingQueue = priorityBlockingQueue;
        this.orderRepository = orderRepository;
        this.cashFlow = cashFlow;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = priorityBlockingQueue.take();
                if (order.getOrderStatus() != OrderStatus.READY) {
                    logger.info("Waiter {} took order for table {} and current status is {}", currentThread().getName(),
                            order.getTableNumber(), order.getOrderStatus());
                    priorityBlockingQueue.add(order);
                    Thread.sleep(5000);
                }
                else {
                    logger.info("Waiter {} completed order for table {}", currentThread().getName(), order.getTableNumber());
                    order.setOrderStatus(OrderStatus.COMPLETED);
                    int billSum = 0;
                    for (DishItem dishItem : order.getDishItems()) {
                        billSum+=dishItem.getPrice();
                    }
                    orderRepository.addCompletedOrder(order);
                    cashFlow.addAndGet(billSum);
                }
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted because of {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
