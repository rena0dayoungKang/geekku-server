package com.kosta.geekku.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.util.PageInfo;

public interface InteriorService {
	List<InteriorDto> interiorListForMain() throws Exception;

	List<SampleDto> sampleListForMain() throws Exception;

	List<InteriorDto> interiorList(String possibleLocation,PageInfo page, Integer limit) throws Exception;

	Integer checkBookmark(String userId, Integer interiorNum) throws Exception;

	boolean toggleBookmark(String userId, Integer interiorNum) throws Exception;

	Map<Object,Object> interiorRegister(InteriorDto interiorDto, MultipartFile coverImage, UUID companyId) throws Exception;

	InteriorDto interiorCompanyDetail(UUID companyId) throws Exception;

	Integer sampleRegister(SampleDto sampleDto, MultipartFile coverImage, UUID companyId) throws Exception;

	Integer reviewRegister(String userId, ReviewDto reviewDto, List<MultipartFile> fileList) throws Exception;

	SampleDto sampleDetail(Integer num) throws Exception;

	Integer interiorRequest(String userId, InteriorRequestDto requestDto) throws Exception;


	InteriorRequestDto requestDetail(Integer num) throws Exception;

	List<SampleDto> sampleList(String date, String[] type, String[] style, String[] size, String[] location,PageInfo page, Integer limit)
			throws Exception;

	Map<String, Object> interiorDetail(Integer interiorNum) throws Exception;

	// 개인 마이페이지 - 인테리어 문의 내역
	Page<InteriorRequestDto> interiorRequestListForUserMypage(int page, int size, UUID userId) throws Exception;

	// 개인 마이페이지 - 인테리어 문의 내역 삭제
	void deleteRequest(Integer requestNum) throws Exception;

	// 개인 마이페이지 - 인테리어 후기 작성내역
	Page<ReviewDto> reviewListForUserMypage(int page, int size, UUID userId) throws Exception;

	// 개인 마이페이지 - 인테리어 후기 수정
	Integer updateReview(ReviewDto reviewDto, Integer num, List<Integer> delFileNum, List<MultipartFile> fileList)
			throws Exception;

	// 개인 마이페이지 - 인테리어 후기 삭제
	void deleteReview(Integer num) throws Exception;

	Page<ReviewDto> interiorReviewList(int page, int size, UUID companyId) throws Exception;

	Page<InteriorRequestDto> interiorRequestList(int page, int size, UUID companyId) throws Exception;

	List<SampleDto> interiorSampleList(PageInfo pageInfo, UUID companyId) throws Exception;

	ReviewDto getReview(Integer reviewNum) throws Exception;

	Map<String, Object> updateInteriorCompany(UUID companyId, InteriorDto interiorDto, MultipartFile file);

}