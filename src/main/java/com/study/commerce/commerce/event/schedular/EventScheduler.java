package com.study.commerce.commerce.event.schedular;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.commerce.commerce.event.Event;
import com.study.commerce.commerce.event.EventProcessor;
import com.study.commerce.commerce.item.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

	private final EventProcessor processor;
	private final ItemService itemService;

	@Scheduled(fixedDelay = 1000)
	public void blackFridayEventScheduler() {
		if (!itemService.validStock()) {
			log.info("====== 선착순 구매 이벤트 종료 ======");
			return;
		}
		processor.run(Event.CHICKEN);
	}
}
