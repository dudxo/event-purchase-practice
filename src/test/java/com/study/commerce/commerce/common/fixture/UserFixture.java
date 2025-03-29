package com.study.commerce.commerce.common.fixture;

import com.study.commerce.commerce.user.domain.Address;
import com.study.commerce.commerce.user.domain.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFixture {

	public static User of(String name) {
		return User.of(name, new Address("경기", "고양대로", "1234-567"));
	}
}
