package com.uzi.restaurant.management.system.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uzi.restaurant.management.system.api.helper.MappingHelper;
import com.uzi.restaurant.management.system.api.model.Bill;
import com.uzi.restaurant.management.system.api.model.BillItem;
import com.uzi.restaurant.management.system.api.model.Order;
import com.uzi.restaurant.management.system.api.model.OrderStatus;
import com.uzi.restaurant.management.system.api.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    private Logger logger= LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MappingHelper mappingHelper;
    private AtomicInteger orderPriority = new AtomicInteger(0);

    public Object createOrder(List<Order> orderList) throws JsonProcessingException {
        List<Order> invalidOrders = validateOrder(orderList);
        if (!ObjectUtils.isEmpty(invalidOrders)) {
            logger.info("Started creating {} orders...", orderList.size());

            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Below orders are invalid:\n");
            errorMessage.append(mappingHelper.writeValueAsPretty(invalidOrders));
            return errorMessage;
        }
        orderList.forEach(order -> {
            order.setOrderStatus(OrderStatus.IN_PROGRESS);
            order.setPriority(orderPriority.incrementAndGet());
            orderRepository.addOrderToQueue(order);
        });

        return orderList.size() + " Order(s) were created successfully!";
    }

    private List<Order> validateOrder(List<Order> orderList) {
        logger.info("Started validating incoming {} orders...", orderList.size());
        List<Order> invalidOrders = new ArrayList<>();
        orderList.forEach(order -> {
            if (!OrderStatus.DEFINED.equals(order.getOrderStatus())) {
                invalidOrders.add(order);
            }
        });
        return invalidOrders.size() == 0 ? null : invalidOrders;
    }

    public Bill calculateBill(Order order) {
        logger.info("Starting to produce bill for table {} order", order.getTableNumber());

        Bill bill = new Bill();
        List<BillItem> billItems = new ArrayList<>();
        bill.setTableNumber(order.getTableNumber());

        order.getDishItems().forEach(dishItem -> {
            BillItem billItem = new BillItem();
            billItem.setName(dishItem.getName());
            int currentPrice = dishItem.getPrice();
            billItem.setPrice(currentPrice);
            billItems.add(billItem);
            bill.addToTotalSum(currentPrice);
        });
        bill.setBillItems(billItems);

        return bill;
    }
}
