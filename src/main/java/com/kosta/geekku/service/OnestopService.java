package com.kosta.geekku.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.util.PageInfo;

public interface OnestopService {
	Integer onestopWrite(OnestopDto onestopDto) throws Exception;

	OnestopDto onestopDetail(Integer onestopnum) throws Exception;

	// Integer onestopModify(OnestopDto onestopDto) throws Exception;

	List<OnestopDto> onestopList(PageInfo pageInfo, String type, String word) throws Exception;

	void onestopDelete(Integer onestopnum) throws Exception;

	Integer onestopAnswerWrite(OnestopAnswerDto onestopAnswerDto, Integer onestopNum) throws Exception;

	List<OnestopAnswerDto> houseAnswerList(PageInfo pageInfo, Integer onestopNum) throws Exception;

	void onestopAnswerDelete(Integer onestopAnswerNum, Integer onestopNum) throws Exception;
	
	// 개인 마이페이지 - 한번에 꾸하기 신청내역 리스트
	Page<OnestopDto> onestopListForUserMypage(int page, int size, String userId) throws Exception;
}
