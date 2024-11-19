package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.CommunityBookmark;

public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark, Integer> {

}
