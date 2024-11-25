package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Estate;

public interface EstateRepository extends JpaRepository<Estate, Integer> {

	List<Estate> findByCompany_CompanyId(UUID companyId);

}
