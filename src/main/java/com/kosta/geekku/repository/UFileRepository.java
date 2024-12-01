package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.UFile;

public interface UFileRepository extends JpaRepository<UFile, Integer> {
	Optional<UFile> findByCompany(Company company) throws Exception;
}
