package com.kosta.geekku.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.util.PageInfo;

public interface InteriorAllRequestService {

	Integer interiorAllWrite(InteriorAllDto interiorAllDto) throws Exception;

	InteriorAllDto interiorDetail(Integer num) throws Exception;

	Integer interiorAllModify(InteriorAllDto interiorAllDto) throws Exception;

	List<InteriorAllDto> interiorAllList(PageInfo page, String type, String word) throws Exception;

	void interiorAllDelete(Integer num) throws Exception;

	Integer interiorAnswerWrite(InteriorAnswerDto interiorAnswerDto, Integer requestAllNum) throws Exception;

	List<InteriorAnswerDto> interiorAnswerList(PageInfo pageInfo, Integer requestAllNum) throws Exception;

	void interiorAnswerDelete(Integer answerAllNum, Integer requestAllNum) throws Exception;
	
	// 마이페이지 - 개인회원 방꾸 작성 내역
	Page<InteriorAllDto> interiorAllListForUserMypage(int page, int size, String userId) throws Exception;
}
