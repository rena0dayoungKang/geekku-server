package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.InteriorDto;
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
	public ResponseEntity<Map<String,Object>> interioMain() {
		try {
			List<InteriorSample> sampleList = interiorService.sampleListForMain();
			List<InteriorDto> interiorList = interiorService.interiorListForMain();
			Map<String,Object> listInfo = new HashMap<>();
			listInfo.put("sampleList", sampleList);
			listInfo.put("interiorList", interiorList);
			return new ResponseEntity<Map<String,Object>>(listInfo,HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
