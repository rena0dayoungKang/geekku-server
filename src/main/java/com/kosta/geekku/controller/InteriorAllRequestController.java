package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.service.InteriorAllRequestService;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorAllRequestController {
	private final InteriorAllRequestService interiorAllService;
	private final HttpSession session;

	@PostMapping("/interiorAllWrite")
	public ResponseEntity<String> makeAccount(@RequestBody InteriorAllDto interiorAllDto) {
		try {
			interiorAllService.interiorAllWrite(interiorAllDto);
			return new ResponseEntity<String>("true", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

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
			//System.out.println(interiorAllList);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("interiorAllList", interiorAllList);
			listInfo.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
