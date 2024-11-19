package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.House;

public interface HouseRepository extends JpaRepository<House, Integer> {

}
