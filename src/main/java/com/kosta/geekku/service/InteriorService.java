package com.kosta.geekku.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.entity.InteriorSample;

public interface InteriorService {
	List<InteriorDto> interiorListForMain() throws Exception;
	List<InteriorSample> sampleListForMain() throws Exception;
	List<InteriorDto> interiorList(String possibleLocation) throws Exception;
	Integer checkBookmark(String userId, Integer interiorNum) throws Exception;
	boolean toggleBookmark(String userId, Integer interiorNum) throws Exception;
	Integer interiorRegister(InteriorDto interiorDto) throws Exception;
	InteriorDto interiorCompanyDetail(Integer num) throws Exception;
	Integer sampleRegister(SampleDto sampleDto) throws Exception;
	Integer reviewRegister(ReviewDto reviewDto) throws Exception;
	SampleDto sampleDetail(Integer num) throws Exception;
	Integer interiorRequest(InteriorRequestDto requestDto) throws Exception;
	InteriorRequestDto requestDetail(Integer num) throws Exception;
	
	// 마이페이지 - 개인회원 인테리어 문의 내역
	Page<InteriorRequestDto> interiorRequestListForUserMypage(int page, int size, String userId) throws Exception;
	// 마이페이지 - 개인회원 인테리어 후기 작성 내역
	Page<ReviewDto> reviewListForUserMypage(int page, int size, String userId) throws Exception;
	// 마이페이지 - 개인회원 인테리어 후기 수정
	void updateReview(ReviewDto reviewDto, Integer num) throws Exception;
	// 마이페이지 - 개인회원 인테리어 후기 삭제
	void deleteReview(Integer num) throws Exception;
}
