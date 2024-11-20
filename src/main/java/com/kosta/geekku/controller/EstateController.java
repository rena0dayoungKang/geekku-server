package com.kosta.geekku.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.service.EstateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EstateController {
	
	private final EstateService estateService; 

	@Value("${upload.path}")
	private String uploadPath;

	@PostMapping("/estateWrite")
	public ResponseEntity<String> estateWrite(EstateDto estateDto, 
			@RequestParam(name="images", required=false) MultipartFile[] images) {
		try {
			Integer estateNum = estateService.estateWrite(estateDto, images == null ? null : Arrays.asList(images));
			return new ResponseEntity<String>(String.valueOf(estateNum), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("매물 등록 오류", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/estateDetail/{num}")
	public ResponseEntity<EstateDto> estateDetail(@PathVariable Integer num) {
		try {
			//Integer estateNum = Integer.parseInt(param.get("estateNum"));
//			Map<String, Object> res = new HashMap<>();
			EstateDto estateDto = estateService.estateDetail(num);
//			res.put("estate", estateDto);
			
//			북마크
//			if (param.get("id") != null) {
//				
//			}
			
			return new ResponseEntity<EstateDto>(estateDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<EstateDto>(HttpStatus.BAD_REQUEST);
		}
	}
}
