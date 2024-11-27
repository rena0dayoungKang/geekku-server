package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.InteriorAllAnswer;

public interface InteriorAllAnswerRepository extends JpaRepository<InteriorAllAnswer, Integer> {

	Page<InteriorAllAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);

}
