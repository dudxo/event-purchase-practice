package com.study.commerce.commerce.item.service;

import org.springframework.stereotype.Service;

import com.study.commerce.commerce.item.domain.Item;
import com.study.commerce.commerce.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	public boolean validStock() {
		Item findItem = itemRepository.findByName("치킨")
			.orElseThrow(() -> new RuntimeException("해당 물품을 찾을 수 없습니다."));

		if (findItem.getStockQuantity() > 0) {
			log.info("남은 재고 수량 = {}", findItem.getStockQuantity());
			return true;
		}

		return false;
	}
}
