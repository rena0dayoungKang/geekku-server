package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private final HttpSession session;

	@GetMapping("/interiorCompanyDetail/{num}")
	public ResponseEntity<Map<String, Object>> onestopDetail(@PathVariable Integer num) {
		try {
			Map<String, Object> res = new HashMap<>();
			System.out.println("controller" + num);
			InteriorDto interiorDto = interiorService.interiorCompanyDetail(num);
			System.out.println(num);
			// boolean heart = onestopService.checkHeart(boardDto.getWriter(), num) != null;
			res.put("interior", interiorDto);
			// res.put("heart", heart);

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

	// �씤�뀒由ъ뼱�뾽�옄 �궡媛� �븳 onestop �떟蹂� 由ъ뒪�듃 議고쉶
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

	// �씤�뀒由ъ뼱�뾽�옄 �궡媛� �븳 諛⑷씀�븯湲� �떟蹂� 由ъ뒪�듃 議고쉶

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

	// �씤�뀒由ъ뼱�뾽�옄 �궡媛� �옉�꽦�븳 �씤�뀒由ъ뼱 �떆怨듭궗濡� 紐⑥븘蹂닿린
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

	// �씤�뀒由ъ뼱�뾽�옄 諛쏆� �씤�뀒由ъ뼱 由щ럭 紐⑥븘蹂닿린
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

	// �씤�뀒由ъ뼱�뾽�옄 �궡媛� �옉�꽦�븳 �씤�뀒由ъ뼱 �떊泥� 紐⑥븘蹂닿린
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
