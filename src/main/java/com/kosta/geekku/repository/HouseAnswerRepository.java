package com.kosta.geekku.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.HouseAnswer;

public interface HouseAnswerRepository extends JpaRepository<HouseAnswer, Integer> {
	Page<HouseAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);

	@Query("SELECT h FROM HouseAnswer h WHERE h.company.companyId = :companyId ORDER BY h.createdAt DESC")
    Page<HouseAnswer> findByCompanyIdOrderByCreatedAtDesc(@Param("companyId") UUID companyId, Pageable pageable);
}
	