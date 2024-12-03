package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Auth;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
	
	Optional<Auth> findByEmailAndCertificationNum(String email, int certificationNum);
	Optional<Auth> findByEmail(String email);
	Optional<Auth> findByCertificationNum(int certificationNum);

}
