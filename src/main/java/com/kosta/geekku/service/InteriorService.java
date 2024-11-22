package com.kosta.geekku.service;

import java.util.List;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequsetDto;
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
	Integer interiorRequest(InteriorRequsetDto requestDto) throws Exception;
	InteriorRequsetDto requestDetail(Integer num) throws Exception;
}
