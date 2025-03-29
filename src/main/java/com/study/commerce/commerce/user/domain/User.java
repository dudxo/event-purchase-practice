package com.study.commerce.commerce.user.domain;

import java.util.ArrayList;
import java.util.List;

import com.study.commerce.commerce.common.domain.BaseTimeEntity;
import com.study.commerce.commerce.order.domain.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_name")
	private String name;

	@Embedded
	@Column(name = "user_address")
	private Address address;

	@Column(name = "user_money")
	private int money;

	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	private User(String name, Address address) {
		this.name = name;
		this.address = address;
		this.money = 0;
	}

	public static User of(String name, Address address) {
		return User.builder()
			.name(name)
			.address(address)
			.build();
	}
}

