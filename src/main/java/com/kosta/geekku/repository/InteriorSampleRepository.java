package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.InteriorSample;

public interface InteriorSampleRepository extends JpaRepository<InteriorSample, Integer> {
	List<InteriorSample> findByInteriorNum(Integer interiorNum);
}
