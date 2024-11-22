package com.kosta.geekku.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorBookmark;

public interface InteriorBookmarkRepository extends JpaRepository<InteriorBookmark, Integer> {
	InteriorBookmark findByInteriorNumAndUserId(Integer interiorNum, UUID userId);
}
