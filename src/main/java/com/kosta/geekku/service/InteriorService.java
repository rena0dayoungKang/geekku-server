package com.kosta.geekku.service;

import java.util.List;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.entity.InteriorSample;

public interface InteriorService {
	List<InteriorDto> interiorListForMain() throws Exception;

	List<InteriorSample> sampleListForMain() throws Exception;

	InteriorDto interiorCompanyDetail(Integer num) throws Exception;

}
