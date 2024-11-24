package com.kosta.geekku.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.EstateBookmark;

public interface EstateBookmarkRepository extends JpaRepository<EstateBookmark, Integer> {
	EstateBookmark findByEstateNumAndUserId(Integer estateNum, UUID userId);
	EstateBookmark findByEstateNum(Integer estateNum);
	EstateBookmark findByUserId(UUID userId);
}
