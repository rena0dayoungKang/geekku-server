package com.kosta.geekku.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SmsController {
	private final AuthService authService;

	@PostMapping("/sendSms")
	public ResponseEntity<String> sendSms(@RequestParam String phone) {
		try {
			boolean isSent = authService.sendSms(phone);
			if (isSent) {
				return new ResponseEntity<String>("SMS 인증번호가 발송되었습니다.", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("SMS 발송에 실패했습니다.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	// SMS 인증번호 확인
	@PostMapping("/verifySms")
	public ResponseEntity<String> verifySms(@RequestParam String phone, @RequestParam int certificationCode) {
		try {
			boolean isVerified = authService.verifySms(phone, certificationCode);
			if (isVerified) {
				return new ResponseEntity<>("SMS 인증이 완료되었습니다.", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("유효하지 않은 인증번호이거나 만료되었습니다.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
