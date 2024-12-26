package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.User;

public interface OnestopRepository extends JpaRepository<Onestop, Integer> {
	Page<Onestop> findAllByUser(Optional<User> user, Pageable pageable) throws Exception;

	Onestop findByOnestopNum(Integer onestopNum);
}
