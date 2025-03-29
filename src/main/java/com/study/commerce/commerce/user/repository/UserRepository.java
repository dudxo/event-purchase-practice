package com.study.commerce.commerce.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.commerce.commerce.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
