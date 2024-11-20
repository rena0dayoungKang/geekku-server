package com.kosta.geekku.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;

public interface CommunityService {

    /**
     * 커뮤니티 글 생성
     * @param communityDto 커뮤니티 DTO
     * @param fileList 업로드할 파일 리스트
     * @return 생성된 커뮤니티 글 번호
     * @throws Exception
     */
    Integer createCommunity(CommunityDto communityDto, List<MultipartFile> fileList) throws Exception;

    /**
     * 커뮤니티 글 상세 조회
     * @param communityNum 커뮤니티 글 번호
     * @return 커뮤니티 DTO
     * @throws Exception
     */
    CommunityDto getCommunityDetail(Integer communityNum) throws Exception;

    /**
     * 커뮤니티 글 리스트 조회
     * @return 커뮤니티 DTO 리스트
     */
    List<CommunityDto> getCommunityList();

    /**
     * 커뮤니티 글 삭제
     * @param communityNum 커뮤니티 글 번호
     * @throws Exception
     */
    void deleteCommunity(Integer communityNum) throws Exception;
}
