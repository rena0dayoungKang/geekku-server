package com.kosta.geekku.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorReview;
import com.kosta.geekku.entity.User;

public interface InteriorReviewRepository extends JpaRepository<InteriorReview, Integer> {
	List<InteriorReview> findByInterior_interiorNum(Integer interiorNum);
//	List<InteriorReview> findByCompanyNameContaining(String companyName);

	Page<InteriorReview> findAllByUser(Optional<User> user, Pageable pageable);

	Page<InteriorReview> findAllByInterior_interiorNum(Optional<Interior> interior, Pageable pageable);

	Page<InteriorReview> findAllByInterior(Optional<Interior> interiorNum, Pageable pageable);
}
