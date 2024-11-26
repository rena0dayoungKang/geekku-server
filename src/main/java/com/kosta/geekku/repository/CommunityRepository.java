package com.kosta.geekku.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer>, JpaSpecificationExecutor<Community> {
	Page<Community> findAll(Pageable pageable);
	Optional<Community> findByCommunityNum(Integer communityNum);
	List<Community> findAllByUser_UserId(UUID userId);

}