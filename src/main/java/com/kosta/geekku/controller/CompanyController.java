package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.entity.Estate;
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

		// 전달된 요청 파라미터 출력
		System.out.println("Received Parameters:");
		System.out.println("bsnmCmpnm: " + bsnmCmpnm);
		System.out.println("brkrNm: " + brkrNm);
		System.out.println("jurirno: " + jurirno);

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

	@PostMapping("/company/changePwd")
	public ResponseEntity<String> changePwd(Authentication authentication, @RequestBody Map<String, String> param) {
		try {
			String currentPassword = param.get("currentPassword");
			String newPassword = param.get("newPassword");

			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();

			CompanyDto companyDto = companyService.getCompany(companyId);
			if (!bCryptPasswordEncoder.matches(currentPassword, companyDto.getPassword())) {
				return new ResponseEntity<>("현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
			}

			// 새 비밀번호
			String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
			companyService.changePassword(companyId, encodedNewPassword);

			return new ResponseEntity<>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("비밀번호 변경에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	// 중개업자 프로필 조회

	@GetMapping("/estateProfile/{companyId}")
	public ResponseEntity<?> getBrokerProfile(@PathVariable String companyId) {
		try {
			CompanyDto companyProfile = companyService.getCompanyProfile(companyId);
			return ResponseEntity.ok(companyProfile);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 정보를 찾을 수 없습니다.");
		}
	}

	// 중개업자 쓴 글 보기(수정해야할 수도 있음)
	@GetMapping("/estateCommunities/{companyId}") // 예시:
													// http://localhost:8080/estateCommunities/7e7506d5-b944-40c8-a269-c3c58d2067bb
	public ResponseEntity<?> getEstateCommunities(@PathVariable String companyId) {
		try {
			List<Estate> estate = companyService.getEstateCommunities(companyId);
			return ResponseEntity.ok(estate);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("중개업자가 작성한 게시글을 찾을 수 없습니다.");
		}
	}

	// 중개업자가 쓴 글 삭제하기 (테스트 해야함)
	@DeleteMapping("/estateCommunityDelete/{estateId}")
	public ResponseEntity<?> deleteEstateCommunity(@PathVariable Integer estateId) {
		try {
			companyService.deleteEstateCommunity(estateId);
			return new ResponseEntity<>("게시글 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("중개업자가 작성한 게시글을 찾을 수 없습니다.");
		}
	}

	// 중개업자 집꾸하기 답변 글 조회 (테스트 해야함) + 페이징 처리
	@GetMapping("/company/estateAnswered/{companyId}")
	public ResponseEntity<Page<HouseAnswerDto>> getAnswersByCompanyId(@PathVariable UUID companyId,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<HouseAnswerDto> houseAnswers = companyService.getAnswersByCompanyId(companyId, pageable);

			return ResponseEntity.ok(houseAnswers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<HouseAnswerDto>>(HttpStatus.BAD_REQUEST);
	}

	 @GetMapping("/onestopAnswered/{companyId}")
	    public ResponseEntity<?> getOnestopAnswered(
	            @PathVariable UUID companyId,
	            @RequestParam(value = "page", defaultValue = "1") int page, 
	            @RequestParam(value = "size", defaultValue = "10") int size) {
	        try {
	            Pageable pageable = PageRequest.of(page - 1, size);
	            Page<OnestopAnswerDto> onestopAnswers = companyService.getOnestopAnswersByCompanyId(companyId, pageable);

	            // 응답 데이터를 Map으로 정리
	            Map<String, Object> response = new HashMap<>();
	            response.put("currentPage", onestopAnswers.getNumber() + 1);
	            response.put("totalPages", onestopAnswers.getTotalPages());
	            response.put("totalItems", onestopAnswers.getTotalElements());
	            response.put("content", onestopAnswers.getContent()); 

	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 중개업자의 데이터를 찾을 수 없습니다.");
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

	@PostMapping("/findCompanyByEmail")
	public ResponseEntity<List<CompanyDto>> findIdByEmail(@RequestBody Map<String, String> param) {
		try {
			String email = param.get("email");
			List<CompanyDto> companyDtoList = companyService.findIdByEmail(email);
			return new ResponseEntity<List<CompanyDto>>(companyDtoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CompanyDto>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/findCompanyByPhone")
	public ResponseEntity<List<CompanyDto>> findCompanyByPhone(@RequestBody Map<String, String> param) {
		try {
			String phone = param.get("phone");
			List<CompanyDto> companyDtoList = companyService.findIdByPhone(phone);
			return new ResponseEntity<List<CompanyDto>>(companyDtoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CompanyDto>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/company/companyCertImg/{num}")
	public ResponseEntity<String> getCompanyImg(Authentication authentication, @PathVariable Integer num) {
		try {
			String filePath = companyService.getCompanyCertificationImagePath(num);
			return new ResponseEntity<String>(filePath, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/company/updateCompanyInfo")
	public ResponseEntity<Map<String, Object>> updateCompanyInfo(Authentication authentication, CompanyDto companyDto,
			@RequestParam(name = "file", required = false) MultipartFile profile,
			@RequestParam(name = "certificationFile", required = false) MultipartFile certFile) {
		System.out.println("---------기업정보수정");
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			Map<String, Object> result = companyService.updateCompanyInfo(companyId, companyDto, profile, certFile);
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
