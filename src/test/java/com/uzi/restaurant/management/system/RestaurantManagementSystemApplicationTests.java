package com.uzi.restaurant.management.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uzi.restaurant.management.system.api.RestaurantManagementSystemApplication;
import com.uzi.restaurant.management.system.api.controller.MenuController;
import com.uzi.restaurant.management.system.api.controller.OrderController;
import com.uzi.restaurant.management.system.api.controller.RestaurantController;
import com.uzi.restaurant.management.system.api.employees.ConcurrentChef;
import com.uzi.restaurant.management.system.api.employees.ConcurrentWaiter;
import com.uzi.restaurant.management.system.api.helper.MappingHelper;
import com.uzi.restaurant.management.system.api.model.Bill;
import com.uzi.restaurant.management.system.api.model.DishItem;
import com.uzi.restaurant.management.system.api.model.Order;
import com.uzi.restaurant.management.system.api.model.OrderStatus;
import com.uzi.restaurant.management.system.api.repository.MenuRepository;
import com.uzi.restaurant.management.system.api.repository.OrderRepository;
import com.uzi.restaurant.management.system.api.service.MenuService;
import com.uzi.restaurant.management.system.api.service.OrderService;
import com.uzi.restaurant.management.system.api.service.RestaurantService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {RestaurantManagementSystemApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantManagementSystemApplicationTests {
    @Autowired
    @InjectMocks
    private MenuController menuController;
    @Autowired
    @InjectMocks
    private MenuService menuService;
    @Autowired
    @InjectMocks
    private MenuRepository menuRepository;
    @Autowired
    @InjectMocks
    private RestaurantController restaurantController;
    @Autowired
    @InjectMocks
    private RestaurantService restaurantService;
    @Autowired
    @InjectMocks
    private OrderController orderController;
    @Autowired
    @InjectMocks
    private OrderService orderService;
    @Autowired
    @InjectMocks
    private OrderRepository orderRepository;
    @Autowired
    @InjectMocks
    private MappingHelper mappingHelper;

    @Test
    @org.junit.jupiter.api.Order(0)
    void main() {
        RestaurantManagementSystemApplication.main(new String[] {});
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void contextLoads() {
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void getMenu() {
        assertNotNull(menuController.getMenu(), "menu should not be null");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void addValidDish() {
        Map<String, List<DishItem>> dish = createDish("Starters");
        assertEquals("Dish added!", menuController.addDish(dish));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void addInvalidDish() {
        Map<String, List<DishItem>> dish = createDish("abcd");
        assertEquals("Invalid Category abcd was provided!", menuController.addDish(dish));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void addDuplicatedDish() {
        Map<String, List<DishItem>> dish = createDish("Starters");
        menuController.addDish(dish);
        assertEquals("Duplicate dish was detected!", menuController.addDish(dish));

    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void verifyCashFlow() {
        assertEquals("Current restaurant cash flow is: 0", restaurantController.getCashFlow());
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void validateGetCompletedOrders() {
        assertNotNull(orderController.getCompletedOrders(), "Completed order should not be null");
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void createValidOrder() throws JsonProcessingException {
        List<Order> orderList = createOrder(OrderStatus.DEFINED);

        assertEquals("1 Order(s) were created successfully!", orderController.createOrder(orderList));
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void createInvalidOrder() throws JsonProcessingException {
        List<Order> orderList = createOrder(OrderStatus.IN_PROGRESS);
        assertNotNull(orderController.createOrder(orderList));
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    void createMissingBill() {
        assertEquals("Order for table number 1 could not be found.", orderController.getBill(1));
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    void createValidBill() throws InterruptedException, JsonProcessingException {
        List<Order> orderList = createOrder(OrderStatus.DEFINED);
        orderController.createOrder(orderList);

        Thread.sleep(10000);

        Bill bill = (Bill) orderController.getBill(1);

        assertEquals(1, bill.getTableNumber());
        assertEquals(25, bill.getTotalSum());
        assertEquals("Hummus", bill.getBillItems().get(0).getName());
        assertEquals(25, bill.getBillItems().get(0).getPrice());
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    void testDishItemsEqual() {
        Map<String, List<DishItem>> dish1 = createDish("Starters");
        Map<String, List<DishItem>> dish2 = createDish("Starters");
        assertEquals(dish1, dish2);
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    void testHashCode() {
        Map<String, List<DishItem>> dish1 = createDish("Starters");
        Map<String, List<DishItem>> dish2 = createDish("Starters");
        assertEquals(dish1.hashCode(), dish2.hashCode());
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    void testOrderCompareTo() {
        List<Order> orderList1 = createOrder(OrderStatus.DEFINED);
        List<Order> orderList2 = createOrder(OrderStatus.DEFINED);

        orderList1.get(0).setPriority(1);
        orderList2.get(0).setPriority(2);

        assertTrue(true, String.valueOf(orderList1.get(0).compareTo(orderList2.get(0)) == 1));
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    void testOrderCompareToEquals() {
        List<Order> orderList1 = createOrder(OrderStatus.DEFINED);
        List<Order> orderList2 = createOrder(OrderStatus.DEFINED);

        orderList1.get(0).setPriority(1);
        orderList2.get(0).setPriority(1);

        assertTrue(true, String.valueOf(orderList1.get(0).compareTo(orderList2.get(0)) == 1));
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    void testOrderCompareToSame() {
        DishItem dishItem = new DishItem();
        assertTrue(true, String.valueOf(dishItem.equals(dishItem)));
    }

    @Test
    @org.junit.jupiter.api.Order(17)
    void testGetObjectMapper() {
        assertNotNull(mappingHelper.getObjectMapper());
    }

    @Test
    @org.junit.jupiter.api.Order(18)
    void testWriteObjectAsString() throws JsonProcessingException {
        List<Order> orderList = createOrder(OrderStatus.DEFINED);
        assertNotNull(mappingHelper.writeValueAsPretty(orderList));
    }

    @Test
    @org.junit.jupiter.api.Order(19)
    void testConcurrentChefInterruptedException() {
       ConcurrentChef concurrentChef = new ConcurrentChef(orderRepository.getPriorityBlockingQueue());
        Thread thread = new Thread(concurrentChef::run);
        thread.start();
        thread.interrupt();
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    void testConcurrentWaiterInterruptedException() {
        ConcurrentWaiter concurrentWaiter = new ConcurrentWaiter(orderRepository.getPriorityBlockingQueue(),
                orderRepository, restaurantService.getCashFlow());
        Thread thread = new Thread(concurrentWaiter::run);
        thread.start();
        thread.interrupt();
    }

    private static Map<String, List<DishItem>> createDish(String category) {
        Map<String, List<DishItem>> dish = new HashMap<>();
        List<DishItem> dishItems = new ArrayList<>();
        DishItem dishItem = new DishItem();

        dishItem.setName("Hummus");
        dishItem.setPrice(15);
        dishItem.setPreparationTime(Duration.ZERO);
        dishItems.add(dishItem);
        dish.put(category, dishItems);
        return dish;
    }
    private static List<Order> createOrder(OrderStatus orderStatus) {
        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setTableNumber(1);
        order.setOrderStatus(orderStatus);
        order.setSpecialComments("Make it tasty");

        List<DishItem> dishItems = new ArrayList<>();
        DishItem dishItem = new DishItem();
        dishItem.setName("Hummus");
        dishItem.setPrice(25);
        dishItem.setPreparationTime(Duration.ZERO);

        dishItems.add(dishItem);
        order.setDishItems(dishItems);
        orderList.add(order);
        return orderList;
    }

}
