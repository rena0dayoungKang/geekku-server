package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorReview;

public interface InteriorReviewRepository extends JpaRepository<InteriorReview, Integer> {
	List<InteriorReview> findByInterior_interiorNum(Integer interiorNum);
}
