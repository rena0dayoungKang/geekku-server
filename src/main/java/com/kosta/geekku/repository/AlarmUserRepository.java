package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.AlarmUser;

public interface AlarmUserRepository extends JpaRepository<AlarmUser, Integer> {
	List<AlarmUser> findByRecvnameAndConfirmFalse(String username);
}
