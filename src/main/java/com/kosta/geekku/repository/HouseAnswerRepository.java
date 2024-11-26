package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.HouseAnswer;

public interface HouseAnswerRepository extends JpaRepository<HouseAnswer, Integer> {
	Page<HouseAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);
}
