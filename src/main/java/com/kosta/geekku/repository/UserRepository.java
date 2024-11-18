package com.kosta.geekku.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	

	

}
