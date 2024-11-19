package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

}
