package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.User;

public interface HouseRepository extends JpaRepository<House, Integer> {
	Page<House> findAllByUser(Optional<User> user, Pageable pageable);
}
