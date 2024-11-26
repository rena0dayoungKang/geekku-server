package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.service.InteriorAllRequestService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorAllRequestController {
	private final InteriorAllRequestService interiorAllService;
	private final HttpSession session;

	@PostMapping("/interiorAllWrite")
	public ResponseEntity<String> interiorAllWrite(@RequestBody InteriorAllDto interiorAllDto) {
		try {
			interiorAllService.interiorAllWrite(interiorAllDto);
			return new ResponseEntity<String>("true", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * @PostMapping("/interiorAllModify") public ResponseEntity<Integer>
	 * onestopModify(InteriorAllDto interiorAllDto,
	 * 
	 * @RequestPart(value = "delFile", required = false) Integer[] delFileNum,
	 * 
	 * @RequestPart(value = "file", required = false) MultipartFile[] fileList) {
	 * try { interiorAllService.interiorAllModify(interiorAllDto); return new
	 * ResponseEntity<Integer>(interiorAllDto.getRequestAllNum(), HttpStatus.OK); }
	 * catch (Exception e) { e.printStackTrace(); return new
	 * ResponseEntity<Integer>(HttpStatus.BAD_REQUEST); } }
	 */

	@GetMapping("/interiorAllDetail/{num}")
	public ResponseEntity<Map<String, Object>> interiorAllDetail(@PathVariable Integer num) {
		try {
			Map<String, Object> res = new HashMap<>();
			System.out.println("controller" + num);
			InteriorAllDto interiorAllDto = interiorAllService.interiorDetail(num);
			// boolean heart = interiorallService.checkHeart(boardDto.getWriter(), num) !=
			// null;
			res.put("interiorAll", interiorAllDto);
			// res.put("heart", heart);

			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorAllList")
	public ResponseEntity<Map<String, Object>> interiorAllList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keyword", required = false) String word) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<InteriorAllDto> interiorAllList = interiorAllService.interiorAllList(pageInfo, type, word);
			// System.out.println(interiorAllList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorAllList", interiorAllList);
			listInfo.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorAllDelete/{num}")
	public ResponseEntity<String> onestopDelete(@PathVariable Integer num) {
		try {
			interiorAllService.interiorAllDelete(num);
			return new ResponseEntity<String>(String.valueOf(true), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	// 방꾸 답변
	@PostMapping("/interiorAnswerWrite")
	public ResponseEntity<String> interiorAnswerWrite(InteriorAnswerDto interiorAnswerDto,
			@RequestParam Integer requestAllNum) {
		try {
			Integer interiorAnswerNum = interiorAllService.interiorAnswerWrite(interiorAnswerDto, requestAllNum);
			return new ResponseEntity<String>(String.valueOf(interiorAnswerNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("방꾸답변 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/interiorAnswerList")
	public ResponseEntity<Map<String, Object>> interiorAnswerList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("requestAllNum") Integer requestAllNum) {
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<InteriorAnswerDto> interiorAnswerList = interiorAllService.interiorAnswerList(pageInfo, requestAllNum);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorAnswerList", interiorAnswerList);
			listInfo.put("pageInfo", pageInfo);
			System.out.println(interiorAnswerList);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/interiorAnswerDelete")
	public ResponseEntity<String> interiorAnswerDelete(@RequestParam("answerAllNum") Integer answerAllNum,
			@RequestParam("requestAllNum") Integer requestAllNum) {
		try {
			interiorAllService.interiorAnswerDelete(answerAllNum, requestAllNum);
			System.out.println(answerAllNum);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("집꾸답변 삭제 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/mypageUserInteriorAllList")
	public ResponseEntity<Page<InteriorAllDto>> interiorAllRequestListForUserMypage(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
			@RequestParam(required = false, defaultValue = "10", value = "size") int size, 
			@RequestParam("userId") String userId) {
		try {
			Page<InteriorAllDto> interiorAllList = interiorAllService.interiorAllListForUserMypage(page, size, userId);
			return new ResponseEntity<Page<InteriorAllDto>>(interiorAllList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<InteriorAllDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}
