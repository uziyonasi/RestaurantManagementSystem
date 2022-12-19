package com.uzi.restaurant.management.system.api.controller;

import com.uzi.restaurant.management.system.api.model.DishItem;
import com.uzi.restaurant.management.system.api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Value("#{${categories}}")
    List<String> categories;
    @GetMapping("/getMenu")
    public Object getMenu() {
        return menuService.getMenu();
    }

    @PostMapping("/addDish")
    public String addDish (@RequestBody Map<String, List<DishItem>> dish) {
        for(String key : dish.keySet()) {
            if (!categories.contains(key)) {
                return "Invalid Category " + key + " was provided!";
            }
        }
        return menuService.addDish(dish);
    }
}
