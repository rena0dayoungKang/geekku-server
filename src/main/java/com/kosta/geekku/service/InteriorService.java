package com.kosta.geekku.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.util.PageInfo;

public interface InteriorService {
	List<InteriorDto> interiorListForMain() throws Exception;

	List<SampleDto> sampleListForMain() throws Exception;

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

	List<InteriorSample> sampleList(String date, String type, String style, Integer size, String location)
			throws Exception;

	Map<String, Object> interiorDetail(Integer interiorNum) throws Exception;

	// 留덉씠�럹�씠吏� - 媛쒖씤�쉶�썝 �씤�뀒由ъ뼱 臾몄쓽 �궡�뿭
	Page<InteriorRequestDto> interiorRequestListForUserMypage(int page, int size, String userId) throws Exception;

	// 留덉씠�럹�씠吏� - 媛쒖씤�쉶�썝 �씤�뀒由ъ뼱 �썑湲� �옉�꽦 �궡�뿭
	Page<ReviewDto> reviewListForUserMypage(int page, int size, String userId) throws Exception;

	// 留덉씠�럹�씠吏� - 媛쒖씤�쉶�썝 �씤�뀒由ъ뼱 �썑湲� �닔�젙
	void updateReview(ReviewDto reviewDto, Integer num) throws Exception;

	// 留덉씠�럹�씠吏� - 媛쒖씤�쉶�썝 �씤�뀒由ъ뼱 �썑湲� �궘�젣
	void deleteReview(Integer num) throws Exception;

	List<ReviewDto> interiorReviewList(PageInfo pageInfo, String companyId) throws Exception;

	List<InteriorRequestDto> interiorRequestList(PageInfo pageInfo, String companyId) throws Exception;

	List<SampleDto> interiorSampleList(PageInfo pageInfo, String companyId) throws Exception;

}
