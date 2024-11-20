package com.kosta.geekku.service;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;

public interface CompanyService {
	void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception;
}
