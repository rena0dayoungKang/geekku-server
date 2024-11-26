package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.service.CommunityService;
import com.kosta.geekku.service.EstateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {
	
	private final EstateService estateService;
	private final CommunityService communityService;
	
	@GetMapping("/listForMain")
	public ResponseEntity<Map<Object, Object>> estateListForMain() {
		try {
			List<EstateDto> estateList = estateService.estateListForMain();
			List<CommunityDto> communityList = communityService.getCommunityListForMain();
			Map<Object, Object> listInfo = new HashMap<>();
			listInfo.put("estateList", estateList);
			listInfo.put("communityList", communityList);
			
			return new ResponseEntity<Map<Object, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<Object, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
