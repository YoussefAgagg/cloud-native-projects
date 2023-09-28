package com.github.youssefagagg.orderservice.repository;

import com.github.youssefagagg.orderservice.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
}