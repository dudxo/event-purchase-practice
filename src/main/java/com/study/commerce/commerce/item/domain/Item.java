package com.study.commerce.commerce.item.domain;

import com.study.commerce.commerce.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	@Column(name = "item_name")
	private String name;

	@Column(name = "item_price")
	private int price;

	@Column(name = "item_quantity")
	private int stockQuantity;

	@Builder(access = AccessLevel.PRIVATE)
	private Item(String name, int price, int stockQuantity) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}

	public static Item of(String name, int price, int stockQuantity) {
		return Item.builder()
			.name(name)
			.price(price)
			.stockQuantity(stockQuantity)
			.build();
	}

	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new RuntimeException("재고가 부족합니다.");
		}

		this.stockQuantity = restStock;
	}
}
