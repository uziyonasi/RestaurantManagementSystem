package com.uzi.restaurant.management.system.api.repository;

import com.uzi.restaurant.management.system.api.helper.MappingHelper;
import com.uzi.restaurant.management.system.api.model.DishItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Repository
public class MenuRepository {
    Map<String, List<DishItem>> menuMap = new LinkedHashMap<>();
    @Autowired
    private MappingHelper mappingHelper;

    @PostConstruct
    public void postConstruct() throws IOException {
        File jsonFile = new ClassPathResource("menu.json").getFile();

        menuMap = (Map) mappingHelper.getObjectMapper().readValue(jsonFile, Object.class);
    }

    public Map<String, List<DishItem>> getMenu() {
        return menuMap;
    }

}
