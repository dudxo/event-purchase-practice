package com.study.commerce.commerce.orderItem.domain;

import com.study.commerce.commerce.common.domain.BaseTimeEntity;
import com.study.commerce.commerce.item.domain.Item;
import com.study.commerce.commerce.order.domain.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice;
	private int count;

	public void addOrder(Order order) {
		this.order = order;
	}

	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.addItem(item);
		orderItem.addOrderPrice(orderPrice);
		orderItem.addCount(count);
		item.removeStock(count);

		return orderItem;
	}

	private void addCount(int count) {
		this.count = count;
	}

	private void addOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	private void addItem(Item item) {
		this.item = item;
	}
}
