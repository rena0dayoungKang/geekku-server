package com.kosta.geekku.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;
import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.Role;
import com.kosta.geekku.service.BookmarkService;
import com.kosta.geekku.service.UserService;
import com.kosta.geekku.util.PageInfo;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login") // Login.js
	public ResponseEntity<UserDto> login(@RequestBody Map<String, String> param) {
		try {
			UserDto userDto = userService.login(param.get("username"), param.get("password"));
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/joinPerson") // JoinPerson.js
	public ResponseEntity<String> joinPeron(@RequestBody UserDto userDto) {
		try {
			userDto.setRole(Role.ROLE_USER);
			String rawPassword = userDto.getPassword();
			userDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));
			userService.joinPerson(userDto);
			return new ResponseEntity<String>("개인 회원가입 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("회원가입 실패", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/userInfo")
	public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
		String username = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUsername();
		try {
			UserDto userDto = userService.getUser(username);
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/user/updateUserInfo")
	public ResponseEntity<String> updateUserInfo(Authentication authentication, @RequestBody UserDto userDto) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId(); // 토큰에서 UUID를 추출
			userService.updateUserInfo(userId, userDto);
			return new ResponseEntity<String>("회원정보 수정 완료", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("회원정보 수정 실패", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/mypagebookmark")
	public ResponseEntity<Map<String, Object>> myEstateBookmarkList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("userId") String userId) {
		try {
			System.out.println(userId);
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			System.out.println("page" + page);
			Slice<EstateBookMarkDto> myEstateBookmarkList = bookmarkService.mypageEstatebookmarkList(page, userId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("myEstateBookmarkList", myEstateBookmarkList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/mypagebookmarkInterior")
	public ResponseEntity<Map<String, Object>> myInteriorBookmarkList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam("userId") String userId) {
		try {
			System.out.println(userId);
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			System.out.println("page" + page);
			Slice<InteriorBookMarkDto> myInteriorBookmarkList = bookmarkService.mypageInteriorbookmarkList(page,
					userId);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("myInteriorBookmarkList", myInteriorBookmarkList);
			listInfo.put("pageInfo", pageInfo);

			return new ResponseEntity<Map<String, Object>>(listInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
}
