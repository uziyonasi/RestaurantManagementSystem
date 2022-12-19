package com.uzi.restaurant.management.system.api.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RestaurantService {
    private AtomicInteger cashFlow = new AtomicInteger(0);

    public AtomicInteger getCashFlow() {
        return cashFlow;
    }

}
