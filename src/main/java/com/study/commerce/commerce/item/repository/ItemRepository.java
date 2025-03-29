package com.study.commerce.commerce.item.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.commerce.commerce.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	Optional<Item> findByName(String name);
}
