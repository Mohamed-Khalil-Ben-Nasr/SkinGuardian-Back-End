package com.skinguardian.backend.skinguardian_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skinguardian.backend.skinguardian_backend.entities.User;

public interface UserRepository extends JpaRepository<User,UUID>{
	List<User> findByName(String name);
}
