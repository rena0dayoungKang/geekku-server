package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.service.InteriorAllRequestService;
import com.kosta.geekku.service.InteriorService;
import com.kosta.geekku.service.OnestopService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorUserController {

	private final InteriorService interiorService;
	private final InteriorAllRequestService interiorAllRequestService;
	private final OnestopService onestopService;

	@GetMapping("/company/interiorCompanyDetail")
	public ResponseEntity<Map<String, Object>> interiorDetail(Authentication authentication) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId(); // 토큰에서
			//System.out.println("controller" + companyId);
			// UUID를
			// 추출

			InteriorDto interiorDto = interiorService.interiorCompanyDetail(companyId);
			Map<String, Object> res = new HashMap<>();
			res.put("interior", interiorDto);
			//System.out.println("res: " + res);

			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어 업자 한번에꾸하기 답변 글 조회
	@GetMapping("/company/myOnestopAnswerList/{companyId}")
	public ResponseEntity<Page<OnestopAnswerDto>> getAnswersByCompanyId(@PathVariable UUID companyId,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<OnestopAnswerDto> onestopAnswers = onestopService.getAnswersByCompanyId(companyId, pageable);

			return ResponseEntity.ok(onestopAnswers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<OnestopAnswerDto>>(HttpStatus.BAD_REQUEST);
	}

	// 인테리어업자 내가 한 방꾸하기 답변 리스트 조회

	@GetMapping("/company/myInteriorAnswerList/{companyId}")
	public ResponseEntity<Page<InteriorAnswerDto>> getInteriorAnswersByCompanyId(@PathVariable UUID companyId,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<InteriorAnswerDto> interiorAnswers = interiorAllRequestService.getInteriorAnswersByCompanyId(companyId,
					pageable);

			return ResponseEntity.ok(interiorAnswers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<InteriorAnswerDto>>(HttpStatus.BAD_REQUEST);
	}

	// 인테리어업자 내가 작성한 인테리어 시공사례 모아보기
	@GetMapping("/company/myInteriorSampleList")
	public ResponseEntity<Map<String, Object>> mypageEstateList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			Authentication authentication) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId(); // 토큰에서
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<SampleDto> estateList = interiorService.interiorSampleList(pageInfo, companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("estateList", estateList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어업자 인테리어에 등록된 리뷰 모아보기
	@GetMapping("/company/myInteriorReviewList")
	public ResponseEntity<Map<String, Object>> interiorReviewList(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "10", value = "size") int size,
			Authentication authentication) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId(); // 토큰에서
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			Page<ReviewDto> interiorReviewList = interiorService.interiorReviewList(page, size, companyId);
			// System.out.println(interiorReviewList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorReviewList", interiorReviewList);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어업자 나한테 들어온 인테리어 신청 모아보기
	@GetMapping("/company/myInteriorRequestList")
	public ResponseEntity<Map<String, Object>> interiorRequestList(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "10", value = "size") int size,
			Authentication authentication) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			Page<InteriorRequestDto> interiorRequestList = interiorService.interiorRequestList(page, size, companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorRequestList", interiorRequestList);
			listInfo.put("page", page);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

}
