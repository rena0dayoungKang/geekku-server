package com.kosta.geekku.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<String> joinPeron(@ModelAttribute UserDto userDto) {
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

	@PostMapping("/user/updateUserInfo")
	public ResponseEntity<Map<String, Object>> updateUserInfo(Authentication authentication,
			UserDto userDto,
			@RequestParam(name = "file", required = false) MultipartFile profile) {
		System.out.println("-----------");
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId(); // 토큰에서 UUID를 추출			
			Map<String, Object> res = userService.updateUserInfo(userId, userDto, profile);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/user/changePwd")
	public ResponseEntity<String> changePwd(Authentication authentication, @RequestBody Map<String, String> param) {
		try {
			String currentPassword = param.get("currentPassword");
			String newPassword = param.get("newPassword");

			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();

			UserDto userDto = userService.getUser(userId);
			if (!bCryptPasswordEncoder.matches(currentPassword, userDto.getPassword())) {
				return new ResponseEntity<>("현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
			}

			// 새 비밀번호
			String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
			userService.changePassword(userId, encodedNewPassword);

			return new ResponseEntity<>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("비밀번호 변경에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/findIdByPhone")
	public ResponseEntity<Map<String, String>> findIdByPhone(@RequestBody Map<String, String> param) {
		try {
			String phone = param.get("phone");
			UserDto userDto = userService.findIdByPhone(phone);

			String formatDate = formattedDate(userDto);

			Map<String, String> result = new HashMap<>();
			result.put("username", userDto.getUsername());
			result.put("createdAt", formatDate);
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, String>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/findIdByEmail")
	public ResponseEntity<Map<String, String>> findIdByEmail(@RequestBody Map<String, String> param) {
		try {
			String email = param.get("email");
			UserDto userDto = userService.findIdByEmail(email);

			String formatDate = formattedDate(userDto);

			Map<String, String> result = new HashMap<>();
			result.put("username", userDto.getUsername());
			result.put("createdAt", formatDate);
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, String>>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/checkNickname")
	public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
		try {
			boolean checkNickname = userService.checkDoubleNickname(nickname);
			System.out.println(checkNickname);
			return new ResponseEntity<Boolean>(checkNickname, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/checkDoubleId")
	public ResponseEntity<Boolean> checkDoubleId(@RequestParam String username){
		try {
			boolean checkDoubleId = userService.checkDoubleId(username);
			System.out.println(checkDoubleId);
			return new ResponseEntity<Boolean>(checkDoubleId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}

	private String formattedDate(UserDto userDto) {
		LocalDate createdAtDate = userDto.getCreatedAt().toLocalDateTime().toLocalDate();
		String formatDate = createdAtDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return formatDate;
	}

	// 개인회원 마이페이지 - 매물 북마크 내역
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
