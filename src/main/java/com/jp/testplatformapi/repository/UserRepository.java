package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository for DB access
public interface UserRepository extends JpaRepository<User, Long> {
}