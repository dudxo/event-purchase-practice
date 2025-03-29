package com.study.commerce.commerce.orderItem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.commerce.commerce.orderItem.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
