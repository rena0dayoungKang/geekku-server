package com.kosta.geekku.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
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
import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.service.EstateService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EstateController {

	private final EstateService estateService;

	@Value("${upload.path}")
	private String uploadPath;

	@PostMapping("/company/estateWrite")
	public ResponseEntity<String> estateWrite(Authentication authentication, EstateDto estateDto,
			@RequestPart(name = "images", required = false) MultipartFile[] images) {
		try {
			UUID companyId = ((PrincipalDetails)authentication.getPrincipal()).getCompany().getCompanyId();
			Integer estateNum = estateService.estateWrite(estateDto, images == null ? null : Arrays.asList(images), companyId);
			return new ResponseEntity<String>(String.valueOf(estateNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("매물 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/estateImage/{num}")
	public void image(@PathVariable String num, HttpServletResponse response) {
		try {
			// 파일이 존재하지 않는 경우 처리
			File file = new File(uploadPath, num);
			if (!file.exists()) {
				System.out.println("파일 존재하지 않음");
				return;
			}
			InputStream ins = new FileInputStream(file);
//			InputStream ins = new FileInputStream(new File(uploadPath, num));
			FileCopyUtils.copy(ins, response.getOutputStream());
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/estateDetail")
	public ResponseEntity<Map<String, Object>> estateDetail(@RequestBody Map<String, Object> params){
		try {
			Integer estateNum = (Integer)params.get("estateNum");

			Map<String, Object> res = new HashMap<>();
			EstateDto estateDto = estateService.estateDetail(estateNum);
			res.put("estate", estateDto);
			
			//북마크
			if (params.get("userId") != null) {
				boolean bookmark = estateService.checkBookmark((String)params.get("userId"), estateNum) != null;
				res.put("bookmark", bookmark);
			} 

			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> res = new HashMap<>();
			res.put("err", "매물 상세보기 오류");
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/estateList")
	public ResponseEntity<Map<String, Object>> estateList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keyword", required = false) String keyword) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<EstateDto> estateList = estateService.estateList(pageInfo, type, keyword);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("estateList", estateList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/estateListForMain")
	public ResponseEntity<List<EstateDto>> estateListForMain() {
		try {
			List<EstateDto> estateList = estateService.estateListForMain();
			return new ResponseEntity<List<EstateDto>>(estateList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<EstateDto>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/company/estateDelete")
	public ResponseEntity<String> estateDelete(@RequestParam Integer estateNum) {
		try {
			estateService.estateDelete(estateNum);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("매물 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
  
	@PostMapping("/user/estateBookmark/{estateNum}")
	public ResponseEntity<String> estateBookmark(Authentication authentication, @PathVariable Integer estateNum) {
		try {
			UUID userId = ((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId();
			boolean bookmark = estateService.toggleBookmark(userId.toString(), estateNum);
			return new ResponseEntity<String>(String.valueOf(bookmark), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("매물 북마크 실패", HttpStatus.BAD_REQUEST);
		}
	}

	// 중개업자 마이페이지 - 매물 등록 내역
	@GetMapping("/company/mypageEstateList")
	public ResponseEntity<Map<String, Object>> mypageEstateList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("companyId") String companyId) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<EstateDto> estateList = estateService.estateListForMypage(pageInfo, companyId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("estateList", estateList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

}
