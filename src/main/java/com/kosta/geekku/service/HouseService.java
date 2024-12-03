package com.kosta.geekku.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.util.PageInfo;

public interface HouseService {
	Integer houseWrite(HouseDto houseDto, UUID userId) throws Exception;

	HouseDto houseDetail(Integer houseNum) throws Exception;

	List<HouseDto> houseList(PageInfo pageInfo, String type, String keyword) throws Exception;

	void houseDelete(Integer houseNum) throws Exception;

	// 집꾸 답변 서비스
	Integer houseAnswerWrite(HouseAnswerDto houseAnswerDto, UUID companyId) throws Exception;

	List<HouseAnswerDto> houseAnswerList(PageInfo pageInfo, Integer houseNum) throws Exception;

	void houseAnswerDelete(Integer houseAnswerNum, Integer houseNum) throws Exception;

	// 마이페이지 - 중개업자 집꾸 답변 리스트
	Page<HouseAnswerDto> houseAnswerListForMypage(int page, int size, UUID companyId) throws Exception;

	// 마이페이지 - 개인회원 집꾸 작성 내역
	Page<HouseDto> houseListForUserMypage(int page, int size, UUID userId) throws Exception;
}
