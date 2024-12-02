package com.kosta.geekku.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.kosta.geekku.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer>, JpaSpecificationExecutor<Community> {
	Page<Community> findAll(Pageable pageable);
	Optional<Community> findByCommunityNum(Integer communityNum);
	List<Community> findAllByUser_UserId(UUID userId);

	Page<Community> findByUser_UserId(UUID userId, Pageable pageable);
	
	@Query("SELECT c FROM Community c JOIN FETCH c.user")
    List<Community> findAllWithUser(Pageable pageable); // username 포함된 모든 커뮤니티 조회


}