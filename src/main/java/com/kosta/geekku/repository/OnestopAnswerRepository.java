package com.kosta.geekku.repository;

import java.util.UUID;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.OnestopAnswer;

public interface OnestopAnswerRepository extends JpaRepository<OnestopAnswer, Integer> {
	Page<OnestopAnswer> findAllByCompany(Optional<Company> company, Pageable pageable);

    @Query("SELECT o FROM OnestopAnswer o WHERE o.company.companyId = :companyId ORDER BY o.createdAt DESC")
    Page<OnestopAnswer> findByCompanyIdOrderByCreatedAtDesc(@Param("companyId") UUID companyId, Pageable pageable);
}
