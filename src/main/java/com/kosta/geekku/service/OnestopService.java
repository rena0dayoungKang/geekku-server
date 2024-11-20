package com.kosta.geekku.service;

import java.util.List;

import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.util.PageInfo;

public interface OnestopService {
	Integer onestopWrite(OnestopDto onestopDto) throws Exception;

	OnestopDto onestopDetail(Integer num) throws Exception;

	Integer onestopModify(OnestopDto onestopDto) throws Exception;

	List<OnestopDto> onestopList(PageInfo page, String type, String word) throws Exception;
}
