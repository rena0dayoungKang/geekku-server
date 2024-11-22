package com.kosta.geekku.service;

import java.util.List;

import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.util.PageInfo;

public interface InteriorService {
	List<InteriorDto> interiorListForMain() throws Exception;

	List<InteriorSample> sampleListForMain() throws Exception;

	InteriorDto interiorCompanyDetail(Integer interiornum) throws Exception;

	List<InteriorDto> interiorList(String possibleLocation) throws Exception;

	Integer checkBookmark(String userId, Integer interiorNum) throws Exception;

	boolean toggleBookmark(String userId, Integer interiorNum) throws Exception;

	Integer interiorRegister(InteriorDto interiorDto) throws Exception;

}
