package com.kosta.geekku.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.UFile;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UFileRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UFileRepository uFileRepository;

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception {

		// type이 "부동산" 인 경우 estateNumber가 null이면 예외처리
		if ("부동산".equals(companyDto.getType()) && isNullOrEmpty(companyDto.getEstateNumber())) {
			throw new IllegalArgumentException("부동산타입은 중개등록번호 입력해야함");
		}

		Company company = companyDto.toEntity();

		// 회원가입시 기본이미지 설정
		byte[] defaultProfileImage = Files.readAllBytes(Paths.get("src/main/resources/static/img/profileImg.png"));
		company.setProfileImage(defaultProfileImage);

		// 사업자 등록증 이미지 첨부
		if (file != null) {
			UFile uFile = new UFile();
			uFile.setDirectory(uploadPath);
			uFile.setName(file.getOriginalFilename());
			uFile.setContentType(file.getContentType());
			uFile.setSize(file.getSize());
			uFile.setCompany(company);
			uFileRepository.save(uFile);
		}

		companyRepository.save(company);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	@Override
	public CompanyDto login(String username, String password) throws Exception {
		Company company = companyRepository.findByUsername(username).orElseThrow(() -> new Exception("아이디 오류"));
		if (!company.getPassword().equals(password)) {
			throw new Exception("비밀번호 오류");
		}
		return company.toDto();
	}

	@Override
	public CompanyDto getCompany(String username) throws Exception {
		Company company = companyRepository.findByUsername(username).orElseThrow(() -> new Exception("사용자 아이디 오류"));
		return company.toDto();
	}

	@Override
	public void updateCompanyInfo(UUID companyId, CompanyDto companyDto) throws Exception {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		if(companyDto.getCompanyAddress() != null) company.setCompanyAddress(companyDto.getCompanyAddress());
		if(companyDto.getPhone() != null) company.setPhone(companyDto.getPhone());
		if(companyDto.getEmail() != null) company.setEmail(companyDto.getEmail());
		if(companyDto.getCompanyCertificationImage() != null) company.setCompanyCertificationImage(companyDto.getCompanyCertificationImage());
		companyRepository.save(company);
	}

}
