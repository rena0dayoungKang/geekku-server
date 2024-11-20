package com.kosta.geekku.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.EstateDto;

public interface EstateService {
	Integer estateWrite(EstateDto estateDto, List<MultipartFile> estateImageList) throws Exception;
	EstateDto estateDetail(Integer estateNum) throws Exception;
}
