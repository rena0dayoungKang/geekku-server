package com.kosta.geekku.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/sendVerificationCode")
	public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
		try {
			authService.sendVerificationCode(email);
			return new ResponseEntity<String>("인증메일 발송 완료", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/verifyCode")
	public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam int certificationNum) {
		try {			
			boolean isVerified = authService.verifyCode(email, certificationNum);
			if (isVerified) {
				return new ResponseEntity<String>("인증 완료", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("인증번호 불일치", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
