package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorSample;

public interface InteriorSampleRepository extends JpaRepository<InteriorSample, Integer> {
	List<InteriorSample> findByInterior_InteriorNum(Integer interiorNum);
	Page<InteriorSample> findAll(Pageable pageable);
//	Page<InteriorSample> findByTypeInOrStyleInOrSizeInOrLocationIn(String[] types, String[] styles, String[] sizes, String[] location, Pageable pageable);
    Page<InteriorSample> findByTypeIn(String[] types, Pageable pageable);
    Page<InteriorSample> findByStyleIn(String[] styles, Pageable pageable);
    Page<InteriorSample> findBySizeIn(String[] sizes, Pageable pageable);
    Page<InteriorSample> findByLocationIn(String[] locations, Pageable pageable);
}
