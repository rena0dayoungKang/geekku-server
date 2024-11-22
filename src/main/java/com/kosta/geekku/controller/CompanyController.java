package com.kosta.geekku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.dto.EstateDto;
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
			
			if("real_estate".equals(companyDto.getType())) {
				companyDto.setRole(Role.ROLE_ESTATE);
			} else if ("interior".equals(companyDto.getType())) {
				companyDto.setRole(Role.ROLE_INTERIOR);
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
											   @RequestParam(required = false) String brkrNm,
											   @RequestParam(required = false) String jurirno) {
		//브이월드 Settings 출력
		estateNumberService.vworldSettings();
		try {
			String response = estateNumberService.searchEstate(bsnmCmpnm, brkrNm, jurirno);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("조회할 수 없습니다", HttpStatus.BAD_REQUEST);
		}
	}
	
	//중개업자 프로필 조회
	@GetMapping("/estateProfile/{companyId}") 
    public ResponseEntity<?> getBrokerProfile(@PathVariable String companyId) {
        try {
            CompanyDto companyProfile = companyService.getCompanyProfile(companyId);
            return ResponseEntity.ok(companyProfile);
        } catch (Exception e) {
            // 다른 예외가 발생한 경우, 서버 오류로 처리
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 정보를 찾을 수 없습니다.");
        }
    }

}
