package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Slice;
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

	@GetMapping("/houseDetail/{houseNum}")
	public ResponseEntity<HouseDto> houseDetail(@PathVariable Integer houseNum) {
		try {
			HouseDto houseDto = houseService.houseDetail(houseNum);
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
	public ResponseEntity<Slice<HouseAnswerDto>> houseAnswerListForMypage(
			 @RequestParam(required = false, defaultValue = "0", value = "page") int page, @RequestParam("companyId") String companyId) {
		try {
			Slice<HouseAnswerDto> houseAnswerList = houseService.houseAnswerListForMypage(page, companyId);
			return new ResponseEntity<Slice<HouseAnswerDto>>(houseAnswerList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Slice<HouseAnswerDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
