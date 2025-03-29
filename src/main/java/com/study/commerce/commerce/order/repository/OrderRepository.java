package com.study.commerce.commerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.commerce.commerce.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	boolean existsByUserId(Long userId);
}
