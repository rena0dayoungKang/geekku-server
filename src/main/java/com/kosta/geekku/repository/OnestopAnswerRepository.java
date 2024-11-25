package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.OnestopAnswer;

public interface OnestopAnswerRepository extends JpaRepository<OnestopAnswer, Integer> {
	Page<OnestopAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);

}
