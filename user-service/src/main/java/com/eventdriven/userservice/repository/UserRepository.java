package com.eventdriven.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventdriven.commonservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
