package com.kosta.geekku.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.Role;
import com.kosta.geekku.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping("/login")   //Login.js
	public ResponseEntity<UserDto> login(@RequestBody Map<String, String> param) {
		try {
			UserDto userDto = userService.login(param.get("username"), param.get("password"));
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/joinPerson")  //JoinPerson.js
	public ResponseEntity<String> joinPeron(@RequestBody UserDto userDto) {
		try {
			userDto.setRole(Role.ROLE_USER);
			String rawPassword = userDto.getPassword();
			userDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));
			userService.joinPerson(userDto);
			return new ResponseEntity<String>("개인 회원가입 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("회원가입 실패",HttpStatus.BAD_REQUEST);
		}
	}
	

}
