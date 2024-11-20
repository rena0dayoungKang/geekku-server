package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Estate;

public interface EstateRepository extends JpaRepository<Estate, Integer> {
	
}
