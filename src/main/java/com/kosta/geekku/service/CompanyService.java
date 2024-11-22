package com.kosta.geekku.service;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;

public interface CompanyService {
	void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception;
	CompanyDto login(String username, String password) throws Exception;
	CompanyDto getCompanyProfile(String companyId) throws Exception;
	
}
