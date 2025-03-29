package com.study.commerce.commerce.event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.study.commerce.commerce.common.fixture.UserFixture;
import com.study.commerce.commerce.common.util.RedisUtil;
import com.study.commerce.commerce.event.schedular.EventScheduler;
import com.study.commerce.commerce.item.domain.Item;
import com.study.commerce.commerce.item.repository.ItemRepository;
import com.study.commerce.commerce.order.repository.OrderRepository;
import com.study.commerce.commerce.order.service.OrderService;
import com.study.commerce.commerce.orderItem.repository.OrderItemRepository;
import com.study.commerce.commerce.user.domain.User;
import com.study.commerce.commerce.user.repository.UserRepository;

@SpringBootTest
@EnableScheduling
public class EventProcessorTest {

	@Autowired
	EventScheduler eventScheduler;
	@Autowired
	EventProcessor processor;
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
	@Autowired
	RedisUtil redisUtil;

	static final int limitCount = 70;


	// @AfterEach
	// void tearDown() {
	// 	orderItemRepository.deleteAllInBatch();
	// 	orderRepository.deleteAllInBatch();
	// 	userRepository.deleteAllInBatch();
	// 	redisUtil.clear();
	// }

	@BeforeEach
	void beforeEach() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
		itemRepository.deleteAllInBatch();
		redisUtil.clear();
		itemRepository.save(Item.of("치킨", 10_000, limitCount)); // 아이템 저장 추가
	}


	@Test
	void 선착순이벤트_100명에게_70개_판매() throws InterruptedException {
		final Event chickenEvent = Event.CHICKEN;
		final int people = 100;

		// addQueue가 끝나는 것을 보장하기 위한 CountDownLatch
		final CountDownLatch countDownLatch = new CountDownLatch(people);

		List<User> users = new ArrayList<>();

		for(int i = 0; i < people; i++) {
			users.add(UserFixture.of("test" + i));
		}
		userRepository.saveAll(users);
		Collections.shuffle(users);

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		List<Callable<Void>> tasks = users.stream()
			.map(user -> (Callable<Void>) () -> {
				processor.addQueue(chickenEvent, user.getId());
				countDownLatch.countDown(); // addQueue 실행이 완료될 때마다 countDown()
				return null;
			})
			.collect(Collectors.toList());

		executorService.invokeAll(tasks);
		executorService.shutdown();

		// 2️⃣ addQueue가 모두 실행될 때까지 대기
		countDownLatch.await();

		// 3️⃣ 모든 addQueue 실행이 완료된 후 스케줄러 실행
		Thread.sleep(1000); // 약간의 딜레이 추가
		eventScheduler.blackFridayEventScheduler();

		Thread.sleep(10000); // 스케줄러 작업 시간

		final long failEventPeople = redisUtil.getSizeForKey(chickenEvent.toString());
		assertEquals(people - limitCount, failEventPeople);
	}


	// List<Thread> workers = Stream
	// 	.generate(() -> new Thread(new AddQueueWorker(countDownLatch, chickenEvent, users)))
	// 	.limit(people)
	// 	.collect(Collectors.toList());
	// workers.forEach(Thread::start);
	// countDownLatch.await();

	private class AddQueueWorker implements Runnable {
		private CountDownLatch countDownLatch;
		private Event event;
		private List<User> users;

		public AddQueueWorker(CountDownLatch countDownLatch, Event event, List<User> users) {
			this.countDownLatch = countDownLatch;
			this.event = event;
			this.users = users;
		}

		@Override
		public void run() {
			User user = users.remove(0); // 각 쓰레드가 서로 다른 유저를 사용하도록 함
			processor.addQueue(event, user.getId());
			countDownLatch.countDown();
		}
	}
}
