package com.kosta.geekku.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.entity.Role;
import com.kosta.geekku.service.CompanyService;
import com.kosta.geekku.service.EstateNumberService;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EstateNumberService estateNumberService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/joinCompany")
	public ResponseEntity<String> joinCompany(CompanyDto companyDto,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		try {
			String rawPassword = companyDto.getPassword();
			companyDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

			if ("estate".equals(companyDto.getType()) || "interior".equals(companyDto.getType())) {
				companyDto.setRole(Role.ROLE_COMPANY);
			} else {
				return new ResponseEntity<String>("사업자 타입 오류", HttpStatus.BAD_REQUEST);
			}

			companyService.joinCompany(companyDto, file);
			return new ResponseEntity<String>("기업회원 가입 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("회원가입 실패", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/searchEstate")
	public ResponseEntity<String> searchEstate(@RequestParam(required = false) String bsnmCmpnm,
			@RequestParam(required = false) String brkrNm, @RequestParam(required = false) String jurirno) {
		// 브이월드 Settings 출력
		estateNumberService.vworldSettings();
		try {
			String response = estateNumberService.searchEstate(bsnmCmpnm, brkrNm, jurirno);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("조회할 수 없습니다", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/company/companyInfo")
	public ResponseEntity<CompanyDto> getCompanyInfo(Authentication authentication) {
		String username = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getUsername();
		try {
			CompanyDto companyDto = companyService.getCompany(username);
			return new ResponseEntity<CompanyDto>(companyDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CompanyDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/company/updateCompanyInfo")
	public ResponseEntity<String> updateCompanyInfo(Authentication authentication, @RequestBody CompanyDto companyDto) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			companyService.updateCompanyInfo(companyId, companyDto);
			return new ResponseEntity<String>("회원정보 수정 완료", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("회원정보 수정 실패", HttpStatus.BAD_REQUEST);
		}
		
	}
}
