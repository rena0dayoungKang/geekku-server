package com.kosta.geekku.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.util.PageInfo;

public interface OnestopService {
	Integer onestopWrite(OnestopDto onestopDto, UUID userId) throws Exception;

	OnestopDto onestopDetail(Integer onestopNum) throws Exception;

	// Integer onestopModify(OnestopDto onestopDto) throws Exception;

	List<OnestopDto> onestopList(PageInfo pageInfo, String type, String keyword) throws Exception;

	void onestopDelete(Integer onestopNum) throws Exception;

	Integer onestopAnswerWrite(OnestopAnswerDto onestopAnswerDto, UUID companyId) throws Exception;

	List<OnestopAnswerDto> onestopAnswerList(PageInfo pageInfo, Integer oneStopNum) throws Exception;

	void onestopAnswerDelete(Integer onestopAnswerNum, Integer onestopNum) throws Exception;

	// 개인 마이페이지 - 한번에 꾸하기 신청내역 리스트
	Page<OnestopDto> onestopListForUserMypage(int page, int size, UUID userId) throws Exception;

	// 기업 마이페이지 - 한번에 꾸하기 답변내역 리스트
	Slice<OnestopAnswerDto> onestopAnswerListForMypage(int page, UUID companyId) throws Exception;

	Page<HouseAnswerDto> getAnswersByCompanyId(UUID companyId, Pageable pageable) throws Exception;

}
