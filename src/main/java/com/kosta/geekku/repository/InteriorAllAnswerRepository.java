package com.kosta.geekku.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.InteriorAllAnswer;

public interface InteriorAllAnswerRepository extends JpaRepository<InteriorAllAnswer, Integer> {

	Page<InteriorAllAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);

	@Query("SELECT o FROM InteriorAllAnswer o WHERE o.company.companyId = :companyId ORDER BY o.createdAt DESC")
	Page<InteriorAllAnswer> findByCompanyIdOrderByCreatedAtDesc(@Param("companyId") UUID companyId, Pageable pageable);

}
