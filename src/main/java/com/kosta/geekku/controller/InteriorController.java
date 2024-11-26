package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.service.InteriorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorController {
	private final InteriorService interiorService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("/interiorMain")
	public ResponseEntity<Map<String, Object>> interioMain() {
		try {
			List<SampleDto> sampleList = interiorService.sampleListForMain();
			List<InteriorDto> interiorList = interiorService.interiorListForMain();
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("sampleList", sampleList);
			listInfo.put("interiorList", interiorList);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorList")
	public ResponseEntity<Map<String, Object>> interiorList(
			@RequestParam(value = "possibleLocation", required = false) String possibleLocation) {
		try {
			List<InteriorDto> interiorList = interiorService.interiorList(possibleLocation);
			System.out.println(interiorList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorList", interiorList);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/interiorBookmark/{num}")
	public ResponseEntity<String> interiorBookmark(String userId, @PathVariable Integer num) {
		try {
//			String id = ((PrincipalDetails)authentication.getPrincipal()).getUser().getId(); 
			boolean bookmark = interiorService.toggleBookmark(userId, num);
			return new ResponseEntity<String>(String.valueOf(bookmark), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorRegister")
	public ResponseEntity<String> interiorRegister(InteriorDto interiorDto) {
		System.out.println(interiorDto);
		try {
			Integer interiorNum = interiorService.interiorRegister(interiorDto);
			return new ResponseEntity<String>(String.valueOf(interiorNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorSampleRegister")
	public ResponseEntity<String> interiorSampleRegister(SampleDto sampleDto) {
		try {
			Integer sampleNum = interiorService.sampleRegister(sampleDto);
			return new ResponseEntity<String>(String.valueOf(sampleNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorReviewRegister")
	public ResponseEntity<String> interiorReviewRegister(ReviewDto reviewDto) {
		try {
			Integer reviewNum = interiorService.reviewRegister(reviewDto);
			return new ResponseEntity<String>(String.valueOf(reviewNum), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/sampleDetail")
	public ResponseEntity<Map<String, Object>> sampleDetail(Integer num) {
		try {
			Map<String, Object> res = new HashMap<>();
			SampleDto sampleDto = interiorService.sampleDetail(num);
			res.put("sample", sampleDto);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorRequest")
	public ResponseEntity<String> interiorRequest(InteriorRequestDto requestDto) {
		try {
			Integer requestNum = interiorService.interiorRequest(requestDto);
			return new ResponseEntity<String>(String.valueOf(requestNum), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/requestDetail")
	public ResponseEntity<Map<String, Object>> requestDetail(Integer num) {
		try {
			Map<String,Object> res = new HashMap<>();
			InteriorRequestDto requestDto = interiorService.requestDetail(num);

			res.put("requestDetail", requestDto);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/sampleList")
	public ResponseEntity<Map<String,Object>> sampleList(
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String style,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String location) {
		try {
			List<InteriorSample> sampleList = interiorService.sampleList(date, type, style, size, location);
			System.out.println(sampleList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("sampleList", sampleList);
			return new ResponseEntity<Map<String,Object>>(listInfo,HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/interiorDetail")
	public ResponseEntity<Map<String,Object>> interiorDetail(Integer num) {
		try {
			Map<String,Object> detailInfo = interiorService.interiorDetail(num);
			return new ResponseEntity<Map<String,Object>>(detailInfo, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	
	// 개인 마이페이지 - 방꾸 신청내역 리스트
	@GetMapping("/mypageUserInteriorRequestList")
	public ResponseEntity<Page<InteriorRequestDto>> interiorRequestListForUserMypage(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
			@RequestParam(required = false, defaultValue = "10", value = "size") int size, 
			@RequestParam("userId") String userId) {
		try {
			Page<InteriorRequestDto> interiorRequestList = interiorService.interiorRequestListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<InteriorRequestDto>>(interiorRequestList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<InteriorRequestDto>>(HttpStatus.OK);
		}
	}
	
	// 개인 마이페이지 - 인테리어 업체 후기 리스트
	@GetMapping("/mypageUserReviewList")
	public ResponseEntity<Page<ReviewDto>> mypageUserReviewList(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
			@RequestParam(required = false, defaultValue = "10", value = "size") int size, 
			@RequestParam("userId") String userId) {
		try {
			Page<ReviewDto> reviewList = interiorService.reviewListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<ReviewDto>>(reviewList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<ReviewDto>>(HttpStatus.OK);
		}
	}
	
	// 개인 마이페이지 - 인테리어 업체 후기 수정
	@PostMapping("/mypageUserReviewUpdate/{num}")
	public ResponseEntity<String> mypageUserReviewUpdate(ReviewDto reviewDto, @PathVariable Integer num) {
		try {
			interiorService.updateReview(reviewDto, num);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("후기 수정 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	// 개인 마이페이지 - 인테리어 업체 후기 삭제
	@PostMapping("/mypageUserReviewDelete/{num}")
	public ResponseEntity<String> mypageUserReviewDelete(@PathVariable Integer num) {
		try {
			interiorService.deleteReview(num);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("후기 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
}
