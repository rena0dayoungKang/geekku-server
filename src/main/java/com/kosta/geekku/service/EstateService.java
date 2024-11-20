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
	void estateDelete(Integer estateNum) throws Exception;
	Integer checkBookmark(UUID string, Integer estateNum) throws Exception;
	boolean toggleBookmark(UUID userId, Integer estateNum) throws Exception;
}
