package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.CommunityComment;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {

}
