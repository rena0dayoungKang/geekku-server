package com.kosta.geekku.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.service.FcmMessageService;
import com.kosta.geekku.service.HouseService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HouseController {

	private final HouseService houseService;
	private final FcmMessageService fcmMessageService;

	@Value("${upload.path}")
	private String uploadPath;
	

	@PostMapping("/user/houseWrite")
	public ResponseEntity<String> houseWrite(Authentication authentication, HouseDto houseDto) {
		try {
			UUID userId = ((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId();
			Integer houseNum = houseService.houseWrite(houseDto, userId);
			return new ResponseEntity<String>(String.valueOf(houseNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/houseDetail/{num}")
	public ResponseEntity<HouseDto> houseDetail(@PathVariable Integer num) {
		try {
			HouseDto houseDto = houseService.houseDetail(num);
			return new ResponseEntity<HouseDto>(houseDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<HouseDto>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/houseList")
	public ResponseEntity<Map<String, Object>> houseList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keyword", required = false) String keyword) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<HouseDto> houseList = houseService.houseList(pageInfo, type, keyword);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("houseList", houseList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/user/houseDelete/{houseNum}")
	public ResponseEntity<String> houseDelete(@PathVariable Integer houseNum) {
		try {
			houseService.houseDelete(houseNum);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}

	// 집꾸 답변
	@PostMapping("/company/houseAnswerWrite")
	public ResponseEntity<String> houseAnswerWrite(Authentication authentication, HouseAnswerDto houseAnswerDto) {
		try {
			UUID companyId = ((PrincipalDetails)authentication.getPrincipal()).getCompany().getCompanyId();
			//System.out.println(companyId);
			Integer houseAnswerNum = houseService.houseAnswerWrite(houseAnswerDto, companyId);
			houseAnswerDto.setAnswerHouseNum(houseAnswerNum);
			//알림 보내기
			fcmMessageService.sendHouseAnswer(houseAnswerDto);
			return new ResponseEntity<String>(String.valueOf(houseAnswerNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸답변 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	// 집꾸 답변 에디터 이미지 url
	@PostMapping("/editorImageUpload")
	public ResponseEntity<String> editorImageUpload(@RequestParam("image") MultipartFile image) {
		try {
			String fileName = image.getOriginalFilename();
			
			// 새 파일명 생성
			int lastIndex = fileName.lastIndexOf(".");
			String ext = fileName.substring(lastIndex, fileName.length());
			String newFileName = LocalDate.now() + "_" + System.currentTimeMillis() + ext;
			
			// 파일 저장
			File file = new File(uploadPath, newFileName);
			image.transferTo(file);
			
			String url = "http://localhost:8080/editorImage/" + newFileName;
			return new ResponseEntity<String>(url, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// 에디터 답변 이미지
	@GetMapping("/editorImage/{filename}")
	public void image(@PathVariable String filename, HttpServletResponse response) {
		try {
		    // 파일이 존재하지 않는 경우 처리
			File file = new File(uploadPath, filename);
            if (!file.exists()) {
                //System.out.println("파일 존재하지 않음");
                return;
            }
            InputStream ins = new FileInputStream(file);
			FileCopyUtils.copy(ins, response.getOutputStream());
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/houseAnswerList/{houseNum}")
	public ResponseEntity<Map<String, Object>> houseAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@PathVariable Integer houseNum) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<HouseAnswerDto> houseAnswerList = houseService.houseAnswerList(pageInfo, houseNum);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("houseAnswerList", houseAnswerList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 중개업자 마이페이지 - 집꾸하기 답변 삭제
	@PostMapping("/company/houseAnswerDelete")
	public ResponseEntity<String> houseAnswerDelete(@RequestBody Map<String, Object> params) {
		try {
			Integer houseAnswerNum = (Integer)params.get("houseAnswerNum");
			Integer houseNum = (Integer)params.get("houseNum");
			houseService.houseAnswerDelete(houseAnswerNum, houseNum);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸답변 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	// 중개업자 마이페이지 - 집꾸하기 답변내역
	@GetMapping("/company/mypageHouseAnswerList")
	public ResponseEntity<Page<HouseAnswerDto>> houseAnswerListForMypage(
			 Authentication authentication,
			 @RequestParam(required = false, defaultValue = "1", value = "page") int page,  
			 @RequestParam(required = false, defaultValue = "10", value = "size") int size) {
		try {
			UUID companyId = ((PrincipalDetails)authentication.getPrincipal()).getCompany().getCompanyId();
			Page<HouseAnswerDto> houseAnswerList = houseService.houseAnswerListForMypage(page, size, companyId);
			return new ResponseEntity<Page<HouseAnswerDto>>(houseAnswerList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<HouseAnswerDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// 개인 마이페이지 - 집꾸 작성내역
	@GetMapping("/user/mypageUserHouseList")
	public ResponseEntity<Page<HouseDto>> houseListForUserMypage(
			Authentication authentication,
			@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
			@RequestParam(required = false, defaultValue = "10", value = "size") int size) {		
		try {
			UUID userId = ((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId();
			Page<HouseDto> houseList = houseService.houseListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<HouseDto>>(houseList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<HouseDto>>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
}
