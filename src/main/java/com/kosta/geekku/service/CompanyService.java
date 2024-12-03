package com.kosta.geekku.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.HouseAnswer;
import com.kosta.geekku.entity.OnestopAnswer;

public interface CompanyService {
	void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception;

	CompanyDto login(String username, String password) throws Exception;

	CompanyDto getCompany(String username) throws Exception;
	
	CompanyDto getCompany(UUID companyId) throws Exception;

	Map<String, Object> updateCompanyInfo(UUID companyId, CompanyDto companyDto, MultipartFile file, MultipartFile certificationFile) throws Exception;

	CompanyDto getCompanyProfile(String companyId) throws Exception;

	List<Estate> getEstateCommunities(String companyId) throws Exception;

	void deleteEstateCommunity(Integer estateId) throws Exception;

	Page<HouseAnswer> getAnswersByCompanyId(UUID companyId, Pageable pageable) throws Exception;

	Page<OnestopAnswer> getOnestopAnswersByCompanyId(UUID companyId, Pageable pageable) throws Exception;

	String getCompanyCertificationImagePath(Integer num) throws Exception;
	
	Map<String, Object> changePassword(UUID companyId, String newPassword) throws Exception;
}
