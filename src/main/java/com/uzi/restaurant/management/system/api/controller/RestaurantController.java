package com.uzi.restaurant.management.system.api.controller;

import com.uzi.restaurant.management.system.api.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/getCashFlow")
    public String getCashFlow() {
        return "Current restaurant cash flow is: " + restaurantService.getCashFlow();
    }
}
