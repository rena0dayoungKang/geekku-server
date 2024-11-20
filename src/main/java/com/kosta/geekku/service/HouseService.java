package com.kosta.geekku.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.util.PageInfo;

public interface HouseService {
	Integer houseWrite(HouseDto houseDto) throws Exception;
	HouseDto houseDetail(Integer houseNum) throws Exception;
	List<HouseDto> houseList(PageInfo pageInfo, String type, String keyword) throws Exception;
	void houseDelete(Integer houseNum) throws Exception;
}
