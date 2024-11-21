package com.kosta.geekku.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;

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
}
