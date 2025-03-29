package com.study.commerce.commerce.orderItem.service;

import org.springframework.stereotype.Service;

import com.study.commerce.commerce.orderItem.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;
}
