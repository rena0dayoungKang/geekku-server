package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.service.HouseService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HouseController {

	private final HouseService houseService;

	@PostMapping("/houseWrite")
	public ResponseEntity<String> houseWrite(HouseDto houseDto) {
		try {
			Integer houseNum = houseService.houseWrite(houseDto);
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

	@PostMapping("/houseDelete/{houseNum}")
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
	@PostMapping("/houseAnswerWrite")
	public ResponseEntity<String> houseAnswerWrite(HouseAnswerDto houseAnswerDto, @RequestParam Integer houseNum) {
		try {
			Integer houseAnswerNum = houseService.houseAnswerWrite(houseAnswerDto, houseNum);
			return new ResponseEntity<String>(String.valueOf(houseAnswerNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸답변 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/houseAnswerList")
	public ResponseEntity<Map<String, Object>> houseAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("houseNum") Integer houseNum) {
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

	@PostMapping("/houseAnswerDelete")
	public ResponseEntity<String> houseAnswerDelete(@RequestParam("houseAnswerNum") Integer houseAnswerNum,
			@RequestParam("houseNum") Integer houseNum) {
		try {
			houseService.houseAnswerDelete(houseAnswerNum, houseNum);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸답변 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/mypageHouseAnswerList")
	public ResponseEntity<Page<HouseAnswerDto>> houseAnswerListForMypage(
			 @RequestParam(required = false, defaultValue = "1", value = "page") int page,  
			 @RequestParam(required = false, defaultValue = "10", value = "size") int size, 
			 @RequestParam("companyId") String companyId) {
		try {
			Page<HouseAnswerDto> houseAnswerList = houseService.houseAnswerListForMypage(page, size, companyId);
			return new ResponseEntity<Page<HouseAnswerDto>>(houseAnswerList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<HouseAnswerDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/mypageUserHouseList")
	public ResponseEntity<Page<HouseDto>> houseListForUserMypage(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
			@RequestParam(required = false, defaultValue = "10", value = "size") int size, 
			@RequestParam("userId") String userId) {
		try {
			Page<HouseDto> houseList = houseService.houseListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<HouseDto>>(houseList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<HouseDto>>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
}
