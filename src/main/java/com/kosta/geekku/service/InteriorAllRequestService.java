package com.kosta.geekku.service;

import java.util.List;

import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.util.PageInfo;

public interface InteriorAllRequestService {

	Integer interiorAllWrite(InteriorAllDto interiorAllDto) throws Exception;

	InteriorAllDto interiorDetail(Integer num) throws Exception;

	Integer interiorAllModify(InteriorAllDto interiorAllDto) throws Exception;

	List<InteriorAllDto> interiorAllList(PageInfo page, String type, String word) throws Exception;

	void interiorAllDelete(Integer num) throws Exception;
}
