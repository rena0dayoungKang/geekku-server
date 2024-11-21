package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.service.InteriorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteriorUserController {

	private final InteriorService interiorService;
	private final HttpSession session;

	@GetMapping("/interiorCompanyDetail/{num}")
	public ResponseEntity<Map<String, Object>> onestopDetail(@PathVariable Integer num) {
		try {
			Map<String, Object> res = new HashMap<>();
			System.out.println("controller" + num);
			InteriorDto interiorDto = interiorService.interiorCompanyDetail(num);
			System.out.println(num);
			// boolean heart = onestopService.checkHeart(boardDto.getWriter(), num) != null;
			res.put("interior", interiorDto);
			// res.put("heart", heart);

			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
