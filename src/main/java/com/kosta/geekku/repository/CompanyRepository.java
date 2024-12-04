package com.kosta.geekku.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
	Optional<Company> findByUsername(String username);
	
	Company findByCompanyNameContaining(String companyName);
	
	Optional<Company> findByEmail(String email);
	
	List<Company> findAllByEmail(String email);
	
	List<Company> findAllByPhone(String phone);
	
}
