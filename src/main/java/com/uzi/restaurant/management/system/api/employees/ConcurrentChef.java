package com.uzi.restaurant.management.system.api.employees;

import com.uzi.restaurant.management.system.api.model.DishItem;
import com.uzi.restaurant.management.system.api.model.Order;
import com.uzi.restaurant.management.system.api.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.PriorityBlockingQueue;

public class ConcurrentChef extends Thread {
    private Logger logger= LoggerFactory.getLogger(ConcurrentChef.class);
    private PriorityBlockingQueue<Order> priorityBlockingQueue;

    public ConcurrentChef(PriorityBlockingQueue<Order> priorityBlockingQueue) {
        this.priorityBlockingQueue = priorityBlockingQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = priorityBlockingQueue.take();
                if (order.getOrderStatus() != OrderStatus.IN_PROGRESS) {
                    logger.info("Chef {} took order for table {} and current status is {}", currentThread().getName(), order.getTableNumber(), order.getOrderStatus());
                    priorityBlockingQueue.add(order);
                    Thread.sleep(5000);
                }
                else {
                    logger.info("Chef {} started working on order for table {}", currentThread().getName(), order.getTableNumber());
                    Duration totalPreparationTime = Duration.ZERO;

                    for (DishItem dishItem : order.getDishItems()) {
                        totalPreparationTime = totalPreparationTime.plus(dishItem.getPreparationTime());
                    }

                    Thread.sleep(totalPreparationTime.toMillis());

                    order.setOrderStatus(OrderStatus.READY);
                    priorityBlockingQueue.add(order);
                    logger.info("Chef {} finished order for table {} after {} ms", currentThread().getName(), order.getTableNumber(), totalPreparationTime.toMillis());
                }
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted because of {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
