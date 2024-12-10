package com.kosta.geekku.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosta.geekku.entity.Interior;

public interface InteriorRepository extends JpaRepository<Interior, Integer> {

	Interior findByCompany_companyId(UUID findCompany);

	Optional<Interior> findNumByCompany_companyId(UUID companyId);

	UUID findCompany_CompanyIdByInteriorNum(Integer interiorNum); //추가
	
}
