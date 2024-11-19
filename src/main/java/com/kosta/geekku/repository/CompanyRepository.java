package com.kosta.geekku.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

}