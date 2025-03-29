package com.study.commerce.commerce.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Column(name = "city")
	private String city;
	@Column(name = "street")
	private String street;
	@Column(name = "zipcode")
	private String zipcode;

}
