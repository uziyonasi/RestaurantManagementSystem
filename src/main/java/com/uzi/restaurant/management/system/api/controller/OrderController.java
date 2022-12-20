package com.uzi.restaurant.management.system.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uzi.restaurant.management.system.api.employees.ConcurrentChef;
import com.uzi.restaurant.management.system.api.employees.ConcurrentWaiter;
import com.uzi.restaurant.management.system.api.model.Order;
import com.uzi.restaurant.management.system.api.repository.OrderRepository;
import com.uzi.restaurant.management.system.api.service.OrderService;
import com.uzi.restaurant.management.system.api.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger= LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantService restaurantService;
    @Value("#{${numberOfWaiters}}")
    private Integer numberOfWaiters;
    @Value("#{${numberOfChefs}}")
    private Integer numberOfChefs;

    @PostConstruct
    public void postConstruct() {
        for (int i = 0; i < numberOfWaiters; i++) {
            ConcurrentWaiter waiter = new ConcurrentWaiter(orderRepository.getPriorityBlockingQueue(), orderRepository, restaurantService.getCashFlow());
            waiter.start();
        }
        for (int i = 0; i < numberOfChefs; i++) {
            ConcurrentChef chef = new ConcurrentChef(orderRepository.getPriorityBlockingQueue());
            chef.start();
        }
    }

    @GetMapping("/getBill")
    public Object getBill(@RequestParam int tableNumber) {
        Order order = orderRepository.getCompletedOrders().get(tableNumber);
        if (ObjectUtils.isEmpty(order)) {
            return "Order for table number " + tableNumber + " could not be found.";
        }
        return orderService.calculateBill(order);
    }

    @PostMapping("/createOrder")
    public Object createOrder(@RequestBody  List<Order> orderList) throws JsonProcessingException {
        return orderService.createOrder(orderList);
    }

    @GetMapping("/getCompletedOrders")
    public Object getCompletedOrders() {
        logger.info("Retrieving all currently completed orders...");
        return orderRepository.getCompletedOrders();
    }
}
