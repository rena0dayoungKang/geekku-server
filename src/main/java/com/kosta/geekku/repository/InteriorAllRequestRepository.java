package com.kosta.geekku.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.entity.User;

public interface InteriorAllRequestRepository extends JpaRepository<InteriorAllRequest, Integer> {
	Page<InteriorAllRequest> findAllByUser(Optional<User> user, Pageable pageable);
}
