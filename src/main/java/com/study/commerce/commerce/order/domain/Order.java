package com.study.commerce.commerce.order.domain;

import java.util.ArrayList;
import java.util.List;

import com.study.commerce.commerce.common.domain.BaseTimeEntity;
import com.study.commerce.commerce.orderItem.domain.OrderItem;
import com.study.commerce.commerce.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;

	private void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		orderItem.addOrder(this);
	}

	private void addUser(User user) {
		this.user = user;
		user.getOrders().add(this);
	}

	private void addStatus(OrderStatus status) {
		this.status = status;
	}

	public static Order createOrder(User user, OrderItem... orderItems) {
		Order order = new Order();
		order.addStatus(OrderStatus.ORDER);
		order.addUser(user);
		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}

		return order;
	}
}
