package com.kosta.geekku.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.util.PageInfo;

public interface EstateService {
	Integer estateWrite(EstateDto estateDto, List<MultipartFile> estateImageList) throws Exception;
	EstateDto estateDetail(Integer estateNum) throws Exception;
	List<EstateDto> estateList(PageInfo page, String type, String keyword) throws Exception;
	List<EstateDto> estateListForMain() throws Exception;
	void estateDelete(Integer estateNum) throws Exception;
	Integer checkBookmark(String userId, Integer estateNum) throws Exception;
	boolean toggleBookmark(String userId, Integer estateNum) throws Exception;
	
	// 마이페이지 - 중개업자 매물 등록 내역
	List<EstateDto> estateListForMypage(PageInfo pageInfo, String companyId) throws Exception;
}
