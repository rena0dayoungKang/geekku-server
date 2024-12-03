package com.kosta.geekku.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
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
	public ResponseEntity<String> interiorRegister(InteriorDto interiorDto,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		System.out.println(interiorDto);
		try {
			Integer interiorNum = interiorService.interiorRegister(interiorDto, file);
			System.out.println(file);
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

	@PostMapping("/user/interiorReviewWrite")
	public ResponseEntity<String> interiorReviewRegister(Authentication authentication,ReviewDto reviewDto,
			@RequestParam(name="file", required = false) MultipartFile[] files) {
		System.out.println(reviewDto);
		System.out.println(files);
		try {
			String id = ((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId().toString();	//재확인
			Integer reviewNum = interiorService.reviewRegister(id ,reviewDto, files==null? null : Arrays.asList(files));
			System.out.println(reviewNum);
			return new ResponseEntity<String>(String.valueOf(reviewNum), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/reviewImage/{num}")
	public void image(@PathVariable String num, HttpServletResponse response) {
		try {
			// 파일이 존재하지 않는 경우 처리
			File file = new File(uploadPath, num);
			if (!file.exists()) {
				System.out.println("파일 존재하지 않음");
				return;
			}
			InputStream ins = new FileInputStream(file);
			FileCopyUtils.copy(ins, response.getOutputStream());
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/user/interiorReview/{reviewNum}")
	public ResponseEntity<ReviewDto> interiorReviewRegister(@PathVariable Integer reviewNum) {
		try {
			ReviewDto reviewDto = interiorService.getReview(reviewNum);
			return new ResponseEntity<ReviewDto>(reviewDto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ReviewDto>(HttpStatus.BAD_REQUEST);
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
			Map<String, Object> res = new HashMap<>();
			InteriorRequestDto requestDto = interiorService.requestDetail(num);

			res.put("requestDetail", requestDto);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/sampleList")
	public ResponseEntity<Map<String, Object>> sampleList(@RequestParam(required = false) String date,
			@RequestParam(required = false) String type, @RequestParam(required = false) String style,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) String location) {
		try {
			List<SampleDto> sampleList = interiorService.sampleList(date, type, style, size, location);
			System.out.println(sampleList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("sampleList", sampleList);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorDetail")
	public ResponseEntity<Map<String, Object>> interiorDetail(Integer num) {
		try {
			Map<String, Object> detailInfo = interiorService.interiorDetail(num);
			return new ResponseEntity<Map<String, Object>>(detailInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 개인 마이페이지 - 1:1 인테리어 문의내역 리스트
	@GetMapping("/user/mypageUserInteriorRequestList")
	public ResponseEntity<Page<InteriorRequestDto>> interiorRequestListForUserMypage(Authentication authentication,
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "10", value = "size") int size) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();
			Page<InteriorRequestDto> interiorRequestList = interiorService.interiorRequestListForUserMypage(page, size,
					userId);

			return new ResponseEntity<Page<InteriorRequestDto>>(interiorRequestList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<InteriorRequestDto>>(HttpStatus.OK);
		}
	}
	
	// 개인 마이페이지 - 1:1 인테리어 문의내역 삭제
	@PostMapping("/user/mypageUserRequestInteriorDelete/{requestNum}")
	public ResponseEntity<String> interiorRequestListForUserMypage(@PathVariable Integer requestNum) {
		try {
			interiorService.deleteRequest(requestNum);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	// 개인 마이페이지 - 인테리어 업체 후기 리스트
	@GetMapping("/user/mypageUserReviewList")
	public ResponseEntity<Page<ReviewDto>> mypageUserReviewList(Authentication authentication,
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "10", value = "size") int size) {
		try {

			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();

			Page<ReviewDto> reviewList = interiorService.reviewListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<ReviewDto>>(reviewList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<ReviewDto>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 개인 마이페이지 - 인테리어 업체 후기 수정
	@PostMapping("/user/mypageUserReviewUpdate/{num}")
	public ResponseEntity<String> mypageUserReviewUpdate(Authentication authentication, ReviewDto reviewDto,
			@PathVariable Integer num, 
			@RequestParam(name="delFile", required=false) Integer[] delFileNum,
			@RequestParam(name="file", required=false) MultipartFile[] fileList) {
		try {
			interiorService.updateReview(reviewDto, num, 
					delFileNum == null ? null : Arrays.asList(delFileNum), 
					fileList == null ? null : Arrays.asList(fileList));
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("후기 수정 오류", HttpStatus.BAD_REQUEST);
		}
	}

	// 개인 마이페이지 - 인테리어 업체 후기 삭제
	@PostMapping("/user/mypageUserReviewDelete/{reviewNum}")
	public ResponseEntity<String> mypageUserReviewDelete(@PathVariable Integer reviewNum) {
		try {
			interiorService.deleteReview(reviewNum);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("후기 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
}
