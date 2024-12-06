package com.kosta.geekku.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.AuthDto;
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
	
	@PostMapping("/sendPwdReset")
	public ResponseEntity<String> snedPwdResetUrl(@RequestParam String username, @RequestParam String email) {
		try {
			authService.sendPwdResetUrl(username, email);
			return new ResponseEntity<String>("비밀번호 재설정 링크를 이메일로 발송", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("재설정 이메일 발송 실패", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/resetPwd")
	public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody AuthDto authDto) {
		try {
			Map<String, Object> resMap = authService.resetPwd(authDto);
			return new ResponseEntity<Map<String,Object>>(resMap, HttpStatus.OK);			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
