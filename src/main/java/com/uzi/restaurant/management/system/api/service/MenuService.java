package com.uzi.restaurant.management.system.api.service;

import com.uzi.restaurant.management.system.api.model.DishItem;
import com.uzi.restaurant.management.system.api.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    private Logger logger= LoggerFactory.getLogger(MenuService.class);

    public Map<String, List<DishItem>> getMenu() {
        logger.info("Retrieving restaurant menu");
        return menuRepository.getMenu();
    }

    public String addDish(Map<String, List<DishItem>> dish) {
        if (!isDishExists(dish)) {
            logger.info("Adding new dish to menu");
            for (Map.Entry<String, List<DishItem>> entry : dish.entrySet()) {
                for (DishItem currentDish : entry.getValue()) {
                    menuRepository.getMenu().get(entry.getKey()).add(currentDish);
                }
            }
            return "Dish added!";
        }
        return "Duplicate dish was detected!";
    }

    private boolean isDishExists(Map<String, List<DishItem>> dish) {
        logger.info("Validating new dish before adding to menu...");
        for (Map.Entry<String, List<DishItem>> entry : dish.entrySet()) {
            List<DishItem> dishItems = menuRepository.getMenu().get(entry.getKey());
            for (DishItem dishItem : entry.getValue()) {
                if (dishItems.contains(dishItem)) {
                    return true;
                }
            }
        }
        return false;
    }
}
