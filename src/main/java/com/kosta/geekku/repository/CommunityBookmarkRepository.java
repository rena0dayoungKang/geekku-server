package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.CommunityBookmark;
import com.kosta.geekku.entity.EstateBookmark;

public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark, Integer> {
	// 유저와 커뮤니티 번호를 기준으로 북마크를 찾기
    CommunityBookmark findByUserUserIdAndCommunityCommunityNum(UUID userId, Integer communityNum);

    // 특정 유저의 모든 북마크된 커뮤니티 조회
    List<CommunityBookmark> findByUserUserId(UUID userId);

    // 북마크 삭제 메소드
    void deleteByUserUserIdAndCommunityCommunityNum(UUID userId, Integer communityNum);
    
    Page<CommunityBookmark> findAllByUser_UserId(UUID userId, Pageable pageable);
}
