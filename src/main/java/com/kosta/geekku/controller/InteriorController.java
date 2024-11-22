package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.service.InteriorService;
import com.kosta.geekku.util.PageInfo;

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
			List<InteriorSample> sampleList = interiorService.sampleListForMain();
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

	@GetMapping("/interiorBookmark/{num}")
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

}
