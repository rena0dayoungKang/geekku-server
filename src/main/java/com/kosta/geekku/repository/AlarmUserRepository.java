package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.AlarmUser;

public interface AlarmUserRepository extends JpaRepository<AlarmUser, Integer> {

	List<AlarmUser> findByUser_UserIdAndStatusFalse(UUID userId);
	List<AlarmUser> findByCompany_CompanyIdAndStatusFalse(UUID companyId);
}

