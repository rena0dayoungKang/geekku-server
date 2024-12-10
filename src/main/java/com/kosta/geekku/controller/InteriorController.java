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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
			System.out.println(possibleLocation);
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
	public ResponseEntity<String> interiorBookmark(Authentication authentication, @PathVariable Integer num) {
		try {
			String userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId().toString();
			boolean bookmark = interiorService.toggleBookmark(userId, num);
			return new ResponseEntity<String>(String.valueOf(bookmark), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/company/interiorRegister")
	public ResponseEntity<Map<Object,Object>> interiorRegister(Authentication authentication, InteriorDto interiorDto,
			@RequestParam(name = "coverImg", required = false) MultipartFile coverImage) {
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			Map<Object,Object> interiorNum = interiorService.interiorRegister(interiorDto, coverImage, companyId);
			return new ResponseEntity<Map<Object,Object>>(interiorNum, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<Object,Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 인테리어 업체 정보 수정
	@PostMapping("/company/interiorModify")
	public ResponseEntity<String> interiorModify(Authentication authentication, InteriorDto interiorDto,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		System.out.println(interiorDto);
		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId(); // UUID 추출
//			System.out.println(companyId);

			Map<String, Object> res = interiorService.updateInteriorCompany(companyId, interiorDto, file);
			System.out.println(file);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/company/interiorSampleRegister")
	public ResponseEntity<String> interiorSampleRegister(Authentication authentication, SampleDto sampleDto,
			@RequestPart(name = "coverImg", required = false) MultipartFile coverImage) {

		try {
			UUID companyId = ((PrincipalDetails) authentication.getPrincipal()).getCompany().getCompanyId();
			Integer sampleNum = interiorService.sampleRegister(sampleDto, coverImage, companyId);

			return new ResponseEntity<String>(String.valueOf(sampleNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/sampleImage/{imageName}")
	public void getSampleImage(@PathVariable String imageName, HttpServletResponse response) {
		try {
			String uploadPath = "C:/geekku/image_upload/sampleImage/";
			File file = new File(uploadPath, imageName);

			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			InputStream ins = new FileInputStream(file);
			response.setContentType("image/png");
			FileCopyUtils.copy(ins, response.getOutputStream());
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/user/interiorReviewWrite")
	public ResponseEntity<String> interiorReviewRegister(Authentication authentication, ReviewDto reviewDto,
			@RequestParam(name = "file", required = false) MultipartFile[] files) {
		System.out.println(reviewDto);
		System.out.println(files);
		try {
			String id = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId().toString(); // 재확인
			Integer reviewNum = interiorService.reviewRegister(id, reviewDto,
					files == null ? null : Arrays.asList(files));
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

	@PostMapping("/sampleDetail")
	public ResponseEntity<Map<String, Object>> sampleDetail(@RequestBody Map<String, String> param) {
		try {
			System.out.println("good");
			System.out.println(param);
			Map<String, Object> res = new HashMap<>();
			SampleDto sampleDto = interiorService.sampleDetail(Integer.parseInt(param.get("num")));
			res.put("sampleInfo", sampleDto);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/user/interiorRequest")
	public ResponseEntity<String> interiorRequest(Authentication authentication,
			@RequestBody InteriorRequestDto requestDto) {
		try {
			System.out.println(requestDto);
			String id = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId().toString(); // 재확인
			System.out.println(id);
			Integer requestNum = interiorService.interiorRequest(id, requestDto);
			System.out.println(requestNum);
			return new ResponseEntity<String>(String.valueOf(requestNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/requestDetail")
	public ResponseEntity<Map<String, Object>> requestDetail(@RequestBody Map<String, String> param) {
		try {
			System.out.println("test");
			System.out.println(param);
			Map<String, Object> res = new HashMap<>();
			InteriorRequestDto requestDto = interiorService.requestDetail(Integer.parseInt(param.get("num")));
			res.put("requestInfo", requestDto);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/sampleList")
	public ResponseEntity<Map<String, Object>> sampleList(@RequestParam(name = "date", required = false) String date,
			@RequestParam(name = "types", required = false) String[] types,
			@RequestParam(name = "styles", required = false) String[] styles,
			@RequestParam(name = "sizes", required = false) String[] sizes,
			@RequestParam(name = "location", required = false) String[] location) {
		try {
			List<SampleDto> sampleList = interiorService.sampleList(date, types, styles, sizes, location);
			System.out.println(sampleList);
			System.out.println(types);
			System.out.println(styles);
			System.out.println(date);
			System.out.println(sizes);
			System.out.println(location);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("sampleList", sampleList);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorDetail")
	public ResponseEntity<Map<String, Object>> interiorDetail(@RequestBody Map<String, String> param) {
		try {
			System.out.println("param=====" + param);
			Map<String, Object> detailInfo = interiorService.interiorDetail(Integer.parseInt(param.get("num")));
			System.out.println("detailInfo=====" + detailInfo);

			System.out.println("-----------test id==" + param.get("id"));
			System.out.println("-----------" + param.get("num"));
			if (param.get("id") != null && !param.get("id").isEmpty()) {
				boolean bookmark = interiorService.checkBookmark(param.get("id"),
						Integer.parseInt(param.get("num"))) != null;
				detailInfo.put("bookmark", bookmark);
				System.out.println("========================bookmarkTest===================");
				System.out.println(bookmark);
			} else {
				System.out.println("else test");
			}
			return new ResponseEntity<Map<String, Object>>(detailInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 개인 마이페이지 - 1:1 인테리어 문의내역 리스트
	@GetMapping("/user/myPageUserInteriorRequestList")
	public ResponseEntity<Page<InteriorRequestDto>> interiorRequestListForUserMyPage(Authentication authentication,
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
			@PathVariable Integer num, @RequestParam(name = "delFile", required = false) Integer[] delFileNum,
			@RequestParam(name = "file", required = false) MultipartFile[] fileList) {
		try {
			interiorService.updateReview(reviewDto, num, delFileNum == null ? null : Arrays.asList(delFileNum),
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