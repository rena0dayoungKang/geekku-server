package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.HouseDto;
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
			System.out.println("controller" + companyId);
			// UUID를
			// 추출

			InteriorDto interiorDto = interiorService.interiorCompanyDetail(companyId);
			Map<String, Object> res = new HashMap<>();
			res.put("interior", interiorDto);
			System.out.println("res: " + res);

			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * @GetMapping("/company/myInteriorInfo") public ResponseEntity<CompanyDto>
	 * getInteriorInfo(Authentication authentication) { String username =
	 * ((PrincipalDetails)
	 * authentication.getPrincipal()).getCompany().getUsername(); try { CompanyDto
	 * companyDto = interiorService.getCompany(username); return new
	 * ResponseEntity<CompanyDto>(companyDto, HttpStatus.OK); } catch (Exception e)
	 * { e.printStackTrace(); return new
	 * ResponseEntity<CompanyDto>(HttpStatus.BAD_REQUEST); } }
	 */

	// 인테리어업자 내가 한 onestop 답변 리스트 조회
	@GetMapping("/myOnestopAnswerList")
	public ResponseEntity<Map<String, Object>> myOnestopAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			Slice<OnestopAnswerDto> myOnestopAnswerList = onestopService.onestopAnswerListForMypage(page, companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("myOnestopAnswerList", myOnestopAnswerList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어업자 내가 한 방꾸하기 답변 리스트 조회

	@GetMapping("/myInteriorAnswerList")
	public ResponseEntity<Map<String, Object>> myInteriorAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			Slice<InteriorAnswerDto> myInteriorAnswerList = interiorAllRequestService.interiorAnswerListForMypage(page,
					companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("myInteriorAnswerList", myInteriorAnswerList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어업자 내가 작성한 인테리어 시공사례 모아보기
	@GetMapping("/myInteriorSampleList")
	public ResponseEntity<Map<String, Object>> mypageEstateList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
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

	// 인테리어업자 받은 인테리어 리뷰 모아보기
	@GetMapping("/myInteriorReviewList")
	public ResponseEntity<Map<String, Object>> interiorReviewList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<ReviewDto> interiorReviewList = interiorService.interiorReviewList(pageInfo, companyId);
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
	@GetMapping("/myInteriorRequestList")
	public ResponseEntity<Map<String, Object>> interiorRequestList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<InteriorRequestDto> interiorRequestList = interiorService.interiorRequestList(pageInfo, companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorRequestList", interiorRequestList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

}
