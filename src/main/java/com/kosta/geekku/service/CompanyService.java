package com.kosta.geekku.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;

public interface CompanyService {
	void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception;
	CompanyDto login(String username, String password) throws Exception;
	CompanyDto getCompany(String username) throws Exception;
	void updateCompanyInfo(UUID companyId, CompanyDto companyDto) throws Exception;
}
