package com.kosta.geekku.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.User;

public interface CommunityService {
    // 커뮤니티 리스트 조회 + 페이징
    Page<CommunityDto> getCommunityList(Pageable pageable);
    // 커뮤니티 글 작성
    Integer createCommunity(CommunityDto communityDto);
    // 커뮤니티 글 상세 조회
    CommunityDto getCommunityDetail(Integer communityNum);
    // 필터링 + 페이징 기능된 리스트 조회
    Page<CommunityDto> getFilteredCommunityList(CommunityFilterDto filterDto, Pageable pageable);
    
    // 커뮤니티 글 작성(임시)
    void createCommunityWithCoverImage(String title, String content, String type, MultipartFile coverImage) throws Exception;
   
    // 커뮤니티 글 수정
    void updateCommunity(Integer id, CommunityDto communityDto, MultipartFile coverImage) throws Exception;
    
    // 커뮤니티 북마크 등록
    boolean toggleCommunityBookmark(String userId, Integer communityNum) throws Exception;
    
    // 커뮤니티 댓글 작성
    void createComment(Integer communityId, String userId, String content) throws Exception;
    // 코뮤니티 댓글 삭제
    void deleteComment(Integer commentId) throws Exception;
    
    User getUserProfile(String user) throws Exception;
    
    List<Community> getUserCommunities(String userId) throws Exception;
    
    // 메인화면 - 집들이 조회수 순 3개
    List<CommunityDto> getCommunityListForMain() throws Exception;
}
