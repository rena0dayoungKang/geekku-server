package com.kosta.geekku.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.CommunityBookmarkDto;
import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;
import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Role;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.service.BookmarkService;
import com.kosta.geekku.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody Map<String, String> param) {
		try {
			UserDto userDto = userService.login(param.get("username"), param.get("password"));
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
//	@PostMapping("/glogout")
//	public ResponseEntity<String> logout(Authentication authentication) {
//		System.out.println(authentication);
//		System.out.println("logout");
//		try {
//			User user = ((PrincipalDetails) authentication.getPrincipal()).getUser(); 
//			if(user!=null) {
//				userService.logout(user.getUserId());
//			} else {
//				Company company = ((PrincipalDetails) authentication.getPrincipal()).getCompany();
//				userService.logout(company.getCompanyId());
//			}
//			return new ResponseEntity<String>("true", HttpStatus.OK);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
//		}
//	}

	@PostMapping("/joinPerson")
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
	public ResponseEntity<Map<String, Object>> updateUserInfo(Authentication authentication, UserDto userDto,
			@RequestParam(name = "file", required = false) MultipartFile profile) {
		System.out.println("-----------개인정보수정");
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId(); // 토큰에서 UUID를 추출
			Map<String, Object> res = userService.updateUserInfo(userId, userDto, profile);
			return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/user/changePwd")
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

	@PostMapping("/findUserByEmail")
	public ResponseEntity<List<UserDto>> findIdByEmail(@RequestBody Map<String, String> param) {
		try {
			String email = param.get("email");
			List<UserDto> userDtoList = userService.findIdByEmail(email);
			return new ResponseEntity<List<UserDto>>(userDtoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<UserDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
    @PostMapping("/findUserByPhone")
    public ResponseEntity<List<UserDto>> findUserByPhone(@RequestBody Map<String, String> param) {
        try {
            String phone = param.get("phone");
            List<UserDto> userDtoList = userService.findIdByPhone(phone);
            return new ResponseEntity<List<UserDto>>(userDtoList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<UserDto>>(HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<Boolean> checkDoubleId(@RequestParam String username) {
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
	@GetMapping("/user/mypagebookmarkEstate")
	public ResponseEntity<Slice<EstateBookMarkDto>> myEstateBookmarkList(Authentication authentication,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();
			Slice<EstateBookMarkDto> myEstateBookmarkList = bookmarkService.mypageEstatebookmarkList(page, userId);

			return new ResponseEntity<Slice<EstateBookMarkDto>>(myEstateBookmarkList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Slice<EstateBookMarkDto>>(HttpStatus.BAD_REQUEST);
		}
	}

	// 개인회원 마이페이지 - 인테리어 업체 북마크 내역
	@GetMapping("/user/mypagebookmarkInterior")
	public ResponseEntity<Slice<InteriorBookMarkDto>> myInteriorBookmarkList(Authentication authentication,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();
			Slice<InteriorBookMarkDto> myInteriorBookmarkList = bookmarkService.mypageInteriorbookmarkList(page, userId);

			return new ResponseEntity<Slice<InteriorBookMarkDto>>(myInteriorBookmarkList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Slice<InteriorBookMarkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// 개인회원 마이페이지 - 집들이 북마크 내역
	@GetMapping("/user/mypagebookmarkCommunity")
	public ResponseEntity<Slice<CommunityBookmarkDto>> myCommunityBookmarkList(Authentication authentication,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		try {
			UUID userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId();
			Slice<CommunityBookmarkDto> myCommunityBookmarkList = bookmarkService.mypageCommunitybookmarkList(page, userId);

			return new ResponseEntity<Slice<CommunityBookmarkDto>>(myCommunityBookmarkList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Slice<CommunityBookmarkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}
