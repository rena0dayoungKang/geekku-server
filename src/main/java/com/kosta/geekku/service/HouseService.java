package com.kosta.geekku.service;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.util.PageInfo;

public interface HouseService {
	Integer houseWrite(HouseDto houseDto) throws Exception;
	HouseDto houseDetail(Integer houseNum) throws Exception;
	List<HouseDto> houseList(PageInfo pageInfo, String type, String keyword) throws Exception;
	void houseDelete(Integer houseNum) throws Exception;
	
	//집꾸 답변 서비스
	Integer houseAnswerWrite(HouseAnswerDto houseAnswerDto, Integer houseNum) throws Exception;
	List<HouseAnswerDto> houseAnswerList(PageInfo pageInfo, Integer houseNum) throws Exception;
	void houseAnswerDelete(Integer houseAnswerNum, Integer houseNum) throws Exception;
	
	// 마이페이지 - 중개업자 집꾸 답변 리스트
	Slice<HouseAnswerDto> houseAnswerListForMypage(int page, String companyId) throws Exception;
}
