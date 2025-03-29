package com.study.commerce.commerce.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.commerce.commerce.common.fixture.UserFixture;
import com.study.commerce.commerce.item.domain.Item;
import com.study.commerce.commerce.item.repository.ItemRepository;
import com.study.commerce.commerce.order.domain.Order;
import com.study.commerce.commerce.order.repository.OrderRepository;
import com.study.commerce.commerce.order.service.OrderService;
import com.study.commerce.commerce.orderItem.repository.OrderItemRepository;
import com.study.commerce.commerce.user.domain.Address;
import com.study.commerce.commerce.user.domain.User;
import com.study.commerce.commerce.user.repository.UserRepository;

@SpringBootTest
public class OrderServiceTest {

	@Autowired
	OrderService orderService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	OrderItemRepository orderItemRepository;

	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
		itemRepository.deleteAllInBatch();
	}

	@Test
	@Transactional
	void 일반주문을_진행한다() {
	    // given
		User savedUser = userRepository.save(User.of("test", new Address("경기", "고양대로", "1234-567")));
		Item savedItem = itemRepository.save(Item.of("치킨", 10_000, 10));

		// when
		Order order = orderService.order(savedUser.getId(), 10);

		User findUser = userRepository.findById(savedUser.getId()).get();
		Item findItem = itemRepository.findById(savedItem.getId()).get();
		// then
		assertAll(
			() -> assertThat(order.getUser().getName()).isEqualTo(savedUser.getName()),
			() -> assertThat(order).isEqualTo(findUser.getOrders().get(0)),
			() -> assertThat(findItem.getStockQuantity()).isZero()
		);
	}

	@Test
	// @Transactional
	void 수량을_초과한_주문() throws InterruptedException {
		// given
		User test1 = UserFixture.of("test1");
		User test2 = UserFixture.of("test2");
		User test3 = UserFixture.of("test3");
		User test4 = UserFixture.of("test4");
		User test5 = UserFixture.of("test5");
		User test6 = UserFixture.of("test6");
		User test7 = UserFixture.of("test7");
		User test8 = UserFixture.of("test8");
		userRepository.saveAll(List.of(test1, test2, test3, test4, test5, test6, test7, test8));
		Item savedItem = itemRepository.save(Item.of("치킨", 10_000, 5));

		int threadCount = 8;
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(threadCount);

		List<User> findUsers = userRepository.findAll();

		// when
		for (int i = 0; i < threadCount; i++) {
			int index = i;
			executorService.submit(() -> {
				try {
					orderService.order(findUsers.get(index).getId(), 1);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

		List<Order> findOrders = orderRepository.findAll();
		Item findItem = itemRepository.findById(savedItem.getId()).get();
		// then
		assertThat(findOrders.size()).isEqualTo(5);
	}


}
