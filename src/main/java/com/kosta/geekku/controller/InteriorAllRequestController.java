package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.repository.InteriorAllRequestRepository;
import com.kosta.geekku.service.FcmMessageService;
import com.kosta.geekku.service.InteriorAllRequestService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorAllRequestController {
	private final InteriorAllRequestService interiorAllService;
	private final FcmMessageService fcmMessageService;
	private final InteriorAllRequestRepository interiorAllRequestRepository;
	

	@PostMapping("/user/interiorAllWrite")
	public ResponseEntity<String> interiorAllWrite(Authentication authentication, InteriorAllDto interiorAllDto) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId(); 
			Integer interiorAllNum = interiorAllService.interiorAllWrite(interiorAllDto, userId);
			System.out.println(interiorAllDto);
			return new ResponseEntity<String>(String.valueOf(interiorAllNum), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorAllDetail/{num}")
	public ResponseEntity<InteriorAllDto> interiorAllDetail(@PathVariable Integer num) {
		try {
			InteriorAllDto interiorAllDto = interiorAllService.interiorDetail(num);
			return new ResponseEntity<InteriorAllDto>(interiorAllDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<InteriorAllDto>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorAllList")
	public ResponseEntity<Map<String, Object>> interiorAllList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keyword", required = false) String word) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<InteriorAllDto> interiorAllList = interiorAllService.interiorAllList(pageInfo, type, word);

			// 리스트를 내림차순으로 정렬
			// interiorAllList.sort(Comparator.comparing(InteriorAllDto::getCreatedAt).reversed());

			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorAllList", interiorAllList);
			listInfo.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/user/interiorAllDelete/{requestAllNum}")
	public ResponseEntity<String> interiorAllDelete(@PathVariable Integer requestAllNum) {
		try {
			interiorAllService.interiorAllDelete(requestAllNum);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	// 방꾸 답변
	/** 알림 해야함 */
	@PostMapping("/company/interiorAnswerWrite")
	public ResponseEntity<String> interiorAnswerWrite(Authentication authentication,
			InteriorAnswerDto interiorAnswerDto) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			
			Integer interiorAnswerNum = interiorAllService.interiorAnswerWrite(interiorAnswerDto, companyId);
			interiorAnswerDto.setAnswerAllNum(interiorAnswerNum);
			
			// userId 조회
	        InteriorAllRequest request = interiorAllRequestRepository.findByRequestAllNum(interiorAnswerDto.getRequestAllNum());
	        if (request == null) {
	            throw new IllegalArgumentException("유효하지 않은 requestAllNum입니다.");
	        }
	        
	        interiorAnswerDto.setUserId(request.getUser().getUserId());

			System.out.println(interiorAnswerDto);
			fcmMessageService.sendInteriorAllAnswer(interiorAnswerDto);//	알림 추가
			return new ResponseEntity<String>(String.valueOf(interiorAnswerNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("방꾸답변 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorAnswerList/{requestAllNum}")
	public ResponseEntity<Map<String, Object>> interiorAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@PathVariable Integer requestAllNum) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<InteriorAnswerDto> interiorAnswerList = interiorAllService.interiorAnswerList(pageInfo, requestAllNum);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorAnswerList", interiorAnswerList);
			listInfo.put("pageInfo", pageInfo);
			System.out.println(interiorAnswerList);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/company/interiorAnswerDelete")
	public ResponseEntity<String> interiorAnswerDelete(@RequestBody Map<String, Object> params) {
		try {
			// 안전하게 값을 추출
			Object rawAnswerNum = params.get("requestAllAnswerNum");
			Object rawAllNum = params.get("requestAllNum");

			Integer requestAllAnswerNum = rawAnswerNum != null ? Integer.parseInt(rawAnswerNum.toString()) : null;
			Integer requestAllNum = rawAllNum != null ? Integer.parseInt(rawAllNum.toString()) : null;

			// 값 검증
			if (requestAllAnswerNum == null || requestAllNum == null) {
				return new ResponseEntity<>("유효하지 않은 요청 값입니다.", HttpStatus.BAD_REQUEST);
			}

			// 서비스 호출
			interiorAllService.interiorAnswerDelete(requestAllAnswerNum, requestAllNum);
			return new ResponseEntity<>("true", HttpStatus.OK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ResponseEntity<>("숫자 형식 오류", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("방꾸답변 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/mypageUserInteriorAllList")
	public ResponseEntity<Page<InteriorAllDto>> interiorAllRequestListForUserMypage(Authentication authentication,
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "10", value = "size") int size) {
		try {
			String userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId().toString();
			Page<InteriorAllDto> interiorAllList = interiorAllService.interiorAllListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<InteriorAllDto>>(interiorAllList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<InteriorAllDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}
