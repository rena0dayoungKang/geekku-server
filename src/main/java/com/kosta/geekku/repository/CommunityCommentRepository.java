package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.entity.CommunityComment;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {
	 List<CommunityComment> findByCommunityCommunityNum(Integer communityNum);  // communityNum에 해당하는 댓글 조회
}
