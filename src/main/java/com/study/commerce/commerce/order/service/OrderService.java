package com.study.commerce.commerce.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.commerce.commerce.item.domain.Item;
import com.study.commerce.commerce.item.repository.ItemRepository;
import com.study.commerce.commerce.order.domain.Order;
import com.study.commerce.commerce.order.repository.OrderRepository;
import com.study.commerce.commerce.orderItem.domain.OrderItem;
import com.study.commerce.commerce.user.domain.User;
import com.study.commerce.commerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public Order order(Long userId, int count) {
		User findUser = userRepository.findById(userId).get();

		if (orderRepository.existsByUserId(userId))
			throw new RuntimeException("중복 구매는 불가능합니다.");

		Item findItem = itemRepository.findByName("치킨").get();
		OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

		return orderRepository.save(Order.createOrder(findUser, orderItem));
	}

}
