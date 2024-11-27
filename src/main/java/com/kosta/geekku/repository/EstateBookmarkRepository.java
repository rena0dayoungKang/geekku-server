package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.EstateBookmark;

public interface EstateBookmarkRepository extends JpaRepository<EstateBookmark, Integer> {
	EstateBookmark findByEstateNumAndUserId(Integer estateNum, UUID userId);

	EstateBookmark findByEstateNum(Integer estateNum);

	EstateBookmark findByUserId(UUID userId);

	List<EstateBookmark> findAllByUserId(UUID userId);


	Page<EstateBookmark> findAllByUserId(UUID userId, Pageable pageable);
}
