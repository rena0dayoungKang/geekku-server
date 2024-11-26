package com.kosta.geekku.service;

import java.util.List;

import org.springframework.data.domain.Slice;

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

	List<OnestopAnswerDto> onestopAnswerList(PageInfo pageInfo, Integer onestopNum) throws Exception;

	void onestopAnswerDelete(Integer onestopAnswerNum, Integer onestopNum) throws Exception;

	Slice<OnestopAnswerDto> onestopAnswerListForMypage(int page, String companyId) throws Exception;
}
