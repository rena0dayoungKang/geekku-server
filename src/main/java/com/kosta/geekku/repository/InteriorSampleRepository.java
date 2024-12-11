package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorSample;

public interface InteriorSampleRepository extends JpaRepository<InteriorSample, Integer> {
	List<InteriorSample> findByInterior_InteriorNum(Integer interiorNum);
	Page<InteriorSample> findAll(Pageable pageable);
	Page<InteriorSample> findByTypeInOrStyleInOrSizeInOrLocationIn(List<String> types, List<String> styles, List<String> sizes, List<String> locs, Pageable pageable);
}
