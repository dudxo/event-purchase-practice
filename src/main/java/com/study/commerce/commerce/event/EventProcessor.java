package com.study.commerce.commerce.event;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.study.commerce.commerce.common.util.RedisUtil;
import com.study.commerce.commerce.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProcessor {

	private final OrderService orderService;
	private final RedisUtil redisUtil;
	private static final Long START = 0L;
	private static final Long PUBLISH_SIZE = 9L;

	private void publish(Event event) {
		Set<Object> queue = redisUtil.getAllForKeyOfRange(event.toString(), START, PUBLISH_SIZE);

		if (queue == null || queue.isEmpty()) {
			log.info("발급 대상이 되는 사용자가 없습니다.");
			return;
		}

		for (Object people : queue) {
			orderService.order(Long.parseLong(people.toString()), 1);
			log.info("'{}'님의 상품 구매가 완료되었습니다.", people);
			redisUtil.remove(event.toString(), people);
		}
	}

	private void getOrder(Event event) {
		Long lastSize = redisUtil.getSizeForKey(event.toString());
		log.info("현재 '{}' 이벤트 대기열에는 {}명이 남아있습니다.", event.toString(), lastSize);
		Set<Object> queue = redisUtil.getAllForKeyOfRange(event.toString(), START, lastSize);

		for (Object people : queue) {
			Long nowRank = redisUtil.getRankForData(event.toString(), people);
			log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, nowRank);
		}
	}

	public void addQueue(Event event, Long userId) {
		final long now = System.currentTimeMillis();

		redisUtil.setValues(event.toString(), userId.toString(), (int)now);
		log.info("대기열에 추가 - {} ({}초)", userId, now);
	}

	public void run(Event event) {
		this.publish(event);
		this.getOrder(event);
	}
}
